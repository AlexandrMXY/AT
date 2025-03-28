%{
 import ru.mephi.bakinaa.lab3.lang.*;
 import ru.mephi.bakinaa.lab3.exceptions.*;
 import ru.mephi.bakinaa.lab3.lang.tree.*;
 import ru.mephi.bakinaa.lab3.lang.tree.ops.*;
 import ru.mephi.bakinaa.lab3.lang.tree.terms.*;
%}
%token INT_NUM FLOAT_NUM STRING TRUE FALSE NULL ID
%token PAR_OPEN PAR_CLOSE SQUARE_BR_OPEN SQUARE_BR_CLOSE CUR_BR_OPEN CUR_BR_CLOSE
%token COMA DOT SEMICOLON SCOPE_OPERATOR
%token EQUALS NOT_EQUALS GREATER LESS GREATER_EQ LESS_EQ
%token OR AND NOT
%token ASSIGN

%left COMA
%left OR AND NOT
%nonassoc EQUALS NOT_EQUALS GREATER LESS GREATER_EQ LESS_EQ

%%

expr: TRUE      { $$ = new YYParserVal(new Bool(true)); }
    | FALSE     { $$ = new YYParserVal(new Bool(false)); }
    | INT_NUM   { $$ = new YYParserVal(new Int($1.sval)); }
    | FLOAT_NUM { $$ = new YYParserVal(new FPNum($1.sval)); }
    | STRING    { $$ = new YYParserVal(Str.fromQuotedString($1.sval)); }
    | NULL      { $$ = new YYParserVal(new Null()); }
    | ID        { $$ = new YYParserVal(new Id($1.sval)); }

    | expr LESS expr       { $$ = new YYParserVal(new Compare(ComparisonMode.LESS      , (TreeNode)$1.obj, (TreeNode)$3.obj)); }
    | expr GREATER expr    { $$ = new YYParserVal(new Compare(ComparisonMode.GREATER   , (TreeNode)$1.obj, (TreeNode)$3.obj)); }
    | expr LESS_EQ expr    { $$ = new YYParserVal(new Compare(ComparisonMode.LESS_EQ   , (TreeNode)$1.obj, (TreeNode)$3.obj)); }
    | expr GREATER_EQ expr { $$ = new YYParserVal(new Compare(ComparisonMode.GREATER_EQ, (TreeNode)$1.obj, (TreeNode)$3.obj)); }
    | expr EQUALS expr     { $$ = new YYParserVal(new Compare(ComparisonMode.EQUAL     , (TreeNode)$1.obj, (TreeNode)$3.obj)); }
    | expr NOT_EQUALS expr { $$ = new YYParserVal(new Compare(ComparisonMode.NOT_EQ    , (TreeNode)$1.obj, (TreeNode)$3.obj)); }

    | expr OR expr  { $$ = new YYParserVal(new Or((TreeNode)$1.obj, (TreeNode)$3.obj)); }
    | expr AND expr { $$ = new YYParserVal(new And((TreeNode)$1.obj, (TreeNode)$3.obj)); }
    | NOT expr      { $$ = new YYParserVal(new Not((TreeNode)$2.obj)); }


    | PAR_OPEN expr PAR_CLOSE    { $$ = $2; }
    | ID PAR_OPEN args PAR_CLOSE { $$ = new YYParserVal(new FunCall($1.sval, (TreeNode)$3.obj)); }

args: exprs { $$ = $1; }
exprs:
    | expr              { $$ = $1; }
    | exprs COMA exprs  { $$ = new YYParserVal(ExprSet.combine((TreeNode)$1.obj, (TreeNode)$3.obj)); }

%%



QueryParser parser;

int yylex() {
    return parser.yylex();
}

void yyerror(String s) {
    throw new LangException(s);
}