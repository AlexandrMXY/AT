package ru.mephi.bakinaa.lab1.validation.flex;

import ru.mephi.bakinaa.lab1.validation.flex.FlexNfsToken;
import ru.mephi.bakinaa.lab1.validation.flex.FlexValidationException;

%%
%public
%class LexNfsLexer
%type FlexNfsToken


%%

nfs:\/ { return FlexNfsToken.header(); }
\/([a-zA-Z]+) { return FlexNfsToken.dir(yytext()); }

[^] { throw new FlexValidationException("Illegal character <" + yytext() + ">"); }
