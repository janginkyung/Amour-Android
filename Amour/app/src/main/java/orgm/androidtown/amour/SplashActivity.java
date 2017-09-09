package orgm.androidtown.amour;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by InKyung on 2017-08-15.
 */

public class SplashActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getPreferences()==null)
                    startActivity(new Intent(SplashActivity.this,SignupActivity.class));
                else
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        },3000);
    }

    // 값 불러오기
    private String getPreferences(){
        SharedPreferences pref = getSharedPreferences("GoogleLogin", MODE_PRIVATE);
        return (pref.getString("hi", ""));
    }

}
