package cn.org.imaginary.http;

import cn.org.imaginary.http.ssl.SSLSocketFactoryBuilder;
import cn.org.imaginary.http.ssl.TrustAnyHostnameVerifier;
import cn.org.imaginary.lang.Validator;
import cn.org.imaginary.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

/**
 * http connection obj
 *
 * @author Imaginary
 * @see
 * @since 1.0
 */
public class HttpConnection {
    private final static Logger logger = LoggerFactory.getLogger(HttpConnection.class);
    private URL url;
    private HttpMethod httpMethod;
    private Proxy proxy;
    private HttpURLConnection connection;

    /**
     * build http connection
     *
     * @param urlStr           URL
     * @param method           request method
     * @param hostnameVerifier hostnameVerifier
     * @param ssf              SSLSocketFactory
     * @param timeout          timeout
     * @param proxy            proxy
     */
    public HttpConnection(String urlStr, HttpMethod method, HostnameVerifier hostnameVerifier, SSLSocketFactory ssf, int timeout, Proxy proxy) {
        if (StrUtils.isBlank(urlStr)) {
            throw new HttpException("Url is empty");
        }
        if (!Validator.isUrl(urlStr)) {
            throw new HttpException("{} is not a url", urlStr);
        }

        this.url = URLUtils.url(urlStr);
        this.httpMethod = ObjectUtils.isNull(method) ? HttpMethod.GET : method;
        this.proxy = proxy;

        try {
            connection = HttpUtils.isHttps(urlStr) ? openHttps(hostnameVerifier, ssf) : openHttp();
        } catch (Exception e) {
            throw new HttpException(e.getMessage(), e);
        }
        if (timeout > 0) {
            this.setConnectionAndReadTimeout(timeout);
        }
        initConnection();

    }

    /**
     * build http connection
     *
     * @param urlStr     URL
     * @param httpMethod request method
     */
    public HttpConnection(String urlStr, HttpMethod httpMethod) {
        this(urlStr, httpMethod, null, null, 0, null);
    }

    /**
     * build http connection
     *
     * @param urlStr     URL
     * @param httpMethod request method
     * @param timeout    request timeout
     */
    public HttpConnection(String urlStr, HttpMethod httpMethod, int timeout) {
        this(urlStr, httpMethod, null, null, timeout, null);
    }

    /**
     * static build http connection
     *
     * @param urlStr     URL
     * @param httpMethod request method
     * @return {@link HttpsURLConnection}
     */
    public static HttpConnection create(String urlStr, HttpMethod httpMethod) {
        return new HttpConnection(urlStr, httpMethod);
    }

    /**
     * build http connection
     *
     * @param urlStr     URL
     * @param httpMethod request method
     * @param timeout    request timeout
     * @return {@link HttpsURLConnection}
     */
    public static HttpConnection create(String urlStr, HttpMethod httpMethod, int timeout) {
        return new HttpConnection(urlStr, httpMethod, timeout);
    }

    /**
     * build http connection
     *
     * @param urlStr           URL
     * @param httpMethod       request method
     * @param hostnameVerifier hostname verifier
     * @param sslSocketFactory ssl socket factory
     * @return {@link HttpsURLConnection}
     */
    public static HttpConnection create(String urlStr, HttpMethod httpMethod, HostnameVerifier hostnameVerifier, SSLSocketFactory sslSocketFactory) {
        return new HttpConnection(urlStr, httpMethod, hostnameVerifier, sslSocketFactory, 0, null);
    }

    /**
     * build http connection
     *
     * @param urlStr           URL
     * @param httpMethod       request method
     * @param hostnameVerifier hostname verifier
     * @param sslSocketFactory ssl socket factory
     * @param timeout          request timeout
     * @param proxy            http proxy
     * @return
     */
    public static HttpConnection create(String urlStr, HttpMethod httpMethod, HostnameVerifier hostnameVerifier, SSLSocketFactory sslSocketFactory, int timeout, Proxy proxy) {
        return new HttpConnection(urlStr, httpMethod, hostnameVerifier, sslSocketFactory, timeout, proxy);
    }


