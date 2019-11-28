package com.jupitertools.springtestelasticsearch;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created on 28/11/2019
 *
 * @author Korovin Anatoliy
 */
@ElasticsearchTestContainer(clusterNodesPropertyHolder = "es.nodes",
                            clusterNamePropertyHolder = "es.name")
@SpringBootTest
class SecondTest {

    @Value("${es.nodes}")
    private String nodes;

    @Value("${es.name}")
    private String name;

    @Test
    void name() {
        System.out.println("nodes: " + nodes);
        System.out.println("name: " + name);
    }
}
