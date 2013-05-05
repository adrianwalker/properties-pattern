package org.adrianwalker.propertiespattern;

interface CloneableProperties<N, V> extends Properties<N, V>, Cloneable {

  CloneableProperties<N, V> clone();
}
