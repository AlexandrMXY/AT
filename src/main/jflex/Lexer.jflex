package ru.mephi.bakinaa.lab3.lang;

//import ru.mephi.bakinaa.lab3.validation.flex.FlexNfsToken;
//import ru.mephi.bakinaa.lab3.validation.flex.FlexValidationException;

%%

%public
%class Lexer
%type String

%%

nfs:\/ { return "a"; }
\/([a-zA-Z]+) { return "b"; }

[^] { throw new RuntimeException("Illegal character <" + yytext() + ">"); }