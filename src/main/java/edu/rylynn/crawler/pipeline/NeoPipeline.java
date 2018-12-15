package edu.rylynn.crawler.pipeline;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;

public class NeoPipeline implements Pipeline {

    GraphDatabaseService graph;
    public NeoPipeline() {
        graph = new GraphDatabaseFactory().newEmbeddedDatabase(new File("/usr/local/Cellar/neo4j/3.5.0/libexec/data/databases"));
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

    }
}
