package cn.org.imaginary.convert;

/**
 * @author Imaginary
 * @see
 * @since 1.0
 */
public final class Convert {
    private Convert() {
    }

    /**
     * 转换为字符串<br>
     * 如果给定的值为null，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static String toStr(Object value, String defaultValue) {
        return convert(String.class, value, defaultValue);
    }

    /**
     * 转换值为指定类型
     *
     * @param <T> 目标类型
     * @param type 类型
     * @param value 值
     * @param defaultValue 默认值
     * @return 转换后的值
     * @throws ConvertException 转换器不存在
     */
    public static <T> T convert(Class<T> type, Object value, T defaultValue) throws ConvertException {
        return ConverterRegistry.getInstance().convert(type, value, defaultValue);
    }

}
