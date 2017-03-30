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
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.util.Log;

import im.vector.R;
import im.vector.adapters.AbsListAdapter;
import im.vector.adapters.RoomAvatarAdapter;

/**
 * Defines a custom Expandable listview.
 * It only tracks the touch move and up.
 */
public class HomeRecyclerView extends RecyclerView {

    public interface OnDragListener {
        void onDrag(Room room, int x, int y);
    }

    boolean mIsDragging = false;

    // default constructor
    public HomeRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDragMode(boolean isDragging) {
        mIsDragging = isDragging;
    }

    private int mGenuineItem = -1;
    private int mCurPosition = -1;
    private Room mSelectedRoom = null;

    private boolean mIgnoreScrollEvent = false;

    public OnDragListener mListener;

    public void scrollTo(Room room, float X, float Y) {
        View child = findChildViewUnder(X, Y);
        ViewHolder holder = getChildViewHolder(child);

        if (null != holder) {
            if (-1 == mGenuineItem) {
                mCurPosition = mGenuineItem = holder.getAdapterPosition();

                if (null == room) {
                    mSelectedRoom = ((RoomAvatarAdapter)getAdapter()).getItem(mCurPosition);
                } else {
                    mSelectedRoom = room;
                    ((RoomAvatarAdapter)getAdapter()).insertAt(room, mGenuineItem);
                }
                Log.e("DTC", "the user selects the item " + mGenuineItem);
            } else {
                int newPosition = holder.getAdapterPosition();

                if ((newPosition != RecyclerView.NO_POSITION) && (mGenuineItem != newPosition)) {
                    Log.e("DTC", "the user move  the item " + mGenuineItem + " to " + newPosition);
                    ((RoomAvatarAdapter)getAdapter()).move(mGenuineItem, newPosition);
                    mGenuineItem = newPosition;
                    mSelectedRoom = ((RoomAvatarAdapter)getAdapter()).getItem(mGenuineItem);
                }
            }

            final LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();

            final int firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
            final int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();

            if ((X < 100) && (0 != firstVisibleItem)) {
                layoutManager.scrollToPosition(firstVisibleItem - 1);
                mIgnoreScrollEvent = true;
            } else if (((X + 100) > this.getWidth()) && (lastVisibleItem < (getAdapter().getItemCount() - 1))) {
                Log.e("DTC", " scroll top next");
                layoutManager.scrollToPosition(lastVisibleItem + 1);
                mIgnoreScrollEvent = true;
            }

            if (mIgnoreScrollEvent) {
                this.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIgnoreScrollEvent = false;
                    }
                }, 100);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsDragging) {
            if (!mIgnoreScrollEvent) {
                View child = findChildViewUnder(event.getX(), event.getY());

                if (null == child) {
                    int[] loc = new int[2];
                    this.getLocationOnScreen(loc);

                    ((RoomAvatarAdapter)getAdapter()).remove(mSelectedRoom);

                    mListener.onDrag(mSelectedRoom,
                            (int)(loc[0] + event.getX()),
                            (int)(loc[1] + event.getY())
                            );
                    return false;
                }

                scrollTo(null, event.getX(), event.getY());
            }

            final int action = event.getAction();

            if ((action == MotionEvent.ACTION_CANCEL) || (action == MotionEvent.ACTION_UP)) {
                mIsDragging = false;
                mIgnoreScrollEvent = false;
                mGenuineItem = -1;
            }
           return false;
        } else {
            return super.onTouchEvent(event);
        }
    }
}
