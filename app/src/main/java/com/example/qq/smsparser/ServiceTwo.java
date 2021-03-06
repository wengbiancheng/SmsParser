package com.example.qq.smsparser;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.qq.smsparser.model.parser.SmsService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 守护进程
 */
public class ServiceTwo extends Service {

    public final static String TAG = "TestService";
    private Timer timer = new Timer();

    @Override
    public void onCreate() {
        thread.start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "APP数据重新加载完成");
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    boolean b = MyApplication.isServiceWorked(ServiceTwo.this, "com.example.qq.smsparser.model.parser.SmsService");
                    Log.e(TAG, "SmsService的存活情况是:" + b);
                    if (!b) {
                        Intent service = new Intent(ServiceTwo.this, SmsService.class);
                        startService(service);
                    }
                }
            };
            timer.schedule(task, 0, 10 * 60 * 1000);
        }
    });

    @Override
    public void onDestroy() {
        timer.cancel();
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "ServiceTwo onDestroy");
        super.onDestroy();
    }
}
