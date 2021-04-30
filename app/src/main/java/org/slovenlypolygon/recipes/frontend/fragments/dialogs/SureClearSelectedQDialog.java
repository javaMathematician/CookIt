package org.slovenlypolygon.recipes.frontend.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.slovenlypolygon.recipes.MainActivity;

import java.util.Objects;

public class SureClearSelectedQDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "Сбросить?";
        String message = "Вы действительно хотите сбросить выбранные компоненты? Это действие нельзя будет отменить.";
        String accept = "Да, хочу сбросить";
        String decline = "Нет, я передумал";

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(accept, (dialog, id) -> {
            ((MainActivity) getActivity()).sureClearSelected();
        });
        builder.setNegativeButton(decline, (dialog, id) -> {
        });

        builder.setCancelable(true);
        return builder.create();
    }
}
