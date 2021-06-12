package org.slovenlypolygon.recipes.settings;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private MainActivity activity;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        setRetainInstance(false);

        getPreferenceScreen().findPreference("dark_theme_accent_color").setOnPreferenceClickListener(preference -> {
            ColorPickerDialogBuilder
                    .with(requireContext())
                    .setTitle("Выберите цвет")
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(20)
                    .setPositiveButton(getString(R.string.confirm), (dialog, selectedColor, allColors) -> changeBackgroundColor(selectedColor))
                    .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {})
                    .build()
                    .show();

            return false;
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = (MainActivity) getActivity();
    }

    private void changeBackgroundColor(int selectedColor) {
        System.out.println(Color.valueOf(selectedColor));
    }

    @Override
    public void onResume() {
        super.onResume();

        activity.getSupportActionBar().setTitle(R.string.app_name);
        activity.getSearchView().setVisibility(View.GONE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        activity.notifySharedPreferencesChanged(sharedPreferences, key);
    }
}