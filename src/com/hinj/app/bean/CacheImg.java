package com.hinj.app.bean;

import android.graphics.Bitmap;

public class CacheImg {
    String path;
    Bitmap icon;
    
    public CacheImg(String path, Bitmap icon){
        this.path = path;
        this.icon = icon;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public Bitmap getIcon() {
        return icon;
    }
    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
    
}
