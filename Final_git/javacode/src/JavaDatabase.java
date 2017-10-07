import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.TimerTask;

public class JavaDatabase{
	static boolean flameflag = false;
	static int previous = 4;
	public static void storeData(int node){
		String url = "jdbc:mysql://localhost:3306/Be_Care?autoReconnect=true&useSSL=false";
		String sql = "SELECT * FROM Location ORDER BY Time DESC LIMIT 1;";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//replace raspberrypi mysql id password
			Connection conn = 
					DriverManager.getConnection(url, "root","jsy123");
			
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("insert into Location (Time, Location) values(NOW()," + node + ");");
			//ResultSet rs = stmt.executeQuery(sql);
			
		} catch (ClassNotFoundException e) { 
			System.out.println("JDBC driver load error");
		} catch (SQLException e) { 
			System.out.println("DB connection error");
		}
	}
	
	//for making beacon map. WARINING: this is not finished yet.
	/*
	public static void storeBeaconData(double[] array){
		String url = "jdbc:mysql://localhost:3306/Be_Care?autoReconnect=true&useSSL=false";
		String sql = "SELECT * FROM Beaconset ORDER BY Time DESC LIMIT 1;";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//replace raspberrypi mysql id password
			//for me, root, h930227
			Connection conn = 
					DriverManager.getConnection(url, "Approach","approach");
			
			double b1=array[0];
			double b2=array[1];
			double b3=array[2];
			double b4=array[3];
			double b5=array[4];
			double b6=array[5];
			
			
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("insert into Location (b1, b2, b3, b4, b5, b6) values("+b1+","+b2+","+b3+","+b4+","+b5+","+b6 + ");");
			//ResultSet rs = stmt.executeQuery(sql);
			
		} catch (ClassNotFoundException e) { 
			System.out.println("JDBC driver load error");
		} catch (SQLException e) { 
			System.out.println("DB connection error");
		} 
	}
	*/
	
	public static int decideDanger(int node){
		String url = "jdbc:mysql://localhost:3306/Be_Care?autoReconnect=true&useSSL=false";
		String sql1 = "SELECT * FROM Door ORDER BY Time DESC LIMIT 1;";
		String sql2 = "SELECT * FROM Flame ORDER BY Time DESC LIMIT 1;";
		String sql3 = "SELECT * FROM Location ORDER BY Time DESC LIMIT 1;";
		
		//boolean flameflag = false;

		int notification = 0;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			//enter raspberrypi mysql id, password
			Connection conn = DriverManager.getConnection(url, "root","jsy123");		
			
			Statement stmt1 = conn.createStatement();
			Statement stmt2 = conn.createStatement();
			Statement stmt3 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(sql1);//door
			ResultSet rs2 = stmt2.executeQuery(sql2);//flame
			ResultSet rs3 = stmt3.executeQuery(sql3);//location
                        Time location_time;
                        int location_node;

			if(rs3.next()){
				location_time = rs3.getTime("Time");
        	                location_node = rs3.getInt("Location");
			}else{
				location_node = previous;
			}

			if(rs1.next()) { 
				//Door data
				int door = rs1.getInt("Door");
				int doorlock = rs1.getInt("Doorlock");
				Time door_time = rs1.getTime("Time");
				if(doorlock == 2){
					//long t_difference = location_time.getTime()-door_time.getTime();
					Statement stmt = conn.createStatement();
					//if the sensed person is patient,
					if(node == 2){
						stmt.executeUpdate("UPDATE Door SET Doorlock=1 WHERE Time = '" + door_time + "';");
                                                System.out.println("door locked");
						notification = 2;
					//if the person is not patient
					}else{
						stmt.executeUpdate("UPDATE Door SET Doorlock=0 WHERE Time = '"+door_time+"';");
						System.out.println("door opened");
						notification = 0;
					}
				}
				else if(doorlock == 1) {
					if(door == 1){	// patient still near door
                                                notification = -1;
                                        }
                                        else {		// patient leaves door
                                                notification = 0; 
                                        }
				}else{
					notification = 0;
				}
			}

			if(rs2.next()){
				int gas = rs2.getInt("Flame");
                                //Time flame_time = rs2.getTime("Time");
                                //System.out.println(gas+", "+ flame_time);
				if(gas==1){		// fire starts
					Statement stmt = conn.createStatement();
					if(node==1){	// patient near fire
                                               	if(!flameflag){		// first notify fire usage
							flameflag = true;
							notification = 1;
							System.out.println("patient is close to the gas valve");
						}
						else{			// secondly not notify fire usage
							notification = -1;
						}
					}
					else{		// patient not near fire
						if(flameflag){		// patient started fire before
							notification = 1;
						} else{			// nok starts fire
                                                	notification = 0;
                                                	System.out.println("nok starts fire");
						}
					}
				}
				else{			// fire dies
                                        flameflag = false;
					notification = 0;
				}
			}
			previous = location_node;
		} catch (ClassNotFoundException e) { 
			System.out.println("JDBC driver load error");
		} catch (SQLException e) { 
			System.out.println("DB connection error");
			e.printStackTrace();
		}
		return notification; 
	}

}
