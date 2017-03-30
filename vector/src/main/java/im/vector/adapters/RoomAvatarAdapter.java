/*
 * Copyright 2017 Vector Creations Ltd
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

package im.vector.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.matrix.androidsdk.MXSession;
import org.matrix.androidsdk.data.Room;
import org.matrix.androidsdk.data.RoomSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import im.vector.Matrix;
import im.vector.R;
import im.vector.util.RoomUtils;
import im.vector.util.VectorUtils;

public class RoomAvatarAdapter extends AbsListAdapter<Room, RoomAvatarAdapter.RoomAvatarViewHolder> {

    private final Context mContext;
    private final MXSession mSession;

    /*
     * *********************************************************************************************
     * Constructor
     * *********************************************************************************************
     */

    public RoomAvatarAdapter(final Context context, final OnSelectItemListener<Room> listener) {
        super(R.layout.adapter_item_avatar_room_view, listener);
        mContext = context;
        mSession = Matrix.getInstance(context).getDefaultSession();
    }

    /*
     * *********************************************************************************************
     * Abstract methods implementation
     * *********************************************************************************************
     */

    @Override
    protected RoomAvatarViewHolder createViewHolder(View itemView) {
        return new RoomAvatarViewHolder(itemView);
    }

    @Override
    protected void populateViewHolder(RoomAvatarViewHolder viewHolder, Room item) {
        viewHolder.populateViews(item);
    }

    @Override
    protected List<Room> getFilterItems(List<Room> items, String pattern) {
        List<Room> filteredRoom = new ArrayList<>();
        for (final Room room : items) {

            final String roomName = VectorUtils.getRoomDisplayName(mContext, mSession, room);
            if (Pattern.compile(Pattern.quote(pattern), Pattern.CASE_INSENSITIVE)
                    .matcher(roomName)
                    .find()) {
                filteredRoom.add(room);
            }
        }
        return filteredRoom;
    }

    /*
     * *********************************************************************************************
     * View holder
     * *********************************************************************************************
     */

    class RoomAvatarViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar_room_layout)
        View vLayout;

        @BindView(R.id.room_avatar)
        ImageView vRoomAvatar;

        private RoomAvatarViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void populateViews(final Room room) {
            VectorUtils.loadRoomAvatar(mContext, mSession, vRoomAvatar, room);
        }
    }
}
