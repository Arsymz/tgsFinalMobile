package com.example.recyclerview.view.music

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.recyclerview.R

class music_Activity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private var isPaused = false
    private var pausedPosition = 0

    private val musicIdList = mutableListOf<Int>() // List untuk menyimpan ID audio

    private var currentAudioIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        // Mengisi musicIdList dengan ID audio dari array
        musicIdList.addAll(getMusicIdList())

        // Inisialisasi MediaPlayer dengan audio pertama
        mediaPlayer = MediaPlayer.create(this, musicIdList[currentAudioIndex])

        // Dapatkan referensi tombol-tombol dari XML layout
        val playButton: Button = findViewById(R.id.btn_play_audio)
        val nextButton: Button = findViewById(R.id.btn_next_audio)
        val previousButton: Button = findViewById(R.id.btn_previous_audio)

        // Set listener untuk tombol "Play/Pause"
        playButton.setOnClickListener {
            togglePlay()
        }

        // Set listener untuk tombol "Next"
        nextButton.setOnClickListener {
            playNext()
        }

        // Set listener untuk tombol "Previous"
        previousButton.setOnClickListener {
            playPrevious()
        }

        // Inisialisasi dan setup seekBar
        setupSeekBar()

        // Perbarui judul lagu pada awalnya
        updateMusicTitle()

        // Cek apakah ada data yang dikirim melalui Intent
        val musicId = intent.getIntExtra("musicId", -1)
        if (musicId != -1) {
            // Cari index audio yang sesuai dengan musicId
            currentAudioIndex = musicIdList.indexOf(musicId)
            changeAudio()
        }
    }

    private fun getMusicIdList(): List<Int> {
        // Mendapatkan ID audio dari array di strings.xml
        val musicIdArray = resources.obtainTypedArray(R.array.music_id)
        val list = mutableListOf<Int>()

        for (i in 0 until musicIdArray.length()) {
            val resourceId = musicIdArray.getResourceId(i, 0)
            list.add(resourceId)
        }

        musicIdArray.recycle() // Jangan lupa untuk me-recycle TypedArray
        return list
    }

    // Fungsi untuk memulai atau menghentikan pemutaran audio
    private fun togglePlay() {
        val playButton: Button = findViewById(R.id.btn_play_audio)
        if (!mediaPlayer.isPlaying) {
            if (isPaused) {
                // Jika sedang di-pause, lanjutkan dari posisi terakhir
                mediaPlayer.seekTo(pausedPosition)
                mediaPlayer.start()
                isPaused = false
            } else {
                // Jika baru dimulai, mulai dari awal
                mediaPlayer.start()
            }
            playButton.text = "Pause Audio"
        } else {
            // Jika sedang berjalan, pause dan simpan posisi
            mediaPlayer.pause()
            playButton.text = "Play Audio"
            isPaused = true
            pausedPosition = mediaPlayer.currentPosition
        }
    }

    // Fungsi untuk memainkan audio berikutnya
    private fun playNext() {
        // Mengubah indeks lagu ke depan dengan loop jika sudah di ujung daftar
        currentAudioIndex = (currentAudioIndex + 1) % musicIdList.size
        changeAudio()
    }

    // Fungsi untuk memainkan audio sebelumnya
    private fun playPrevious() {
        // Mengubah indeks lagu ke belakang dengan loop jika sudah di awal daftar
        currentAudioIndex = if (currentAudioIndex > 0) currentAudioIndex - 1 else musicIdList.size - 1
        changeAudio()
    }

    // Fungsi untuk mengganti audio yang sedang diputar
    private fun changeAudio() {
        // Menghentikan dan melepaskan MediaPlayer yang sedang berjalan
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()

        // Inisialisasi MediaPlayer dengan audio baru
        mediaPlayer = MediaPlayer.create(this, musicIdList[currentAudioIndex])

        // Memulai pemutaran lagu baru
        togglePlay()

        // Perbarui judul lagu
        updateMusicTitle()
    }

    // Fungsi untuk memperbarui judul lagu
    private fun updateMusicTitle() {
        val musicTitleTextView: TextView = findViewById(R.id.musicTitleTextView)
        val judulArray = resources.getStringArray(R.array.judul_music)
        musicTitleTextView.text = judulArray[currentAudioIndex]

     // baru
        // Pastikan currentAudioIndex tidak melebihi batas indeks array judulArray
        if (currentAudioIndex in judulArray.indices) {
            musicTitleTextView.text = judulArray[currentAudioIndex]
        } else {
            // Handle jika currentAudioIndex tidak valid
            musicTitleTextView.text = "Unknown Title"
        }
    }

    // Fungsi untuk mengatur dan memperbarui SeekBar
    private fun setupSeekBar() {
        // Inisialisasi seekBar di sini jika belum diinisialisasi
        if (!::seekBar.isInitialized) {
            seekBar = findViewById(R.id.seekBar)
            seekBar.max = mediaPlayer.duration

            // Handler untuk pembaruan periodik pada UI
            val handler = Handler(Looper.getMainLooper())

            // Runnable untuk pembaruan periodik SeekBar
            val updateSeekBar = object : Runnable {
                override fun run() {
                    try {
                        if (mediaPlayer != null && mediaPlayer.isPlaying) {
                            runOnUiThread {
                                seekBar.progress = mediaPlayer.currentPosition
                            }
                        }
                        handler.postDelayed(this, 15) // Pembaruan setiap 15 milidetik
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                        // Tangani IllegalStateException yang mungkin terjadi saat MediaPlayer dilepaskan
                    }
                }
            }

            // Jalankan pembaruan pertama
            handler.post(updateSeekBar)

            // SeekBar listener for manual seek
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        try {
                            mediaPlayer.seekTo(progress)
                        } catch (e: IllegalStateException) {
                            e.printStackTrace()
                            // Tangani IllegalStateException yang mungkin terjadi saat MediaPlayer dilepaskan
                        }
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    // Fungsi yang dipanggil saat aktivitas dihancurkan
    override fun onDestroy() {
        super.onDestroy()
        // Melepaskan MediaPlayer saat aktivitas dihancurkan
        mediaPlayer.release()
    }
}
