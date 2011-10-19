package com.maxifier.mxcache;

import com.maxifier.mxcache.context.CacheContext;
import com.maxifier.mxcache.impl.instanceprovider.DefaultInstanceProvider;

/**
* Created by IntelliJ IDEA.
* User: dalex
* Date: 16.03.11
* Time: 11:13
*/
class DefaultCacheContext implements CacheContext {
    @Override
    public InstanceProvider getInstanceProvider() {
        return DefaultInstanceProvider.getInstance();
    }

    @Override
    public String toString() {
        return "DefaultCacheContext";
    }
}
