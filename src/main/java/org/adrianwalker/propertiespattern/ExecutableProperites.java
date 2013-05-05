package org.adrianwalker.propertiespattern;

interface ExecutableProperites<N, V, T, A> extends Properties<N, V> {

  T execute(N name, A... args);
}
