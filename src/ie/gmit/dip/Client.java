package ie.gmit.dip;

/**
 * @author Stephy Raju
 * 
 * The Client Program used java.net.Socket to establish a client connection and create a sender and receiver threads.
 * Client Program accepts a port number from the user and validates it for a valid port number range.
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	private static final int NUMBER_ZERO = 0;
	private static final int MAX_ALLOWED_PORT_NUMBER = 65535;
	
    public static void main(String[] args){
        final Socket clientSocket; 
        final BufferedReader bufferedReader;  
        final PrintWriter printWriter;
        final Scanner scanner = new Scanner(System.in);
       
        Client client = new Client();
        Integer portNumber = client.validatePortNumber();
        System.out.println("Client connecting to Server Port Number: " + portNumber);
        System.out.println("");
        
        try {
                    	
        	clientSocket = new Socket("localhost", portNumber);
            
            
            printWriter = new PrintWriter(clientSocket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Thread sender = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                	boolean isRunning = true;
                	
                    while(isRunning){
                        msg = scanner.nextLine();
                        if (msg == null || msg.equals("quit") || msg.equals("q")) {
                        	System.out.println("Quiting Clinet chat. Bye. " + msg);
                            isRunning = false;
                         }
                        printWriter.println(msg);
                        
                        printWriter.flush();
                        
                    }
                   
                 
                    System.out.println("Exiting client sender.");
                    System.exit(0);
                }
            });
            sender.start();
            Thread receiver = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    try {
                    	
                        boolean isRunning = true;
                        while(isRunning){
                        	
                        	
                        	 msg = bufferedReader.readLine();
                        	 if(msg == null) {
                         		System.out.println("Server is not reachable");
                         		System.out.println("Exiting Client...");
                                printWriter.close();
                                clientSocket.close();
                                System.exit(0);
                         	}
                            System.out.println("Server: " + msg);
                           
                            if (msg.equals("quit") || msg.equals("q")) {
                            	
                            	System.out.println("Quiting Server chat. Bye. " + msg);
                            	isRunning = false;      	
                            	
                            }
                            
                        }
                        System.out.println("Exiting Client...");
                        printWriter.close();
                        clientSocket.close();
                        System.exit(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receiver .start();
        } catch (ConnectException e) {
            System.out.println("Server is not reachable, exiting");
            System.exit(0);
        
        } catch (IOException e){
    	
        e.printStackTrace();
        }
    }
    
    private Integer validatePortNumber() {
        Scanner scanner = new Scanner(System.in);

        boolean isValidNumber = false;
        int number;
        do {
            System.out.print("Please enter server port number to connect: ");
        	    
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
        System.out.printf("Cleint now connecting to port %d.%n", number);
        System.out.println("");
        
        return number;
    }
}

