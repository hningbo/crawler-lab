package edu.rylynn.crawler;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;

public class BaiduPageProcessor implements PageProcessor {
    private static final String URL_REGEX = "";

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).
            setUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36").
            addHeader("Connection", "keep-alive").
            addHeader("Accept", "*/*").
            addHeader("Cache-Control", "max-age=0");

    @Override
    public void process(Page page) {
        page.getHtml().links().all().forEach(System.out::println);
        page.putField("","");
        //System.out.println(page.getRawText());
    }

    @Override
    public Site getSite() {
        return this.site;
    }
}
