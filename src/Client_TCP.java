import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client_TCP {
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private String name, serverName;
	private BufferedReader keyboard;
	private Thread listener;

	public Client_TCP(String adr, int port) throws IOException {
		InetAddress ipAddress = InetAddress.getByName("127.0.0.1");
		System.out.println("Wel!");
		socket = new Socket(InetAddress.getByName("127.0.0.1"), 17000);
		System.out.println("Welcome!");
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		keyboard = new BufferedReader(new InputStreamReader(System.in));
		listener = new Thread(new FromServer());
		listener.start();
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Welcome!");
		new Client_TCP("localhost", 17000).run();
	}

	public void run() {
		try {
			while (true) {
				String line;
				line = keyboard.readLine();
				if (socket.isClosed())
					break;
				out.writeUTF(line);
				if (line.equals("@quit")) {
					socket.close();
					break;
				}
				if (line.equals("close")) {
					socket.close();
					break;
				}
				if (line.contains("@name"))
					name = line.substring("@name".length() + 1);
			}
		} catch (Exception x) {
			x.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class FromServer implements Runnable {

		public void run() {
			try {
				while (true) {
					String line;
					line = in.readUTF();
					if (line.equals("@quit") || line.equals("@close")) {
						System.out.println("server and client is quited");
						socket.close();
						break;
					}
					if (line.contains("@name"))
						serverName = line.substring("@name".length() + 1);
					else
						System.out.println(serverName + ": " + line);
				}
			} catch (IOException e) {
				try {
					socket.close();
				} catch (IOException s) {
					s.printStackTrace();
				}
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}