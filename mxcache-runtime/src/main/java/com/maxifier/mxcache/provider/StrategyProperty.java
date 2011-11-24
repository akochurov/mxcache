package com.maxifier.mxcache.provider;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

/**
 * Created by IntelliJ IDEA.
 * User: dalex
 * Date: 30.04.2010
 * Time: 19:36:29
 * <p>
 * ������������ ����� �������� ���������������� ���������.
 * @see CacheDescriptor#getProperty(StrategyProperty)
 * @see CacheDescriptor#getProperty(AnnotationProperty)
 * @see AnnotationProperty 
 */
public class StrategyProperty<T> {
    private final String name;

    private final Class<T> type;

    private final T defaultValue;

    /**
     * @param name ��������
     * @param type ���
     * @param defaultValue �������� �� ���������
     */
    public StrategyProperty(@NotNull String name, @NotNull Class<T> type, T defaultValue) {
        if (defaultValue != null && !type.isInstance(defaultValue)) {
            throw new IllegalArgumentException("Invalid defaultValue = " + defaultValue + " for property " + name + ": " + type);
        }
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    /**
     * ������� �������� �� �������� �� ��������� null.
     *
     * @param name ��������
     * @param type ���
     */
    public StrategyProperty(String name, Class<T> type) {
        this(name, type, null);
    }

    public static <T> StrategyProperty<T> create(@NotNull String name, @NotNull Class<T> type, T defaultValue) {
        return new StrategyProperty<T>(name, type, defaultValue);
    }

    public static <T> StrategyProperty<T> create(@NotNull String name, @NotNull Class<T> type) {
        return new StrategyProperty<T>(name, type);
    }

    public static <A extends Annotation, T> AnnotationProperty<A, T> create(@NotNull String name, @NotNull Class<T> type, Class<A> annotationClass, String annotationPropertyName) {
        return new ReflectiveAnnotationProperty<A, T>(name, type, null, annotationClass, annotationPropertyName);
    }

    public static <A extends Annotation, T> AnnotationProperty<A, T> create(@NotNull String name, @NotNull Class<T> type, T defaultValue, Class<A> annotationClass, String annotationPropertyName) {
        return new ReflectiveAnnotationProperty<A, T>(name, type, defaultValue, annotationClass, annotationPropertyName);
    }

    /**
     * @return �������� (��� ������� �������� ������������ � mxcache.xml)
     */
    public String getName() {
        return name;
    }

    /**
     * @return ��� ��������
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * @return �������� �� ���������
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StrategyProperty that = (StrategyProperty) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name + ": " + type;
    }
}