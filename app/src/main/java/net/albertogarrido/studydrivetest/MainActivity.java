package net.albertogarrido.studydrivetest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static net.albertogarrido.studydrivetest.WorkerService.CONSUMER;
import static net.albertogarrido.studydrivetest.WorkerService.PRODUCER;
import static net.albertogarrido.studydrivetest.WorkerService.WORKER;
import static net.albertogarrido.studydrivetest.WorkerService.WORKER_TYPE;

public class MainActivity extends AppCompatActivity {

    private int consumers = 0;
    private int producers = 0;
    private ItemsAdapter adapter = new ItemsAdapter();
    private List<Item> itemsList = new ArrayList<>(10);

    private Messenger producerMessenger;
    private WorkerBroadcastReceiver broadcastReceiver;
    private boolean isServiceBound;

    private ServiceConnection producerConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            producerMessenger = new Messenger(service);
            isServiceBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            producerMessenger = null;
            isServiceBound = false;
        }
    };

    @BindView(R.id.consumerCount)
    TextView consumerCount;
    @BindView(R.id.producerCount)
    TextView producerCount;
    @BindView(R.id.listItemsCount)
    TextView listItemsCount;
    @BindView(R.id.list)
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        configureList();
        refresh();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(WorkerService.createIntent(this), producerConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        broadcastReceiver = new WorkerBroadcastReceiver();
        final IntentFilter intentFilter = new IntentFilter(WORKER);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (broadcastReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        broadcastReceiver = null;
    }

    @OnClick(R.id.addConsumer)
    public void onAddConsumerClick(Button button) {
        consumers++;
        consumerCount.setText(getResources().getQuantityString(R.plurals.consumers, consumers, consumers));
        sendWorker(CONSUMER);
    }

    @OnClick(R.id.addProducer)
    public void onAddProducerClick(Button button) {
        producers++;
        producerCount.setText(getResources().getQuantityString(R.plurals.producers, producers, producers));
        sendWorker(PRODUCER);
    }

    private void sendWorker(int workerType) {
        if (!isServiceBound) return;
        Message msg;
        switch (workerType) {
            case PRODUCER:
                msg = Message.obtain(null, PRODUCER);
                break;
            case CONSUMER:
                msg = Message.obtain(null, CONSUMER);
                break;
            default:
                throw new IllegalArgumentException("No other option than producer or consumer");
        }
        try {
            producerMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        adapter.submitList(itemsList);
        listItemsCount.setText(getResources().getQuantityString(R.plurals.listItems, itemsList.size(), itemsList.size()));
        delayAndScroll();
    }

    private void delayAndScroll() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.getLayoutManager().scrollToPosition(itemsList.size() - 1);
            }
        }, 50);
    }

    private void configureList() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
    }

    class WorkerBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int workerType;
            if (bundle != null) {
                workerType = bundle.getInt(WORKER_TYPE);
                switch (workerType) {
                    case PRODUCER:
                        itemsList.add(new Item());
                        break;
                    case CONSUMER:
                        if (itemsList.size() > 0) {
                            itemsList.remove(itemsList.size() - 1);
                        } else {
                            String msg = "";
                            if (producers < consumers) {
                                msg += "You have more producers than consumers and t";
                            } else {
                                msg += "T";
                            }
                            msg += "here are no more items to remove, add more producers";
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("No other option than producer or consumer");
                }
                refresh();
            }
        }
    }

}
