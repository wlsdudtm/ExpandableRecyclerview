package com.lge.exapandablerecyclerview;

import android.animation.ObjectAnimator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by wlsdud.choi on 2016-04-05.
 */
public class MyItemAnimator extends DefaultItemAnimator {

    private final String TAG = "[Simple][MyItemAnimator]";

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        Log.i(TAG, "animateAdd. holder : "+holder.getAdapterPosition()+", "+holder.getLayoutPosition());

        return super.animateAdd(holder);
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {

        Log.i(TAG, "animateRemove. holder : "+holder.getAdapterPosition()+", "+holder.getLayoutPosition());

        View view = holder.itemView;

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f, 0.0f).setDuration(100);
        alphaAnimator.setInterpolator(new DecelerateInterpolator());
        alphaAnimator.start();

        return true;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        Log.i(TAG, "animateMove. fromX, fromY, toX, toY : "+fromX+", "+fromY+", "+toX+", "+toY);

        return super.animateMove(holder, fromX, fromY, toX, toY);
    }
}
