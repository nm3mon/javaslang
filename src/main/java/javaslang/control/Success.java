/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.CheckedFunction1;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A succeeded Try.
 *
 * @param <T> component type of this Success
 * @since 1.0.0
 */
public final class Success<T> implements Try<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private final T value;

    /**
     * Constructs a Failure.
     *
     * @param value A value
     */
    public Success(T value) {
        this.value = value;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public T orElse(T other) {
        return value;
    }

    @Override
    public T orElseGet(Function<? super Throwable, ? extends T> other) {
        return value;
    }

    @Override
    public void orElseRun(Consumer<? super Throwable> action) {
        // nothing to do
    }

    @Override
    public <X extends Throwable> T orElseThrow(Function<? super Throwable, X> exceptionProvider) throws X {
        return value;
    }

    @Override
    public Success<T> recover(Function<Throwable, ? extends T> f) {
        return this;
    }

    @Override
    public Success<T> recoverWith(Function<Throwable, Try<T>> f) {
        return this;
    }

    @Override
    public Success<T> onFailure(Consumer<Throwable> f) {
        return this;
    }

    @Override
    public Some<T> toOption() {
        return new Some<>(value);
    }

    @Override
    public Right<Throwable, T> toEither() {
        return new Right<>(value);
    }

    @Override
    public Optional<T> toJavaOptional() {
        return Optional.ofNullable(value);
    }

    @Override
    public Try<T> filter(Predicate<? super T> predicate) {
        try {
            if (predicate.test(value)) {
                return this;
            } else {
                return new Failure<>(new NoSuchElementException("Predicate does not hold for " + value));
            }
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @Override
    public Failure<Throwable> failed() {
        return new Failure<>(new UnsupportedOperationException("Success.failed()"));
    }

    @Override
    public Try<T> peek(Consumer<? super T> action) {
        try {
            action.accept(value);
            return this;
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @Override
    public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        try {
            return new Success<>(mapper.apply(value));
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @Override
    public <U> Try<U> flatMap(Function<? super T, ? extends Try<U>> mapper) {
        try {
            return mapper.apply(value);
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @Override
    public <R> Try<R> andThen(CheckedFunction1<T, R> f) {
        return flatMap(value -> Try.of(() -> f.apply(value)));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this) || (obj instanceof Success && Objects.equals(value, ((Success<?>) obj).value));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "Success(" + value + ")";
    }
}
