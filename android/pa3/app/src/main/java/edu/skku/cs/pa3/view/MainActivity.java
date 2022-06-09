package edu.skku.cs.pa3.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import edu.skku.cs.pa3.R;
import edu.skku.cs.pa3.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    public MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new MainViewModel(this);

        Log.d("name", getIntent().getStringExtra("user_id"));

        mainViewModel.initView(getIntent().getStringExtra("user_id"));
    }
}