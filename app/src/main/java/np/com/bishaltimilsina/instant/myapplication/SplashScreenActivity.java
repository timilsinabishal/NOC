package np.com.bishaltimilsina.instant.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    RotateAnimation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final ImageView imageView=(ImageView) findViewById(R.id.imageViewSplashScreen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              Intent intent=new Intent(SplashScreenActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                animation=new RotateAnimation(0,360);
                animation.setDuration(2000);
                imageView.startAnimation(animation);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
