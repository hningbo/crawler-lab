package edu.rylynn.crawler;

import edu.rylynn.crawler.pipeline.NeoPipeline;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

public class SpiderMain {
    public static void main(String[] args) {
        Spider.create(new BaiduPageProcessor())
                .addPipeline(new JsonFilePipeline("/Users/rylynn"))
                .addPipeline(new NeoPipeline())
                .setScheduler(new QueueScheduler().setDuplicateRemover(new HashSetDuplicateRemover()))
                .addUrl("https://baike.baidu.com/item/委内瑞拉/5451")
                .thread(5)
                .run();
    }
}
