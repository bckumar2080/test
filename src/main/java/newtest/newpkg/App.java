package newtest.newpkg;

/**
 * Hello world!
 *
 */
 

	import java.io.File;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.lang.Object;


	import org.apache.http.Header;
	//get libraries
	import org.apache.http.HttpEntity;
	import org.apache.http.HttpResponse;
	import org.apache.http.NameValuePair;
	import org.apache.http.client.HttpClient;
	import org.apache.http.client.entity.UrlEncodedFormEntity;
	import org.apache.http.client.utils.URIBuilder;
	import org.apache.http.entity.StringEntity;
	import org.apache.http.impl.client.CloseableHttpClient;
	import org.apache.http.impl.client.DefaultHttpClient;
	import org.apache.http.impl.client.HttpClientBuilder;
	import org.apache.http.message.BasicHeader;
	import org.apache.http.message.BasicNameValuePair;
	import org.apache.http.protocol.HTTP;
	import org.apache.http.util.EntityUtils;

	import java.net.URISyntaxException;
	import java.util.AbstractList;
	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map.Entry;

	import org.apache.http.client.methods.CloseableHttpResponse;
	import org.apache.http.client.methods.HttpGet;
	import org.apache.http.client.methods.HttpPost;

	import java.io.BufferedReader;
	import java.io.InputStream;
	import java.io.InputStreamReader;
	import com.fasterxml.jackson.core.JsonGenerationException;
	import com.fasterxml.jackson.core.JsonParseException;
	import com.fasterxml.jackson.core.JsonParser;
	import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
	import com.fasterxml.jackson.databind.JsonMappingException;
	import com.fasterxml.jackson.databind.ObjectMapper;
	import com.fasterxml.jackson.databind.ObjectWriter;
	import com.fasterxml.jackson.databind.SerializationFeature;
	import com.fasterxml.jackson.databind.JsonNode;
	import com.fasterxml.jackson.databind.node.ArrayNode;
	import com.fasterxml.jackson.databind.node.ObjectNode;
	import com.fasterxml.jackson.databind.JsonSerializable.Base;


	@SuppressWarnings("deprecation")
	public class App {
			   
		private static CloseableHttpClient client = HttpClientBuilder.create().build();
		private static String apijsonoutput;
		private static Object jsonapiobject;
		private static Object jsonapiIDsobject;
		private static Object jsonpkgobject;
		private static String PostMsg;
		private static String postApiname;
		private static String postPkgname;
		private static Object depEnv;
		public static void main(String args[]) throws Exception {
		buildAPI();
		postAPI();
			//getAPI();
			//buildPkg();
			//postPkg();
		}
		
//		public static void buildAPI() throws IOException, URISyntaxException 	
		public static void buildAPI() throws IOException, URISyntaxException 	
		{
			
		try {
	  //load API file
		      ObjectMapper mapper = new ObjectMapper();
		      File apiFile = new File("C:\\Applications\\Automation_Work\\testing.json");
		      JsonNode apiJson= mapper.readTree(apiFile);
		      ObjectNode apiNode = (ObjectNode) apiJson;
		      JsonNode apiEndpoints= apiJson.get("endpoints");
		      
		  // load properties file
		      ObjectMapper propmapper = new ObjectMapper();
		      File propFile = new File("C:\\Applications\\Automation_Work\\prop_modif2.json");
		      JsonNode prop= propmapper.readTree(propFile);
		     ObjectNode depEnvObj = (ObjectNode) ((prop.get("env")).get("organization"));
		     depEnv = depEnvObj;
		   
		      // Not required
		      // ObjectNode prop = (ObjectNode) propJson;
		      JsonNode propEndpoints = prop.get("endpoints");
		      
		   
		      
	      //replace API Name
		      (apiNode).put("name",prop.get("Api_name").textValue());
		   System.out.println("Replaced API Name"+ "\n");    
		 
		    //replace env org
		      apiNode.replace("organization",(JsonNode) depEnv);
		  //  System.out.println("Inserted Environment information"+ "\n");
		      	      
		 
		      //replace endpoint Name, Domain Name, system domain loop
		      
		      for (int i = 0; i < (apiEndpoints.size()); i++) 
		      {
		    	  if (i == (propEndpoints.size()))
		    		  break;
		    	//  ((ObjectNode)apiEndpointNode.get(i)).put("name",((propEndpoints.get(i)).get("name")).textValue() );
		    	  ObjectNode apiEndpointNode = ((ObjectNode)apiEndpoints.get(i));
		    	  ObjectNode propEndpointNode = ((ObjectNode)propEndpoints.get(i));
		    	  
		    	  ObjectNode apioAuthNode = (ObjectNode)apiEndpointNode.get("processor").get("preInputs");
		    	  ObjectNode propoAuthNode = (ObjectNode)prop.get("env").get("oAuth");
		    	
		    	//Replace Endpoint
		    	  apiEndpointNode.put("name", propEndpointNode.get("name").textValue());
		    	  
		    	//Replace trafficManagerDomain
		    	  apiEndpointNode.put("trafficManagerDomain", prop.get("env").get("trafficManagerDomain").textValue());
		    	  
		    	  //Replace oAuth Token SP Key 
		    	  
		    	  apioAuthNode.put("shared_token_spkey", propoAuthNode.get("shared_token_spkey").textValue());
		    	  
		    	//replace TM Domain
		 	     
		    	  //((ObjectNode)((ObjectNode)apiJson).get("endpoints").get(0)).put("trafficManagerDomain",prop.get("trafficManagerDomain").textValue() );
		 	      
		    	  
		    	//replace Domain Name, system domain loop
		    	  for (int j = 0; j < (apiEndpointNode.get("publicDomains").size()); j++) 
			      {
		    		  ObjectNode apiEndpointDomainNode = ((ObjectNode)apiEndpointNode.get("publicDomains").get(j));
		    		  ObjectNode propEndpointDomainNode = ((ObjectNode)propEndpointNode.get("publicDomains").get(j));
		    		 
		    		 // System.out.println(propEndpointDomainNode);
			    	//  System.out.println("printed once");
			    	  apiEndpointDomainNode.put("address",propEndpointDomainNode.get("address").textValue());
			    }
		    	  
		    	  
		    	//replace system domain Name
		    	  for (int j = 0; j < (apiEndpointNode.get("systemDomains").size()); j++) 
			      {
		    		  ObjectNode apiEndpointDomainNode = ((ObjectNode)apiEndpointNode.get("systemDomains").get(j));
		    		  ObjectNode propEndpointDomainNode = ((ObjectNode)propEndpointNode.get("systemDomains").get(j));
		    		//  System.out.println(propEndpointDomainNode);
			    	//  System.out.println("printed once");
			    	  apiEndpointDomainNode.put("address",propEndpointDomainNode.get("address").textValue());
			     }
		    	  	    	
		    	 }
		      	 
		     System.out.println(apiJson);
		     
		     // write output to file
		     
		     ObjectMapper mapper1 = new ObjectMapper();
		     ObjectWriter writer = mapper1.writer(new DefaultPrettyPrinter());
		     writer.writeValue(new File("C:\\Applications\\Automation_Work\\newApi.json"), apiJson);
		     
//		     System.out.println(apiJson.get("name").asText());
		     String apijsonoutput1 = apiJson.get("name").asText();
		     apijsonoutput = apijsonoutput1;
		     ObjectNode jsonobj = (ObjectNode) apiJson;
		     jsonapiobject = jsonobj;
//		    final String ApiNameOutput = apiJson.get("name").asText();
		    
				//System.out.println(apijsonoutput);
				//System.out.println("API Service created");
				//System.out.println(jsonapiobject);
		//    s+=ApiNameOutput; 
	 } catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	 
	 
	//get API & Method IDs
	 
		
		//// working post code
		public static void postAPI() throws Exception 
		{
			System.out.println(jsonapiobject);
			   	 ObjectNode postapiNode = (ObjectNode) jsonapiobject; 
			   	System.out.println(postapiNode);
				String url = "https://integration.cloud.tibcoapps.com:443/ubc7achkvvvmhva7akjg4j55yfewndhm/services";
				
				HttpPost post = new HttpPost(url);
				post.addHeader("Accept", "application/json");
				post.addHeader("headerValue", "HeaderInformation");
				post.addHeader("User-Agent", "PostmanRuntime/7.4.0");
				post.addHeader("Content-Type", "text/plain");

				//setting json object to post request.
				StringEntity entity = new StringEntity(postapiNode.toString(), "UTF8");
				post.setEntity(entity);
			
				
				try {
					HttpResponse response = client.execute(post);
					   //this is post response:
			        int posthttpStatus = response.getStatusLine().getStatusCode();
			        System.out.println("Response Code : " +posthttpStatus);
			        
			      //read post response body:
			        HttpEntity responseentity = response.getEntity();
					if (responseentity != null) {
			           String postRespStr = EntityUtils.toString(responseentity, "UTF-8"); 
			           // parsing JSON
			           ObjectMapper postmapper = new ObjectMapper();
			           JsonNode postmapperObj = postmapper.readTree(postRespStr);
			           JsonNode postApiNode = postmapperObj.get("name");
			           //Convert String to JSON Object
			           postApiname = postApiNode.textValue();
			           System.out.println(postApiNode);
			           System.out.println(postmapperObj);
			        }
			}
			 catch (Exception e) {
			  }
				    
				    
				    System.out.println(postApiname);
				    
				  
				  //  String Jsonpoststring = Jsonpostresp.toString;
				   // APIname = Jsonpoststring;
		}
	}
	
