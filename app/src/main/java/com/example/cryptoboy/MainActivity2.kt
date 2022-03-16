package com.example.cryptoboy

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.example.cryptoboy.databinding.ActivityMain2Binding
import java.io.IOException


private const val LOG_TAG = "AudioRecordTest"
    private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity2 : AppCompatActivity() {

        private lateinit var binding: ActivityMain2Binding
        private var fileName: String = ""
        private var recorder: MediaRecorder? = null
        private var player: MediaPlayer? = null

        // Requesting permission to RECORD_AUDIO
        private var permissionToRecordAccepted = false
        private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMain2Binding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)

            binding.playBtn.setOnClickListener {
                binding.textView2.text = "Playing recorded audio"
                startPlaying()
            }

            binding.stopBtn.setOnClickListener {
                binding.textView2.text = "Recording stopped"
                stopRecording()
            }

            binding.startBtn.setOnClickListener {
                binding.textView2.text = "Recording audio"
                startRecording()
            }

            // Record to the external cache directory for visibility
            fileName = "${cacheDir.absolutePath}/complainTest.mp3"

/*            val masterKey = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            val encryptedFile = EncryptedFile.Builder(
                this,
                test(),
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            println("tridiv " + encryptedFile.openFileInput())
*/

            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            } else {
                false
            }
            if (!permissionToRecordAccepted) {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
            }
        }

        private fun onRecord(start: Boolean) = if (start) {
            startRecording()
        } else {
            stopRecording()
        }

        private fun onPlay(start: Boolean) = if (start) {
            startPlaying()
        } else {
            stopPlaying()
        }

        private fun startPlaying() {
            player = MediaPlayer().apply {
                try {
                     setDataSource(fileName)
                    prepare()
                    start()
                } catch (e: IOException) {
                    Log.e(LOG_TAG, "prepare() failed")
                }
            }
        }

        private fun stopPlaying() {
            player?.release()
            player = null
        }

        private fun startRecording() {
            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
                setOutputFile(fileName)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

                try {
                    prepare()
                } catch (e: IOException) {
                    Log.e(LOG_TAG, "prepare() failed")
                }

                start()
            }
        }

        private fun stopRecording() {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
        }

        override fun onStop() {
            super.onStop()
            recorder?.release()
            recorder = null
            player?.release()
            player = null
        }
    }