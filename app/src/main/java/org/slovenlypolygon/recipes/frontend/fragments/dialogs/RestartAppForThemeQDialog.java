package org.slovenlypolygon.recipes.frontend.fragments.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class RestartAppForThemeQDialog extends DialogFragment {
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

    builder.setPositiveButton(accept, (dialog, id) -> {
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
