# XConfiguration

This project is a continuation of the [ez-cfg](https://github.com/JarvisCraft/ez-cfg "ez-cfg") project of the user [PROgrm_JARvis](https://github.com/JarvisCraft "PROgrm_JARvis"). 
It is a convenient library for interacting with the Bukkit / Spigot configuration.

## Usage
First of all, we need to create our configuration class:
```java
public class MessagesConfiguration
extends Configuration
{
    public MessagesConfiguration(Plugin plugin, String configurationName)
    {
        super(plugin, configurationName);
    }
	
    @Setter
    @Getter
    @ConfigurationField
    public String helloWorld = "Hello, world!";
}
```

For example, we can display hello world message from configuration:

```java
public class Main
extends JavaPlugin 
{
    private MessagesConfiguration messagesConfiguration = new MessagesConfiguration(this, "messages.yml");

    @Override
    public void onEnable() 
    {
        this.messagesConfiguration.load();
		
	this.getLogger().info(this.messagesConfiguration.getHelloWorld());
    }
	
    @Override
    public void onDisable() 
    {
	this.messagesConfiguration = null;
    }
}
```
After loading the plugin, the messages.yml file is generated automatically, together with the default values, are specified in the configuration class. If messages.yml has already been generated, the field in the class will contain the value from messages.yml.

You can also easily create your own field types for configurations.
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
    public MessagesConfiguration(Plugin plugin, String configurationName)
    {
        super(plugin, configurationName);
    }
	
    @ConfigurationField
    public String helloWorld = "Hello, world!";
	
    @ConfigurationField
    public POJO = new POJO("pojo", 1);
}
```
For more specific cases, you can create a specific ConfigurationData class for your object and specify how it should be serialized there:
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

    public static POJO deserialize(String string)
    {
	String[] parts = string.split(":");
		
	return new POJO(parts[0], Integer.parseInt(parts[1]));
    }
}
```
```java
public class ConfigurationDataPOJO
extends ConfigurationData<POJO>
{
    @Override
    public void set(FileConfiguration configuration, String path, POJO pojo)
    {
        configuration.set(path, pojo.serialize());
    }

    @Override
    public POJO get(FileConfiguration configuration, Class<POJO> type, String path)
    {
        return POJO.deserialize(configuration.getString(path));
    }

    @Override
    public POJO get(FileConfiguration configuration, Class<POJO> type, String path, POJO default)
    {
        Object value = configuration.get(path);

        return value instanceof String ? POJO.deserialize((String) value) : default;
    }

    @Override
    public boolean isValid(FileConfiguration configuration, String path)
    {
        return configuration.isString(path);
    }

    @Override
    public Time getDefault()
    {
        return new POJO("pojo", 1);
    }
}
```
Don't forget to register your ConfigurationType class:
```java
@Getter
public enum ConfigurationType
{
    ...
    POJO(new ConfigurationDataPOJO(), POJO.class);
}
```

## Notes
- This project is still in develpment stage.
- Comments in the configuration file are not currently supported.
