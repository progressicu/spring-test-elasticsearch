package com.jupitertools.springtestelasticsearch;

import java.util.UUID;

import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 26/11/2019
 * <p>
 * TODO: replace on the JavaDoc
 *
 * @author Korovin Anatoliy
 */
@SpringBootTest
@ElasticsearchTestContainer
class ElasticTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    void name() {
        assertThat(elasticsearchTemplate).isNotNull();
    }

    @Test
    void initInner() {
        elasticsearchTemplate.deleteIndex(TestDocument.class);
        elasticsearchTemplate.createIndex(TestDocument.class);
        elasticsearchTemplate.putMapping(TestDocument.class);
        elasticsearchTemplate.refresh(TestDocument.class);

        TestDocument a = new TestDocument(UUID.randomUUID(), "AAAA");
        TestDocument b = new TestDocument(UUID.randomUUID(), "BB");

        // Act
        elasticsearchTemplate.index(new IndexQueryBuilder().withObject(a).build());
        elasticsearchTemplate.index(new IndexQueryBuilder().withObject(b).build());
        elasticsearchTemplate.refresh(TestDocument.class);

        // Assert
        TestDocument readBackA =
                elasticsearchTemplate.queryForObject(GetQuery.getById(a.getId().toString()), TestDocument.class);

        NativeSearchQuery q = new NativeSearchQuery(QueryBuilders.matchAllQuery());
        long count = elasticsearchTemplate.count(q, TestDocument.class);
        assertThat(count).isEqualTo(2);

        System.out.println(readBackA);
    }

    @Document(indexName = "test-document")
    private static class TestDocument {

        @Id
        private UUID id;
        private String name;

        //region boilerplate
        public TestDocument() {
        }

        public TestDocument(UUID id, String name) {
            this.id = id;
            this.name = name;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "TestDocument{" +
                   "id=" + id +
                   ", name='" + name + '\'' +
                   '}';
        }
        //endregion boilerplate
    }
}
