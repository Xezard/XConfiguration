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
package ru.xezard.configurations;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Deque;

public class YamlEffectiveModel
{
    private Deque<String> keys = new ArrayDeque<> ();

    @Setter
    @Getter
    private long currentIndent;

    public String getCurrentPath()
    {
        return String.join(".", this.keys);
    }

    public String toKey(String line)
    {
        return line.split(":")[0].trim();
    }

    public long getIndent(String line)
    {
        int indent = 0;

        for (char c : line.toCharArray())
        {
            if (!Character.isWhitespace(c))
            {
                return indent;
            }

            indent++;
        }

        return indent;
    }

    public int size()
    {
        return this.keys.size();
    }

    public boolean isKey(String line)
    {
        return line.contains(":");
    }

    public void addKey(String key)
    {
        this.keys.addLast(key);
    }

    public void replaceCurrentKey(String newKey)
    {
        this.keys.removeLast();
        this.keys.addLast(newKey);
    }

    public void removeKeys(long amount)
    {
        for (int i = 0; i < amount; i++)
        {
            this.keys.removeLast();
        }
    }
}