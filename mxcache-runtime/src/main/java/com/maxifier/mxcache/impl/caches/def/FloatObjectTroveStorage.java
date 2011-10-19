package com.maxifier.mxcache.impl.caches.def;

import gnu.trove.*;

import com.maxifier.mxcache.storage.*;
import static com.maxifier.mxcache.impl.caches.def.TroveHelper.*;

/**
 * Project: Maxifier
 * Created by: Yakoushin Andrey
 * Date: 15.02.2010
 * Time: 13:29:47
 * <p/>
 * Copyright (c) 1999-2009 Magenta Corporation Ltd. All Rights Reserved.
 * Magenta Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * @author ELectronic ENgine
 */
public class FloatObjectTroveStorage<T> extends TFloatObjectHashMap<Object> implements FloatObjectStorage<T> {
    public FloatObjectTroveStorage() {
    }

    public FloatObjectTroveStorage(TFloatHashingStrategy strategy) {
        super(strategy);        
    }

    @Override
    public Object load(float key) {
        Object v = get(key);
        if (v == null) {
            return UNDEFINED;
        }
        if (v == NULL_REPLACEMENT) {
            return null;
        }
        return v;
    }

    @Override
    public void save(float key, T value) {
        put(key, value == null ? NULL_REPLACEMENT : value);
    }
}
