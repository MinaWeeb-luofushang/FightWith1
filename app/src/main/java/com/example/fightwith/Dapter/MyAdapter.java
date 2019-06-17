package com.example.fightwith.Dapter;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fightwith.R;

import java.util.List;
import java.util.Map;



public class MyAdapter extends BaseAdapter {


    List<Map<String,Object>> list;
    LayoutInflater inflater;


    public void setList(List<Map<String, Object>> list) {
        this.list=list;
    }

    public MyAdapter (Context context){
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     * position 当前视图的id
     * convertView 上滑动 消失的参数
     * parent 容器
     * view 当前锁定视图
     * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.fightwith_home,null);
            holder = new ViewHolder();
            holder.data_view=(LinearLayout)convertView.findViewById(R.id.data_view);
            holder.time = (TextView)convertView.findViewById(R.id.time_tv);
            holder.over = (TextView)convertView.findViewById(R.id.over_tv);
            holder.name1 = (TextView)convertView.findViewById(R.id.name1_tv);
            holder.img1 = (ImageView)convertView.findViewById(R.id.img_tv1);
            holder.img2 = (ImageView)convertView.findViewById(R.id.img_tv2);
            holder.res1 = (TextView)convertView.findViewById(R.id.res1_tv);
            holder.res2 = (TextView)convertView.findViewById(R.id.res2_tv);
            holder.name2 = (TextView)convertView.findViewById(R.id.name2_tv);
            convertView.setTag(holder);

        }else {
            holder=(ViewHolder) convertView.getTag();
        }

            final Map map = list.get(position);
            holder.time.setText((String) map.get("time"));
            holder.over.setText((String) map.get("over"));
            holder.name1.setText((String) map.get("name1"));
            holder.img1.setTag(map.get("icon1"));
            holder.img2.setTag(map.get("icon2"));


            holder.res1.setText((String) map.get("res1"));
            holder.res2.setText((String) map.get("res2"));
            holder.name2.setText((String) map.get("name2"));
            String id = (String) map.get("bgcolor");

            if(id == String.valueOf(0)){
                holder.data_view.setBackgroundResource(R.color.color0);
            }else if(id==String.valueOf(1)){
                holder.data_view.setBackgroundResource(R.color.color1);
            }else if(id==String.valueOf(2)){
                holder.data_view.setBackgroundResource(R.color.color2);
            }else{
                holder.data_view.setBackgroundResource(R.color.color3);
            }
        return convertView;
    }
    static class ViewHolder{
        LinearLayout data_view;
        TextView time;
        TextView over;
        TextView name1;
        ImageView img1;
        ImageView img2;
        TextView res1;
        TextView res2;
        TextView name2;
    }

}
