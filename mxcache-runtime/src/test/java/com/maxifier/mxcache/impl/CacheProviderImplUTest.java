package com.maxifier.mxcache.impl;

import com.maxifier.mxcache.*;
import com.maxifier.mxcache.caches.Cache;
import com.maxifier.mxcache.caches.*;
import com.maxifier.mxcache.context.CacheContext;
import com.maxifier.mxcache.impl.instanceprovider.DefaultInstanceProvider;
import com.maxifier.mxcache.impl.resource.DependencyNode;
import com.maxifier.mxcache.impl.resource.DependencyTracker;
import com.maxifier.mxcache.provider.StorageFactory;
import com.maxifier.mxcache.storage.IntStorage;
import com.maxifier.mxcache.storage.ObjectObjectStorage;
import com.maxifier.mxcache.storage.Storage;
import com.maxifier.mxcache.interfaces.Statistics;
import com.maxifier.mxcache.provider.CacheDescriptor;
import com.maxifier.mxcache.provider.CacheManager;
import com.maxifier.mxcache.provider.CachingStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.ref.Reference;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;
import static org.testng.Assert.fail;

@Test
public class CacheProviderImplUTest {
    private static final String TEST_LOAD_FINGERPRINT = "thisIsStorage";
    private static final int TEST_SIZE_FINGERPRINT = 71;

    private static final int TEST_CALCULATABLE_FINGERPRINT = 0xBABE;
    private static final int TEST_CACHE_FINGERPRINT = 0xCAFE;

    static class Y implements CachingStrategy {
        @NotNull
        @Override
        public <T> CacheManager<T> getManager(CacheContext context, CacheDescriptor<T> descriptor) {
            throw new UnsupportedOperationException();
        }
    }

    static class Z extends X {
        Z() {
            super(67);
        }

        Z(int v) {
            super(v);
        }
    }

    static class X implements CachingStrategy {
        private final int v;

        X() {
            this(132);
        }

        X(int v) {
            this.v = v;
        }


