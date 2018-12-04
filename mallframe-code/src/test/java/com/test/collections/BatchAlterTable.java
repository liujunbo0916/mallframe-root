package com.test.collections;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import com.easaa.core.util.FtpUtil;

public class BatchAlterTable {

	
	public static void main(String[] args) {
		
		
		//获取所有表
		/*Connection conn = null;

		try {
		    conn = DbUtilsTool.openConn("MySQL", "39.108.217.96", "3306", "db_travel", "root", "root");
		    String sql = "show tables";
		    QueryRunner qr = new QueryRunner();
		    List<String> tblNameList= (List<String>) qr.query(conn, sql, new ColumnListHandler(1));
		    sql = "ALTER DATABASE db_travel CHARACTER SET `utf8mb4` COLLATE `utf8mb4_general_ci`";
		    qr.update(conn,sql);
		    for (String str:tblNameList)
		    {
		        sql = "ALTER TABLE "+str+" CONVERT TO CHARACTER SET `utf8mb4` COLLATE `utf8mb4_general_ci`";
		        qr.update(conn,sql);
		    }
		}
		catch (Exception e)
		{
		            e.printStackTrace();
		            throw new RuntimeException(e);
		}
		finally {
		    if(conn!=null) {
		        try {
		            conn.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		   }
		}*/
		
		//测试ftp上传图片
		File file = new File("D://favicon.jpg");
		try {
			FtpUtil.upload(file, "test", "tt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
}
