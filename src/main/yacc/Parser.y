%{
import ru.mephi.bakinaa.regex.tree.*;
import ru.mephi.bakinaa.regex.tree.raw.*;
import ru.mephi.bakinaa.regex.parser.*;
import ru.mephi.bakinaa.regex.chars.*;
%}

%token CHAR CHAR_GROUP
%token OPEN_BR CLOSE_BR STAR OR CONCAT

//%left OR
//%left CONCAT
//%right STAR

%%

exp: CHAR { $$ = new ParserVal(new RawChar($1.sval)); }
 | CHAR_GROUP { $$ = new ParserVal(new CharGroupNode($1.sval)); }
 | exp OR exp { $$ = new ParserVal(new Or((TreeNode)$1.obj, (TreeNode)$3.obj)); }
 | exp CONCAT exp { $$ = new ParserVal(new Concat((TreeNode)$1.obj, (TreeNode)$3.obj)); }
// | exp exp { $$ = new ParserVal(new Concat((TreeNode)$1.obj, (TreeNode)$2.obj)); }
 | exp STAR { $$ = new ParserVal(new Star((TreeNode)$1.obj)); }
 | OPEN_BR exp CLOSE_BR { $$ = $2; }


%%
public Lexer lexer;
public SymbolsTable sTable;

void yyerror(String s) {
    throw new ParsingException(s);
}

int yylex() {
    try {
        int tokId = 0;
        Token tok = lexer.yylex();

        switch (tok.type()) {
            case Token.Type.GROPU_OPEN: {
                tokId = OPEN_BR;
            } break;
            case Token.Type.GROUP_CLOSE: {
                tokId = CLOSE_BR;
            } break;
            case Token.Type.STAR: {
                tokId = STAR;
            } break;
            case Token.Type.OR: {
                tokId = OR;
            } break;
            case Token.Type.CONCAT: {
                tokId = CONCAT;
            } break;
            case Token.Type.END: {
                tokId = 0;
            } break;
            case Token.Type.CHAR: {
                tokId = CHAR;
                sTable.registerGroup(new CharGroup(tok.data().charAt(0)));
                yylval = new ParserVal(tok.data());
            } break;
            case Token.Type.CHAR_GROUP: {
                tokId = CHAR_GROUP;
                // TODO
                // sTable.registerGroup(...);
                yylval = new ParserVal(tok.data());
            } break;
        }
        return tokId;
    } catch (Exception e) {
        yyerror("Error");
    }
    return 0;
}


public ParserVal yyval() {
    return yyval;
}
