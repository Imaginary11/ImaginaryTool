package cn.org.imaginary.util;

import com.sun.xml.internal.ws.util.UtilException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Imaginary
 * @see
 * @since 1.0
 */
public final class URLUtils {
    private URLUtils() {
    }

    /**
     * 通过一个字符串形式的URL地址创建URL对象
     * @param url URL
     * @return URL对象
     */
    public static URL url(String url){
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new UtilException(e.getMessage(), e);
        }
    }
}
