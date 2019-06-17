package com.example.fightwith;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;


import com.bumptech.glide.Glide;
import com.example.fightwith.Dapter.MyAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    private ListView list_view;
    MyAdapter adapter;
    private Spinner spinner;
    private Button btn_time;
    private Banner banner;
    private ArrayList<String> pt_file;


    private ArrayList<String> image_path;
    private ArrayList<Integer> image_path1;

    List<Map<String,Object>> list = new ArrayList<>();

    /**
     * 线程接收参数msg
     * msg.what 标识
     * */
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 001:
                    try {
                        Map<String, Object> map = null;
                        JSONObject person = new JSONObject(String.valueOf(msg.obj));
                        JSONObject data = new JSONObject(String.valueOf(person.get("UserInfo")));
                        JSONObject getList = new JSONObject(String.valueOf(data.get("data")));
                        JSONArray dataList = getList.getJSONArray("MatchList");

                        list.clear();
                        for (int index = 0; index < dataList.length(); index++) {
                            JSONObject OBJ = (JSONObject) dataList.get(index);
                            int id = OBJ.getInt("id");
                            String date = OBJ.getString("date");
                            String over = OBJ.getString("Result");
                            String name1 = OBJ.getString("name1");
                            String name1res = OBJ.getString("name1res");
                            String name1con = OBJ.getString("name1con");
                            String name2 = OBJ.getString("name2");
                            String name2res = OBJ.getString("name2res");
                            String name2con = OBJ.getString("name2con");
                            String color = OBJ.getString("color");

                            map = new HashMap<String, Object>();
                            map.put("time", date);
                            map.put("over", over);
                            map.put("name1", name1);
                            map.put("icon1", name1con);
                            map.put("res1", name1res);
                            map.put("name2", name2);
                            map.put("res2", name2res);
                            map.put("icon2", name2con);
                            map.put("bgcolor", color);
                            if (spinner.getSelectedItem().equals(over)) {
                                list.add(map);
                                adapter.setList(list);
                                list_view.setAdapter(adapter);
                            }else if(spinner.getSelectedItem().equals("全部")){
                                list.add(map);
                                adapter.setList(list);
                                list_view.setAdapter(adapter);
                            }
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                case 002:
                     try{

                     }catch (Exception e){
                         e.printStackTrace();
                     }
            }
        }
    };

    //日历点击
    private void  showDatePickDlg(){
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //主线程/UI线程
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        showDatePickDlg();
        adapter = new MyAdapter(this);
        listSpinner();
        getImgByUrl();
        initImg();
    }
    /**
     * 多线程访问json
     * 返回mgs.obj 和标识mgs.what
     * byte[] buffer 生成字符串
     * */
    private void sendRequestConn(){
        new Thread(){
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                try {
                    URL url = new URL("https://www.wulingshan.club/FightWith/json/jsonData.json");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setConnectTimeout(3000);
                    conn.setReadTimeout(3000);
                    conn.setRequestMethod("GET");
                    InputStream in = conn.getInputStream();

                    byte[] buffer=new byte[10240];
                    int len=in.read(buffer,0,1024);
                        while (len!=-1){
                            String s = new String(buffer,0,1024);
                            sb.append(s);
                            len=in.read(buffer,0,1024);
                        }
                        JSONObject data = new JSONObject(sb.toString());
                        Message mgs = new Message();
                        mgs.obj = data;
                        mgs.what = 001;
                        handler.sendMessage(mgs);
                        if (in!=null){
                            in.close();
                        }
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /**
     * 绑定视图findId
     * */
    private void init (){

        spinner=findViewById(R.id.spinner_tv);
        list_view= findViewById(R.id.list_view);
        btn_time = findViewById(R.id.time_btn);
        banner = findViewById(R.id.banner);
        DateFormat df = new SimpleDateFormat("MM-dd");
        btn_time.setText(df.format(new Date()));
        list_view.setOnItemLongClickListener(this);
        list_view.setOnItemClickListener(this);
    }
    /**
     * 图片轮播
     * */
    private void initImg(){
            image_path1 = new ArrayList<>();
            image_path1.add(R.mipmap.over02);
            image_path1.add(R.mipmap.over03);
            image_path1.add(R.mipmap.over04);

            banner.setImageLoader(new MyImageLoader());
            banner.setBannerAnimation(Transformer.Default);
            //设置轮播间隔时间
            banner.setDelayTime(3000);
            //设置是否为自动轮播，默认是true
            banner.isAutoPlay(true);
            //设置指示器的位置，小点点，居中显示
            banner.setIndicatorGravity(BannerConfig.CENTER);
            banner.setImages(image_path1).start();
    }
    //带参跳转界面
    private void skipIntent(String data){

        Intent intent= new Intent(MainActivity.this,ruleActivity.class);
        //创建Bundle携带数据
         Bundle bundle = new Bundle();
        //键值对传参
        bundle.putString("data", data);
        intent.putExtras(bundle);
        //跳转
        startActivity(intent);
    }
    //点击当前选中的 对战
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        skipIntent(list.get(position).get("time").toString());
        //结束当前界面
    }
    //长按钮当前选中的 对战
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,"长按了"+view,Toast.LENGTH_SHORT).show();
        return true;
    }
    /**
     * 图片加载器
     * */
    private class MyImageLoader extends ImageLoader{
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);
        }
    }
    /**
     * 监听Spinner
     * */
    private void listSpinner(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            //单击选中的spinner
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sendRequestConn();
            }

            //适配器为空时
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    /**
     * 通过网络地址获取网络图片,将图片保存到本地
     * */
    public Bitmap GetImageInputStream(String imgUrl){
        URL url;
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try{
            url = new URL(imgUrl);
            connection=(HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(13000);//超时设置
            connection.setDoInput(true);
            connection.setUseCaches(false);//当前不用缓存
            InputStream inputStream=connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    /**
     * 保存位图到本地
     * @param bitmap
     * @param path 本地路径
     * @return void
     */
    public void SavaImage(Bitmap bitmap, String path,String ptFile){
        File file=new File(path);
        FileOutputStream fileOutputStream=null;
        file.delete();
         //文件夹不存在，创建它
        if(!file.exists()){
            System.out.println(file.delete());
            file.mkdir();
        }
        try {
            fileOutputStream=new FileOutputStream(path+"/"+ptFile+".jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *使用线程下载图片
     * */
    private void getImgByUrl(){
        //图片url
        image_path = new ArrayList<>();
        image_path.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3726070038,2578664885&fm=27&gp=0");
        image_path.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=4210520771,88908682&fm=27&gp=0");
        image_path.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=574634891,1208986072&fm=27&gp=0");
        image_path.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2099334565,439795590&fm=27&gp=0");
        //图片命名覆盖原来的图片 防止重复
        pt_file = new ArrayList<>();
        pt_file.add("one");
        pt_file.add("tow");
        pt_file.add("there");
        pt_file.add("four");
        new Thread(){
            @Override
            public void run() {
                for (int index=0;index<image_path.size();index++){
                    Bitmap bitmap = GetImageInputStream(image_path.get(index));
                    SavaImage(bitmap, Environment.getExternalStorageDirectory().getPath()+"/FightWithImg",pt_file.get(index));
                }
           }
        }.start();
    }



}
