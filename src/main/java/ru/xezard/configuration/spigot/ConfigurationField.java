package ru.xezard.configuration.spigot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationField
{
    String value() default "";

    ConfigurationType type() default ConfigurationType.OBJECT;

    String[] comment() default {};
}