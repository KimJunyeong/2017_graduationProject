import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class JavaServer {
	public static void main(String srgs[])
    {   
        ServerSocket serverSocket = null;
        Socket socket = null;
        BufferedReader bufferedReader = null;
        PrintStream printStream = null;
        
        String newline;
        
        
        try{
            serverSocket = new ServerSocket(0);
            System.out.println("I'm waiting here: " 
                + serverSocket.getLocalPort());            
            
            //socket connection
            socket = serverSocket.accept();
            System.out.println("from " + 
                socket.getInetAddress() + ":" + socket.getPort());
            
            //client data를 inputStreamReader에 저장
            InputStreamReader inputStreamReader = 
                new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            
            String line;
            while((line=bufferedReader.readLine()) != null){
                //System.out.println(line);
                newline = line.replaceAll("dog=", "").replaceAll("shoe=", "").replaceAll("fridge=", "").replaceAll("bag=", "").replaceAll("bed=", "").replaceAll("door", "");
                System.out.println("new line: " + newline);
                String bArray[] = newline.split("\n");
                dumpArray(bArray);
            }
            
            
        }catch(IOException e){
            System.out.println(e.toString());
        }finally{
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    System.out.print(ex.toString());
                }
            }
            
            if(printStream!=null){
                printStream.close();
            }
            
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException ex) {
                    System.out.print(ex.toString());
                }
            }
        }
        
    }
	
	public static void dumpArray(String[] array) {
	    for (int i = 0; i < array.length; i++)
	      System.out.format("array[%d] = %s%n", i, array[i]);
	  }
}
