package org.adrianwalker.propertiespattern;

import java.util.Set;

interface FilterableProperties<N, V> extends Properties<N, V> {

  Set<N> filter(N filter);
}
