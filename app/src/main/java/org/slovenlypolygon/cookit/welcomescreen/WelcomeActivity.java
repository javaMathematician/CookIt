package org.slovenlypolygon.cookit.welcomescreen;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.slovenlypolygon.cookit.R;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable @javax.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome_carcass);

        ViewPager viewPager = findViewById(R.id.pagerIntroSlider);
        TabLayout tabLayout = findViewById(R.id.tabs);

        Button next = findViewById(R.id.next_slide_button);
        Button close = findViewById(R.id.close_slide_button);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 6;
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return WelcomeHelpFragment.newInstance(position);
            }
        };

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        next.setOnClickListener(view -> {
            int currentItem = viewPager.getCurrentItem();

            if (currentItem < adapter.getCount() - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            } else {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        close.setOnClickListener(view -> finish());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                boolean temp = position == adapter.getCount() - 1;
                next.setText(temp ? R.string.close_help : R.string.next);
                close.setVisibility(temp ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected void onDestroy() {
        getSharedPreferences("org.slovenlypolygon.cookit_preferences", Context.MODE_PRIVATE).edit().putBoolean("first_launch", false).apply();

        super.onDestroy();
    }
}
