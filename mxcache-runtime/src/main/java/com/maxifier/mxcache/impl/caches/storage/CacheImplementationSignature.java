package com.maxifier.mxcache.impl.caches.storage;

import com.maxifier.mxcache.transform.TransformGenerator;
import org.jetbrains.annotations.NotNull;

/**
* Created by IntelliJ IDEA.
* User: dalex
* Date: 15.10.2010
* Time: 9:19:25
*/
class CacheImplementationSignature {
    private final Class cacheClass;
    private final Class calculatableClass;
    private final Class storageClass;
    @NotNull
    private final TransformGenerator userKeyTransformer;
    @NotNull
    private final TransformGenerator userValueTransformer;
    private final boolean perElementLocking;

    private final int hash;

    public CacheImplementationSignature(Class cacheClass, Class calculatableClass, Class storageClass, @NotNull TransformGenerator userKeyTransformer, @NotNull TransformGenerator userValueTransformer, boolean perElementLocking) {
        this.cacheClass = cacheClass;
        this.calculatableClass = calculatableClass;
        this.storageClass = storageClass;
        this.userKeyTransformer = userKeyTransformer;
        this.userValueTransformer = userValueTransformer;
        this.perElementLocking = perElementLocking;

        int hash = cacheClass.hashCode();
        hash = 31 * hash + storageClass.hashCode();
        hash = 31 * hash + userKeyTransformer.hashCode();
        hash = 31 * hash + userValueTransformer.hashCode();
        this.hash = perElementLocking ? hash : ~hash;
    }

    public Class getCacheClass() {
        return cacheClass;
    }

    public Class getCalculatableClass() {
        return calculatableClass;
    }

    public Class getStorageClass() {
        return storageClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CacheImplementationSignature w = (CacheImplementationSignature) o;
        return cacheClass == w.cacheClass &&
                storageClass == w.storageClass &&
                userKeyTransformer.equals(w.userKeyTransformer) &&
                userValueTransformer.equals(w.userValueTransformer) &&
                perElementLocking == w.perElementLocking;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
