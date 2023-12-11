package com.example.recyclerview.view.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclerview.R
import com.example.recyclerview.data.Music
import com.example.recyclerview.view.music.music_Activity



class DetailActivity : AppCompatActivity() {

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val receivedData=if (Build.VERSION.SDK_INT>=30){
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Music>("shadow")
        }else{
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Music>("shadow")
        }
        val gambar:ImageView=findViewById(R.id.img_gambar)
        val judul:TextView=findViewById(R.id.tv_Judul)
        val description:TextView=findViewById(R.id.tv_description)
        val playButton:ImageView=findViewById(R.id.tombol_play)
        if (receivedData!=null){
            gambar.setImageResource(receivedData.gambar)
            judul.text=receivedData.judul
            description.text=receivedData.data_deskripsi
            playButton.setOnClickListener {
                val musicIntent = Intent(this, music_Activity::class.java)
                musicIntent.putExtra("musicId", receivedData.musicId)
                startActivity(musicIntent)
            }
        }
    }
}