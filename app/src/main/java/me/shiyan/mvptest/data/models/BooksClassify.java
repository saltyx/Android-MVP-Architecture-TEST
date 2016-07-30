package me.shiyan.mvptest.data.models;

import android.support.annotation.Nullable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by shiyan on 2016/7/26.
 */
public class BooksClassify extends RealmObject{
    @PrimaryKey
    private int     id;

    private String  description;

    private String  keywords;

    private String  name;
    private int     seq;

    private String  title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getKeywords() {
        return keywords;
    }

    public void setKeywords( String keywords) {
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