    /**
     * open http proxy connection
     *
     * @return {@link HttpURLConnection}
     * @throws IOException
     */
    private HttpURLConnection openHttp() throws IOException {
        return (HttpURLConnection) this.openConnection();
    }

    /**
     * open https connection
     *
     * @param hostnameVerifier hostname verifier
     * @param sslSocketFactory ssl socket factory
     * @return {@link HttpsURLConnection}
     */
    private HttpsURLConnection openHttps(HostnameVerifier hostnameVerifier, SSLSocketFactory sslSocketFactory) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        final HttpsURLConnection httpsURLConnection = (HttpsURLConnection) openConnection();
        httpsURLConnection.setHostnameVerifier(null != hostnameVerifier ? hostnameVerifier : new TrustAnyHostnameVerifier());
        httpsURLConnection.setSSLSocketFactory(null != sslSocketFactory ? sslSocketFactory : SSLSocketFactoryBuilder.create().build());
        return httpsURLConnection;
    }


    /**
     * build url connection
     *
     * @return {@link URLConnection}
     * @throws IOException
     */
    private URLConnection openConnection() throws IOException {
        return null == proxy ? url.openConnection() : url.openConnection(proxy);
    }

    /**
     * set connection timeout
     *
     * @param timeout concret timeout
     * @return {@link HttpConnection}
     */
    public HttpConnection setConnectionTimeout(int timeout) {
        if (timeout > 0 && null != connection) {
            connection.setConnectTimeout(timeout);
        }
        return this;
    }

    /**
     * set connection read timeout
     *
     * @param timeout concret timeout
     * @return {@link HttpConnection}
     */
    public HttpConnection setReadTimeout(int timeout) {
        if (timeout > 0 && null != connection) {
            connection.setReadTimeout(timeout);
        }
        return this;
    }

    /**
     * set connect and read timeout
     *
     * @param timeout concret timeout
     * @return {@link HttpConnection}
     */
    public HttpConnection setConnectionAndReadTimeout(int timeout) {
        return this.setConnectionTimeout(timeout).setReadTimeout(timeout);
    }

    /**
     * set cookie
     *
     * @param cookie cookie
     * @return {@link HttpConnection}
     */
    public HttpConnection setCookie(String cookie) {
        if (null != cookie) {
            header(Header.COOKIE, cookie, true);
        }
        return this;
    }

    /**
     * set http header
     *
     * @param header     header name
     * @param value      header value
     * @param isOverride is override
     * @return {@link HttpConnection}
     */
    public HttpConnection header(String header, String value, boolean isOverride) {
        if (null != connection) {
            if (isOverride) {
                connection.setRequestProperty(header, value);
            } else {
                connection.addRequestProperty(header, value);
            }
        }
        return this;
    }

    /**
     * set http header
     *
     * @param header     header obj
     * @param value      header value
     * @param isOverride is override
     * @return {@link HttpConnection}
     */
    public HttpConnection header(Header header, String value, boolean isOverride) {
        return this.header(header, value, isOverride);
    }

    /**
     * set http header
     *
     * @param headers    header map
     * @param isOverride is override
     * @return {@link HttpConnection}
     */
    public HttpConnection header(Map<String, List<String>> headers, boolean isOverride) {
        if (!CollectionUtils.isEmpty(headers)) {
            String name;
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                name = entry.getKey();
                for (String value : entry.getValue()) {
                    this.header(name, StrUtils.nullToEmpty(value), isOverride);
                }
            }
        }
        return this;
    }

    /**
     * get http header
     *
     * @param name header name
     * @return hader value
     */
    public String getHeader(String name) {
        return connection.getHeaderField(name);
    }

    /**
     * get http header
     *
     * @param header
     * @return header value
     */
    public String getHeader(Header header) {
        return getHeader(header.toString());
    }

    /**
     * get http headers
     *
     * @return headers map
     */
    public Map<String, List<String>> getHeaders() {
        return connection.getHeaderFields();
    }


    /**
     * init connection and set some properties
     *
     * @return {@link HttpConnection}
     */
    public HttpConnection initConnection() {
        try {
            connection.setRequestMethod(this.httpMethod.toString());
        } catch (ProtocolException e) {
            throw new HttpException(e.getMessage(), e);
        }
        // we can use connection.getOutputStream().write() when set doOutput true,the same to set doInput true
        connection.setDoInput(true);
        if (HttpMethod.POST.equals(this.httpMethod)
                || HttpMethod.PUT.equals(this.httpMethod)
                || HttpMethod.PATCH.equals(this.httpMethod)
                || HttpMethod.DELETE.equals(this.httpMethod)) {
            connection.setDoOutput(true);
            connection.setUseCaches(false);
        }

        // default header
        header(Header.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8", true);
        header(Header.ACCEPT_ENCODING, "gzip", true);
        header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded", true);
        header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 imaginary", true);
        // set cookie
        setCookie(CookiePool.get(url.getHost()));
        return this;
    }

    /**
     * use chunkedSteramingMode to upload file instead of HttpUrlConnection <br>
     * for HttpUrlConnection default upload method has some risk,its step are: <br>
     * <li>1.read file data and cache to local </li>
     * <li>2.send to server</li>
     * when upload large file may cause OutOfMemory
     *
     * @param blocksize
     * @return {@link HttpConnection}
     */
    public HttpConnection setChunkedStreamingMode(int blocksize) {
        connection.setChunkedStreamingMode(blocksize);
        return this;
    }

    /**
     * set auto 30x reforward
     *
     * @param isInstanceFollowRedirects is auto reforward
     * @return {@link HttpConnection}
     */
    public HttpConnection setInstanceFollowRedirects(boolean isInstanceFollowRedirects) {
        connection.setInstanceFollowRedirects(isInstanceFollowRedirects);
        return this;
    }

    /**
     * connect to server
     *
     * @return {@link HttpConnection}
     * @throws IOException a case of connection error
     */
    public HttpConnection connect() throws IOException {
        if (null != connection) {
            connection.connect();
        }
        return this;
    }

    /**
     * connect to server
     *
     * @return {@link HttpConnection}
     */
    public HttpConnection disconnect() {
        if (null != connection) {
            connection.disconnect();
        }
        return this;
    }

    /**
     * get inputstream from HttpConnection
     *
     * @return {@link InputStream}
     * @throws IOException a case of OPT IO error
     */
    public InputStream getInputStream() throws IOException {
        final String cookie = getHeader(Header.SET_COOKIE);
        if (!StrUtils.isBlank(cookie)) {
            logger.debug("set cookie {} ", cookie);
            CookiePool.set(url.getHost(), cookie);
        }
        return null != connection ? connection.getInputStream() : null;
    }

    /**
     * get error stream
     *
     * @return {@link InputStream}
     */
    public InputStream getErrorStream() {
        return null != connection ? connection.getErrorStream() : null;
    }

    /**
     * get output stream
     *
     * @return {@link OutputStream}
     * @throws IOException a case of OPT IO
     */
    public OutputStream getOutputSteam() throws IOException {
        return null != connection ? connection.getOutputStream() : null;
    }

    /**
     * get response code
     *
     * @return response code
     * @throws IOException a case of OPT conn
     */
    public int getResponseCode() throws IOException {
        return null != connection ? connection.getResponseCode() : 0;
    }

    public String getCharset() {
        return HttpUtils.getCharset(connection);
    }


    // getter/setter start
    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public HttpURLConnection getConnection() {
        return connection;
    }

    public void setConnection(HttpURLConnection connection) {
        this.connection = connection;
    }
    // getter/setter end
}
