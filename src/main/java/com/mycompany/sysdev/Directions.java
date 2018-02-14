/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sysdev;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import com.mycompany.sysdev.graph.GraphInstantiation;
import com.mycompany.sysdev.graph.Graph;
/**
 *
 * @author dell
 */
@Path("services/directions")
public class Directions {
	
    private static final String GOOGLE_DIRECTIONS = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_KEY = "key=AIzaSyBmOV7fMxeGKh8SvaHTHC8R-ess45KUFz0";
    private GraphInstantiation fg = new com.mycompany.sysdev.graph.GraphInstantiation(); // Load graph only once
    private Graph graph = fg.getGraph();
    
    // Google Directions API call based on the URL 
    @GET
    @Path("/uri")
    @Produces(MediaType.APPLICATION_JSON)
    public static String directionSearchJerseyURI(
            @QueryParam("originLat") double origin_lat,
            @QueryParam("originLon") double origin_lon,
            @QueryParam("destinationLat") double destination_lat,
            @QueryParam("destinationLon") double destination_lon) {
	
        Client client = Client.create();
        WebResource webresource = client.resource(buildDirectionQueryString(origin_lat, origin_lon, destination_lat, destination_lon));
        // Add acceptable media types and invoke the GET method with the parameter of the type of the returned response
        ClientResponse response = webresource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed: HTTP error code: " + response.getStatus());
        }
        String responseString = response.getEntity(String.class);
        return responseString;
    }
    
    // Google Directions API call based on the JSON file
    @POST
    @Path("/obj")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public static String directionSearchJerseyOBJ(String json_string) {
        
        // JSON parsing - doesn't work
        ObjectMapper mapper = new ObjectMapper();
        
        RequestObject request_object = null;
        double origin_lat = 0;
        double origin_lon = 0;
        double destination_lat = 0;
        double destination_lon = 0;
        
        try {
            request_object = mapper.readValue(json_string, RequestObject.class);
            origin_lat = request_object.getS().getLat();
            origin_lon = request_object.getS().getLon();
            destination_lat = request_object.getT().getLat();
            destination_lon = request_object.getT().getLon();  
        } catch (IOException ex) {
        }
        System.out.println("Request object=" + request_object);
        
        Client client = Client.create();
        WebResource webResource = client.resource(buildDirectionQueryString(origin_lat, origin_lon, destination_lat, destination_lon));
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed: HTTP error code: " + response.getStatus());
        }
        String responseString = response.getEntity(String.class);
        return responseString;
    } 
    
    // Dijkstra shortest path run on the TCP server via server socket
    @GET
    @Path("/dijkstra")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String directionDijkstraShortestPath(
            @QueryParam("originLat") double origin_lat,
            @QueryParam("originLon") double origin_lon,
            @QueryParam("destinationLat") double destination_lat,
            @QueryParam("destinationLon") double destination_lon) {
        
        Socket socket = null; 
        try {
            // Create a stream socket and connects it to the specified port number at the specified IP address.
            socket = new Socket("localhost", 9595);

        DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
        
        oos.writeDouble(origin_lat);
        oos.writeDouble(origin_lon);
        oos.writeDouble(destination_lat);
        oos.writeDouble(destination_lon);
        oos.flush();
        
        DataInputStream ois = new DataInputStream(socket.getInputStream());
        String response = (String) ois.readUTF();
        return response;

        } catch (UnknownHostException e) {
            System.out.println("Unknown Host...");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOProblems...");
            e.printStackTrace();
        } finally {
            if (socket != null)
                try {
                    socket.close();
                    System.out.println("Socket closed...");
                } catch (IOException e) {
                    System.out.println("Socket nicht zu schliessen...");
                    e.printStackTrace();
                }
        }
        return null; 
    }
    
    // returns a query string for a Goggle Directions API call
    public static String buildDirectionQueryString(double origin_lat, double origin_lon, double destination_lat, double destination_lon) {
        String origin = "origin=" + origin_lat + "," + origin_lon;
        String destination = "destination=" + destination_lat + "," + destination_lon;
	return GOOGLE_DIRECTIONS + origin + "&" + destination + "&" + GOOGLE_KEY;
    }
        
}
