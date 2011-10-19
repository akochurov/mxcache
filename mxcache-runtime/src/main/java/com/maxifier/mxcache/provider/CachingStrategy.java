package com.maxifier.mxcache.provider;

import com.maxifier.mxcache.context.CacheContext;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: dalex
 * Date: 22.04.2010
 * Time: 18:26:55
 */
public interface CachingStrategy {
    /**
     * @param context context
     * @param descriptor ���������� ���������� ���������
     * @param <T> ��� ������-��������� ����
     * @return �������� ���� ��� ��������� ����������� 
     */
    @NotNull
    <T> CacheManager<T> getManager(CacheContext context, CacheDescriptor<T> descriptor);
}
