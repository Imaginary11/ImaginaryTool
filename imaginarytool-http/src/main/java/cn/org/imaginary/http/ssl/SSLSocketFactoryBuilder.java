package cn.org.imaginary.http.ssl;


import cn.org.imaginary.util.ArraysUtils;
import cn.org.imaginary.util.StrUtils;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * a chained mode ssl socket factory builder
 *
 * @author Imaginary
 * @see
 * @since 1.0
 */
public class SSLSocketFactoryBuilder {

    /**
     * Supports some version of SSL; may support other versions
     */
    public static final String SSL = "SSL";
    /**
     * Supports SSL version 2 or later; may support other versions
     */
    public static final String SSLv2 = "SSLv2";
    /**
     * Supports SSL version 3; may support other versions
     */
    public static final String SSLv3 = "SSLv3";

    /**
     * Supports some version of TLS; may support other versions
     */
    public static final String TLS = "TLS";
    /**
     * Supports RFC 2246: TLS version 1.0 ; may support other versions
     */
    public static final String TLSv1 = "TLSv1";
    /**
     * Supports RFC 4346: TLS version 1.1 ; may support other versions
     */
    public static final String TLSv11 = "TLSv1.1";
    /**
     * Supports RFC 5246: TLS version 1.2 ; may support other versions
     */
    public static final String TLSv12 = "TLSv1.2";


    private String protocol = TLS;
    private KeyManager[] keyManagers;
    private TrustManager[] trustManagers = {new DefaultTrustManager()};
    private SecureRandom secureRandom = new SecureRandom();

    /**
     * create SSLSocketFactoryBuilder
     *
     * @return SSLSocketFactoryBuilder
     */
    public static SSLSocketFactoryBuilder create() {
        return new SSLSocketFactoryBuilder();
    }

    /**
     * set protocol
     *
     * @param protocol
     * @return SSLSocketFactoryBuilder
     */
    public SSLSocketFactoryBuilder setProtocol(String protocol) {
        if (!StrUtils.isNotBlank()) {
            this.protocol = protocol;
        }
        return this;
    }

    /**
     * set trustManagers
     *
     * @param trustManagers
     * @return SSLSocketFactoryBuilder
     */
    public SSLSocketFactoryBuilder setTrustManager(TrustManager... trustManagers) {
        if (ArraysUtils.isNotEmpty(trustManagers)) {
            this.trustManagers = trustManagers;
        }
        return this;
    }

    /**
     * set key managers
     *
     * @param keyManagers
     * @return SSLSocketFactoryBuilder
     */
    public SSLSocketFactoryBuilder setKeyManager(KeyManager... keyManagers) {
        if (ArraysUtils.isNotEmpty(keyManagers)) {
            this.keyManagers = keyManagers;
        }
        return this;
    }

    /**
     * set secureRandom
     *
     * @param secureRandom
     * @return SSLSocketFactoryBuilder
     */
    public SSLSocketFactoryBuilder setSecureRandom(SecureRandom secureRandom) {
        if (null != secureRandom) {
            this.secureRandom = secureRandom;
        }
        return this;
    }

    /**
     * build SslSocketFactory
     *
     * @return SSLSocketFactory
     * @throws NoSuchAlgorithmException a case of no such algorithm exception
     * @throws KeyManagementException   a case of keyManager exception
     */
    public SSLSocketFactory build() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance(this.protocol);
        sslContext.init(keyManagers, trustManagers, secureRandom);
        return sslContext.getSocketFactory();
    }


}
