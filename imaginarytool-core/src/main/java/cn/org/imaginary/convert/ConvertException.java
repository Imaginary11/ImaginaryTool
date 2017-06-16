package cn.org.imaginary.convert;

import cn.org.imaginary.exception.ExceptionUtils;
import cn.org.imaginary.util.StrUtils;

/**
 * @author Imaginary
 * @see
 * @since 1.0
 */
public class ConvertException extends RuntimeException{
    private static final long serialVersionUID = 4730597402855274362L;

    public ConvertException(Throwable e) {
        super(ExceptionUtils.getMessage(e), e);
    }

    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(String messageTemplate, Object... params) {
        super(StrUtils.format(messageTemplate, params));
    }

    public ConvertException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ConvertException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtils.format(messageTemplate, params), throwable);
    }
}
