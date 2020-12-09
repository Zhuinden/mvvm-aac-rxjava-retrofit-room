package com.zhuinden.mvvmaacrxjavaretrofitroom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhuinden.mvvmaacrxjavaretrofitroom.features.cats.CatFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, CatFragment())
                .commit()
        }
    }
}
