package ru.mephi.bakinaa.regex.parser;

import ru.mephi.bakinaa.regex.parser.Token;

%%
%public
%class Lexer
%type Token

%byaccj

%%

%(.) { return Token.character(yytext()); }
\( { return Token.groupOpen(); }
\) { return Token.groupClose(); }
\* { return Token.star(); }
\| { return Token.or(); }
\. { return Token.concat(); }

\[.*[^%]\] { return Token.charGroup(yytext()); }
.  { return Token.character(yytext()); }

<<EOF>> { return Token.end(); }





