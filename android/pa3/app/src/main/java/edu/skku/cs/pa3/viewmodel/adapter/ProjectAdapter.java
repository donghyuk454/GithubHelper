package edu.skku.cs.pa3.viewmodel.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.skku.cs.pa3.R;
import edu.skku.cs.pa3.model.github.project.Project;
import edu.skku.cs.pa3.view.DetailActivity;

public class ProjectAdapter extends BaseAdapter {
    private List<Project> items;
    private Context mContext;
    private String avatar;
    private String username;

    private int contentSize;
    private boolean sizeChanged = false;

    public ProjectAdapter(Context mContext, List<Project> items, String avatar, String username){
        this.items = items;
        this.mContext = mContext;
        this.avatar = avatar;
        this.username = username;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_project, viewGroup, false);
        }

        TextView textView = view.findViewById(R.id.projectName);

        textView.setText(items.get(i).getName());

//        contentSize = view.getLayoutParams().height;
//
//        ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams();
//
//        Log.d("view size final !", String.valueOf(contentSize));
//        if (items.size() >= 3) {
//            layoutParams.height = 3*contentSize;
//        } else {
//            layoutParams.height = items.size()*contentSize;
//        }
//
//        viewGroup.setLayoutParams(layoutParams);
//        viewGroup.requestLayout();

        Log.d(username+"view size final !", String.valueOf(viewGroup.getLayoutParams().height));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                Project item = items.get(i);
                intent.putExtra("avatar_url", avatar);
                intent.putExtra("name", username);
                intent.putExtra("p_name", item.getName());
                intent.putExtra("full_name", item.getFull_name());
                intent.putExtra("html_url", item.getHtml_url());
                intent.putExtra("description", item.getDescription());
                mContext.startActivity(intent);
            }
        });

        return view;
    }
}
