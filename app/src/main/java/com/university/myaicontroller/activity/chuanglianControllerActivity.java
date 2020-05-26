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

public class chuanglianControllerActivity extends AppCompatActivity
{
    Button btn_start_recognizer;
    Button btn_chaunglian;
    Button back_btn;
    TextView tv_text;
    ImageView iv_item_image;
    AutoSpeechRecognizer recognizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuanglian);
        //初始化元件
        btn_start_recognizer = findViewById(R.id.chuanglian_sound_control);
        back_btn = findViewById(R.id.back_btn);
        tv_text = findViewById(R.id.tv_text);
        iv_item_image = findViewById(R.id.iv_item_image);
        btn_chaunglian = findViewById(R.id.chuanglian_statues);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
        }, 1);

        //设置按钮也可以开关电器
        btn_chaunglian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_chaunglian.getText().equals("窗帘已打开"))
                {
                    btn_chaunglian.setText("窗帘已关闭");
                }else {
                    btn_chaunglian.setText("窗帘已打开");
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
                                    if(result.contains("打开窗帘")){
                                        btn_chaunglian.setText("窗帘已打开");
                                    }else if(result.contains("关闭窗帘")){
                                        btn_chaunglian.setText("窗帘已关闭");
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
