package raspberrypi;

public class JavaCalculate {

	public static void findNode(String line){
		String[] array = null;
        String[] name_value = null;
        double[] intArray = new double[7];
       
        double node[][] = {
        		{-78.95, -88.8, -92.45,-88.1,-95.15,-105,-96.3},
        		{-90.05, -77.05, -84.8,-93.3,-89.55,-104.25,-90.2},
        		{-93.2, -85.65, -82.15,-94.9,-89.75,-103.2,-79.15},
        		{-89.6, -95.15, -101.7,-75.45,-85.85,-103.75,-100.9},
        		{-103.05, -97.9, -98.35,-83.5,-85.05,-86.8,-102.8},
        		{-104.7, -105, -100.05,-93.9,-88.75,-82.4,-91.4},
        		{-102.75, -95.3, -92.85,-100.9,-90.55,-94.3,-83.1}
        };
        
        array = null;
        array = line.replaceAll("null", "").split(",");

        double diff[][] = new double[7][7];					//10 node, 6 beacon signal value
        double tdiff[] = {0,0,0,0,0,0,0};			//total difference for each node value
		int minIndex;									//smallest tdiff value = closest node
		
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
                case "fridge":
                    intArray[5] = Integer.parseInt(name_value[1]);
                    break;
                case "chair":
                    intArray[6] = Integer.parseInt(name_value[1]);
                    break;
                default:
                    break;
            }
        }
        dumpArray(intArray);//
        
        for(int i=0;i<7;i++){
        	tdiff[i] = 0;
			for(int j=0;j<7;j++){
				if(intArray[j] == 0){
					intArray[j] = -105;
				}
				int temp = (int) (node[i][j] - intArray[j]);
				diff[i][j] = (int) Math.pow(temp, 2);
				tdiff[i] += diff[i][j];
			}
			System.out.print(tdiff[i]+":");
		}
        
        minIndex = 0;
        
	    for (int i = 0; i < 7; i++) {
	    	double newnumber = tdiff[i];
	        if ((newnumber < tdiff[minIndex])) {
	            minIndex = i;
	        }
	    }
	    JavaClient.sendNode(minIndex+1);
	   // JavaDatabase.storeData(minIndex+1);
	   //JavaDatabase.decideDanger(minIndex+1);
	    //JavaDatabase.storeBeaconData(intArray);
	}
	public static void dumpArray(double[] array) {
	    for (int i = 0; i < array.length; i++)
	      System.out.format("array[%d] = %f%n", i, array[i]);//
	  }
}
