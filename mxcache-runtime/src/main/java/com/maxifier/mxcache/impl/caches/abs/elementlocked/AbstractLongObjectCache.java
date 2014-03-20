/*
 * Copyright (c) 2008-2014 Maxifier Ltd. All Rights Reserved.
 */
package com.maxifier.mxcache.impl.caches.abs.elementlocked;

import com.maxifier.mxcache.CacheFactory;
import com.maxifier.mxcache.caches.*;
import com.maxifier.mxcache.impl.MutableStatistics;
import com.maxifier.mxcache.impl.CacheId;
import com.maxifier.mxcache.impl.CalculatableHelper;
import com.maxifier.mxcache.impl.resource.*;
import com.maxifier.mxcache.provider.CacheDescriptor;
import com.maxifier.mxcache.storage.elementlocked.*;


/**
 * AbstractLongObjectCache<F>
 *
 * THIS IS GENERATED CLASS! DON'T EDIT IT MANUALLY!
 *
 * GENERATED FROM P2OCache.template
 *
 * @author Andrey Yakoushin (andrey.yakoushin@maxifier.com)
 * @author Alexander Kochurov (alexander.kochurov@maxifier.com)
 */
public abstract class AbstractLongObjectCache<F> extends AbstractElementLockedCache implements LongObjectCache<F>, LongObjectElementLockedStorage<F> {
    private final LongObjectCalculatable<F> calculatable;

    public AbstractLongObjectCache(Object owner, LongObjectCalculatable<F> calculatable, MutableStatistics statistics) {
        super(owner, statistics);
        this.calculatable = calculatable;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public F getOrCreate(long o) {
        if (DependencyTracker.isBypassCaches()) {
            return calculatable.calculate(owner, o);
        } else {
            lock(o);
            try {
                Object v = load(o);
                if (v != UNDEFINED) {
                    DependencyTracker.mark(getDependencyNode());
                    hit();
                    return (F)v;
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
                                unlock(o);
                                try {
                                    e.getResource().waitForEndOfModification();
                                } finally {
                                    lock(o);
                                }
                                v = load(o);
                                if (v != UNDEFINED) {
                                    hit();
                                    return (F)v;
                                }
                            }
                        }
                    }
                } finally {
                    DependencyTracker.exit(callerNode);
                }
            } finally {
                unlock(o);
            }
        }
    }

    @SuppressWarnings({ "unchecked" })
    protected F create(long key) {
        long start = System.nanoTime();
        F t = calculatable.calculate(owner, key);
        long end = System.nanoTime();
        miss(end - start);
        save(key, t);
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
