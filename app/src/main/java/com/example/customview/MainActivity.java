package com.example.customview;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Progress.ProgressCallback {

    /*Inspiration : https://creativemarket.com/CreativeForm/1624699-Music-UIUX-Mobile-App-Kit?u=Perfect-Design */

    private Handler mHandler;
    private boolean callbackHandler = false;
    private Progress progress;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**Fullscreen**/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);



        progress = findViewById(R.id.progress);
        progress.setProgressCallback(this);


        mHandler = new Handler();
        callbackHandler = true;


        mp = new MediaPlayer();

        try {
            /* Music : https://www.auboutdufil.com/index.php?id=504*/
            AssetFileDescriptor descriptor = getAssets().openFd("Meydn.mp3");
            mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            //mp.setDataSource("https://r4---sn-n4g-jqbe7.googlevideo.com/videoplayback?expire=1562097789&ei=HGQbXZL5PIG6xgL7rqn4CA&ip=188.123.75.17&id=o-AIunjwijgmOVtUDQprXY06VStk4g7p39tPwOQjn5re4d&itag=251&source=youtube&requiressl=yes&mm=31%2C26&mn=sn-n4g-jqbe7%2Csn-aigzrn7d&ms=au%2Conr&mv=m&mvi=3&pl=19&initcwndbps=886250&mime=audio%2Fwebm&gir=yes&clen=3560501&dur=216.541&lmt=1560468137319781&mt=1562076021&fvip=4&keepalive=yes&c=WEB&txp=3531432&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRQIgIA1M4hGbtI0GVgJ2qDo1L6L0Pnjh9qK1mEgQzjc-d-8CIQC4VB5_TzKZTEHdiAmxJ4xwCO0voz79gZ9P63fmcfN8SQ%3D%3D&ratebypass=yes&sig=ALgxI2wwRQIhAIrF_YJpCb5fhkhs2tsKS1CPwKwTUVpE6aTLREDFadozAiBu8_H406QEwAtc4yx3oDBI0qxip9jqB1vtc5p7ECJv4g%3D%3D");//Write your location here
            mp.prepare();
            mStatusChecker.run();
            mp.start();


            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mHandler.removeCallbacks(mStatusChecker);
                    callbackHandler = false;
                }
            });

            mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    mp.start();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        /*

        new Thread(new Runnable() {
            public void run() {

                for(float i = 0; i < 100; i+=0.01)
                {
                    progress.setProgress(i);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        */

    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                double prog = (double) mp.getCurrentPosition()/mp.getDuration()*100;
                System.out.println("prog" + prog);

                progress.setProgress((float) prog);
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, 100);
            }
        }
    };


    /*Implemented method from Progress*/
    @Override
    public void onStartSeeking() {
        mp.pause();
    }

    @Override
    public void onStopSeeking(float newProgress) {

        if(!callbackHandler)
        {
            mStatusChecker.run();
        }
        float NewPosition = newProgress * mp.getDuration() / 100;

        System.out.println("NewPosition : " + NewPosition + " /  " + mp.getDuration());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mp.seekTo((int) NewPosition,MediaPlayer.SEEK_CLOSEST);
        }
        else
        {
            mp.seekTo((int) NewPosition);
        }

    }
}
