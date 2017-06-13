package cn.org.imaginary.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * a cookie pool which simulate the browser cookie
 *
 * @author Imaginary
 * @see
 * @since 1.0
 */
public class CookiePool {
    // key : host ,value : cookie
    private static Map<String, String> cookies = new ConcurrentHashMap<>();

    /**
     * get cookie by host
     *
     * @param host website host
     * @return cookie website cookie
     */
    public static String get(String host) {
        return cookies.get(host);
    }

    /**
     * set cookie to pool
     *
     * @param host   website host
     * @param cookie website cookie
     */
    public static void set(String host, String cookie) {
        cookies.put(host, cookie);
    }

}
