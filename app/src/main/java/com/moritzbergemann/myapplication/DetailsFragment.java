package com.moritzbergemann.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.moritzbergemann.myapplication.model.MapElement;
import com.moritzbergemann.myapplication.model.Structure;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get map element to show the details of (set by caller)
        CityViewModel viewModel = new ViewModelProvider(requireActivity()).get(CityViewModel.class);
        MapElement mapElement = viewModel.getMapElementForDetails();

        if (mapElement == null) {
            throw new IllegalArgumentException("Inspect menu unduly called - no map element exists");
        } else if (mapElement.getStructure() == null) {
            throw new IllegalArgumentException("Inspect menu unduly called - no structure in map element");
        }

        EditText structureName = view.findViewById(R.id.structureName);
        if (mapElement.getOwnerName() != null) {
            structureName.setText(mapElement.getOwnerName());
        } else {
            //Set name to structure type by default
            structureName.setText(Structure.getTypeName(mapElement.getStructure().getType()));
        }

        //Listen to changes to text to change building name
        structureName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                mapElement.setOwnerName(editable.toString());
                //TODO DB stuff
            }
        });

        TextView structureType = view.findViewById(R.id.structureType);
        structureType.setText(Structure.getTypeName(mapElement.getStructure().getType()));

        TextView rowText = view.findViewById(R.id.rowValue);
        rowText.setText(String.format(Locale.US, "%d", mapElement.getRowPos()));

        TextView colText = view.findViewById(R.id.columnValue);
        colText.setText(String.format(Locale.US, "%d", mapElement.getColPos()));

        Button chooseImageButton = view.findViewById(R.id.chooseImageButton);
        //TODO thumbnail image selection
    }


}