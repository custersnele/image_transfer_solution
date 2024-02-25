package be.pxl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ImageClient {


	public void start(String serverAddress, int serverPort) {
		try (Socket socket = new Socket(serverAddress, serverPort)) {
			//  socket.setSoTimeout(20000);
			File selectedFile = selectFile();
			OutputStream outputStream = socket.getOutputStream();
			BufferedImage image = ImageIO.read(selectedFile);

			Util.sendImage(outputStream, image, "jpg");

			// Receiving the processed image from the server
			InputStream is = socket.getInputStream();


			BufferedImage convertedImage = Util.receiveImage(is);
			displayImage(convertedImage);
		} catch (IOException e) {
			System.out.println("error occured");
		}

	}

	public File selectFile() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		} else {
			return null;
		}
	}

	public void displayImage(BufferedImage image) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Received Image");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			ImageIcon imageIcon = new ImageIcon(image);
			JLabel label = new JLabel(imageIcon);
			frame.getContentPane().add(label, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
		});
	}
}
