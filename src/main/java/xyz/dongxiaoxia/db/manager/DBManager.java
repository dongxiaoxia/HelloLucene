package xyz.dongxiaoxia.db.manager;

import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class DBManager {
    private DBManager(){
        try {
            JAXPConfigurator.configure(DBPool.getDBPool().getPoolPath(), false);
            Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static class DBManagerDao{
        private static DBManager dbManager = new DBManager();
    }

    public Connection getConnection(String poolName) throws SQLException {
      return DriverManager.getConnection(poolName);
    }

    public static DBManager getDBManager(){
        return DBManagerDao.dbManager;
    }
}
