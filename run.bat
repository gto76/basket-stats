SET mainClass=si.gto76.basketstats.BasketStats
SET appName=basket-stats

SET scriptName=%~n0

echo %scriptName%: COMPILING...
mkdir target\main
dir /s /B *.java > sources.txt
javac -d target\main -cp lib\hamcrest-core-1.3.jar:lib\junit-4.11.jar @sources.txt
del sources.txt
cd target
mkdir lib
cd lib

echo %scriptName%: EXTRACTING LIBRARIES...
jar xf ..\..\lib\hamcrest-core-1.3.jar
jar xf ..\..\lib\junit-4.11.jar
cd ..

echo %scriptName%: PACKAGING IN JAR...
jar cvfe %appName%.jar %mainClass% -C .. -C ..\src\main\resources . -C main . -C lib .

echo %scriptName%: EXECUTING JAR...
java -jar %appName%.jar
