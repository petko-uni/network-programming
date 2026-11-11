package dev.petko.np.lab1;

import java.net.*;

public class Main {
    public static void main(String[] args) {
        // 1. Write a program to find the Internet address of your machine.
        try {
            var address = InetAddress.getLocalHost();

            System.out.println(address);
        } catch (UnknownHostException ex) {
            System.err.printf("Unknown host %s\n", args[0]);
        }

        // 2. Write a program to check the following protocols (FTP, HTTP, HTTPS, and SMTP) is supported by the google.ie web server.
        var host = "google.ie";
        String[] names = {"FTP", "HTTP", "HTTPS", "SMTP"};
        int[] ports = {21, 80, 443, 25};

        for (int i = 0; i < names.length; i++) {
            try (Socket s = new Socket()) {
                s.connect(new InetSocketAddress(host, ports[i]), 3000);
                System.out.println(names[i] + " (port " + ports[i] + "): supported");
            } catch (Exception e) {
                System.out.println(names[i] + " (port " + ports[i] + "): not supported");
            }
        }
    }   
};
