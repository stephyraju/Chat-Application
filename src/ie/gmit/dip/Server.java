package ie.gmit.dip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 
 * @author Stephy Raju
 *
 * The Server program uses java.net.SocketServer to establish a server connection and create a sender and receiver threads.
 * Server program accepts a port number from the user and validates it for a valid port number range.
 * If the entered port is valid, Server program will try to start the ServerSocket on that port. If that port is being
 * used by any other application, the Server program will stop and let the user to enter another unused port.
 */
public class Server {
	
	private static final int NUMBER_ZERO = 0;
	private static final int MAX_ALLOWED_PORT_NUMBER = 65535;

	public static void main(String[] args) {
		
		Server server = new Server();
		Integer portNumber = server.validatePortNumber();
		
		System.out.println("Server Started on port number: " + portNumber);
		System.out.println("");
		final ServerSocket serverSocket;
        final Socket clientSocket ;
        final BufferedReader bufferedReader;
        final PrintWriter printWriter;
        final Scanner scanner = new Scanner(System.in);

        try {
            serverSocket = new ServerSocket(portNumber);
            clientSocket = serverSocket.accept();
            printWriter = new PrintWriter(clientSocket.getOutputStream());
            bufferedReader = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));

            Thread sender = new Thread(new Runnable() {
                String msg; 
                @Override  
                public void run() {
                    while(true){
                        msg = scanner.nextLine();
                        printWriter.println(msg);    
                        printWriter.flush(); 
                    }
                }
            });
            
            sender.start();
		
            Thread receive = new Thread(new Runnable() {
                String msg ;
                @Override
                public void run() {
                    try {
                        msg = bufferedReader.readLine();
                        
                        while(msg!=null){
                            System.out.println("Client: " + msg);
                            msg = bufferedReader.readLine();
                            printWriter.println(msg); 
                        }

                        System.out.println("Exiting server chat");

                        printWriter.close();
                        clientSocket.close();
                        serverSocket.close();
                        System.exit(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receive.start();
        } catch (BindException e) {
            System.out.println("Port is already in use. Enter a free port number and try agin.");
            System.exit(0);
        
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
	
	private Integer validatePortNumber() {
        Scanner scanner = new Scanner(System.in);

        boolean isValidNumber = false;
        int number;
        do {
            System.out.print("Please enter a port number for server to run: ");
        	    
            while (!scanner.hasNextInt()) {
                String input = scanner.next();
                System.out.printf("\"%s\" is not a valid port number.%n. Enter a valid port number: ", input);
            }
            
            number = scanner.nextInt();
            System.out.println("Entered Number: " + number);
            
            if(number < NUMBER_ZERO || number > MAX_ALLOWED_PORT_NUMBER) {
            	System.out.printf("\"%s\" is not a valid port number.%n. Enter a valid port number: ", number);
            }else {
            	isValidNumber = true;
            }

        } while (!isValidNumber);

        System.out.printf("You have entered a valid port number %d.%n", number);
        System.out.println("");
        System.out.printf("Server now starting on port %d.%n", number);
        System.out.println("");
        
        return number;
    }
}