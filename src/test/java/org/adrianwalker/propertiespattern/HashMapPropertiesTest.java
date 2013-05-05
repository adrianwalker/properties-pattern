package org.adrianwalker.propertiespattern;

import java.util.Arrays;
import static org.adrianwalker.propertiespattern.HashMapProperties.PROTOTYPE;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HashMapPropertiesTest {

  @Rule
  public ExpectedException exception = ExpectedException.none();

  public HashMapPropertiesTest() {
  }

  @Test
  public void testGet() {

    String name = "empty";

    Properties properties = new HashMapProperties();
    assertNull(properties.get(name));
  }

  @Test
  public void testPut() {

    String name = "name";
    String value = "value";

    Properties properties = new HashMapProperties();
    assertNull(properties.put(name, value));
  }

  @Test
  public void testPutGet() {

    String name = "name";
    String value = "value";

    Properties properties = new HashMapProperties();
    assertNull(properties.put(name, value));
    assertEquals(value, properties.get(name));
  }

  @Test
  public void testPutReplace() {

    String name = "name";
    String value1 = "value1";
    String value2 = "value2";

    Properties properties = new HashMapProperties();
    assertNull(properties.put(name, value1));
    assertEquals(value1, properties.put(name, value2));
  }

  @Test
  public void testHas() {

    String name = "empty";

    Properties properties = new HashMapProperties();
    assertFalse(properties.has(name));
  }

  @Test
  public void testPutHas() {

    String name = "name";
    String value = "value";

    Properties properties = new HashMapProperties();
    assertNull(properties.put(name, value));
    assertTrue(properties.has(name));
  }

  @Test
  public void testRemove() {

    String name = "name";

    Properties properties = new HashMapProperties();
    assertNull(properties.remove(name));
  }

  @Test
  public void testPutRemove() {

    String name = "name";
    String value = "value";

    Properties properties = new HashMapProperties();
    assertNull(properties.put(name, value));
    assertEquals(value, properties.remove(name));
  }

  @Test
  public void testPutRemoveHas() {

    String name = "name";
    String value = "value";

    Properties properties = new HashMapProperties();
    assertNull(properties.put(name, value));
    assertTrue(properties.has(name));
    assertEquals(value, properties.remove(name));
    assertFalse(properties.has(name));
  }

  @Test
  public void testNullPrototypePut() {

    Properties prototype = null;

    exception.expect(IllegalArgumentException.class);
    HashMapProperties properties = new HashMapProperties();
    assertNull(properties.put(PROTOTYPE, prototype));
  }

  @Test
  public void testPutPrototype() {

    Properties prototype = new HashMapProperties();
    Properties properties = new HashMapProperties();
    assertNull(properties.put(PROTOTYPE, prototype));
    assertTrue(properties.has(PROTOTYPE));
  }

  @Test
  public void testPrototypeHas() {

    String name1 = "name1";
    String name2 = "name2";
    String value = "value";

    Properties prototype = new HashMapProperties();
    assertNull(prototype.put(name1, value));

    Properties properties = new HashMapProperties();
    assertNull(properties.put(PROTOTYPE, prototype));
    assertTrue(properties.has(name1));
    assertFalse(properties.has(name2));
  }

  @Test
  public void testPrototypeGet() {

    String name1 = "name1";
    String name2 = "name2";
    String value = "value";

    Properties prototype = new HashMapProperties();
    assertNull(prototype.put(name1, value));

    HashMapProperties properties = new HashMapProperties();
    assertNull(properties.put(PROTOTYPE, prototype));
    assertEquals(value, properties.get(name1));
    assertNull(properties.get(name2));
  }

  @Test
  public void testPrototypeOverride() {

    String name = "name";
    String value1 = "value1";
    String value2 = "value2";

    Properties prototype = new HashMapProperties();
    assertNull(prototype.put(name, value1));

    Properties properties = new HashMapProperties();
    assertNull(properties.put(PROTOTYPE, prototype));
    assertNull(properties.put(name, value2));
    assertEquals(value1, prototype.get(name));
    assertEquals(value2, properties.get(name));
  }

  @Test
  public void testPrototypeRemoveHas() {

    String name = "name";
    String value = "value";

    Properties prototype = new HashMapProperties();
    assertNull(prototype.put(name, value));

    Properties properties = new HashMapProperties();
    assertNull(properties.put(PROTOTYPE, prototype));
    assertEquals(value, properties.remove(name));
    assertFalse(properties.has(name));
  }

  @Test
  public void testPrototypeRemoveGet() {

    String name = "name";
    String value = "value";

    Properties prototype = new HashMapProperties();
    assertNull(prototype.put(name, value));

    Properties properties = new HashMapProperties();
    assertNull(properties.put(PROTOTYPE, prototype));
    assertEquals(value, properties.remove(name));
    assertNull(properties.get(name));
  }

  @Test
  public void testPrototypeRemovePutHasGet() {

    String name = "name";
    String value1 = "value1";
    String value2 = "value2";

    Properties prototype = new HashMapProperties();
    assertNull(prototype.put(name, value1));

    Properties properties = new HashMapProperties();
    assertNull(properties.put(PROTOTYPE, prototype));
    assertEquals(value1, properties.remove(name));
    assertFalse(properties.has(name));
    assertEquals(null, properties.put(name, value2));
    assertTrue(properties.has(name));
    assertEquals(value2, properties.get(name));
  }

  @Test
  public void testClone() {

    String name1 = "name1";
    String value1 = "value1";
    String name2 = "name2";
    String value2 = "value2";
    String value3 = "value3";

    Properties prototype = new HashMapProperties();
    assertNull(prototype.put(name1, value1));

    CloneableProperties properties = new HashMapProperties();
    assertNull(properties.put(PROTOTYPE, prototype));
    assertEquals(null, properties.put(name2, value2));

    CloneableProperties clone = properties.clone();
    assertEquals(value1, clone.get(name1));
    assertEquals(value2, clone.get(name2));
    assertEquals(value2, clone.put(name2, value3));
    assertEquals(value3, clone.get(name2));
    assertEquals(value1, properties.get(name1));
    assertEquals(value2, properties.get(name2));
  }

  @Test
  public void testExecute() {

    String name = "name";
    String value = "value";
    Strategy strategy = new Strategy() {
      @Override
      public Object execute(Properties properties, Object... args) {
        return args[0];
      }
    };

    Properties prototype = new HashMapProperties();
    assertNull(prototype.put(name, strategy));

    ExecutableProperites properties = new HashMapProperties();
    assertNull(properties.put(PROTOTYPE, prototype));

    assertEquals(value, properties.execute(name, value));
  }

  @Test
  public void testExecuteNonStrategy() {

    String name = "name";
    String value = "value";

    Properties prototype = new HashMapProperties();
    assertNull(prototype.put(name, value));

    ExecutableProperites properties = new HashMapProperties();
    assertNull(properties.put(PROTOTYPE, prototype));

    assertNull(properties.execute(name, value));
  }

  @Test
  public void testEmptyStringFilter() {

    String[] names = {"aaa", "bbb", "ccc", "abc"};
    String[] expecteds = {};
    String filter = "";

    testFilter(names, filter, expecteds);
  }

  @Test
  public void testNonEmptyStringFilter() {

    String[] names = {"aaa", "bbb", "ccc", "abc"};
    String[] expecteds = {"ccc"};
    String filter = "ccc";

    testFilter(names, filter, expecteds);
  }

  @Test
  public void testNonEmptyStringFilterNoMatches() {

    String[] names = {"aaa", "bbb", "ccc", "abc"};
    String[] expecteds = {};
    String filter = "ddd";

    testFilter(names, filter, expecteds);
  }

  @Test
  public void testRegexFilter() {

    String[] names = {"foo", "bar", "fooBar", "barFoo"};
    String[] expecteds = {"foo", "fooBar", "barFoo"};
    String filter = ".*[Ff]oo.*";

    testFilter(names, filter, expecteds);
  }

  @Test
  public void testRegexFilterNoMatches() {

    String[] names = {"foo", "bar", "fooBar", "barFoo"};
    String[] expecteds = {};
    String filter = "\\d+";

    testFilter(names, filter, expecteds);
  }

  @Test
  public void testPrototypeMatchAllFilter() {

    String[] prototypeNames = {"a", "b", "c", "d"};
    String[] names = {"c", "d", "e", "f"};
    String[] expecteds = {"a", "b", "c", "d", "e", "f"};
    String filter = ".*";

    testPrototypeFilter(prototypeNames, names, filter, expecteds);
  }

  @Test
  public void testPrototypeRegexFilter() {

    String[] prototypeNames = {"foo", "bar"};
    String[] names = {"fooBar", "barFoo"};
    String filter = ".*[Ff]oo.*";
    String[] expecteds = {"barFoo", "foo", "fooBar"};

    testPrototypeFilter(prototypeNames, names, filter, expecteds);
  }

  private void testFilter(final String[] names, final String filter, final String[] expecteds) {

    HashMapProperties properties = populateProperties(names);

    Object[] actuals = properties.filter(filter).toArray();

    Arrays.sort(expecteds);
    Arrays.sort(actuals);

    assertArrayEquals(expecteds, actuals);
  }

  private void testPrototypeFilter(final String[] prototypeNames, final String[] names, final String filter, final String[] expecteds) {

    HashMapProperties prototype = populateProperties(prototypeNames);

    HashMapProperties properties = populateProperties(names);
    properties.put(PROTOTYPE, prototype);

    Object[] actuals = properties.filter(filter).toArray();

    Arrays.sort(expecteds);
    Arrays.sort(actuals);

    assertArrayEquals(expecteds, actuals);
  }

  private HashMapProperties populateProperties(final String[] names) {

    String value = "value";
    HashMapProperties properties = new HashMapProperties();
    for (String name : names) {
      properties.put(name, value);
    }
    
    return properties;
  }
}
