package com.arbl.arduinobluetooth.core.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return setLayout(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserver()
        initClickListener()
        initFetchData()
        initLocalData()
        initView()
    }

    abstract fun setLayout(inflater: LayoutInflater, container: ViewGroup?): View

    open fun initFetchData() {}
    open fun initLocalData() {}
    open fun initRecyclerView() {}
    open fun initObserver() {}
    open fun initClickListener() {}
    open fun initView() {}
}