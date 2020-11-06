######################################################################################
# (jdk installed env path to java and javac set)

# Build
./build

# Run
./run "argument"
#####################################################################################
# If it fails to recognize OSTYPE do this:

# FOR WINDOWS (MSYS, FOR EXAMPLE)
# Build
javac -classpath "jar/gluegen-rt.jar;jar/jogl-all.jar" src/ft_linear_regression.java
# Run
java -classpath "jar/gluegen-rt.jar;jar/jogl-all.jar;src" ft_linear_regression


# FOR MAC OS X
# Build
javac -classpath "jar/gluegen-rt.jar:jar/jogl-all.jar" src/ft_linear_regression.java
# Run
java -classpath "jar/gluegen-rt.jar:jar/jogl-all.jar:src" ft_linear_regression