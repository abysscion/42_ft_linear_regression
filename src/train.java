import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class train {
    private static ArrayList<double[]> predictsData;
    private static ArrayList<double[]> originalData;
    private static boolean vpFlag;
    private static boolean voFlag;
    private static boolean errFlag;
    private static boolean vsFlag;
    private static boolean lrSet;
    private static double tmpTheta0;
    private static double tmpTheta1;
    private static double deltaMSE;
    private static double lastMSE;
    private static double theta0;
    private static double theta1;
    private static double curMSE;
    private static double minX;
    private static double maxX;
    private static double minY;
    private static double maxY;
    private static float lrFlag;

    public static void main(String[] args) {
        try {
            disableAccessWarnings();
            if (args.length < 1)
                printUsage();
            else {
                validateArguments(args);
                init(args[args.length - 1]);
                trainModel();
                if (vsFlag)
                    data_visualiser.showStandardizedData(originalData);
                if (voFlag)
                    data_visualiser.showOriginalData(originalData, minX, minY, maxX, maxY);
                if (vpFlag) {
                    if (!new File("predictsData.csv").exists()) {
                        System.out.println("Error: predictsData.csv does not exists!");
                        System.out.println("Tip: before using -vp flag you firstly should predict something...");
                    }
                    else {
                        readData("predictsData.csv", false);
                        standardizeData(predictsData);
                        data_visualiser.showPredictedOverOriginalData(originalData, predictsData, minX, minY, maxX, maxY);
                    }
                }
            }
        }
        catch (Exception e) {
            var msg = e.getMessage();
            if (msg != null)
                System.out.println("Error: " + msg);
            else
                System.out.println("Error: " + e.toString());
        }
    }

    public static double estimatePrice(double mileage) {
        return ((tmpTheta0 + (tmpTheta1 * mileage)));
    }

    private static void init(String dataPath) throws Exception{
        if (!lrSet)
            lrFlag = 1.0f;
        minX = minY = Integer.MAX_VALUE;
        maxX = maxY = Integer.MIN_VALUE;
        theta0 = 0.0;
        theta1 = 0.0;
        tmpTheta0 = 0.0;
        tmpTheta1 = 0.0;
        lastMSE = 0.0;
        readData(dataPath, true);
        curMSE = calculateMSE();
        deltaMSE = curMSE;
    }

    private static double calculateMSE() {
        var tmpSum = 0.0;
        var tmpDiff = 0.0;

        for (int i = 0, dataSize = originalData.size(); i < dataSize; i++) {
            var dataEntry = originalData.get(i);

            if (i > 0) {
                tmpDiff = estimatePrice(dataEntry[0]) - dataEntry[1];
                tmpDiff *= tmpDiff;
                tmpSum += tmpDiff;
            }
        }
        return tmpSum / originalData.size();
    }

    private static void setMinMaxOfData(ArrayList<double[]> data){
        for (var dataEntry : data) {
            if (dataEntry[0] < minX)
                minX = dataEntry[0];
            if (dataEntry[0] > maxX)
                maxX = dataEntry[0];
            if (dataEntry[1] < minY)
                minY = dataEntry[1];
            if (dataEntry[1] > maxY)
                maxY = dataEntry[1];
        }
    }

    private static void standardizeData(ArrayList<double[]> data) {
        setMinMaxOfData(data);
        for (var dataEntry:data) {
            dataEntry[0] = (dataEntry[0] - minX) / (maxX - minX);
            dataEntry[1] = (dataEntry[1] - minY) / (maxY - minY);
        }
    }

    private static void trainModel() throws IOException {
        standardizeData(originalData);
        while (deltaMSE > 0.0000001 || deltaMSE < -0.0000001) {
            theta0 = tmpTheta0;
            theta1 = tmpTheta1;
            tmpTheta0 -= getGradient0();
            tmpTheta1 -= getGradient1();
            lastMSE = curMSE;
            curMSE = calculateMSE();
            deltaMSE = curMSE - lastMSE;
            if (errFlag)
                System.out.println(curMSE);
        }
        theta1 = (maxY - minY) * theta1 / (maxX - minX);
        theta0 = minY + ((maxY - minY) * theta0) + theta1 * (1 - minX);
        saveModel();
    }

    private static void saveModel() throws IOException {
        var writer = new FileWriter("model.csv", false);

        writer.write(theta0 + "," + theta1);
        writer.flush();
        writer.close();
    }

    private static double getGradient0() {
        var tmpSum = 0.0;

        for (var dataEntry : originalData)
            tmpSum += (estimatePrice(dataEntry[0]) - dataEntry[1]);

        return lrFlag * (tmpSum / (originalData.size()));
    }

    private static double getGradient1() {
        var tmpSum = 0.0;

        for (var dataEntry : originalData)
            tmpSum += ((estimatePrice(dataEntry[0]) - dataEntry[1]) * dataEntry[0]);

        return lrFlag * (tmpSum / (originalData.size()));
    }

    private static void readData(String dataPath, final boolean original) throws Exception {
        try {
            if (!new File(dataPath).exists())
                throw new Exception("file [" + dataPath + "] not found!");
            var reader = new BufferedReader(new FileReader(dataPath));
            var line = "";

            if (original) {
                originalData = new ArrayList<>();
                reader.readLine();
            } else
                predictsData = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                var dataEntry = line.split(",");
                var dataCell = new double[2];

                dataCell[0] = Double.parseDouble(dataEntry[0]);
                dataCell[1] = Double.parseDouble(dataEntry[1]);
                if (original)
                    originalData.add(dataCell);
                else
                    predictsData.add(dataCell);
            }
        }
        catch (IOException e) {
            throw new Exception("unable to read or write data file!");
        }
        catch (NumberFormatException e) {
            throw new Exception("wrong symbols in data file [" + (original ? "data.csv" : "predictsData.csv") + "]");
        }
    }

    private static void validateArguments(String[] args) throws Exception {
        for (int i = 0, argsLength = args.length; i < argsLength; i++) {
            var argument = args[i];

            switch (argument) {
                case "-vs":
                    if (!vsFlag)
                        vsFlag = true;
                    else
                        throw new Exception("-vs flag is already set!");
                    break;
                case "-err":
                    if (!errFlag)
                        errFlag = true;
                    else
                        throw new Exception("-err flag is already set!");
                    break;
                case "-vp":
                    if (!vpFlag)
                        vpFlag = true;
                    else
                        throw new Exception("-vp flag is already set!");
                    break;
                case "-vo":
                    if (!voFlag)
                        voFlag = true;
                    else
                        throw new Exception("-vo flag is already set!");
                    break;
                default:
                    if (argument.contains("-lr:")) {
                        if (lrSet)
                            throw new Exception("-lr flag is already set!");
                        else {
                            try {
                                var lrValue = Float.parseFloat(argument.substring(4));

                                if (lrValue >= 0.001f && lrValue <= 1.0) {
                                    lrFlag = lrValue;
                                    lrSet = true;
                                }
                                else
                                    throw new Exception("wrong value for -lr flag");
                            } catch (NumberFormatException e) {
                                throw new NumberFormatException("wrong value for -lr flag");
                            } catch (NullPointerException e) {
                                throw new NullPointerException("no numeric argument provided for -lr flag");
                            }
                        }
                    } else {
                        if (i != argsLength - 1)
                            throw new Exception("unknown argument provided or file path is not last argument");
                        var file = new File(argument);
                        if (!file.exists())
                            throw new Exception("file [" + argument + "] does not exist!");
                        if (argument.length() < 4)
                            throw new Exception("file [" + argument + "] is not a .csv file dataset!");
                        if (argument.substring(argument.length() - 4).compareTo(".csv") != 0)
                            throw new Exception("file [" + argument + "] has wrong extension!");
                    }
                    break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void disableAccessWarnings() {
        try {
            Class unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Object unsafe = field.get(null);

            Method putObjectVolatile = unsafeClass.getDeclaredMethod("putObjectVolatile", Object.class, long.class, Object.class);
            Method staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset", Field.class);

            Class loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field loggerField = loggerClass.getDeclaredField("logger");
            Long offset = (Long) staticFieldOffset.invoke(unsafe, loggerField);
            putObjectVolatile.invoke(unsafe, loggerClass, offset, null);
        } catch (Exception ignored) {
        }
    }

    private static void printUsage() {
        System.out.println("Usage: train [options] <path/to/dataset.csv>\n\t\t(to train model on provided dataset)");
        System.out.println("options:\n" +
                                "\t-vo\t - visualize original dataset\n" +
                                "\t-vs\t - visualize standardized dataset\n" +
                                "\t-vp\t - visualize predicted over original dataset\n" +
                                "\t-err\t - print mean squared error after each regression iteration\n" +
                                "\t-lr:<number>\t - set learning rate, where number is rate in range (0.1, 1.0)");
    }
}
