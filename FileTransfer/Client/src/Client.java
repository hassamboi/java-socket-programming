import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket = null;
    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;

    public void initConnection(String address, int port, String filePath) throws Exception {
        try {
            socket = new Socket(address, port); // Creating a socket for the client to connect to the server
            // Buffering the reading and writing for better practice
            dataInputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            System.out.println("Connected to Server");

            System.out.println("Sending file");
            this.sendFile(filePath); // send the file to the server
            String msgFromServer = dataInputStream.readUTF();

            System.out.println("Server: " + msgFromServer);
            this.closeConnection(); // close the connection to the server
        } catch (IOException e) {
            System.out.println("Server not found. Exiting...\n" + e.toString());
        }
    }

    public void sendFile(String path) throws Exception{
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        // send file size
        dataOutputStream.writeLong(file.length());
        // break file into chunks
        byte[] buffer = new byte[4*1024];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            dataOutputStream.write(buffer,0,bytes);
            dataOutputStream.flush();
        }
        fileInputStream.close();
    }

    public void closeConnection() {
        try {
            if(socket != null)
                socket.close();
            if(dataOutputStream != null)
                dataOutputStream.close();
            if(dataInputStream != null)
                dataInputStream.close();
            System.out.println("Disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        String address = "localhost";
        int port = 4999;
        String filePath = "D:/Java Projects/FileTransfer/Client/myFile.pdf";

        Client client = new Client();
        client.initConnection(address, port, filePath);
    }
}