package com.hinj.app.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hinj.app.activity.R;
import com.hinj.app.bean.FileInfo;
import com.hinj.app.bean.FileItem;
import com.hinj.app.utils.AsyncLoadImage;

public class FileListAdapter extends BaseAdapter implements OnCheckedChangeListener{
    private static final String TAG = "FileListAdapter";
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<FileInfo> allFileInfos;
    private AsyncLoadImage asyncLoadImage;
    private HashSet<FileInfo> selectedFileInfos;

    public FileListAdapter(Context context, ArrayList<FileInfo> allFileInfos,
            Handler handler, HashSet<FileInfo> selectedFileInfos) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.allFileInfos = allFileInfos;
        this.asyncLoadImage = new AsyncLoadImage(handler);
        this.selectedFileInfos = selectedFileInfos;
    }

    public void bindData(ArrayList<FileInfo> allFileInfos) {
        this.allFileInfos = allFileInfos;
        this.notifyDataSetChanged();
    }
    

    public AsyncLoadImage getAsyncLoadImage() {
        return asyncLoadImage;
    }

    @Override
    public int getCount() {
        return this.allFileInfos != null ? this.allFileInfos.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (this.allFileInfos != null) ? this.allFileInfos.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileItem item = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.file_item, parent, false);
            item = new FileItem();
            item.fileIcon = (ImageView) convertView.findViewById(R.id.file_icon);
            item.fileName = (TextView) convertView.findViewById(R.id.file_name);
            item.fileChecked = (CheckBox) convertView.findViewById(R.id.file_check);
            convertView.setTag(item);
        } else {
            item = (FileItem) convertView.getTag();
        }
        FileInfo info = this.allFileInfos.get(position);
        File file = info.getFile();
        item.fileName.setText(file.getName());
        item.fileChecked.setChecked(info.isSelected());
        item.fileChecked.setTag(info);
        item.fileChecked.setOnCheckedChangeListener(this);
        item.fileIcon.setTag(file.getAbsolutePath());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files!=null && files.length>0) {
                item.fileIcon.setImageResource(R.drawable.folder_);
            } else {
                item.fileIcon.setImageResource(R.drawable.folder);
            }
        } else {
            String name = file.getName().toLowerCase();
            if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg")
                    || name.endsWith(".bmp")) {
                asyncLoadImage.loadImage(item.fileIcon, file.getAbsolutePath());
            } else if (name.endsWith(".txt")) {
                item.fileIcon.setImageResource(R.drawable.text);
            } else if (name.endsWith(".chm")) {
                item.fileIcon.setImageResource(R.drawable.chm);
            } else if (name.endsWith(".html") || name.endsWith(".xml") || name.endsWith(".htm")) {
                item.fileIcon.setImageResource(R.drawable.html);
            } else if (name.endsWith(".mp4") || name.endsWith(".3gp") || name.endsWith(".wmv")
                    || name.endsWith(".rm")) {
                item.fileIcon.setImageResource(R.drawable.format_media);
            } else if (name.endsWith(".mp3") || name.endsWith(".wma") || name.endsWith(".ape")) {
                item.fileIcon.setImageResource(R.drawable.format_music);
            } else if (name.endsWith(".xls")) {
                item.fileIcon.setImageResource(R.drawable.excel);
            } else if (name.endsWith(".apk")) {
                asyncLoadImage.loadApkIcon(this.context, item.fileIcon, file.getAbsolutePath());
            } else {
                item.fileIcon.setImageResource(R.drawable.file);
            }
        }
        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.v(TAG, "check changed = " + buttonView);
        FileInfo fileInfo = (FileInfo) buttonView.getTag();
        if (isChecked) {
            this.selectedFileInfos.add(fileInfo);
        } else {
            this.selectedFileInfos.remove(fileInfo);
        }
        
    }

}
