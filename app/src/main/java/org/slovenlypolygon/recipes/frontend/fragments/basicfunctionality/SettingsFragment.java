package org.slovenlypolygon.recipes.frontend.fragments.basicfunctionality;

import android.graphics.Color;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.slovenlypolygon.recipes.R;

public class SettingsFragment extends PreferenceFragmentCompat {
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

    private void changeBackgroundColor(int selectedColor) {
        System.out.println(Color.valueOf(selectedColor));
    }
}