        @NotNull
        @Override
        public synchronized <T> CacheManager<T> getManager(CacheContext context, final CacheDescriptor<T> descriptor) {
            return new CacheManager<T>() {
                @Override
                public CacheDescriptor<T> getDescriptor() {
                    return descriptor;
                }

                @Override
                public Cache createCache(@Nullable T owner) {
                    return new IntCache() {
                        @Override
                        public int getOrCreate() {
                            return v;
                        }

                        @Override
                        public Lock getLock() {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public void clear() {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public int size() {
                            throw new UnsupportedOperationException();
                        }

                        @Override
                        public Statistics getStatistics() {
                            return null;
                        }

                        @Override
                        public CacheDescriptor getDescriptor() {
                            return null;
                        }

                        @Override
                        public DependencyNode getDependencyNode() {
                            return DependencyTracker.DUMMY_NODE;
                        }
                    };
                }

                @Override
                public List<Cache> getInstances() {
                    return Collections.emptyList();
                }

                @Override
                public String getImplementationDetails() {
                    return IntCache.class.getCanonicalName();
                }

                @Override
                public CacheContext getContext() {
                    return null;
                }
            };
        }
    }

    @Cached
    @Strategy(X.class)
    public int x() {
        throw new UnsupportedOperationException();
    }

    @Cached
    public int y() {
        throw new UnsupportedOperationException();
    }

    @Cached
    @UseStorageFactory(TestStorageFactory.class)
    public int z() {
        throw new UnsupportedOperationException();
    }

    @Cached
    @UseStorageFactory(InvalidStorageFactory.class)
    public int w() {
        throw new UnsupportedOperationException();
    }

    @UseStorage(TestStorage1.class)
    public String wStorage(String v) {
        return v;
    }

    @UseStorage(TestStorage3.class)
    public String wStorage3(String v) {
        return v;
    }

    @UseStorage(TestStorage2.class)
    public String wStorage2(String v) {
        return v;
    }

    public static class TestStorage1 implements ObjectObjectStorage {

        @Override
        public Object load(Object key) {
            return TEST_LOAD_FINGERPRINT;
        }

        @Override
        public void save(Object key, Object value) {
        }

        @Override
        public void clear() {
        }

        @Override
        public int size() {
            return TEST_SIZE_FINGERPRINT;
        }
    }

    public static class TestStorage3 implements ObjectObjectStorage {
        public TestStorage3(CacheDescriptor descriptor, CacheContext context) {
        }

        @Override
        public Object load(Object key) {
            return TEST_LOAD_FINGERPRINT;
        }

        @Override
        public void save(Object key, Object value) {
        }

        @Override
        public void clear() {
        }

        @Override
        public int size() {
            return TEST_SIZE_FINGERPRINT;
        }
    }

    public static class TestStorage2 implements ObjectObjectStorage {
        @SuppressWarnings({ "UnusedDeclaration" })
        public TestStorage2(String x) {
            fail("This method should not be called");
        }

        @Override
        public Object load(Object key) {
            fail("This method should not be called");
            return null;
        }

        @Override
        public void save(Object key, Object value) {
            fail("This method should not be called");
        }

        @Override
        public void clear() {
            fail("This method should not be called");
        }

        @Override
        public int size() {
            fail("This method should not be called");
            return 0;
        }
    }

    public static class TestStorageFactory implements StorageFactory {
        @SuppressWarnings({ "UnusedDeclaration" })
        public TestStorageFactory(CacheDescriptor descriptor) {
        }

        @NotNull
        @Override
        public Storage createStorage(Object owner) throws InvocationTargetException, IllegalAccessException, InstantiationException {
            return new TestStorage();
        }

        @Override
        public String getImplementationDetails() {
            return null;
        }
    }

    public static class InvalidStorageFactory implements StorageFactory {
        @NotNull
        @Override
        public Storage createStorage(Object owner) throws InvocationTargetException, IllegalAccessException, InstantiationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getImplementationDetails() {
            return null;
        }
    }

    public void testCustomManager() {
        CacheProviderImpl p = new CacheProviderImpl(false);
        p.registerCache(getClass(), 0, null, int.class, null, null, new IntCalculatable() {
            @Override
            public int calculate(Object owner) {
                throw new UnsupportedOperationException();
            }
        }, "z", "()I", null);
        IntCache c = (IntCache) p.createCache(getClass(), 0, this, CacheFactory.getDefaultContext());
        Assert.assertEquals(c.getOrCreate(), TEST_CACHE_FINGERPRINT);
    }

    public void testInvalidCustomManager() {
        CacheProviderImpl p = new CacheProviderImpl(false);
        p.registerCache(getClass(), 0, null, int.class, null, null, new IntCalculatable() {
            @Override
            public int calculate(Object owner) {
                return TEST_CALCULATABLE_FINGERPRINT;
            }
        }, "w", "()I", null);
        // invalid cache manager doesn't create caches, it throws exceptions
        IntCache c = (IntCache) p.createCache(getClass(), 0, this, CacheFactory.getDefaultContext());
        Assert.assertEquals(c.getOrCreate(), TEST_CALCULATABLE_FINGERPRINT);
    }

    @SuppressWarnings({ "unchecked" })
    public void testCustomStorage() {
        CacheProviderImpl p = new CacheProviderImpl(false);
        p.registerCache(getClass(), 0, String.class, String.class, null, null, new ObjectObjectCalculatable<String, String>() {
            @Override
            public String calculate(Object owner, String s) {
                throw new UnsupportedOperationException();
            }
        }, "wStorage", "(Ljava/lang/String;)Ljava/lang/String;", null);

        ObjectObjectCalculatable<String, String> calculatable = mock(ObjectObjectCalculatable.class);

        ObjectObjectCache<String, String> c = (ObjectObjectCache) p.createCache(getClass(), 0, this, CacheFactory.getDefaultContext());
        assertEquals(c.size(), TEST_SIZE_FINGERPRINT);
        assertEquals(c.getOrCreate("test"), TEST_LOAD_FINGERPRINT);
        verifyZeroInteractions(calculatable);
    }

    @SuppressWarnings( { "unchecked" })
    public void testCustomStorageWithCustomConstructor() {
        CacheProviderImpl p = new CacheProviderImpl(false);
        p.registerCache(getClass(), 0, String.class, String.class, null, null, new ObjectObjectCalculatable<String, String>() {
            @Override
            public String calculate(Object owner, String s) {
                throw new UnsupportedOperationException();
            }
        }, "wStorage3", "(Ljava/lang/String;)Ljava/lang/String;", null);

        ObjectObjectCalculatable<String, String> calculatable = mock(ObjectObjectCalculatable.class);

        ObjectObjectCache<String, String> c = (ObjectObjectCache) p.createCache(getClass(), 0, this, CacheFactory.getDefaultContext());
        assertEquals(c.size(), TEST_SIZE_FINGERPRINT);
        assertEquals(c.getOrCreate("test"), TEST_LOAD_FINGERPRINT);
        verifyZeroInteractions(calculatable);
    }

    @SuppressWarnings({ "unchecked" })
    public void testInvalidStorage() {
        CacheProviderImpl p = new CacheProviderImpl(false);
        p.registerCache(getClass(), 0, String.class, String.class, null, null, new ObjectObjectCalculatable<String, String>() {
            @Override
            public String calculate(Object owner, String v) {
                if (v.equals("test")) {
                    return "it's ok";
                }
                throw new UnsupportedOperationException();
            }
        }, "wStorage2", "(Ljava/lang/String;)Ljava/lang/String;", null);

        ObjectObjectCache<String, String> c = (ObjectObjectCache) p.createCache(getClass(), 0, this, CacheFactory.getDefaultContext());
        // it will use default cause it cannot create TestStorage2
        assertEquals(c.size(), 0);
        assertEquals(c.getOrCreate("test"), "it's ok");
        assertEquals(c.size(), 1);
    }

    public void testBind() {
        CacheProviderImpl p = new CacheProviderImpl(false);
        p.registerCache(getClass(), 0, null, int.class, null, null, new IntCalculatable() {
            @Override
            public int calculate(Object owner) {
                throw new UnsupportedOperationException();
            }
        }, "x", "()I", null);


        DefaultInstanceProvider.getInstance().bind(X.class).toInstance(new X(77));

        // ��� ��������� ��������� ������� ����������
        IntCache � = (IntCache) p.createCache(this.getClass(), 0, this, CacheFactory.getDefaultContext());
        assert �.getOrCreate() == 77;

        DefaultInstanceProvider.getInstance().clearBinding(X.class);
    }

    public void testGetCaches() {
        CacheProviderImpl p = new CacheProviderImpl(false);
        p.registerCache(this.getClass(), 0, null, int.class, null, null, new IntCalculatable() {
            @Override
            public int calculate(Object owner) {
                return 132;
            }
        }, "y", "()I", null);

        // ������� ���, ����� �������� �������� ����������
        Cache c = p.createCache(this.getClass(), 0, this, CacheFactory.getDefaultContext());

        // we will test cache getCaches()
        List<CacheManager> caches = p.getCaches();
        assert caches.size() == 1;
        CacheDescriptor descriptor = caches.get(0).getDescriptor();
        assert descriptor.getOwnerClass() == this.getClass();
        assert descriptor.getId() == 0;

        assert c != null;
    }

    public void testDefault() {
        CacheProviderImpl p = new CacheProviderImpl(false);
        p.registerCache(this.getClass(), 0, null, int.class, null, null, new IntCalculatable() {
            @Override
            public int calculate(Object owner) {
                return 132;
            }
        }, "y", "()I", null);
        
        IntCache � = (IntCache) p.createCache(this.getClass(), 0, this, CacheFactory.getDefaultContext());
        assert �.getOrCreate() == 132;
        assert �.getOrCreate() == 132;
    }

    private static class TestStorage implements IntStorage {
        @Override
        public boolean isCalculated() {
            return true;
        }

        @Override
        public int load() {
            return TEST_CACHE_FINGERPRINT;
        }

        @Override
        public void save(int v) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return 0;
        }
    }
}