package org.slovenlypolygon.recipes.frontend.fragments.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;

import java.util.Objects;

public class RestartAppForThemeQDialog extends DialogFragment {
    boolean a = true;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "Перезапустить?";
        String message = "Для измененения темы придётся перезапустить приложение. Перезапустить сейчас или подождать следующего запуска?";
        String accept = "Сейчас";
        String decline = "Позже";


        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(title);
        builder.setMessage(message);
        ImageButton themeBtn = getActivity().findViewById(R.id.themeBtn);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Dark", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (sharedPreferences.getString("Dark", "Dark").equals("Dark")) {
            themeBtn.setBackgroundResource(R.drawable.light_mode);
            editor.putString("Dark", "Light");
        } else {
            themeBtn.setBackgroundResource(R.drawable.dark_mode);
            editor.putString("Dark", "Dark");
        }
        editor.apply();

        builder.setPositiveButton(accept, (dialog, id) -> {
            // if accepted then change the theme
            getActivity().setTheme(sharedPreferences.getString("Dark", "Dark").equals("Dark") ? R.style.Dark : R.style.Light);

            // restart activity
            Intent intent = getActivity().getIntent();
            getActivity().finish();
            startActivity(intent);
        });

        builder.setNegativeButton(decline, (dialog, id) -> {
        });

        builder.setCancelable(true);
        return builder.create();
    }
}
