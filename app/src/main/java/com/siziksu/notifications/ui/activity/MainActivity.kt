package com.siziksu.notifications.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.siziksu.notifications.R
import com.siziksu.notifications.ui.manager.notifications.NotificationsManager
import kotlinx.android.synthetic.main.activity_main.buttonCustom
import kotlinx.android.synthetic.main.activity_main.buttonCustomSticky
import kotlinx.android.synthetic.main.activity_main.buttonInboxStyle
import kotlinx.android.synthetic.main.activity_main.buttonNormal
import kotlinx.android.synthetic.main.activity_main.buttonNormalSticky
import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity() {

    private var manager: NotificationsManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        manager = NotificationsManager(this)
        manager?.clearMessages()
        buttonNormal.setOnClickListener { manager?.showNotification(false) }
        buttonCustom.setOnClickListener { manager?.showCustomNotification(false) }
        buttonInboxStyle.setOnClickListener { manager?.showInboxNotification(false) }
        buttonNormalSticky.setOnClickListener { manager?.showNotification(true) }
        buttonCustomSticky.setOnClickListener { manager?.showCustomNotification(true) }
    }
}
