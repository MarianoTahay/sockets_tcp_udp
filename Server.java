import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
    
    public static void main(String [] args) {
        args_validation(args);

        if(args[3].equalsIgnoreCase("TCP")){
            tcp_server(Integer.parseInt(args[1]));
        } else if(args[3].equalsIgnoreCase("UDP")){
            udp_server(Integer.parseInt(args[1]));
        } else {
            System.out.println("Unknown protocol: " + args[3]);
            System.exit(0);
        }

        
    }

    public static void args_validation(String[] args){

        if(args[0].equalsIgnoreCase("port")){

            System.out.println("Port: " + args[1]);

            if(args[2].equalsIgnoreCase("protocol")){
                System.out.println("Protocol: " + args[3]);
            } else{
                System.out.println("port [] protocol [TCP/UDP]");
                System.exit(0);
            }

        } else{
            System.out.println("port [] protocol [TCP/UDP]");
            System.exit(0);
        }
    }

    public static void tcp_server(int port){
        //El servidor deberia de crear un socket para que el cliente se conecte
        try{
            try (ServerSocket server = new ServerSocket(port)) {

                System.out.println("Server is Up!");

                Socket client_connection = server.accept();
                InputStream in = client_connection.getInputStream();
                OutputStream out = client_connection.getOutputStream();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss]");

                while(true){
                    // Recibe el mensaje del cliente [bytes]
                    byte[] encoded_message = new byte[client_connection.getReceiveBufferSize()];
                    int bytesRead = in.read(encoded_message);
                    String decoded_message = new String(encoded_message, 0, bytesRead).trim();

                    System.out.println("> " + decoded_message);

                    // El server manda el mensaje [bytes]
                    LocalDateTime now = LocalDateTime.now();
                    String dateTimeString = now.format(formatter);
                    String response = message_processing(decoded_message);
                    String server_log = client_connection.getLocalAddress() + " server [" + dateTimeString + "] TCP: " + response;
                    byte[] encoded_response = server_log.getBytes();
                    out.write(encoded_response);

                    System.out.println("< " + server_log);

                    if(response.equals("EXIT")){
                        break;
                    }
                }

                out.close();
                in.close();
                client_connection.close();

            }
            
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void udp_server(int port){
        try(DatagramSocket server = new DatagramSocket(port)){
            byte[] encoded_message = new byte[server.getReceiveBufferSize()];

            System.out.println("Server is Up!");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss]");

            while(true){

                // Recibe el mensaje del cliente [bytes]
                DatagramPacket message_received = new DatagramPacket(encoded_message, encoded_message.length);
                server.receive(message_received);
                String decoded_message = new String(message_received.getData(), 0, message_received.getLength());

                System.out.println("> " + decoded_message);

                // El server manda el mensaje [bytes]
                LocalDateTime now = LocalDateTime.now();
                String dateTimeString = now.format(formatter);
                String response = message_processing(decoded_message);
                String server_log = server.getLocalAddress().toString() + " server [" + dateTimeString + "] UDP: " + response;

                byte[] encoded_response = server_log.getBytes();
                DatagramPacket send_message = new DatagramPacket(encoded_response, encoded_response.length, message_received.getAddress(), message_received.getPort());
                server.send(send_message);

                System.out.println("< " + server_log);

                if(response.equals("EXIT")){
                    break;
                }

            }
            server.close();
        } catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public static String message_processing(String log){
        int lastColonIndex = log.lastIndexOf(":");
        if (lastColonIndex != -1 && lastColonIndex < log.length() - 1) {
            String message = log.substring(lastColonIndex + 1).trim();

            if(operation_validation(message)){
                return operation_result(message);
            } else {
                return message;
            }

        }
        return "Message could not be resolved...";
    }

    public static boolean operation_validation(String message){
        String operation_regex = "^\\d+([+\\-*/%]\\d+)+$";
        return Pattern.matches(operation_regex, message.replaceAll("\\s+",""));
    }

    public static String operation_result(String operation){

        String[] parts = (operation.replaceAll("\\s+","")).split("(?<=\\d)(?=[+\\-*/%])|(?<=[+\\-*/%])(?=\\d)");
        if (parts.length != 3) {
            return "Error";
        }

        double left = Double.parseDouble(parts[0]);
        double right = Double.parseDouble(parts[2]);
        char operator = parts[1].charAt(0);

        switch (operator) {
            case '+':
                return String.valueOf(left + right);
            case '-':
                return String.valueOf(left - right);
            case '*':
                return String.valueOf(left * right);
            case '/':
                if (right == 0) return "Error";
                return String.valueOf(left / right);
            case '%':
                return String.valueOf(left % right);
            default:
                return "Unsupported operator: " + operator;
        }
    }


}
