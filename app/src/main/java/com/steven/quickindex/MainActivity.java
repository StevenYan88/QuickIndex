package com.steven.quickindex;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.steven.quickindex.bean.Person;
import com.steven.quickindex.commonAdapter.CommonRecycleAdapter;
import com.steven.quickindex.commonAdapter.CommonViewHolder;
import com.steven.quickindex.commonAdapter.DefaultItemDecoration;
import com.steven.quickindex.utils.DataUtil;
import com.steven.quickindex.widget.LetterSideBarView;
import com.steven.quickindex.widget.SideBarTouchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRv;
    private LetterSideBarView mLetterSideBarView;
    private TextView mIndexTv;
    private List<Person> mList;
    private Handler mHandler = new Handler();
    private boolean isScale = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRv = findViewById(R.id.rv);
        mLetterSideBarView = findViewById(R.id.letterSideBarView);
        mIndexTv = findViewById(R.id.indexTv);
        initRv();
        mLetterSideBarView.setOnSideBarTouchListener(new SideBarTouchListener() {
            @Override
            public void onTouch(String letter, boolean isTouch) {
                for (int i = 0; i < mList.size(); i++) {
                    if (letter.equals(mList.get(i).getPinyin().charAt(0) + "")) {
                        mRv.scrollToPosition(i);
                        break;
                    }
                }
                showCurrentIndex(letter);
            }
        });
    }

    private void showCurrentIndex(String letter) {
        mIndexTv.setText(letter);
        if (!isScale) {
            isScale = true;
            ViewCompat.animate(mIndexTv)
                    .scaleX(1f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(380)
                    .start();
            ViewCompat.animate(mIndexTv)
                    .scaleY(1f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(380)
                    .start();
        }

        mHandler.removeCallbacksAndMessages(null);
        // 延时隐藏
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewCompat.animate(mIndexTv)
                        .scaleX(0f)
                        .setDuration(380)
                        .start();
                ViewCompat.animate(mIndexTv)
                        .scaleY(0f)
                        .setDuration(380)
                        .start();
                isScale = false;
            }
        }, 380);
    }

    private void initRv() {
        mList = new ArrayList<>();
        for (int i = 0; i < DataUtil.testData3.length; i++) {
            Person person = new Person(DataUtil.testData3[i]);
            mList.add(person);
        }
        //排序
        Collections.sort(mList);
        PersonAdapter adapter = new PersonAdapter(this, mList, R.layout.person_recycler_item);
        mRv.setAdapter(adapter);
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRv.addItemDecoration(new DefaultItemDecoration(this, R.drawable.default_item));
    }

    private class PersonAdapter extends CommonRecycleAdapter<Person> {
        public PersonAdapter(Context context, List<Person> mData, int layoutId) {
            super(context, mData, layoutId);
        }

        @Override
        protected void convert(CommonViewHolder holder, Person person, int position) {
            String currentWord = person.getPinyin().charAt(0) + "";
            if (position > 0) {
                String lastWord = mList.get(position - 1).getPinyin().charAt(0) + "";
                //拿当前的首字母和上一个首字母比较,与首字母相同，需要隐藏当前item的索引
                holder.setVisibility(R.id.indexTv, currentWord.equals(lastWord) ? View.GONE : View.VISIBLE);
            } else {
                holder.setVisibility(R.id.indexTv, View.VISIBLE);
            }
            holder.setText(R.id.indexTv, currentWord);
            holder.setText(R.id.userNameTv, person.getName());
        }
    }
}
