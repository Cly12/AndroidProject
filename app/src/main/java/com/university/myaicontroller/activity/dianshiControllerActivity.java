package com.university.myaicontroller.activity;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.alibaba.idst.nls.NlsListener;
import com.alibaba.idst.nls.StageListener;
import com.university.myaicontroller.R;
import com.university.myaicontroller.util.AutoSpeechRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class dianshiControllerActivity extends AppCompatActivity
{
    Button btn_start_recognizer;
    Button btn_dianshi;
    Button back_btn;
    TextView tv_text;
    AutoSpeechRecognizer recognizer;
    TextView tv_pindao;
    TextView tv_yinliang;
    Button addPindao;
    Button delPindao;
    Button addYinliang;
    Button delYinliang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dianshi);
        //初始化元件
        btn_start_recognizer = findViewById(R.id.dianshi_sound_control);
        back_btn = findViewById(R.id.back_btn);
        tv_text = findViewById(R.id.tv_text);
        btn_dianshi = findViewById(R.id.dianshi_statues);
        tv_pindao = findViewById(R.id.tv_pindao);
        tv_yinliang = findViewById(R.id.tv_yinliang);
        addPindao = findViewById(R.id.dianshi_pindao_add_bt);
        delPindao = findViewById(R.id.dianshi_pindao_min_bt);
        addYinliang = findViewById(R.id.dianshi_yinliang_add_bt);
        delYinliang = findViewById(R.id.dianshi_yinliang_min_bt);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
        }, 1);

        //设置增加减少音量音频按钮事件
        addPindao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //电视打开的情况下才能调节音量等
                if(btn_dianshi.getText().equals("电视已打开"))
                {
                    String result = tv_pindao.getText().toString();
                    //获取字符串中的数字
                    String regEx="[^0-9]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(result);
                    int num = Integer.parseInt(m.replaceAll("").trim());
                    tv_pindao.setText("频道" + (num + 1));
                }
            }
        });

        delPindao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //电视打开的情况下才能调节音量等
                if(btn_dianshi.getText().equals("电视已打开"))
                {
                    String result = tv_pindao.getText().toString();
                    //获取字符串中的数字
                    String regEx="[^0-9]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(result);
                    int num = Integer.parseInt(m.replaceAll("").trim());
                    if (num > 1){
                        tv_pindao.setText("频道" + (num - 1));
                    }
                }
            }
        });

        addYinliang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //电视打开的情况下才能调节音量等
                if(btn_dianshi.getText().equals("电视已打开"))
                {
                    int num = Integer.parseInt(tv_yinliang.getText().toString());
                    if (num < 10 ){
                        tv_yinliang.setText(String.valueOf(num + 1));
                    }
                }
            }
        });

        delYinliang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //电视打开的情况下才能调节音量等
                if(btn_dianshi.getText().equals("电视已打开"))
                {
                    int num = Integer.parseInt(tv_yinliang.getText().toString());
                    if (num > 1){
                        tv_yinliang.setText(String.valueOf(num - 1));
                    }
                }
            }
        });

        //设置按钮也可以开关电器
        btn_dianshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_dianshi.getText().equals("电视已打开"))
                {
                    btn_dianshi.setText("电视已关闭");
                }else {
                    btn_dianshi.setText("电视已打开");
                }

            }
        });

        //返回按钮设置监听事件
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //语音按钮设置监听事件
        recognizer = getRecognizer(this);
        btn_start_recognizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recognizer.isRecognizing()) {
                    tv_text.setText("语音输入");
                    recognizer.stopRecognize();
                }
                else {
                    tv_text.setText("识别中...点击停止");
                    recognizer.startRecognize();
                }
            }
        });

    }

    public AutoSpeechRecognizer getRecognizer(final Activity activity) {
        if (recognizer == null) {
            recognizer = new AutoSpeechRecognizer(
                    this,
                    new NlsListener() {
                        @Override
                        public void onRecognizingResult(int i, RecognizedResult recognizedResult) {
                            if (i == 0) {
                                try {
                                    JSONObject jsonObject = new JSONObject(recognizedResult.asr_out);
                                    // 识别出来的的字符串。若要做修改，请在这改
                                    String result = jsonObject.getString("result");

                                    if(result.contains("打开电视")){
                                        btn_dianshi.setText("电视已打开");
                                    }else if(result.contains("关闭电视")){
                                        btn_dianshi.setText("电视已关闭");
                                    }
                                    //电视打开的情况下才能调节音量等
                                    if(btn_dianshi.getText().equals("电视已打开")){
                                        int num = 0;
                                        //获取字符串中的数字
                                        String regEx="[^0-9]";
                                        Pattern p = Pattern.compile(regEx);
                                        Matcher m = p.matcher(result);
                                        if(!m.replaceAll("").trim().isEmpty()){
                                            num = Integer.parseInt(m.replaceAll("").trim());
                                        }

                                        if(result.contains("打开频道")){  //打开频道5
                                            tv_pindao.setText("频道"+ num);
                                        }else if(result.contains("下一个频道")){
                                            tv_pindao.setText("频道"+ (num + 1));
                                        }else if(result.contains("上一个频道")){
                                            //获取字符串中的数字
                                            m = p.matcher(tv_pindao.getText().toString());
                                            if(!m.replaceAll("").trim().isEmpty()){
                                                num = Integer.parseInt(m.replaceAll("").trim());
                                            }
                                            if (num != 0)
                                            {
                                                tv_pindao.setText("频道"+ (num - 1));
                                            }
                                        }
                                        if(result.contains("增大音量")){
                                            num = Integer.parseInt(tv_yinliang.getText().toString());
                                            if(num < 10){
                                                tv_yinliang.setText(String.valueOf(num + 1));
                                            }
                                        }else if(result.contains("减小音量")){
                                            num = Integer.parseInt(tv_yinliang.getText().toString());
                                            if(num > 0){
                                                tv_yinliang.setText(String.valueOf(num - 1));
                                            }
                                        }else if(result.contains("音量")){  //音量调至2
                                            m = p.matcher(tv_pindao.getText().toString());
                                            if(!m.replaceAll("").trim().isEmpty()){
                                                num = Integer.parseInt(m.replaceAll("").trim());
                                            }
                                            if (num <= 10 && num >=0){
                                                tv_yinliang.setText(String.valueOf(num));
                                            }
                                        }
                                    }
                                    Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
                                    //sendRecorder(tmp);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                Log.e("ASRError", "onRecognizingResult: error: " + i);
                            }
                        }
                    },
                    new StageListener() {
                        @Override
                        public void onVoiceVolume(int i) {

                        }
                    });
        }
        return recognizer;
    }
}
