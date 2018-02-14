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
public class RequestObject {
    
    private Marker s;
    private Marker t;
    private String _class;
    
    public RequestObject(){ 
    }
    
    public RequestObject(Marker s, Marker t) {
        this.s = s;
        this.t = t;
    }
    
    public Marker getS() {
        return s;
    }

    public void setS(Marker s) {
        this.s = s;
    }

    public Marker getT() {
        return t;
    }

    public void setT(Marker t) {
        this.t = t;
    }
}
