package com.maxifier.mxcache.impl.caches.def;

import com.maxifier.mxcache.storage.Storage;
import com.maxifier.mxcache.storage.*;
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
public class ShortObjectCacheTest {
    public void testCache() {
        ShortObjectStorage cache = new ShortObjectTroveStorage();

        assert cache.size() == 0;

        assert cache.load((short)42) == Storage.UNDEFINED;

        cache.save((short)42, "123");

        assert cache.size() == 1;
        Assert.assertEquals(cache.load((short)42), "123");

        cache.save((short)42, null);

        assert cache.size() == 1;
         Assert.assertNull(cache.load((short)42));

        cache.clear();

        assert cache.load((short)42) == Storage.UNDEFINED;
  }
}