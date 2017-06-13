package cn.org.imaginary.http;

import cn.org.imaginary.util.CharsetUtils;
import cn.org.imaginary.util.CollectionUtils;
import cn.org.imaginary.util.StrUtils;
import com.sun.org.apache.regexp.internal.RE;

import java.util.*;

/**
 * the base class of http
 *
 * @param <T> the subclass type
 * @author Imaginary
 * @see
 * @since 1.0
 */
public abstract class HttpBase<T> {

    // http type
    public static final String HTTP_1_0 = "http/1.0";
    public static final String HTTP_1_1 = "http/1.1";
    // store headers
    protected Map<String, List<String>> headers = new HashMap<>();
    // default charset
    protected String charset = CharsetUtils.UTF_8;
    // http version
    protected String httpVersion = HTTP_1_1;
    // store body
    protected String body;

    /**
     * get header by name
     *
     * @param name header name
     * @return header value
     */
    public String getHeader(String name) {
        if (StrUtils.isNotBlank(name)) {
            return null;
        }
        List<String> values = headers.get(name.trim());
        return CollectionUtils.isEmpty(values) ? null : values.get(0);
    }

    /**
     * get header by name
     *
     * @param name enum type name
     * @return header value
     */
    public String getHeader(Header name) {
        return getHeader(name.toString());
    }

    /**
     * set a header if isOverride then replace it
     *
     * @param name       header name
     * @param value      header value
     * @param isOverride is override header value model ?
     * @return this
     */
    public T setHeader(String name, String value, boolean isOverride) {
        if (!StrUtils.isEmpty(name, value)) {
            List<String> values = headers.get(name);
            if (isOverride || CollectionUtils.isEmpty(values)) {
                List<String> valueList = new ArrayList<>();
                valueList.add(value);
                headers.put(name, valueList);
            } else {
                values.add(value);
            }
        }
        return (T) this;
    }

    /**
     * set a header if isOverride then replace it
     *
     * @param name       header name
     * @param value      header value
     * @param isOverride is override header value model ?
     * @return this
     */
    public T setHeader(Header name, String value, boolean isOverride) {
        return setHeader(name.toString(), value, isOverride);
    }

    /**
     * set a header if exists then replace it
     *
     * @param name  header name
     * @param value header value
     * @return this
     */
    public T setHeader(String name, String value) {
        return setHeader(name, value, true);
    }

    /**
     * set a header if exists then replace it
     *
     * @param name  header name
     * @param value header value
     * @return this
     */
    public T setHeader(Header name, String value) {
        return setHeader(name.toString(), value, true);
    }

    /**
     * set a header if exists then replace it
     *
     * @param headers header map
     * @return this
     */
    public T setHeader(Map<String, List<String>> headers) {
        if (CollectionUtils.isEmpty(headers)) {
            return (T) this;
        }
        String name;
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            name = entry.getKey();
            for (String value : entry.getValue()) {
                this.setHeader(name, StrUtils.nullToEmpty(value));
            }
        }
        return (T) this;
    }

    /**
     * remove header by name
     *
     * @param name header name
     * @return this
     */
    public T removeHeader(String name) {
        if (!StrUtils.isEmpty(name)) {
            headers.remove(name.trim());
        }
        return (T) this;
    }

    /**
     * remove header by name
     *
     * @param name Header name of enum type
     * @return this
     */
    public T removeHeader(Header name) {
        return removeHeader(name.toString());
    }

    /**
     * get all headers
     *
     * @return Headers Map
     */
    public Map<String, List<String>> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }


}
