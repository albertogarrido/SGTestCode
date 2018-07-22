package net.albertogarrido.studydrivetest;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.content.LocalBroadcastManager;

public class WorkerService extends Service {

    public static Intent createIntent(Context context) {
        return new Intent(context, WorkerService.class);
    }

    public static final String WORKER = "workerSent";
    public static final String WORKER_TYPE = "workerType";
    public static final int PRODUCER = 1;
    public static final int CONSUMER = 2;

    final Messenger mMessenger = new Messenger(new IncomingHandler());

    private void createConsumer() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WORKER);
                Bundle bundle = new Bundle();
                bundle.putInt(WORKER_TYPE, CONSUMER);
                intent.putExtras(bundle);
                LocalBroadcastManager.getInstance(WorkerService.this).sendBroadcast(intent);
                handler.postDelayed(this, 4000);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private void createProducer() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WORKER);
                Bundle bundle = new Bundle();
                bundle.putInt(WORKER_TYPE, PRODUCER);
                intent.putExtras(bundle);
                LocalBroadcastManager.getInstance(WorkerService.this).sendBroadcast(intent);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PRODUCER:
                    createProducer();
                    break;
                case CONSUMER:
                    createConsumer();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

}
