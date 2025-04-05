%{
 import ru.mephi.bakinaa.lab3.lang.*;
 import ru.mephi.bakinaa.lab3.exceptions.*;
 import ru.mephi.bakinaa.lab3.lang.defs.*;
 import ru.mephi.bakinaa.lab3.lang.enums.*;
 import ru.mephi.bakinaa.lab3.commons.objects.*;
 import ru.mephi.bakinaa.lab3.commons.*;
 import ru.mephi.bakinaa.lab3.utils.ParserUtils;
%}

%token INT_NUM FLOAT_NUM STRING TRUE FALSE NULL ID
%token INDEX_TYPE TYPE_NAME MODIFIER CONSTRAINT
%token RELATIONSHIP ROW
%token PAR_OPEN PAR_CLOSE SQUARE_BR_OPEN SQUARE_BR_CLOSE CUR_BR_OPEN CUR_BR_CLOSE
%token COMA DOT SEMICOLON SCOPE_OPERATOR ARROW
%token EQUALS NOT_EQUALS GREATER LESS GREATER_EQ LESS_EQ
%token OR AND NOT
%token ADD SUB MUL DIV
%token ASSIGN

%left COMA
%right ASSIGN
%left OR
%left AND
%left NOT
%left ADD SUB
%left MUL DIV
%left NEG
%nonassoc EQUALS NOT_EQUALS GREATER LESS GREATER_EQ LESS_EQ
%left SEMICOLON
%left DOT
%nonassoc SCOPE_OPERATOR

%%

statements: expr SEMICOLON    { $$ = util.statement($1); }
    | tableDef SEMICOLON      { $$ = util.statement($1); }
    | statements statements   { $$ = util.statement($1, $2); }

definitionsGroup: CUR_BR_OPEN definition CUR_BR_CLOSE { $$ = $2; }
definition: id ASSIGN expr SEMICOLON { $$ = new YYParserVal(new Definitions(new Assign((Id)$1.obj, (Expression)$3.obj))); }
    | constraintDef SEMICOLON        { $$ = new YYParserVal(new Definitions((Definition) $1.obj)); }
    | colDef SEMICOLON               { $$ = new YYParserVal(new Definitions((Definition) $1.obj)); }
    | definition definition          { $$ = new YYParserVal(((Definitions)$1.obj).add((Definitions)$2.obj)); }

expr: TRUE      { $$ = new YYParserVal(Bool.TRUE); }
    | FALSE     { $$ = new YYParserVal(Bool.FALSE); }
    | INT_NUM   { $$ = new YYParserVal(Int.parse($1.sval)); }
    | FLOAT_NUM { $$ = new YYParserVal(Real.parse($1.sval)); }
    | STRING    { $$ = new YYParserVal(Str.fromQuotedString($1.sval)); }
    | NULL      { $$ = new YYParserVal((Object)null); }
    | id        { $$ = $1; }

    | expr ADD expr      { $$ = util.fun(Functions.ADD, $1, $3); }
    | expr SUB expr      { $$ = util.fun(Functions.SUB, $1, $3); }
    | expr MUL expr      { $$ = util.fun(Functions.MUL, $1, $3); }
    | expr DIV expr      { $$ = util.fun(Functions.DIV, $1, $3); }
    | SUB expr %prec NEG { $$ = util.fun(Functions.NEG, $1, $3); }

    | expr LESS expr       { $$ = util.fun(Functions.LESS, $1, $3); }
    | expr GREATER expr    { $$ = util.fun(Functions.GREATER, $1, $3); }
    | expr LESS_EQ expr    { $$ = util.fun(Functions.LESS_EQ, $1, $3); }
    | expr GREATER_EQ expr { $$ = util.fun(Functions.GREATER_EQ, $1, $3); }
    | expr EQUALS expr     { $$ = util.fun(Functions.EQ, $1, $3); }
    | expr NOT_EQUALS expr { $$ = util.fun(Functions.NOT_EQ, $1, $3); }

    | expr OR expr  { $$ = util.fun(Functions.AND, $1, $3); }
    | expr AND expr { $$ = util.fun(Functions.OR, $1, $3); }
    | NOT expr      { $$ = util.fun(Functions.NOT, $2); }

    | ROW definitionsGroup                { $$ = new YYParserVal(new RowDefinition((Definitions)$2.obj)); }
    | PAR_OPEN expr PAR_CLOSE             { $$ = $2; }
    | id PAR_OPEN args PAR_CLOSE          { $$ = util.fun($1, (FunArgs)$3.obj); }
    | expr DOT id PAR_OPEN args PAR_CLOSE { $$ = util.fun($3, $1, (FunArgs)$5.obj); }

args: exprs { $$ = $1; }
exprs: expr             { $$ = new YYParserVal(new FunArgs((Expression)$1.obj)); }
    | exprs COMA exprs  { $$ = new YYParserVal(((FunArgs)$1.obj).addAll((FunArgs)$3.obj)); }

id: ID { $$ = new YYParserVal(new Id($1.sval)); }
    | ID SCOPE_OPERATOR ID { $$ = new YYParserVal(new Id($1.sval, $3.sval)); }

colDef: TYPE_NAME id  { $$ = new YYParserVal(new ColDefinition($1.sval, (Id)$2.obj)); }
    | MODIFIER colDef { ((ColDefinition)$2.obj).addModifier(Modifier.parse($1.sval)); $$ = $2; }

constraintDef: CONSTRAINT PAR_OPEN args PAR_CLOSE { $$ = new YYParserVal(ConstraintDefinition.parse($1.sval, (FunArgs)$3.obj)); }
    | id ARROW id { $$ = new YYParserVal(ConstraintDefinition.foreignKey((Id)$1.obj, (Id)$3.obj)); }

tableDef: INDEX_TYPE RELATIONSHIP id definitionsGroup { $$ = new YYParserVal(new TableDefinition((Definitions)$4.obj, (Id)$3.obj)); }
    | RELATIONSHIP id definitionsGroup { $$ = new YYParserVal(new TableDefinition((Definitions)$3.obj, (Id)$2.obj)); }

%%


ParserUtils util;
QueryParser parser;

int yylex() {
    return parser.yylex();
}

void yyerror(String s) {
    throw new LangException(s);
}