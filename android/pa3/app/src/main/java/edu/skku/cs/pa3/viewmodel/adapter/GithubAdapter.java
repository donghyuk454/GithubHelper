package edu.skku.cs.pa3.viewmodel.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.skku.cs.pa3.R;
import edu.skku.cs.pa3.model.github.GithubInfo;


public class GithubAdapter extends BaseAdapter {

    private List<GithubInfo> items;
    private Context mContext;

    public GithubAdapter(List<GithubInfo> items, Context mContext) {
        this.items = items;
        this.mContext = mContext;
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
            view = layoutInflater.inflate(R.layout.item_friend, viewGroup, false);
        }

        // avatar image 넣기
        GithubInfo info = items.get(i);

        ImageView imageView = view.findViewById(R.id.profileImage);
        Glide.with(view).load(info.getAvatar_url()).into(imageView);

        TextView name = view.findViewById(R.id.p_name);
        name.setText(info.getName());

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(info.getHtml_url()));
                mContext.startActivity(intent);
            }
        });

        if (items.get(i).getProjects().size() == 0) {
            TextView t = view.findViewById(R.id.textView2);
            t.setText("No project");
        }
        ListView listView = view.findViewById(R.id.projects);
        ListView listView1 = viewGroup.findViewById(R.id.friendList);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                listView1.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        ProjectAdapter projectAdapter = new ProjectAdapter(mContext, items.get(i).getProjects(), items.get(i).getAvatar_url(), items.get(i).getName());
        listView.setAdapter(projectAdapter);

        int height = 0;
        int wid = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        if (projectAdapter.getCount() >= 3) {
            for (int j = 0; j < 3; j++) {
                View v = projectAdapter.getView(j, null, listView);
                v.measure(wid, View.MeasureSpec.UNSPECIFIED);
                height += v.getMeasuredHeight();
            }
        }
        else
            for (int j = 0; j < projectAdapter.getCount(); j++) {
                View v = projectAdapter.getView(j, null, listView);
                v.measure(wid, View.MeasureSpec.UNSPECIFIED);
                height += v.getMeasuredHeight();
            }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = height;
        listView.setLayoutParams(params);

        return view;
    }

    public void setItems(List<GithubInfo> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
