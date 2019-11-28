package com.jupitertools.springtestelasticsearch.customizer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.jupitertools.springtestelasticsearch.ElasticsearchTestContainer;
import com.jupitertools.springtestelasticsearch.ElasticsearchTestContainers;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

/**
 * Created on 26/11/2019
 * <p>
 * This ContextCustomizerFactory will start the docker image of the Elasticsearch
 * if in tests used the @{@link ElasticsearchTestContainer} annotation.
 *
 * @author Korovin Anatoliy
 */
public class ElasticsearchContextCustomizerFactory implements ContextCustomizerFactory {

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass,
                                                     List<ContextConfigurationAttributes> list) {

        Set<ElasticsearchTestContainer> annotations =
                AnnotationUtils.getRepeatableAnnotations(testClass,
                                                         ElasticsearchTestContainer.class,
                                                         ElasticsearchTestContainers.class);

        Set<ContainerDescription> descriptions =
                annotations.stream()
                           .map(annotation -> new ContainerDescription(annotation.clusterNodesPropertyHolder(),
                                                                       annotation.clusterNamePropertyHolder()))
                           .collect(Collectors.toSet());

        return new ElasticsearchContextCustomizer(descriptions);
    }
}
