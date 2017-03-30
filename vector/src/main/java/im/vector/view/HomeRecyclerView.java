/* 
 * Copyright 2016 OpenMarket Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.matrix.androidsdk.util.Log;

import im.vector.R;

/**
 * Defines a custom Expandable listview.
 * It only tracks the touch move and up.
 */
public class HomeRecyclerView extends RecyclerView {

    boolean mIsDragging = false;

    // default constructor
    public HomeRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDragMode(boolean isDragging) {
        mIsDragging = isDragging;
    }

    private int mSelectedItem = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsDragging) {
            View child = findChildViewUnder(event.getX(), event.getY());
            ViewHolder holder = getChildViewHolder(child);

            if (null != holder) {
                Log.e("DTC", "onTouchEvent holder " + holder.getAdapterPosition());
                mSelectedItem = holder.getAdapterPosition();
            }

            final int action = event.getAction();

            if ((action == MotionEvent.ACTION_CANCEL) || (action == MotionEvent.ACTION_UP)) {
                mIsDragging = false;
            }
           return false;
        } else {
            return super.onTouchEvent(event);
        }
    }
}
