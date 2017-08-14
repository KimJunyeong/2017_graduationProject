
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class JavaClient {
	public static void sendNode(int node) {

        Socket socket = null;

        String msg = "";

        try {                               
                System.out.println(node+"th node");
                
                switch(node){
                    case 1:
                    case 2: msg="1"; break;
                    case 3: msg="7"; break;
                    case 4:
                    case 5: msg="4"; break;
                    case 6:
                    case 7: msg="2"; break;
                    case 8: msg="8"; break;
                    case 9:
                    case 10: msg="5"; break;
                    case 11:
                    case 12: msg="3"; break;
                    case 13: msg="9"; break;
                    case 14:
                    case 15: msg="6"; break;
                    case 16: msg="10"; break;
                    default: msg="11"; break;
                }

                msg+="-2";

        		socket = new Socket("192.168.1.90", 8080);
                System.out.println("Run Client");

                ObjectOutputStream outputStream;
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(msg);
                outputStream.flush();
                
        } catch (IOException ex) {
            System.out.println(ex.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(socket != null){
            try {
                socket.close();
                System.out.println("socket closed");
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }
    }
}
