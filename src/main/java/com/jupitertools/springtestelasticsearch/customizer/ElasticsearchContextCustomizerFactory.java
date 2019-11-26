package com.jupitertools.springtestelasticsearch.customizer;

import java.util.List;

import com.jupitertools.springtestelasticsearch.ElasticsearchTestContainer;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

/**
 * Created on 26/11/2019
 * <p>
 * TODO: replace on the JavaDoc
 *
 * @author Korovin Anatoliy
 */
public class ElasticsearchContextCustomizerFactory implements ContextCustomizerFactory {

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass,
                                                     List<ContextConfigurationAttributes> list) {

        ElasticsearchTestContainer annotation =
                AnnotationUtils.getAnnotation(testClass, ElasticsearchTestContainer.class);

        if (annotation != null) {
            return new ElasticsearchContextCustomizer();
        } else {
            // nothing to customize
            return null;
        }
    }
}
