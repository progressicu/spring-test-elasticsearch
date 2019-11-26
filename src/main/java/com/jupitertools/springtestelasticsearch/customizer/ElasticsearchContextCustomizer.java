package com.jupitertools.springtestelasticsearch.customizer;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * Created on 26/11/2019
 * <p>
 * TODO: replace on the JavaDoc
 *
 * @author Korovin Anatoliy
 */
public class ElasticsearchContextCustomizer implements ContextCustomizer {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchContextCustomizer.class);
    private static final String DOCKER_IMAGE_NAME = "docker.elastic.co/elasticsearch/elasticsearch:6.4.1";

    @Override
    public void customizeContext(ConfigurableApplicationContext configurableApplicationContext,
                                 MergedContextConfiguration mergedContextConfiguration) {

        WaitStrategy waitStrategy =
                new HttpWaitStrategy().forPort(9200)
                                      .forStatusCodeMatching(response -> response == HTTP_OK ||
                                                                         response == HTTP_UNAUTHORIZED)
                                      .withStartupTimeout(Duration.ofMinutes(2));

        GenericContainer container =
                new FixedHostPortGenericContainer<>(DOCKER_IMAGE_NAME)
                        .withExposedPorts(9200, 9300)
                        .withEnv("cluster.name", "test_cluster")
                        .withEnv("discovery.type", "single-node")
                        .waitingFor(waitStrategy);

        log.debug("Starting Elasticsearch TestContainer");
        container.start();
        log.debug("Started Elasticsearch TestContainer at:[{}]", getHostPort(container));

        TestPropertyValues testPropertyValues =
                TestPropertyValues.of(
                        "spring.data.elasticsearch.cluster-nodes=" + getHostPort(container),
                        "spring.data.elasticsearch.cluster-name=" + "test_cluster");

        testPropertyValues.applyTo(configurableApplicationContext);
    }

    private String getHostPort(GenericContainer container) {
        return String.format("%s:%s",
                             container.getContainerIpAddress(),
                             container.getMappedPort(9300));
    }
}
