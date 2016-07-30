package me.shiyan.mvptest.data.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by shiyan on 2016/7/26.
 */
public class BookDetail extends RealmObject {

    @PrimaryKey
    private int     id;
    private String  name;
    private String  author;
    private int     bookclass;
    private String  img;
    private int     time;
    private String  summary;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getBookclass() {
        return bookclass;
    }

    public void setBookclass(int bookclass) {
        this.bookclass = bookclass;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
