import java.util.Scanner;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class CurrencyConverter {

    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the base currency (e.g., USD): ");
        String baseCurrency = scanner.nextLine().toUpperCase();
        
        System.out.print("Enter the target currency (e.g., EUR): ");
        String targetCurrency = scanner.nextLine().toUpperCase();
        
        double exchangeRate = fetchExchangeRate(baseCurrency, targetCurrency);
        if (exchangeRate == -1) {
            System.out.println("Error fetching exchange rate.");
            return;
        }

        System.out.print("Enter the amount to convert: ");
        double amount = scanner.nextDouble();
        
        double convertedAmount = amount * exchangeRate;
        
        System.out.printf("Converted Amount: %.2f %s\n", convertedAmount, targetCurrency);
    }

    private static double fetchExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            URL url = new URL(API_URL + baseCurrency);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }

            Scanner sc = new Scanner(url.openStream());
            StringBuilder inline = new StringBuilder();
            while (sc.hasNext()) {
                inline.append(sc.nextLine());
            }
            sc.close();

            JSONObject jsonResponse = new JSONObject(inline.toString());
            return jsonResponse.getJSONObject("rates").getDouble(targetCurrency);

        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
        
        return -1; 
    }
}
