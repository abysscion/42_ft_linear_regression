import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Dictionary;

public class train {
    private static ArrayList<int[]> data;
    private static String filePath;
    private static boolean voFlag;
    private static boolean vsFlag;
    private static boolean errFlag;
    private static float lrFlag;
    private static double tmpTheta0;
    private static double tmpTheta1;
    private static double theta0;
    private static double theta1;
    private static double deltaMSE;
    private static double lastMSE;
    private static double curMSE;

    public static void main(String[] args) {
        try {
            if (args.length < 1)
                printUsage();
            else {
                validateArguments(args);
                init(args[args.length - 1]);
            }
        }
        catch (Exception e) {
            System.out.println("Error happened! The message is: \n" + e.toString());
        }
    }

    private static void init(String fileName) throws Exception{
        var reader = new BufferedReader(new FileReader(fileName));
        var line = "";

        data = new ArrayList<>();
        if (reader.readLine() != null) {
            while ((line = reader.readLine()) != null) {
                var dataEntry = line.split(",");
                var dataCell = new int[2];

                dataCell[0] = Integer.parseInt(dataEntry[0]);
                dataCell[1] = Integer.parseInt(dataEntry[1]);
                data.add(dataCell);
            }
        }

        theta0 = 0.0;
        theta1 = 0.0;
        tmpTheta0 = 0.0;
        tmpTheta1 = 0.0;
        lastMSE = 0.0;
        curMSE = calculateMSE();
        deltaMSE = curMSE;
    }

    private static double calculateMSE() {
        var tmpSum = 0.0;
        var tmpDiff = 0.0;

        for (int i = 0, dataSize = data.size(); i < dataSize; i++) {
            var dataEntry = data.get(i);
            if (i > 0) {
                tmpDiff = estimatePrice(dataEntry[0]) - dataEntry[1];
                tmpDiff *= tmpDiff;
                tmpSum += tmpDiff;
            }
        }
        return tmpSum / data.size();
    }

    private static void printRedData(){
        if (data != null)
        {
            System.out.println("km, mileage");
            for (var dataEntry:data)
                System.out.println(dataEntry[0] + "," + dataEntry[1]);
        }
    }

    private static double estimatePrice(int mileage) {
        return ((tmpTheta0 + (tmpTheta1 * mileage)));
    }

    private static void validateArguments(String[] args) throws Exception {
        for (int i = 0, argsLength = args.length; i < argsLength; i++) {
            var argument = args[i];

            switch (argument) {
                case "-vo":
                    if (!voFlag)
                        voFlag = true;
                    else
                        throw new Exception("-vo flag is already set!");
                    break;
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
                default:
                    if (argument.contains("-lr:")) {
                        try {
                            var lrValue = Float.parseFloat(argument.substring(4));
                            if (lrFlag >= 0.1f) {
                                throw new Exception("-lr flag is already set!");
                            } else if (lrValue < 0.1f || lrValue > 1.0f)
                                throw new Exception("wrong value for -lr flag");
                            else
                                lrFlag = lrValue;
                        } catch (NumberFormatException e) {
                            throw new Exception("wrong value for -lr flag");
                        } catch (NullPointerException e) {
                            throw new Exception("no numeric argument provided for -lr flag");
                        }
                    } else {
                        if (i != argsLength - 1)
                            throw new Exception("File path should be last argument!");
                        var file = new File(argument);
                        if (!file.exists())
                            throw new Exception("File [" + argument + "] does not exist!");
                        if (argument.length() < 4)
                            throw new Exception("File [" + argument + "] is not a .csv file dataset!");
                        if (argument.substring(argument.length() - 4).compareTo(".csv") != 0)
                            throw new Exception("File [" + argument + "] has wrong extension!");
                    }
                    break;
            }
        }
    }

    private static void printUsage() {
        System.out.println("Usage: train [options] <path/to/dataset.csv>\n\t\t(to train model on provided dataset)");
        System.out.println("options:\n" +
                                "\t-vo\t - visualize original dataset\n" +
                                "\t-vs\t - visualize standardized dataset\n" +
                                "\t-err\t - print mean squared error after each regression iteration\n" +
                                "\t-lr:%number%\t - set learning rate, where %number% is rate in range (0.1, 1.0)");
    }
}
