package edu.skku.cs.pa3.viewmodel;

import android.app.Activity;
import android.util.Log;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupViewModel extends BaseObservable {

    private Activity activity;
    EditText id;
    EditText github;

    public SignupViewModel (Activity activity) {
        this.activity = activity;

        Button btn = activity.findViewById(R.id.signup_signUpButton);
        id = activity.findViewById(R.id.signup_id);
        github = activity.findViewById(R.id.signup_github);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp(id.getText().toString(), github.getText().toString());
            }
        });
    }

    public void signUp(String u_id, String github_id) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://0yetb1moyh.execute-api.ap-northeast-2.amazonaws.com/dev/user").newBuilder();

        String url = urlBuilder.toString();

        UserModel user = new UserModel();
        user.setUser_id(u_id);
        user.setGithub_id(github_id);
        Gson gson = new Gson();
        String json =gson.toJson(user, UserModel.class);

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
                Log.d("response", myResponse);
                BaseModel res = gsonBuilder.fromJson(myResponse, BaseModel.class);

                if(res.isSuccess()) {
                    // 유저 등록 성공
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(activity.getApplicationContext(), "회원가입에 성공하였습니다!", Toast.LENGTH_SHORT);
                            toast.show();

                            activity.finish();
                        }
                    });
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
