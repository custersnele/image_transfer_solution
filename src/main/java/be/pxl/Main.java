package be.pxl;

public class Main {

	public static void main(String[] args) {
		if (args[0] == null) {
			System.out.println("Run this program with the server or client option.");
		}
		switch (args[0]) {
			case "server":
				int portNumber = Integer.parseInt(args[1]);
				new ImageServer().start(portNumber);
				break;
			case "client":
				String serverAddress = args[1];
				portNumber = Integer.parseInt(args[2]);
				new ImageClient().start(serverAddress, portNumber);
				break;
		}
	}

}
