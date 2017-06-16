package cn.org.imaginary.http;

import cn.org.imaginary.convert.Convert;
import cn.org.imaginary.http.ssl.SSLSocketFactoryBuilder;
import cn.org.imaginary.util.*;
import com.alibaba.fastjson.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Imaginary
 * @see
 * @since 1.0
 */
public class HttpRequest extends HttpBase<HttpRequest> {

    private static final String BOUNDARY = "--------------------Imaginary" + RandomUtils.randomString(16);
    private static final byte[] BOUNDARY_END = StrUtils.format("--{}--\r\n", BOUNDARY).getBytes();
    private static final String CONTENT_DISPOSITION_TEMPLATE = "Content-Disposition: form-data; name=\"{}\"\r\n\r\n";
    private static final String CONTENT_DISPOSITION_FILE_TEMPLATE = "Content-Disposition: form-data; name=\"{}\"; filename=\"{}\"\r\n";

    private static final String CONTENT_TYPE_X_WWW_FORM_URLENCODED_PREFIX = "application/x-www-form-urlencoded;charset=";
    private static final String CONTENT_TYPE_MULTIPART_PREFIX = "multipart/form-data; boundary=";
    private static final String CONTENT_TYPE_FILE_TEMPLATE = "Content-Type: {}\r\n\r\n";


    private String url = "";
    private HttpMethod method = HttpMethod.GET;
    // default timeout
    private int timeout = -1;
    // store form data
    protected Map<String, Object> form;
    // file form object for upload
    protected Map<String, File> fileForm;
    // connection object
    private HttpConnection httpConnection;
    // disabled cache
    private boolean isDisableCache;
    // redirect
    private Boolean isFollowRedirects;
    // redirect times
    private int redirectCount;
    // proxy
    private Proxy proxy;
    // HostnameVerifier for https
    private HostnameVerifier hostnameVerifier;
    // SSLSocketFactory fro https
    private SSLSocketFactory socketFactory;

    /**
     * build HttpRequest
     *
     * @param url url
     */
    public HttpRequest(String url) {
        this.url = url;
    }

    /**
     * set request method
     *
     * @param method request method
     * @return {@link HttpRequest}
     */
    public HttpRequest method(HttpMethod method) {
        this.method = method;
        return this;
    }

    /**
     * set http get request
     *
     * @param url request url
     * @return {@link HttpRequest}
     */
    public static HttpRequest get(String url) {
        return new HttpRequest(url).method(HttpMethod.GET);
    }

    /**
     * set http post request
     *
     * @param url request url
     * @return {@link HttpRequest}
     */
    public static HttpRequest post(String url) {
        return new HttpRequest(url).method(HttpMethod.POST);
    }

    /**
     * set http put request
     *
     * @param url request url
     * @return {@link HttpRequest}
     */
    public static HttpRequest put(String url) {
        return new HttpRequest(url).method(HttpMethod.PUT);
    }

    /**
     * set http delete request
     *
     * @param url request url
     * @return {@link HttpRequest}
     */
    public static HttpRequest delete(String url) {
        return new HttpRequest(url).method(HttpMethod.DELETE);
    }

    /**
     * set http head request
     *
     * @param url request url
     * @return {@link HttpRequest}
     */
    public static HttpRequest head(String url) {
        return new HttpRequest(url).method(HttpMethod.HEAD);
    }

    /**
     * set http options request
     *
     * @param url request url
     * @return {@link HttpRequest}
     */
    public static HttpRequest options(String url) {
        return new HttpRequest(url).method(HttpMethod.OPTIONS);
    }

    /**
     * set http trace request
     *
     * @param url request url
     * @return {@link HttpRequest}
     */
    public static HttpRequest trace(String url) {
        return new HttpRequest(url).method(HttpMethod.TRACE);
    }

    /**
     * set content type
     *
     * @param contentType content type
     * @return {@link HttpRequest}
     */
    public HttpRequest contentType(String contentType) {
        header(Header.CONTENT_TYPE, contentType);
        return this;
    }

    /**
     * set long connection
     *
     * @param isKeepAlive keep alive
     * @return {@link HttpRequest}
     */
    public HttpRequest keepAlive(boolean isKeepAlive) {
        header(Header.CONNECTION, isKeepAlive ? "Keep-Alive" : "Close");
        return this;
    }

    /**
     * get iskeepAlive
     *
     * @return true or false
     */
    public boolean isKeepAlive() {
        String connection = getHeader(Header.CONNECTION);
        return null == connection ? !httpVersion.equalsIgnoreCase(HTTP_1_0) : !connection.equalsIgnoreCase("close");
    }

    /**
     * get content length
     *
     * @return content length
     */
    public String getContentLength() {
        return getHeader(Header.CONTENT_LENGTH);
    }

