package com.moritzbergemann.myapplication;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moritzbergemann.myapplication.model.GameData;
import com.moritzbergemann.myapplication.model.Structure;
import com.moritzbergemann.myapplication.model.StructureData;

import java.util.List;
import java.util.Locale;

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
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SelectorFragment.
     */
    // TODO: Rename and change types and number of parameters
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSelectorRecyclerView = view.findViewById(R.id.recyclerView);
        mSelectorRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

        StructureAdapter adapter = new StructureAdapter(StructureData.get().getStructureTypes(), getActivity());

        mSelectorRecyclerView.setAdapter(adapter);
    }

    /**
     * RecyclerView adapter class for structures scrollbar. Stores a bunch of structures and updates
     *  as needed
     */
    private class StructureAdapter extends RecyclerView.Adapter<StructureAdapter.StructureViewHolder> {
        private List<Structure> mStructures;
        private Activity mActivity;

        public StructureAdapter(List<Structure> structures, Activity activity) { //FIXME should this just call getInstance() straight up?
            super();
            mStructures = structures;
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

            holder.bind(mStructures.get(position));
        }

        @Override
        public int getItemCount() {
            return mStructures.size();
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

                mCost.setText(String.format(Locale.US, "$%d", structure.getCost()));

                //Making click select this structure as the structure to auto-paste
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Setting selected structure as structure to build on map tiles
                        GameData.get().setSelectedStructure(structure);

                        Log.v(TAG, String.format("User selected %s structure with resource ID '%d'",
                                structure.getType(), structure.getImageId()));

                        StructureAdapter.this.notifyItemChanged(getAdapterPosition());
                    }
                });

                //TODO
//            //Making long click select this structure for drag and drop
//            itemView.setLongClickable(true);
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @RequiresApi(api = Build.VERSION_CODES.N)
//                @Override
//                public boolean onLongClick(View view) {
//                    //Setting image to drag with
//                    View.DragShadowBuilder builder = new View.DragShadowBuilder(mImage);
//
//                    //Starting drag and drop
//                    itemView.startDragAndDrop(null, builder, structure, 0);
//
//                    return true;
//                }
//            });
            }
        }
    }
}