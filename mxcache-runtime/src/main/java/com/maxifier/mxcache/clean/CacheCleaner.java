package com.maxifier.mxcache.clean;

import com.maxifier.mxcache.caches.CleaningNode;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: dalex
 * Date: 29.03.2010
 * Time: 13:24:40
 */
public interface CacheCleaner {
    /**
     * ������� ��� ���� � �������� ����������. �� ������� ����������� ����.
     *
     * @param o ���������
     */
    void clearCacheByInstance(Object o);

    /**
     * ������� ��� ���� � �������� �����������. �� ������� ����������� ����.
     *
     * @param o ����������
     */
    void clearCacheByInstances(Object... o);

    /**
     * ������� ��� ���� ��������� ����������, ������� �������� ���
     * @param o ���������
     * @param tag ���
     */
    void clearInstanceByTag(Object o, String tag);

    /**
     * ������� ��� ���� ��������� ����������, ������� �������� ������
     *
     * @param o   ���������
     * @param group ������
     */
    void clearInstanceByGroup(Object o, String group);

    /**
     * ������� ��� ���� � �������� ������, � ��� ����� ��� ���� ���� ����������� ������� ������ (���� ����������� �
     * ������ � � ��������), � ��� ����������� ����, ����������� � ������ ������ � ���� ��� ��������
     * (�.�. ����������� ���� ������� �� ���������). 
     *
     * @param aClass �����
     */
    void clearCacheByClass(Class<?> aClass);

    /**
     * ������� ��� ����, ������������� � �������� ������
     * @param group ������
     */
    void clearCacheByGroup(String group);

    /**
     * ������� ��� ����, ������� �������� ���
     * @param tag ���
     */
    void clearCacheByTag(String tag);

    /**
     * ������� ��� ����, �������������� �������� ����������
     * @param annotationClass ����� ���������
     */
    void clearCacheByAnnotation(Class<? extends Annotation> annotationClass);

    /**
     * ������� ��� ����, �������� ���������� 
     * @param elements �������� ��� �������
     */
    void clearAll(Collection<? extends CleaningNode> elements);
}
