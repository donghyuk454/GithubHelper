package edu.skku.cs.pa3.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edu.skku.cs.pa3.R;
import edu.skku.cs.pa3.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new LoginViewModel(this);
    }
}
