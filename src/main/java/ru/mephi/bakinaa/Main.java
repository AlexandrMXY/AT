package ru.mephi.bakinaa;

import ru.mephi.bakinaa.regex.RegEx;
import ru.mephi.bakinaa.regex.nfa.NfaRegExMatcher;

public class Main {
    public static void main(String[] args) {

//        RegEx regex = RegEx.compile("#((a|b)*)f*#((a|b)*)#0", true);
//        var matcher = regex.matcher("abaabafbaabbbabaaba");//
        RegEx regex = RegEx.compile("#(a+a+)#0", true);
        var matcher = regex.matcher("aaaaaaaa");
        System.out.println(matcher.matches());
        System.out.println(((NfaRegExMatcher)matcher).getCaptures());
    }
}
