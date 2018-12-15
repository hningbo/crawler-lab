package edu.rylynn.crawler;

import edu.rylynn.crawler.pipeline.NeoPipeline;
import us.codecraft.webmagic.Spider;

public class SpiderMain {
    public static void main(String[] args) {
        Spider.create(new BaiduPageProcessor())
                .addPipeline(new NeoPipeline())
                .addUrl("https://baike.sogou.com/v95115.htm")
                .thread(5)
                .run();
    }
}
