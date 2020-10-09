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

import lombok.experimental.UtilityClass;
import ru.xezard.configurations.Configuration;

import java.io.File;
import java.util.List;

@UtilityClass
public class ConfigurationsLoaders
{
    public <T extends Configuration> List<T> fromFolder(Class<T> templateTypeClass,
                                                        File folder, boolean deep)
    {
        return new FolderConfigurationsLoader<T> (deep).load(templateTypeClass, folder);
    }

    public <T extends Configuration> List<T> fromPath(Class<T> templateTypeClass,
                                                      String path, boolean deep)
    {
        return fromFolder(templateTypeClass, new File(path), deep);
    }
}