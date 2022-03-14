package com.skdziwak.kavamaker;

import com.skdziwak.kavamaker.codetree.Node;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CodeTreeVisitor {
    private final CommonTokenStream tokenStream;
    private final Set<Token> consumedTokens = new HashSet<>();

    public CodeTreeVisitor(CommonTokenStream tokenStream) {
        this.tokenStream = tokenStream;
    }

    public Node visit(ParseTree tree) {
        if (tree instanceof TerminalNode) {
            return new Node(tree.getText(), processName(tree.getClass().getSimpleName()));
        } else {
            Node treeNode = new Node();
            treeNode.setType(processName(tree.getClass().getSimpleName()));
            int count = tree.getChildCount();
            for (int i = 0 ; i < count ; i++) {
                ParseTree child = tree.getChild(i);
                if (i == 0) {
                    List<Token> hiddenTokensToLeft = tokenStream.getHiddenTokensToLeft(child.getSourceInterval().a);
                    if (hiddenTokensToLeft != null) {
                        for (Token token : hiddenTokensToLeft) {
                            token = consume(token);
                            if (token != null) {
                                treeNode.add(new Node(token.getText(), "Hidden"));
                            }
                        }
                    }
                }
                treeNode.add(this.visit(child));
                List<Token> hiddenTokensToRight = tokenStream.getHiddenTokensToRight(child.getSourceInterval().b);
                if (hiddenTokensToRight != null) {
                    for (Token token : hiddenTokensToRight) {
                        token = consume(token);
                        if (token != null) {
                            treeNode.add(new Node(token.getText(), "Hidden"));
                        }
                    }
                }
            }

            return treeNode;
        }
    }

    private Token consume(Token token) {
        if (consumedTokens.contains(token)) {
            return null;
        }
        consumedTokens.add(token);
        return token;
    }

    private String processName(String name) {
        if (name.endsWith("Context")) {
            return name.substring(0, name.length() - 7);
        }
        if (name.endsWith("Impl")) {
            return name.substring(0, name.length() - 4);
        }
        return name;
    }
}
