/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sysdev.graph;

/**
 *
 * @author dell
 */
public class Edge {

    // All the data are read in from the json, so no edit is possible
    private final Node start;
    private final Node end;
    private final double maxspeed;
    private final int weight;
    private final double duration;

    public Edge(Node start, Node end, double maxspeed) {
        this.start = start;
        this.end = end;
        this.weight = computeWeight(start, end);
        this.maxspeed = maxspeed;
        this.duration = weight / maxspeed * 60 / 1000;// in minutes
    }
    
    // Calculate the weight of the edge as a distance between two points
    public static int computeWeight(Node start, Node end) {
        final double EARTHRADIUS = 6371.0;
        double sinLat = Math.sin(Math.toRadians(end.getLat() - start.getLat())/2);
        double sinLon = Math.sin(Math.toRadians(end.getLon() - start.getLon())/2);
        double a = sinLat*sinLat + Math.cos(Math.toRadians(start.getLat()))
                * Math.cos(Math.toRadians(end.getLat())) * sinLon * sinLon;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTHRADIUS * c * 1000;
        return (int) Math.round(distance);
    }
	
    public Node getStart() {
        return start;
    }
    
    public Node getEnd() {
        return end;
    }
        
    public double getMaxspeed() {
	return maxspeed;
    }        
        
    public int getWeight() {
        return weight;
    }
	
    public double getDuration() {
        return duration;
    }
        
}