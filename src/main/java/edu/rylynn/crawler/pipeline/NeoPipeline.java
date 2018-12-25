package edu.rylynn.crawler.pipeline;

import edu.rylynn.crawler.Utility;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NeoPipeline implements Pipeline {
    private List<String> seedUrl = Utility.getSeedURL();
    private static final Logger LOGGER = Logger.getLogger(NeoPipeline.class);
    private GraphDatabaseService graph;

    public NeoPipeline() {
        graph = new GraphDatabaseFactory().newEmbeddedDatabase(new File("/usr/local/Cellar/neo4j/3.5.0/libexec/data/databases"));

    }

    /*
        如果后面没有信息块的值没有超链接，就直接putField，
        如果跟着超链接就将超链接的URL和值一并作为组合值CombinedValue传给Pipeline，并将链接关系作为两个实体之间的关系
        <dd class="basicInfo-item value">
        <a target="_blank" href="https://baike.baidu.com/item/%E8%A5%BF%E7%8F%AD%E7%89%99/148941" data-lemmaid="148941">西班牙</a>，
        <a target="_blank" href="https://baike.baidu.com/item/%E6%B3%95%E5%9B%BD">法国</a>，
        <a target="_blank" href="https://baike.baidu.com/item/%E9%98%BF%E6%A0%B9%E5%BB%B7/77652" data-lemmaid="77652">阿根廷</a>，
        <a target="_blank" href="https://baike.baidu.com/item/%E5%BE%B7%E5%9B%BD">德国</a>等
        </dd>
    */

    @Override
    public void process(ResultItems resultItems, Task task) {
        String propertyValue;
        String entityName;
        String entityUrl;

        String thisUrl = resultItems.get("url");
        String thisName = resultItems.get("entityName");


        /*
        第一个插入的点，需要在前面创建，后面插入的点都会在上一层创建
         */
        Node thisEntity;
        if(!seedUrl.contains(thisUrl)){
            thisEntity = graph.createNode(MyLabel.ENTITY);
            thisEntity.setProperty("url", thisUrl);
            thisEntity.setProperty("entityName", thisName);
        }
        else{
            thisEntity = graph.findNode(MyLabel.ENTITY, "url", thisUrl);
        }


        Map<String, String> basicInfo = resultItems.get("basicInfo");
        for (Map.Entry<String, String> entry : basicInfo.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            Document document = Jsoup.parse(v);
            Elements elements = document.getElementsByAttribute("href");
            if (elements.size() != 0) {
                //如果要并行，就要不停地重新new String对象，牺牲空间
                for (Element element : elements) {
                    entityName = element.text();
                    entityUrl = URLDecoder.decode(element.attr("href"));
                    Map<String, Object> map = new HashMap<>();
                    map.put("url", entityUrl);
                    map.put("entityName", entityName);
                    ResourceIterator<Node> nodes = graph.findNodes(MyLabel.ENTITY, map);
                    if (nodes.hasNext()) {
                        //TODO:如果已经入库了，直接建立连接，如果没入库，就先建立结点在建立连接
                        nodes.next().createRelationshipTo(thisEntity, MyRealationshipType.LINK);
                    } else {
                        //Node relateEntity
                    }
                }
            } else {
                propertyValue = document.text().trim();
                thisEntity.setProperty(k, propertyValue);
            }
        }
    }

    enum MyLabel implements Label {
        /**
         *
         */
        ENTITY
    }

    enum MyRealationshipType implements RelationshipType {
        /**
         *
         */
        LINK
    }
}
