package org.slovenlypolygon.recipes.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.switchmaterial.SwitchMaterial;

import org.slovenlypolygon.recipes.R;
import org.slovenlypolygon.recipes.abstractfragments.SimpleCookItFragment;

public class SettingsFragment extends SimpleCookItFragment {
    private SwitchMaterial switchMaterial;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);
        setRetainInstance(true);

        switchMaterial = rootView.findViewById(R.id.download_pictures_switch);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable @javax.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            activity.getSharedPreferences("org.slovenlypolygon.recipes_preferences", Context.MODE_PRIVATE).edit().putBoolean("download_pictures", isChecked).apply();
            activity.notifySharedPreferencesChanged();
        });
    }
}

