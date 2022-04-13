import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket = null;
    private BufferedReader bufferedReader = null;
    private BufferedWriter bufferedWriter = null;

    public void initConnection(String address, int port) {
        try {
            socket = new Socket(address, port); // Creating a socket for the client to connect to the server
            // Buffering the reading and writing for better practice
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Used to read incoming text
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // Used to write outgoing text
            System.out.println("Connected to Server");
            this.startCommunication();
        } catch (IOException e) {
            System.out.println("Server not found. Exiting...\n" + e.toString());
        }
    }

    public void startCommunication() {
        try {
            Scanner scanner = new Scanner(System.in);
            while(true) {
                String msgToSend = scanner.nextLine().trim(); // read the input from the client

                if(msgToSend.length() > 0) {
                    bufferedWriter.write(msgToSend); // write the input on the stream
                    bufferedWriter.newLine(); // indicate the line has ended
                    bufferedWriter.flush(); // flush the buffer to send the message
                    System.out.print("Server: " + bufferedReader.readLine() + "\n"); //wait for the server's reply, read and print it
                } else {
                    System.out.println("Invalid input");
                }

                if(msgToSend.equalsIgnoreCase(".")) {
                    this.closeConnection(); // type 'exit' to stop communication
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Lost connection to the Server. Exiting...\n" + e.toString());
        }
    }

    public void closeConnection() {
        try {
            if(socket != null)
                socket.close();
            if(bufferedReader != null)
                bufferedReader.close();
            if(bufferedWriter != null)
                bufferedWriter.close();
            System.out.println("Disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String address = "localhost";
        int port = 4999;

        Client client = new Client();
        client.initConnection(address, port);
    }
}