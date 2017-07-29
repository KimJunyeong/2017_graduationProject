import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

class host
{
    public static void main(String srgs[])
    {   
        ServerSocket serverSocket = null;
        Socket socket = null;
        BufferedReader bufferedReader = null;
        PrintStream printStream = null;
        
        String[] newline;
        //String bArray[];
        String line;
        int[] bArray;
        
        int node[][] = {
				{-95,-90,-83,-93,-90,-92},
				{-95,-93,-79,-91,-91,-93},
				{-95,-90,-86,-86,-89,-94},
				{-95,-92,-92,-82,-89,-93},
				{-95,-93,-93,-85,-88,-93},
				{-96,-81,-86,-90,-90,-94},
				{-95,-79,-86,-92,-89,-93},
				{-93,-84,-88,-88,-89,-90},
				{-92,-92,-91,-85,-80,-85},
				{-95,-92,-93,-90,-79,-88},
				{-83,-87,-92,-95,-91,-91},
				{-86,-88,-93,-96,-88,-90},
				{-87,-89,-91,-94,-85,-84},
				{-90,-94,-95,-95,-89,-84},
				{-89,-94,-94,-94,-86,-81},
				{-91,-93,-94,-95,-90,-88}
		};
		
		int diff[][] = new int[16][6];
		int tdiff[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		int max = -1;
        
        try{
            serverSocket = new ServerSocket(13150);
            System.out.println("I'm waiting here: " 
                + serverSocket.getLocalPort());            
                                
            socket = serverSocket.accept();
            System.out.println("from " + 
                socket.getInetAddress() + ":" + socket.getPort());
            
            InputStreamReader inputStreamReader = 
                new InputStreamReader(socket.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            
            
            while((line=bufferedReader.readLine()) != null){
                //System.out.println(line);
                newline = line.replaceAll("dog=", "").replaceAll("shoe=", "").replaceAll("fridge=", "").replaceAll("bag=", "").replaceAll("bed=", "").replaceAll("door", "").split("\n");
                bArray = new int[newline.length];
                System.out.println("new line: " + newline);
                for (int i = 0; i < newline.length; i++) {
                    try {
                        bArray[i] = Integer.parseInt(newline[i]);
                    } catch (NumberFormatException nfe) {
                        //NOTE: write something here if you need to recover from formatting errors
                    };
                }
                dumpArray(bArray);
                
                while(true){
        			for(int i=0;i<16;i++){
        				for(int j=0;j<6;j++){
        					int temp = node[i][j] - bArray[j];
        					diff[i][j] = (int) Math.pow(temp, 2);
        					tdiff[i] += diff[i][j];
        				}
        			}
        			
        			int minIndex = 0;
        		    for (int i = 0; i < 16; i++) {
        		        int newnumber = tdiff[i];
        		        if ((newnumber < tdiff[minIndex])) {
        		            minIndex = i;
        		        }
        		    }
        		    System.out.println(minIndex+1 +" is the closest node");
        		    //delay 넣는건데 DB 연결 오류가 생김. 하지만 코드는 잘 돌아감.
        		    try {
        				TimeUnit.SECONDS.sleep(10);
        			} catch (InterruptedException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        		}
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
    
    public static void dumpArray(int[] array) {
	    for (int i = 0; i < array.length; i++)
	      System.out.format("array[%d] = %s%n", i, array[i]);
	  }
}