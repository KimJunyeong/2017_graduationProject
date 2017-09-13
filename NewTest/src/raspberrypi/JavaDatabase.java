package raspberrypi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class JavaDatabase {
	public static void storeData(int node){
		String url = "jdbc:mysql://localhost:3306/raspberrypi?autoReconnect=true&useSSL=false";
		String sql = "SELECT * FROM Location ORDER BY time DESC LIMIT 1;";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//replace raspberrypi mysql id password
			Connection conn = 
					DriverManager.getConnection(url, "id","password");
			
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("insert into Location (time, Location) values(NOW()," + node + ");");
			//ResultSet rs = stmt.executeQuery(sql);
			
		} catch (ClassNotFoundException e) { 
			System.out.println("JDBC driver load error");
		} catch (SQLException e) { 
			System.out.println("DB connection error");
		} 
	}
	
	public static void storeBeaconData(double[] array){
		String url = "jdbc:mysql://localhost:3306/raspberrypi?autoReconnect=true&useSSL=false";
		String sql = "SELECT * FROM Beaconset ORDER BY time DESC LIMIT 1;";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//replace raspberrypi mysql id password
			//for me, root, h930227
			Connection conn = 
					DriverManager.getConnection(url, "id","password");
			
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
	
	public static int decideDanger(int node){
		String url = "jdbc:mysql://localhost:3306/raspberrypi?autoReconnect=true&useSSL=false";
		String sql1 = "SELECT * FROM Door ORDER BY time DESC LIMIT 1;";
		String sql2 = "SELECT * FROM Flame ORDER BY time DESC LIMIT 1;";
		String sql3 = "SELECT * FROM Location ORDER BY time DESC LIMIT 1;";
		
		int notification = 0;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			//enter raspberrypi mysql id, password
			Connection conn = 
					DriverManager.getConnection(url, "id","password");		
			
			Statement stmt1 = conn.createStatement();
			Statement stmt2 = conn.createStatement();
			Statement stmt3 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(sql1);
			ResultSet rs2 = stmt2.executeQuery(sql2);
			ResultSet rs3 = stmt3.executeQuery(sql3);
			
			if(rs1.next()&&rs2.next()) { 
				Time d_time = rs1.getTime("Time");
				int lock = rs1.getInt("Doorlock");
				Time f_time = rs2.getTime("Time");
				//Boolean gas = rs1.getBoolean("gas");
				//System.out.println(s_time+", "+lock+", "+distance+", "+gas);
				if(rs3.next()){
					Time l_time = rs3.getTime("Time");
					int l_node = rs3.getInt("Node");
					//System.out.println(l_time+", "+l_node);
					
					long t_difference = l_time.getTime()-d_time.getTime();
					//System.out.println(t_difference);
					
					if(lock == 0 && node == 10){
						
						Statement stmt = conn.createStatement();
						
						stmt.executeUpdate("insert into Door values(NOW(), 0);");
						System.out.println("door locked");
						notification = 0;
					}else if(node== 10){
						if(t_difference<-10000||t_difference>50000){
							notification = 2;
							System.out.println("patient is close to the door");
						}
						else{
							
							notification = 0;
						}
					}else if(node== 1){
						if(t_difference<-10000||t_difference>30000){
							notification = 1;
							System.out.println("patient is close to the gas valve");
						}
						else if(t_difference>900000){
							System.out.println("turn off gas valve");
						}
						
					}else{
						notification = 0;
					}
					
				}
				
			}
				
		} catch (ClassNotFoundException e) { 
			System.out.println("JDBC driver load error");
		} catch (SQLException e) { 
			System.out.println("DB connection error");
		}
		return notification; 
	}
	
	public static void dumpData(ResultSet rs){
		ResultSetMetaData rsmd;
		try {
			rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
			    for (int i = 1; i <= columnsNumber; i++) {
			        if (i > 1) System.out.print(",  ");
			        String columnValue = rs.getString(i);
			        System.out.print(columnValue + " " + rsmd.getColumnName(i));
			    }
			    System.out.println("");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
