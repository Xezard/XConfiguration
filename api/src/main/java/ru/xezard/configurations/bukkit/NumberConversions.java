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

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberConversions
{
    public int toInt(Object object)
    {
        if (object instanceof Number)
        {
            return ((Number) object).intValue();
        }

        try {
            return Integer.parseInt(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {}

        return 0;
    }

    public float toFloat(Object object)
    {
        if (object instanceof Number)
        {
            return ((Number) object).floatValue();
        }

        try {
            return Float.parseFloat(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {}

        return 0;
    }

    public short toShort(Object object)
    {
        if (object instanceof Number)
        {
            return ((Number) object).shortValue();
        }

        try {
            return Short.parseShort(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {}

        return 0;
    }

    public double toDouble(Object object)
    {
        if (object instanceof Number)
        {
            return ((Number) object).doubleValue();
        }

        try {
            return Double.parseDouble(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {}

        return 0;
    }

    public long toLong(Object object)
    {
        if (object instanceof Number)
        {
            return ((Number) object).longValue();
        }

        try {
            return Long.parseLong(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {}

        return 0;
    }

    public byte toByte(Object object)
    {
        if (object instanceof Number)
        {
            return ((Number) object).byteValue();
        }

        try {
            return Byte.parseByte(object.toString());
        } catch (NumberFormatException | NullPointerException ignored) {}

        return 0;
    }
}