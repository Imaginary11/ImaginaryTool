package cn.org.imaginary.util;

/**
 * @author Imaginary
 * @see
 * @since 1.0
 */
public class ArraysUtils {
    private ArraysUtils() {
    }

    /**
     * 数组是否为非空
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为非空
     */
    public static <T> boolean isNotEmpty(final T[] array) {
        return (array != null && array.length != 0);
    }
}
