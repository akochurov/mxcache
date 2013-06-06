package com.maxifier.mxcache.resource;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: dalex
 * Date: 12.04.2010
 * Time: 9:17:37
 */
public interface MxResource extends Serializable {
    /**
     * ��� ������� ���������.
     * @return ��� �������.
     */
    @NotNull
    String getName();

    /**
     * ��������� ������ � ��������� ������.
     * ����� ��������� ������ ResourceOccupied. �� �������������� � �����������, �.�. ��� ����� �������� �
     * ������������� deadlock'��.
     * @throws ResourceModificationException ���� ������� ����� ��� ������� ���������� �� ������ ����� �������, � ���
     * ���� ��� �� ������ ���.
     * �.�. ����������� ������� �� ������ �������� ��������� � �����, ��������� �� ����� ������ �������.  
     */
    void readStart() throws ResourceModificationException;

    /**
     * ���������� ������
     */
    void readEnd();

    /**
     * ��������� ������ � ��������� ������
     * @throws ResourceModificationException � ����� ������ ��������� ���� �� ���� ���������� �����. 
     */
    void writeStart() throws ResourceModificationException;

    /**
     * ���������� ������
     */
    void writeEnd();

    /**
     * ������� �� �������� ������, ��������� � ����� ������ ��������� ����� ���� �������� � ������ ������
     * @return true, ���� ������ ��������� � ��������� ������
     */
    boolean isReading();

    /**
     * ������� �� �������� ������, ��������� � ����� ������ ��������� ����� ���� �������� � ������ ������
     * @return true, ���� ������ ��������� � ��������� �����������
     */
    boolean isWriting();

    /**
     * ��������� ��������� ������ �������.
     * ���� ����� �� �����������, ��� ������ ����� ��������, ��������� � ����� ������ ��������� ����� ����
     * �������� � ������ ������.
     * @throws ResourceModificationException ���� ����������� ������� ����� ������� ����� (���������� ���������
     * ��������� �����������, ���� ���� ����������� ���� ����). 
     */
    void waitForEndOfModification() throws ResourceModificationException;

    /**
     * ��������� ������ � ��������� ������ � ������� ��� ����, ������� ������� �� ������� �������.
     */
    void clearDependentCaches();
}
