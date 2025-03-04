cd yacc
yacc -Jclass=Parser -Jpackage=ru.mephi.bakinaa.regex.parser ..\src\main\yacc\Parser.y
xcopy Parser.java ..\src\main\java\ru\mephi\bakinaa\regex\parser\Parser.java
xcopy ParserVal.java ..\src\main\java\ru\mephi\bakinaa\regex\parser\ParserVal.java
cd ..