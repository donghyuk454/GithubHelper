package edu.skku.cs.pa3.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edu.skku.cs.pa3.R;
import edu.skku.cs.pa3.viewmodel.SignupViewModel;

public class SighUpActivity extends AppCompatActivity {

    private SignupViewModel signupViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupViewModel = new SignupViewModel(this);
    }
}
