package net.albertogarrido.studydrivetest

import android.content.*
import android.os.*
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import net.albertogarrido.studydrivetest.WorkerService.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var consumers = 0
    private var producers = 0
    private val adapter = ItemsAdapter()
    private val itemsList = ArrayList<Item>(10)

    private var producerMessenger: Messenger? = null
    private val broadcastReceiver: WorkerBroadcastReceiver by lazy { WorkerBroadcastReceiver() }
    private var isServiceBound: Boolean = false

    private val producerConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            producerMessenger = Messenger(service)
            isServiceBound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            producerMessenger = null
            isServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButtonListeners()
        configureList()
        refresh()
    }

    override fun onStart() {
        super.onStart()
        bindService(WorkerService.createIntent(this), producerConnection, Context.BIND_AUTO_CREATE)
    }

    public override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(WORKER)
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
    }

    public override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    private fun initButtonListeners() {
        addConsumerButton.setOnClickListener {
            consumers++
            consumerCount.text = resources.getQuantityString(R.plurals.consumers, consumers, consumers)
            sendWorker(CONSUMER)
        }
        addProducerButton.setOnClickListener {
            producers++
            producerCount.text = resources.getQuantityString(R.plurals.producers, producers, producers)
            sendWorker(PRODUCER)
        }
    }

    private fun sendWorker(workerType: Int) {
        if (!isServiceBound) return
        val msg: Message = when (workerType) {
            PRODUCER -> Message.obtain(null, PRODUCER)
            CONSUMER -> Message.obtain(null, CONSUMER)
            else -> throw IllegalArgumentException("No other option than producer or consumer")
        }
        try {
            producerMessenger!!.send(msg)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    private fun refresh() {
        adapter.submitList(itemsList)
        listItemsCount.text = resources.getQuantityString(R.plurals.listItems, itemsList.size, itemsList.size)
        delayAndScroll()
    }

    private fun delayAndScroll() {
        Handler().postDelayed({ list.layoutManager.scrollToPosition(itemsList.size - 1) }, 50)
    }

    private fun configureList() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        list.layoutManager = layoutManager
        list.adapter = adapter
    }

    internal inner class WorkerBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val extras = intent.extras

            extras?.let { bundle ->
                val workerType = bundle.getInt(WORKER_TYPE)
                when (workerType) {
                    PRODUCER -> itemsList.add(Item.create())
                    CONSUMER -> {
                        if (itemsList.size > 0) {
                            itemsList.removeAt(itemsList.size - 1)
                        } else {
                            val msg = if (producers < consumers) {
                                "You have more producers than consumers and there are no more items to remove, add more producers"
                            } else {
                                "There are no more items to remove, add more producers"
                            }
                            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                    else -> throw IllegalArgumentException("No other option than producer or consumer")
                }
                refresh()
            }
        }
    }

}
