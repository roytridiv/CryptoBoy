package com.example.cryptoboy

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.widget.Toast

import android.os.Environment
import java.lang.StringBuilder


class MainActivity3 : AppCompatActivity() {
    private val filePath by lazy { this.cacheDir.path+"/test" }
    private val sharedPref: SharedPreferences by lazy {
        getSharedPreferences(
            "permissions",
            Context.MODE_PRIVATE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        val c: Cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
        writeFile()
        encryptDownloadedFile()
        val file = decryptEncryptedFile()



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

    @Throws(Exception::class)
    fun readFile(filePath: String): ByteArray {
        val file = File(filePath)
        val fileContents = file.readBytes()
        val inputBuffer = BufferedInputStream(
            FileInputStream(file)
        )

        inputBuffer.read(fileContents)
        inputBuffer.close()

        return fileContents
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


    private fun encryptDownloadedFile() {
        try {


            val fileData = readFile(filePath+"/testCrypto/crypto.txt")
//
//            //get secret key
            val secretKey = getSecretKey(sharedPref)
//            //encrypt file
            val encodedData = encrypt(secretKey, fileData)
//
            println(encodedData.size)

            for (i in encodedData){
                println("tridiv "+i)
            }

            saveFile(encodedData, filePath)


        } catch (e: Exception) {
//            Log.d(mTag, e.message)
        }
    }

    @Throws(Exception::class)
    fun decrypt(yourKey: SecretKey, fileData: ByteArray): ByteArray {
        val decrypted: ByteArray
        val cipher = Cipher.getInstance("AES", "BC")
        cipher.init(Cipher.DECRYPT_MODE, yourKey, IvParameterSpec(ByteArray(cipher.blockSize)))
        decrypted = cipher.doFinal(fileData)
        return decrypted
    }

    private fun decryptEncryptedFile(): ByteArray {
        val filePath = filesDir.absolutePath + "/filename"
        val fileData = readFile(filePath)
        val secretKey = getSecretKey(sharedPref)
        return decrypt(secretKey, fileData)
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
}