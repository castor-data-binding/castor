package org.castor.cpa.jpa.info;

import java.util.HashMap;
import java.util.Map;

public class ClassInfoRegistry {

    /**
     * Map for ClassInfo storage
     */
    private static final Map<Class, ClassInfo> classInfos = new HashMap<Class, ClassInfo>();

    /**
     * Returns the {@link ClassInfo} instance registered for the given Class type.
     * @param type A given {@link Class} type.
     * @return The {@link ClassInfo} registered for the given {@link Class} type.
     */
    public static ClassInfo getClassInfo(final Class<?> type) {
        return classInfos.get(type);
    }

    /**
     * Registers a {@link ClassInfo} instance for the given {@link Class} instance.
     * @param type A given {@link Class} type.
     * @param classInfo The {@link ClassInfo} instance to register.
     */
    public static void registerClassInfo(final Class<?> type, ClassInfo classInfo) {
        classInfos.put(type, classInfo);
    }

}
