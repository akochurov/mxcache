package com.maxifier.mxcache.activity;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: dalex
 * Date: 01.07.2010
 * Time: 12:09:42
 */
public interface Activity extends Serializable {
    /**
     * ��� ���������� ���������.
     *
     * @return ��� �������.
     */
    @NotNull
    String getName();

    /**
     * ��������� ���������� � �������� scope.
     * ���� ���������� ��� ���� ��������� � ����� �� ��� ����� ������� scope, �� ������ ������.
     * @param scope ������� ���������� ����������
     */
    void start(@NotNull ActivityScope scope);

    /**
     * ��������� ���������� � ������� scope.
     * ������ ���������� ������ ���� ��������� � ��� �� scope � ������� �� ���, ������� ���� ��������.
     * ��������� thread-local ��������� ����� ������ � ��� �� ������, ��� � ��������.
     * @param scope ������� ���������� ����������
     * @throws IllegalStateException ���� ���������� �� ���� �������� � �������� scope.
     */
    void finish(@NotNull ActivityScope scope);

    /**
     * ���������, �������� �� ����������.
     * @return true, ���� ���������� ���� ���� �� ��� ��������.
     */
    boolean isRunning();

    /**
     * ��������� ���������. ��������� ����������� ������ ���, ����� ���������� ���������� ��� �����������.
     * <p>
     * � ������ ������ ��� ���������� ���������� � thread-local scope, ���������� ���������� ������ � ������,
     * ��������� ��������� (��� global scope ��� �� �����������).
     * ������ ��������� ���������� ����� ���������� ���������. �.�. started ����� ���� ������ ��� isRunning() == false,
     * � finished - ��� isRunning() == true. ���������� � ��������� (����� Error) �� ������ �� ������ Activity.
     * @param listener ���������
     */
    void addListener(ActivityListener listener);

    /**
     * ������� ���������.
     * @param listener ���������
     */
    void removeListener(ActivityListener listener);
}
