/*
 * Copyright (c) 2008-2014 Maxifier Ltd. All Rights Reserved.
 */
package com.maxifier.mxcache.impl.caches.storage;

import com.maxifier.mxcache.caches.*;
import com.maxifier.mxcache.impl.wrapping.Wrapping;
import com.maxifier.mxcache.storage.ObjectDoubleStorage;
import com.maxifier.mxcache.storage.elementlocked.ObjectDoubleElementLockedStorage;
import com.maxifier.mxcache.provider.Signature;
import com.maxifier.mxcache.resource.MxResource;
import com.maxifier.mxcache.impl.MutableStatisticsImpl;
import com.maxifier.mxcache.impl.resource.DependencyTracker;
import com.maxifier.mxcache.impl.resource.ResourceOccupied;
import com.maxifier.mxcache.interfaces.StatisticsHolder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.*;

import java.util.concurrent.locks.*;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * ObjectDoubleCacheTest
 *
 * THIS IS GENERATED CLASS! DON'T EDIT IT MANUALLY!
 *
 * GENERATED FROM P2PCacheTest.template
 *
 * @author Andrey Yakoushin (andrey.yakoushin@maxifier.com)
 * @author Alexander Kochurov (alexander.kochurov@maxifier.com)
 */
@SuppressWarnings({ "unchecked" })
@Test
public class ObjectDoubleCacheTest {
    private static final Signature SIGNATURE = new Signature(Object.class, double.class);

    private static final ObjectDoubleCalculatable CALCULATABLE = new ObjectDoubleCalculatable() {
        @Override
        public double calculate(Object owner, Object o) {
            assert o == "123";
            return 42d;
        }
    };

    @DataProvider(name = "both")
    public Object[][] v200v210v219() {
        return new Object[][] {{false}, {true}};
    }

    private static class Occupied implements ObjectDoubleCalculatable {
        private boolean occupied;

        private int occupiedRequests;

        public Occupied() {

        }

        public synchronized void setOccupied(boolean occupied) {
            this.occupied = occupied;
            notifyAll();
        }

