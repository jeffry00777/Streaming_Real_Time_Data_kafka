import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class DataStream {

    public static JSONObject getData() throws IOException {
        String apiUrl = "https://randomuser.me/api/";
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        }

        StringBuilder jsonResponse = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            jsonResponse.append(scanner.nextLine());
        }
        scanner.close();

        JSONObject response = new JSONObject(jsonResponse.toString());
        return response.getJSONArray("results").getJSONObject(0);
    }

    public static JSONObject formatData(JSONObject dataRes) {
        JSONObject formattedData = new JSONObject();
        JSONObject location = dataRes.getJSONObject("location");
        
        formattedData.put("first_name", dataRes.getJSONObject("name").getString("first"));
        formattedData.put("last_name", dataRes.getJSONObject("name").getString("last"));
        formattedData.put("gender", dataRes.getString("gender"));
        formattedData.put("address", location.getJSONObject("street").getInt("number") + " " + 
                location.getJSONObject("street").getString("name") + 
                " " + location.getString("city") + 
                " " + location.getString("state") + 
                " " + location.getString("country"));
        formattedData.put("postcode", location.get("postcode"));
        formattedData.put("email", dataRes.getString("email"));
        formattedData.put("username", dataRes.getJSONObject("login").getString("username"));
        formattedData.put("dob", dataRes.getJSONObject("dob").getString("date"));
        formattedData.put("registered_date", dataRes.getJSONObject("registered").getString("date"));
        formattedData.put("phone", dataRes.getString("phone"));
        formattedData.put("picture", dataRes.getJSONObject("picture").getString("medium"));

        return formattedData;
    }

    public static void streamData() {
        try {
            JSONObject res = getData();
            JSONObject formattedDataRes = formatData(res);
            System.out.println(formattedDataRes.toString(3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        streamData();
    }
}
