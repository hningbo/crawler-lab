package edu.rylynn.crawler;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        Jedis jedis = new Jedis();
        System.out.println(jedis.get("queue_baike.baidu.com"));
    }
}
