package com.maxifier.mxcache.impl.caches.def;

import com.maxifier.mxcache.storage.IntBooleanStorage;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * Project: Maxifier
 * Created by: Yakoushin Andrey
 * Date: 15.02.2010
 * Time: 13:40:10
 * <p/>
 * Copyright (c) 1999-2009 Magenta Corporation Ltd. All Rights Reserved.
 * Magenta Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * @author ELectronic ENgine
 */
@Test
public class IntBooleanCacheTest {
    public void testCache() {
        IntBooleanStorage cache = new IntBooleanTroveStorage();

        assert cache.size() == 0;

        assert !cache.isCalculated(42);

        cache.save(42, true);

        assert cache.size() == 1;
        Assert.assertEquals(cache.load(42), true);

        cache.clear();

        assert !cache.isCalculated(42);
    }
}
