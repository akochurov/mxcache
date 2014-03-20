/*
 * Copyright (c) 2008-2014 Maxifier Ltd. All Rights Reserved.
 */
package com.maxifier.mxcache.clean;

import com.maxifier.mxcache.caches.CleaningNode;
import javax.annotation.Nullable;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.maxifier.mxcache.caches.Cache;

/**
 * ClassCleanableInstanceList
 *
 * @author Andrey Yakoushin (andrey.yakoushin@maxifier.com)
 * @author Alexander Kochurov (alexander.kochurov@maxifier.com)
 */
@SuppressWarnings({"unchecked"})
final class ClassCleanableInstanceList<T> extends WeakList<T> implements CleanableInstanceList {
    private final ClassCleanableInstanceList<? super T> parent;
    private final List<ClassCleanableInstanceList<? extends T>> children;

    private final Class clazz;

    private final Cleanable<T> cleanable;

    private final Map<String, ClassCacheIds> groups;
    private final Map<String, ClassCacheIds> tags;

    private final Lock readLock;
    private final Lock writeLock;

    private volatile int version;

    //------------------------------------------------------------------------------------------------------------------

    public ClassCleanableInstanceList(@Nullable ClassCleanableInstanceList<? super T> parent, Cleanable<T> cleanable, Map<String, ClassCacheIds> groups, Map<String, ClassCacheIds> tags, Class clazz) {
        this.parent = parent;
        this.tags = tags;
        this.groups = groups;
        this.clazz = clazz;
        //noinspection CollectionWithoutInitialCapacity
        children = new ArrayList<ClassCleanableInstanceList<? extends T>>();
        this.cleanable = cleanable;
        if (parent != null) {
            parent.addChild(this);
        }

        ReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    //------------------------------------------------------------------------------------------------------------------

    void addChild(ClassCleanableInstanceList<? extends T> child) {
        writeLock.lock();
        try {
            version++;
            children.add(child);
        } finally {
            writeLock.unlock();
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public int deepLock() {
        readLock.lock();
        int version = this.version;
        for (ClassCleanableInstanceList<? extends T> child : children) {
            version += child.deepLock();
        }
        return version;
    }

    @Override
    public void deepUnlock() {
        for (ClassCleanableInstanceList<? extends T> child : children) {
            child.deepUnlock();
        }
        readLock.unlock();
    }

    @Override
    public void getLists(List<WeakList<?>> lists) {
        lists.add(this);
        for (ClassCleanableInstanceList<? extends T> child : children) {
            child.getLists(lists);
        }
    }

    @Override
    public void getCaches(List<CleaningNode> caches) {
        ClassCleanableInstanceList<? super T> list = this.parent;
        while (list != null) {
            for (T t : this) {
                list.cleanable.appendInstanceCachesTo(caches, t);
            }
            list = list.parent;
        }
        getCachesHierarchically(caches);
    }

    private void getCachesHierarchically(List<CleaningNode> caches) {
        cleanable.appendStaticCachesTo(caches);
        for (T t : this) {
            cleanable.appendInstanceCachesTo(caches, t);
        }
        readLock.lock();
        try {
            for (ClassCleanableInstanceList<? extends T> child : children) {
                child.getCachesHierarchically(caches);
            }
        } finally {
            readLock.unlock();
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    public void clearCache() {
        CleaningHelper.clear(this);
    }

    public void clearCacheByGroup(Object o, String group) {
        T t = (T) o;
        CleaningHelper.lockAndClear(getGroupInstanceCaches(group, t));
    }

    public void clearCacheByTag(Object o, String tag) {
        T t = (T) o;
        CleaningHelper.lockAndClear(getTagInstanceCaches(tag, t));
    }

    private List<Cache> getTagInstanceCaches(String tag, Object t) {
        //noinspection CollectionWithoutInitialCapacity
        List<Cache> caches = new ArrayList<Cache>();
        ClassCleanableInstanceList<? super T> classList = this;
        while (classList != null) {
            classList.appendTagInstanceCaches(tag, t, caches);
            classList = classList.parent;
        }
        return caches;
    }

    private void appendTagInstanceCaches(String tag, Object t, List<Cache> caches) {
        if (tags != null) {
            ClassCacheIds ids = tags.get(tag);
            if (ids != null) {
                ids.appendInstanceCaches(cleanable, t, caches);
            }
        }
    }

    private List<Cache> getGroupInstanceCaches(String tag, Object t) {
        //noinspection CollectionWithoutInitialCapacity
        List<Cache> caches = new ArrayList<Cache>();
        ClassCleanableInstanceList<? super T> classList = this;
        while (classList != null) {
            classList.appendGroupInstanceCaches(tag, t, caches);
            classList = classList.parent;
        }
        return caches;
    }

    private void appendGroupInstanceCaches(String tag, Object t, List<Cache> caches) {
        if (groups != null) {
            ClassCacheIds ids = groups.get(tag);
            if (ids != null) {
                ids.appendInstanceCaches(cleanable, t, caches);
            }
        }
    }

    public void clearCache(Object o) {
        T t = (T) o;
        CleaningHelper.lockAndClear(getInstanceCaches(t));
    }

    private List<CleaningNode> getInstanceCaches(T t) {
        //noinspection CollectionWithoutInitialCapacity
        List<CleaningNode> caches = new ArrayList<CleaningNode>();
        ClassCleanableInstanceList<? super T> classList = this;
        while (classList != null) {
            classList.cleanable.appendInstanceCachesTo(caches, t);
            classList = classList.parent;
        }
        return caches;
    }

    public Cleanable<T> getCleanable() {
        return cleanable;
    }

    @Override
    public String toString() {
        return "ClassCleanableInstanceList[" + clazz + "]"; 
    }
}