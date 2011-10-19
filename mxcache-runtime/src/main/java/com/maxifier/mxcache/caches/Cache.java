package com.maxifier.mxcache.caches;

import com.maxifier.mxcache.interfaces.StatisticsHolder;
import com.maxifier.mxcache.provider.CacheDescriptor;

/**
 * Created by IntelliJ IDEA.
 * User: dalex
 * Date: 07.04.2010
 * Time: 16:33:00
 */
public interface Cache extends StatisticsHolder, CleaningNode {

    /**
     * @return ��������������� ����� ���������. (����� ��������� ����� ���������� � ����� ������!)
     */
    int size();

    /**
     * @return ���������� ����, ��� �������� ������ ������ ���������
     */
    CacheDescriptor getDescriptor();
}
