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

package im.vector.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.matrix.androidsdk.data.Room;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import im.vector.R;
import im.vector.adapters.AbsListAdapter;
import im.vector.adapters.RoomAvatarAdapter;
import im.vector.view.HomeRecyclerView;

public class FavouritesFragment extends AbsHomeFragment {

    @BindView(R.id.recyclerview_1)
    HomeRecyclerView mRecyclerView1;

    @BindView(R.id.recyclerview_2)
    HomeRecyclerView mRecyclerView2;

    /*
     * *********************************************************************************************
     * Static methods
     * *********************************************************************************************
     */

    public static FavouritesFragment newInstance() {
        return new FavouritesFragment();
    }

    /*
     * *********************************************************************************************
     * Fragment lifecycle
     * *********************************************************************************************
     */

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViews();

        if (savedInstanceState != null) {
            // Restore adapter items
        }
    }

    /*
     * *********************************************************************************************
     * Abstract methods implementation
     * *********************************************************************************************
     */

    @Override
    protected void onMarkAllAsRead() {

    }

    @Override
    protected void onFilter(String pattern, OnFilterListener listener) {
        Toast.makeText(getActivity(), "favourite onFilter "+pattern, Toast.LENGTH_SHORT).show();
        //TODO adapter getFilter().filter(pattern, listener)
        //TODO call listener.onFilterDone(); when complete
        listener.onFilterDone(0);
    }

    @Override
    protected void onResetFilter() {

    }

    /*
     * *********************************************************************************************
     * UI management
     * *********************************************************************************************
     */

    private void initViews() {
        Collection<Room> rooms =  mSession.getDataHandler().getStore().getRooms();

        mRecyclerView1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView1.setHasFixedSize(true);
        mRecyclerView1.setNestedScrollingEnabled(false);

        final RoomAvatarAdapter adapter1 = new RoomAvatarAdapter(getActivity(), new AbsListAdapter.OnSelectItemListener<Room>() {
            @Override
            public void onSelectItem(Room item, int position) {

            }
        });

        adapter1.setOnLongClickListener(new AbsListAdapter.OnLongClickItemListener<Room>() {
            @Override
            public void onItemLongClick(Room room, int position) {
                mRecyclerView1.setDragMode(true);
            }
        });

        mRecyclerView1.setAdapter(adapter1);
        adapter1.setItems(new ArrayList<>(rooms));


        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setNestedScrollingEnabled(false);

        final RoomAvatarAdapter adapter2 = new RoomAvatarAdapter(getActivity(), new AbsListAdapter.OnSelectItemListener<Room>() {
            @Override
            public void onSelectItem(Room item, int position) {

            }
        });
        mRecyclerView2.setAdapter(adapter2);
        adapter2.setItems(new ArrayList<>(rooms));

        adapter2.setOnLongClickListener(new AbsListAdapter.OnLongClickItemListener<Room>() {
            @Override
            public void onItemLongClick(Room room, int position) {
                mRecyclerView2.setDragMode(true);
            }
        });
    }
}
