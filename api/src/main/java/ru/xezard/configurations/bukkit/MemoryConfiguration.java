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
package ru.xezard.configurations.bukkit;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class MemoryConfiguration
extends MemorySection
implements Configuration
{
    @Getter
    protected Configuration defaults;

    protected MemoryConfigurationOptions options;

    public MemoryConfiguration(Configuration defaults)
    {
        this.defaults = defaults;
    }

    @Override
    public void addDefault(String path, Object value)
    {
        Preconditions.checkNotNull(path, "Path may not be null");

        if (this.defaults == null)
        {
            this.defaults = new MemoryConfiguration();
        }

        this.defaults.set(path, value);
    }

    public void addDefaults(Map<String, Object> defaults)
    {
        Preconditions.checkNotNull(defaults, "Defaults may not be null");

        defaults.forEach(this::addDefault);
    }

    public void addDefaults(Configuration defaults)
    {
        Preconditions.checkNotNull(defaults, "Defaults may not be null");

        this.addDefaults(defaults.getValues(true));
    }

    public void setDefaults(Configuration defaults)
    {
        Preconditions.checkNotNull(defaults, "Defaults may not be null");

        this.defaults = defaults;
    }

    @Override
    public ConfigurationSection getParent()
    {
        return null;
    }

    public MemoryConfigurationOptions options()
    {
        if (this.options == null)
        {
            this.options = new MemoryConfigurationOptions(this);
        }

        return this.options;
    }
}