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
public class Node {
    
    private double lat;
    private double lon;
    private final String point_id;
	
    private final long cell_lat_index;
    private final long cell_lon_index;
    private final String cell_id;

    // Let the world map is divided into grid cells defined by 2 indexes
    // The size of one cell is about 100x100 m
    // Every point on the map belongs to one of the cells    

    public Node(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;        
        this.point_id = lat + "" + lon;
        this.cell_lat_index = cellLatIndex(lat);
        this.cell_lon_index = cellLonIndex(lon);
        this.cell_id = cellLatIndex(lat) + "" + cellLonIndex(lon);
    }
    
    public long cellLatIndex(double lat) {
        return Math.round(lat*1111);
    }

    public long cellLonIndex(double lon) {
        return Math.round(lon*1111);
    }
    
    public long getCellLatIndex() {
	return cell_lat_index;
    }
    
    public long getCellLonIndex() {
        return cell_lon_index;
    }
    
    public String getCellId() {
        return cell_id;
    }
    
    public String getPointId() {
        return point_id;
    }
    
    public double getLat() {
	return lat;
    }
    
    public void setLat(double lat) {
        this.lat = lat;
    }
    
    public double getLon() {
        return lon;
    }
    
    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        if (point_id == null) {
            if (other.point_id != null)
                return false;
        } else if (!point_id.equals(other.point_id))
            return false;
        return true;
    }
    
}
