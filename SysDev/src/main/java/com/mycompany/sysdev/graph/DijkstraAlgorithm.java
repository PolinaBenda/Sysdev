/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sysdev.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 *
 * @author dell
 */
public class DijkstraAlgorithm {

    private final List<Node> nodes;
    private final List<Edge> edges;
    private Set<Node> settledNodes; // nodes visited
    private Set<Node> unSettledNodes; // nodes to visit
    private Map<Node, Node> predecessors; // save steps of the path
    private Map<Node, Integer> distance; // shortest distance from one node to each other nodes of the graph
    private int path_distance;
    private double path_duration;

    public DijkstraAlgorithm(Graph graph) {
        this.nodes = new ArrayList<Node>(graph.getNodes().values());
        this.edges = new ArrayList<Edge>(graph.getEdges());
        // Details for the info panel
        this.path_distance = 0; 
        this.path_duration = 0;
        //List<Node> nlist = new ArrayList<Node>(graph.getNodes().values());
        //this.nodes = nlist;
    }

    public void executeDijkstraAlgorithm(Node origin, Node destination) {
        settledNodes = new HashSet<Node>();
        unSettledNodes = new HashSet<Node>();
        distance = new HashMap<Node, Integer>();
        predecessors = new HashMap<Node, Node>();
        
        // Assign zero distance value for the initial node
        distance.put(origin, 0);
        // Set the initial node as current to visit
        unSettledNodes.add(origin);

        while (!isSettled(destination)) {
            // Pick the unvisited neighbor node with the minimal distance and visit it
            Node current_node = getMinimalCurrentDistance(unSettledNodes);
            settledNodes.add(current_node);
            unSettledNodes.remove(current_node);
            updateCurrentDistances(current_node);
        }
    }
    
    // Whether the node has been visited
    private boolean isSettled(Node node) {
        return settledNodes.contains(node);
    }

    // Pick the unvisited node with the minimal distance to visit it
    private Node getMinimalCurrentDistance(Set<Node> unvisited_nodes) {
    	Node minimum = null;
        for (Node node : unvisited_nodes) {
            if (minimum == null) {
                minimum = node;
            } else {
                if (getCurrentDistance(node) < getCurrentDistance(minimum)) {
                    minimum = node;
                }
            }
        }
        return minimum;
    }
    
    //Return current distance assigned to a node in the graph
    private int getCurrentDistance(Node Node) {
        Integer d = distance.get(Node);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }
    
    // Calculate tentative distances of unvisited neighbors through the current node
    private void updateCurrentDistances(Node current_node) {
        List<Node> unvisited_neighbors = getNeighbors(current_node);
        for (Node neighbor : unvisited_neighbors) {
            if (getCurrentDistance(neighbor) > getCurrentDistance(current_node)
                    + getNeighboringDistance(current_node, neighbor)) {
                distance.put(neighbor, getCurrentDistance(current_node)
                        + getNeighboringDistance(current_node, neighbor));
                predecessors.put(neighbor, current_node);
                unSettledNodes.add(neighbor);
            }
        }
    }
    
    // Find unvisited neighbors of the current node
    private List<Node> getNeighbors(Node current_node) {
        List<Node> unvisited_neighbors = new ArrayList<Node>();
        for (Edge edge : edges) {
            if (edge.getStart().equals(current_node)
                    && !isSettled(edge.getEnd())) {
                unvisited_neighbors.add(edge.getEnd());
            }
        }
        return unvisited_neighbors;
    }
    
    // Return distance between neighboring nodes connected with edge
    private int getNeighboringDistance(Node start, Node end) {
        for (Edge edge : edges) {
            if (edge.getStart().equals(start)
                    && edge.getEnd().equals(end)) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Try to find distance between one and the same node");
    }
    
    // Define the shortest path from the origin to the destination
    public LinkedList<Node> getShortestPath(Node destination) {
        LinkedList<Node> path = new LinkedList<Node>();
        Node step = destination;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            path_distance = path_distance + getNeighboringDistance(predecessors.get(step), step);           
            path_duration = path_duration + getNeighboringDuration(predecessors.get(step), step);
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }    
    
    // Return duration of the way between neighboring nodes connected with edge
    private double getNeighboringDuration(Node start, Node end) {
        for (Edge edge : edges) {
            if (edge.getStart().equals(start)
                    && edge.getEnd().equals(end)) {
                return edge.getDuration();
            }
        }
        throw new RuntimeException("Try to find duration between one and the same node");
    }

    public int getPath_distance() {
        return path_distance;
    }

    public double getPath_duration() {
        return path_duration;
    }

    public List<Node> getNodes() {
        return nodes;
    }

}