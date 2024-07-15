import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.*;

public class Client {
    public static void main(String [] args) throws IOException{

        System.out.println();
        args_validation(args);

        if(args[1].equalsIgnoreCase("TCP")){
            tcp_client(args[3], Integer.parseInt(args[5]));
        } else if(args[1].equalsIgnoreCase("UDP")){
            udp_client(args[3], Integer.parseInt(args[5]));
        } else {
            System.out.println("Unknown protocol: " + args[1]);
            System.exit(0);
        }
        
    }

    public static void args_validation(String[] args){

        if(args[0].equalsIgnoreCase("protocol")){

            System.out.println("Protocol set to: " + args[1]);

            if(args[2].equalsIgnoreCase("server")){
                System.out.println("Server set to: " + args[3]);

                if(args[4].equalsIgnoreCase("port")){
                    System.out.println("Port set to: " + args[5]);
                } else{
                    System.out.println("Err: protocol [TCP/UDP] server [] port []");
                    System.exit(0);
                }

            } else{
                System.out.println("Err: protocol [TCP/UDP] server [] port []");
                System.exit(0);
            }

        } else{
            System.out.println(" Err: protocol [TCP/UDP] server [] port []");
            System.exit(0);
        }
    }

    public static void tcp_client(String host, int port){
        try(Socket cliente = new Socket("localhost", port)){

            InputStream in = cliente.getInputStream();
            OutputStream out = cliente.getOutputStream();

            BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss]");
            

            while(true){

                System.out.print("Ingrese la operación a realizar: ");
                String message = reader.readLine();

                // El cliente manda el mensaje [bytes]
                LocalDateTime now = LocalDateTime.now();
                String dateTimeString = now.format(formatter);
                String client_log = host + " client [" + dateTimeString + "] TCP: " + message;
                byte[] encoded_message = client_log.getBytes();
                out.write(encoded_message);

                System.out.println("< " + client_log);

                // Recibe el mensaje del server [bytes]
                byte[] encoded_response = new byte[cliente.getReceiveBufferSize()];
                int bytesRead = in.read(encoded_response);
                String decoded_response = new String(encoded_response, 0, bytesRead).trim();

                System.out.println("> " + decoded_response);

                if(message.equals("EXIT")){
                    break;
                }

            }

            out.close();
            in.close();
            cliente.close();

        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public static void udp_client(String host, int port){

        try(DatagramSocket client = new DatagramSocket()){

            InetAddress address = InetAddress.getByName(host);

            BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss]");

            while(true){
                System.out.print("Ingrese la operación a realizar: ");
                String message = reader.readLine();

                // El cliente manda el mensaje [bytes]
                LocalDateTime now = LocalDateTime.now();
                String dateTimeString = now.format(formatter);
                String client_log = host + " client [" + dateTimeString + "] UDP: " + message;
                byte[] encoded_message = client_log.getBytes();
                DatagramPacket send_message = new DatagramPacket(encoded_message, encoded_message.length, address, port);
                client.send(send_message);

                System.out.println("< " + client_log);

                // Recibe el mensaje del server [bytes]
                byte[] encoded_response = new byte[client.getReceiveBufferSize()];
                DatagramPacket message_received = new DatagramPacket(encoded_response, encoded_message.length);
                client.receive(message_received);
                String decoded_response = new String(message_received.getData(), 0, message_received.getLength());

                System.out.println("> " + decoded_response);

                if(message.equals("EXIT")){
                    break;
                }

            }

            client.close();

        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

}
