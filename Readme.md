# Linear regression
Ecole 42 project.

## Description
This project is about creating a program that predicts the price of a car by using a linear function train with a gradient descent algorithm. While main program implemented in Java, graphic part is implemented with OpenGL.

![Graphic](/images/screen.jpg)
> Graphic, that represents linear regression function as blue line, original dataset as green circles and predicted values as orange circles.

## Requirements
- [JDK 15.0](https://openjdk.java.net/projects/jdk/15/)

## Setup

1. Download or `git clone` sources.
2. Run `./build.sh` to compile java classes.

## Usage

#### Train
Firstly we need to **train** model via "train" program, after training file **model.csv** whould be created saving *theta0* and *theta1* weights.
`./train [options] <path/to/dataset.csv>`

**options:**

| Flags | Definitions |
| :------------ | :------------ |
| -vo | visualize original dataset |
| -vs | visualize standardized dataset |
| -vp | visualize predicted over original dataset |
| -err | print mean squared error after each regression iteration |
| -lr:&lt;number> | set learning rate, where number is rate in range (0.1, 1.0) |

#### Predict
Now we can predict values using program "predict". Running this program will ask for mileage prompt, after what predict whould be made, saved and printed (via `./train -vp data.csv`, for example).
`./predict`