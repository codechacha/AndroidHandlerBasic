package com.codechacha.handler

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private val handler: Handler = MyHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handlerThread = HandlerThread("MyHandlerThread")
        handlerThread.start()
        val handlerWithLooper = MyHandlerWithLooper(handlerThread.looper)

        doSomethingBtn.setOnClickListener {
            val msg: Message = handler.obtainMessage(MyHandler.MSG_DO_SOMETHING1)
            handler.sendMessage(msg)

            handler.sendEmptyMessage(MyHandler.MSG_DO_SOMETHING2)

            val msg2 = Message.obtain(handler, MyHandler.MSG_DO_SOMETHING3)
            handler.sendMessage(msg2)
        }

        delayDoSomethingBtn.setOnClickListener {
            handler.sendEmptyMessageDelayed(MyHandler.MSG_DO_SOMETHING1, 5000)

            handler.sendEmptyMessage(MyHandler.MSG_DO_SOMETHING2)

            handler.sendEmptyMessageDelayed(MyHandler.MSG_DO_SOMETHING3, 2000)
        }

        doSomethingWithParamBtn.setOnClickListener {
            val msg = handler.obtainMessage(MyHandler.MSG_DO_SOMETHING4)
            msg.arg1 = 10
            msg.arg2 = 100
            msg.obj = Person("chacha")
            handler.sendMessage(msg)
        }

        doSomethingInOtherThreadBtn.setOnClickListener {
            Log.d(TAG, "This is UI Thread!")
            handlerWithLooper.sendEmptyMessage(MyHandler.MSG_DO_SOMETHING1)
            handlerWithLooper.sendEmptyMessageDelayed(MyHandler.MSG_DO_SOMETHING2, 1000)
            handlerWithLooper.sendEmptyMessageDelayed(MyHandler.MSG_DO_SOMETHING3, 2000)
        }


        val myThreadForHandler = MyThreadForHandler()
        myThreadForHandler.start()
        doSomethingInOtherThread2Btn.setOnClickListener {
            Log.d(TAG, "This is UI Thread!")
            myThreadForHandler.doSomething()
        }
    }

    data class Person(val name: String)


    class MyHandler : Handler() {
        companion object {
            const val TAG = "MyHandler"
            const val MSG_DO_SOMETHING1 = 1
            const val MSG_DO_SOMETHING2 = 2
            const val MSG_DO_SOMETHING3 = 3
            const val MSG_DO_SOMETHING4 = 4
        }
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_DO_SOMETHING1 -> {
                    Log.d(TAG, "Do something1")
                }
                MSG_DO_SOMETHING2 -> {
                    Log.d(TAG, "Do something2")
                }
                MSG_DO_SOMETHING3 -> {
                    Log.d(TAG, "Do something3")
                }
                MSG_DO_SOMETHING4 -> {
                    Log.d(TAG, "Do something4, arg1: ${msg.arg1}, arg2: ${msg.arg2}, obj: ${msg.obj}")
                }
            }
        }
    }

    class MyHandlerWithLooper(looper: Looper) : Handler(looper) {
        companion object {
            const val TAG = "MyHandler"
            const val MSG_DO_SOMETHING1 = 1
            const val MSG_DO_SOMETHING2 = 2
            const val MSG_DO_SOMETHING3 = 3
            const val MSG_DO_SOMETHING4 = 4
        }
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_DO_SOMETHING1 -> {
                    Log.d(TAG, "Do something1")
                }
                MSG_DO_SOMETHING2 -> {
                    Log.d(TAG, "Do something2")
                }
                MSG_DO_SOMETHING3 -> {
                    Log.d(TAG, "Do something3")
                }
                MSG_DO_SOMETHING4 -> {
                    Log.d(TAG, "Do something4, arg1: ${msg.arg1}, arg2: ${msg.arg2}, obj: ${msg.obj}")
                }
            }
        }
    }

    class MyThreadForHandler : Thread() {
        private var myHandler: MyHandler? = null
        override fun run() {
            Looper.prepare()
            myHandler = MyHandler()
            Looper.loop()
        }

        fun doSomething() {
            myHandler!!.sendEmptyMessage(MyHandler.MSG_DO_SOMETHING1)
            myHandler!!.sendEmptyMessageDelayed(MyHandler.MSG_DO_SOMETHING2, 1000)
            myHandler!!.sendEmptyMessageDelayed(MyHandler.MSG_DO_SOMETHING3, 2000)
        }
    }



}
