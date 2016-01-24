package xyz.dongxiaoxia.db.manager;

import java.sql.*;
import java.util.HashMap;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class DBOperation {
    private String poolName;
    private Connection conn = null;

    public DBOperation(String poolName) {
        this.poolName = poolName;
    }

    public void close() {
        try {
            if (this.conn != null) {
                this.conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void open() throws SQLException {
        close();
        this.conn = DBManager.getDBManager().getConnection(this.poolName);
    }

    private PreparedStatement setPres(String sql, HashMap<Integer, Object> params) throws SQLException, ClassNotFoundException {
        if (null == params || params.size() < 1) {
            return null;
        }
        PreparedStatement pres = this.conn.prepareStatement(sql);
        for (int i = 1; i <= params.size(); i++) {
            if (params.get(i) == null) {
                pres.setString(i, "");
            } else if (params.get(i).getClass() == Class.forName("java.lang.String")) {
                pres.setString(i, params.get(i).toString());
            } else if (params.get(i).getClass() == Class.forName("java.lang.Integer")) {
                pres.setInt(i, (Integer) params.get(i));
            } else if (params.get(i).getClass() == Class.forName("java.lang.Long")) {
                pres.setLong(i, (Long) params.get(i));
            } else if (params.get(i).getClass() == Class.forName("java.lang.Double")) {
                pres.setDouble(i, (Double) params.get(i));
            } else if (params.get(i).getClass() == Class.forName("java.lang.Float")) {
                pres.setFloat(i, (Float) params.get(i));
            } else if (params.get(i).getClass() == Class.forName("java.lang.Boolean")) {
                pres.setBoolean(i, (Boolean) params.get(i));
            } else if (params.get(i).getClass() == Class.forName("java.sql.Date")) {
                pres.setDate(i, (Date) params.get(i));
            } else {
                return null;
            }
        }
        return pres;
    }

    public int executeUpdate(String sql) throws SQLException {
        this.open();
        Statement statement = this.conn.createStatement();
        return statement.executeUpdate(sql);
    }

    public int executeUpdate(String sql, HashMap<Integer, Object> params) throws SQLException, ClassNotFoundException {
        this.open();
        PreparedStatement pres = setPres(sql, params);
        if (null == pres) {
            return 0;
        }
        return pres.executeUpdate();
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        this.open();
        Statement statement = this.conn.createStatement();
        return statement.executeQuery(sql);
    }

    public ResultSet executeQuery(String sql, HashMap<Integer, Object> params) throws SQLException, ClassNotFoundException {
        this.open();
        PreparedStatement pres = setPres(sql, params);
        if (null == pres) {
            return null;
        }
        return pres.executeQuery();
    }
}
