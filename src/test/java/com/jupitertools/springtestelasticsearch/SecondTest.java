package com.jupitertools.springtestelasticsearch;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 28/11/2019
 *
 * @author Korovin Anatoliy
 */
@Disabled("TODO: if this test run before standard ports mapping, the next test will fail, should fix it")
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
        assertThat(nodes).isNotNull();
        assertThat(name).isNotNull();
        System.out.println("nodes: " + nodes);
        System.out.println("name: " + name);
    }
}
