package cn.org.imaginary.http.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * https verification manager
 *
 * @author Imaginary
 * @see
 * @since 1.0
 */
public class TrustAnyHostnameVerifier implements HostnameVerifier {
    /**
     * trust any verification
     *
     * @param s
     * @param sslSession
     * @return
     */
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
}
