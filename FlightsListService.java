package com.ryanAir.restFulApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Antonio Jimenez Fernandez
 */

@Path("/flightsListService")
public class FlightsListService {
 
	String departure;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	Date departureDateTime;
	String arrival;
	Date arrivalDateTime;
	Integer stops;
	Long interval;
	JSONObject flights;
	JSONArray legs;
	HttpClient client;
	String json;
	
	  @GET
	  @Produces("application/json")
	  public Response listFlights(String url) throws JSONException, ClientProtocolException, IOException {
 
		HttpPost httpPost = new HttpPost(url);
        HttpResponse httpResponse = client.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream is = httpEntity.getContent();
        interval = TimeUnit.MILLISECONDS.toSeconds(arrivalDateTime.getTime() - departureDateTime.getTime());
        
        if((departureDateTime.before((Date) flights.get("departureDateTime"))) 
        		&& (arrivalDateTime.after((Date) flights.get("arrivalDateTime")))
        		&& (interval > 7200)){

			for(int i = 0; i < legs.length(); i++)
			{
				flights = legs.getJSONObject(i);
			}

			try {
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
		        StringBuilder sb = new StringBuilder();
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		            sb.append(line + "\n");
		            System.out.println(line);
		        }
		        is.close();
		        json = sb.toString();

		    } catch (Exception e) {
		        System.out.println("Buffer Error, Error converting result " + e.toString());
		    }

		    try {
		    	flights = new JSONObject(json);
		    } catch (JSONException e) {
		        System.out.println("error on parse data in jsonparser.java");
		    }
			
			String result = "@Produces(\"application/json\") Output: \n\nList of Flights Output: \n\n" + flights;
			return Response.status(200).entity(result).build();
        }else{
        	return null; 
        }
	  }
	
}
