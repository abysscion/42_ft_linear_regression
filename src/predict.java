public class predict {
    private double theta0;
    private double theta1;
    private double tmpTheta0;
    private double tmpTheta1;
    private double lastMSE;
    private double curMSE;
    private double deltaMSE;

    public static void main(String[] args) {
        try {
            if (args.length < 1)
                PrintUsage();
            else
                ProcessArguments(args);
        }
        catch (Exception e) {
            System.out.println("Error happened! The message is: \n" + e.toString());
        }
    }

    private static void ProcessArguments(String[] args) {
        
    }

    private static void PrintUsage() {
        System.out.println("Usage: predict <desired numeric mileage to predict>");
    }
}