        @Override
        public synchronized double calculate(Object owner, Object o) {
            if (occupied) {
                occupiedRequests++;
                notifyAll();

                MxResource r = mock(MxResource.class);
                doAnswer(new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        synchronized(Occupied.this) {
                            while(occupied) {
                                Occupied.this.wait();
                            }
                        }
                        return null;
                    }
                }).when(r).waitForEndOfModification();
                throw new ResourceOccupied(r);
            }
            return 42d;
        }
    }

    @Test(dataProvider = "both")
    public void testOccupied(boolean elementLocked) throws Throwable {
        ObjectDoubleStorage storage = createStorage(elementLocked);
        Occupied occupied = new Occupied();

        when(storage.isCalculated("123")).thenReturn(false);
        when(storage.size()).thenReturn(0);

        final ObjectDoubleCache cache = (ObjectDoubleCache) Wrapping.getFactory(SIGNATURE, SIGNATURE, elementLocked).
                wrap("123", occupied, storage, new MutableStatisticsImpl());
        cache.setDependencyNode(DependencyTracker.DUMMY_NODE);

        occupied.setOccupied(true);

        class TestThread extends Thread {
            public Throwable t;

            @Override
            public void run() {
                try {
                    assert cache.getSize() == 0;
                    assert cache.getStatistics().getHits() == 0;
                    assert cache.getStatistics().getMisses() == 0;

                    assert cache.getOrCreate("123") == 42d;

                    assertEquals(cache.getStatistics().getHits(), 0);
                    assertEquals(cache.getStatistics().getMisses(), 1);
                } catch (Throwable t) {
                    this.t = t;
                }
            }
        }
        TestThread t = new TestThread();
        t.start();

        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized(occupied) {
            while(occupied.occupiedRequests == 0) {
                occupied.wait();
            }
        }

        occupied.setOccupied(false);

        t.join();

        if (t.t != null) {
            throw t.t;
        }

        verify(storage).size();
        verify(storage, atLeast(1)).isCalculated("123");
        verify(storage).save("123", 42d);
        if (elementLocked) {
            
                ((ObjectDoubleElementLockedStorage)verify(storage, atLeast(1))).lock("123");
                ((ObjectDoubleElementLockedStorage)verify(storage, atLeast(1))).unlock("123");
            
        }
        verifyNoMoreInteractions(storage);
    }

    @Test(dataProvider = "both")
    public void testMiss(boolean elementLocked) {
        ObjectDoubleStorage storage = createStorage(elementLocked);

        when(storage.isCalculated("123")).thenReturn(false);
        when(storage.size()).thenReturn(0);

        ObjectDoubleCache cache = (ObjectDoubleCache) Wrapping.getFactory(SIGNATURE, SIGNATURE, elementLocked).
                wrap("123", CALCULATABLE, storage, new MutableStatisticsImpl());
        cache.setDependencyNode(DependencyTracker.DUMMY_NODE);

        assert cache.getSize() == 0;
        assert cache.getStatistics().getHits() == 0;
        assert cache.getStatistics().getMisses() == 0;

        assert cache.getOrCreate("123") == 42d;

        assert cache.getStatistics().getHits() == 0;
        assert cache.getStatistics().getMisses() == 1;

        verify(storage).size();
        verify(storage, atLeast(1)).isCalculated("123");
        verify(storage).save("123", 42d);
        if (elementLocked) {
            
                ((ObjectDoubleElementLockedStorage)verify(storage, atLeast(1))).lock("123");
                ((ObjectDoubleElementLockedStorage)verify(storage, atLeast(1))).unlock("123");
            
        }
        verifyNoMoreInteractions(storage);
    }

    @Test(dataProvider = "both")
    public void testHit(boolean elementLocked) {
        ObjectDoubleStorage storage = createStorage(elementLocked);

        ObjectDoubleCache cache = (ObjectDoubleCache) Wrapping.getFactory(SIGNATURE, SIGNATURE, elementLocked).
                wrap("123", CALCULATABLE, storage, new MutableStatisticsImpl());
        cache.setDependencyNode(DependencyTracker.DUMMY_NODE);

        when(storage.isCalculated("123")).thenReturn(true);
        when(storage.load("123")).thenReturn(42d);
        when(storage.size()).thenReturn(1);

        assert cache.getSize() == 1;
        assert cache.getStatistics().getHits() == 0;
        assert cache.getStatistics().getMisses() == 0;

        assert cache.getOrCreate("123") == 42d;

        assert cache.getStatistics().getHits() == 1;
        assert cache.getStatistics().getMisses() == 0;

        verify(storage).size();
        verify(storage, atLeast(1)).isCalculated("123");
        verify(storage).load("123");
        if (elementLocked) {
            
                ((ObjectDoubleElementLockedStorage)verify(storage, atLeast(1))).lock("123");
                ((ObjectDoubleElementLockedStorage)verify(storage, atLeast(1))).unlock("123");
            
        }
        verifyNoMoreInteractions(storage);
    }

    @Test(dataProvider = "both")
    public void testClear(boolean elementLocked) {
        ObjectDoubleStorage storage = createStorage(elementLocked);

        ObjectDoubleCache cache = (ObjectDoubleCache) Wrapping.getFactory(SIGNATURE, SIGNATURE, elementLocked).
                wrap("123", CALCULATABLE, storage, new MutableStatisticsImpl());
        cache.setDependencyNode(DependencyTracker.DUMMY_NODE);

        cache.clear();

        verify(storage).clear();
        verifyNoMoreInteractions(storage);
    }

    @Test(dataProvider = "both")
    public void testSetDuringDependencyNodeOperations(boolean elementLocked) {
        ObjectDoubleStorage storage = createStorage(elementLocked);

        when(storage.isCalculated("123")).thenReturn(false, true);
        when(storage.load("123")).thenReturn(42d);

        ObjectDoubleCalculatable calculatable = mock(ObjectDoubleCalculatable.class);
        MxResource r = mock(MxResource.class);
        when(calculatable.calculate("123", "123")).thenThrow(new ResourceOccupied(r));

        ObjectDoubleCache cache = (ObjectDoubleCache) Wrapping.getFactory(SIGNATURE, SIGNATURE, elementLocked).
                wrap("123", calculatable, storage, new MutableStatisticsImpl());
        cache.setDependencyNode(DependencyTracker.DUMMY_NODE);

        assert cache.getStatistics().getHits() == 0;
        assert cache.getStatistics().getMisses() == 0;

        assert cache.getOrCreate("123") == 42d;

        assert cache.getStatistics().getHits() == 1;
        assert cache.getStatistics().getMisses() == 0;

        verify(storage, times(2)).isCalculated("123");
        verify(storage).load("123");
        if (elementLocked) {
            
                ((ObjectDoubleElementLockedStorage)verify(storage, atLeast(1))).lock("123");
                ((ObjectDoubleElementLockedStorage)verify(storage, atLeast(1))).unlock("123");
            
        }
        verifyNoMoreInteractions(storage);
        verify(calculatable).calculate("123", "123");
        verifyNoMoreInteractions(calculatable);
    }

    @Test(dataProvider = "both")
    public void testResetStat(boolean elementLocked) {
        ObjectDoubleStorage storage = createStorage(elementLocked);

        when(storage.isCalculated("123")).thenReturn(false);

        ObjectDoubleCache cache = (ObjectDoubleCache) Wrapping.getFactory(SIGNATURE, SIGNATURE, elementLocked).
                wrap("123", CALCULATABLE, storage, new MutableStatisticsImpl());
        cache.setDependencyNode(DependencyTracker.DUMMY_NODE);

        assert cache.getStatistics().getHits() == 0;
        assert cache.getStatistics().getMisses() == 0;

        assert cache.getOrCreate("123") == 42d;

        assert cache.getStatistics().getHits() == 0;
        assert cache.getStatistics().getMisses() == 1;

        cache.getStatistics().reset();

        assert cache.getStatistics().getHits() == 0;
        assert cache.getStatistics().getMisses() == 0;

        verify(storage, atLeast(1)).isCalculated("123");
        verify(storage).save("123", 42d);
        if (elementLocked) {
            
                ((ObjectDoubleElementLockedStorage)verify(storage, atLeast(1))).lock("123");
                ((ObjectDoubleElementLockedStorage)verify(storage, atLeast(1))).unlock("123");
            
        }
        verifyNoMoreInteractions(storage);
    }

    private ObjectDoubleStorage createStorage(boolean elementLocked) {
        ObjectDoubleStorage storage = mock(elementLocked ? ObjectDoubleElementLockedStorage.class : ObjectDoubleStorage.class);
        if (elementLocked) {
            when(((ObjectDoubleElementLockedStorage)storage).getLock()).thenReturn(new ReentrantLock());
        }
        return storage;
    }    

    @Test(dataProvider = "both")
    public void testTransparentStat(boolean elementLocked) {
        ObjectDoubleStorage storage = mock(elementLocked ? ObjectDoubleElementLockedStorage.class : ObjectDoubleStorage.class, withSettings().extraInterfaces(StatisticsHolder.class));

        ObjectDoubleCache cache = (ObjectDoubleCache) Wrapping.getFactory(SIGNATURE, SIGNATURE, elementLocked).
                wrap("123", CALCULATABLE, storage, new MutableStatisticsImpl());
        cache.setDependencyNode(DependencyTracker.DUMMY_NODE);

        cache.getStatistics();

        verify((StatisticsHolder)storage).getStatistics();
        verifyNoMoreInteractions(storage);
    }
}