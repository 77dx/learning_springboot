package com.example.busclient.controller;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ch.qos.logback.core.util.SystemInfo;

public class OperateMysql {

	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://192.168.5.84:3306/member?serverTimezone=UTC";
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
   
  
    //查询boss用户
    public ArrayList<User> select() {
    	String sql="select * from boss_user";
    	ArrayList<User> users= new ArrayList<User>();
    	try {
			Statement stmt=(Statement)this.coon.createStatement();
			ResultSet rs=(ResultSet)stmt.executeQuery(sql);
			while(rs.next()) {
					int id = rs.getInt("id");
					String account  = rs.getString("account");
					String pwd = rs.getString("pwd");
					String name = rs.getString("name");
					String email = rs.getString("email");
					String phone = rs.getString("phone");
					String wechat_open_id = rs.getString("wechat_open_id");
					Date last_login_time = rs.getDate("last_login_time");
					int status = rs.getInt("status");
					Date create_time = rs.getDate("create_time");
					
					User user = new User();
					user.setId(id);
					user.setAccount(account);
					user.setPwd(pwd);
					user.setName(name);
					user.setEmail(email);
					user.setPhone(phone);
					user.setWechat_open_id(wechat_open_id);
					user.setLast_login_time(last_login_time);
					user.setStatus(status);
					user.setCreate_time(create_time);

					users.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
    	
    }
    
   
	public static void main(String[] args) throws Exception {
		
		OperateMysql o = new OperateMysql();
		ArrayList<User> list = o.select();
		for (User user : list) {
			System.out.println(user.getId());
		}
		o.close();
		
	}

}

