package com.maxifier.mxcache.provider;

import com.maxifier.mxcache.caches.Cache;
import com.maxifier.mxcache.context.CacheContext;
import com.maxifier.mxcache.impl.CacheId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: dalex
 * Date: 10.03.2010
 * Time: 9:24:10
 * </p>
 * ��������� ���� ������ ����� �������� �� �����������.
 */
public interface CacheProvider {
    /**
     * ��� �������� ������ � �����, �� �������� ���� ����� � ����������.
     * @param cacheOwner �����, � ������� �������� ������ ����������� �����
     * @param cacheId ������������� ����
     * @param keyType ��� �����, null ���� ���������� ����� �� ����� ����������. ���� ���������� ���������, �� ��� �����
 * ������������� � Tuple ����������� ����
     * @param valueType ��� ��������, ��������� � ����
     * @param group ������ (�� ��������� @Cached ������)
     * @param tags ���� (�� ��������� @Cached ������)
     * @param calculable ������, ����������� ��������. ������ ���� ���� �� �������
     * @param methodName ��� ������
     * @param methodDesc ���������� ������
     * @param cacheName name of cache, may be null (used by some strategies)
     */
    <T> void registerCache(Class<T> cacheOwner, int cacheId, Class keyType, Class valueType, String group, String[] tags, Object calculable, String methodName, String methodDesc, @Nullable String cacheName);

    /**
     * ��� ����������� ����� ���������� ����� �������� ������ � ������ �������������, ��� ������������� - �
     * ������������.
     *
     * @param cacheOwner �����, � ������� �������� ������ ����������� ����� (����� �� ��������� � ������� ����������, �
     * ���� ��� �������)
     * @param cacheId ������������� ���� (��������� � ���, ��� ��� ������� � registerCache)
     * @param instance ��������� ������� � �����. ��� ����������� ����� - null.
     * @param context context
     * @return ��������� ����
     */
    Cache createCache(@NotNull Class cacheOwner, int cacheId, @Nullable Object instance, CacheContext context);

    CacheDescriptor getDescriptor(CacheId id);

    /**
     * @return ������ ���� ����������.
     */
    List<CacheManager> getCaches();
}
