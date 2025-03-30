package ru.mephi.bakinaa.lab3.utils;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import lombok.SneakyThrows;
import ru.mephi.bakinaa.lab3.lang.tree.Statements;
import ru.mephi.bakinaa.lab3.lang.tree.Dot;
import ru.mephi.bakinaa.lab3.lang.tree.ExprSet;
import ru.mephi.bakinaa.lab3.lang.tree.FunCall;
import ru.mephi.bakinaa.lab3.lang.tree.TreeNode;
import ru.mephi.bakinaa.lab3.lang.tree.defs.*;
import ru.mephi.bakinaa.lab3.lang.tree.ops.*;
import ru.mephi.bakinaa.lab3.lang.tree.terms.*;

import java.io.File;
import java.util.Objects;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.*;

public class GVUtils {
    @SneakyThrows
    public static void save(TreeNode root, String file) {
        MutableGraph g = mutGraph("g").setDirected(true);

        treeIter(g, root);

        Graphviz.fromGraph(g).render(Format.PNG).toFile(new File(file));
    }

    private static MutableNode treeIter(MutableGraph g, TreeNode node) {
        if (node == null)
            return null;
        MutableNode result = mutNode(String.valueOf(System.identityHashCode(node))).add(Label.of(getNodeLabel(node)));
        g.add(result);

        switch (node) {
            case Assign n -> addLinks(g, result, n.getLeft(), n.getRight());
            case And n -> addLinks(g, result, n.getLeft(), n.getRight());
            case Or n -> addLinks(g, result, n.getLeft(), n.getRight());
            case Not n -> addLinks(g, result, n.getArg());
            case ExprSet n -> addLinks(g, result, n.getExprs().toArray(TreeNode[]::new));
            case Dot n -> addLinks(g, result, n.getBase(), n.getPath());
            case Compare n -> addLinks(g, result, n.getLeft(), n.getRight());
            case Statements n -> addLinks(g, result, n.getStatements().toArray(TreeNode[]::new));
            case FunCall n -> addLinks(g, result, n.getCaller(), n.getArgs());

            case ConstraintDefinition n -> addLinks(g, result, n.getArgs());

            case TableDefinition n -> addLinks(g, result, n.getAllRowsAndConstraints().toArray(TreeNode[]::new));

            default -> {}
        }

        return result;
    }

    private static void addLinks(MutableGraph g, MutableNode base, TreeNode... targets) {
        for (TreeNode target : targets)
            if (target != null)
                base.addLink(treeIter(g, target));
    }

    private static String getNodeLabel(TreeNode node) {
        return switch (node) {
            case And n -> "&&";
            case Or n -> "||";
            case Not n -> "!";
            case Assign n -> "=";
            case Compare n -> switch (n.getMode()) {
                case EQUAL -> "==";
                case NOT_EQ -> "!=";
                case GREATER -> ">";
                case LESS -> "<";
                case GREATER_EQ -> ">=";
                case LESS_EQ -> "<=";
            };
            case ExprSet n -> "ARGS";
            case FunCall n -> n.getId().toString() + "()";
            case Dot n -> ".";

            case Bool n -> String.valueOf(n.value);
            case FPNum n -> String.valueOf(n.value);
            case Int n -> String.valueOf(n.value);
            case Str n -> "\"" + n.value + "\"";
            case Null n -> "null";
            case Id n -> n.toString();

            case Statements n -> "STATEMENTS";
            case RowDefinition n -> "ROW " + n.getAssigns().toString();

            case ColDefinition n -> "COL " + n.getModifiers().stream().map(Objects::toString).collect(Collectors.joining(" ")) + " " + n.getType().toString() + " " + n.getName();
            case TableDefinition n -> "TABLE " + n.getIndexType() + " " + n.getId();

            case ConstraintDefinition n -> n.getConstraint().toString();

            default -> "?";
        };
    }
}
