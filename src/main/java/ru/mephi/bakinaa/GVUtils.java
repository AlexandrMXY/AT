package ru.mephi.bakinaa;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import lombok.SneakyThrows;
import ru.mephi.bakinaa.regex.RegexDFA;
import ru.mephi.bakinaa.regex.tree.*;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static guru.nidi.graphviz.model.Factory.*;

public class GVUtils {
    @SneakyThrows
    public static void saveTree(TreeNode root, String file) {
        MutableGraph g = mutGraph("tree");
        treeIter(g, root);
        Graphviz gv = Graphviz.fromGraph(g);
        gv.render(Format.PNG).toFile(new File(file));
    }

    private static void treeIter(MutableGraph g, TreeNode node) {
        for (TreeNode child : node.getChildren()) {

            g.add(
                mutNode(String.valueOf(System.identityHashCode(node)))
                    .add(Label.of(nodeLabel(node)))
                    .addLink(mutNode(String.valueOf(System.identityHashCode(child)))
                                    .add(Label.of(nodeLabel(child)))));
            treeIter(g, child);
        }
    }

    private static String nodeLabel(TreeNode node) {
        return switch (node) {
            case Char n -> n.getFirstpos().toString();
            case Or n -> "|";
            case Star n -> "*";
            case Concat n -> "+";
            default -> "?";
        };
    }

    @SneakyThrows
    public static void saveDFA(RegexDFA dfa, String file) {
        MutableGraph g = mutGraph("dfa");

        dfa.getStates().forEach((sourceSet, sourceState) -> {
            if (sourceState.isFinal) {
                g.add(mutNode(sourceSet.toString()).add(Color.GREEN));
            }
            sourceState.transitions.forEach((charId, target) -> {
                   g.add(
                           mutNode(sourceSet.toString())
                                   .addLink(
                                           to(mutNode(findTargetId(dfa, target)))
                                                   .with(Label.of(String.valueOf(charId))))
                   );
            });
        });

        Graphviz.fromGraph(g).render(Format.PNG).toFile(new File(file));
    }

    private static String findTargetId(RegexDFA dfa, RegexDFA.State state) {
        Optional<Map.Entry<Set<Integer>, RegexDFA.State>> entry = dfa.getStates().entrySet().stream().filter((e) -> e.getValue() == state).findAny();
        return entry.orElseThrow().getKey().toString();
    }
}
