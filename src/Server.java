import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {
    private DatagramSocket socket;
    private String name = "server", clientName = "client";
    private BufferedReader keyboard;
    private Thread sender;
    private InetAddress ipAddress;
    private int port;

    public Server(int port) throws IOException {
        socket = new DatagramSocket(port);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome!");
        int port = 17000/*Integer.parseInt(args[0])*/;
        new Server(port).run();
    }

    private String read() throws IOException {
        byte[] buf = new byte[1000];
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        socket.receive(p);
        ipAddress = p.getAddress();
        port = p.getPort();
        return new String(p.getData(), 0, p.getLength());
    }

    public void run() {
        try {
            sender = new Thread(new Sender());
            sender.start();
            while (true) {
                String line;
                line = read(); 
                if (line.equals("@quit")) {
                    System.out.println("client is quited");
                    break;
                }
                if (line.contains("@name"))
                    clientName = line.substring("@name".length() + 1);
                else {
                    send(line);
                    System.out.println(clientName + ": " + line);
                }
            }
        } catch (IOException e) {
            socket.close();
        } finally {
            socket.close();
        }
    }

    private void send(String s) throws IOException {
        byte[] m = s.getBytes();
        DatagramPacket p = new DatagramPacket(m, m.length, ipAddress, port);
        socket.send(p);
    }

    private class Sender implements Runnable {

        Sender() {
            keyboard = new BufferedReader(new InputStreamReader(System.in));
        }

        public void run() {
            try {
                while (!socket.isClosed()) {
                    String line;
                    line = keyboard.readLine();
                    if (line != null && !socket.isClosed()) {
                        send(line);
                        if (line.equals("@quit")) {
                            socket.close();
                            break;
                        }
                        if (line.contains("@name"))
                            name = line.substring("@name".length() + 1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}