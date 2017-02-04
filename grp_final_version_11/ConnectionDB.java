// http://blog.csdn.net/yu_han_23/article/details/17026193
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used to connect with database which using JDBC.
 * 
 */
public class ConnectionDB {  

	public static final String DBDRIVER="com.mysql.jdbc.Driver"; 
    public static final String DBURL="jdbc:mysql://localhost:3307/grp";  
    public static final String DBUSER="root";  
    public static final String DBPASSWORD=""; 
    private final static int max = (int) Math.pow(2, 63);
    private final static String max_connection = "SET GLOBAL max_connections = "+max ;
	
    static Connection conn = null ;  
        
    static Statement st;    
     
    private static ConnectionDB instance;
    
    /**
     * this method implement the singleton pattern.
     */
    synchronized public static ConnectionDB getInstance() {
		if(instance == null){
			instance = new ConnectionDB();
		}   	
    	return instance;
	}
    
    /**
     * this method is used to change the max connection to database.
     */
    public void ChangeMaxConneciton() {
    	conn = getConnection();
    	try {
			st = (Statement) conn.createStatement();
	        st.executeQuery(max_connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * this method is used to connect with currentDevice table.
     * @return Connection object
     */
    public Connection getConnection() {    
        Connection con = null;     
        try {    
            Class.forName(DBDRIVER);   
                    
            con = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);    

        } catch (Exception e) {    
            System.out.println("Connection failed!" + e.getMessage());    
        }    
        return con;    
    }
    
    /**
     * this method is used to check DID' state from currentDevice table.
     * @return int
     */
    public int checkDIDState(String did) {
  	
    	conn = getConnection();
    	int number = 0;
    	
    	try{
        	String ssql = "SELECT STATE FROM CurrentDevice WHERE (DID = '"+did+"')";
        	st = (Statement) conn.createStatement();
            ResultSet rs = st.executeQuery(ssql);         

            while(rs.next()) {
            	String state = rs.getString("STATE");
            	System.out.println(state);
            	
            	if(state.equals("1")) {
            		number++;
            	}
            }
        	
        } catch (SQLException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
		}
    	
    	return number;
    }
    
    /**
     * this method is used to check RID&DID's state from currentDevice table.
     * @return String
     */
    public String checkState(String rdid) {
    	
    	conn = getConnection();
    	String resultcheck = null;
    	
    	String[] rdids = rdid.split("&");
    	try{
        	String ssql = "SELECT STATE FROM CurrentDevice WHERE (RID = '"+rdids[0]+"') AND (DID = '"+rdids[1]+"')";
        	st = (Statement) conn.createStatement();
            ResultSet rs = st.executeQuery(ssql);    
            
            while(rs.next()) {
            	String state = rs.getString("STATE");
            	
            	if(!state.equals("0")) {
            		resultcheck = "true";
            	}else {
            		resultcheck = "false";
            	}
            	
            }
        	      	
        } catch (SQLException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
		}
    	return resultcheck;
    }
    
    /**
     * this method is used to change all the device's state on.
     * @return List<String>
     */
    public List<String> getStateOn() {
	
    	conn = getConnection(); 
        List<String> result = new ArrayList<String>();
    	
        try{
        	String ssql = "SELECT RID, DID FROM CurrentDevice WHERE (STATE = 0)";
        	st = (Statement) conn.createStatement();
            ResultSet rs = st.executeQuery(ssql);         

            while(rs.next()) {
                String string= null;
                string = rs.getString("RID")+"&"+rs.getString("DID");
            	result.add(string);
            }
        	
        } catch (SQLException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
		}
        return result;
    }
    
    public List<String> checkDID(String did) {
    	conn = getConnection();
    	List<String> didsList = new ArrayList<String>();
        try{
        	String ssql = "SELECT RID, DID, STATE FROM CurrentDevice WHERE (DID = '"+did+"')";
        	st = (Statement) conn.createStatement();
            ResultSet rs = st.executeQuery(ssql);         

            while(rs.next()) {
                String string= null;
                string = rs.getString("RID")+"&"+rs.getString("DID")+"&"+rs.getString("STATE");
                didsList.add(string);
            }
        	
        } catch (SQLException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
		}
        return didsList;
    }
    
    /**
     * this method is used to check DID's state, and if the state is on, then return true, else return false.
     * @return boolean
     */
    public boolean checkID(String id) {
    	conn = getConnection();
    	boolean check = false;
//    	String result = null;
    	int counter = 0;
    	String ssql[] = id.split("&");
    	String sql = "SELECT * FROM CurrentDevice WHERE (RID = '"+ssql[0]+"') AND (DID = '"+ssql[1]+"')";
    	try {
			st = (Statement) conn.createStatement();
	        ResultSet rs = st.executeQuery(sql);
	        
	        while(rs.next()) {
	        	counter = rs.getInt(1);
	        }
	        if(counter == 0) {
	        	check = false;
	        }else {
	        	check = true;
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return check;
    }
    
    /**
     * this method is used to get the RID&DID from table and store them into a list.
     * @return List<String>
     */
    public List<String> getID() {
    	
    	conn = getConnection(); 
        List<String> result = new ArrayList<String>();
    	
        try{
        	String ssql = "SELECT RID, DID FROM CurrentDevice";
        	st = (Statement) conn.createStatement();
            ResultSet rs = st.executeQuery(ssql);         

            while(rs.next()) {
                String string= null;
                string = rs.getString("RID")+"&"+rs.getString("DID");
            	result.add(string);
            }
        	
        } catch (SQLException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
		}
        return result;
    }
    
    public List<String> getState(String id) {
    	
    	conn = getConnection(); 
    	String sql[] = id.split("&");
        List<String> result = new ArrayList<String>();
    	
        try{
        	String ssql = "SELECT STATE FROM CurrentDevice WHERE RID = ('"+sql[0]+"') AND DID = ('"+sql[1]+"')";
        	st = (Statement) conn.createStatement();
            ResultSet rs = st.executeQuery(ssql);         

            while(rs.next()) {
                String string= null;
                string = rs.getString("STATE");
            	result.add(string);
            }
        	
        } catch (SQLException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
		}
        return result;
    }

    /**
     * this method is used to get the VALUE from table and return as a String.
     * @return String
     */
    public String getValue(String rdid) {
    	
    	conn = getConnection(); 
        String result = null;
        String[] id = rdid.split("&");
    	
        try{
        	String ssql = "SELECT DVALUE FROM CurrentDevice WHERE (RID='"+id[0]+"') AND (DID='"+id[1]+"')";
        	st = (Statement) conn.createStatement();
            ResultSet rs = st.executeQuery(ssql);         

            while(rs.next()) {
                result = rs.getString("DVALUE");
            }
        	
        } catch (SQLException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
		}
        return result;
    }
    
    /**
     * this method is used to insert the sum consumption into Histroy table.
     * @return void
     */
    public void insertHistroy(long sum) {    	
    	conn = getConnection();        
        try {          		
        	String sql = "INSERT INTO Histroy(SDVALUE)"    
	        			+ " VALUES ('"+sum+"')";
	                    
	        st = (Statement) conn.createStatement();
            st.executeUpdate(sql); 	   
	                        
            st.close();
            conn.close(); 
        } catch (SQLException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
		}      
    }
    
    /**
     * this method is used to turn all the device's state to on.
     * 
     */
    public void turnOnAll() {
    	conn = getConnection();
    	String sql = "UPDATE CurrentDevice SET STATE = '1'";
    	
        try {
        	st = (Statement) conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void insertRidDid(String data,String state) {
    	conn = getConnection(); 
        String linedata[] = data.split(",");
        Date date = new Date();
        
        try {
        	String ssql = "SELECT * FROM CurrentDevice WHERE (RID='"+linedata[0]+"') AND (DID='"+linedata[1]+"')";
        	st = (Statement) conn.createStatement();
            ResultSet rs = st.executeQuery(ssql);
            int counter = 0;
            	
            while(rs.next()) {
            	counter = rs.getInt(1);
            	System.out.println(rs.getString("DID")+"!111111111");
            }
                   
           	if(counter == 0){ // no data           	
	            String sql = "INSERT INTO CurrentDevice(RID, DID, STATE, TIMEV)"    
	                        + " VALUES ('"+linedata[0]+"','"+linedata[1]+"','"+state+"','"+date+"')";   
	            System.out.println("sql="+sql);  	                    
	            st = (Statement) conn.createStatement();						  
	            int count = st.executeUpdate(sql);    	                        
	            System.out.println("insert into CurrentDevice " + count + " numbers of data");   
	                        
	            st.close();
	            conn.close(); 
	            
            }
        } catch (SQLException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
		}     
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * this method is used to insert into the currentDevice new Entry.
     */
    public void insert(String data){  
    	
        conn = getConnection(); 
        String linedata[] = data.split(",");
        Date date = new Date();
        
        try {
        	String ssql = "SELECT * FROM CurrentDevice WHERE (RID='"+linedata[0]+"') AND (DID='"+linedata[1]+"')";
        	st = (Statement) conn.createStatement();
            ResultSet rs = st.executeQuery(ssql);
            int counter = 0;
            	
            while(rs.next()) {
            	counter = rs.getInt(1);
            	System.out.println(rs.getString("DID")+"!111111111");
            }
                   
           	if(counter == 0){ // no data 
            	
	            String sql = "INSERT INTO CurrentDevice(RID, DID, DVALUE, STATE, TIMEV)"    
	                        + " VALUES ('"+linedata[0]+"','"+linedata[1]+"','"+linedata[2]+"','"+linedata[3]+"','"+date+"')";   
	            System.out.println("sql="+sql);  
	                    
	            st = (Statement) conn.createStatement();
						  
	            int count = st.executeUpdate(sql);
	                        
	            System.out.println("insert into CurrentDevice " + count + " numbers of data");   
	                        
	            st.close();
	            conn.close(); 
	            
            }
        } catch (SQLException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
		}  
        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * this method is used to delete from the currentDevice table.
     */
    public void delete(String data) {
    	
    	conn = getConnection(); 
        String linedata[] = data.split(",");
        
        try {
				
	        	String sql = "DELETE FROM CurrentDevice"
	        				 +" WHERE (RID="+linedata[0]+")"+"AND (DID="+linedata[1]+")";
	        
	        	st = (Statement) conn.createStatement();
			
	        	int count = st.executeUpdate(sql);
	        	
	        	System.out.println("delete from CurrentDevice " + count + " numbers of data");   
	            
	            st.close();
	            conn.close();
//			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    /**
     * this method is used to update the currentDevice table VALUE.
     */
    public void updateValue(String data, long sum) 
    {
    	
    	conn = getConnection(); 
        String linedata[] = data.split("&");
        
        try {
				
	        	String sql = "UPDATE CurrentDevice SET DVALUE = '"+sum+"' WHERE (RID= '"+linedata[0]+"')"+"AND (DID= '"+linedata[1]+"')";
	        
	        	st = (Statement) conn.createStatement();
			
	        	int count = st.executeUpdate(sql);
	        	
	        	System.out.println("update from CurrentDevice " + count + " numbers of data");   
	            
	            st.close();
	            conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    /**
     * this method is used to update the currentDevice table DSTATE.
     */
    public void updateState(String data, String state) 
    {
    	
    	conn = getConnection(); 
        String linedata[] = data.split("&");
        
        try {
        	String ssql = "SELECT * FROM CurrentDevice WHERE (RID='"+linedata[0]+"') AND (DID='"+linedata[1]+"')";
        	st = (Statement) conn.createStatement();
            ResultSet rs = st.executeQuery(ssql);
            int counter = 0;        	
            while(rs.next()) {
            	counter = rs.getInt(1);
            }        	
			if(counter !=0 ) { // if have data
				
	        	String sql = "UPDATE CurrentDevice SET STATE = '"+state+"' WHERE (RID='"+linedata[0]+"')"+"AND (DID='"+linedata[1]+"')";
	        
	        	st = (Statement) conn.createStatement();
			
	        	int count = st.executeUpdate(sql);
	        	
	        	System.out.println("update from CurrentDevice " + count + " numbers of data");   
	            
	            st.close();
	            conn.close();
			}else {
				insertRidDid(data, state);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    /**
     * this method is used to insert into currentDevice table the attribute of HOUR_INT.
     */
    public void insertHourValue(String data, int hour, long sum){  
    	
        conn = getConnection(); 
        String linedata[] = data.split(",");

        Date date = new Date();
        
        try {
        	String ssql = "SELECT HOUR_"+hour+" FROM CurrentDevice WHERE (RID='"+linedata[0]+"') AND (DID='"+linedata[1]+"')";
        	st = (Statement) conn.createStatement();
            ResultSet rs = st.executeQuery(ssql);
            int counter = 0;
            	
            while(rs.next()) {
            	counter = rs.getInt(1);
            }
                   
           	if(counter == 0){ // no data 
            		
	            String sql = "INSERT INTO CurrentDevice(RID, DID, STATE, DVALUE,TIMEV,HOUR_1,HOUR_2,HOUR_3,HOUR_4,HOUR_5,HOUR_6,HOUR_7,HOUR_8,HOUR_9,HOUR_10,HOUR_11,HOUR_12)"    
	                        + " VALUES ('"+linedata[0]+"','"+linedata[1]+"','"+linedata[2]+"','"+linedata[3]+"','"+date+"','"+"')";   
	            System.out.println("sql="+sql);  
	                    
	            st = (Statement) conn.createStatement();
						  
	            int count = st.executeUpdate(sql);    
	                        
	            System.out.println("insert into CurrentDevice " + count + " numbers of data");   
	                        
	            st.close();
	            conn.close(); 
	            
            }
        } catch (SQLException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
		}  
        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * this method is used to update the currentDevice table the attribute of HOUR_INT.
     */
    public void updateHourValue(String data, int hour, long sum)
    {
    	
    	conn = getConnection(); 
        String linedata[] = data.split("&");
        
        try {				
	        String sql = "UPDATE CurrentDevice SET HOUR_"+hour+"='"+sum+"' WHERE (RID='"+linedata[0]+"')"+"AND (DID='"+linedata[1]+"')";	     
	      	st = (Statement) conn.createStatement();			
	       	int count = st.executeUpdate(sql);    	
	        System.out.println("update from CurrentDevice " + count + " numbers of data");   
	            
	        st.close();
	        conn.close();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }

    /**
     * this method is used to update the currentDevice table the attribute of DVALUE.
     */
    public void updateCurrentDeviceDValue(String data) {
    	conn = getConnection(); 
        String linedata[] = data.split("&");
        int counter = 0;
    	try {
    		for(int i=0; i<12; i++) {
    			String ssql = "SELECT HOUR_"+(i+1)+" FROM CurrentDevice WHERE (RID='"+linedata[0]+"') AND (DID='"+linedata[1]+"')";
				st = (Statement) conn.createStatement();
				ResultSet rs = st.executeQuery(ssql);
				while(rs.next()){
					counter+=Integer.parseInt(
							rs.getString("HOUR_"+(i+1)));
				}				
    		}
            updateValue(data, counter);
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * this method is used to update the currentDevice table the attribute of DID'State.
     */
    public void updateDIDState(String did, String state) 
    {	
    	conn = getConnection(); 
        try {
        	String ssql = "SELECT RID, DID FROM CurrentDevice WHERE (DID='"+did+"')";
        	st = (Statement) conn.createStatement();
            ResultSet rs = st.executeQuery(ssql);
            int counter = 0;          	
            while(rs.next()) {
            	counter = rs.getInt(1);  
            	
            	System.out.println(counter);
            } 
       			if(counter !=0 ) { // if have data				
	        	String sql = "UPDATE CurrentDevice SET STATE = '"+state+"' WHERE (DID= '"+did+"')";	        
	        	st = (Statement) conn.createStatement();			
	        	int count = st.executeUpdate(sql);	        	
	        	System.out.println("update from CurrentDevice " + count + " numbers of data");   
	            
	            st.close();
	            conn.close();
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }   
}
