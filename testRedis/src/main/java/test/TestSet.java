package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.awt.windows.WPrinterJob;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestSet {


    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void setValue() {
        //存入值
        redisTemplate.boundSetOps("nameset").add("曹操");
        redisTemplate.boundSetOps("nameset").add("刘备");
        redisTemplate.boundSetOps("nameset").add("孙权");

    }

    @Test
    public void getValue() {
        Set nameset = redisTemplate.boundSetOps("nameset").members();
        System.out.println(nameset  );
    }


    @Test
    public void deleValue() {
        redisTemplate.boundSetOps("nameset").remove("孙权");
    }
    @Test
    public void deleAllValue() {
        redisTemplate.delete("nameset");
    }


    @Test
    public void testClassReflc() {
        Class<User> userClass = User.class;
        Field[] declaredFields = userClass.getDeclaredFields();
        ArrayList<String> fieldsSql = new ArrayList<>();
        for (Field declaredField : declaredFields) {

            String[] split = declaredField.getName().split("(?<!^)(?=[A-Z])");
            for (int i = 0; i < split.length; i++) {
                split[i] = split[i].toLowerCase();
            }
            String join = String.join("_", split);
            System.out.println(join);
        }
    }
}
