package be.pxl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Util {

	public static void sendImage(OutputStream outputStream, BufferedImage bufferedImage, String type) {
		try {
			DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, type, baos);
		 System.out.println("Write byte stream");
		 dataOutputStream.writeLong(baos.size());
			System.out.println(baos.size());
		outputStream.write(baos.toByteArray());
		System.out.println("Image sent");
		outputStream.flush();
		//outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}



	public static BufferedImage receiveImage(InputStream inputStream) {
		try {
			DataInputStream dis = new DataInputStream(inputStream);

			// Read the size of the image data
			long size = dis.readLong(); // Make sure this matches how you're sending it from the client
			System.out.println("Read size: " + size);
//			// Now read the image bytes
//			byte[] imageBytes = new byte[size];
//			System.out.println("Receiving: " + size);
//			dis.readFully(imageBytes); // This ensures all bytes are read
//			System.out.println("Received fully");
			// Convert bytes to a BufferedImage
			ByteArrayOutputStream image = new ByteArrayOutputStream();

			int nRead;
			int bytes = 0;
			byte[] buffer = new byte[4 * 1024];
			System.out.println("reading data");
			while (size > 0 && (bytes = dis.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
				image.write(buffer,0,bytes);
				size -= bytes;      // read upto file size
			}
			System.out.println("data read");
			image.flush();


			ByteArrayInputStream bais = new ByteArrayInputStream(image.toByteArray());
			BufferedImage myimage = ImageIO.read(bais);

			// Process the image (e.g., save it)
			File outputFile = new File("receivedImage.jpg");
			ImageIO.write(myimage, "jpg", outputFile);

			System.out.println("Image received and saved.");
			return myimage;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static BufferedImage convertToGrayscale(BufferedImage originalImage) {
		if (originalImage == null) {
			return null;
		}
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		// Create a new BufferedImage to hold the grayscale version
		BufferedImage grayscaleImage = new BufferedImage(width, height, originalImage.getType());

		// Convert each pixel to grayscale
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = originalImage.getRGB(i, j);
				int alpha = (rgb >> 24) & 0xff;
				int red = (rgb >> 16) & 0xff;
				int green = (rgb >> 8) & 0xff;
				int blue = (rgb) & 0xff;

				// Calculate the average of RGB values (simple grayscale conversion)
				int average = (red + green + blue) / 3;

				// Replace RGB value with average
				int newPixel = (alpha << 24) | (average << 16) | (average << 8) | average;
				grayscaleImage.setRGB(i, j, newPixel);
			}
		}

		return grayscaleImage;
	}

}
