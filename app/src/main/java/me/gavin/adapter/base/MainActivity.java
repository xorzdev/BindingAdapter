package me.gavin.adapter.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.gavin.adapter.base.base.BindingAdapter;
import me.gavin.adapter.base.base.IntConsumer;
import me.gavin.adapter.base.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private String[] names = {
            "处于 25-35 岁的你，开始买奢侈品了吗？",
            "有些法律上的事，非要解释给你听，你才知道理解错了",
            "要是早点知道这些，也不至于当初在校招季踩坑无数……",
            "孩子眼中的爸爸，可不只是「走，我们出去玩儿」就够了",
            "为什么胖这个字写成「月半」，不写成「月圆」或者「月全」？"
    };

    private String[] images = {
            "https://pic2.zhimg.com/v2-f474e73c7aabf49a96cb39f68b81b6e1.jpg",
            "https://pic2.zhimg.com/v2-f474e73c7aabf49a96cb39f68b81b6e1.jpg",
            "https://pic1.zhimg.com/v2-d081c48015edde0e8065709f864ee474.jpg",
            "https://pic3.zhimg.com/v2-4d8dcd6de27f81bc01435e0c6867b60e.jpg",
            "https://pic2.zhimg.com/v2-006da90be72f74b5e7fc49f1658896a1.jpg",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Random random = new Random();
        List<Story> userList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Story u = new Story();
            int index = random.nextInt(names.length);
            u.setTitle(i + " - " + names[index]);
            u.setImage(images[index]);
            userList.add(u);
        }

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        BindingAdapter adapter = new BindingAdapter<>(this, userList, R.layout.item_story);
        adapter.setOnItemClickListener(new IntConsumer() {
            @Override
            public void accept(int i) {
                Toast.makeText(MainActivity.this, i + " - 被点击了", Toast.LENGTH_SHORT).show();
            }
        });
        binding.recycler.setAdapter(adapter);
    }
}
