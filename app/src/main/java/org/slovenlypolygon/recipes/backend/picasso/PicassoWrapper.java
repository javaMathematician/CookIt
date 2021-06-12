package org.slovenlypolygon.recipes.backend.picasso;

import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;

public class PicassoWrapper {
    private String imageURL;
    private ImageView imageView;

    public PicassoWrapper setImageView(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    public PicassoWrapper setImageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public void process() {
        Picasso picasso = Picasso.get();
        picasso.load(imageURL)
                .placeholder(R.drawable.loading_animation)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .fit()
                .centerCrop()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        picasso.setIndicatorsEnabled(false);
                        picasso.load(imageURL)
                                .placeholder(R.drawable.loading_animation)
                                .error(R.drawable.error_image)
                                .fit()
                                .centerCrop()
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                    }
                });
    }
}
