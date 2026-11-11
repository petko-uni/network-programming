package dev.petko.np.lab4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
  private static final int DEFAULT_PORT = 5000;
  private static final String DEFAULT_HOST = "localhost";

  public static void main(String[] args) throws IOException {
    if (args.length < 1) {
      usage();
      return;
    }
    switch (args[0].toLowerCase()) {
    case "server":
      runServer(args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PORT);
      break;
    case "client":
      runClient(args.length > 1 ? args[1] : DEFAULT_HOST,
                args.length > 2 ? Integer.parseInt(args[2]) : DEFAULT_PORT);
      break;
    default:
      usage();
    }
  }

  private static void usage() {
    System.out.println("Usage:");
    System.out.println("  java dev.petko.np.lab4.Main server [port]");
    System.out.println("  java dev.petko.np.lab4.Main client [host] [port]");
  }

  private static void runServer(int port) throws IOException {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("MathServer listening on port " + port);
      while (true) {
        Socket client = serverSocket.accept();
        System.out.println("Client connected: " +
                           client.getRemoteSocketAddress());
        new ClientHandler(client).start(); // one thread per client
      }
    }
  }

  private static class ClientHandler extends Thread {
    private final Socket socket;

    ClientHandler(Socket socket) { this.socket = socket; }

    @Override
    public void run() {
      try (var in = new BufferedReader(
               new InputStreamReader(socket.getInputStream()));
           var out = new PrintWriter(socket.getOutputStream(), true)) {

        String line;
        while ((line = in.readLine()) != null) {
          line = line.trim();
          if (line.equalsIgnoreCase("quit")) {
            break;
          }
          System.out.println("[" + getName() + "] Request: " + line);
          out.println(compute(line));
        }
      } catch (IOException e) {
        System.out.println("Connection error: " + e.getMessage());
      } finally {
        try {
          socket.close();
        } catch (IOException ignored) {
        }
        System.out.println("Client disconnected: " +
                           socket.getRemoteSocketAddress());
      }
    }

    private String compute(String request) {
      String[] parts = request.split("\\s+");
      if (parts.length != 2) {
        return "ERROR: use format <sin|cos|sqrt> <number>";
      }

      try {
        double x = Double.parseDouble(parts[1]);
        switch (parts[0].toLowerCase()) {
        case "sin":
          return String.valueOf(Math.sin(x));
        case "cos":
          return String.valueOf(Math.cos(x));
        case "sqrt":
          if (x < 0) {
            return "ERROR: sqrt of negative number";
          }
          return String.valueOf(Math.sqrt(x));
        default:
          return "ERROR: unknown operation '" + parts[0] + "'";
        }
      } catch (NumberFormatException e) {
        return "ERROR: invalid number '" + parts[1] + "'";
      }
    }
  }

  private static void runClient(String host, int port) throws IOException {
    try (var socket = new Socket(host, port);
         var in =
             new BufferedReader(new InputStreamReader(socket.getInputStream()));
         var out = new PrintWriter(socket.getOutputStream(), true);
         var keyboard = new Scanner(System.in)) {

      System.out.println("Connected to MathServer at " + host + ":" + port);
      while (true) {
        System.out.print(
            "Enter request (e.g. sqrt 4.0, sin 10, cos 10, quit): ");
        if (!keyboard.hasNextLine()) {
          break;
        }
        String request = keyboard.nextLine().trim();
        out.println(request);
        if (request.equalsIgnoreCase("quit")) {
          break;
        }
        System.out.println("Server reply: " + in.readLine());
      }
    }
  }
}
