package com.siziksu.notifications.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.siziksu.notifications.R;
import com.siziksu.notifications.ui.manager.CustomNotificationManager;
import com.siziksu.notifications.ui.manager.NotificationManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private NotificationManager manager;
    private CustomNotificationManager customManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        manager = new NotificationManager(this);
        customManager = new CustomNotificationManager(this);
    }

    @OnClick(R.id.buttonNormal)
    public void onLeftButtonClick() {
        manager.showNotification();
    }

    @OnClick(R.id.buttonCustom)
    public void onRightButtonClick() {
        customManager.showCustomNotification();
    }
}
