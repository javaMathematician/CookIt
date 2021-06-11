package org.slovenlypolygon.recipes.backend.picasso;

import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.slovenlypolygon.recipes.R;

public class PicassoBuilder {
    private String imageURL;
    private ImageView imageView;
    private boolean downloadQ;

    public PicassoBuilder setImageView(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    public PicassoBuilder setImageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public PicassoBuilder setDownloadQ(boolean downloadQ) {
        this.downloadQ = downloadQ;
        return this;
    }

    public void process() {
        Picasso picasso = Picasso.get();

        picasso.setIndicatorsEnabled(false);
        picasso.load(imageURL)
                .placeholder(R.drawable.loading_animation)
                .networkPolicy(downloadQ ? NetworkPolicy.OFFLINE : NetworkPolicy.NO_STORE)
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
