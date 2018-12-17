package edu.rylynn.crawler;

import edu.rylynn.crawler.pipeline.JsonSourcePipeline;
import edu.rylynn.crawler.pipeline.NeoPipeline;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.RedisScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

public class SpiderMain {

    private static final String REDIS_HOST = "127.0.0.1";
    private static final String SOURCE_PREFIX = "/Users/rylynn/json/";
    public static void main(String[] args) {

        Spider.create(new BaiduPageProcessor())
                .addPipeline(new JsonSourcePipeline(SOURCE_PREFIX))
                .addPipeline(new NeoPipeline())
                .setScheduler(new RedisScheduler(REDIS_HOST))
                .addUrl("https://baike.baidu.com/item/委内瑞拉/5451")
                .thread(5)
                .run();
    }
}
