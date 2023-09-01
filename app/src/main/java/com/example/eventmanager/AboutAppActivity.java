package com.example.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

public class AboutAppActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("O aplikaciji");
        textView=findViewById(R.id.aboutText);

    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item)
    {
        if(item.getItemId()== android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LanguageHelper.applyLanguage(this);
        getSupportActionBar().setTitle(getString(R.string.menu_aboutApp));
       textView.setText(getString(R.string.about_app_segment));

    }
}