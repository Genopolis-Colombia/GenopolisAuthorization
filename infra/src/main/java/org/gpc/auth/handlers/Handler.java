package org.gpc.auth.handlers;

@FunctionalInterface
public interface Handler<T,R> {
  R handle(T command);
}
