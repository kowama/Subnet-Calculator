package com.nimina.kowama.calculatornetadmin;

public class IpNetwork {
    private final int IPv4_SIZE=32;
    private int[] mNetworkAddress = new int[4];
    private int mNetworkMask;

    public IpNetwork(int A, int B, int C, int D, int mask) throws IllegalArgumentException {
        if ((A < 0) || (A > 255) || (B < 0) || (B > 255) || (C < 0) || (C > 255) || (D < 0) || (D > 255) || (mask < 0) || (mask > 32)) {
            throw new IllegalArgumentException("ip address not valid !");
        } else {
            mNetworkAddress[0]=A;
            mNetworkAddress[1]=B;
            mNetworkAddress[2]=C;
            mNetworkAddress[3]=D;
            mNetworkMask = mask;

        }

    }
    public int availableHost(){
        return (int) (Math.pow(2,IPv4_SIZE - mNetworkMask) - 2);
    }

    public String networkAddressDec(){
        return "10.10.10.0";
    }
    public String subnetMaskDec(){
        return "255.255.255.0";
    }
    public String Hosts(){
        return "10.10.10.1 - 10.10.10.254";
    }
    public String broadcastAddressDec(){
        return "10.10.10.255";
    }
    //to recalculate
    public String networkAddressBin(){
        String netAddress="";
        for (int i=0; i<mNetworkAddress.length; i++){
            String ipByte = Integer.toBinaryString(mNetworkAddress[i]);
            while (ipByte.length() != 8){
                ipByte = "0"+ipByte;
            }
            netAddress +=ipByte+" ";
        }
        return netAddress;
    }
    public String subnetMaskBin(){
        String netMask="";
        for (int i=0; i<IPv4_SIZE; i++){
            if (i<mNetworkMask)   {
                netMask += "1";
            }else netMask += "0";
            if (i>0 && i%8 ==0)
                netMask += " ";
        }
        return netMask;

    }
    public String firstHostBin(){
        String netAddress =  networkAddressBin();
        return  netAddress.substring(0,netAddress.length()-1)+"1";
    }
    public String lastHostBin(){
        String broadcastAddress =  broadcastAddressBin();

        return broadcastAddress.substring(0,broadcastAddress.length()-1) + "0";

    }
    public String broadcastAddressBin(){
        String netAddress =  networkAddressBin().trim();
        String lastHostAddress = "";
        for (int i=0; i<IPv4_SIZE; i++){
            if (i<mNetworkMask){
                lastHostAddress += netAddress.charAt(i);

            }else {
                lastHostAddress += "1";
            }
            if (i >1 && i%8 == 0){
                lastHostAddress += " ";
            }
        }
        return lastHostAddress;
    }
}
