package kyr.company.pagedlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kyr.company.pagedlist.adapter.MyListAdapter;
import kyr.company.pagedlist.databinding.ActivityMainBinding;
import kyr.company.pagedlist.model.ItemModel;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyListAdapter(getApplicationContext());
        binding.recyclerView.setAdapter(adapter);

        MyViewModel viewModel= ViewModelProviders.of(this).get(MyViewModel.class);

        viewModel.getNews().observe(this, news -> {
//            PagedList<ItemModel> aa = news;
            adapter.submitList(news);
        });
//        binding.recyclerView.setAdapter(adapter);

    }

    @BindingAdapter("bind:publishedAt")
    public static void publishedAt(TextView view, String date) {
        view.setText(AppUtils.getDate(date) + " at " + AppUtils.getTime(date));
    }

    @BindingAdapter("bind:urlToImage")
    public static void urlToImage(ImageView view, String url) {
        Glide.with(MyApplication.context).load(url).override(250, 200).into(view);
    }
}