package GetCountryPhotoDetails;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {
	
	private final static String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) {
        String phoneNumber = "";
        String countryCode = "";
        countryCode = args[0];
        phoneNumber = args[1];
        
    	SpringApplication.run(Application.class);
    	
   	
    	String urlToPost = "http://localhost:8085/phone-number-validation-api-server/v1/Mobilenumber/" + 
    						"mobile-number/validate?phoneNumber=" + phoneNumber.trim() + "&isoCountryCode=" + countryCode.trim(); 
    	
    	System.out.println(urlToPost);
    	JSONObject jsonMobile = null;
    	JSONObject jsonLandLine = null;
    	
    	try {
			String result = sendGet(urlToPost);
			System.out.println(" the value of the result " + result);
			// Use this convert to json.
			jsonMobile = (JSONObject) new JSONParser().parse(result);
			
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
    	
    	urlToPost = "http://localhost:8085/phone-number-validation-api-server/v1/Landlinenumber/landline-number/" + 
    			"validate?phoneNumber=" + phoneNumber.trim() +  "&isoCountryCode=" + countryCode.trim();
    	System.out.println(urlToPost);
    			
   
    	try {
    		String result = sendGet(urlToPost);
			// Use this convert to json.
			jsonLandLine = (JSONObject) new JSONParser().parse(result);
    		
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}

    	if ((jsonMobile.get("valid").equals("true")) || (jsonLandLine.get("valid").equals("true"))) {
    		System.out.println(" This is valid Phone Number ");
    	}
    	Map<String, String> _map = task2();
    	System.out.println(" the iso2 value of the Dutch " + _map.get("Dutch"));
    	
    }

    private static Map<String, String> task2() {
    	// Read the file.
    	FileInputStream fis = null;
        BufferedReader reader = null;
        Map<String, String> _map= new HashMap<String, String>();
              
        try {
            fis = new FileInputStream("country-codes.csv");
            reader = new BufferedReader(new InputStreamReader(fis));
          
            System.out.println("Reading File line by line using BufferedReader");
            String line = reader.readLine();
            while (line != null) {
            	// split the line and get the values
            	System.out.println(" the line value " + line);
            	int commaLoc = line.indexOf(",");
            	String countryName = line.substring(0,commaLoc);
            	String countryCode = line.substring(commaLoc+1);
                System.out.println(countryName);
                System.out.println(countryCode);
                if (countryName.equalsIgnoreCase("Dutch"))
                	_map.put(countryName, countryCode);
                line = reader.readLine();
            }           
          
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return _map;
    }

    // Get the results of the URL  and convert to String and send back
    private static String sendGet(String url) throws Exception {

 		URL obj = new URL(url);
 		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

 		// optional default is GET
 		con.setRequestMethod("GET");

 		//add request header
 		con.setRequestProperty("User-Agent", USER_AGENT);

 		int responseCode = con.getResponseCode();
 		System.out.println("\nSending 'GET' request to URL : " + url);
 		System.out.println("Response Code : " + responseCode);

 		BufferedReader in = new BufferedReader(
 		        new InputStreamReader(con.getInputStream()));
 		String inputLine;
 		StringBuffer response = new StringBuffer();

 		while ((inputLine = in.readLine()) != null) {
 			response.append(inputLine);
 		}
 		in.close();

 		//print result
 		return response.toString();

 	}    
    
    

}