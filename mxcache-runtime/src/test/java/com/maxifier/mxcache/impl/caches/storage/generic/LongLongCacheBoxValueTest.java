/*
 * Copyright (c) 2008-2014 Maxifier Ltd. All Rights Reserved.
 */
package com.maxifier.mxcache.impl.caches.storage.generic;

import com.maxifier.mxcache.caches.*;
import com.maxifier.mxcache.storage.*;
import com.maxifier.mxcache.resource.MxResource;
import com.maxifier.mxcache.impl.MutableStatisticsImpl;
import com.maxifier.mxcache.impl.resource.DependencyTracker;
import com.maxifier.mxcache.impl.resource.ResourceOccupied;
import org.testng.annotations.Test;

import com.maxifier.mxcache.provider.Signature;
import com.maxifier.mxcache.impl.wrapping.Wrapping;

import static org.mockito.Mockito.*;

/**
 * LongLongCacheBoxValueTest
 *
 * THIS IS GENERATED CLASS! DON'T EDIT IT MANUALLY!
 *
 * GENERATED FROM P2PBoxValueCacheTest.template
 *
 * @author Andrey Yakoushin (andrey.yakoushin@maxifier.com)
 * @author Alexander Kochurov (alexander.kochurov@maxifier.com)
 */
@Test
public class LongLongCacheBoxValueTest {
    private static final LongLongCalculatable CALCULATABLE = new LongLongCalculatable() {
        @Override
        public long calculate(Object owner, long o) {
            assert o == 42L;
            return 42L;
        }
    };

    public void testMiss() {
        LongObjectStorage storage = mock(LongObjectStorage.class);

        when(storage.load(42L)).thenReturn(Storage.UNDEFINED);
        when(storage.size()).thenReturn(0);

        LongLongCache cache = (LongLongCache) Wrapping.getFactory(new Signature(long.class, Object.class), new Signature(long.class, long.class), false).
                wrap("123", CALCULATABLE, storage, new MutableStatisticsImpl());
        cache.setDependencyNode(DependencyTracker.DUMMY_NODE);

        assert cache.getSize() == 0;
        assert cache.getStatistics().getHits() == 0;
        assert cache.getStatistics().getMisses() == 0;

        assert cache.getOrCreate(42L) == 42L;

        assert cache.getStatistics().getHits() == 0;
        assert cache.getStatistics().getMisses() == 1;

        verify(storage).size();
        verify(storage, atLeast(1)).load(42L);
        verify(storage).save(42L, 42L);
        verifyNoMoreInteractions(storage);
    }

    public void testHit() {
        LongObjectStorage storage = mock(LongObjectStorage.class);

        LongLongCache cache = (LongLongCache) Wrapping.getFactory(new Signature(long.class, Object.class), new Signature(long.class, long.class), false).
                wrap("123", CALCULATABLE, storage, new MutableStatisticsImpl());
        cache.setDependencyNode(DependencyTracker.DUMMY_NODE);

        when(storage.load(42L)).thenReturn(42L);
        when(storage.size()).thenReturn(1);

        assert cache.getSize() == 1;
        assert cache.getStatistics().getHits() == 0;
        assert cache.getStatistics().getMisses() == 0;

        assert cache.getOrCreate(42L) == 42L;

        assert cache.getStatistics().getHits() == 1;
        assert cache.getStatistics().getMisses() == 0;

        verify(storage).size();
        verify(storage, atLeast(2)).load(42L);
        verifyNoMoreInteractions(storage);
    }

    public void testClear() {
        LongObjectStorage storage = mock(LongObjectStorage.class);

        LongLongCache cache = (LongLongCache) Wrapping.getFactory(new Signature(long.class, Object.class), new Signature(long.class, long.class), false).
                wrap("123", CALCULATABLE, storage, new MutableStatisticsImpl());
        cache.setDependencyNode(DependencyTracker.DUMMY_NODE);

        cache.clear();

        verify(storage).clear();
        verifyNoMoreInteractions(storage);
    }

    public void testSetDuringDependencyNodeOperations() {
        LongObjectStorage storage = mock(LongObjectStorage.class);

        when(storage.load(42L)).thenReturn(Storage.UNDEFINED, 42L);

        LongLongCalculatable calculatable = mock(LongLongCalculatable.class);
        MxResource r = mock(MxResource.class);
        when(calculatable.calculate("123", 42L)).thenThrow(new ResourceOccupied(r));

        LongLongCache cache = (LongLongCache) Wrapping.getFactory(new Signature(long.class, Object.class), new Signature(long.class, long.class), false).
                wrap("123", calculatable, storage, new MutableStatisticsImpl());
        cache.setDependencyNode(DependencyTracker.DUMMY_NODE);

        assert cache.getStatistics().getHits() == 0;
        assert cache.getStatistics().getMisses() == 0;

        assert cache.getOrCreate(42L) == 42L;

        assert cache.getStatistics().getHits() == 1;
        assert cache.getStatistics().getMisses() == 0;

        verify(storage, atLeast(2)).load(42L);
        verifyNoMoreInteractions(storage);
        verify(calculatable).calculate("123", 42L);
        verifyNoMoreInteractions(calculatable);
    }

    public void testResetStat() {
        LongObjectStorage storage = mock(LongObjectStorage.class);

        when(storage.load(42L)).thenReturn(Storage.UNDEFINED);

        LongLongCache cache = (LongLongCache) Wrapping.getFactory(new Signature(long.class, Object.class), new Signature(long.class, long.class), false).
                wrap("123", CALCULATABLE, storage, new MutableStatisticsImpl());
        cache.setDependencyNode(DependencyTracker.DUMMY_NODE);

        assert cache.getStatistics().getHits() == 0;
        assert cache.getStatistics().getMisses() == 0;

        assert cache.getOrCreate(42L) == 42L;

        assert cache.getStatistics().getHits() == 0;
        assert cache.getStatistics().getMisses() == 1;

        cache.getStatistics().reset();

        assert cache.getStatistics().getHits() == 0;
        assert cache.getStatistics().getMisses() == 0;

        verify(storage, atLeast(1)).load(42L);
        verify(storage).save(42L, 42L);
        verifyNoMoreInteractions(storage);
    }
}