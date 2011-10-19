package com.maxifier.mxcache.hashing;

import com.maxifier.mxcache.context.CacheContext;

import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: dalex
 * Date: 01.07.2010
 * Time: 15:01:18
 */
public interface HashingStrategyFactory {
    /**
     * @param context �������� �������
     * @param method �����
     * @return ���������� ��������� �����������, ��� null, ���� ���������� �����������
     */
    Object createHashingStrategy(CacheContext context, Method method);
}
