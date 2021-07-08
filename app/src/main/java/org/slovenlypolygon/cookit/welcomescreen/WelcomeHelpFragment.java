package org.slovenlypolygon.cookit.welcomescreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.annotation.StringRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import org.slovenlypolygon.cookit.R;

import javax.annotation.Nullable;

public class WelcomeHelpFragment extends Fragment {
    @RawRes private static final int[] PAGE_IMAGE = {
            R.raw.ingredient_ex,
            R.raw.dish_ex,
            R.raw.step_ex,
            R.raw.step_ex,
            R.raw.step_ex,
            R.raw.step_ex
    };

    @StringRes private static final int[] PAGE_TITLES = {
            R.string.ingredients_help,
            R.string.dishes_help,
            R.string.step_by_step_help,
            R.string.favorite_dishes_help,
            R.string.favorite_ingredients_help,
            R.string.more_in_help
    };

    @StringRes private static final int[] PAGE_TEXT = {
            R.string.ingredients_content,
            R.string.dishes_content,
            R.string.step_by_step_content,
            R.string.favorite_dishes_content,
            R.string.favorite_ingredients_content,
            R.string.more_in_help_content
    };

    @ColorRes private static final int[] BACKGROUND_IMAGES = {
            R.color.first_color_help,
            R.color.second_color_help,
            R.color.third_color_help,
            R.color.first_color_help,
            R.color.second_color_help,
            R.color.third_color_help
    };

    private static final String ARG_POSITION = "slider-position";
    private int position;

    public static WelcomeHelpFragment newInstance(int position) {
        WelcomeHelpFragment fragment = new WelcomeHelpFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.welcome_help, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setBackground(AppCompatResources.getDrawable(requireActivity(), BACKGROUND_IMAGES[position]));
        view.<TextView>findViewById(R.id.welcome_help_screen_title).setText(PAGE_TITLES[position]);
        view.<TextView>findViewById(R.id.welcome_help_screen_content).setText(PAGE_TEXT[position]);
        view.<ImageView>findViewById(R.id.welcome_help_screen_image).setImageResource(PAGE_IMAGE[position]);
    }
}
