/*
 * Copyright (c) 2008-2014 Maxifier Ltd. All Rights Reserved.
 */
package com.maxifier.mxcache.impl.caches.def;

import com.maxifier.mxcache.storage.Storage;
import com.maxifier.mxcache.storage.*;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * LongObjectCacheTest - test for LongObjectTroveStorage
 *
 * THIS IS GENERATED CLASS! DON'T EDIT IT MANUALLY!
 *
 * GENERATED FROM #SOURCE#
 *
 * @author Andrey Yakoushin (andrey.yakoushin@maxifier.com)
 * @author Alexander Kochurov (alexander.kochurov@maxifier.com)
 */
@Test
public class LongObjectCacheTest {
    public void testCache() {
        LongObjectStorage cache = new LongObjectTroveStorage();

        assert cache.size() == 0;

        assert cache.load(42L) == Storage.UNDEFINED;

        cache.save(42L, "123");

        assert cache.size() == 1;
        Assert.assertEquals(cache.load(42L), "123");

        cache.save(42L, null);

        assert cache.size() == 1;
         Assert.assertNull(cache.load(42L));

        cache.clear();

        assert cache.load(42L) == Storage.UNDEFINED;
  }
}