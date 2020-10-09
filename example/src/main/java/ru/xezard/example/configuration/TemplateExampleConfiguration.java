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
package ru.xezard.example.configuration;

import lombok.Getter;
import ru.xezard.configurations.Configuration;
import ru.xezard.configurations.ConfigurationField;
import ru.xezard.example.data.ExampleObject;

@Getter
public class TemplateExampleConfiguration
extends Configuration
{
    public TemplateExampleConfiguration(String pathToFile)
    {
        super(pathToFile);
    }

    @ConfigurationField("Complex-object")
    private ExampleObject object;
}