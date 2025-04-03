package ru.mephi.bakinaa.lab3.utils;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import lombok.SneakyThrows;
import ru.mephi.bakinaa.lab3.commons.Expression;
import ru.mephi.bakinaa.lab3.commons.Expressions;
import ru.mephi.bakinaa.lab3.commons.objects.*;

import java.io.File;
import java.util.Objects;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.*;

public class GVUtils {
    @SneakyThrows
    public static void save(Expressions exprs, String file) {
        MutableGraph g = mutGraph("g").setDirected(true);

        MutableNode root = mutNode("ROOT");
        g.add(root);
        for (Expression expr : exprs.getExpressions())
            root.addLink(to(iter(expr, g)));

        Graphviz.fromGraph(g).render(Format.PNG).toFile(new File(file));
    }

    private static MutableNode iter(Expression arg, MutableGraph g) {
        MutableNode node = mutNode(String.valueOf(arg instanceof Bool ? new Object() : System.identityHashCode(arg)))
                .add(Label.of(arg.toString()));
        g.add(node);
        if (arg instanceof ru.mephi.bakinaa.lab3.commons.FunCall<?> call) {
            for (var a : call.getArgs())
                node.addLink(to(iter(a, g)));
        }

        return node;
    }
}
