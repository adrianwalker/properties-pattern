package org.adrianwalker.propertiespattern;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class HashMapProperties<V, T, A> implements
        Properties<String, V>,
        ExecutableProperites<String, V, T, A>,
        CloneableProperties<String, V>,
        FilterableProperties<String, V> {

  public static final String PROTOTYPE = "prototype";
  private final Map<String, V> properties;

  public HashMapProperties() {

    this.properties = Collections.synchronizedMap(new HashMap());
  }

  private HashMapProperties(final Map<String, V> map) {

    this.properties = Collections.synchronizedMap(new HashMap<String, V>(map));
  }

  @Override
  public V get(final String name) {

    if (properties.containsKey(name)) {

      return properties.get(name);
    }

    if (properties.containsKey(PROTOTYPE)) {

      Properties<String, V> prototype =
              (Properties<String, V>) properties.get(PROTOTYPE);

      return prototype.get(name);
    }

    return null;
  }

  @Override
  public V put(final String name, final V value) {

    if (PROTOTYPE.equals(name) && !(value instanceof Properties)) {

      throw new IllegalArgumentException(
              "prototype must be an instance of Properties");
    }

    return properties.put(name, value);
  }

  @Override
  public boolean has(final String name) {

    if (properties.containsKey(name)) {

      return null != properties.get(name);
    }

    if (properties.containsKey(PROTOTYPE)) {

      Properties<String, V> prototype =
              (Properties<String, V>) properties.get(PROTOTYPE);

      return prototype.has(name);
    }

    return false;
  }

  @Override
  public V remove(final String name) {

    if (properties.containsKey(PROTOTYPE)) {

      Properties<String, V> prototype =
              (Properties<String, V>) properties.get(PROTOTYPE);

      if (prototype.has(name)) {

        properties.put(name, null);

        return prototype.get(name);
      }
    }

    if (properties.containsKey(name)) {

      return properties.remove(name);
    }

    return null;
  }

  @Override
  public HashMapProperties<V, T, A> clone() {

    return new HashMapProperties<V, T, A>(properties);
  }

  @Override
  public T execute(final String name, final A... args) {

    V property = get(name);

    if (property instanceof Strategy) {

      return ((Strategy<String, V, T, A>) property).execute(this, args);
    }

    return null;
  }

  @Override
  public Set<String> filter(final String regex) {

    Pattern pattern = Pattern.compile(regex);

    Set<String> filteredNames = new HashSet<String>();

    Set<String> names = properties.keySet();

    synchronized (properties) {
      for (String name : names) {
        if (!PROTOTYPE.equals(name) && pattern.matcher(name).matches()) {
          filteredNames.add(name);
        }
      }
    }

    if (properties.containsKey(PROTOTYPE)) {
      FilterableProperties<String, V> prototype =
              (FilterableProperties<String, V>) properties.get(PROTOTYPE);
      filteredNames.addAll(prototype.filter(regex));
    }

    return filteredNames;
  }
}
