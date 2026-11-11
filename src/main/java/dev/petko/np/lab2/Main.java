package dev.petko.np.lab2;

import java.net.*;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java Lab2 <ip-address>");
            return;
        }
 
        var IP = args[0];
        var website = ".sbl.spamhaus.org"; // www.spamhaus.org/sbl/
 
        String[] parts = IP.split("\\.");
        if (parts.length != 4) {
            System.out.printf("%s is not a valid IPv4 address\n", IP);
            return;
        }

        var reversed = new StringBuilder();
        for (int i = parts.length - 1; i >= 0; i--) {
            reversed.append(parts[i]);
            if (i > 0) reversed.append(".");
        }
 
        var query = reversed + website;
        try {
            InetAddress.getByName(query);
            System.out.println(IP + " is spam");
        } catch (Exception e) {
            System.out.println(IP + " appears legitimate");
        }
    } 
};
