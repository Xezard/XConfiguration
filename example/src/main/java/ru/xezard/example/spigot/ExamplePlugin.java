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
package ru.xezard.example.spigot;

import org.bukkit.plugin.java.JavaPlugin;
import ru.xezard.configurations.bukkit.serialization.ConfigurationSerialization;
import ru.xezard.configurations.loaders.ConfigurationsLoaders;
import ru.xezard.example.configuration.ExampleConfiguration;
import ru.xezard.example.configuration.TemplateExampleConfiguration;
import ru.xezard.example.data.ExampleObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExamplePlugin
extends JavaPlugin
{
    private ExampleConfiguration configuration = new ExampleConfiguration(this.getDataFolder());

    private List<ExampleObject> objects = new ArrayList<> ();

    static
    {
        ConfigurationSerialization.registerClass(ExampleObject.class);
    }

    @Override
    public void onEnable()
    {
        this.configuration.load();

        List<TemplateExampleConfiguration> templateExampleConfigurations = ConfigurationsLoaders.fromPath
        (
                TemplateExampleConfiguration.class,
                this.getDataFolder() + File.separator + "test",
                true
        );

        templateExampleConfigurations.forEach((template) -> this.objects.add(template.getObject()));

        System.out.println(this.objects);
    }

    @Override
    public void onDisable()
    {
        this.configuration = null;
    }
}