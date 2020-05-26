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

public class kongtiaoControllerActivity extends AppCompatActivity
{
    Button btn_start_recognizer;
    Button btn_kongtiao;
    Button back_btn;
    TextView tv_text;
    ImageView iv_item_image;
    AutoSpeechRecognizer recognizer;
    Button addKongtiao;
    Button delKongtiao;
    TextView tv_wendu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kongtiao);
        //初始化元件
        btn_start_recognizer = findViewById(R.id.kongtiao_sound_control);
        back_btn = findViewById(R.id.back_btn);
        tv_text = findViewById(R.id.tv_text);
        iv_item_image = findViewById(R.id.iv_item_image);
        btn_kongtiao = findViewById(R.id.kongtiao_statues);
        addKongtiao = findViewById(R.id.kongtiao_wendu_add_bt);
        delKongtiao = findViewById(R.id.kongtiao_wendu_min_bt);
        tv_wendu = findViewById(R.id.tv_wendu);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
        }, 1);

        //调整温度按钮事件
        addKongtiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //电视打开的情况下才能调节音量等
                if(btn_kongtiao.getText().equals("空调已打开"))
                {
                    int num = Integer.parseInt(tv_wendu.getText().toString());
                    if (num < 25){
                        tv_wendu.setText(String.valueOf(num + 1));
                    }
                }
            }
        });

        delKongtiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //电视打开的情况下才能调节音量等
                if(btn_kongtiao.getText().equals("空调已打开"))
                {
                    int num = Integer.parseInt(tv_wendu.getText().toString());
                    if (num > 16){
                        tv_wendu.setText(String.valueOf(num - 1));
                    }
                }
            }
        });

        //设置按钮也可以开关电器
        btn_kongtiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_kongtiao.getText().equals("空调已打开"))
                {
                    btn_kongtiao.setText("空调已关闭");
                }else {
                    btn_kongtiao.setText("空调已打开");
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
                                    if(result.contains("打开空调")){
                                        btn_kongtiao.setText("空调已打开");
                                    }else if(result.contains("关闭空调")){
                                        btn_kongtiao.setText("空调已关闭");
                                    }

                                    //电视打开的情况下才能调节音量等
                                    if(btn_kongtiao.getText().equals("空调已打开")){
                                        int num = 0;
                                        //获取字符串中的数字
                                        String regEx="[^0-9]";
                                        Pattern p = Pattern.compile(regEx);
                                        Matcher m = p.matcher(result);
                                        if(!m.replaceAll("").trim().isEmpty()){
                                            num = Integer.parseInt(m.replaceAll("").trim());
                                        }

                                        if(result.contains("增大温度")){
                                            num = Integer.parseInt(tv_wendu.getText().toString());
                                            if(num < 25){
                                                tv_wendu.setText(String.valueOf(num + 1));
                                            }
                                        }else if(result.contains("减小温度")){
                                            num = Integer.parseInt(tv_wendu.getText().toString());
                                            System.out.println("-------------->" + num);
                                            if(num > 16){
                                                tv_wendu.setText(String.valueOf(num - 1));
                                            }
                                        }else if(result.contains("温度")){  //温度调至20
                                            if (num <= 25 && num >=16){
                                                tv_wendu.setText(String.valueOf(num));
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
