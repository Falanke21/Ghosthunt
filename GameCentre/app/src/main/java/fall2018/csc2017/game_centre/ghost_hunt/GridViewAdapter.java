package fall2018.csc2017.game_centre.ghost_hunt;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * View
 *
 * Adapter for the grid view.
 */
class GridViewAdapter extends BaseAdapter {

    /**
     * A list of image views of the tiles in the grid.
     */
    private ArrayList<ImageView> tileViews;

    /**
     * Width of each tile.
     */
    private int tileWidth;

    /**
     * Height of each tile.
     */
    private int tileHeight;

    GridViewAdapter(ArrayList<ImageView> tileViews, int tileWidth, int tileHeight) {
        this.tileViews = tileViews;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    /**
     * Get number of views in the grid.
     *
     * @return number of views
     */
    @Override
    public int getCount() {
        return tileViews.size();
    }

    /**
     * Get the image view at a specific position.
     *
     * @param position position of the tile
     * @return the image view at the specified position
     */
    @Override
    public Object getItem(int position) {
        return tileViews.get(position);
    }

    /**
     * Get the row id associated with the specified position in the view list.
     *
     * @param position the position of the item
     * @return the id of the item at the specified position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the tile at the specified position
     *
     * @param position    the position of the tile
     * @param convertView the old view to reuse, if possible
     * @param parent      the parent that this view will eventually be attached to
     * @return a View corresponding to the tile at the specified position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView tileView;
        if (convertView == null) {
            tileView = tileViews.get(position);
        } else {
            tileView = (ImageView) convertView;
        }
        AbsListView.LayoutParams params =
                new AbsListView.LayoutParams(tileWidth, tileHeight);
        tileView.setLayoutParams(params);
        return tileView;
    }
}
