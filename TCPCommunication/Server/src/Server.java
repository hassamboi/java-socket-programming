import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private BufferedReader bufferedReader = null;
    private BufferedWriter bufferedWriter = null;

    public void initServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            while(true) {
                socket = serverSocket.accept(); // Accept client connection
                System.out.println("Client Connected");
                // Setup communication
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.startCommunication(); // Function to communicate with the client
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startCommunication() {
        try {
        // Keep communicating with the client until the client disconnects
            while(true) {
                String msgFromClient = bufferedReader.readLine();

                System.out.println("Client: " + msgFromClient);
                bufferedWriter.write("Message Received.");
                bufferedWriter.newLine();
                bufferedWriter.flush();

                if(msgFromClient.equals(".")) {
                    this.closeClientConnection(); // Close the client connection after communication is done
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeClientConnection() {
        try {
            if(socket != null)
                socket.close();
            if(bufferedWriter != null)
                bufferedWriter.close();
            if(bufferedReader != null)
                bufferedReader.close();
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

    public static void main(String[] args) {
        int port = 4999;

        Server server = new Server();
        server.initServer(port);
    }
}
