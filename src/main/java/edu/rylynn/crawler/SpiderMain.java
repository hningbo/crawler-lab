package edu.rylynn.crawler;

import us.codecraft.webmagic.Spider;

public class SpiderMain {
    public static void main(String[] args) {
        Spider.create(new BaiduPageProcessor())
                .addUrl("")
                .thread(5)
                .run();
    }
}
