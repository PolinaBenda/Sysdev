/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sysdev.graph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.LineString;
import org.geojson.LngLatAlt;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author dell
 */
public class GraphInstantiation {

    private HashMap<String, Node> nodes;
    private List<Edge> edges;
    private Graph graph;
    
    public GraphInstantiation() {
	this.setGraph(this.jsonImport());
    }
    
    // Import data from json file to fill the graph
    public Graph jsonImport() {
	nodes = new HashMap<String, Node>();
	edges = new ArrayList<Edge>();
	try {
            // Get json file
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("schleswig-holstein.json").getFile());
            
            // Extract list of features
            FeatureCollection featureCollection = new ObjectMapper().readValue(file, FeatureCollection.class);
            List<Feature> features = featureCollection.getFeatures();
			
            // Loop through each feature to add nodes, edges and properties to the graph
            for (Feature feature : features) {
		GeoJsonObject obj = feature.getGeometry();
		if (obj instanceof LineString) {
                    List<LngLatAlt> coordinates_list = ((LineString) obj).getCoordinates();
                    int max_speed = feature.getProperty("maxspeed");
                    
                    Node previous_node = null;
                    for (LngLatAlt coordinates : coordinates_list) {
                        Node next_node = new Node(coordinates.getLatitude(), coordinates.getLongitude());
                        String key = next_node.getPointId();
                        if (!nodes.containsKey(key)) {
                            nodes.put(key, next_node);
                        }
                        if (previous_node != null) {
                            edges.add(new Edge(previous_node, next_node, max_speed));
                        }
                        previous_node = next_node;
                    }
		}
            }
	} catch (JsonParseException e1) {
            e1.printStackTrace();
	} catch (JsonMappingException e1) {
            e1.printStackTrace();
	} catch (IOException e1) {
            e1.printStackTrace();
	}
	Graph graph_from_json = new Graph(nodes, edges);
	return graph_from_json;
    }

    public Graph getGraph() {
	return graph;
    }

    public void setGraph(Graph graph) {
	this.graph = graph;
    }

}