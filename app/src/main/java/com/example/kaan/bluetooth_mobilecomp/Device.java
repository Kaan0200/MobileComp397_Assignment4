package com.example.kaan.bluetooth_mobilecomp;

/**
 * Created by Kaan on 11/9/2015.
 */
public class Device {
    public String name;
    public String address;
    public String bondstate;
    public String btclass;
    public String deviceclass;

    public Device(String name, String address, String bondstate,
                  String btclass, String deviceClass){
        this.name = name;
        this.address = address;
        this.bondstate = bondstate;
        this.btclass = btclass;
        this.deviceclass = deviceClass;
    }

    public Device(String name, String address){
        this.name = name;
        this.address = address;
    }
}
