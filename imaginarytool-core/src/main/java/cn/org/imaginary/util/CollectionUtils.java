package cn.org.imaginary.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Imaginary
 * @see
 * @since 1.0
 */
public final class CollectionUtils {
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

    /**
     * replace list to str with special conjunction
     *
     * @param <T>         list type
     * @param iterable    {@link Iterable}
     * @param conjunction special conjunction
     * @return string that connect by conjunction
     */
    public static <T> String join(Iterable<T> iterable, CharSequence conjunction) {
        if (null == iterable) {
            return null;
        }
        return join(iterable.iterator(), conjunction);
    }

    /**
     * replace list to str with special conjunction
     *
     * @param <T>         list type
     * @param iterator    {@link Iterator}
     * @param conjunction special conjunction
     * @return string that connect by conjunction
     */
    public static <T> String join(Iterator<T> iterator, CharSequence conjunction) {
        if (null == iterator) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        T item;
        while (iterator.hasNext()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }

            item = iterator.next();
            if (ArrayUtils.isArray(item)) {
                sb.append(ArrayUtils.join(ArrayUtils.wrap(item), conjunction));
            } else if (item instanceof Iterable<?>) {
                sb.append(join((Iterable<?>) item, conjunction));
            } else if (item instanceof Iterator<?>) {
                sb.append(join((Iterator<?>) item, conjunction));
            } else {
                sb.append(item);
            }
        }
        return sb.toString();
    }
}
