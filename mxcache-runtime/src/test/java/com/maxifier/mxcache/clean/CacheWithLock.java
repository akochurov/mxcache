/*
 * Copyright (c) 2008-2014 Maxifier Ltd. All Rights Reserved.
 */
package com.maxifier.mxcache.clean;

import com.maxifier.mxcache.caches.Cache;
import com.maxifier.mxcache.impl.resource.DependencyNode;
import com.maxifier.mxcache.impl.resource.DependencyTracker;
import com.maxifier.mxcache.interfaces.Statistics;
import com.maxifier.mxcache.provider.CacheDescriptor;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

/**
 * @author Alexander Kochurov (alexander.kochurov@maxifier.com)
 */
class CacheWithLock implements Cache {
    private final ReentrantLock lock;

    public CacheWithLock(ReentrantLock lock) {
        this.lock = lock;
    }

    @Override
    public Lock getLock() {
        return lock;
    }

    @Override
    public void clear() {
        assert lock.isHeldByCurrentThread();
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Statistics getStatistics() {
        return null;
    }

    @Override
    public CacheDescriptor getDescriptor() {
        return null;
    }

    @Override
    public void setDependencyNode(DependencyNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DependencyNode getDependencyNode() {
        return DependencyTracker.DUMMY_NODE;
    }

    @Override
    public Object getCacheOwner() {
        throw new UnsupportedOperationException();
    }
}
