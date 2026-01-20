package org.gpc.auth.usecase;

@FunctionalInterface
public interface UseCase<T,R> {
    R execute(T command);
}
