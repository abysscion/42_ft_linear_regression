#!/bin/bash
if [ "$OSTYPE" == "msys" -o "$OSTYPE" == "cygwin" ] ; then
	echo -e "\e[37m[Building...]\e[2m"
	javac -classpath "jar/gluegen-rt.jar;jar/jogl-all.jar" src/*.java
	echo -e "\e[1;92m[Ok]\e[0m"
elif [[ "$OSTYPE" == "darwin"* ]]; then
	echo -e "\033[37m[Building...]\033[2m"
	javac -classpath "jar/gluegen-rt.jar:jar/jogl-all.jar" src/*.java
	echo -e "\033[1;92m[Ok]\033[0m"
else
	echo "Can't recognize OSTYPE. It wouldn't work on platforms other than Windows and Mac OS..."
fi