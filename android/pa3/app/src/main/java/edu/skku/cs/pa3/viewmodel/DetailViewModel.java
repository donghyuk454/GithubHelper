package edu.skku.cs.pa3.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BaseObservable;

import com.bumptech.glide.Glide;

import edu.skku.cs.pa3.R;

public class DetailViewModel extends BaseObservable {
    private Activity activity;

    public DetailViewModel (Activity activity) {
        this.activity = activity;
    }

    public void initView(String name, String p_name, String fullname, String html, String desc, String avatar) {
        TextView nameText = activity.findViewById(R.id.p_name);
        TextView fullText = activity.findViewById(R.id.p_fullname);
        TextView descText = activity.findViewById(R.id.p_desc);
        nameText.setText("name: " + p_name);
        fullText.setText("full name: " + fullname);
        descText.setText("description: " + desc);
        ImageView imageView = activity.findViewById(R.id.UserProfileImage);
        Glide.with(activity).load(avatar).into(imageView);
        TextView userName = activity.findViewById(R.id.userName);
        userName.setText(name);

        Button btn = activity.findViewById(R.id.gotopro);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(html));
                activity.startActivity(intent);
            }
        });
    }

}
