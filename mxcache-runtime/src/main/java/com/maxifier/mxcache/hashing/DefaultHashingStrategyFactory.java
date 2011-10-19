package com.maxifier.mxcache.hashing;

import com.maxifier.mxcache.NoSuchInstanceException;
import com.maxifier.mxcache.context.CacheContext;
import gnu.trove.TObjectHashingStrategy;
import gnu.trove.TObjectIdentityHashingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * Created by IntelliJ IDEA.
 * User: dalex
 * Date: 01.07.2010
 * Time: 14:59:26
 * <p>
 * ������� ��������� ����������� ��-���������; �������� ��������� @{@link HashingStrategy},
 * ��� �������� ������������ ����������� �� �����������.
 * ����� �������� � ��������� (Tuple).
 * <p>� ������ ����� ������ ���������������� ������� ��������� � ��� (�� ����������� ����������)
 * <p> � ����������� ������ ����� ��������� ��������� ������������� ��������� ��� �������������� �������� ����������
 * ���������. 
 */
public final class DefaultHashingStrategyFactory extends AbstractHashingStrategyFactory {
    private static final Logger logger = LoggerFactory.getLogger(DefaultHashingStrategyFactory.class);

    private static final DefaultHashingStrategyFactory INSTANCE = new DefaultHashingStrategyFactory();

    public static DefaultHashingStrategyFactory getInstance() {
        return INSTANCE;
    }

    private DefaultHashingStrategyFactory() {}

    /**
     * {@inheritDoc}
     * ��� ���������� ������������ ��������  
     */
    @Override
    protected Object findStrategyClass(CacheContext context, Class paramType, Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof HashingStrategy) {
                //noinspection unchecked
                return instantiate(context, ((HashingStrategy)annotation).value());
            } else if (annotation instanceof IdentityHashing) {
                if (paramType.isPrimitive()) {
                    throw new UnsupportedOperationException();
                }
                if (paramType.isArray()) {
                    // ������� ��-��������� ������������ �� ��������� ���������
                    return null;
                }
                return new TObjectIdentityHashingStrategy();
            }
        }
        if (paramType.isArray()) {
            return getArrayHashingStrategy(paramType);
        }
        return null;
    }

    /**
     * ���� ����� ������ ��������� ��������� ��������� ������ ��������� �����������.
     * ���������� ���� �����, ���� ��������� ������������� ������������� ��� ��������� (��������, ������������ singleton).
     * ����� �� ������ ����������� ����������, ���� ���� ������� �� �������.
     * ���������� ���������� � InstanceProvider.
     * @param context cache context
     * @param strategyClass ����� ���������
     * @return ��������� ��������� ������ ���������; null, ���� ����� ������� �� �������.
     */
    protected <T> T instantiate(CacheContext context, Class<T> strategyClass) {
        try {
            return context.getInstanceProvider().forClass(strategyClass);
        } catch (NoSuchInstanceException e) {
            logger.error("Cannot instantiate strategy of type " + strategyClass, e);
            return null;
        }
    }

    private TObjectHashingStrategy getArrayHashingStrategy(Class paramType) {
        if (paramType == boolean[].class) {
            return BooleanArrayHashingStrategy.getInstance();
        }
        if (paramType == byte[].class) {
            return ByteArrayHashingStrategy.getInstance();
        }
        if (paramType == char[].class) {
            return CharArrayHashingStrategy.getInstance();
        }
        if (paramType == short[].class) {
            return ShortArrayHashingStrategy.getInstance();
        }
        if (paramType == int[].class) {
            return IntArrayHashingStrategy.getInstance();
        }
        if (paramType == long[].class) {
            return LongArrayHashingStrategy.getInstance();
        }
        if (paramType == float[].class) {
            return FloatArrayHashingStrategy.getInstance();
        }
        if (paramType == double[].class) {
            return DoubleArrayHashingStrategy.getInstance();
        }
        if (paramType.isArray()) {
            return ArrayHashingStrategy.getInstance();
        }
        throw new UnsupportedOperationException();
    }
}
