package xyz.dongxiaoxia.hellolucene.model;

/**
 * Created by dongxiaoxia on 2016/1/21.
 *
 * 索引对象模型
 */
public class Document {
    private String name;
    private String path;
    private Long size;
    private String type;
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
