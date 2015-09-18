package com.self.instagramviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.self.instagramviewer.rest.model.InstagramPhoto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ssaraf on 9/17/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    private static class ViewHolder {
        private ImageView ivPhoto;
        private TextView tvCaption;
    }

    public InstagramPhotosAdapter(Context context, ArrayList<InstagramPhoto> photos) {
        super(context, R.layout.item_photo, photos);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.item_photo, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        InstagramPhoto photo = getItem(position);
        Picasso.with(getContext()).load(photo.imageUrl).into(viewHolder.ivPhoto);
        viewHolder.tvCaption.setText(photo.caption);

        return convertView;

    }
}
