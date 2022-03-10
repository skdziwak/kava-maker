package com.skdziwak.kavamaker.codetree;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Node {
    @Getter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<Node> nodes = new LinkedList<>();

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String text;

    public Node() {
    }

    public Node(String text, String type) {
        this.text = text;
        this.type = type;
    }

    public void add(Node node) {
        nodes.add(node);
    }

    @Override
    public String toString() {
        if (nodes.isEmpty()) {
            return text;
        } else {
            return nodes.stream().map(Object::toString).collect(Collectors.joining());
        }
    }
}
