package com.mx7.encodingtest

import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.warnyul.android.widget.FastVideoView
import org.florescu.android.rangeseekbar.RangeSeekBar
import org.florescu.android.rangeseekbar.RangeSeekBar.OnRangeSeekBarChangeListener

class VideoActivity: AppCompatActivity(), View.OnClickListener {

    private val TAG = "VideoActivity"
    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val selectButton = findViewById<Button>(R.id.select_button)
        val nextButton = findViewById<Button>(R.id.next_button)
        val videoView = findViewById<CustomVideoView>(R.id.playback_video)
        val rotationButton = findViewById<Button>(R.id.rotation_button)
        // val playbackVideo = findViewById<FastVideoView>(R.id.playback_video)

        selectButton.setOnClickListener(this)
        nextButton.setOnClickListener(this)
        rotationButton.setOnClickListener(this)

        resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                        result: ActivityResult ->

                    videoView.setMediaController(MediaController(this))
                    videoView.setOnPreparedListener(object: MediaPlayer.OnPreparedListener {
                        override fun onPrepared(mediaPlayer: MediaPlayer) {
                            mediaPlayer.isLooping = true
                            mediaPlayer.setVolume(0F, 0F)
                            mediaPlayer.start()
                        }

                    })
                    videoView.setVideoPath(result.data?.data.toString())
                    // videoView.rotation = 90F
                    // videoView.setstate
                    videoView.start()

            }

        /*
        // set up the VideoView.
        // We will be using VideoView to view our video
        videoView.setOnPreparedListener { mp -> // get the duration of the video
            val duration = mp.duration / 1000

            // initially set the left TextView to "00:00:00"
            tvLeft.setText("00:00:00")

            // initially set the right Text-View to the video length
            // the getTime() method returns a formatted string in hh:mm:ss
            tvRight.setText(getTime(mp.duration / 1000))

            // this will run he video in loop
            // i.e. the video won't stop
            // when it reaches its duration
            mp.isLooping = true

            // set up the initial values of rangeSeekbar
            rangeSeekBar.setRangeValues(0, duration)
            rangeSeekBar.setSelectedMinValue(0)
            rangeSeekBar.setSelectedMaxValue(duration)
            rangeSeekBar.setEnabled(true)
            rangeSeekBar.setOnRangeSeekBarChangeListener(OnRangeSeekBarChangeListener<Any?> { bar, minValue, maxValue -> // we seek through the video when the user
                // drags and adjusts the seekbar
                videoView.seekTo(minValue as Int * 1000)

                // changing the left and right TextView according to
                // the minValue and maxValue
                tvLeft.setText(getTime(bar.selectedMinValue as Int))
                tvRight.setText(getTime(bar.selectedMaxValue as Int))
            })

            // this method changes the right TextView every 1 second
            // as the video is being played
            // It works same as a time counter we see in any Video Player
            val handler = Handler()
            handler.postDelayed(Runnable {
                if (videoView.currentPosition >= rangeSeekBar.getSelectedMaxValue()
                        .toInt() * 1000
                ) videoView.seekTo(rangeSeekBar.getSelectedMinValue().toInt() * 1000)
                handler.postDelayed(r, 1000)
            }.also { r = it }, 1000)
        }

        videoView.setOnPreparedListener { mediaPlayer ->
            val duration = mediaPlayer.duration / 100
        }
        */
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.select_button -> {
                // create an intent to retrieve the video
                // file from the device storage
                val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                )
                intent.type = "video/*"
                // intent.putExtra("CallType", 1)
                resultLauncher?.launch(intent)
                // startActivityForResult(intent, 123)
            }

            R.id.next_button -> {
                val intent = Intent(this@VideoActivity , MainActivity::class.java)
                startActivity(intent)
            }

            R.id.rotation_button -> {
                val videoView = findViewById<CustomVideoView>(R.id.playback_video)
                Log.d(TAG, "rotation_button: >>>  ${videoView.rotation}")
                // videoView.rotation = 90F
                if(videoView.rotation == 360F){
                    videoView.rotation = 0F
                } else {
                    videoView.rotation = videoView.rotation + 90F
                }
                Log.d(TAG, "rotation_button: >>>  ${videoView.rotation}")
            }
        }
    }
}