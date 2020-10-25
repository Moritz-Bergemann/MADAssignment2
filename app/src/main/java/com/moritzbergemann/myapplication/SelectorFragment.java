package com.moritzbergemann.myapplication;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moritzbergemann.myapplication.mapactions.BuildStructure;
import com.moritzbergemann.myapplication.mapactions.DemolishStructure;
import com.moritzbergemann.myapplication.mapactions.GetStructureDetails;
import com.moritzbergemann.myapplication.model.GameData;
import com.moritzbergemann.myapplication.model.Structure;
import com.moritzbergemann.myapplication.model.StructureData;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectorFragment extends Fragment {
    private static final String TAG = "SelectorFragment";

    RecyclerView mSelectorRecyclerView;

    public SelectorFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment SelectorFragment.
     */
    public static SelectorFragment newInstance() {
        return new SelectorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selector, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Setting up RecyclerView
        mSelectorRecyclerView = view.findViewById(R.id.recyclerView);
        mSelectorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

        //Getting unique identifiers of structures as list of strings (sorted alphabetically)
        List<String> structureIdList = new LinkedList<>(StructureData.get().getStructureTypes().keySet());
        structureIdList.sort(String.CASE_INSENSITIVE_ORDER);

        StructureAdapter adapter = new StructureAdapter(structureIdList, getActivity());

        mSelectorRecyclerView.setAdapter(adapter);

        //Demolish button
        ConstraintLayout demolishButton = view.findViewById(R.id.demolishButton);
        demolishButton.setOnClickListener(clickedDemolishButton -> {
            CityViewModel viewModel = new ViewModelProvider(requireActivity()).get(CityViewModel.class);
            viewModel.setMapAction(new DemolishStructure());
        });

        //Inspect building button
        ConstraintLayout infoButton = view.findViewById(R.id.infoButton);
        infoButton.setOnClickListener(clickedInfoButton -> {
            if (requireActivity() instanceof CityActivity) {
                CityActivity activity = (CityActivity) requireActivity();
                CityViewModel viewModel = new ViewModelProvider(requireActivity()).get(CityViewModel.class);
                viewModel.setMapAction(new GetStructureDetails(activity));
            }
        });
    }

    /**
     * RecyclerView adapter class for structures scrollbar. Stores a bunch of structures (by their
     *  unique ID) and updates as needed
     */
    private class StructureAdapter extends RecyclerView.Adapter<StructureAdapter.StructureViewHolder> {
        private List<String> mStructureIds;
        private Activity mActivity;

        public StructureAdapter(List<String> structureIds, Activity activity) { //FIXME should this just call getInstance() straight up?
            super();
            mStructureIds = structureIds;
            mActivity = activity;
        }

        @NonNull
        @Override
        public StructureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Log.v(TAG, "onCreateViewHolder() called in RecyclerView adapter");

            LayoutInflater li = LayoutInflater.from(mActivity); //NOTE why not just mActivity.getLayoutInflater()? tf is the difference

            return new StructureViewHolder(li, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull StructureViewHolder holder, int position) {
            Log.v(TAG, String.format("ViewHolder '%s' bound with element at position '%d'", holder.toString(), position));

            holder.bind(Objects.requireNonNull(StructureData.get().getStructureTypes().get(mStructureIds.get(position))));
        }

        @Override
        public int getItemCount() {
            return mStructureIds.size();
        }


        /**
         * ViewHolder for structures scrollbar. Caches image and label components and adds click listener.
         */
        private class StructureViewHolder extends RecyclerView.ViewHolder {
            private ImageView mImage;
            private TextView mType;
            private TextView mCost;

            public StructureViewHolder(LayoutInflater li, ViewGroup parent) {
                super(li.inflate(R.layout.structure_icon, parent, false));

                //Caching components of itemView for quick access (since these need to be modified)
                mImage = itemView.findViewById(R.id.image);
                mType = itemView.findViewById(R.id.type);
                mCost = itemView.findViewById(R.id.costValue);
            }

            /**
             * Adds a new Structure to the ViewHolder for it to display.
             */
            public void bind(final Structure structure) {
                //Updating image itself and label
                mImage.setImageResource(structure.getImageId());
                switch (structure.getType()) {
                    case RESIDENTIAL:
                        mType.setText("Residential");
                        break;
                    case COMMERCIAL:
                        mType.setText("Commercial");
                        break;
                    case ROAD:
                        mType.setText("Road");
                        break;
                    default:
                        throw new IllegalArgumentException("Bad type");
                }

                mCost.setText(String.format(Locale.US, "$%d",
                        GameData.get().getSettings().getStructureCost(structure.getType())));

                //Making click select this structure as the structure to auto-paste
                itemView.setOnClickListener(view -> {
                    //Setting selected structure as structure to build on map tiles
                    CityViewModel viewModel = new ViewModelProvider(requireActivity()).get(CityViewModel.class);
                    viewModel.setMapAction(new BuildStructure(structure));

                    Log.v(TAG, String.format("User selected %s structure with resource ID '%d'",
                            structure.getType(), structure.getImageId()));
                });
            }
        }
    }
}