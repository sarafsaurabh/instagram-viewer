package com.self.instagramviewer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.self.instagramviewer.rest.model.InstagramPhoto;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by ssaraf on 9/17/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    private static final Transformation IMG_ROUND_TRANSFORM = new RoundedTransformationBuilder()
            .borderColor(Color.BLACK)
            .cornerRadiusDp(30)
            .oval(false)
            .build();


    private static class ViewHolder {
        private ImageView ivPhoto;
        private TextView tvCaption;
        private TextView tvUsername;
        private TextView tvCreatedTime;
        private TextView tvLikesCount;
        private ImageView ivProfilePic;
    }

    public InstagramPhotosAdapter(Context context, ArrayList<InstagramPhoto> photos) {
        super(context, R.layout.item_photo, photos);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if(convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.item_photo, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvLikesCount = (TextView) convertView.findViewById(R.id.tvLikesCount);
            viewHolder.tvCreatedTime = (TextView) convertView.findViewById(R.id.tvCreatedTime);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        InstagramPhoto photo = getItem(position);
        Picasso.with(getContext()).load(photo.imageUrl)
                .placeholder(R.drawable.spinner).into(viewHolder.ivPhoto);
        viewHolder.tvCaption.setText(photo.caption);
        viewHolder.tvUsername.setText(photo.username);

        // 37 minutes ago => ["37", "minutes", "ago"]
        String[] createdTimeSplit = DateUtils.getRelativeTimeSpanString(photo.createdTime * 1000,
                System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_RELATIVE).toString().split(" ");

        viewHolder.tvCreatedTime.setText(createdTimeSplit[0] + createdTimeSplit[1].charAt(0));

        // Set the clock drawable as a component of the  TextView
        viewHolder.tvCreatedTime.setCompoundDrawablesWithIntrinsicBounds(
                getScaledClock(viewHolder), null, null, null);

        // Set the heart drawable as a component of the  TextView
        viewHolder.tvLikesCount.setCompoundDrawablesWithIntrinsicBounds(
                getScaledHeart(viewHolder), null, null, null);

        Picasso.with(getContext()).load(photo.profilePicUrl).placeholder(R.drawable.spinner)
                .transform(IMG_ROUND_TRANSFORM).into(viewHolder.ivProfilePic);

        String likeCount = String.valueOf(photo.likesCount) + " "
                + (photo.likesCount == 1 ? "like" : "likes");

        viewHolder.tvLikesCount.setText(likeCount);
        return convertView;

    }

    private ScaleDrawable getScaledClock(final ViewHolder viewHolder) {

        Drawable clock = this.getContext().getResources().getDrawable(R.drawable.clock);

        // Wrap to scale up to the TextView height
        final ScaleDrawable scaledDrawable = new ScaleDrawable(clock, Gravity.CENTER, 1F, 1F) {
            // Give this drawable a height and width being at
            // least the TextView height. It will be
            // used by TextView.setCompoundDrawablesWithIntrinsicBounds
            public int getIntrinsicHeight() {
                return Math.min(super.getIntrinsicHeight(),
                        viewHolder.tvCreatedTime.getLineHeight());
            };

            public int getIntrinsicWidth() {
                return Math.min(super.getIntrinsicHeight(),
                        viewHolder.tvCreatedTime.getLineHeight());
            };
        };

        // Set explicitly level else the default value
        // (0) will prevent .draw to effectively draw
        // the underlying Drawable
        scaledDrawable.setLevel(10000);

        return scaledDrawable;
    }

    private ScaleDrawable getScaledHeart(final ViewHolder viewHolder) {

        Drawable clock = this.getContext().getResources().getDrawable(R.drawable.heart_small_blue);

        // Wrap to scale up to the TextView height
        final ScaleDrawable scaledDrawable = new ScaleDrawable(clock, Gravity.CENTER, 1F, 1F) {
            // Give this drawable a height and width being at
            // least the TextView height. It will be
            // used by TextView.setCompoundDrawablesWithIntrinsicBounds
            public int getIntrinsicHeight() {
                return Math.min(super.getIntrinsicHeight() - 15,
                        viewHolder.tvCreatedTime.getLineHeight() - 15);
            };

            public int getIntrinsicWidth() {
                return Math.min(super.getIntrinsicHeight() - 15,
                        viewHolder.tvCreatedTime.getLineHeight() - 15);
            };
        };

        // Set explicitly level else the default value
        // (0) will prevent .draw to effectively draw
        // the underlying Drawable
        scaledDrawable.setLevel(10000);

        return scaledDrawable;
    }
}
