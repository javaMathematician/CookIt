package org.slovenlypolygon.recipes.frontend.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.slovenlypolygon.recipes.MainActivity;
import org.slovenlypolygon.recipes.R;

public class SureClearSelectedQDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getString(R.string.resources);
        String message = getString(R.string.sure_reset_q);
        String accept = getString(R.string.reset_agree);
        String decline = getString(R.string.reset_disagree);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(accept, (dialog, id) -> ((MainActivity) getActivity()).sureClearSelected());
        builder.setNegativeButton(decline, (dialog, id) -> {
        });

        builder.setCancelable(true);
        return builder.create();
    }
}
