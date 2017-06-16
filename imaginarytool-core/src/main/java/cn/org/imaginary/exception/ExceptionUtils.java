package cn.org.imaginary.exception;

import cn.org.imaginary.util.StrUtils;

/**
 * @author Imaginary
 * @see
 * @since 1.0
 */
public final class ExceptionUtils {
    /**
     * 获得完整消息，包括异常名
     *
     * @param e 异常
     * @return 完整消息
     */
    public static String getMessage(Throwable e) {
        return StrUtils.format("{}: {}", e.getClass().getSimpleName(), e.getMessage());
    }
}
