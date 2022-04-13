import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private DataInputStream dataInputStream = null;
    private DataOutputStream dataOutputStream = null;

    public void initServer(int port) throws Exception {
        try {
            serverSocket = new ServerSocket(port);
            String fileName = "receivedFile.pdf";
            while(true) {
                socket = serverSocket.accept(); // Accept client connection
                System.out.println("Client Connected");
                // Setup communication
                dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                this.receiveFile(fileName); // Function to receive the file
                System.out.println("File received by Client " + socket.getInetAddress().toString());

                dataOutputStream.writeUTF("File Received");
                dataOutputStream.flush();
                this.closeClientConnection(); // Close the connection to the client
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveFile(String fileName) throws Exception{
        int bytes;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        long size = dataInputStream.readLong(); // read file size
        byte[] buffer = new byte[4*1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes; // read up to file size
        }
        fileOutputStream.close();
    }

    public void closeClientConnection() {
        try {
            if(socket != null)
                socket.close();
            if(dataOutputStream != null)
                dataOutputStream.close();
            if(dataInputStream != null)
                dataInputStream.close();
            System.out.println("Client Disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void terminateServer() {
        try {
            if(serverSocket != null)
                serverSocket.close();
            System.out.println("Server terminated");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 4999;

        Server server = new Server();
        server.initServer(port);
    }
}