package com.example.username.myslideshow;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //スライドショー用の画像リスト
    ImageSwitcher mImageSwitcher;
    int[] mImageResources = {R.drawable.slide00, R.drawable.slide01
            , R.drawable.slide02, R.drawable.slide03
            , R.drawable.slide04, R.drawable.slide05
            , R.drawable.slide06, R.drawable.slide07
            , R.drawable.slide08, R.drawable.slide09};
    int mPosition = 0;

    //自動でスライドショーを実施するかのフラグ
    boolean mIsSlideshow = false;
    //音楽プレイヤーの宣言
    MediaPlayer mMediaPlayer;

    public class MainTimerTask extends TimerTask {
        @Override
        public void run() {
            if (mIsSlideshow) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        movePosition(1);
                    }
                });
            }
        }
    }

    Timer mTimer = new Timer();
    TimerTask mTimerTask = new MainTimerTask();
    Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///////////////////////////////////////////ここから
        //イメージビューファクトリーの初期化処理
        mImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView =
                        new ImageView(getApplicationContext());
                return imageView;
            }
        });
        //////////////////////////////////////////ここまで

        //スライドショーボタンのオン・オフを見て、時間でスライドの変更をする
        mTimer.schedule(mTimerTask, 0, 5000);

        mMediaPlayer = MediaPlayer.create(this, R.raw.getdown);
        mMediaPlayer.setLooping(true);
    }
    

    private void movePosition(int move) {
        mPosition = mPosition + move;
        if (mPosition >= mImageResources.length) {
            mPosition = 0;
        } else if (mPosition < 0) {
            mPosition = mImageResources.length - 1;
        }
        mImageSwitcher.setImageResource(mImageResources[mPosition]);

    }


    public void onPrevButtonTapped(View view) {
        mImageSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mImageSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        movePosition(-1);
        findViewById(R.id.imageView).animate().setDuration(1000).alpha(0.0f);
    }

    public void onNextButtonTapped(View view) {
        //setInAnimationで画像のフェードイン、フェードアウトを指定
        mImageSwitcher.setInAnimation(this, android.R.anim.slide_in_left);
        mImageSwitcher.setOutAnimation(this, android.R.anim.slide_out_right);
        movePosition(1);
        findViewById(R.id.imageView).animate().setDuration(1000).alpha(0.0f);
    }

    //スライドショーボタンを押したときにtrueとfalseを変える
    public void onSlideshowButtonTapped(View view) {
        mIsSlideshow = !mIsSlideshow;
        if (mIsSlideshow) {
            mMediaPlayer.start();
        } else {
            mMediaPlayer.pause();
            mMediaPlayer.seekTo(0);
        }

    }

}

