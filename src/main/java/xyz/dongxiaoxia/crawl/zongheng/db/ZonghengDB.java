package xyz.dongxiaoxia.crawl.zongheng.db;

import xyz.dongxiaoxia.crawl.zongheng.model.CrawlListInfo;
import xyz.dongxiaoxia.crawl.zongheng.model.NovelChapterModel;
import xyz.dongxiaoxia.crawl.zongheng.model.NovelInfoModel;
import xyz.dongxiaoxia.crawl.zongheng.model.NovelReadModel;
import xyz.dongxiaoxia.db.manager.DBServer;
import xyz.dongxiaoxia.util.JsonUtil;
import xyz.dongxiaoxia.util.ParseMD5;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dongxiaoxia on 2016/1/24.
 */
public class ZonghengDB {
    private static final String POOLNAME = "proxool.hellolucene";

    public List<CrawlListInfo> getCrawlListInfos() {
        List<CrawlListInfo> infos = new ArrayList<CrawlListInfo>();
        DBServer dbServer = new DBServer(POOLNAME);
        try {
            String sql = "select * from crawllist where state = 1";
            ResultSet rs = dbServer.select(sql);
            while (rs.next()) {
                CrawlListInfo info = new CrawlListInfo();
                infos.add(info);
                info.setUrl(rs.getString("url"));
                info.setInfo(rs.getString("info"));
                info.setFrequency(rs.getInt("frequency"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbServer.close();
        }
        return infos;
    }


    public void updateInfoState(String id, int state) {
        DBServer dbServer = new DBServer(POOLNAME);
        try {
            String sql = "update novelinfo set state = " + state + " where id = '" + id + "'";
            dbServer.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbServer.close();
        }
    }

    private boolean hasNovelInfo(String id) {
        DBServer dbServer = new DBServer(POOLNAME);
        try {
            String sql = "select sum(1) as count from novelinfo where id = '" + id + "'";
            ResultSet rs = dbServer.select(sql);
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbServer.close();
        }
        return false;
    }

    public void saveInfoUrls(List<String> urls) {
        if (urls == null || urls.size() < 1) {
            return;
        }
        for (String url : urls) {
            String id = ParseMD5.parseStrToMD5(url);
            if (hasNovelInfo(id)) {
                updateInfoState(id, 1);
            } else {
                insertInfoUrl(id, url);
            }
        }
    }

    public void updateNovelInfo(NovelInfoModel bean) {
        if (bean == null) {
            return;
        }
        DBServer dbServer = new DBServer(POOLNAME);
        try {
            HashMap<Integer, Object> params = new HashMap<Integer, Object>();
            int i = 1;
            params.put(i++, bean.getName());
            params.put(i++, bean.getAuthor());
            params.put(i++, bean.getDesc());
            params.put(i++, bean.getType());
            params.put(i++, bean.getLastChapter());
            params.put(i++, bean.getChapterListUrl());
            params.put(i++, bean.getWordCount());
            params.put(i++, bean.getKeyWords());
            long now = System.currentTimeMillis();
            params.put(i++, now);
            params.put(i++, "0");
            String columns = "name,author,description,type,lastchapter,chapterlisturl,wordcount,keywords,updatetime,state";
            String condition = "where id = '" + bean.getId() + "'";
            dbServer.update("novelinfo", columns, condition, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbServer.close();
        }
    }

    public void saveNovelChapter(List<NovelChapterModel> beans, String novelId) {
        if (beans == null) {
            return;
        }
        for (NovelChapterModel novelChapterModel : beans) {
            if (!hasNovelChapter(novelChapterModel.getId())) {
                insertNovelChapter(novelChapterModel, novelId);
            }
        }
    }

    public void updateChapterState(String id, int state) {
        DBServer dbServer = new DBServer(POOLNAME);
        try {
            String sql = "update novelchapter set state = " + state + " where id = '" + id + "'";
            dbServer.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbServer.close();
        }

    }

    public String getRandIntroPageUrl(int state) {
        DBServer dbServer = new DBServer(POOLNAME);
        try {
            String sql = "select * from novelinfo where state = '" + state + "' order by rand() limit 1";
            ResultSet rs = dbServer.select(sql);
            while (rs.next()) {
                return rs.getString("url");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbServer.close();
        }
        return null;
    }

    public NovelChapterModel getRandChapter(int state) {
        DBServer dbServer = new DBServer(POOLNAME);
        try {
            String sql = "select * from novelchapter where state = '" + state + "' order by rand() limit 1";
            ResultSet rs = dbServer.select(sql);
            while (rs.next()) {
                NovelChapterModel bean = new NovelChapterModel();
                bean.setId(rs.getString("id"));
                bean.setUrl(rs.getString("url"));
                bean.setTitle(rs.getString("title"));
                bean.setChapterId(rs.getInt("chapterid"));
                bean.setChapterTime(rs.getLong("chaptertime"));
                return bean;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbServer.close();
        }
        return null;
    }

    public void saveNovelRead(NovelReadModel bean) {
        if (bean == null) {
            return;
        }
        DBServer dbServer = new DBServer(POOLNAME);
        try {
            if (hasNoveRead(bean.getId())) {
                return;
            }
            HashMap<Integer, Object> params = new HashMap<Integer, Object>();
            int i = 1;
            params.put(i++, bean.getId());
            params.put(i++, bean.getUrl());
            params.put(i++, bean.getTitle());
            params.put(i++, bean.getWordCount());
            params.put(i++, bean.getChapterId());
            params.put(i++, bean.getContent());
            params.put(i++, bean.getChapterTime());
            long now = System.currentTimeMillis();
            params.put(i++, now);
            params.put(i++, now);
            String columns = "id,url,title,wordcount,chapterid,content,chaptertime,createtime,updatetime";
            dbServer.insert("novelchapterdetail", columns, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbServer.close();
        }
    }

    private boolean hasNoveRead(String id) {
        DBServer dbServer = new DBServer(POOLNAME);
        try {
            String sql = "select sum(1) as count from novelchapterdetail where id = '" + id + "'";
            ResultSet rs = dbServer.select(sql);
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbServer.close();
        }
        return false;
    }

    private boolean hasNovelChapter(String id) {
        DBServer dbServer = new DBServer(POOLNAME);
        try {
            String sql = "select sum(1) as count from novelchapter where id = '" + id + "'";
            ResultSet rs = dbServer.select(sql);
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbServer.close();
        }
        return false;
    }

    private void insertNovelChapter(NovelChapterModel bean, String novelId) {
        if (bean == null) {
            return;
        }
        DBServer dbServer = new DBServer(POOLNAME);
        try {
            HashMap<Integer, Object> params = new HashMap<Integer, Object>();
            int i = 1;
            params.put(i++, bean.getId());
            params.put(i++, novelId);
            params.put(i++, bean.getUrl());
            params.put(i++, bean.getTitle());
            params.put(i++, bean.getWordCount());
            params.put(i++, bean.getChapterId());
            params.put(i++, bean.getChapterTime());
            long now = System.currentTimeMillis();
            params.put(i++, now);
            params.put(i++, "1");
            String columns = "id,novelid,url,title,wordcount,chapterid,chaptertime,createtime,state";
            dbServer.insert("novelchapter", columns, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbServer.close();
        }
    }

    private void insertInfoUrl(String id, String url) {
        DBServer dbServer = new DBServer(POOLNAME);
        try {
            HashMap<Integer, Object> params = new HashMap<Integer, Object>();
            int i = 1;
            params.put(i++, id);
            params.put(i++, url);
            long now = System.currentTimeMillis();
            params.put(i++, now);
            params.put(i++, now);
            params.put(i++, 1);
            dbServer.insert("novelinfo", "id,url,createtime,updatetime,state", params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbServer.close();
        }
    }

    public void method() {
        DBServer dbServer = new DBServer(POOLNAME);
        try {

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbServer.close();
        }
    }

    public static void main(String[] args) {
        ZonghengDB db = new ZonghengDB();
        System.out.println(JsonUtil.parseJson(db.getCrawlListInfos()));
        System.out.println(db.getRandIntroPageUrl(1));
        System.out.println(JsonUtil.parseJson(db.getRandChapter(1)));
    }
}
