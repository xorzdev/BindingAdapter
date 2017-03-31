package com.gavin.magicadapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String[] names = new String[] {
            "张三", "李四", "王六", "亚索", "泰达米尔"
    };

    private String[] images = new String[] {
            "http://img04.sogoucdn.com/app/a/100520024/aaaa5ece8c214c800d8932029dd533d4",
            "http://img02.sogoucdn.com/app/a/100520024/4f0c1a1eb2423dd56acd655ea5b291ce",
            "http://img02.sogoucdn.com/app/a/100520024/48f346864ba87289fe755986f5fc05e3",
            "http://img04.sogoucdn.com/app/a/100520024/acfeb1fdb3ef946561db8d8ea77b4152",
            "http://img02.sogoucdn.com/app/a/100520024/77d27afcb273278189591baf724d3bcf",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Random random = new Random();
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User u = new User();
            u.setName(names[random.nextInt(names.length)]);
            u.setHead(images[random.nextInt(images.length)]);
            userList.add(u);
        }

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new BindingAdapter<>(this, userList, R.layout.item_user, BR.item));
    }
}
