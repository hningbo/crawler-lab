package edu.rylynn.crawler;

import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaiduPageProcessor implements PageProcessor {
    private static final String URL_REGEX = "https://baike.baidu.com/item/.*/\\d*";
    private static final Logger LOGGER = Logger.getLogger(BaiduPageProcessor.class);
    private Site site = Site.me().setRetryTimes(3).setSleepTime(10000).
            setUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36").
            addHeader("Connection", "keep-alive").
            addHeader("Accept", "*/*").
            addHeader("Cache-Control", "max-age=0");

    @Override
    public void process(Page page) {
        String url = URLDecoder.decode(page.getUrl().toString());
        String[] splitUrl = url.split("/");
        String entityName = page.getHtml().xpath("//dd[@class='lemmaWgt-lemmaTitle-title']").get();

        long pageId = Long.parseLong(splitUrl[splitUrl.length - 1]);
        page.putField("time", new Date());
        page.putField("htmlSource", page.getHtml().toString());
        page.putField("url", url);
        page.putField("pageId", pageId);
        page.putField("entityName", entityName);

        Selectable basicInfoXPath = page.getHtml().xpath("//div[@class='basic-info cmn-clearfix']");

        List<String> basicInfoName = basicInfoXPath
                .xpath("//dt[@class='basicInfo-item name']/text()")
                .all();

        List<String> basicInfoValue = basicInfoXPath
                .xpath("//dd[@class='basicInfo-item value']")
                .all();


        Map<String, String> basicInfo = new HashMap<>();
        for (int i = 0; i < basicInfoName.size(); i++) {
            basicInfo.put(basicInfoName.get(i), basicInfoValue.get(i));
        }

        page.putField("basicInfo", basicInfo);

        List<String> nextUrl = page.getHtml().links().regex(URL_REGEX).all()
                .parallelStream()
                .map(URLDecoder::decode)
                .collect(Collectors.toList());

        page.addTargetRequests(nextUrl);
    }

    @Override
    public Site getSite() {
        return this.site;
    }
}
