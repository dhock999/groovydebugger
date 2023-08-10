package com.manywho.services.atomsphere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.manywho.services.atomsphere.ServiceConfiguration;

public class AtomsphereAPI {
	
    //TODO queryMore queryToken uses just a text blob, not jsonobject so we need to support passing a string payload
    //APIM https://api.boomi.com/apim/api/rest/v1/{accountID}
    public static Document executeAPIXML(ServiceConfiguration configuration, String entityName, String method, String resource, String payload, boolean isAPIMEntity) throws SAXException, IOException, ParserConfigurationException 
	{
		if (resource!=null)
			resource="/"+resource;
		else 
			resource="";
		String urlString = "https://api.boomi.com/api/rest/v1/";
		if (isAPIMEntity)
			urlString = "https://api.boomi.com/apim/api/rest/v1/";
        StringBuffer response= new StringBuffer();
        HttpURLConnection conn=null;
        String responseCode = null;
		
        try {
Thread.sleep(300); //Boomi Default Rate Limit is 1 call per 200ms   	
    		URL url = new URL(urlString+String.format("%s/%s%s", configuration.getAccount(), entityName, resource));
//    		logger.info(method + " " + url.toString());
            conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(method);
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Content-Type", "application/xml");
	        conn.setRequestProperty("Accept", "application/xml");
	    	String userpass = configuration.getUsername() + ":" + configuration.getPassword();
	    	String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
	    	conn.setRequestProperty ("Authorization", basicAuth);
	    	if (payload!=null)
	    	{
		        conn.setDoInput(true);
	    		String body = payload;
//		        LOGGER.info(body);
		        byte[] input = body.getBytes();
		 		conn.setRequestProperty("Content-Length", ""+input.length);
		        OutputStream os = conn.getOutputStream();
		        os.write(input, 0, input.length);  
	    	}  
	    		
	 
			responseCode = conn.getResponseCode() +"";
	        
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
			    response.append(responseLine+"\r\n");
			}
		} catch (ProtocolException e1) {
			throw new RuntimeException(e1);
		} catch (InterruptedException e1) {
			throw new RuntimeException(e1);
		} catch (IOException e) {
			try {
				if (conn!=null)
				{
					responseCode = conn.getResponseCode() +"";
					response.append("Error code: " + conn.getResponseCode());
					response.append(" " + conn.getResponseMessage() + " ");
					InputStream errorStream = conn.getErrorStream();
					if (errorStream!=null)
					{
						BufferedReader br = new BufferedReader(new InputStreamReader(errorStream)) ;
						String responseLine = null;
						while ((responseLine = br.readLine()) != null) {
						    response.append(responseLine+"\r\n");
						}
					}
//					logger.error(response.toString());
					throw new RuntimeException(response.toString());
				} else throw new RuntimeException(e);
			} catch (IOException e1) {
				throw new RuntimeException(e1);
			}
		}
//	    LOGGER.info(response.toString());
        String responseString = response.toString();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(responseString)));

        return doc;
	}
}
