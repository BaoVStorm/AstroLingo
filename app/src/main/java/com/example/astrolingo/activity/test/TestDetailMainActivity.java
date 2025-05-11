package com.example.astrolingo.activity.test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.astrolingo.R;
import com.example.astrolingo.apdapter.test.TestDetailAdapter;
import com.example.astrolingo.domain.test.testDetail_page;

import java.util.ArrayList;
import java.util.List;

public class TestDetailMainActivity extends AppCompatActivity  {
    ViewPager2 viewpager;

    List<testDetail_page> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_test_detail_main);

        viewpager = findViewById(R.id.viewpager);

        // add temp value
        list = new ArrayList<>();
        list.add(new testDetail_page("start_part"));
        list.add(new testDetail_page("part1"));
        list.add(new testDetail_page("part2"));

        // testDetail_page item = (testDetail_page) getIntent().getSerializableExtra("testItem");
            // add list img
            //


        TestDetailAdapter adapter = new TestDetailAdapter(list);
        viewpager.setAdapter(adapter);
    }
}
