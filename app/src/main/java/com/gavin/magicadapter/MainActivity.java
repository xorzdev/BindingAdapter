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
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490951276314&di=be8ef05f74ab576234f00664ab97556a&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F0%2F50%2F5ffd1286284.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490951291472&di=2c818cffb380050a5044cd8602cfb3cf&imgtype=0&src=http%3A%2F%2Fimg.popo.cn%2Fuploadfile%2F2017%2F0217%2F1487316191770266.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490951300870&di=4963b101ddffdda57c659b9dc95f52e5&imgtype=0&src=http%3A%2F%2Fimage.3761.com%2Fnvxing%2F2016022923%2F2016022923025165610.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490951311588&di=0e3adeec2b0438f85fbd208e9802adc9&imgtype=0&src=http%3A%2F%2Fnpic7.edushi.com%2Fcn%2Fzixun%2Fzh-chs%2F2017-03%2F03%2F3822397-2017030223252852.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490951329510&di=1d5699eb36c5c5bd9848289e299cbfaf&imgtype=0&src=http%3A%2F%2Fd.5857.com%2Fqqny_170228%2Fdesk_009.jpg",
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
