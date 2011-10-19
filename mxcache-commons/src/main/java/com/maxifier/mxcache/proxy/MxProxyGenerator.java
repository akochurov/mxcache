package com.maxifier.mxcache.proxy;

import gnu.trove.THashMap;

import java.util.Map;

public final class MxProxyGenerator {
    private static final Map<MxProxyFactory, MxProxyFactory> PROXY_MAP = new THashMap<MxProxyFactory, MxProxyFactory>();
    private static final Map<MxGenericProxyFactory, MxGenericProxyFactory> GENERIC_PROXY_MAP = new THashMap<MxGenericProxyFactory, MxGenericProxyFactory>();

    private MxProxyGenerator() {
    }

    /**
     * ������� ������� ������� ��������� ����.
     *
     * @param sourceClass    �������� ���������
     * @param containerClass �����/��������� ����������
     * @return ������� ������� ��� ��������� ���� �������� � �����������.
     */
    public static synchronized <T, C extends Resolvable<T>> MxProxyFactory<T, C> getProxyFactory(Class<T> sourceClass, Class<C> containerClass) {
        MxProxyFactory<T, C> proxy = new MxProxyFactory<T, C>(sourceClass, containerClass);
        @SuppressWarnings({"unchecked"})
        MxProxyFactory<T, C> oldProxy = PROXY_MAP.get(proxy);
        if (oldProxy == null) {
            PROXY_MAP.put(proxy, proxy);
            return proxy;
        }
        return oldProxy;
    }

    /**
     * ������� generic ������� ������� ��������� ����.
     * ����������� ���, ��� ������������ ������ ������ ������ ��������� ���������� ��������� ����� ��� ���
     * ����������-����������, ������� ��������� �������� ������
     *
     * @param sourceClass    �������� ���������
     * @param containerClass �����/��������� ����������
     * @return ������� ������� ��� ��������� ���� �������� � �����������.
     */
    public static synchronized <T, C extends Resolvable<T>> MxGenericProxyFactory<T, C> getGenericProxyFactory(Class<T> sourceClass, Class<C> containerClass) {
        MxGenericProxyFactory<T, C> proxy = new MxGenericProxyFactory<T, C>(sourceClass, containerClass);
        @SuppressWarnings({"unchecked"})
        MxGenericProxyFactory<T, C> oldProxy = GENERIC_PROXY_MAP.get(proxy);
        if (oldProxy == null) {
            GENERIC_PROXY_MAP.put(proxy, proxy);
            return proxy;
        }
        return oldProxy;
    }
}
