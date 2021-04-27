package org.slovenlypolygon.recipes.frontend.fragments.dialogs;

import android.app.Dialog;
import android.content.Context;
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
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setTitle("Перезапустить?")
                .setMessage("Для измененения темы придётся перезапустить приложение. Перезапустить сейчас или подождать следующего запуска?")
                .setPositiveButton("Сейчас", (dialog, id) -> ((MainActivity) getActivity()).sureChangeTheme())
                .setNegativeButton("Позже", (dialog, id) -> { })
                .setCancelable(true);

        return builder.create();
    }
}
