package com.flins.test3;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class OperateMysql {

	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://192.168.5.84:3306/memeber?serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "123456";
    Connection  coon=null;
    public OperateMysql(){
    	try{
    		//  加载驱动程序
          Class.forName(JDBC_DRIVER);
          coon=(Connection)DriverManager.getConnection(DB_URL,USER,PASS);
          if(!coon.isClosed()){
        	  System.out.println("成功连接数据库！");
          } 
        }catch (Exception e){
                e.printStackTrace();
            }
    }
    public void close(){
        try{
             this.coon.close();
         }catch(Exception e){
            e.printStackTrace();
        }
   }
  
    //查询商户appId
    public String getAppID(int mer_id) {
    	String app_id = null;
    	try {
    		String sql = "select * from pf_merchant_info where id= ? ";
    		PreparedStatement stat = this.coon.prepareStatement(sql);
    		stat.setInt(1, mer_id);
			ResultSet rs=stat.executeQuery();
			while(rs.next()) {
				 app_id  = rs.getString("app_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return app_id;  	
    }
    
    //查询商户密钥
    public Map<String,String> getSecretKey(int mer_id) throws Exception{
    	Map<String,String> keys = new HashMap<String,String>();
    	
    	String sql = "select * from pf_merchant_secret where mer_id = ?";
    	PreparedStatement stat = this.coon.prepareStatement(sql);
    	stat.setInt(1, mer_id);
    	ResultSet rs = stat.executeQuery();
    	String key = null;
    	String public_key = null;
    	String private_key = null;
    	while(rs.next()) {
	    	key = rs.getString("secret_key");
	    	public_key = rs.getString("public_key");
	    	private_key = rs.getString("private_key");
    	}
    	keys.put("key", key);
    	keys.put("public", public_key);
    	keys.put("private",private_key);
    	return keys;
    }
   
	public static void main(String[] args) throws Exception {
		OperateMysql o = new OperateMysql();
		Map results = o.getSecretKey(28);
		System.out.println(results.get("key"));
		System.out.println(results.get("public"));
		System.out.println(results.get("private"));
	}

}

