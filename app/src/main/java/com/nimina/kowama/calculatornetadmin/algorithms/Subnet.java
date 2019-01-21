package com.nimina.kowama.calculatornetadmin.algorithms;
/*The Subnet class*/
/**
 *
 * @author Kowama
 */


public class Subnet {
    public String name;
    public int neededSize;
    public int allocatedSize;
    public String address;
    public String mask;
    public String decMask;
    public String range;
    public String broadcast;

    public Subnet(){

    }
    public Subnet(String address){

    }
    public Subnet(String address, String name){
        this.address = address;
        this.name = name;
    }

}
