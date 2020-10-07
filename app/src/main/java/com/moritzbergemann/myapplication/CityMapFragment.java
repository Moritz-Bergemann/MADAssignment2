package com.moritzbergemann.myapplication;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moritzbergemann.myapplication.model.GameData;
import com.moritzbergemann.myapplication.model.GameMap;
import com.moritzbergemann.myapplication.model.MapElement;
import com.moritzbergemann.myapplication.model.Structure;

public class CityMapFragment extends Fragment {
    private static final String TAG = "CityMapFragment";


    private RecyclerView mMapRecyclerView;

    public CityMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment
     *
     * @return A new instance of fragment CityMapFragment.
     */
    public static CityMapFragment newInstance() {
        return new CityMapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Setting up recycler view
        mMapRecyclerView = view.findViewById(R.id.mapRecyclerView);
        mMapRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
                GameData.get().getMap().getMapHeight(), RecyclerView.HORIZONTAL, false));


        CityMapAdapter cityMapAdapter = new CityMapAdapter(GameData.get().getMap(), getActivity());
        mMapRecyclerView.setAdapter(cityMapAdapter);
    }

    private class CityMapAdapter extends RecyclerView.Adapter<CityMapAdapter.MapElementViewHolder> {
        private GameMap mGameMap;
        private Activity activity;

        /**
         * Default constructor - takes in information on data to represent (here the map elements)
         *  and the calling activity for context awareness
         */
        public CityMapAdapter(GameMap mapData, Activity activity) {
            this.mGameMap = mapData;
            this.activity = activity;
        }

        @NonNull
        @Override
        public MapElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //Get the layout inflater to make the view holder in from context activity
            return new MapElementViewHolder(LayoutInflater.from(activity), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MapElementViewHolder holder, int position) {
            holder.bind(mGameMap.getMapElement(position % mGameMap.getMapHeight(), position / mGameMap.getMapHeight()));
        }

        @Override
        public int getItemCount() {
            return mGameMap.getMapWidth() * mGameMap.getMapHeight();
        }

        private class MapElementViewHolder extends RecyclerView.ViewHolder {
            ImageView mBackgroundImage;
            ImageView mStructureImage;

            MapElement mMapElement;

            public MapElementViewHolder(LayoutInflater li, ViewGroup parent) {
                super(li.inflate(R.layout.map_element, parent, false));

                //Caching
                mBackgroundImage = itemView.findViewById(R.id.background);
                mStructureImage = itemView.findViewById(R.id.topImage);

                mMapElement = null;

                //Setting held View's size to be square of correct size
                int size = parent.getMeasuredHeight() / mGameMap.getMapHeight() + 1;
                ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                lp.width = size;
                lp.height = size;
            }

            public void bind(MapElement mapElement) {
                mBackgroundImage.setImageResource(mapElement.getBackgroundImageResource());
                Structure elementStructure = mapElement.getStructure();
                if (elementStructure != null) {
                    mStructureImage.setVisibility(View.VISIBLE);
                    mStructureImage.setImageResource(elementStructure.getImageId());
                } else {
                    mStructureImage.setVisibility(View.INVISIBLE);
                }

                mMapElement = mapElement;

                // TODO
//                //Setting onClick to auto-build the current selected structure
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Structure structureToSet = AppData.get().getSelectedStructure();
//                        //Add the currently selected structure (if there is one) to this ViewHolder's
//                        // map element (updates MAP_DATA_TODO as well via pass-by-reference)
//                        if (structureToSet != null) {
//                            Log.v(TAG, "Setting block structure");
//
//                            //Set this element to have this structure for future references
//                            mMapElement.setStructure(structureToSet);
//
//                            //Let the adapter know stuff's changed
//                            CityMapAdapter.this.notifyItemChanged(getAdapterPosition());
//                        }
//                    }
//                });

                // TODO decide whether to bother implementing this
//                //Responding to manual drag of structure
//                itemView.setOnDragListener(new View.OnDragListener() {
//                    @Override
//                    public boolean onDrag(View view, DragEvent dragEvent) {
//                        boolean responded = false;
//
//                        switch (dragEvent.getAction()) {
//                            case DragEvent.ACTION_DRAG_STARTED: //Must return true to 'drag started' when the drag appears in this fragment to keep getting updates on it
//                                responded = true;
//                                break;
//                            case DragEvent.ACTION_DROP: //If the user dragged a structure here
//                                if (view == itemView) { //Making sure it was dropped onto THIS ItemView
//                                    Object thingDragged = dragEvent.getLocalState();
//
//                                    if (thingDragged instanceof Structure) {
//                                        //Set this element to have this structure
//                                        mMapElement.setStructure((Structure)thingDragged);
//
//                                        //Let the adapter know stuff's changed
//                                        CityMapAdapter.this.notifyItemChanged(getAdapterPosition());
//
//                                        responded = true;
//                                    }
//                                }
//                                break;
//                        }
//
//                        return responded;
//                    }
//                }); //PERSONAL NOTE: I'm pretty sure the drag/drop listener system works on a ViewGroup level (i.e. here on the RecyclerView) rather than an individual view level - hence the situation where only listening to on 'ACTION_DRAG_ENDED' caused the action to be performed on every cell in the grid.
            }
        }
    }

}