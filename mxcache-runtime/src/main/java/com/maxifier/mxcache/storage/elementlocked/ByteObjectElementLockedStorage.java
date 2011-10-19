package com.maxifier.mxcache.storage.elementlocked;

import com.maxifier.mxcache.storage.*;

/**
 * Project: Maxifier
 * Created by: Yakoushin Andrey
 * Date: 15.02.2010
 * Time: 13:29:47
 * <p/>
 * Copyright (c) 1999-2009 Magenta Corporation Ltd. All Rights Reserved.
 * Magenta Technology proprietary and confidential.
 * Use is subject to license terms.
 * <p>
 * ������������ ����������� ������� ����� ������ ������ ��������� ��������:
 * 1) ������ contains, get � put ������ ���������� � �����������.
 * 2) ���� ���������� ���������� read-write ����������, �� ������ contains � get
 *    ���������� � ����������� �� ������, � put - � ����������� �� ������.
 * 3) ���� ����� contains ���������� true, �� �������������, ��� ������ ���
 *    ������������ ���������� ����� ������ ����� get � ����� �� ����������.
 *    (��� ������ put ����� �������� �� ������!)
 *
 * @author ELectronic ENgine
 */
public interface ByteObjectElementLockedStorage<F> extends ByteObjectStorage<F>, ElementLockedStorage {
    void lock(byte key);

    void unlock(byte key);
}
