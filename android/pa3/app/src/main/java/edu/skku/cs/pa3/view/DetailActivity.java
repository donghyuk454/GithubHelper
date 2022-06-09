package edu.skku.cs.pa3.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edu.skku.cs.pa3.R;
import edu.skku.cs.pa3.viewmodel.DetailViewModel;

public class DetailActivity extends AppCompatActivity {

    private DetailViewModel detailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detailViewModel = new DetailViewModel(this);
        Intent intent = getIntent();
        detailViewModel.initView(intent.getStringExtra("name"), intent.getStringExtra("p_name"), intent.getStringExtra("full_name"), intent.getStringExtra("html_url"), intent.getStringExtra("description"), intent.getStringExtra("avatar_url"));
    }
}
