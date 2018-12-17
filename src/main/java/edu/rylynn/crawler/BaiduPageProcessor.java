package edu.rylynn.crawler;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import edu.rylynn.crawler.entity.PersistentPage;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

public class BaiduPageProcessor implements PageProcessor {
    private static final String URL_REGEX = "https://baike.baidu.com/item/.*/\\d*";
    private static final Logger logger = Logger.getLogger(BaiduPageProcessor.class);
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).
            setUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36").
            addHeader("Connection", "keep-alive").
            addHeader("Accept", "*/*").
            addHeader("Cache-Control", "max-age=0");

    @Override
    public void process(Page page) {
        String url = URLDecoder.decode(page.getUrl().toString());
        String[] splitUrl = url.split("/");
        long pageId = Long.parseLong(splitUrl[splitUrl.length - 1]);
        page.putField("time", new Date());
        page.putField("htmlSource", page.getHtml().toString());
        page.putField("url", url);
        page.putField("pageId", pageId);

        List<String> nextUrl = page.getHtml().links().regex(URL_REGEX).all();
        page.addTargetRequests(nextUrl);
    }

    @Override
    public Site getSite() {
        return this.site;
    }


}
