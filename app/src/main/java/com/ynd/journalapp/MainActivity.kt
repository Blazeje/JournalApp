package com.ynd.journalapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ynd.ui.JournalFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, JournalFragment())
                .commit()
        }
    }
}
