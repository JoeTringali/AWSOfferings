package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.eks.AmazonEKS;

import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class LambdaFunctionHandler implements RequestStreamHandler {
	
	private JSONParser parser = new JSONParser();
	private String servicesNeededAsString;
    private static final String amazonawsDotComLiteral = ".amazonaws.com";
    private static final String cnDotAmazonawsDotComLiteral = "cn.amazonaws.com";
	
	@SuppressWarnings("unchecked")
	public void handleRequest(
    		InputStream inputStream, 
    		OutputStream outputStream, 
    		Context context) throws IOException 
	{
        LambdaLogger logger = context.getLogger();
        logger.log("Loading Java Lambda handler of LambdaFunctionHandler");
        servicesNeededAsString = "*";
        int responseCode = 200;
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject responseJson = new JSONObject();
        
        try
        {
            JSONObject event = (JSONObject) parser.parse(reader);
            Optional<JSONObject> queryStringParameters = 
                Optional.ofNullable((JSONObject) event.get("queryStringParameters"));
            queryStringParameters.ifPresent(qsp -> servicesNeededAsString = (String) qsp.get("services"));
            
            Set<String> servicesNeeded = 
                new HashSet<String>(Arrays.asList(
                    servicesNeededAsString.toLowerCase().split("\\s*,\\s*")));
            servicesNeeded.remove("*");
            List<Region> regions = RegionUtils.getRegions();
            Map<String, Set<String>> regionServicesMap = new HashMap<String, Set<String>>();
            regions.forEach(region ->
            {
            	final String regionName = region.getName();
            	final String periodPlusRegion = "." + regionName;
            	Collection<String> availableEndpoints = region.getAvailableEndpoints();
            	List<String> availableEndpointsWithoutSuffix = 
            	    availableEndpoints
            	        .stream()
            	        .map(availableEndpoint -> 
            	        {
            	        	return removeEndpointSuffix(
									periodPlusRegion, availableEndpoint);
                        })
            	        .filter(service -> servicesNeededAsString.equals("*") || servicesNeeded.contains(service))
            	        .collect(Collectors.toList());
            	Set<String> servicesSet = new HashSet<String>();
            	availableEndpointsWithoutSuffix.forEach(service ->
            	{
            		servicesSet.add(service);
            		if (servicesNeededAsString.equals("*"))
            		{
            			servicesNeeded.add(service);
            		}
            		regionServicesMap.put(regionName, servicesSet);
            	});
            });
            int servicesNeededSize = servicesNeeded.size();
            Set<String> regionsWithServicesNeeded = new HashSet<String>();
            regionServicesMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() == servicesNeededSize)
                .forEach(entry -> regionsWithServicesNeeded.add(entry.getKey()));
            JSONObject responseBody = new JSONObject();
            JSONArray servicesNeededList = new JSONArray();
            servicesNeeded
                .stream()
                .sorted()
                .forEach(service -> servicesNeededList.add(service));
            responseBody.put("services", servicesNeededList);
            JSONArray list = new JSONArray();
            regionsWithServicesNeeded
                .stream()
                .sorted()
                .forEach(region -> list.add(region));
            responseBody.put("regions", list);
            JSONObject headerJson = new JSONObject();
            headerJson.put("Content-Type", "application/json");
            responseJson.put("isBase64Encoded", false);
            responseJson.put("statusCode", responseCode);
            responseJson.put("headers", headerJson);
            responseJson.put("body", responseBody.toString());
        }
        catch(ParseException parseException)
        {
        	responseJson.put("statusCode", 400);
        	responseJson.put("exception", parseException);
        }
        
        logger.log(responseJson.toJSONString());
        OutputStreamWriter outputStreamWriter = 
            new OutputStreamWriter(outputStream, "UTF-8");
        outputStreamWriter.write(responseJson.toJSONString());
        outputStreamWriter.close();
    }

	private String removeEndpointSuffix(final String periodPlusRegion, 
        String availableEndpoint) {
		final int nPos1 = availableEndpoint.indexOf(cnDotAmazonawsDotComLiteral);
		if (nPos1 >= 0)
		{
			availableEndpoint = availableEndpoint.substring(0, nPos1);
		}
		final int nPos2 = availableEndpoint.indexOf(amazonawsDotComLiteral);
		if (nPos2 >= 0)
		{
			availableEndpoint = availableEndpoint.substring(0, nPos2);
		}
		final int nPos3 = availableEndpoint.indexOf(periodPlusRegion);
		if (nPos3 >= 0)
		{
			availableEndpoint = availableEndpoint.substring(0, nPos3);
		}
	    	  return availableEndpoint;
	}
}
