package com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.api;


import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Created by Zhuinden on 2016.07.28..
 */
@Root(name = "response")
public class CatsBO {
    @Path("data/images")
    @ElementList(type = CatBO.class, inline = true)
    private ArrayList<CatBO> cats;

    public ArrayList<CatBO> getCats() {
        return cats;
    }

    public void setCats(ArrayList<CatBO> cats) {
        this.cats = cats;
    }
}