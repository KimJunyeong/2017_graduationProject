import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class JavaClient {

	public static void sendNode(int node) {

        Socket socket = null;

        String msg = "";

        try {                         
            //JavaDatabase.decideDanger(node);
            msg+="-"+JavaDatabase.decideDanger(node);      
        	//msg = Integer.toString(node);
    		//msg += "-" + Integer.toString(danger);

            socket = new Socket("192.168.43.43", 8080);
            
            ObjectOutputStream outputStream;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(msg);
            outputStream.flush();
    	    
            System.out.println("Run Client");
    	    System.out.println(node +" is the closest node");
                
        } catch (IOException ex) {
            System.out.println(ex.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        /*
        if(socket != null){
            try {
                socket.close();
                System.out.println("socket closed");
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }
        */
    }
}
