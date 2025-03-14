package ru.mephi.bakinaa.regex.dfa;

import lombok.RequiredArgsConstructor;
import ru.mephi.bakinaa.IOUtils;
import ru.mephi.bakinaa.regex.RegEx;
import ru.mephi.bakinaa.regex.chars.CharGroup;
import ru.mephi.bakinaa.regex.chars.SymbolsTable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class DfaRegEx implements RegEx {
    private final SymbolsTable symbolsTable;
    private final DFA dfa;

    public DfaRegExMatcher matcher(String string) {
        return new DfaRegExMatcher(dfa, symbolsTable, string);
    }


    public String restore() {
        int arrSize = dfa.getStates() * dfa.getStates();
        StringBuilder[] cur  = new  StringBuilder[arrSize];
        StringBuilder[] prev = new  StringBuilder[arrSize];
        StringBuilder[] temp;

        for (int i = 0; i < arrSize; i++) {
            cur[i] = new StringBuilder();
            prev[i] = new StringBuilder();
        }

        for (int i = 0; i < dfa.getStates(); i++) {
            for (int j = 0; j < dfa.getStates(); j++) {
                var sb = cur[getArrayIndex(i, j)];
                int finalJ = j;
                dfa.getTransitions().get(i).forEach((charId, toId) -> {
                    if (toId == finalJ) {
                        pushRE(sb, symbolsTable.charAsString(charId));
                    }
                });
                if (i == j && sb.isEmpty())
                    pushRE(sb, "$");
                if (!sb.isEmpty())
                    sb.insert(0, '(').append(')');
            }
        }

        for (int k = 0; k < dfa.getStates(); k++) {
            temp = cur;
            cur = prev;
            prev = temp;

            var kk = prev[getArrayIndex(k, k)];

            if (!kk.isEmpty())
                kk.append("*");

            for (int i = 0; i < dfa.getStates(); i++) {
                for (int j = 0; j < dfa.getStates(); j++) {
                    var sb = cur[getArrayIndex(i, j)];
                    sb.setLength(0);
                    var ij = prev[getArrayIndex(i, j)];
                    var ik = prev[getArrayIndex(i, k)];
                    var kj = prev[getArrayIndex(k, j)];

                    if (ik.isEmpty() || kj.isEmpty()) {
                        sb.append(ij);
                        if (!sb.isEmpty())
                            sb.insert(0, '(').append(')');
                        continue;
                    }


                    if (i != k)
                        sb.append(ik);
                    sb.append(kk);
                    if (k != j)
                        sb.append(kj);

                    if (!ij.isEmpty() && !sb.isEmpty())
                        sb.append('|');
                    sb.append(ij);

                    if (!sb.isEmpty())
                        sb.insert(0, '(').append(')');
                }
            }
        }

        StringBuilder res = new StringBuilder();
        for (int fs : dfa.getFinals()) {
            if (!res.isEmpty())
                res.append('|');
            res.append(cur[getArrayIndex(dfa.getInitialState(), fs)]);
        }

        return res.toString();
    }

    private void pushRE(StringBuilder sb, String re) {
        if (!re.isEmpty()) {
            if (!sb.isEmpty())
                sb.append('|');
            sb.append(re);
        }
    }

    private int getArrayIndex(int i, int j) {
        return i * dfa.getStates() + j;
    }

    @Override
    public RegEx subtract(RegEx otherRegEx) {
        if (!(otherRegEx instanceof DfaRegEx other))
            throw new UnsupportedOperationException();

        SymbolsTable resSt = symbolsTable.multiply(other.symbolsTable);

        DFA result = new DFA(
                dfaProdId(dfa.getInitialState(), other.dfa.getInitialState()),
                dfaProdId(dfa.getStubId(), other.dfa.getStubId()),
                dfa.getStates() * other.dfa.getStates());

        List<Boolean> reachable = new ArrayList<>();
        for (int i = 0; i < result.getStates(); i++)
            reachable.add(false);
        reachable.set(result.getInitialState(), true);

        reachable.set(result.getStubId(), true);

        for (int resCharId = 0; resCharId <= resSt.lastGroupId(); resCharId++) {
            int charId1 = symbolsTable.ifOf(resSt.getGroup(resCharId));
            int charId2 = other.symbolsTable.ifOf(resSt.getGroup(resCharId));

            for (int fromId1 = 0; fromId1 < dfa.getStates(); fromId1++) {
                for (int fromId2 = 0; fromId2 < other.dfa.getStates(); fromId2++) {
                    if (dfa.isFinal(fromId1) && !other.dfa.isFinal(fromId2))
                        result.setFinal(dfaProdId(fromId1, fromId2), true);

                    int toId1 = dfa.move(fromId1, charId1);
                    if (toId1 == -1) {
                        result.addTransition(dfaProdId(fromId1, fromId2), result.getStubId(), resCharId);
                        continue;
                    }

                    int toId2 = other.dfa.move(fromId2, charId2);
                    if (toId2 == -1)
                        toId2 = other.dfa.getStubId();

                    int toResId = dfaProdId(toId1, toId2);
                    int fromResId = dfaProdId(fromId1, fromId2);
                    result.addTransition(fromResId, toResId, resCharId);
                    if (reachable.get(fromResId))
                        updateReachable(reachable, result, toResId);
                }
            }
        }

        IOUtils.println("\nSub sym table");
        if (IOUtils.isPrint())
            for (CharGroup g : resSt.getGroups())
                IOUtils.printf("%s ", g.toString());


        IOUtils.saveDFA(result, "sub0.png");
        result = new DFAMinimizer(result, resSt, reachable).minimize();
        IOUtils.saveDFA(result, "sub1.png");
        return new DfaRegEx(resSt, result);
    }

    private void updateReachable(List<Boolean> reachable, DFA dfa, int stateId) {
        reachable.set(stateId, true);
        dfa.getTransitions().get(stateId).forEach((charId, toId) -> {
            if (!reachable.get(toId))
                updateReachable(reachable, dfa, toId);
        });
    }

    private int dfaProdId(int first, int second) {
        return first + second * dfa.getStates();
    }
}
