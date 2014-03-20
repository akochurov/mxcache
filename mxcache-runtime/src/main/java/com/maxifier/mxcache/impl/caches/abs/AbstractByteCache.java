/*
 * Copyright (c) 2008-2014 Maxifier Ltd. All Rights Reserved.
 */
package com.maxifier.mxcache.impl.caches.abs;

import com.maxifier.mxcache.CacheFactory;
import com.maxifier.mxcache.caches.*;
import com.maxifier.mxcache.impl.MutableStatistics;
import com.maxifier.mxcache.impl.CacheId;
import com.maxifier.mxcache.impl.CalculatableHelper;
import com.maxifier.mxcache.impl.resource.*;
import com.maxifier.mxcache.provider.CacheDescriptor;
import com.maxifier.mxcache.storage.*;

/**
 * AbstractByteCache
 *
 * THIS IS GENERATED CLASS! DON'T EDIT IT MANUALLY!
 *
 * GENERATED FROM P2PCache.template
 *
 * @author Andrey Yakoushin (andrey.yakoushin@maxifier.com)
 * @author Alexander Kochurov (alexander.kochurov@maxifier.com)
 */
public abstract class AbstractByteCache extends AbstractCache implements ByteCache, ByteStorage {
    private final ByteCalculatable calculatable;

    public AbstractByteCache(Object owner, ByteCalculatable calculatable, MutableStatistics statistics) {
        super(owner, statistics);
        this.calculatable = calculatable;
    }

    @Override
    public byte getOrCreate() {
        if (DependencyTracker.isBypassCaches()) {
            return calculatable.calculate(owner);
        } else {
            lock();
            try {
                if (isCalculated()) {
                    DependencyTracker.mark(getDependencyNode());
                    hit();
                    return load();
                }
                DependencyNode callerNode = DependencyTracker.track(getDependencyNode());
                try {
                    while(true) {
                        try {
                            return create();
                        } catch (ResourceOccupied e) {
                            if (callerNode != null) {
                                throw e;
                            } else {
                                unlock();
                                try {
                                    e.getResource().waitForEndOfModification();
                                } finally {
                                    lock();
                                }
                                if (isCalculated()) {
                                    hit();
                                    return load();
                                }
                            }
                        }
                    }
                } finally {
                    DependencyTracker.exit(callerNode);
                }
            } finally {
                unlock();
            }
        }
    }

    protected byte create() {
        long start = System.nanoTime();
        byte t = calculatable.calculate(owner);
        long end = System.nanoTime();
        miss(end - start);
        save(t);
        return t;
    }

    @Override
    public CacheDescriptor getDescriptor() {
        CacheId id = CalculatableHelper.getId(calculatable.getClass());
        return CacheFactory.getProvider().getDescriptor(id);
    }

    @Override
    public String toString() {
        return getDescriptor() + ": " + owner;
    }
}