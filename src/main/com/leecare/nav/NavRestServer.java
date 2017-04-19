package com.leecare.nav;

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
 
@Path("/")
public class NavRestServer {
	
	org.apache.logging.log4j.Logger logger = LogManager.getLogger(NavRestServer.class);
	
	
	@POST
	@Path("/charge")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postAll(@Context HttpServletRequest httpServletRequest, @Context UriInfo info, @Context HttpHeaders headers, InputStream incomingData,@QueryParam("salt") String salt) {
		return logHttpPostRequest(info, headers, incomingData, salt, httpServletRequest);
	}
	
	@POST
	@Path("/ResidentMaster")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postResidentss(@Context HttpServletRequest httpServletRequest, @Context UriInfo info, @Context HttpHeaders headers, InputStream incomingData, @QueryParam("salt") String salt) {
		logger.debug("POST ResidentMaster");
		return logHttpPostRequest(info, headers, incomingData, salt, httpServletRequest);
	}

	@PUT
	@Path("/ResidentMaster")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putResidentss(@Context HttpServletRequest httpServletRequest, @Context UriInfo info, @Context HttpHeaders headers, InputStream incomingData, @QueryParam("salt") String salt) {
		logger.debug("PUT ResidentMaster");
		return logHttpPostRequest(info, headers, incomingData, salt, httpServletRequest);
	}
	
	@POST
	@Path("/consumables")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postConsumables(@Context HttpServletRequest httpServletRequest, @Context UriInfo info, @Context HttpHeaders headers, InputStream incomingData, @QueryParam("salt") String salt) {
		return logHttpPostRequest(info, headers, incomingData, salt, httpServletRequest);
	}
	
	@POST
	@Path("/uploadedconsumables")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postUploadedConsumables(@Context HttpServletRequest httpServletRequest, @Context UriInfo info, @Context HttpHeaders headers, InputStream incomingData, @QueryParam("salt") String salt) {
		return logHttpPostRequest(info, headers, incomingData, salt, httpServletRequest);
	}
	@POST
	@Path("/AdmissionDischargeMovement")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postMovements(@Context HttpServletRequest httpServletRequest, @Context UriInfo info, @Context HttpHeaders headers, InputStream incomingData, @QueryParam("salt") String salt) {
		return logHttpPostRequest(info, headers, incomingData, salt, httpServletRequest);
	}
	
	@POST
	@Path("/FinancialAssessmentForm")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postFAFForm(@Context HttpServletRequest httpServletRequest, @Context UriInfo info, @Context HttpHeaders headers, InputStream incomingData, @QueryParam("salt") String salt) {
		logger.debug("POST FinancialAssessmentForm" + ". PATH =" + info.getPath());
		return logHttpPostRequest(info, headers, incomingData, salt, httpServletRequest);
	}
	
	@POST
	@Path("/ResidentAssessmentForm")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postRAFForm(@Context HttpServletRequest httpServletRequest, @Context UriInfo info, @Context HttpHeaders headers, InputStream incomingData, @QueryParam("salt") String salt) {
		return logHttpPostRequest(info, headers, incomingData, salt, httpServletRequest);
	}
	
	@POST
	@Path("/ResidentAdminDetail")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postRCADForm(@Context HttpServletRequest httpServletRequest, @Context UriInfo info, @Context HttpHeaders headers, InputStream incomingData, @QueryParam("salt") String salt) {
		logger.debug("POST " + ". PATH =" + info.getPath());
		return logHttpPostRequest(info, headers, incomingData, salt, httpServletRequest);
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
			logger.error("Error Parsing: - ");
		}
		
		logger.debug("POST : Data Received: " + requestData.toString());
		if(contentType != null && contentType.contains("json")) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(requestData.toString());
			String prettyJsonString = gson.toJson(je);
			logger.debug("POST :JASON Data Received: " + prettyJsonString);
			return Response.status(200).entity(prettyJsonString).build();
		}
		return Response.status(200).entity("Rest Server Response .......").build();
	}
	@GET
	@Path("/")
	public Response verifyRESTService(InputStream incomingData) {
		String result = "REST SERVER Successfully started...................";
		return Response.status(200).entity(result).build();
	}
 
}