package com.nimina.kowama.calculatornetadmin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kowama
 */


public class NetworkManager {
    public static  class Subnet {
        public String name;
        public int neededSize;
        public int allocatedSize;
        public String address;
        public String maskCIDR;
        public String decMask;
        public String firstUsableHost;
        public String lastUsableHost;
        public String range;
        public String broadcast;

        public Subnet(){

        }
        public Subnet(String netAddress) throws IllegalArgumentException{
            if(!checkIfValidNetworkAddress(netAddress)){
                throw new IllegalArgumentException("netAddress not valid");
            }
            this.name = "A";
            int currentIp = findFirstIp(netAddress);
            this.address = convertIpToQuartet(currentIp);
            this.maskCIDR = "/" + netAddress.split("/")[1];
            this.decMask = toDecMask(Integer.parseInt(netAddress.split("/")[1]));
            this.allocatedSize = findUsableHosts(Integer.parseInt(netAddress.split("/")[1]));
            this.broadcast = convertIpToQuartet(currentIp + allocatedSize + 1);
            this.firstUsableHost = convertIpToQuartet(currentIp + 1);
            this.lastUsableHost = convertIpToQuartet(currentIp + allocatedSize);
            this.range = firstUsableHost + " - " + lastUsableHost;


        }
    }
    /**
     * Check if a IP address is a valid network address.
     *
     * @param netAddress String CIDR notation
     *                   exemple : 192.168.100.0/24
     * @return  boolean
     */
    public static boolean checkIfValidNetworkAddress(String netAddress){
        String[] ipPart = netAddress.split("/");
        /**iPart[0]  =  A.B.C.D
         * ipPart[1] =  mask
         */
        if(ipPart.length !=2){
            return false;
        }
        String[] ipOctets = ipPart[0].split("\\.");
        if(ipOctets.length != 4){
            return false;
        }

        for (String octet : ipOctets){
            if (Integer.parseInt(octet)<0 || Integer.parseInt(octet)>255){
                return false;
            }
        }
        if(Integer.parseInt(ipPart[1]) < 0 || Integer.parseInt(ipPart[1]) > 32 ){
            return false;
        }

        return true;
    }


    /**
     * Calculate VLSM.
     *
     * @param majorNetwork Major network
     * @param subnets A map of required subnets
     * @return A list of output subnets
     */
    public static List<Subnet> calcVLSM(String majorNetwork, Map<String, Integer> subnets) {
        Map<String, Integer> sortedSubnets = sortMap(subnets);
        List<Subnet> output = new ArrayList<>();
        int currentIp = findFirstIp(majorNetwork);

        for (String key : sortedSubnets.keySet()) {  // for all subnets
            Subnet subnet = new Subnet();

            subnet.name = key;
            subnet.address = convertIpToQuartet(currentIp);

            int neededSize = sortedSubnets.get(key);
            subnet.neededSize = neededSize;

            int mask = calcMask(neededSize);
            subnet.maskCIDR = "/" + mask;
            subnet.decMask = toDecMask(mask);

            int allocatedSize = findUsableHosts(mask);
            subnet.allocatedSize = allocatedSize;
            subnet.broadcast = convertIpToQuartet(currentIp + allocatedSize + 1);

            String firstUsableHost = convertIpToQuartet(currentIp + 1);
            String lastUsableHost = convertIpToQuartet(currentIp + allocatedSize);
            subnet.range = firstUsableHost + " - " + lastUsableHost;

            output.add(subnet);

            currentIp += allocatedSize + 2;
        }

        return output;
    }

    /**
     * Sort map according to descending order of values.
     *
     * @param map Map
     * @return Sorted Map
     */
    public static Map<String, Integer> sortMap(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());  // descending
            }
        });

        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    /**
     * Merge IP address quartet into a single binary string.
     * <p/>
     * Example: <code>192.168.0.1</code> to <code>11000000101010000000000000000001</code>
     *
     * @param ipAddress IP address
     * @return A single integer that stores IP address binary string
     */
    public static int convertQuartetToBinaryString(String ipAddress) {
        String[] ip = ipAddress.split("\\.|/");

        int octet1 = Integer.parseInt(ip[0]);
        int octet2 = Integer.parseInt(ip[1]);
        int octet3 = Integer.parseInt(ip[2]);
        int octet4 = Integer.parseInt(ip[3]);

        int output = octet1;
        output = (output << 8) + octet2;
        output = (output << 8) + octet3;
        output = (output << 8) + octet4;

        return output;
    }

    /**
     * Separate IP address binary string into quartet.
     * <p/>
     * Example: <code>11000000101010000000000000000001</code> to <code>192.168.0.1</code>
     *
     * @param ipAddress IP address binary string
     * @return A string of IP address in the form of quartet
     */
    public static String convertIpToQuartet(int ipAddress) {
        int octet1 = (ipAddress >> 24) & 255;
        int octet2 = (ipAddress >> 16) & 255;
        int octet3 = (ipAddress >> 8) & 255;
        int octet4 = ipAddress & 255;

        return octet1 + "." + octet2 + "." + octet3 + "." + octet4;
    }

    /**
     * Find the first IP address for the specified network.
     *
     * @param majorNetwork Major network IP
     * @return The first IP address
     */
    public static int findFirstIp(String majorNetwork) {
        String[] ip = majorNetwork.split("/");
        int mask = Integer.parseInt(ip[1]); // parse CIDR mask
        int offset = Integer.SIZE - mask;

        int majorAddress = convertQuartetToBinaryString(majorNetwork);
        int firstIp = (majorAddress >> offset) << offset;

        return firstIp;
    }

    /**
     * Calculate mask and return it in CIDR notation.
     *
     * @param neededSize The size for subnet
     * @return Mask
     */
    public static int calcMask(int neededSize) {
        int highestBit = Integer.highestOneBit(neededSize);
        int position = (int) (Math.log(highestBit) / Math.log(2));
        return Integer.SIZE - (position + 1);   // +1 because position starts with 0
    }

    /**
     * Find the total number of usable IP address/hosts.
     *
     * @param mask Mask
     * @return Total number of hosts
     */
    public static int findUsableHosts(int mask) {
        return (int) Math.pow(2, Integer.SIZE - mask) - 2;
    }

    /**
     * Convert mask from CIDR notation to quartet form.
     * <p/>
     * Example: <code>'/24'</code> to <code>'255.255.255.0'</code>
     *
     * @param mask Mask in CIDR notation
     * @return Mask in quartet form
     */
    public static String toDecMask(int mask) {
        if (mask == 0) {
            return "0.0.0.0";
        }
        int allOne = -1;    // '255.255.255.255'
        int shifted = allOne << (Integer.SIZE - mask);

        return convertIpToQuartet(shifted);
    }


}