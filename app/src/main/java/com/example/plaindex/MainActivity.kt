package com.example.plaindex

import android.app.Activity
import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.example.plaindex.databinding.ActivityMainBinding

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.view.*
import java.io.File
import java.io.FileReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val model = ViewModelProviders.of(this)[MainViewModel::class.java]
        setContentView(binding.root)
        //setSupportActionBar(toolbar)

        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        var recfiles = model.getRecentFiles().value
        binding.recentFilesTv.visibility = if(recfiles == null || recfiles.isEmpty()) View.INVISIBLE else View.VISIBLE
        model.getRecentFiles().observe(this, Observer<List<String>>{ files ->
            binding.recentFilesTv.text = files.size.toString()
            binding.recentFilesTv.visibility = if (files.isEmpty()) View.INVISIBLE else View.VISIBLE
        })
        binding.NewNoteButton.text = "New binding note"
        binding.OpenNoteButton.setOnClickListener(View.OnClickListener { view: View? ->
            // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
            // browser.
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                // Filter to only show results that can be "opened", such as a
                // file (as opposed to a list of contacts or timezones)
                addCategory(Intent.CATEGORY_OPENABLE)

                // Filter to show only images, using the image MIME data type.
                // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
                // To search for all documents available via installed storage providers,
                // it would be "*/*".
                type = "*/*"
            }

            startActivityForResult(intent, 42)

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if( requestCode == 42 && resultCode == Activity.RESULT_OK ){
            data?.data?.also { uri ->
                binding.root.content_et.setText(getDecryptedText( uri ))
            }
        }
    }

    fun getDecryptedText( uri: Uri): String{
        val fileContent = contentResolver.openInputStream(uri)?.readBytes()!!
        /*val plaintext: ByteArray = fileContent
        //val keygen = KeyGenerator.getInstance("AES")
        //keygen.init(256)
        var keyPlain = "password"
        var passByteArray: ByteArray = MessageDigest.getInstance("SHA-256").digest(keyPlain.toByteArray())
        val key: SecretKey = SecretKeySpec(passByteArray, "AES_256")
        val cipher = Cipher.getInstance("AES_256/CBC/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(ByteArray(cipher.blockSize)))
        val ciphertext: ByteArray = cipher.doFinal(plaintext)
        //var decryptedContent = ciphertext.toString(Charset.defaultCharset())*/
        return fileContent.toString(Charset.defaultCharset())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
