package edu.skku.cs.pa3.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.databinding.BaseObservable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import edu.skku.cs.pa3.R;
import edu.skku.cs.pa3.model.BaseModel;
import edu.skku.cs.pa3.model.user.UserModel;
import edu.skku.cs.pa3.view.MainActivity;
import edu.skku.cs.pa3.view.SighUpActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginViewModel extends BaseObservable {

    private Activity activity;
    private EditText idText;

    public LoginViewModel (Activity activity) {
        this.activity = activity;

        Button btn1 = activity.findViewById(R.id.login_loginButton);
        Button btn2 = activity.findViewById(R.id.login_signupButton);
        idText = activity.findViewById(R.id.login_id);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(idText.getText().toString());
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getApplicationContext(), SighUpActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    public void login(String u_id) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://0yetb1moyh.execute-api.ap-northeast-2.amazonaws.com/dev/login").newBuilder();

        String url = urlBuilder.toString();

        UserModel user = new UserModel();
        user.setUser_id(u_id);
        Gson gson = new Gson();
        String json = gson.toJson(user, UserModel.class);

        Request req = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"),json))
                .build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gsonBuilder = new GsonBuilder().create();
                BaseModel res = gsonBuilder.fromJson(myResponse, BaseModel.class);

                if(res.isSuccess()) {
                    // 로그인 성공
                    Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                    intent.putExtra("user_id", u_id);
                    activity.startActivity(intent);

                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(activity.getApplicationContext(), res.getMsg(), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            }
        });
    }
}
