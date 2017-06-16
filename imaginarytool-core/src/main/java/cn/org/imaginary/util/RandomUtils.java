package cn.org.imaginary.util;

import java.util.*;

/**
 * random utils
 *
 * @author Imaginary
 * @see
 * @since 1.0
 */
public final class RandomUtils {
    private RandomUtils() {
    }

    /**
     * 用于随机选的数字
     */
    private static final String BASE_NUMBER = "0123456789";
    /**
     * 用于随机选的字符
     */
    private static final String BASE_CHAR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";

    private static final String BASSE_CHAR_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * 用于随机选的字符和数字
     */
    private static final String BASE_CHAR_NUMBER = BASE_CHAR_LOWERCASE + BASSE_CHAR_UPPERCASE + BASE_NUMBER;

    /**
     * get random number
     *
     * @return random number
     */
    public static int randomInt() {
        return new Random().nextInt();
    }

    /**
     * get random number with a limit
     *
     * @param limit limit scope
     * @return random number
     */
    public static int randomInt(int limit) {
        return new Random().nextInt(limit);
    }

    /**
     * get random number bettwen a scope,contains start not contains end
     *
     * @param min min number
     * @param max max number
     * @return random number
     */
    public static int randomInt(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    /**
     * get random bytes
     *
     * @param length length
     * @return random byte
     */
    public static byte[] randomByte(int length) {
        byte[] bytes = new byte[length];
        new Random().nextBytes(new byte[length]);
        return bytes;
    }

    /**
     * get random elements from list
     *
     * @param list list
     * @param <T>  type of list
     * @return random element
     */
    public static <T> T randomElement(List<T> list) {
        return null != list ? randomElement(list, list.size()) : null;
    }

    /**
     * get random element from list
     *
     * @param list list
     * @param size random limit
     * @param <T>  random element
     * @return
     */
    public static <T> T randomElement(List<T> list, int size) {
        return null != list ? list.get(new Random().nextInt(size)) : null;
    }

    /**
     * get random count elements from list by count
     *
     * @param list  list
     * @param count count
     * @param <T>   elemet type
     * @return random elements list
     */
    public static <T> List<T> randomElements(List<T> list, int count) {
        final List<T> result = new ArrayList<T>(count);
        while (--count > 0) {
            result.add(randomElement(list));
        }
        return result;
    }

    /**
     * get random elements set by count
     *
     * @param <T>        element type
     * @param collection collection
     * @param count      element count
     * @return random elements set
     * @throws IllegalArgumentException 需要的长度大于给定集合非重复总数
     */
    public static <T> Set<T> randomEleSet(Collection<T> collection, int count) {
        ArrayList<T> source = new ArrayList<>(new HashSet<>(collection));
        if (count > source.size()) {
            throw new IllegalArgumentException("Count is larger than collection distinct size !");
        }
        final HashSet<T> result = new HashSet<T>(count);
        int limit = collection.size();
        while (result.size() < count) {
            result.add(randomElement(source, limit));
        }
        return result;
    }

    /**
     * get random String maked up by numbers and alphabet(lower/upper)
     *
     * @param length length
     * @return random string
     */
    public static String randomString(int length) {
        return randomString(BASE_CHAR_NUMBER, length);
    }

    /**
     * get random string maked up by numbers
     * @param length
     * @return
     */
    public static String randomNumbers(int length) {
        return randomString(BASE_CHAR_NUMBER, length);
    }

    /**
     * get a random string
     *
     * @param baseString base charactors where random string from
     * @param length     random string length
     * @return random string
     */
    public static String randomString(String baseString, int length) {
        final Random random = new Random();
        final StringBuilder stringBuilder = new StringBuilder();
        if (length < 1) {
            length = 1;
        }
        int baseStringLength = baseString.length();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(baseString.charAt(random.nextInt(baseStringLength)));
        }
        return stringBuilder.toString();
    }

    /**
     * get uuid
     *
     * @return uuid
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

}
