package ru.mephi.bakinaa;

import ru.mephi.bakinaa.regex.RegEx;
import ru.mephi.bakinaa.regex.nfa.NfaRegExMatcher;

public class Main {
    public static void main(String[] args) {

//        RegEx regex = RegEx.compile("#((a|b)*)f*#((a|b)*)#0", true);
//        var matcher = regex.matcher("abaabafbaabbbabaaba");//
        // abc*
//        RegEx regex = RegEx.compile("((10)*0|1(01)*1)($|(0(01)*(1|00)|1(10)*(0|11)))*", false);
//        var matcher = regex.matcher("");
//        System.out.println(matcher.matches());
//
//        String restored = regex.restore();
//
//        RegEx regex2 = RegEx.compile(restored);
//        String restored2 = regex2.restore();
//        System.out.println(restored);
//        System.out.println(restored2);
//        System.out.println(restored.equals(restored2));

//        RegEx inv = RegEx.compile("abbc").inversion();
//        System.out.println(inv.matcher("cbba").matches());
//        System.out.println(inv.restore());

        System.out.println(RegEx.compile("abc/a|b)").matcher("abca").matches());

//        System.out.println(((NfaRegExMatcher)matcher).getCaptures());
        // a*b
    }
}