    /**
     * set content length
     *
     * @param length length of content
     * @return {@link HttpRequest}
     */
    public HttpRequest contentLength(int length) {
        return header(Header.CONTENT_LENGTH, String.valueOf(length));
    }

    /**
     * set form data
     *
     * @param key   form key
     * @param value form data
     * @return {@link HttpRequest}
     */
    public HttpRequest form(String key, Object value) {
        if (StrUtils.isBlank(key) || ObjectUtils.isNull(value)) {
            return this;
        }

        super.body = null;

        if (value instanceof File) {

        } else if (null == this.form) {
            this.form = new HashMap<>();
        }

        String valuesStr;

        if (value instanceof List) {
            valuesStr = CollectionUtils.join((List<?>) value, ",");
        } else if (value instanceof Array) {
            valuesStr = ArrayUtils.join((Object[]) value, ",");
        } else {
            valuesStr = Convert.toStr(value, null);
        }
        this.form.put(key, valuesStr);
        return this;
    }

    /**
     * set from data
     *
     * @param key       key
     * @param value     value
     * @param parameter key,value,key,value....
     * @return
     */
    public HttpRequest form(String key, Object value, Object... parameter) {
        form(key, value);
        for (int i = 0; i < parameter.length; i += 2) {
            key = parameter[i].toString();
            form(key, parameter[i + 1]);
        }
        return this;
    }

    /**
     * set form data by map
     *
     * @param formMap map type if form data
     * @return {@link HttpRequest}
     */
    public HttpRequest form(Map<String, Object> formMap) {
        for (Map.Entry<String, Object> entry : formMap.entrySet()) {
            form(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * set file type form data
     *
     * @param key   form key
     * @param value form value
     * @return {@link HttpRequest}
     */
    public HttpRequest form(String key, File value) {
        if (null == value) {
            return this;
        }
        if (!isKeepAlive()) {
            keepAlive(true);
        }
        if (null == this.fileForm) {
            this.fileForm = new HashMap<>();
        }
        this.fileForm.put(key, value);
        return this;
    }

    /**
     * get form
     *
     * @return map type form data
     */
    public Map<String, Object> getForm() {
        return this.form;
    }

    /**
     * set content body
     *
     * @param body body content
     * @return {@link HttpRequest}
     */
    public HttpRequest body(String body) {
        this.body = body;
        // diabled form when body enabled
        this.form = null;
        contentLength(body.length());
        return this;
    }

    /**
     * set body and content type
     *
     * @param body        body content
     * @param contentType contentType
     * @return {@link HttpRequest}
     */
    public HttpRequest body(String body, String contentType) {
        this.body(body);
        this.contentType(contentType);
        return this;
    }

    /**
     * set json type body
     *
     * @param json json body
     * @return {@link HttpRequest}
     */
    public HttpRequest body(JSONObject json) {
        this.body(json.toJSONString());
        String jsonContentType = "application/json";
        if (!StrUtils.isBlank(this.charset)) {
            jsonContentType = StrUtils.format("{};charset={}", jsonContentType, this.charset);
        }
        this.contentType(jsonContentType);
        return this;
    }

    /**
     * set body bytes
     *
     * @param bodyBytes
     * @return {@link HttpRequest}
     */
    public HttpRequest body(byte[] bodyBytes) {
        return body(StrUtils.str(bodyBytes, this.charset));
    }

    /**
     * set timeout
     *
     * @param timeout timeout
     * @return {@link HttpRequest}
     */
    public HttpRequest timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * disable cache
     *
     * @return {@link HttpRequest}
     */
    public HttpRequest disableCache() {
        this.isDisableCache = true;
        return this;
    }

    /**
     * open redirects
     *
     * @param isFollowRedirects isRedirects
     * @return {@link HttpRequest}
     */
    public HttpRequest setFollowRedirects(boolean isFollowRedirects) {
        this.isFollowRedirects = isFollowRedirects;
        return this;
    }

    /**
     * set hostname verifier ,trust any host if hostnameVerifier null
     *
     * @param hostnameVerifier hostnameVerifier
     * @return {@link HttpRequest}
     */
    public HttpRequest setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    /**
     * set socketfactory
     *
     * @param socketFactory socketFactory
     * @return {@link HttpRequest}
     */
    public HttpRequest setSocketFactory(SSLSocketFactory socketFactory) {
        this.socketFactory = socketFactory;
        return this;
    }

    /**
     * set proxy
     *
     * @param proxy proxy
     * @return {@link HttpRequest}
     */
    public HttpRequest setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    /**
     * set https protocol
     *
     * @param protocol protocol
     * @return {@link HttpRequest}
     */
    public HttpRequest setSSLProtocol(String protocol) {
        if (null == socketFactory) {
            try {
                this.socketFactory = SSLSocketFactoryBuilder.create().setProtocol(protocol).build();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }
        return this;
    }


}
