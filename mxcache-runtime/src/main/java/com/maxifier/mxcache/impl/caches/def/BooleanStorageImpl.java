/*
 * Copyright (c) 2008-2014 Maxifier Ltd. All Rights Reserved.
 */
package com.maxifier.mxcache.impl.caches.def;

import com.maxifier.mxcache.storage.*;

/**
 * BooleanStorageImpl
 * THIS IS GENERATED CLASS! DON'T EDIT IT MANUALLY!
 *
 * GENERATED FROM PStorageImpl.template
 *
 * @author Andrey Yakoushin (andrey.yakoushin@maxifier.com)
 * @author Alexander Kochurov (alexander.kochurov@maxifier.com)
 */
public class BooleanStorageImpl implements BooleanStorage {
    private volatile boolean set;
    private boolean value;

    @Override
    public boolean isCalculated() {
        return set;
    }

    @Override
    public boolean load() {
        return value; 
    }

    @Override
    public void save(boolean v) {
        set = true;
        value = v;
    }

    @Override
    public void clear() {
        set = false;
    }

    @Override
    public int size() {
        return set ? 1 : 0;
    }
}
