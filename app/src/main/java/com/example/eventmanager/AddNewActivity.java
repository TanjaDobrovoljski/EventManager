package com.example.eventmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.text.InputFilter;
import android.text.Spanned;

public class AddNewActivity extends AppCompatActivity {

    private DBHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Dodavanje nove aktivnosti");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int fragmentId = getIntent().getIntExtra("fragmentId", 0);
        dbHelper=new DBHelper(this);
        // Determine which fragment to display based on the fragment ID
        Fragment fragment = null;
        switch (fragmentId) {
            case 1:
                fragment = new FreeTimeFragment(dbHelper);
                break;
            case 2:
              // fragment=new WorkFragment(dbHelper);
                break;
            case 3:
              // fragment=new TravelFragment(dbHelper);
                break;
            default:
                // Default to a fallback fragment or handle the case as desired
               // fragment = new Fragment1();
                break;
        }

        // Replace the container with the selected fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}