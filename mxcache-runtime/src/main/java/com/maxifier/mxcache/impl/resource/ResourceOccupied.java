package com.maxifier.mxcache.impl.resource;

import com.maxifier.mxcache.resource.MxResource;

/**
 * Created by IntelliJ IDEA.
 * User: dalex
 * Date: 14.04.2010
 * Time: 10:47:47
 *
 * <p>
 * ��� ������ ����� �� ������ ������� ����� ���, ������� �� RuntimeException, � Error - ������� ����� ���������
 * �������� ��� Exception'�.
 * <p>
 * <b>���� ������� �� ��������� stack trace!!!</b>
 */
public class ResourceOccupied extends Error {
    private final MxResource resource;

    public ResourceOccupied(MxResource resource) {
        super(resource.toString());
        this.resource = resource;
    }

    public MxResource getResource() {
        return resource;
    }

    @Override
    public Throwable fillInStackTrace() {
        // do nothing - we only need to traverse stack, not stacktrace.
        return this;
    }
}
