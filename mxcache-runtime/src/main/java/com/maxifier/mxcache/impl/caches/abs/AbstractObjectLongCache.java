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
 * AbstractObjectLongCache<E>
 *
 * THIS IS GENERATED CLASS! DON'T EDIT IT MANUALLY!
 *
 * GENERATED FROM P2PCache.template
 *
 * @author Andrey Yakoushin (andrey.yakoushin@maxifier.com)
 * @author Alexander Kochurov (alexander.kochurov@maxifier.com)
 */
public abstract class AbstractObjectLongCache<E> extends AbstractCache implements ObjectLongCache<E>, ObjectLongStorage<E> {
    private final ObjectLongCalculatable<E> calculatable;

    public AbstractObjectLongCache(Object owner, ObjectLongCalculatable<E> calculatable, MutableStatistics statistics) {
        super(owner, statistics);
        this.calculatable = calculatable;
    }

    @Override
    public long getOrCreate(E o) {
        if (DependencyTracker.isBypassCaches()) {
            return calculatable.calculate(owner, o);
        } else {
            lock();
            try {
                if (isCalculated(o)) {
                    DependencyTracker.mark(getDependencyNode());
                    hit();
                    return load(o);
                }
                DependencyNode callerNode = DependencyTracker.track(getDependencyNode());
                try {
                    while(true) {
                        try {
                            return create(o);
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
                                if (isCalculated(o)) {
                                    hit();
                                    return load(o);
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

    protected long create(E o) {
        long start = System.nanoTime();
        long t = calculatable.calculate(owner, o);
        long end = System.nanoTime();
        miss(end - start);
        save(o, t);
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