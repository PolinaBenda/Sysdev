/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sysdev;

/**
 *
 * @author dell
 */
public class Marker {
    
    private double lat;
    private double lon;
    
    public Marker() {
    }
            
    public Marker(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
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
}
