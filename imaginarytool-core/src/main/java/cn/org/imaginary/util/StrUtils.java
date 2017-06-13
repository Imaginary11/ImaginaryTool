package cn.org.imaginary.util;

/**
 * str utils
 *
 * @author Imaginary
 * @see
 * @since 1.0
 */
public class StrUtils {

    public static final char C_SPACE = ' ';
    public static final char C_TAB = '	';
    public static final char C_DOT = '.';
    public static final char C_SLASH = '/';
    public static final char C_BACKSLASH = '\\';
    public static final char C_CR = '\r';
    public static final char C_LF = '\n';
    public static final char C_UNDERLINE = '_';
    public static final char C_COMMA = ',';
    public static final char C_DELIM_START = '{';
    public static final char C_DELIM_END = '}';
    public static final char C_COLON = ':';

    public static final String SPACE = " ";
    public static final String TAB = "	";
    public static final String DOT = ".";
    public static final String DOUBLE_DOT = "..";
    public static final String SLASH = "/";
    public static final String BACKSLASH = "\\";
    public static final String EMPTY = "";
    public static final String CR = "\r";
    public static final String LF = "\n";
    public static final String CRLF = "\r\n";
    public static final String UNDERLINE = "_";
    public static final String COMMA = ",";
    public static final String DELIM_START = "{";
    public static final String DELIM_END = "}";
    public static final String COLON = ":";

    public static final String HTML_NBSP = "&nbsp;";
    public static final String HTML_AMP = "&amp";
    public static final String HTML_QUOTE = "&quot;";
    public static final String HTML_LT = "&lt;";
    public static final String HTML_GT = "&gt;";

    public static final String EMPTY_JSON = "{}";

    private StrUtils() {
    }

    /**
     * is charsequence blank for contains sub season： <br>
     * 1. null <br>
     * 2. null space <br>
     * 3. ""<br>
     *
     * @param str a str of check
     * @return true or false
     */
    public static boolean isNotBlank(CharSequence str) {
        int length;
        if (null == str || (length = str.length()) == 0) {
            return true;
        }
        for (int index = 0; index < length; index++) {
            if (!NumberUtils.isBlankChar(str.charAt(index))) {
                return false;
            }
        }
        return true;
    }

    /**
     * is charsequence blank for contains sub season： <br>
     * 1. null <br>
     * 2. ""<br>
     *
     * @param str a str of check
     * @return true or false
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * is charsequence blank for contains sub season： <br>
     * 1. null <br>
     * 2. ""<br>
     *
     * @param strs a series strs of check
     * @return true or false
     */
    public static boolean isEmpty(CharSequence... strs) {
        if (strs == null || strs.length == 0) {
            return false;
        }
        for (CharSequence charSequence : strs) {
            if (isEmpty(charSequence)) {
                return false;
            }
        }
        return true;
    }

    /**
     * is charsequence not blank for contains sub season： <br>
     * 1. null <br>
     * 2. ""<br>
     *
     * @param str a str of check
     * @return true or false
     */
    public static boolean isNotEmpty(CharSequence str) {
        return false == isEmpty(str);
    }


    /**
     * 当给定字符串为null时，转换为Empty
     *
     * @param str 被转换的字符串
     * @return 转换后的字符串
     */
    public static String nullToEmpty(CharSequence str) {
        return nullToDefault(str, EMPTY);
    }

    /**
     * 如果字符串是<code>null</code>，则返回指定默认字符串，否则返回字符串本身。
     * <p>
     * <pre>
     * nullToDefault(null, &quot;default&quot;)  = &quot;default&quot;
     * nullToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;&quot;
     * nullToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
     * nullToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
     * </pre>
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     */
    public static String nullToDefault(CharSequence str, String defaultStr) {
        return (str == null) ? defaultStr : str.toString();
    }
}
