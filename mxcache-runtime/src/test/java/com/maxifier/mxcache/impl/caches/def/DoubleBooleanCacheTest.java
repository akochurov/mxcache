/*
 * Copyright (c) 2008-2014 Maxifier Ltd. All Rights Reserved.
 */
package com.maxifier.mxcache.impl.caches.def;

import com.maxifier.mxcache.storage.DoubleBooleanStorage;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * DoubleBooleanCacheTest - test for DoubleBooleanTroveStorage
 *
 * THIS IS GENERATED CLASS! DON'T EDIT IT MANUALLY!
 *
 * GENERATED FROM P2PCacheTest.template
 *
 * @author Andrey Yakoushin (andrey.yakoushin@maxifier.com)
 * @author Alexander Kochurov (alexander.kochurov@maxifier.com)
 */
@Test
public class DoubleBooleanCacheTest {
    public void testCache() {
        DoubleBooleanStorage cache = new DoubleBooleanTroveStorage();

        assert cache.size() == 0;

        assert !cache.isCalculated(42d);

        cache.save(42d, true);

        assert cache.size() == 1;
        Assert.assertEquals(cache.load(42d), true);

        cache.clear();

        assert !cache.isCalculated(42d);
    }
}
