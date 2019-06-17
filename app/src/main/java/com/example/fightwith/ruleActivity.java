package com.example.fightwith;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;

import android.widget.TextView;
import android.widget.VideoView;


public class ruleActivity extends Activity {
    private static final int VIDEO_LAYOUT_SCALE = 1;
    private VideoView videoView;
    private TextView textView;
    private Button btn;
    private  Bundle bundle;

    private String videoPath="http://cdn.masfight.com/masfight/upload/doc/2019/04/30/1f1496fe5d6945f6854a0ea427ff0b50.mp4";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fightwidth_rule);
        init();
        getData();
        TvListent();
        test();
    }
    /**
     * 获取数据
     * */
    private void getData(){
        //Bundle接收数据
       bundle = this.getIntent().getExtras();
        System.out.println(bundle.get("data"));
        textView.setText(String.valueOf(bundle.get("data")));
    }

    //绑定视图findId
    private void init (){
        videoView = findViewById(R.id.video_view);
        textView=findViewById(R.id.test_tv);
        btn=findViewById(R.id.tv_btn);
    }

    //视频播放
    private void TvListent(){
                //设置视频控制器
                videoView.setMediaController(new MediaController(ruleActivity.this));
                //播放完成回调
                videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());
                videoView.setMinimumHeight(VIDEO_LAYOUT_SCALE);
                //设置视频路径
                videoView.setVideoPath(videoPath);
                //开始播放视频
                    if (bundle.get("data").equals("05-06 16:00")){
                        videoView.start();
                    }else {
                        videoView.setVideoPath("https://v.youku.com/v_show/id_XMjUyNDIxNjAwNA==.html");
                        videoView.start();
                }
    }
    //监听播放视频
    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            //Toast.makeText( ruleActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }
    //测试事件
    private void test() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
