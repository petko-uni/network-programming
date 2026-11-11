package dev.petko.np.lab3;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {
  static final int PORT = 5000;

  public static void main(String[] args) throws IOException {
    if (args.length < 1) {
      usage();
      return;
    }

    switch (args[0].toLowerCase()) {
    case "server":
      runServer();
      break;
    case "client":
      runClient(args.length > 1 ? args[1] : "localhost");
      break;
    default:
      usage();
    }
  }

  static void usage() {
    System.out.println("Usage:");
    System.out.println("  java Lab3 server");
    System.out.println("  java Lab3 client [host]");
  }

  // ---------------------------- SERVER ----------------------------
  static void runServer() throws IOException {
    try (var ss = new ServerSocket(PORT)) {
      System.out.println("Server waiting on port " + PORT + "...");
      try (var s = ss.accept();
           var in = new DataInputStream(s.getInputStream());
           var out = new DataOutputStream(s.getOutputStream())) {

        System.out.println("Client connected.");
        while (true) {
          int choice = in.readInt();
          System.out.println("Received choice: " + choice);

          if (choice == 0) {
            out.writeUTF("0");
            out.flush();
            System.out.println("Sent 0. Server shutting down.");
            break;
          } else if (choice == 1) {
            out.writeUTF("Send two numbers to add:");
            out.flush();
            double a = in.readDouble(), b = in.readDouble();
            out.writeUTF("Result: " + (a + b));
          } else if (choice == 2) {
            out.writeUTF("Send one number for square root:");
            out.flush();
            double a = in.readDouble();
            out.writeUTF("Result: " + Math.sqrt(a));
          } else if (choice == 3) {
            out.writeUTF("Send base and exponent:");
            out.flush();
            double a = in.readDouble(), b = in.readDouble();
            out.writeUTF("Result: " + Math.pow(a, b));
          } else {
            out.writeUTF("Invalid choice. Use 1, 2, 3 or 0.");
          }
          out.flush();
        }
      }
    }
  }

  // ---------------------------- CLIENT ----------------------------
  static void runClient(String host) throws IOException {
    try (var s = new Socket(host, PORT);
         var in = new DataInputStream(s.getInputStream());
         var out = new DataOutputStream(s.getOutputStream());
         var sc = new Scanner(System.in)) {

      while (true) {
        System.out.print("Enter choice (1=Add, 2=Sqrt, 3=Power, 0=Exit): ");
        int choice = sc.nextInt();
        out.writeInt(choice);
        out.flush();

        String reply = in.readUTF();
        if (reply.equals("0")) {
          System.out.println("Server sent 0. Client terminating.");
          break;
        }
        System.out.println("Server: " + reply);

        if (choice == 1 || choice == 3) {
          System.out.print("Enter first number: ");
          out.writeDouble(sc.nextDouble());
          System.out.print("Enter second number: ");
          out.writeDouble(sc.nextDouble());
          out.flush();
          System.out.println(in.readUTF());
        } else if (choice == 2) {
          System.out.print("Enter number: ");
          out.writeDouble(sc.nextDouble());
          out.flush();
          System.out.println(in.readUTF());
        }
      }
    }
  }
}
