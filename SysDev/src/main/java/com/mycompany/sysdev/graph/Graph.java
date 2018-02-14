/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sysdev.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author dell
 */
public class Graph {

    private final HashMap<String, Node> nodes;
    private final List<Edge> edges;
    private HashMap<String, ArrayList<Node>> gridcells;

    public Graph(HashMap<String, Node> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
        // Distribute nodes into grid cells
        HashMap<String, ArrayList<Node>> gridcells = new HashMap<String, ArrayList<Node>>();
        for (Node node : nodes.values()) {
            ArrayList<Node> in_gridcell = gridcells.get(node.getCellId());
            if (in_gridcell == null) {
        	in_gridcell = new ArrayList<Node>();
            }
            in_gridcell.add(node);
            gridcells.put(node.getCellId(), in_gridcell);
        }
        this.gridcells = gridcells;
    }
    
    // Find the closest node of the graph for an arbitrary point on the map
    public Node findNearestNode(Node map_point) {
    	List<Node> close_nodes = new ArrayList<Node>();
    	// Run over neighborhood grid cells to find a bunch of closest nodes
        // of the graph to choose the nearest_node one among them
    	for (long i = -1; i < 2; i++) {
            for (long j = -1; j < 2; j++) {
                String neighborhood_cell_id = (map_point.getCellLatIndex() + i)
                        + "" + (map_point.getCellLonIndex() + j);
                try {
                    for (Node node : this.gridcells.get(neighborhood_cell_id)) {
            		if(node != null) {
                            close_nodes.add(node);
            		}
                    }
		} catch (Exception e) {
                    System.out.println("No nodes in the neighborhood cell");
                }
            }
    	}
    	System.out.println("Search in the neighborhood cells is over");
    	
    	// If there are no nodes of the graph in the neighborhood cells
        // do long search over all nodes
    	if (close_nodes.isEmpty()) {
            System.out.println("No close nodes!");
            for (Node node : this.nodes.values()) {
                if(node != null) {
                    close_nodes.add(node);
                }
            }
    	}
    	
        // Do linear search for the chosen closest nodes of the graph
    	int shortest_distance = Integer.MAX_VALUE;
    	Node nearest_node = null;
    	for (Node node : close_nodes) {
            Edge helper_edge = new Edge(map_point, node, 10);
            if (helper_edge.getWeight() < shortest_distance) {
        	nearest_node = node;
        	shortest_distance = helper_edge.getWeight();
            }
        }
	return nearest_node;
    }

    public HashMap<String, Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

}