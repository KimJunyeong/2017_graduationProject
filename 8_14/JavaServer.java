
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
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
        
        String[] array = null;
        String[] name_value = null;
        int[] intArray = new int[6];
        
        int node[][] = {
				{-95,-90,-83,-93,-90,-92},
				{-95,-90,-86,-86,-89,-94},
				{-95,-92,-92,-82,-89,-93},
				{-96,-81,-86,-90,-90,-94},
				{-93,-84,-88,-88,-89,-90},
				{-92,-92,-91,-85,-80,-85},				{-83,-87,-92,-95,-91,-91},
				{-87,-89,-91,-94,-85,-84},
				{-90,-94,-95,-95,-89,-84},
				{-91,-93,-94,-95,-90,-88}
		};
		
        try{
        	serverSocket = new ServerSocket(55547);
            System.out.println("I'm waiting here: " 
                + serverSocket.getLocalPort());            
                                
            socket = serverSocket.accept();
            System.out.println("from " + 
                socket.getInetAddress() + ":" + socket.getPort());
            
            InputStreamReader inputStreamReader = 
                new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            
            String line;
            
            while((line=bufferedReader.readLine()) != null){
            	array = null;
                array = line.replaceAll("null", "").split(",");
                dumpArray(array);
                int diff[][] = new int[16][6];					//16 node, 6 beacon signal value
        		int tdiff[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};//total diffrent, each node value
        		int minIndex;									//smallesst tdiff value, closest node
                for(int i = 0;i < array.length;i++) {
                    name_value = array[i].split(":");

                    switch(name_value[0]){
                        case "bag":
                            intArray[0] = Integer.parseInt(name_value[1]);
                            break;
                        case "bed":
                            intArray[1] = Integer.parseInt(name_value[1]);
                            break;
                        case "bike":
                            intArray[2] = Integer.parseInt(name_value[1]);
                            break;
                        case "car":
                            intArray[3] = Integer.parseInt(name_value[1]);
                            break;
                        case "dog":
                            intArray[4] = Integer.parseInt(name_value[1]);
                            break;
                        case "door":
                            intArray[5] = Integer.parseInt(name_value[1]);
                            break;
                        default:
                            break;
                    }
                }
                
                for(int i=0;i<10;i++){
                	tdiff[i]=0;
    				for(int j=0;j<6;j++){
    					int temp = node[i][j] - intArray[j];
    					diff[i][j] = (int) Math.pow(temp, 2);
    					tdiff[i] += diff[i][j];
    				}
    			}
                minIndex = 0;
    		    for (int i = 0; i < 10; i++) {
    		        int newnumber = tdiff[i];
    		        if ((newnumber < tdiff[minIndex])) {
    		            minIndex = i;
    		        }
    		    }
    		    System.out.println(minIndex+1 +" is the closest node");
    		    //database.storeData();
    		    //database.getData();
    		    //JavaClient.sendNode(minIndex+1);
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
