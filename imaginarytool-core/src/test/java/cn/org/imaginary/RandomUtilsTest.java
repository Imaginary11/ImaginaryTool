package cn.org.imaginary;

import cn.org.imaginary.util.RandomUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Imaginary
 * @see
 * @since 1.0
 */
public class RandomUtilsTest {
    private final Logger logger = LoggerFactory.getLogger(RandomUtilsTest.class);
    @Test
    public void testRandomInt(){
        for(int i =0; i < 10 ;i++){
            logger.info("random number between 5~8 is {}", RandomUtils.randomInt(5,8));
        }
    }
}
