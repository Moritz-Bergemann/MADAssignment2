package com.moritzbergemann.myapplication;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.moritzbergemann.myapplication.mapactions.SelectableMapAction;
import com.moritzbergemann.myapplication.model.GameData;
import com.moritzbergemann.myapplication.model.GameMap;
import com.moritzbergemann.myapplication.model.MapElement;
import com.moritzbergemann.myapplication.model.MapException;
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

        CityViewModel viewModel = new ViewModelProvider(getActivity()).get(CityViewModel.class);
        viewModel.getMapElementWithImageUpdated().observe(getViewLifecycleOwner(), mapElement -> { //FIXME wtf is this? What does it do?
//            mMapRecyclerView.getAdapter().notifyItemChanged();
        });
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
                    if (mapElement.getSpecialImage() != null) { //If there is a special image to show
                        mStructureImage.setImageBitmap(mapElement.getSpecialImage());
//                        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), mapElement.getSpecialImage()); //TODO remove this test code
//                        mStructureImage.setImageDrawable(bitmapDrawable);
                    } else {
                        //Just show the regular image
                        mStructureImage.setVisibility(View.VISIBLE);
                        mStructureImage.setImageResource(elementStructure.getImageId());
                    }
                } else {
                    mStructureImage.setVisibility(View.INVISIBLE);
                }

                mMapElement = mapElement;

                itemView.setOnClickListener(clickedElement -> {
                    CityViewModel viewModel = new ViewModelProvider(getActivity()).get(CityViewModel.class);
                    SelectableMapAction mapAction = viewModel.getMapAction();

                    if (mapAction != null) {
                        try {
                            //Perform the currently selected action at this position on the map
                            mapAction.performAction(mapElement.getRowPos(), mapElement.getColPos());
                        } catch (MapException m) {
                            Toast.makeText(activity, m.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        //Notify the adapter something might have changed
                        CityMapAdapter.this.notifyItemChanged(getAdapterPosition());

                        //Notify the rest of the UI something might have changed
                        GameData.get().notifyUIUpdate(); //FIXME is this the right place for this?
                    }
                });
            }
        }
    }
}