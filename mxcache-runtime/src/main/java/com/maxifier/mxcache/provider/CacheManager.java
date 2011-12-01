package com.maxifier.mxcache.provider;

import com.maxifier.mxcache.caches.Cache;
import com.maxifier.mxcache.context.CacheContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dalex
 * Date: 22.04.2010
 * Time: 18:03:44
 */
public interface CacheManager<T> {
    /**
     * @return ����������, ���������� ��� ��������
     */
    CacheDescriptor<T> getDescriptor();

    /**
     * ������� ��������� ����
     * @param owner �������� ����
     * @return ��������� ����
     */
    Cache createCache(@Nullable T owner);

    /**
     * ���� ����� ����� ������ ��� ����������� (JMX).
     * @return ������ ������ �� ���������� ����
     */
    List<Cache> getInstances();

    /**
     * @return implementation details, e.g. class name of storage/cache
     */
    String getImplementationDetails();

    CacheContext getContext();
}
