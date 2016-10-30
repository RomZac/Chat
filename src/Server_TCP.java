import java.net.*;
import java.io.*;
import java.util.Scanner;
/* logic break and continue*/


public class Server_TCP {
	private ServerSocket ser_socket;
	private Socket socket;
	private String name, clientName;
	private BufferedReader keyboard;
	private Thread sender;
	private DataInputStream in;
	private DataOutputStream out;

	public Server_TCP(int port) throws IOException {
		ser_socket = new ServerSocket(port);
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Welcome!");
		new Server_TCP(17000).run();
	}

	public void run() throws IOException {
		try {
			while (true) {
				socket = ser_socket.accept();
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				sender = new Thread(new Sender());
				sender.start();
				while (true) {
					String line = in.readUTF();
					if (line.equals("@quit")) {
						System.out.println("client is quited");
						socket.close();
						break;
					}
					if (line.equals("@close")) {
						System.out.println("server closed");
						close();
						break;
					}
					if (line.contains("@name"))
						clientName = line.substring("@name".length() + 1);
					else {
						System.out.println(clientName + ": " + line);
					}
				}
			}
		} catch (SocketException e) {
			close();
		} finally {
			close();
		}
	}

	private void close() {
		try {
			ser_socket.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
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
						out.writeUTF(line);
						out.flush();
						if (line.equals("@quit")) {
							socket.close();
							break;
						}
						if (line.equals("@close")) {
							System.out.println("server closed");
							close();
							break;
						}
						if (line.contains("@name"))
							name = line.substring("@name".length() + 1);

					}
				}
			} catch (Exception e) {
				if ("Socket closed".equals(e.getMessage())) {
					close();
				}
				e.printStackTrace();
				close();
			}
		}
	}
}