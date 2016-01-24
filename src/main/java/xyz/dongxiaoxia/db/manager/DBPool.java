package xyz.dongxiaoxia.db.manager;

import xyz.dongxiaoxia.util.ClassUtil;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class DBPool {
    private static String poolPath;

    private DBPool(){
    }

    public static DBPool getDBPool(){
        return DBPoolDao.dbPool;
    }

    private static class DBPoolDao{
        private static DBPool dbPool = new DBPool();
    }

    public String getPoolPath(){
        if (poolPath == null){
            poolPath = ClassUtil.getClassRootPath(DBPool.class) + "proxool.xml";
        }
        return poolPath;
    }

}
