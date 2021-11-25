package com.mx7.encodingtest

import android.content.Intent
import android.graphics.Matrix
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.MediaController
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class VideoActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "VideoActivity"
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private var isReversedLeft: Boolean = false
    private var isReversedUp: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val selectButton = findViewById<Button>(R.id.select_button)
        val nextButton = findViewById<Button>(R.id.next_button)
        val videoView = findViewById<CustomVideoView>(R.id.playback_video)
        val rotationButton = findViewById<Button>(R.id.rotation_button)
        val reverseLeftRightButton = findViewById<Button>(R.id.reverse_left_right_button)
        val reverseUpDownButton = findViewById<Button>(R.id.reverse_up_down_button)

        selectButton.setOnClickListener(this)
        nextButton.setOnClickListener(this)
        rotationButton.setOnClickListener(this)
        reverseLeftRightButton.setOnClickListener(this)
        reverseUpDownButton.setOnClickListener(this)

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                videoView.setMediaController(MediaController(this))
                videoView.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                    override fun onPrepared(mediaPlayer: MediaPlayer) {
                        mediaPlayer.isLooping = true
                        mediaPlayer.setVolume(0F, 0F)
                        mediaPlayer.start()
                    }
                })
                // TODO need to distinguish back and select video
                videoView.setVideoPath(result.data?.data.toString())
                videoView.start()
            }
    }

    override fun onClick(view: View) {
        when (view.id) {
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
                val intent = Intent(this@VideoActivity, MainActivity::class.java)
                startActivity(intent)
            }

            R.id.rotation_button -> {
                val videoView = findViewById<CustomVideoView>(R.id.playback_video)
                Log.d(TAG, "rotation_button: >>>  ${videoView.rotation}")
                // videoView.rotation = 90F
                if (videoView.rotation == 360F) {
                    videoView.rotation = 0F
                } else {
                    videoView.rotation = videoView.rotation + 90F
                }
                Log.d(TAG, "rotation_button: >>>  ${videoView.rotation}")
            }

            R.id.reverse_left_right_button -> {
                reverseLeftRight(isReversedLeft)
            }

            R.id.reverse_up_down_button -> {
                reverseUpDown(isReversedUp)
            }
        }
    }

    private fun reverseLeftRight(checkReversed: Boolean) {
        val videoView = findViewById<CustomVideoView>(R.id.playback_video)
        val matrix = Matrix()
        isReversedLeft = if (checkReversed) {
            matrix.setScale(1F, 1F)
            videoView.setTransform(matrix)
            false
        } else {
            matrix.setScale(-1F, 1F)
            matrix.postTranslate(videoView.width.toFloat(), 0F)
            videoView.setTransform(matrix)
            true
        }
        videoView.start()
    }

    private fun reverseUpDown(checkReversed: Boolean) {
        val videoView = findViewById<CustomVideoView>(R.id.playback_video)
        val matrix = Matrix()
        isReversedUp = if (checkReversed) {
            matrix.setScale(1F, 1F)
            videoView.setTransform(matrix)
            false
        } else {
            matrix.setScale(1F, -1F)
            matrix.postTranslate(0F, videoView.height.toFloat())
            videoView.setTransform(matrix)
            true
        }
        videoView.start()
    }


}