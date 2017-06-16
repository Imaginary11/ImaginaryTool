package cn.org.imaginary.http;

import cn.org.imaginary.util.CharsetUtils;
import cn.org.imaginary.util.CollectionUtils;
import cn.org.imaginary.util.StrUtils;

import java.nio.charset.Charset;
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
        if (StrUtils.isBlank(name)) {
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
    public T header(String name, String value, boolean isOverride) {
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
    public T header(Header name, String value, boolean isOverride) {
        return header(name.toString(), value, isOverride);
    }

    /**
     * set a header if exists then replace it
     *
     * @param name  header name
     * @param value header value
     * @return this
     */
    public T header(String name, String value) {
        return header(name, value, true);
    }

    /**
     * set a header if exists then replace it
     *
     * @param name  header name
     * @param value header value
     * @return this
     */
    public T header(Header name, String value) {
        return header(name.toString(), value, true);
    }

    /**
     * set a header if exists then replace it
     *
     * @param headers header map
     * @return this
     */
    public T header(Map<String, List<String>> headers) {
        if (CollectionUtils.isEmpty(headers)) {
            return (T) this;
        }
        String name;
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            name = entry.getKey();
            for (String value : entry.getValue()) {
                this.header(name, StrUtils.nullToEmpty(value));
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

    /**
     * get httpVersion
     *
     * @return httpVersion
     */
    public String getHttpVersion() {
        return httpVersion;
    }

    /**
     * set httpVersion
     *
     * @param httpVersion
     * @return HttpBase
     */
    public T httpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
        return (T) this;
    }

    /**
     * return charset
     *
     * @return charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * set charset
     *
     * @param charset charset parameter
     * @return HttpBase
     */
    public T charset(Charset charset) {
        if (null != charset) {
            this.charset = charset.name();
        }
        return (T) this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Request Headers: ").append(StrUtils.CRLF);
        for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
            sb.append("    ").append(entry).append(StrUtils.CRLF);
        }

        sb.append("Request Body: ").append(StrUtils.CRLF);
        sb.append("    ").append(this.body).append(StrUtils.CRLF);

        return sb.toString();
    }


}
