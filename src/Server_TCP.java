import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server_TCP {
    private ServerSocket ser_socket;
    private Socket socket;
    private String name = "server", clientName = "client";
    private BufferedReader keyboard;
    private Thread sender;
    private InetAddress ipAddress;
    
    public Server_TCP(int port) throws IOException {
    	ser_socket = new ServerSocket(port);
	}

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome!");
        new Server_TCP(Integer.parseInt(args[0])).run();
    }

    public void run() {
    	boolean exit;
        try {
        	while(true){
        		socket = ser_socket.accept();
        		exit = true;
        	}
        	
            while (true) {
            	
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