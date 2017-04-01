package com.android.lvxin.ripplebutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    CircularRippleButton rippleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rippleButton = (CircularRippleButton) findViewById(R.id.btn_ripple);

        rippleButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "onTouch: ACTION_DOWN");
                        rippleButton.startRipple();
                        return true; // 这里一定要返回true，否则ACTION_DOWN之后的action都不会被执行
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "onTouch: ACTION_UP");
                        rippleButton.stopRipple();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
    }

}
