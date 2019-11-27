package com.jupitertools.springtestelasticsearch;

import java.util.UUID;

import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.BeforeEach;
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
 *
 * @author Korovin Anatoliy
 */
@SpringBootTest
@ElasticsearchTestContainer
class ElasticsearchTestContainerTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    private static final UUID FIRST_ID = UUID.randomUUID();
    private static final UUID SECOND_ID = UUID.randomUUID();
    private static final String FIRST_DATA = "FIRST";
    private static final String SECOND_DATA = "SECOND";

    @BeforeEach
    void setUp() {
        // prepare the Index of TestDocument
        elasticsearchTemplate.deleteIndex(TestDocument.class);
        elasticsearchTemplate.createIndex(TestDocument.class);
        elasticsearchTemplate.putMapping(TestDocument.class);
        elasticsearchTemplate.refresh(TestDocument.class);

        // populate dataset
        TestDocument first = new TestDocument(FIRST_ID, FIRST_DATA);
        TestDocument second = new TestDocument(SECOND_ID, SECOND_DATA);
        elasticsearchTemplate.index(new IndexQueryBuilder().withObject(first).build());
        elasticsearchTemplate.index(new IndexQueryBuilder().withObject(second).build());

        // flush
        elasticsearchTemplate.refresh(TestDocument.class);
    }

    @Test
    void findById() {
        // Arrange
        GetQuery getQuery = GetQuery.getById(FIRST_ID.toString());
        // Act
        TestDocument first = elasticsearchTemplate.queryForObject(getQuery, TestDocument.class);
        // Assert
        assertThat(first).isNotNull()
                         .extracting(TestDocument::getName)
                         .isEqualTo(FIRST_DATA);
    }

    @Test
    void countQuery() {
        // Arrange
        NativeSearchQuery countQuery = new NativeSearchQuery(QueryBuilders.matchAllQuery());
        // Act
        long count = elasticsearchTemplate.count(countQuery, TestDocument.class);
        // Assert
        assertThat(count).isEqualTo(2);
    }

    /**
     * nested Elasticsearch document just for testing
     */
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
