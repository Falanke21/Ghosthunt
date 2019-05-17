package fall2018.csc2017.game_centre;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Model class, excluded from unit test.
 * Adapt from
 * http://xperience57.blogspot.com/2015/09/android-saving-bitmap-as-serializable.html
 *
 * A proxy bitmap class is the solution to bitmap's no serialization.
 */
class ProxyBitmap implements Serializable {
    /**
     * The int array of pixels to represent the bitmap.
     */
    private final int[] pixels;

    /**
     * The width and height of this bitmap.
     */
    private final int width, height;

    /**
     * Constructor of this proxy bitmap.
     * @param bitmap the bitmap that is ready to be stored.
     */
    ProxyBitmap(Bitmap bitmap) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
    }

    /**
     * Getter for this bitmap.
     * @return the bitmap of demanded.
     */
    Bitmap getBitmap() {
        if (pixels != null) {
            return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
        }
        return null;
    }
}
