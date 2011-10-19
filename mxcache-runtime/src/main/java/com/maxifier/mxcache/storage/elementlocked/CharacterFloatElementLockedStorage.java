package com.maxifier.mxcache.storage.elementlocked;

import com.maxifier.mxcache.storage.*;

/**
 * Project: Maxifier
 * Created by: Yakoushin Andrey
 * Date: 15.02.2010
 * Time: 13:54:51
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
public interface CharacterFloatElementLockedStorage extends CharacterFloatStorage, ElementLockedStorage {
    void lock(char key);

    void unlock(char key);
}