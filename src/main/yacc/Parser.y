%{
 import ru.mephi.bakinaa.lab3.lang.*;
 import ru.mephi.bakinaa.lab3.exceptions.*;
 import ru.mephi.bakinaa.lab3.lang.tree.*;
 import ru.mephi.bakinaa.lab3.lang.tree.ops.*;
 import ru.mephi.bakinaa.lab3.lang.tree.defs.*;
 import ru.mephi.bakinaa.lab3.lang.tree.terms.*;
 import ru.mephi.bakinaa.lab3.lang.enums.*;
%}

%token INT_NUM FLOAT_NUM STRING TRUE FALSE NULL ID
%token INDEX_TYPE TYPE_NAME MODIFIER CONSTRAINT
%token RELATIONSHIP ROW
%token PAR_OPEN PAR_CLOSE SQUARE_BR_OPEN SQUARE_BR_CLOSE CUR_BR_OPEN CUR_BR_CLOSE
%token COMA DOT SEMICOLON SCOPE_OPERATOR ARROW
%token EQUALS NOT_EQUALS GREATER LESS GREATER_EQ LESS_EQ
%token OR AND NOT
%token ASSIGN

%left COMA
%right ASSIGN
%left OR AND NOT
%nonassoc EQUALS NOT_EQUALS GREATER LESS GREATER_EQ LESS_EQ
%left SEMICOLON
%left DOT
%nonassoc SCOPE_OPERATOR

%%

statement: expr SEMICOLON { $$ = new YYParserVal(new Statements((TreeNode)$1.obj)); }
    | tableDef SEMICOLON { $$ = new YYParserVal(new Statements((TreeNode)$1.obj)); }
    | constraintDef SEMICOLON { $$ = new YYParserVal(new Statements((TreeNode)$1.obj)); }
    | rowDef SEMICOLON { $$ = new YYParserVal(new Statements((TreeNode)$1.obj)); }
    | statement statement { $$ = new YYParserVal(Statements.combine((TreeNode)$1.obj, (TreeNode)$2.obj)); }

statementsGroup: CUR_BR_OPEN statement CUR_BR_CLOSE { $$ = $2; }

expr: TRUE      { $$ = new YYParserVal(new Bool(true)); }
    | FALSE     { $$ = new YYParserVal(new Bool(false)); }
    | INT_NUM   { $$ = new YYParserVal(new Int($1.sval)); }
    | FLOAT_NUM { $$ = new YYParserVal(new FPNum($1.sval)); }
    | STRING    { $$ = new YYParserVal(Str.fromQuotedString($1.sval)); }
    | NULL      { $$ = new YYParserVal(new Null()); }
    | id        { $$ = $1; }

    | id ASSIGN expr       { $$ = new YYParserVal(new Assign((Id)$1.obj, (TreeNode)$3.obj)); }

    | expr LESS expr       { $$ = new YYParserVal(new Compare(ComparisonMode.LESS      , (TreeNode)$1.obj, (TreeNode)$3.obj)); }
    | expr GREATER expr    { $$ = new YYParserVal(new Compare(ComparisonMode.GREATER   , (TreeNode)$1.obj, (TreeNode)$3.obj)); }
    | expr LESS_EQ expr    { $$ = new YYParserVal(new Compare(ComparisonMode.LESS_EQ   , (TreeNode)$1.obj, (TreeNode)$3.obj)); }
    | expr GREATER_EQ expr { $$ = new YYParserVal(new Compare(ComparisonMode.GREATER_EQ, (TreeNode)$1.obj, (TreeNode)$3.obj)); }
    | expr EQUALS expr     { $$ = new YYParserVal(new Compare(ComparisonMode.EQUAL     , (TreeNode)$1.obj, (TreeNode)$3.obj)); }
    | expr NOT_EQUALS expr { $$ = new YYParserVal(new Compare(ComparisonMode.NOT_EQ    , (TreeNode)$1.obj, (TreeNode)$3.obj)); }

    | expr OR expr  { $$ = new YYParserVal(new Or((TreeNode)$1.obj, (TreeNode)$3.obj)); }
    | expr AND expr { $$ = new YYParserVal(new And((TreeNode)$1.obj, (TreeNode)$3.obj)); }
    | NOT expr      { $$ = new YYParserVal(new Not((TreeNode)$2.obj)); }

    | expr DOT id   { $$ = new YYParserVal(new Dot((TreeNode)$1.obj, (Id)$3.obj)); }

    | ROW statementsGroup                 { $$ = new YYParserVal(new RowDefinition((Statements)$2.obj)); }
    | PAR_OPEN expr PAR_CLOSE             { $$ = $2; }
    | id PAR_OPEN args PAR_CLOSE          { $$ = new YYParserVal(new FunCall((Id)$1.obj, (TreeNode)$3.obj)); }
    | expr DOT id PAR_OPEN args PAR_CLOSE { $$ = new YYParserVal(new FunCall((TreeNode)$1.obj, (Id)$3.obj, (TreeNode)$5.obj)); }

args: exprs { $$ = $1; }
exprs: expr             { $$ = $1; }
    | exprs COMA exprs  { $$ = new YYParserVal(ExprSet.combine((TreeNode)$1.obj, (TreeNode)$3.obj)); }

id: ID { $$ = new YYParserVal(new Id($1.sval)); }
    | ID SCOPE_OPERATOR ID { $$ = new YYParserVal(new Id($1.sval, $3.sval)); }

rowDef: TYPE_NAME id  { $$ = new YYParserVal(new ColDefinition($1.sval, (Id)$2.obj)); }
    | MODIFIER rowDef { ((ColDefinition)$2.obj).addModifier(Modifier.parse($1.sval)); $$ = $2; }

constraintDef: CONSTRAINT PAR_OPEN args PAR_CLOSE { $$ = new YYParserVal(ConstraintDefinition.parse($1.sval, (ExprSet)$3.obj)); }
    | id ARROW id { $$ = new YYParserVal(ConstraintDefinition.foreignKey((Id)$1.obj, (Id)$3.obj)); }

tableDef: INDEX_TYPE RELATIONSHIP id statementsGroup { $$ = new YYParserVal(new TableDefinition((Statements)$4.obj, (Id)$3.obj)); }
    | RELATIONSHIP id statementsGroup { $$ = new YYParserVal(new TableDefinition((Statements)$3.obj, (Id)$2.obj)); }

%%



QueryParser parser;

int yylex() {
    return parser.yylex();
}

void yyerror(String s) {
    throw new LangException(s);
}