package edu.rylynn.crawler.pipeline;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import edu.rylynn.crawler.entity.PersistentPage;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author rylynn
 * @version 2018-12-17
 * @classname JsonSourcePipeline
 * @discription
 */

public class JsonSourcePipeline implements Pipeline {
    private static final Logger logger = Logger.getLogger(JsonSourcePipeline.class);

    private String filePrefix;
    private String fileSuffix;

    public JsonSourcePipeline(String filePrefix) {
        this.filePrefix = filePrefix;
        this.fileSuffix = ".json";
    }

    public void setFilePrefix(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        PersistentPage persistentPage = new PersistentPage();
        persistentPage.setCrawledTime(resultItems.get("time"));
        persistentPage.setHtml(resultItems.get("htmlSource"));
        persistentPage.setPageId(resultItems.get("pageId"));
        persistentPage.setUrl(resultItems.get("url"));

        try {
            String filename = filePrefix.concat(Long.toString(resultItems.get("pageId"))).concat(fileSuffix);
            JsonWriter writer = new JsonWriter(new FileWriter(filename));
            writer.setIndent("  ");
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            gson.toJson(persistentPage, PersistentPage.class, writer);
            writer.close();
            logger.info(persistentPage.getUrl() + " is saved into the path " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
