package com.paxovision.skynet.core.data;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SkynetDataContext {
    private static SkynetDataContext instance = null;
    private ThreadLocal<Map<String, Object>> dataCollection = new InheritableThreadLocal<>();

    private SkynetDataContext() {
        log.info("SkynetDataContext created...");
    }

    public static SkynetDataContext getInstance() {
        if (instance == null) {
            instance = new SkynetDataContext();
        }

        if (instance.dataCollection.get() == null) {
            Map<String, Object> data = new ConcurrentHashMap<>();
            instance.dataCollection.set(data);
        }
        return instance;
    }

    public <M> M get(String key) {
        Object value = null;
        if (instance.dataCollection.get() != null) {
            Map<String, Object> data = instance.dataCollection.get();
            if (data.containsKey(key)) {
                value = data.get(key);
                if (value != null) {
                    log.info("Key: " + key + "-> Value: " + value);
                } else {
                    value = null;
                    log.info("Key : " + key + " contains null value");
                }
            } else {
                value = null;
                log.info("Key : " + key + " does not exist in Integration Data Collection");
            }
        }
        return (M) value;
    }

    public <M> M get(SkynetDataContextKey key) {
        return get(key.getValue());
    }

    public Object put(String key, Object value) {
        if (instance.dataCollection.get() != null) {
            Map<String, Object> data = instance.dataCollection.get();
            if (data.containsKey(key)) {
                //Object tmp = data.get(key);
                log.info("Key: " + key + " overriding value: " + value.toString());
                data.put(key, value);
            } else {
                if (value != null) {
                    log.info("Key: " + key + " adding value: " + value);
                    data.put(key, value);
                }
            }
        }
        return value;
    }

    public Object put(SkynetDataContextKey key, Object value) {
        return put(key.getValue(), value);
    }

    /**
     * Check if the key exists.
     * Takes key as String
     *
     * @param key {@link String}
     * @return boolean
     */
    public boolean isKeyExist(String key) {
        return instance.dataCollection.get().containsKey(key);
    }

    /**
     * Check if the key exists
     * Takes key as SkynetDataContextKey
     *
     * @param key {@link SkynetDataContextKey}
     * @return boolean
     */
    public boolean isKeyExist(SkynetDataContextKey key) {
        return isKeyExist(key.getValue());
    }

    public void clear() {
        instance.dataCollection.remove();
    }


}
