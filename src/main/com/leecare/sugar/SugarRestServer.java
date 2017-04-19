package com.leecare.sugar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.jersey.api.core.HttpRequestContext;
 
@Path("/rest/v10")
public class SugarRestServer {
	
	org.apache.logging.log4j.Logger logger = LogManager.getLogger(SugarRestServer.class);
	
	//dm.crm.wmb.org.au/rest/v10/oauth2/token
	@POST
	@Path("/oauth2/token")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postLogin(@Context HttpServletRequest httpServletRequest, @Context UriInfo info, @Context HttpHeaders headers, InputStream incomingData,@QueryParam("salt") String salt) {
		logHttpPostRequest(info, headers, incomingData, salt, httpServletRequest);
		return Response.status(200).entity("{ \"access_token\": \"ddd31419-6619-c79d-401f-58c7686e964b\", "
							    	+ "\"expires_in\": 3600,"
							        + "\"token_type\": \"bearer\", "
							        + "\"scope\": null,"
							        + "\"refresh_token\": \"ddf16d9c-0de7-e1a9-13a2-58c76826dce4\","
							        + "\"refresh_expires_in\": 1209599,"
							        + "\"download_token\": \"df3ae42d-2900-c1a0-c6f4-58c768b06b1e\""
									+ "}").build();
	}
	
	@POST
	@Path("/Contacts")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postAccounts(@Context HttpServletRequest httpServletRequest,
			@Context UriInfo info, 
			@Context HttpHeaders headers, 
			InputStream incomingData) {
		return logHttpPostRequest(info, headers, incomingData, null, httpServletRequest);
	}
	
	
	private Response logHttpPostRequest(UriInfo info, HttpHeaders headers, InputStream incomingData, String salt, HttpServletRequest httpServletRequest) {
		logger.debug("POST " + ". PATH =" + info.getPath());
		String contentType = null;
		MultivaluedMap<String, String> map = headers.getRequestHeaders();
		for(Map.Entry<String, List<String>> entry : map.entrySet()){
			logger.debug("Key = " + entry.getKey());
			for(String value : entry.getValue()){
				logger.debug("  Value = " + value);
				if(entry.getKey().equalsIgnoreCase("Content-Type")) {
					contentType = value;
				}
			}
			
		}
		
		logger.debug("  salt = " + salt);
		StringBuilder requestData = new StringBuilder();
		
		logger.debug("POST : Data Received: " + requestData.toString());
		
		if(contentType != null && contentType.contains("json")) {
			
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
				String line = null;
				while ((line = in.readLine()) != null) {
					requestData.append(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error Parsing: - ");
			}
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(requestData.toString());
			String prettyJsonString = gson.toJson(je);
			logger.debug("POST :JASON Data Received: " + prettyJsonString);
			//return Response.status(200).entity(prettyJsonString).build();
		} else if(contentType != null && contentType.contains("multipart")){
			try {
				httpServletRequest.getParameterMap();
				Collection<Part> parts = httpServletRequest.getParts();
				httpServletRequest.getParameterMap();
				for(Part p : parts) {
					String result = IOUtils.toString(p.getInputStream(), StandardCharsets.UTF_8);
					logger.debug("POST : Form Data Received: " + p.getName() +  ":" + result + ":ContentType=" + p.getContentType());
				}
				BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
				String line = null;
				while ((line = in.readLine()) != null) {
					requestData.append(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error Parsing: - ");
			}
		}
		//return Response.status(200).entity("Rest Server Response .......").build();
		return Response.status(200).entity("{\"sugarId\" : \"ddd31419-6619-c79d-401f-58c7686e964b\"}").build();
	}
	 
}