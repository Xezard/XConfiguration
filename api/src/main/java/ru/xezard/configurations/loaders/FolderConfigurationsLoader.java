/*
 *  This file is part of XConfiguration,
 *  licensed under the GNU General Public License v3.0.
 *
 *  Copyright (c) Xezard (Zotov Ivan)
 *
 *  XConfiguration is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  XConfiguration is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with XConfiguration. If not, see <https://www.gnu.org/licenses/>.
 */
package ru.xezard.configurations.loaders;

import lombok.AllArgsConstructor;
import ru.xezard.configurations.Configuration;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class FolderConfigurationsLoader<T extends Configuration>
implements IConfigurationLoader<T, File>
{
    private boolean deep;

    @Override
    @SuppressWarnings("unchecked")
    public List<T> load(Class<T> templateClass, File folder)
    {
        List<T> loadedConfigurations = new ArrayList<> ();

        if (folder == null || folder.isFile())
        {
            return loadedConfigurations;
        }

        File[] files = folder.listFiles();

        if (files == null || files.length < 1)
        {
            return loadedConfigurations;
        }

        for (File file : files)
        {
            if (file.isDirectory() && this.deep)
            {
                loadedConfigurations.addAll(this.load(templateClass, file));
                continue;
            }

            if (!file.getName().endsWith(".yml"))
            {
                continue;
            }

            T template = null;

            try {
                template = (T) templateClass.getConstructors()[0].newInstance(file.getAbsolutePath());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            if (template == null)
            {
                continue;
            }

            template.load();

            loadedConfigurations.add(template);
        }

        return loadedConfigurations;
    }
}