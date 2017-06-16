package cn.org.imaginary.http;

import java.util.regex.Pattern;

/**
 * @author Imaginary
 * @see
 * @since 1.0
 */
public final class HttpUtils {
    public static final Pattern CHARSET_PATTERN = Pattern.compile("charset=(.*?)\"");
    public static final String HTTPS ="https://";
    public static final String HTTP ="http://";

    private HttpUtils(){}

    public static boolean isHttps(String url){
        return null == url ? false : url.toLowerCase().startsWith(HTTPS);
    }
}
