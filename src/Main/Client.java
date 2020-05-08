package Main;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client
{
    private static final String SERVER_IP = "127.0.0.1";
    private static boolean connected = true;

    public static void main(String[] args) throws IOException
    {
        InetAddress ip = InetAddress.getByName(SERVER_IP);
        try {
            Socket socket = new Socket(ip, 5056);
            Scanner scanner = new Scanner(System.in);

            DataInputStream reader = new DataInputStream(socket.getInputStream());
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

            // Check for server message
            Thread checkForServerMessage = new Thread(() -> {
                try {
                    while(connected)
                        System.out.println(reader.readUTF());
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            });
            checkForServerMessage.start();

            // Check for client message
            while(connected)
            {
                // Main.Client message
                String clientMessage = scanner.nextLine();
                writer.writeUTF(clientMessage);

                if(clientMessage.equals("/leave"))
                {
                    System.out.println("Leaving session...");
                    connected = false;
                    socket.close();
                }
            }

            scanner.close();
            reader.close();
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
} 