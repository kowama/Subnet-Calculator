package com.nimina.kowama.calculatornetadmin.algorithms;

public class IpNetwork {
    private int[] mNetworkAddress = new int[4];
    private int[] mNetworkMask;
    private int mNetworkMaskCIDR;

    public IpNetwork(int A, int B, int C, int D, int mask) throws IllegalArgumentException {
        if ((A < 0) || (A > 255) || (B < 0) || (B > 255) || (C < 0) || (C > 255) || (D < 0) || (D > 255) || (mask < 1) || (mask > 30)) {
            throw new IllegalArgumentException("ip address not valid !");
        } else {

        }

    }
    public int availableHost(){
        return (int) (Math.pow(2,32 - mNetworkMaskCIDR) - 2);
    }
    public String networkAddressDec(){

        return "10.10.10.0";
    }
    public String subnetMaskDec(){
        return "";
    }
    public String Hosts(){
        return "";
    }
    public String broadcastAddressDec(){
        return "10.10.10.255";
    }
    public String networkAddressBin(){
        return "";

    }
    public String subnetMaskBin(){
        return "";
    }
    public String firstHostBin(){
        return "";
    }
    public String lastHostBin(){
        return "";
    }
    public String broadcastAddressBin() {
        return "";
    }
}
