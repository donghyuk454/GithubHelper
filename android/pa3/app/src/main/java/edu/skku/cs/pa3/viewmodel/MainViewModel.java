package edu.skku.cs.pa3.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BaseObservable;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.skku.cs.pa3.R;
import edu.skku.cs.pa3.viewmodel.adapter.GithubAdapter;
import edu.skku.cs.pa3.viewmodel.adapter.ProjectAdapter;
import edu.skku.cs.pa3.model.BaseModel;
import edu.skku.cs.pa3.model.github.GithubInfo;
import edu.skku.cs.pa3.model.user.AddFriendModel;
import edu.skku.cs.pa3.model.user.UserModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static androidx.appcompat.app.AlertDialog.*;

public class MainViewModel extends BaseObservable {

    private Activity activity;
    private UserModel userModel;
    private GithubAdapter friendGithubAdapter;

    public MainViewModel(Activity activity) {
        this.activity = activity;
    }

    public void initView(String u_id){
        //floating action button
        FloatingActionButton floatingActionButton = activity.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = layoutInflater.inflate(R.layout.activity_popup, null);
                EditText editText = dialogView.findViewById(R.id.dialogEditText);
                builder.setView(dialogView)
                        .setPositiveButton("Add", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String f_id = editText.getText().toString();
                                addFriend(u_id, f_id);
                            }
                        });
                builder.show();
            }
        });

        ListView listView = activity.findViewById(R.id.friendList);
        ScrollView scrollView = activity.findViewById(R.id.scrollview);
        ListView listView1 = activity.findViewById(R.id.myProjects);
        listView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });



        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder =
                HttpUrl.parse("https://0yetb1moyh.execute-api.ap-northeast-2.amazonaws.com/dev/user").newBuilder();
        urlBuilder.addQueryParameter("user_id", u_id);
        String url = urlBuilder.build().toString();

        Request req = new Request.Builder()
                .url(url)
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(activity, "네트워크를 확인해주세요!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();
                Log.d("res", myResponse);
                Gson gson = new GsonBuilder().create();
                userModel = gson.fromJson(myResponse, UserModel.class);
                Log.d("image", userModel.getGithub().getAvatar_url());
                Log.d("isSuccess", String.valueOf(userModel.isSuccess()));
                if(userModel.isSuccess()) {
                    Log.d("image", userModel.getGithub().getAvatar_url());
                    // 성공
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ListView projectsListview = activity.findViewById(R.id.myProjects);

                            GithubInfo githubInfo = userModel.getGithub();

                            TextView textView = activity.findViewById(R.id.userName);
                            textView.setText(githubInfo.getName());
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userModel.getGithub().getHtml_url()));
                                    activity.startActivity(intent);
                                }
                            });
                            ImageView imageView = activity.findViewById(R.id.UserProfileImage);

                            Log.d("image", githubInfo.getAvatar_url());

                            Glide.with(activity).load(githubInfo.getAvatar_url()).into(imageView);

                            if (userModel.getProjects().size() == 0) {
                                TextView t = activity.findViewById(R.id.textView4);
                                t.setText("No project");
                            }

                            ProjectAdapter projectAdapter = new ProjectAdapter(activity, userModel.getProjects(), githubInfo.getAvatar_url(), githubInfo.getName());
                            projectsListview.setAdapter(projectAdapter);

                            int height = 0;
                            int wid = View.MeasureSpec.makeMeasureSpec(projectsListview.getWidth(), View.MeasureSpec.AT_MOST);
                            if (projectAdapter.getCount() >= 3) {
                                for (int i = 0; i < 3; i++) {
                                    View v = projectAdapter.getView(i, null, listView);
                                    v.measure(wid, View.MeasureSpec.UNSPECIFIED);
                                    height += v.getMeasuredHeight();
                                }
                            }
                            else
                                for (int i = 0; i < projectAdapter.getCount(); i++) {
                                    View v = projectAdapter.getView(i, null, listView);
                                    v.measure(wid, View.MeasureSpec.UNSPECIFIED);
                                    height += v.getMeasuredHeight();
                                }

                            ViewGroup.LayoutParams params = projectsListview.getLayoutParams();
                            params.height = height;
                            projectsListview.setLayoutParams(params);

                            Log.d("final view height", String.valueOf(projectsListview.getLayoutParams().height));

                        }
                    });

                    // get friends
                    if (userModel.getFriends().length > 0) {
                        getFriends(u_id);
                    }
                }
                else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(activity, userModel.getMsg(), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            }
        });
    }

    void getFriends(String u_id) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder =
                HttpUrl.parse("https://0yetb1moyh.execute-api.ap-northeast-2.amazonaws.com/dev/friends").newBuilder();
        urlBuilder.addQueryParameter("user_id", u_id);
        String url = urlBuilder.build().toString();

        Request req = new Request.Builder()
                .url(url)
                .build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Gson gson = new GsonBuilder().create();
                GithubInfo[] friends = gson.fromJson(myResponse, GithubInfo[].class);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<GithubInfo> friendsList = new ArrayList<GithubInfo>(Arrays.asList(friends));
                        ListView friendsListview = activity.findViewById(R.id.friendList);

                        if (friendGithubAdapter == null) {
                            friendGithubAdapter = new GithubAdapter(friendsList, activity);
                            friendsListview.setAdapter(friendGithubAdapter);
                        }
                        else
                            friendGithubAdapter.setItems(friendsList);
                    }
                });
            }
        });
    }

    public void addFriend(String u_id, String f_id) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://0yetb1moyh.execute-api.ap-northeast-2.amazonaws.com/dev/friend").newBuilder();

        String url = urlBuilder.toString();

        AddFriendModel data = new AddFriendModel();
        data.setUser_id(u_id);
        data.setFriend_id(f_id);
        Gson gson = new Gson();
        String json = gson.toJson(data, AddFriendModel.class);

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
                    // 추가 성공
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(activity.getApplicationContext(), "친구 " +f_id+"를 추가했습니다!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                    getFriends(u_id);
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
