package com.maxifier.mxcache.legacy.layered;

import com.magenta.dataserializator.MxObjectInput;
import com.magenta.dataserializator.MxObjectOutput;
import com.maxifier.mxcache.MxCacheException;
import com.maxifier.mxcache.impl.resource.DependencyNode;
import com.maxifier.mxcache.impl.resource.DependencyTracker;
import com.maxifier.mxcache.impl.resource.ResourceOccupied;
import com.maxifier.mxcache.proxy.Resolvable;
import com.maxifier.mxcache.proxy.MxProxy;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.ref.Reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MxLayeredStrategy<T> extends MxList.Element<MxLayeredStrategy<T>> implements Externalizable, Comparable<MxLayeredStrategy<T>>, Resolvable<T> {
    private static final Logger logger = LoggerFactory.getLogger(MxLayeredStrategy.class);

    private static final long serialVersionUID = -5171623695018264162L;

    private static final float MIN_REUSAGE_TO_PRESERVE = 5.0f;

    private MxLayeredCache<T> manager;

    /**
     * ������ �� ����: ������������ ��� �������� ���� �� ��������� ����
     * (��� ��������� �������� �� weak-������)
     */
    private Reference<MxLayeredStrategy<T>> selfReference;

    private Object[] data;

    private int queries;
    private int lastQueryTime;

    private int count;

    private T shorttimeValue;

    private MxReusageForecastManager<T> reusageForecastManager;

    private float reusageForecast;

    void updateReusageForecast() {
        reusageForecast = reusageForecastManager.getReusageForecast(this);
    }

    Reference<MxLayeredStrategy<T>> getSelfReference() {
        return selfReference;
    }

    void setSelfReference(Reference<MxLayeredStrategy<T>> selfReference) {
        this.selfReference = selfReference;
    }

    /**
     * should be called with synchronization on manager.
     *
     * @return true if this strategy should be preserved
     */
    boolean isConfident() {
        if (reusageForecast >= MIN_REUSAGE_TO_PRESERVE || shorttimeValue != null) {
            return true;
        }
        for (int i = 0; i < count - 1; i++) {
            if (data[i] != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        MxObjectOutput output = (MxObjectOutput) out;
        output.serialize(manager);
        output.serialize(getValue());
        output.writeObject(reusageForecastManager);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        MxObjectInput input = (MxObjectInput) in;
        manager = input.deserialize();
        count = manager.getLayerCount();
        data = new Object[count];

        T stv = (T) input.deserialize();
        reusageForecastManager = (MxReusageForecastManager) input.readObject();

        synchronized (manager) {
            manager.registerStrategy(this);
            manager.addAndUpdate(this);
            shorttimeValue = stv;
        }
    }

    /**
     * @deprecated externalizable use only
     */
    @Deprecated
    public MxLayeredStrategy() {
    }

    public MxLayeredStrategy(MxLayeredCache<T> manager, MxCacheLayer layer, Object value, MxReusageForecastManager<T> reusageForecastManager) {
        this.reusageForecastManager = reusageForecastManager;
        this.manager = manager;
        count = manager.getLayerCount();
        data = new Object[count];

        synchronized (this.manager) {
            if (layer == null) {
                manager.addAndUpdate(this);
                //noinspection unchecked
                shorttimeValue = (T) value;
            } else {
                setValue(layer.getId(), value);
            }
        }
    }

    /**
     * ���� ����� ����� ���������, ������ ���� �� ����� �������, ��� ���� MxLayeredStrategy ������ �� �����������
     * (��������, ���� ������� ������ �� ��������������� �� ������-������).
     * � ��������� ������, ����� ���������� ���������� ������.
     */
    void removeFromAllCaches() {
        if (shorttimeValue != null) {
            shorttimeValue = null;
            manager.removeFromShorttime(this);
        }
        for (int i = 0; i < count - 1; i++) {
            if (data[i] != null) {
                manager.getLayer(i).removeFromCache(this);
                data[i] = null;
            }
        }
        data[count - 1] = null;
    }

    void clearShorttime() {
        if (data[0] == null) {
            setValue(0, shorttimeValue);
        }
        shorttimeValue = null;
    }

    @Override
    public T getValue() {
        DependencyNode callerNode = DependencyTracker.track(DependencyTracker.DUMMY_NODE);
        try {
            while (true) {
                try {
                    return getValue0();
                } catch (ResourceOccupied e) {
                    if (callerNode != null) {
                        throw e;
                    } else {
                        e.getResource().waitForEndOfModification();
                    }
                }
            }
        } finally {
            DependencyTracker.exit(callerNode);
        }
    }

    private T getValue0() {
        synchronized (manager) {
            queries++;
            lastQueryTime = manager.getTime();
            if (shorttimeValue == null) {
                long start = System.nanoTime();
                shorttimeValue = getInternalValue();
                manager.addAndUpdate(this);
                long end = System.nanoTime();
                manager.miss(end - start);
            } else {
                manager.moveToEnd(this);
                manager.hit();
            }
            return shorttimeValue;
        }
    }

    void exitPool(int layer) {
        convertDown(layer, data[layer]);
        data[layer] = null;
    }

    /**
     * ������� ����������� ���� ��� ��������, ������� ��������� ����� ���������� �������������, ��� ������� ������,
     * ��� ���� ��������� ����������� ������� ������, � ���������.
     *
     * @param layerId ������� ����
     * @param value   ��������
     */
    private void convertDown(int layerId, Object value) {
        if (value instanceof MxProxy) {
            logger.warn("MxProxy passed to convertDown(int,Object)");
        }
        float minCost = Float.POSITIVE_INFINITY;
        int minLayer = -1;
        for (int i = layerId + 1; i < count; i++) {
            MxCacheLayer layer = manager.getLayer(i);
            if (data[i] != null || (manager.getConverter().canConvert(layerId, i) && (i == count - 1 || layer.canCache(this)))) {
                float cost = reusageForecast * manager.getConverter().getConvertCost(i, 0, 0);
                if (data[i] == null) {
                    cost += manager.getConverter().getConvertCost(layerId, i, 0);
                }
                cost /= layer.getPreferenceFactor();
                if (minCost > cost) {
                    minCost = cost;
                    minLayer = i;
                }
            }
        }
        if (minLayer == -1) {
            throw new IllegalStateException("Cannot collapse: no layer found");
        }
        if (data[minLayer] == null) {
            if (minLayer != count - 1) {
                if (!manager.getLayer(minLayer).tryToCache(this)) {
                    throw new IllegalStateException("Cannot add to longtime cache");
                }
            }
            data[minLayer] = manager.getConverter().convert(layerId, minLayer, 0, value);
        }
    }

    private void setValue(int layerId, Object value) {
        if (value instanceof MxProxy) {
            logger.warn("MxProxy passed to convertDown(int,Object)");
        }
        if (data[layerId] != null) {
            throw new IllegalArgumentException("Layer " + manager.getLayer(layerId) + " is already in pool");
        }
        if (layerId == count - 1) {
            data[layerId] = value;
        } else {
            if (manager.getLayer(layerId).tryToCache(this)) {
                data[layerId] = value;
            } else {
                convertDown(layerId, value);
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    private T getInternalValue() {
        T v = (T) data[0];
        if (v != null) {
            return v;
        }
        int min = 0;
        float minCost = Float.POSITIVE_INFINITY;
        for (int i = 1; i < count; i++) {
            float cost = manager.getConverter().getConvertCost(i, 0, 0) / manager.getLayer(i).getPreferenceFactor();
            if (data[i] != null && cost < minCost) {
                min = i;
                minCost = cost;
            }
        }
        if (min == 0) {
            throw new MxCacheException("Cannot deconvert " + this + ": all layers are empty");
        }
        return convertAndSave(min);
    }

    @SuppressWarnings ({ "unchecked" })
    private T convertAndSave(int min) {
        T v = (T) manager.getConverter().convert(min, 0, 0, data[min]);
        if (manager.getLayer(0).tryToCache(this)) {
            data[0] = v;
        }
        return v;
    }

    public int getQueries() {
        return queries;
    }

    public int getLastQueryTime() {
        return lastQueryTime;
    }

    @Override
    public int compareTo(MxLayeredStrategy<T> o) {
        return Float.compare(reusageForecast, o.reusageForecast);
    }

    String getKey() {
        StringBuilder builder = new StringBuilder();
        if (shorttimeValue != null) {
            builder.append("SHORTTIME ");
        }
        for (int i = 0; i < count; i++) {
            if (data[i] != null) {
                builder.append(manager.getLayer(i).getName()).append(' ');
            }
        }
        return builder.toString();
    }

    MxLayeredCache<T> getManager() {
        return manager;
    }

    @Override
    public String toString() {
        return getKey();
    }
}
