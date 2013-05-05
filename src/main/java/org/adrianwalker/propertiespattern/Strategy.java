package org.adrianwalker.propertiespattern;

interface Strategy<N, V, T, A> {

  T execute(Properties<N, V> properties, A... args);
}
