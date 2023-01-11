package com.example.widgetapp22

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

class SoundService : Service() {
    private var player : MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        Log.e("Service","onBind")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("Service", "Inside startCommand + ${intent?.action}, $startId")

        if(intent == null || intent.action.equals(""))
            return super.onStartCommand(intent, flags, startId)

        if(intent.action.equals("Play")){
            player = MediaPlayer.create(this, R.raw.default_audio)
            player?.start()
        }
        if(intent.action.equals("Stop")){
            player?.stop()
        }
        if(intent.action.equals("Next")){
            player?.stop()
            player = MediaPlayer.create(this, R.raw.next_audio)
            player?.start()
        }
        if(intent.action.equals("Prev")){
            player?.stop()
            player = MediaPlayer.create(this, R.raw.default_audio)
            player?.start()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        player?.release()
    }



}