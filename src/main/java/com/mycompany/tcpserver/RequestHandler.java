/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tcpserver;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.mycompany.sysdev.graph.DijkstraAlgorithm;
import com.mycompany.sysdev.graph.Graph;
import com.mycompany.sysdev.graph.Node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Geometry;
import org.geojson.LineString;
import org.geojson.LngLatAlt;

/**
 *
 * @author schubert
 */
public class RequestHandler implements Runnable{
    //client socket to read the request 
    private Socket socket;
    //Server data being handed down from the listener
    private Graph graph;
    //read for running the RequestHandler
    private Thread thread;
    //Name of the thread
    private String threadName;
    //number for generating new ThreadIDs
    private static long threadID = 0;
    
    public RequestHandler(Socket socket,Graph serverData){
       this.socket = socket;
       this.graph = serverData;
    }
    
    /***
     * Read the request from the socket and output the result as a GeoJson
     */
    public void run(){
        InputStream in = null;
        DataOutputStream out = null;
        try {
            // Returns an input stream for this socket.
            in = socket.getInputStream();
            DataInputStream ois = new DataInputStream(in);
            
            double originLat = ois.readDouble();
            double originLon = ois.readDouble();
            double destinationLat = ois.readDouble();
            double destinationLon = ois.readDouble();
            
            String output = "";
            DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
            Node nearest_origin = graph.findNearestNode(new Node(originLat, originLon));
            Node nearest_destination = graph.findNearestNode(new Node(destinationLat, destinationLon));
            dijkstra.executeDijkstraAlgorithm(nearest_origin, nearest_destination);
            LinkedList<Node> path = dijkstra.getShortestPath(nearest_destination);
            
            FeatureCollection featureCollection = new FeatureCollection();
            Feature feature = new Feature();
            List<LngLatAlt> path_coordinates = new ArrayList<LngLatAlt>();
            for (Node node : path) {
                LngLatAlt lngLatAlt = new LngLatAlt(node.getLon(), node.getLat());
                path_coordinates.add(lngLatAlt);
            }
            Geometry<LngLatAlt> opt_linestring = new LineString();
            opt_linestring.setCoordinates(path_coordinates);
            feature.setGeometry(opt_linestring);
            
            Map<String, Object> costs = new HashMap<String, Object>();        
            costs.put("Distance", (int) dijkstra.getPath_distance());
            costs.put("Travel_Time", (int) (dijkstra.getPath_duration()));
            
            feature.setProperty("costs", costs);
            featureCollection.add(feature);
            
            output = new ObjectMapper().writeValueAsString(featureCollection);
            output = "{\"data\": " + output + "}";
            
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(output);
            out.flush();
            out.close();
        } catch (IOException e) {
            System.err.println("IOException occurred while processing request.");
            e.printStackTrace();
        }
    }
    
    /***
     * Start a new runnable
     */
    public void start(){
        threadID++;
        String threadName = "RequestHandler"+(threadID-1);
        thread = new Thread(this,threadName);
        thread.start();
    }

}
