package com.skdziwak.kavamaker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skdziwak.kavamaker.antlr.JavaLexer;
import com.skdziwak.kavamaker.antlr.JavaParser;
import com.skdziwak.kavamaker.codetree.Node;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java -jar kavamaker.jar [ASSEMBLE|DISASSEMBLE]");
            System.exit(1);
        }
        if (args[0].equalsIgnoreCase("assemble")) {
            String json = Files.readString(Path.of("main.json"));
            ObjectMapper objectMapper = new ObjectMapper();
            Node node = objectMapper.readValue(json, Node.class);
            System.out.print(node);
        } else if (args[0].equalsIgnoreCase("disassemble")) {
            String code = new String(IOUtils.toByteArray(System.in), StandardCharsets.UTF_8);
            JavaLexer lexer = new JavaLexer(CharStreams.fromString(code));
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokenStream);
            JavaParser.CompilationUnitContext compilationUnitContext = parser.compilationUnit();
            CodeTreeVisitor codeTreeVisitor = new CodeTreeVisitor(tokenStream);

            Node node = codeTreeVisitor.visit(compilationUnitContext);
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.print(objectMapper.writeValueAsString(node));
        } else {
            System.out.println("Usage: java -jar kavamaker.jar [ASSEMBLE|DISASSEMBLE]");
            System.exit(1);
        }
    }
}
