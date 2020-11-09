import java.io.*;

public class predict {
    private static int mileageToPredict;

    public static void main(String[] args) {
        try {
            System.out.print("Enter mileage to predict price: ");
            var inputReader = new BufferedReader(new InputStreamReader(System.in));
            validateMileage(inputReader.readLine());
            var fileReader = new BufferedReader(new FileReader("model.csv"));
            var readData = fileReader.readLine().split(",");
            var predictedPrice = Double.parseDouble(readData[0]) + Double.parseDouble(readData[1]) * mileageToPredict;
            System.out.println(predictedPrice);
        }
        catch (FileNotFoundException e) {
            System.out.println("0");
        }
        catch (NumberFormatException e) {
            System.out.println("Error: wrong mileage format!");
        }
        catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }

    private static void validateMileage(String mileage) throws Exception {
        var mlg = Integer.parseInt(mileage);

        if (mlg < 0)
            throw new Exception("mileage can't be negative!");
        mileageToPredict = mlg;
    }
}
