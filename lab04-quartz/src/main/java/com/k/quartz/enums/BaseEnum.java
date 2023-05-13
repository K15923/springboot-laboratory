package com.k.quartz.enums;

public abstract class BaseEnum<E extends Enum<E>, T> {
    private final T value;

    protected BaseEnum(T value) {
        this.value = value;
    }

    public static <E extends Enum<E>, T> BaseEnum<E, T> fromValue(T value, BaseEnum<E, T>[] values) {
        for (BaseEnum<E, T> v : values) {
            if (v.getValue().equals(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException("No such enum object for the value: " + value);
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}