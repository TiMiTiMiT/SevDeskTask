package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class App
{
    public static void main(String[] args) {
        String bestCustomer = getBestCustomer(1, 2025);
        System.out.println(bestCustomer);
    }

    public static String getBestCustomer(int month, int year){

        // get all invoices during month of given date
        String allInvoices = makeRequest("Invoice");

        JSONObject allInvoicesJson = new JSONObject(allInvoices);
        JSONArray invoicesArray = allInvoicesJson.getJSONArray("objects");

        // get the sum of netSum for every customer
        Map<String, Integer> idToAmountMap = new HashMap<>();
        for(int i = 0; i < invoicesArray.length(); i++){
            JSONObject currentObject = invoicesArray.getJSONObject(i);
            // get date of the object
            String invoiceDate = currentObject.getString("invoiceDate");

            String[] dateParts = invoiceDate.split("-");
            int invoiceYear = Integer.parseInt(dateParts[0]);   // First substring is the year
            int invoiceMonth = Integer.parseInt(dateParts[1]); // Second substring is the month

            if(invoiceYear == year && invoiceMonth == month){
                String customerId = currentObject.getJSONObject("contact").getString("id");
                int invoiceAmount = currentObject.getInt("sumNet");

                addOrUpdateAmount(idToAmountMap, customerId, invoiceAmount);
            }
        }

        // get customer with highest sumn of sumNet
        String highestCustomerId = "";
        int highestSum = 0;

        for (String id : idToAmountMap.keySet()) {
            int currentSum = idToAmountMap.get(id);

            if(currentSum > highestSum){
                highestCustomerId = id;
                highestSum = currentSum;
            }
        }

        // get all customers to find customer name
        String allCustomers = makeRequest("Contact");

        JSONObject allContactsJson = new JSONObject(allCustomers);
        JSONArray contactsArray = allContactsJson.getJSONArray("objects");
        for(int i = 0; i < contactsArray.length(); i++) {
            JSONObject currentObject = contactsArray.getJSONObject(i);
            String currentId = currentObject.getString("id");

            if(Objects.equals(currentId, highestCustomerId)){
                return "ID: " + highestCustomerId + "; Name: " + currentObject.getString("name");
            }
        }

        return "No Customer Found";
    }

    public static String makeRequest(String apiRoute){
        try{
            URL url = new URL("https://my.sevdesk.de/api/v1/" + apiRoute);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            //Add Api Token here
            connection.setRequestProperty("Authorization","YOUR API KEY");

            // read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString();
        } catch (Exception e){
            e.printStackTrace();
        }

        return "no response";
    }

    private static void addOrUpdateAmount(Map<String, Integer> map, String id, int amount) {
        if (map.containsKey(id)) {
            // If it exists, increase the amount
            map.put(id, map.get(id) + amount);
        } else {
            // If it doesn't exist, add a new entry
            map.put(id, amount);
        }
    }
}
