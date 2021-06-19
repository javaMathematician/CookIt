package org.slovenlypolygon.cookit.welcomescreen;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
        Button previous = findViewById(R.id.previous_slide_button);
        SliderPagerAdapter adapter = new SliderPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(adapter);
        previous.setVisibility(View.GONE);
        tabLayout.setupWithViewPager(viewPager);

        next.setOnClickListener(view -> {
            int currentItem = viewPager.getCurrentItem();

            if (currentItem < adapter.getCount() - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            } else {
                finish();
            }
        });

        previous.setOnClickListener(view -> {
            int currentItem = viewPager.getCurrentItem();

            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                next.setText(position == adapter.getCount() - 1 ? R.string.close_help : R.string.next);
                previous.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
}
