package com.arbl.arduinobluetooth.core.base.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initLayout())
        initAppBar()
        initIntentData()
        initRecyclerView()
        initObserver()
        initClickListener()
        initFetchData()
        initLocalData()
        initView()
    }

    abstract fun initLayout(): View
    open fun initAppBar() {}
    open fun initFetchData() {}
    open fun initLocalData() {}
    open fun initRecyclerView() {}
    open fun initIntentData() {}
    open fun initObserver() {}
    open fun initClickListener() {}
    open fun initView() {}
}