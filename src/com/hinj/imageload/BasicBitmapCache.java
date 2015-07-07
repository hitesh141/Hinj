package com.hinj.imageload;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.util.Log;


/**
 * Basic implementation of BitmapCache 
 * 
 * @author Ramkailash
 */
public class BasicBitmapCache implements BitmapCache{
    
    private static class CacheEntry {
        public Bitmap data;
        public int nUsed;
        public long timestamp;
    }
    
    
    private static final String TAG = "BasicBitmapCache";
    private static final boolean DEBUG = false;
    
    private int mMaxSize;
    private HashMap<String, CacheEntry> mMap = new HashMap<String, CacheEntry> ();
    

    /**
     * max number of resource this cache contains
     * @param size
     */
    public BasicBitmapCache (int size) {
        this.mMaxSize = size;
    }
    
    
    @Override
    public synchronized boolean exists(String key){
       return mMap.get(key) != null;
    }

    
    @Override
    public synchronized void invalidate(String key){
        CacheEntry e = mMap.get(key);
        Bitmap data = e.data;
        //data.recycle(); // we are only relying on GC to reclaim the memory
        mMap.remove(key);
        if(DEBUG) Log.d(TAG, key + " is invalidated from the cache");
    }

    
    @Override
    public synchronized void clear(){
         for ( String key : mMap.keySet()) {
             invalidate(key);
         }
    }

    
    /**
     * If the cache storage is full, return an item to be removed. 
     * 
     * Default strategy:  oldest out: O(n)
     * 
     * @return item key
     */
    protected synchronized String findItemToInvalidate() {
        Map.Entry<String, CacheEntry> out = null;
        for(Map.Entry<String, CacheEntry> e : mMap.entrySet()){
            if( out == null || e.getValue().timestamp < out.getValue().timestamp) {
                out = e;
            }
        }
        return out.getKey();
    }

    
    @Override
    public synchronized Bitmap loadData(String key) {
        if(!exists(key)) {
            return null;
        }
        CacheEntry res = mMap.get(key);
        res.nUsed++;
        res.timestamp = System.currentTimeMillis();
        return res.data;
    }


    @Override
    public synchronized void storeData(String key, Object data) {
        if(this.exists(key)) {
            return;
        }
        CacheEntry res = new CacheEntry();
        res.nUsed = 1;
        res.timestamp = System.currentTimeMillis();
        res.data = (Bitmap)data;
        
        //if the number exceeds, move an item out 
        //to prevent the storage from increasing indefinitely.
        if(mMap.size() >= mMaxSize) {
            String outkey = this.findItemToInvalidate();
            this.invalidate(outkey);
        }
        
        mMap.put(key, res);
    }

}
