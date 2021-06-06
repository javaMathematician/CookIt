package org.slovenlypolygon.recipes.backend.computervision;

import android.graphics.Bitmap;
import android.graphics.Matrix;

class BitmapUtils {
    static Bitmap scaleBitmap(Bitmap input) {
        final int currentWidth = input.getWidth();
        final int currentHeight = input.getHeight();
        final int currentPixels = currentWidth * currentHeight;

        final long maxPixels = 1024 * 900 * 4;

        if (currentPixels <= maxPixels) {
            return input;
        }

        final double scaleFactor = Math.sqrt(maxPixels / (double) currentPixels);
        final int newWidthPx = (int) Math.floor(currentWidth * scaleFactor);
        final int newHeightPx = (int) Math.floor(currentHeight * scaleFactor);

        return Bitmap.createScaledBitmap(input, newWidthPx, newHeightPx, true);
    }

    static Bitmap rotate(Bitmap source, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
    }
}
