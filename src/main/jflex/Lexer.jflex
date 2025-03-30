package ru.mephi.bakinaa.lab3.lang;

import ru.mephi.bakinaa.lab3.exceptions.LangException;
import static ru.mephi.bakinaa.lab3.lang.enums.TokenType.*;

%%

%public
%class Lexer
%type Token

NUM0 = [0-9]([0-9]|([0-9]_[0-9]))*
INT_NUM = -?{NUM0}
FLOAT_NUM = {INT_NUM}\.{NUM0}
STR = [\"]([^\"\\]|\\\\|\\\")*[\"]

IDENTIFIER = [:letter:][[:letter:][:digit:]_$]*

INDEX = hashtable|tree|ordered
MODIFIER = primary|unique|notnull
CONSTRAINT = Unique|Primary|Predicate
TYPE = String|Boolean|Integer|Real

%%

true { return TRUE.instance(); }
false { return FALSE.instance(); }
null { return NULL.instance(); }
relationship { return RELATIONSHIP.instance(); }
row { return ROW.instance(); }
{TYPE} { return TYPE_NAME.instance(yytext()); }
{CONSTRAINT} { return CONSTRAINT.instance(yytext()); }
{MODIFIER} { return MODIFIER.instance(yytext()); }
{INDEX}  { return INDEX_TYPE.instance(yytext()); }

\( { return PAR_OPEN.instance(); }
\) { return PAR_CLOSE.instance(); }
\[ { return SQUARE_BR_OPEN.instance(); }
\] { return SQUARE_BR_CLOSE.instance(); }
\{ { return CUR_BR_OPEN.instance(); }
\} { return CUR_BR_CLOSE.instance(); }

\, { return COMA.instance(); }
\. { return DOT.instance(); }
; { return SEMICOLON.instance(); }
:: { return SCOPE_OPERATOR.instance(); }
-\> { return ARROW.instance(); }

== { return EQUALS.instance(); }
\!= { return NOT_EQUALS.instance(); }
\<= { return LESS_EQ.instance(); }
\>= { return GREATER_EQ.instance(); }
\< { return LESS.instance(); }
\> { return GREATER.instance(); }

= { return ASSIGN.instance(); }
\|\| { return OR.instance(); }
&& { return AND.instance(); }
\! { return NOT.instance(); }

{INT_NUM} { return INT_NUM.instance(yytext()); }
{FLOAT_NUM} { return FLOAT_NUM.instance(yytext()); }
{STR} { return STRING.instance(yytext()); }
{IDENTIFIER} { return IDENTIFIER.instance(yytext()); }

[\s] {}
[^] { throw new LangException("Illegal character <" + yytext() + ">"); }
