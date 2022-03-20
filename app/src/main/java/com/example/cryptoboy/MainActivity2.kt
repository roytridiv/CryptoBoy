package com.example.cryptoboy

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.cryptoboy.databinding.ActivityMain2Binding
import java.io.*
import java.lang.StringBuilder
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.util.Base64
import java.security.SecureRandom
import javax.crypto.KeyGenerator


private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200


class MainActivity2 : AppCompatActivity() {
    private val filePath by lazy { this.cacheDir.path + "/test" }
    private lateinit var binding: ActivityMain2Binding
    private var fileName: String = ""
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private val sharedPref: SharedPreferences by lazy {
        getSharedPreferences(
            "permissions",
            Context.MODE_PRIVATE
        )
    }
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
//            handler.post(updateVisualizer)
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
            setOutputFile(filePath+"/testCrypto/crypto.mp3")
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
        encryptFile()
    }

    override fun onStop() {
        super.onStop()
        recorder?.release()
        recorder = null
        player?.release()
        player = null
    }

    private fun writeFile() {
        try {
            val root = File(filePath, "testCrypto")
            if (!root.exists()) {
                root.mkdirs()
            }
            val gpxfile = File(root, "crypto.txt")
            val writer = FileWriter(gpxfile)
            writer.append("TESTINGTESTINGTESTINGTESTING")
            writer.flush()
            writer.close()
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun encryptFile() {
        try {


            val secretKey = getSecretKey(sharedPref)
//            //encrypt file
            val file = readFile()
            var encodedData : ByteArray? = null
            if(file !=  null){
                encodedData = encrypt(secretKey, file)
            }


            encodedData?.let {
                println(it.size)
                saveFile(it, filePath) }


        } catch (e: Exception) {
//            Log.d(mTag, e.message)
        }
    }

    @Throws(IOException::class)
    private fun readFile(file : File) {
        val uploadedString = StringBuilder()
        val reader = BufferedReader(FileReader(file))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            uploadedString.append(line)
            uploadedString.append('\n')
        }
        println("tridiv "+uploadedString.toString())
//        reader.close()
    }

    @Throws(Exception::class)
    fun saveFile(fileData: ByteArray, path: String) {
        val file = File(path)
        val bos = BufferedOutputStream(FileOutputStream(file, false))
        bos.write(fileData)
        bos.flush()
        bos.close()
    }

    @Throws(Exception::class)
    fun encrypt(yourKey: SecretKey, fileData: ByteArray): ByteArray {
        val data = yourKey.encoded
        val skeySpec = SecretKeySpec(data, 0, data.size, "AES")
        val cipher = Cipher.getInstance("AES", "BC")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IvParameterSpec(ByteArray(cipher.blockSize)))
        return cipher.doFinal(fileData)
    }


//    @Throws(java.lang.Exception::class)
//    fun encodeFile(yourKey: SecretKey, fileData: ByteArray?): ByteArray? {
//        var encrypted: ByteArray? = null
//        val data = yourKey.encoded
//        val skeySpec = SecretKeySpec(data, 0, data.size, algorithm)
//        val cipher = Cipher.getInstance(algorithm)
//        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IvParameterSpec(ByteArray(cipher.blockSize)))
//        encrypted = cipher.doFinal(fileData)
//        return encrypted
//    }


    @Throws(Exception::class)
    fun readFile(): ByteArray {
        val file = File(filePath+"/testCrypto/crypto.mp3")
        val fileContents = file.readBytes()
        val inputBuffer = BufferedInputStream(
            FileInputStream(file)
        )

        inputBuffer.read(fileContents)
        inputBuffer.close()

        return fileContents
    }



    private fun getSecretKey(sharedPref: SharedPreferences): SecretKey {

        val key = sharedPref.getString("secretKeyPref", null)

        if (key == null) {
            //generate secure random
            val secretKey = generateSecretKey()
            saveSecretKey(sharedPref, secretKey!!)
            return secretKey
        }

        val decodedKey = Base64.decode(key, Base64.NO_WRAP)
        val originalKey = SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")

        return originalKey
    }

    @Throws(Exception::class)
    fun generateSecretKey(): SecretKey? {
        val secureRandom = SecureRandom()
        val keyGenerator = KeyGenerator.getInstance("AES")
        //generate a key with secure random
        keyGenerator?.init(128, secureRandom)
        return keyGenerator?.generateKey()
    }

    private fun saveSecretKey(sharedPref: SharedPreferences, secretKey: SecretKey): String {
        val encodedKey = Base64.encodeToString(secretKey.encoded, Base64.NO_WRAP)
        sharedPref.edit().putString("secretKeyPref", encodedKey).apply()
        return encodedKey
    }

    private val handler = Handler()
    val REPEAT_INTERVAL = 40

    var updateVisualizer: Runnable = object : Runnable {
        override fun run() {
            if (true) // if we are already recording
            {
                // get the current amplitude
                val x: Int = recorder!!.getMaxAmplitude()
                binding.visualizer.addAmplitude(x.toFloat()) // update the VisualizeView
                binding.visualizer.invalidate() // refresh the VisualizerView

                // update in 40 milliseconds
                handler.postDelayed(
                    this, REPEAT_INTERVAL.toLong()
                )
            }
        }
    }

}