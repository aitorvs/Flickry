package com.example.aitor.flickry.photos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.aitor.flickry.DataListAdapter;
import com.example.aitor.flickry.R;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;
import timber.log.Timber;

public final class PhotoAdapter extends DataListAdapter<Photo> {
    private final Picasso picasso;

    @Inject
    PhotoAdapter(Picasso picasso) {
        this.picasso = picasso;
    }

    @Override
    protected DataListViewHolder createNewViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_item, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    protected boolean areItemsTheSame(Photo oldItem, Photo newItem) {
        return oldItem.id().equals(newItem.id());
    }

    @Override
    protected boolean areContentsTheSame(Photo oldItem, Photo newItem) {
        return oldItem.url().equals(newItem.url());
    }

    public final void accept(FlickrData data) {
        Timber.d("accept: %d photos", data.photos().size());
        if (data.page() > 1) {
            addAll(data.photos());
        } else {
            replace(data.photos());
        }
    }

    private class PhotoViewHolder extends DataListViewHolder{
        private final ImageView image;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }

        @Override
        protected void bindDataToView(Photo photo) {
            picasso
                .load(photo.url())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .fit()
                .centerCrop()
                .into(image);
        }
    }
}
