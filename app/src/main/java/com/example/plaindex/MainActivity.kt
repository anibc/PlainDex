package com.example.plaindex

import android.app.Activity
import android.opengl.Visibility
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.plaindex.databinding.ActivityMainBinding

import kotlinx.android.synthetic.main.activity_main.*

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
