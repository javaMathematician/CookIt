package org.slovenlypolygon.recipes.frontend.frontendcards;

import android.content.Context;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatImageView;
import org.apache.commons.io.FileUtils;
import org.slovenlypolygon.recipes.backend.utils.Converters;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class StructuredCardImage extends AppCompatImageView {
    private final Context context;
    private String imageURL;

    public StructuredCardImage(Context context) {
        super(context);
        this.context = context;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void makeContent() {
        int margin = Converters.fromDP(context, 8);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, MATCH_PARENT);
        layoutParams.setMargins(margin, margin, margin, margin);
        layoutParams.weight = 1;

        this.setLayoutParams(layoutParams);
        this.setImageResource(android.R.drawable.ic_menu_compass);
    }
}
