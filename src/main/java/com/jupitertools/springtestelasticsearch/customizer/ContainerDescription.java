package com.jupitertools.springtestelasticsearch.customizer;

import java.util.Objects;

/**
 * Created on 28/11/2019
 * <p>
 * TODO: replace on the JavaDoc
 *
 * @author Korovin Anatoliy
 */
public class ContainerDescription {

    private final String clusterNodes;
    private final String clusterName;

    public ContainerDescription(String clusterNodes, String clusterName) {
        this.clusterNodes = clusterNodes;
        this.clusterName = clusterName;
    }

    public String getClusterNodes() {
        return clusterNodes;
    }

    public String getClusterName() {
        return clusterName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ContainerDescription that = (ContainerDescription) o;
        return Objects.equals(clusterNodes, that.clusterNodes) &&
               Objects.equals(clusterName, that.clusterName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clusterNodes, clusterName);
    }
}
