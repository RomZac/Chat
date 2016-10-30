import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client_TCP {
    private DatagramSocket socket;
    private String name = "client", serverName = "server";
    private BufferedReader keyboard;
    private Thread listener;
    private InetAddress ipAddress;
    private int port;

    public void Client(String adr, int port) throws SocketException, UnknownHostException {
        ipAddress = InetAddress.getByName(adr);
        this.port = port;
        socket = new DatagramSocket();
        keyboard = new BufferedReader(new InputStreamReader(System.in));
        listener = new Thread(new FromServer());
        listener.start();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome!");
        int port =17000; //Integer.parseInt(args[0]);
        new Client("127.0.0.1"/*args[1]*/, port).run();
    }

    public void run() {
        try {
            while (true) {
                String line;
                line = keyboard.readLine();
                if (socket.isClosed())
                    break;
                send(line);
                if (line.equals("@quit")) {
                    socket.close();
                    break;
                }
                if (line.contains("@name"))
                    name = line.substring("@name".length() + 1);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private class FromServer implements Runnable {

        public void run() {
            try {
                while (true) {
                    String line;
                    line = read(); 
                    if (line.equals("@quit")) {
                        System.out.println("server is quited");
                        break;
                    }
                    if (line.contains("@name"))
                        serverName = line.substring("@name".length() + 1);
                    else
                        System.out.println(serverName + ": " + line);
                }
            } catch (IOException e) {
                socket.close();
            } finally {
                socket.close();
            }
        }
    }

}