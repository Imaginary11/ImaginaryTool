package cn.org.imaginary.util;

/**
 * @author Imaginary
 * @see
 * @since 1.0
 */
public final class ObjectUtils {
    private ObjectUtils() {
    }

    /**
     * boolean obj is null
     * @param obj validate obj parameter
     * @return true or false
     */
    public static boolean isNull(Object obj){
        return null == obj;
    }
}
