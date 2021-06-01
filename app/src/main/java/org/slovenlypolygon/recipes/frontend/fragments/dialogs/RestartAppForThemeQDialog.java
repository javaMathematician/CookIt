package org.slovenlypolygon.recipes.frontend.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;

public class RestartAppForThemeQDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.restart_q)
                .setMessage(R.string.restart_theme_q)
                .setPositiveButton(R.string.agree, (dialog, id) -> ((MainActivity) getActivity()).sureChangeThemeAndRestart())
                .setNegativeButton(R.string.disagree, (dialog, id) -> {
                })
                .setCancelable(true);

        return builder.create();
    }
}
