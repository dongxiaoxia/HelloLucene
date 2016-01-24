package xyz.dongxiaoxia.db.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class DBServer {
    private DBOperation dbOperation;
    public DBServer(String poolName){
        dbOperation = new DBOperation(poolName);
    }

    public void close(){
        dbOperation.close();
    }

    public int insert(String sql)throws SQLException{
        return dbOperation.executeUpdate(sql);
    }

    public int insert(String tableName,String columns,HashMap<Integer,Object> params) throws SQLException, ClassNotFoundException {
        String sql = insertSql(tableName,columns,params);
        return dbOperation.executeUpdate(sql,params);
    }

    public int delete(String sql) throws SQLException {
        return  dbOperation.executeUpdate(sql);
    }

    public int delete(String tableName,String condition) throws SQLException {
        if (null == tableName){
            return 0;
        }
        String sql = "delete from " + tableName + " " + condition;
        return dbOperation.executeUpdate(sql);
    }

    public int update(String sql) throws SQLException {
        return dbOperation.executeUpdate(sql);
    }

    public int update(String tableName,String columns,String condition, HashMap<Integer,Object> params) throws SQLException, ClassNotFoundException {
        String sql = updateSql(tableName,columns,condition,params);
        return dbOperation.executeUpdate(sql, params);
    }

    public ResultSet select(String sql) throws SQLException {
        return dbOperation.executeQuery(sql);
    }

    public ResultSet select(String tableName,String columns,String condition) throws SQLException {
        String sql = "select " + columns + " from " + tableName + " " + condition;
        return dbOperation.executeQuery(sql);
    }

    private String updateSql(String tableName, String columns,String condition, HashMap<Integer,Object> params){
        if (tableName == null || columns == null){
            return "";
        }
        String[] column = columns.split(",");
        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(tableName);
        sb.append(" set ");
        sb.append(column[0]);
        sb.append("=?");
        for (int i= 1;i<column.length;i++){
            sb.append(", ");
            sb.append(column[i]);
            sb.append("=?");
        }
        sb.append(" ");
        sb.append(condition);
        return sb.toString();
    }

    private String insertSql(String tableName, String columns, HashMap<Integer, Object> params){
        if (tableName == null || columns == null){
            return "";
        }
        int n = columns.split(",").length;
        StringBuilder sb = new StringBuilder("");
        sb.append("insert into ");
        sb.append(tableName);
        sb.append(" (");
        sb.append(columns);
        sb.append(") values (? ");
        for (int i = 1;i<n;i++){
            sb.append(",?");
        }
        sb.append(")");
        return sb.toString();
    }
}
