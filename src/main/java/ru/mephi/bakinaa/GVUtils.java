package ru.mephi.bakinaa;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import lombok.SneakyThrows;
import ru.mephi.bakinaa.regex.DFAState;
import ru.mephi.bakinaa.regex.tree.*;
import ru.mephi.bakinaa.regex.tree.raw.RawChar;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static guru.nidi.graphviz.model.Factory.*;

public class GVUtils {
    public static void saveTree(TreeNode root, String file) {
        saveTree(root, file, GVUtils::defaultNodeLabel);
    }

    @SneakyThrows
    public static void saveTree(TreeNode root, String file, Function<TreeNode, String> label) {
        MutableGraph g = mutGraph("tree");
        treeIter(g, root, label);
        Graphviz gv = Graphviz.fromGraph(g);
        gv.render(Format.PNG).toFile(new File(file));
    }

    private static void treeIter(MutableGraph g, TreeNode node, Function<TreeNode, String> label) {
        for (TreeNode child : node.getChildren()) {

            g.add(
                mutNode(String.valueOf(System.identityHashCode(node)))
                    .add(Label.of(label.apply(node)))
                    .addLink(mutNode(String.valueOf(System.identityHashCode(child)))
                                    .add(Label.of(label.apply(child)))));
            treeIter(g, child, label);
        }
    }

    public static String defaultNodeLabel(TreeNode node) {
        return switch (node) {
            case Char n -> n.getFirstpos().toString();
            case Or n -> "|";
            case Star n -> "*";
            case Concat n -> ".";
            case RawChar n -> "\"" + String.valueOf(n.c) + "\"";
            default -> "?";
        };
    }

    public static String datalizedNodeLabel(TreeNode node) {
        return node.getFirstpos().toString() + " "
                + defaultNodeLabel(node) + (node.nullable ? " T " : " F ") + node.getLastpos().toString();
    }

    @SneakyThrows
    public static void saveDFA(DFAState iState, Map<Set<Integer>, DFAState> stateMap, String file) {
        MutableGraph g = mutGraph("tree").setDirected(true);

        stateMap.forEach((k, fromState) -> {
            g.add(mutNode(String.valueOf(System.identityHashCode(fromState))).add(Label.of(k.toString())));

            fromState.transitions.forEach((charId, targetState) -> {
                g.add(mutNode(String.valueOf(System.identityHashCode(fromState))).addLink(
                        to(mutNode(String.valueOf(System.identityHashCode(targetState))))
                                .with(Label.of(String.valueOf(charId)))
                ));
            });

            if (fromState.isFinal)
                g.add(mutNode(String.valueOf(System.identityHashCode(fromState))).add(Shape.BOX));
        });

        g.add(mutNode(String.valueOf(System.identityHashCode(iState))).add(Color.GREEN));

        Graphviz.fromGraph(g).render(Format.PNG).toFile(new File(file));
    }
}
