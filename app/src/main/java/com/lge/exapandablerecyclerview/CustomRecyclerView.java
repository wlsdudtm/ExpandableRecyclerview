package com.lge.exapandablerecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
/**
 * Created by wlsdud.choi on 2016-03-29.
 */
public class CustomRecyclerView extends RecyclerView {

    private static final String TAG = "[Simple][CustomRecyclerView]";

    public CustomRecyclerView(Context context) {
        super(context);
        setItemAnimator(new MyItemAnimator());
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setItemAnimator(new MyItemAnimator());
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setItemAnimator(new MyItemAnimator());
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        Log.i(TAG, "setAdapter");

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelperCallback((ItemAdapter)adapter));
        helper.attachToRecyclerView(this);

    }

}
