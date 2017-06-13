package cn.org.imaginary.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author Imaginary
 * @see
 * @since 1.0
 */
public class CollectionUtils {
    private CollectionUtils() {
    }

    /**
     * is collection empty ?
     *
     * @param collection collection
     * @return true or false
     */
    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }

    /**
     * is map empty ?
     *
     * @param map map
     * @return true or false
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
