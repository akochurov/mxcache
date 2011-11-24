package com.maxifier.mxcache.impl.caches.abs;

import com.maxifier.mxcache.CacheFactory;
import com.maxifier.mxcache.caches.*;
import com.maxifier.mxcache.impl.MutableStatistics;
import com.maxifier.mxcache.impl.CacheId;
import com.maxifier.mxcache.impl.CalculatableHelper;
import com.maxifier.mxcache.impl.resource.*;
import com.maxifier.mxcache.provider.CacheDescriptor;
import com.maxifier.mxcache.storage.*;

import org.jetbrains.annotations.NotNull;

/**
 * Project: Maxifier
 * Created by: Yakoushin Andrey
 * Date: 15.02.2010
 * Time: 13:54:51
 * <p/>
 * Copyright (c) 1999-2009 Magenta Corporation Ltd. All Rights Reserved.
 * Magenta Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * @author ELectronic ENgine
 */
public abstract class AbstractIntDoubleCache extends AbstractCache implements IntDoubleCache, IntDoubleStorage {
    private final IntDoubleCalculatable calculatable;

    @NotNull
    private final DependencyNode node;

    private final Object owner;

    public AbstractIntDoubleCache(Object owner, IntDoubleCalculatable calculatable, @NotNull DependencyNode node, MutableStatistics statistics) {
        super(statistics);
        this.node = node;
        this.owner = owner;
        this.calculatable = calculatable;
    }

    @Override
    public double getOrCreate(int o) {
        lock();
        try {
            if (isCalculated(o)) {
                DependencyTracker.mark(node);
                hit();
                return load(o);
            }
            DependencyNode callerNode = DependencyTracker.track(node);
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

    private double create(int o) {
        long start = System.nanoTime();
        double t = calculatable.calculate(owner, o);
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
    public DependencyNode getDependencyNode() {
        return node;
    }

    @Override
    public String toString() {
        return getDescriptor() + ": " + owner;
    }
}