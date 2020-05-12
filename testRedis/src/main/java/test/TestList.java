package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestList {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testValue1() {
        redisTemplate.boundListOps("namelist").rightPush("刘备");
        redisTemplate.boundListOps("namelist").rightPush("关羽");
        redisTemplate.boundListOps("namelist").rightPush("张飞");

    }
    @Test
    public void testValue2() {
        redisTemplate.boundListOps("namelist").leftPush("刘备");
        redisTemplate.boundListOps("namelist").leftPush("关羽");
        redisTemplate.boundListOps("namelist").leftPush("张飞");

    }
    @Test
    public void testValueget() {
        List namelist = redisTemplate.boundListOps("namelist").range(0, 10);
        System.out.println(namelist);

    }
    @Test
    public void testValueIndex() {
        Object namelist = redisTemplate.boundListOps("namelist").index(1);
        System.out.println(namelist.toString());

    }
    @Test
    public void testValueRemove() {
        redisTemplate.boundListOps("namelist").remove(4,"刘备");

    }
}
