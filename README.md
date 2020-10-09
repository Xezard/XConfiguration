# XConfiguration

[![GitHub](https://img.shields.io/github/license/xezard/XConfiguration)](https://github.com/Xezard/XConfiguration/blob/master/LICENSE) [![](https://jitpack.io/v/Xezard/XConfiguration.svg)](https://jitpack.io/#Xezard/XConfiguration)

XConfiguration is a convenient library for interacting with the Bukkit / Spigot configuration.

* Supported Java version: 8+
* Tested on spigot version: 1.16.4
* Tested on bungeecord

## Getting started

To get a XConfiguration into your build:

* **Gradle**:
First include jitpack repository:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```
And then add XConfiguration library to the dependency:
```groovy
dependencies {
    implementation 'com.github.Xezard:XConfiguration:1.0-RELEASE'
}
```
* **Maven**:
First include jitpack repository:
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
And then add XConfiguration library to the dependency:
```xml
<dependency>
    <groupId>com.github.Xezard</groupId>
    <artifactId>XConfiguration</artifactId>
    <version>2.0.0</version>
</dependency>
```

## Usage
Consider the following example:
```java
@Getter
public class MessagesConfiguration
extends Configuration
{
    public MessagesConfiguration(File folder)
    {
        super(folder.getAbsolutePath() + File.separator + "messages.yml");
    }
	
    @ConfigurationField("Hello-world")
    public String helloWorld = "Hello, world!";
}
```

First of all, we need to create our configuration class.
This class must be inherited from ru.xezard.configurations.Configuration.
In the superclass constructor, you need to pass the path to file and its name for the configuration.
Inside a class, all declared fields annotated with @ConfigurationField are considered configuration fields. The @ConfigurationField annotation value is the path for the field value in the configuration.

In the main class of the plugin:
```java
public class Main
extends JavaPlugin 
{
    private MessagesConfiguration messagesConfiguration = new MessagesConfiguration(this.getDataFolder());

    @Override
    public void onEnable() 
    {
    	/* 
	 * This method automatically creates a configuration file 
	 * if it has not already been created. Also, it fills your class 
	 * with values from the configuration, or vice versa fills the 
	 * configuration with values from the class if there are no 
	 * corresponding fields in the configuration.
	 *
	 * You can also call this method when you want to reload 
	 * the configuration of your plugin.
	 */
        this.messagesConfiguration.load();
		
	/*
	 * After loading the configuration, we can easily get
	 * the values of our fields from the class.
	 */
	this.getLogger().info(this.messagesConfiguration.getHelloWorld());
    }
	
    @Override
    public void onDisable() 
    {
	this.messagesConfiguration = null;
    }
}
```
You can also specify comments for your fields in the configuration:

```java
@Getter
public class MessagesConfiguration
extends Configuration
{
    public MessagesConfiguration(Plugin plugin)
    {
    	super(plugin, "messages.yml");
    }
	
    @ConfigurationField("Hello-world.With-comment-above")
    @ConfigurationComments({"# First comment line", "# Second comment line"})
    public String helloWorld = "Hello, world!";
}
```
After calling the `load()` method, you will see the following configuration:

```yaml
# First comment line
# Second comment line
Hello-world:
  With-comment-above: Hello, world!
```
Also you can easily create your own field types for configurations.
For example:
```java
@Data
@AllArgsConstructor
@SerializableAs("Pojo")
public class POJO
implements ConfigurationSerializable
{
    private String name;
	
    private int id;
	
    @Override
    public Map<String, Object> serialize()
    {
	return Map.of("Name", this.name, "Id", this.id);
    }

    public static POJO deserialize(Map<String, Object> serialized)
    {
        return new POJO((String) serialized.get("Name"), (int) serialized.get("Id"));
    }
}
```
And then:
```java

@Setter
@Getter
public class MessagesConfiguration
extends Configuration
{
    public MessagesConfiguration(Plugin plugin)
    {
        super(plugin, "messages.yml");
    }
	
    @ConfigurationField("POJO")
    public POJO pojo = new POJO("pojo", 1);
}
```
For more specific cases, you can create a ConfigurationData class for your object and specify how it should be serialized there:
```java
@Data
@AllArgsConstructor
public class POJO
{
    private String name;
	
    private int id;
	
    public String serialize()
    {
	return this.name + ":" + this.id;
    }

    public static POJO deserialize(String serialized)
    {
	String[] parts = string.split(":");
		
	return new POJO(parts[0], Integer.parseInt(parts[1]));
    }
}
```
```java
@EqualsAndHashCode(callSuper = true)
public class ConfigurationDataPOJO
extends AbstractConfigurationData<POJO>
{
    public ConfigurationDataPOJO()
    {
        super(POJO.class);
    }

    @Override
    public void set(FileConfiguration configuration, String path, ExampleObject value, Field field) 
    {
        configuration.set(path, value.serialize());
    }

    @Override
    public POJO get(FileConfiguration configuration, String path, Field field)
    {
        // You must return null if path is not set in the configuration
        return configuration.isSet(path) ? POJO.deserialize(configuration.getString(path)) : null;
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isString(path);
    }
}
```

Don't forget to register your custom ConfigurationData class:
```java
public class Main
extends JavaPlugin 
{
    private MessagesConfiguration messagesConfiguration = new MessagesConfiguration(this);
	
    private ConfigurationDataPOJO configurationDataPOJO = new ConfigurationDataPOJO();

    @Override
    public void onEnable() 
    {
	ConfigurationsDataManager.getInstance().register(configurationDataPOJO);
		
        this.messagesConfiguration.load();

	this.getLogger().info(this.messagesConfiguration.getPojo());
    }
	
    @Override
    public void onDisable() 
    {
	ConfigurationManager.unregister(configurationDataPOJO);
	
	this.messagesConfiguration = null;
    }
}
```

## Bugs and Feedback

For bugs, questions and discussions please use the [Github Issues](https://github.com/Xezard/XConfiguration/issues).

## License
XConfiguration is licensed under the Apache 2.0 License. Please see [LICENSE](https://github.com/Xezard/XConfiguration/blob/master/LICENSE "LICENSE") for more info.
