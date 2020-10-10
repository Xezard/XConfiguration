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

import ru.xezard.configurations.Configuration;
import ru.xezard.configurations.ConfigurationComments;
import ru.xezard.configurations.ConfigurationField;
import ru.xezard.example.data.ExampleEnum;
import ru.xezard.example.data.ExampleObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationComments({"test", "kek"})
public class ExampleConfiguration
extends Configuration
{
    public ExampleConfiguration(File folder)
    {
        super(folder.getAbsolutePath() + File.separator + "example.yml");
    }

    @ConfigurationField("Maps.Enum-key")
    private Map<ExampleEnum, String> exampleEnumStringMap = new HashMap<ExampleEnum, String> ()
    {{
        this.put(ExampleEnum.EXAMPLE, "test");
        this.put(ExampleEnum.ANOTHER_EXAMPLE, "another test");
    }};

    @ConfigurationField("Maps.Integer-key")
    private Map<Integer, String> exampleIntegerStringMap = new HashMap<Integer, String> ()
    {{
        this.put(1, "test");
        this.put(2, "another test");
    }};

    @ConfigurationField("Maps.Simple")
    private Map<String, String> exampleStringStringMap = new HashMap<String, String> ()
    {{
        this.put("test", "test");
        this.put("another test", "another test");
    }};

    @ConfigurationField("List.Complex-objects")
    private List<ExampleObject> objects = Arrays.asList
    (
            ExampleObject.builder().id(2).name("test!").build(),
            ExampleObject.builder().id(3).name("test?").build()
    );

    @ConfigurationField("List.String")
    private List<String> strings = Arrays.asList("test", "another test");

    @ConfigurationField("List.Short")
    private List<Short> shorts = Arrays.asList((short) 1.0, (short) 2.0);

    @ConfigurationField("List.Long")
    private List<Long> longs = Arrays.asList((long) 1.0, (long) 2.0);

    @ConfigurationField("List.Integer")
    private List<Integer> integers = Arrays.asList(1, 1);

    @ConfigurationField("List.Float")
    private List<Float> floats = Arrays.asList((float) 1.0, (float) 1.5);

    @ConfigurationField("List.Enum")
    private List<ExampleEnum> enums = Arrays.asList(ExampleEnum.EXAMPLE, ExampleEnum.ANOTHER_EXAMPLE);

    @ConfigurationField("List.Double")
    private List<Double> doubles = Arrays.asList(1.2, 2.3);

    @ConfigurationField("List.Character")
    private List<Character> characters = Arrays.asList('a', 'b');

    @ConfigurationField("List.Byte")
    private List<Byte> bytes = Arrays.asList((byte) 0, (byte) 1);

    @ConfigurationField("List.Boolean")
    private List<Boolean> booleans = Arrays.asList(true, false);

    @ConfigurationField("Complex-object")
    private ExampleObject exampleObject =
            ExampleObject.builder()
                         .id(1)
                         .name("test")
                         .build();

    @ConfigurationField("Base-types.Boolean")
    private boolean exampleBoolean = true;

    @ConfigurationField("Base-types.Byte")
    private byte exampleByte = 127;

    @ConfigurationField("Base-types.Character")
    private char exampleCharacter = 'c';

    @ConfigurationField("Base-types.Double")
    private double exampleDouble = 25.5;

    @ConfigurationField("Base-types.Enum")
    private ExampleEnum exampleEnum = ExampleEnum.EXAMPLE;

    @ConfigurationField("Base-types.Float")
    private float exampleFloat = 255555;

    @ConfigurationField("Base-types.Integer")
    private int exampleInteger = 5;

    @ConfigurationField("Base-types.Long")
    private long exampleLong = 42142;

    @ConfigurationField("Base-types.Short")
    private short exampleShort = 512;

    @ConfigurationField("Base-types.String")
    @ConfigurationComments({"# comments test", "# new line of comments test"})
    private String exampleString = "Test string";
}
