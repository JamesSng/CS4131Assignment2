package com.example.assignment2.onboarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.example.assignment2.MainActivity;
import com.example.assignment2.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        ViewPager viewPager = findViewById(R.id.viewPagerOnBoarding);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new StepOneFragment());
        viewPagerAdapter.addFragment(new StepTwoFragment());
        viewPagerAdapter.addFragment(new StepThreeFragment());
        viewPagerAdapter.addFragment(new StepFourFragment());

        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayoutIndicator);
        tabLayout.setupWithViewPager(viewPager);

        findViewById(R.id.button).setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("started_before", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putInt("started_before", 1);
            edit.apply();
            startActivity(new Intent(this, MainActivity.class));
        });

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int i) {
            return mList.get(i);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        public void addFragment(Fragment fragment) {
            mList.add(fragment);
        }


    }
}