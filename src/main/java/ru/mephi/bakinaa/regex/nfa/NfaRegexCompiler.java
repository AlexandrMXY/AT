package ru.mephi.bakinaa.regex.nfa;

import ru.mephi.bakinaa.GVUtils;
import ru.mephi.bakinaa.regex.RegEx;
import ru.mephi.bakinaa.regex.dfa.DfaRegEx;
import ru.mephi.bakinaa.regex.RegExCompiler;
import ru.mephi.bakinaa.regex.RegExException;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;
import ru.mephi.bakinaa.regex.tree.*;

import static ru.mephi.bakinaa.regex.nfa.NFA.StatesPair;

public class NfaRegexCompiler implements RegExCompiler {
    private final TreeNode root;
    private final SymbolsTable symbolsTable;

    private final NFA nfa = new NFA();

    public NfaRegexCompiler(TreeNode root, SymbolsTable symbolsTable) {
        this.root = root;
        this.symbolsTable = symbolsTable;
    }

    @Override
    public NfaRegEx compile() {
        System.out.println("Compiling NFA");

        StatesPair sp = treeIteration(root);
        nfa.setInitialStateId(sp.initialState());
        nfa.setFinalStateId(sp.finalState());

        GVUtils.saveNFA(nfa, "2.png");

        return new NfaRegEx(nfa, symbolsTable);
    }

    private StatesPair treeIteration(TreeNode root) {
        if (root == null)
            return null;

        StatesPair left = treeIteration(root.getLeft());
        StatesPair right = treeIteration(root.getRight());
        StatesPair result;

        switch (root) {
            case Or or -> {
                result = new StatesPair(nfa.addNode(), nfa.addNode());
                nfa.addEpsiolnTransition(result.initialState(), left.initialState());
                nfa.addEpsiolnTransition(result.initialState(), right.initialState());

                nfa.addEpsiolnTransition(left.finalState(), result.finalState());
                nfa.addEpsiolnTransition(right.finalState(), result.finalState());
            }
            case Concat concat -> {
                result = new StatesPair(left.initialState(), right.finalState());
                nfa.addEpsiolnTransition(left.finalState(), right.initialState());
            }
            case Star star -> {
                result = left;
                nfa.addEpsiolnTransition(result.finalState(), result.initialState());
            }
            case Char c -> {
                result = new StatesPair(nfa.addNode(), nfa.addNode());
                nfa.addTransition(result.initialState(), result.finalState(), symbolsTable.getCharIdOfTreeIndex(c.pos));
            }
            case Capture c -> {
                result = new StatesPair(nfa.addNode(), nfa.addNode());
                nfa.addTransition(
                        result.initialState(),
                        new NFATransition.CaptureBeginTransition(left.initialState(), c.id));
                nfa.addTransition(
                        left.finalState(),
                        new NFATransition.CaptureEndTransition(result.finalState(), c.id));
            }
            case Backreference b -> {
                result = new StatesPair(nfa.addNode(), nfa.addNode());
                nfa.addTransition(
                        result.initialState(),
                        new NFATransition.BackreferenceTransition(result.finalState(), b.captureId));
            }

            default ->
                throw new RegExException();

        }

        return result;
    }
}
