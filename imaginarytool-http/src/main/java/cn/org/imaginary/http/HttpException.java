package cn.org.imaginary.http;

import cn.org.imaginary.util.StrUtils;

/**
 * http request exception
 *
 * @author Imaginary
 * @see
 * @since 1.0
 */
public class HttpException extends RuntimeException {
    public HttpException(Throwable e) {
        super(e.getMessage(), e);
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String messageTemplate, Object... params) {
        super(StrUtils.format(messageTemplate, params));
    }

    public HttpException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public HttpException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtils.format(messageTemplate, params), throwable);
    }
}
