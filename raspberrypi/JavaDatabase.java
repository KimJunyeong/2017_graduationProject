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
		String url = "jdbc:mysql://localhost:3306/test1?autoReconnect=true&useSSL=false";
		String sql = "SELECT * FROM location ORDER BY time DESC LIMIT 1;";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = 
					DriverManager.getConnection(url, "root","h930227");
			
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("insert into location (time, node) values(NOW()," + node + ");");
			//ResultSet rs = stmt.executeQuery(sql);
			
		} catch (ClassNotFoundException e) { 
			System.out.println("JDBC �뱶�씪�씠踰� 濡쒕뱶 �삤瑜�");
		} catch (SQLException e) { 
			System.out.println("DB �뿰寃� �삤瑜�");
		} 
	}
	
	public static int decideDanger(int node){
	//public static void main(String[] args){
		String url = "jdbc:mysql://localhost:3306/test1?autoReconnect=true&useSSL=false";
		String sql1 = "SELECT * FROM sensor ORDER BY time DESC LIMIT 1;";
		String sql2 = "SELECT * FROM location ORDER BY time DESC LIMIT 1;";
		//String sql2 = "SELECT * FROM location";
		//Time s_time = null;
		//Boolean lock = false;
		//Boolean distance = false;
		//Boolean gas = false;
		
		//Time b_time = null;
		//int node = 0;
		
		int notification = 0;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = 
					DriverManager.getConnection(url, "root","h930227");
			
			
			Statement stmt1 = conn.createStatement();
			Statement stmt2 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(sql1);
			ResultSet rs2 = stmt2.executeQuery(sql2);
			
			if(rs1.next()) { 
				Time s_time = rs1.getTime("time");
				Boolean lock = rs1.getBoolean("lock");
				Boolean distance = rs1.getBoolean("distance");
				Boolean gas = rs1.getBoolean("gas");
				//System.out.println(s_time+", "+lock+", "+distance+", "+gas);
				if(rs2.next()){
					Time l_time = rs2.getTime("time");
					int l_node = rs2.getInt("node");
					System.out.println(l_time+", "+l_node);
					
					long t_difference = l_time.getTime()-s_time.getTime();
					//System.out.println(t_difference);
					
					
					if(distance == true && lock == true && node != 10){
						//�솚�옄媛� �븘�땺 寃쎌슦 - �븣由쇰せ�븯寃�
						Statement stmt = conn.createStatement();
						//python�뿉�꽌 �씫�쓣 嫄� �븣 lock�쓣 true濡� 諛붽퓭以섏빞 �븿
						stmt.executeUpdate("insert into sensor values(NOW(), 0, 0, 0);");
						System.out.println("door unlocked");
						notification = 0;
					}else if(distance == true&&node== 10){
						if(t_difference<-10000||t_difference>30000){
							notification = 2;
							System.out.println("patient is close to the door");
						}
						else{
							//�씠誘� �씫�쓣 �뻽�쑝�땲源�..?
							notification = 0;
						}
					}else if(gas == true&&node== 1){
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
			
			//dumpData(rs2);
			/*
			if(rs1.next()) { 
				s_time = rs1.getTime("time");
				lock = rs1.getBoolean("lock");
				distance = rs1.getBoolean("distance");
				gas = rs1.getBoolean("gas");
				System.out.println(s_time+", "+lock+", "+distance+", "+gas);
				if(rs2.next()){
					b_time = rs2.getTime("time");
					node = rs2.getInt("node");
					System.out.println(b_time+", "+node);
				}
				if(distance == true && lock == true && node != 16){
					//�솚�옄媛� �븘�땺 寃쎌슦 - �븣由쇰せ�븯寃�
					Statement stmt = conn.createStatement();
					//python�뿉�꽌 �씫�쓣 嫄� �븣 lock�쓣 true濡� 諛붽퓭以섏빞 �븿
					stmt.executeUpdate("insert into sensor values(NOW(), 0, 0, 0);");
					System.out.println("door unlocked");
				}else if(distance == true&&node== 16){
					
				}else if(gas == true&&node== 1){
					
				}
			}
			*/
			/*
			if(rs2.next()){
				b_time = rs2.getTime("time");
				node = rs2.getInt("node");
				System.out.println(b_time+", "+node);
			}
			*/
			
			/*
			if(gas == true&&node== 1){
				//�븣由�?
			}
			
			if(distance == true&&node== 16){
				//�븣由�? 
			}
			*/
			/*
			if(gas == true && node != 1){
				//�솚�옄媛� �븘�땺 寃쎌슦 - �븣由쇰せ�븯寃�
				//�씠 遺�遺꾩� �븘吏� �븘�슂 �뾾�쓣 �벏
			}
			*/
			/*
			if(distance == true && lock == true && node != 16){
				//�솚�옄媛� �븘�땺 寃쎌슦 - �븣由쇰せ�븯寃�
				Statement stmt = conn.createStatement();
				//python�뿉�꽌 �씫�쓣 嫄� �븣 lock�쓣 true濡� 諛붽퓭以섏빞 �븿
				stmt.executeUpdate("insert into sensor (time, lock, distance, gas) values(NOW()," + false + ", "+false+", "+false+ ");");
			}
			*/
			
			
			
		} catch (ClassNotFoundException e) { 
			System.out.println("JDBC �뱶�씪�씠踰� 濡쒕뱶 �삤瑜�");
		} catch (SQLException e) { 
			System.out.println("DB �뿰寃� �삤瑜�");
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
