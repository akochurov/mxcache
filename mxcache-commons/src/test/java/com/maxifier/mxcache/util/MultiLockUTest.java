package com.maxifier.mxcache.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by IntelliJ IDEA.
 * User: dalex
 * Date: 08.11.2010
 * Time: 14:03:07
 */
@Test
public class MultiLockUTest {

    public void testNestedLock() {
        MultiLock lock = new MultiLock();
        MultiLock.Sublock part = new MultiLock.Sublock(lock);

        part.lock();
        try {
            part.lock();
            part.unlock();
        } finally {
            part.unlock();
        }
    }

    public void testLockWithParent() {
        MultiLock lock = new MultiLock();
        MultiLock.Sublock part = new MultiLock.Sublock(lock);

        lock.lock();
        try {
            part.lock();
            part.unlock();
        } finally {
            lock.unlock();
        }
    }

    public void testSimulateDeadlock() throws InterruptedException, BrokenBarrierException {
        final MultiLock lock = new MultiLock();
        final MultiLock.Sublock part = new MultiLock.Sublock(lock);
        final CyclicBarrier barrier = new CyclicBarrier(2);
        final AtomicBoolean notification = new AtomicBoolean();

        Thread thread = new Thread() {
            @Override
            public void run() {
                part.lock();
                barrier(barrier);
                barrier(barrier);
                try {
                    lock.lock();
                    try {
                        notification.set(true);
                    } finally {
                        lock.unlock();
                    }
                } finally {
                    part.unlock();
                }
            }
        };

        thread.start();

        barrier(barrier);

        lock.lock();

        barrier(barrier);

        try {
            // ��� ��� �� ���������� ������ �����
            part.lock();
            try {
                // ���������, ��� ����������
                Assert.assertTrue(notification.get());
            } finally {
                part.lock();
            }
            thread.join();
        } finally {
            lock.unlock();
        }
    }

    public void testSimulateDeadlock2() throws InterruptedException, BrokenBarrierException {
        final MultiLock lock = new MultiLock();
        final MultiLock.Sublock part = new MultiLock.Sublock(lock);
        final CyclicBarrier barrier = new CyclicBarrier(2);
        final AtomicBoolean notification = new AtomicBoolean();

        Thread thread = new Thread() {
            @Override
            public void run() {
                part.lock();
                barrier(barrier);
                barrier(barrier);
                try {
                    lock.lock();
                    try {
                        notification.set(true);
                    } finally {
                        lock.unlock();
                    }
                } finally {
                    part.unlock();
                }
            }
        };

        thread.start();

        barrier(barrier);

        lock.lock();
        try {
            lock.lock();
            try {
                barrier(barrier);
                // ��� ��� �� ���������� ������ �����
                part.lock();
                try {
                    // ���������, ��� ����������
                    Assert.assertTrue(notification.get());
                } finally {
                    part.lock();
                }
                thread.join();
            } finally {
                lock.unlock();
            }
        } finally {
            lock.unlock();
        }
    }

    private void barrier(CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}
