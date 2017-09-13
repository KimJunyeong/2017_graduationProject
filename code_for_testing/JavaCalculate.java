//package raspberrypi;

public class JavaCalculate {

	public static void findNode(String line){
		String[] array = null;
        String[] name_value = null;
        double[] receive = new double[7];
        
        double node[][] = {
				{76.35, 78.55, 90.75, 88.2,  88.75, 92.45},
				{78.9,  78.7,  83.15, 87.05, 88.6,  90.45},
				{87.3,  80.95, 74.35, 92.35, 91.3,  89.4 },
				{87.25, 94.25, 96.59, 73.65, 79.9,  92.82},
				{92.74, 94.73, 94.75, 87.4,  74.7,  77.2 },
				{97,    97.67, 94,    94.5,  90.75, 69.6 },
				//{85.3,  89.9,  92.2,  83.3,  86.35, 94.4 },
				//{82.85, 86,    91.05, 86.65, 81.05, 86.85},
				//{87.85, 91.75, 84.4,  89.7,  87.25, 84.05},
				{94,    92.5,  95.8,  97.62, 93.1,  91.05}
		};
        
        array = null;
        array = line.replaceAll("null", "").split(",");

        //int diff[][] = new int[10][6];					//10 node, 6 beacon signal value
        double diff[][]= new double[7][6];
	double tdiff[] = {0,0,0,0,0,0,0};			//total difference for each node value
		int minIndex;									//smallest tdiff value = closest node
		
        for(int i = 0;i < array.length;i++) {
            name_value = array[i].split(":");

            switch(name_value[0]){
                case "bag":
                    receive[0] = Double.parseDouble(name_value[1]);
                    break;
                case "bed":
                    receive[1] = Double.parseDouble(name_value[1]);
                    break;
                case "bike":
                    receive[2] = Double.parseDouble(name_value[1]);
                    break;
                case "car":
                    receive[3] = Double.parseDouble(name_value[1]);
                    break;
                case "dog":
                    receive[4] = Double.parseDouble(name_value[1]);
                    break;
                case "fridge":
                    receive[5] = Double.parseDouble(name_value[1]);
                    break;
		case "chair":
			receive[6] = Double.parseDouble(name_value[1]);
			break;
                default:
                    break;
            }
        }
        dumpArray(receive);//
        /*
        for(int i=0;i<7;i++){
        	tdiff[i] = 0;
			for(int j=0;j<6;j++){
				if(receive[j] == 0){
					receive[j] = -100;
				}
				double temp = (node[i][j] + receive[j]);		//type error?
				diff[i][j] = Math.pow(temp, 2);
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
	//System.out.println("node is "+  minIndex);
	    
	    if(minIndex>5 && minIndex<9){		//node 7 8 9
	    	//JavaClient.sendNode(7);
	    } else{
		    //JavaClient.sendNode(minIndex+1);	
	    }
*/
	    
	    //JavaDatabase.storeData(minIndex+1);
	    //JavaDatabase.decideDanger(minIndex+1);
	}
	public static void dumpArray(double[] array) {
	    System.out.println();
	    for (int i = 0; i < array.length; i++)
	      System.out.format("array[%d] = %f%n", i, array[i]);
	    System.out.println();//
	  }
}
