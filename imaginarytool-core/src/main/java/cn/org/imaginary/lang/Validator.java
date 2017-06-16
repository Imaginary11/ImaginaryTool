package cn.org.imaginary.lang;

import java.net.MalformedURLException;
import java.util.regex.Pattern;

/**
 * @author Imaginary
 * @see
 * @since 1.0
 */
public final class Validator {
    private Validator() {
    }

    /** 英文字母 、数字和下划线 */
    public final static Pattern GENERAL = Pattern.compile("^\\w+$");
    /** 数字 */
    public final static Pattern NUMBERS = Pattern.compile("\\d+");
    /** 分组 */
    public final static Pattern GROUP_VAR = Pattern.compile("\\$(\\d+)");
    /** IP v4 */
    public final static Pattern IPV4 = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
    /** 货币 */
    public final static Pattern MONEY = Pattern.compile("^(\\d+(?:\\.\\d+)?)$");
    /** 邮件 */
    public final static Pattern EMAIL = Pattern.compile("(\\w|.)+@\\w+(\\.\\w+){1,2}");
    /** 移动电话 */
    public final static Pattern MOBILE = Pattern.compile("1\\d{10}");
    /** 身份证号码 */
    public final static Pattern CITIZEN_ID = Pattern.compile("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)");
    /** 邮编 */
    public final static Pattern ZIP_CODE = Pattern.compile("\\d{6}");
    /** 生日 */
    public final static Pattern BIRTHDAY = Pattern.compile("^(\\d{2,4})([/\\-\\.年]?)(\\d{1,2})([/\\-\\.月]?)(\\d{1,2})日?$");
    /** URL */
    public final static Pattern URL = Pattern.compile("(https://|http://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");
    /** 中文字、英文字母、数字和下划线 */
    public final static Pattern GENERAL_WITH_CHINESE = Pattern.compile("^[\\u0391-\\uFFE5\\w]+$");
    /** UUID */
    public final static Pattern UUID = Pattern.compile("^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$");
    /** 不带横线的UUID */
    public final static Pattern UUID_SIMPLE = Pattern.compile("^[0-9a-z]{32}$");
    /** 中国车牌号码 */
    public final static Pattern PLATE_NUMBER = Pattern.compile("^[京,津,渝,沪,冀,晋,辽,吉,黑,苏,浙,皖,闽,赣,鲁,豫,鄂,湘,粤,琼,川,贵,云,陕,秦,甘,陇,青,台,蒙,桂,宁,新,藏,澳,军,海,航,警][A-Z][0-9,A-Z]{5}$");

    /**
     * 验证是否为URL
     *
     * @param value 值
     * @return 是否为URL
     */
    public static boolean isUrl(String value) {
        try {
            new java.net.URL(value);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

}
