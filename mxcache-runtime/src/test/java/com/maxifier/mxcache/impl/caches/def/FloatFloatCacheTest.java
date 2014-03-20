/*
 * Copyright (c) 2008-2014 Maxifier Ltd. All Rights Reserved.
 */
package com.maxifier.mxcache.impl.caches.def;

import com.maxifier.mxcache.storage.FloatFloatStorage;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * FloatFloatCacheTest - test for FloatFloatTroveStorage
 *
 * THIS IS GENERATED CLASS! DON'T EDIT IT MANUALLY!
 *
 * GENERATED FROM P2PCacheTest.template
 *
 * @author Andrey Yakoushin (andrey.yakoushin@maxifier.com)
 * @author Alexander Kochurov (alexander.kochurov@maxifier.com)
 */
@Test
public class FloatFloatCacheTest {
    public void testCache() {
        FloatFloatStorage cache = new FloatFloatTroveStorage();

        assert cache.size() == 0;

        assert !cache.isCalculated(42f);

        cache.save(42f, 42f);

        assert cache.size() == 1;
        Assert.assertEquals(cache.load(42f), 42f);

        cache.clear();

        assert !cache.isCalculated(42f);
    }
}