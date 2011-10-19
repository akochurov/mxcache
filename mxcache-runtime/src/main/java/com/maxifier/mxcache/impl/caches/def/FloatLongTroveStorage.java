package com.maxifier.mxcache.impl.caches.def;

import gnu.trove.*;

import com.maxifier.mxcache.storage.*;

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

public class FloatLongTroveStorage extends TFloatLongHashMap implements FloatLongStorage {
    public FloatLongTroveStorage() {
    }

    public FloatLongTroveStorage(TFloatHashingStrategy strategy) {
        super(strategy);
    }

    @Override
    public boolean isCalculated(float o) {
        return super.contains(o);
    }

    @Override
    public long load(float o) {
        return super.get(o);
    }

    @Override
    public void save(float o, long t) {
        put(o, t);
    }
}