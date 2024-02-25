package be.pxl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ImageServer {

    public  void start(int portNumber) {

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server is listening on port " + portNumber);
            //serverSocket.setSoTimeout(20000);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                try {
                    // Read image from client
                    InputStream is = socket.getInputStream();


                    // Read the image from ByteArrayInputStream
                    System.out.println("Start reading image...");
                    BufferedImage receivedImage = Util.receiveImage(is);
                    System.out.println("Received image: " + receivedImage.getWidth());
                   // is.close();

                    // Convert image to grayscale (insert your image processing code here)
                    BufferedImage grayscaleImage = Util.convertToGrayscale(receivedImage);
                    if (grayscaleImage != null) {

                        // Send the processed image back to the client
                        System.out.println("Image converted");
                        OutputStream output = socket.getOutputStream();
                        Util.sendImage(output, grayscaleImage, "jpg");

                        System.out.println("Image processed and sent back to client");
                    }
                } catch (IOException e) {
                    System.out.println("Exception when receiving/sending image: " + e.getMessage());
                } finally {
                    socket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
        }
    }
}
