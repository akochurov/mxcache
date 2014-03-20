/*
 * Copyright (c) 2008-2014 Maxifier Ltd. All Rights Reserved.
 */
package com.maxifier.mxcache.impl.caches.storage;

import com.maxifier.mxcache.impl.MutableStatistics;
import com.maxifier.mxcache.impl.caches.abs.*;
import com.maxifier.mxcache.caches.*;
import com.maxifier.mxcache.storage.*;

import com.maxifier.mxcache.interfaces.Statistics;
import com.maxifier.mxcache.interfaces.StatisticsHolder;

import javax.annotation.Nonnull;

import javax.annotation.Nullable;

/**
 * StorageByteIntCacheImpl
 *
 * THIS IS GENERATED CLASS! DON'T EDIT IT MANUALLY!
 *
 * GENERATED FROM StorageP2PCache.template
 *
 * @author Andrey Yakoushin (andrey.yakoushin@maxifier.com)
 * @author Alexander Kochurov (alexander.kochurov@maxifier.com)
 */
public class StorageByteIntCacheImpl extends AbstractByteIntCache implements StorageHolder<ByteIntStorage> {
    private static final long serialVersionUID = 100L;

    private ByteIntStorage storage;

    public StorageByteIntCacheImpl(Object owner, ByteIntCalculatable calculatable, @Nonnull MutableStatistics statistics) {
        super(owner, calculatable, statistics);
    }

    @Override
    public void setStorage(@Nonnull ByteIntStorage storage) {
        if (this.storage != null) {
            throw new UnsupportedOperationException("Storage already set");
        }
        this.storage = storage;
    }

    @Override
    public boolean isCalculated(byte key) {
        return storage.isCalculated(key);
    }

    @Override
    public int load(byte key) {
        return storage.load(key);
    }

    @Override
    public void save(byte key, int value) {
        storage.save(key, value);
    }

    @Override
    public void clear() {
        storage.clear();
    }
    
    @Override
    public int size() {
        return storage.size();
    }

    @Nullable
    @Override
    public Statistics getStatistics() {
        if (storage instanceof StatisticsHolder) {
            return ((StatisticsHolder)storage).getStatistics();
        }
        return super.getStatistics();
    }
}