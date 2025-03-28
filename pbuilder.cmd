mkdir target\parser-build
cd target\parser-build
mkdir ..\generated-sources\yacc\ru\mephi\bakinaa\lab3\lang
..\..\yacc.exe -Jclass=YYParser -Jpackage=ru.mephi.bakinaa.lab3.lang ..\..\src\main\yacc\Parser.y
copy *.java ..\generated-sources\yacc\ru\mephi\bakinaa\lab3\lang
cd ..\..