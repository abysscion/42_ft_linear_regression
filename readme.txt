######################################################################################
# (jdk installed env path to java and javac set)

# Build
./build

# Run (check usage on command type)
# to train
./train
# to predict
./predict

#####################################################################################
# If it fails to recognize OSTYPE do this:

# FOR WINDOWS (MSYS, FOR EXAMPLE)
# Build
javac -classpath "jar/gluegen-rt.jar;jar/jogl-all.jar" src/ft_linear_regression.java
javac src/train.java
javac src/predict.java

# FOR MAC OS X
# Build
javac -classpath "jar/gluegen-rt.jar:jar/jogl-all.jar" src/ft_linear_regression.java
javac src/train.java
javac src/predict.java
