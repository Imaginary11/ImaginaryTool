package cn.org.imaginary.convert;


import cn.org.imaginary.util.ArrayUtils;

import java.util.Map;

/**
 * @author Imaginary
 * @see
 * @since 1.0
 */
public class ConverterRegistry {
    /** 默认类型转换器 */
    private Map<Class<?>, Converter<?>> defaultConverterMap;
    /** 用户自定义类型转换器 */
    private Map<Class<?>, Converter<?>> customConverterMap;

    /** 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载 */
    private static class SingletonHolder {
        /** 静态初始化器，由JVM来保证线程安全 */
        private static ConverterRegistry instance = new ConverterRegistry();
    }
    /**
     * 获得单例的 {@link ConverterRegistry}
     * @return {@link ConverterRegistry}
     */
    public static ConverterRegistry getInstance(){
        return SingletonHolder.instance;
    }

    public ConverterRegistry() {
        defaultConverter();
    }

    private void defaultConverter() {
    }
    /**
     * 转换值为指定类型<br>
     * 自定义转换器优先
     *
     * @param <T> 转换的目标类型（转换器转换到的类型）
     * @param type 类型
     * @param value 值
     * @param defaultValue 默认值
     * @return 转换后的值
     * @throws ConvertException 转换器不存在
     */
    public <T> T convert(Class<T> type, Object value, T defaultValue) throws ConvertException {
        return convert(type, value, defaultValue, true);
    }

    /**
     * 转换值为指定类型
     *
     * @param <T> 转换的目标类型（转换器转换到的类型）
     * @param type 类型
     * @param value 值
     * @param defaultValue 默认值
     * @param isCustomFirst 是否自定义转换器优先
     * @return 转换后的值
     * @throws ConvertException 转换器不存在
     */
    @SuppressWarnings("unchecked")
    public <T> T convert(Class<T> type, Object value, T defaultValue, boolean isCustomFirst) throws ConvertException{
        if(null == type && null == defaultValue){
            throw new NullPointerException("[type] and [defaultValue] are both null, we can not know what type to convert !");
        }
        if(null == value ){
            return defaultValue;
        }
        if(null == type){
            type = (Class<T>) defaultValue.getClass();
        }
        //默认强转
        if(type.isInstance(value)){
            return (T)value;
        }

        //数组强转
        final Class<?> valueClass = value.getClass();
        if(type.isArray() && valueClass.isArray()){
            try {
                return (T) ArrayUtils.cast(type, value);
            } catch (Exception e) {
                //强转失败进行下一步
            }
        }

        Converter<T> converter = getConverter(type, isCustomFirst);
        if (null == converter) {
            throw new ConvertException("No Converter for type [{}]", type.getName());
        }
        return converter.convert(value, defaultValue);
    }

    /**
     * 获得转换器<br>
     * @param <T> 转换的目标类型
     *
     * @param type 类型
     * @param isCustomFirst 是否自定义转换器优先
     * @return 转换器
     */
    public <T> Converter<T> getConverter(Class<T> type, boolean isCustomFirst) {
        Converter<T> converter = null;
        if(isCustomFirst){
            converter = this.getCustomConverter(type);
            if(null == converter){
                converter = this.getDefaultConverter(type);
            }
        }else{
            converter = this.getDefaultConverter(type);
            if(null == converter){
                converter = this.getCustomConverter(type);
            }
        }
        return converter;
    }

    /**
     * 获得默认转换器
     *
     * @param <T> 转换的目标类型（转换器转换到的类型）
     * @param type 类型
     * @return 转换器
     */
    @SuppressWarnings("unchecked")
    public <T> Converter<T> getDefaultConverter(Class<T> type) {
        return (null == defaultConverterMap) ? null : (Converter<T>)defaultConverterMap.get(type);
    }

    /**
     * 获得自定义转换器
     * @param <T> 转换的目标类型（转换器转换到的类型）
     *
     * @param type 类型
     * @return 转换器
     */
    @SuppressWarnings("unchecked")
    public <T> Converter<T> getCustomConverter(Class<T> type) {
        return (null == customConverterMap) ? null : (Converter<T>)customConverterMap.get(type);
    }

}
