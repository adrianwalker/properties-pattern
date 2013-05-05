package org.adrianwalker.propertiespattern;

import java.util.Set;
import static org.adrianwalker.propertiespattern.HashMapProperties.PROTOTYPE;

public final class Example {

  public static void main(final String[] args) {

    /* 
     * create a prototype properties object with a greeting and a 
     * strategy which returns the greeting and an argument
     */
    Properties prototype = new HashMapProperties();
    prototype.put("greeting", "Hello");
    prototype.put("getGreeting", new Strategy() {
      @Override
      public String execute(final Properties properties, final Object... args) {

        return String.format("%s %s", properties.get("greeting"), args[0]);
      }
    });

    /* 
     * create a properties object which inherits the greeting and 
     * strategy from the prototype
     */
    HashMapProperties properties = new HashMapProperties();
    properties.put(PROTOTYPE, prototype);

    /* 
     * clone the properties object and override the greeting 
     * property
     */
    HashMapProperties clone = properties.clone();
    clone.put("greeting", "Ahoy");

    /*
     * filter for the getter properties, then execute the getter
     * on each properties object with a message argument and
     * print the message
     */
    Set<String> getters = clone.filter("get.*");
    for (String getter : getters) {
      System.out.println(properties.execute(getter, "World"));
      System.out.println(clone.execute(getter, "Sailor"));
    }
  }
}
