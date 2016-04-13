package com.lge.exapandablerecyclerview.model;

import java.util.ArrayList;

/**
 * Created by wlsdud.choi on 2016-04-08.
 */
public class ParentItem extends Item {

    public boolean visibilityOfChildItems = true;
    public ArrayList<ChildItem> unvisibleChildItems = new ArrayList<>();

    public ParentItem(String name, int viewType){
        this.name = name;
        this.viewType = viewType;
    }

}
