package com.lge.exapandablerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lge.exapandablerecyclerview.model.ChildItem;
import com.lge.exapandablerecyclerview.model.Item;
import com.lge.exapandablerecyclerview.model.ParentItem;

import java.util.ArrayList;

/**
 * Created by wlsdud.choi on 2016-04-01.
 */
public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchHelperListener {

    private final String TAG = "[Simple][NewItemAdaptr]";
    private final int PARENT_ITEM_VIEW = 0;
    private final int CHILD_ITEM_VIEW = 1;

    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Item> visibleItems = new ArrayList<>();

    public ItemAdapter(){
        char alphabet = 'A';
        for(int i = 0; i < 10; i++){
            Item item1 = new ParentItem((char)(alphabet+i)+"", PARENT_ITEM_VIEW);
            Item item2 = new ChildItem((char)(alphabet+i)+"-1", CHILD_ITEM_VIEW);
            Item item3 = new ChildItem((char)(alphabet+i)+"-2", CHILD_ITEM_VIEW);
            Item item4 = new ChildItem((char)(alphabet+i)+"-3", CHILD_ITEM_VIEW);

            items.add(item1);
            items.add(item2);
            items.add(item3);
            items.add(item4);

            visibleItems.add(item1);
            visibleItems.add(item2);
            visibleItems.add(item3);
            visibleItems.add(item4);
        }
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount. count : "+ visibleItems.size());

        return visibleItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.i(TAG, "getItemViewType. position : "+position+", viewType : "+visibleItems.get(position).viewType+", item : "+ visibleItems.get(position).name);

        return visibleItems.get(position).viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder. viewType : "+viewType);
        RecyclerView.ViewHolder viewHolder = null;

        switch(viewType){
            case PARENT_ITEM_VIEW:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                viewHolder = new ParentItemVH(view);
                break;
            case CHILD_ITEM_VIEW:
                View subview = LayoutInflater.from(parent.getContext()).inflate(R.layout.subitem, parent, false);
                viewHolder = new ChildItemVH(subview);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder. position : "+position);
        if(holder instanceof ParentItemVH){
            Log.i(TAG, "onBindViewHolder. parentItem. "+visibleItems.get(position).name);
            ParentItemVH parentItemVH = (ParentItemVH)holder;

            parentItemVH.name.setText(visibleItems.get(position).name);
            parentItemVH.arrow.setTag(holder);

            parentItemVH.arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int holderPosition = ((ParentItemVH)v.getTag()).getAdapterPosition();
                    if(((ParentItem)visibleItems.get(holderPosition)).visibilityOfChildItems){
                        collapseChildItems(holderPosition);
                    }else{
                        expandChildItems(holderPosition);
                    }
                }
            });

            parentItemVH.itemView.setTag(holder);
            if(parentItemVH.getItemViewType() == PARENT_ITEM_VIEW) {
                parentItemVH.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int holderPosition = ((ParentItemVH)v.getTag()).getAdapterPosition();
                        if(((ParentItem)visibleItems.get(holderPosition)).visibilityOfChildItems){
                            collapseChildItems(holderPosition);
                        }
                        return true;
                    }
                });
            }

        }else if(holder instanceof ChildItemVH){
            Log.i(TAG, "onBindViewHolder. sub item. "+visibleItems.get(position).name);

            ((ChildItemVH)holder).name.setText(visibleItems.get(position).name);
        }
    }

    private void collapseChildItems(int position){
        Log.i(TAG, "collapseChildItems");
        ParentItem parentItem = (ParentItem)visibleItems.get(position);
        parentItem.visibilityOfChildItems = false;

        int subItemSize = getVisibleChildItemSize(position);
        for(int i = 0; i < subItemSize; i++){
            parentItem.unvisibleChildItems.add((ChildItem) visibleItems.get(position + 1));
            visibleItems.remove(position + 1);
        }

        notifyItemRangeRemoved(position + 1, subItemSize);
    }

    private int getVisibleChildItemSize(int parentPosition){
        int count = 0;
        parentPosition++;
        while(true){
            if(parentPosition == visibleItems.size() || visibleItems.get(parentPosition).viewType == PARENT_ITEM_VIEW){
                break;
            }else{
                parentPosition++;
                count++;
            }
        }
        return count;
    }

    private void expandChildItems(int position){
        Log.i(TAG, "expandChildItems");

        ParentItem parentItem = (ParentItem)visibleItems.get(position);
        parentItem.visibilityOfChildItems = true;
        int childSize = parentItem.unvisibleChildItems.size();

        for(int i = childSize - 1; i >= 0; i--){
            visibleItems.add(position + 1, parentItem.unvisibleChildItems.get(i));
        }
        parentItem.unvisibleChildItems.clear();

        notifyItemRangeInserted(position + 1, childSize);
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        Log.i(TAG, "onItemMove. fromPosition : "+fromPosition+", toPosition : "+toPosition);
        Log.i(TAG, "onItemMove. fromItem : "+visibleItems.get(fromPosition).name+", toItem : "+visibleItems.get(toPosition).name);

        if(fromPosition < 0 || fromPosition >= visibleItems.size() || toPosition < 0 || toPosition >= visibleItems.size()){
            return false;
        }

        Item fromItem = visibleItems.get(fromPosition);

        if(visibleItems.get(fromPosition).viewType == CHILD_ITEM_VIEW){
            if(fromPosition <= 0 || toPosition <= 0){
                return false;
            }
            Log.i(TAG, "onItemMove. remove add");

            visibleItems.remove(fromPosition);
            visibleItems.add(toPosition, fromItem);

            notifyItemMoved(fromPosition, toPosition);

        }else{
            if(visibleItems.get(fromPosition).viewType == visibleItems.get(toPosition).viewType) {
                if(fromPosition > toPosition){
                    Log.i(TAG, "onItemMove. remove add");

                    visibleItems.remove(fromPosition);
                    visibleItems.add(toPosition, fromItem);

                    notifyItemMoved(fromPosition, toPosition);
                }else{
                    int toParentPosition = getParentPosition(toPosition);
                    int toLastchildSize = getVisibleChildItemSize(toParentPosition);
                    Log.i(TAG, "onItemMove. lastChild : "+toLastchildSize);
                    if(toLastchildSize == 0){
                        Log.i(TAG, "onItemMove. remove add");

                        visibleItems.remove(fromPosition);
                        visibleItems.add(toPosition, fromItem);

                        notifyItemMoved(fromPosition, toPosition);
                    }

                }
            }else{
                if(fromPosition < toPosition){
                    int toParentPosition = getParentPosition(toPosition);
                    int toLastchildPosition = getVisibleChildItemSize(toParentPosition) + toParentPosition;
                    Log.i(TAG, "onItemMove. lastChild : "+toLastchildPosition);

                    if(toLastchildPosition == toPosition) {
                        Log.i(TAG, "onItemMove. remove add");

                        visibleItems.remove(fromPosition);
                        visibleItems.add(toPosition, fromItem);

                        notifyItemMoved(fromPosition, toPosition);
                    }
                }
            }
        }

        for(int i = 0; i < visibleItems.size(); i++){
            Log.i(TAG, "onItemMove : "+visibleItems.get(i).name);
        }

        return true;
    }

    @Override
    public void onItemRemove(int position) {
        Log.i(TAG, "onItemRemove. item : "+visibleItems.get(position).name);
        switch(visibleItems.get(position).viewType){
            case PARENT_ITEM_VIEW:
                int childItemSize = getVisibleChildItemSize(position);

                for(int i = 0; i <= childItemSize; i++){
                    visibleItems.remove(position);
                }
                notifyItemRangeRemoved(position, childItemSize + 1);

                break;
            case CHILD_ITEM_VIEW:
                visibleItems.remove(position);
                notifyItemRemoved(position);
                break;
        }
    }

    private int getParentPosition(int position){
        while(true){
            if(visibleItems.get(position).viewType == PARENT_ITEM_VIEW){
                break;
            }else{
                position--;
            }
        }
        return position;
    }

    public class ParentItemVH extends RecyclerView.ViewHolder {

        TextView name;
        ImageButton arrow;

        public ParentItemVH(View itemView) {
            super(itemView);
            Log.i(TAG, "ParentItemVH");
            name = (TextView)itemView.findViewById(R.id.item_name);
            arrow = (ImageButton)itemView.findViewById(R.id.item_arrow);
        }
    }

    public class ChildItemVH extends RecyclerView.ViewHolder {

        TextView name;

        public ChildItemVH(View itemView) {
            super(itemView);
            Log.i(TAG, "ChildItemVH");
            name = (TextView)itemView.findViewById(R.id.subitem_name);
        }
    }
}
