package com.moritzbergemann.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.moritzbergemann.myapplication.model.GameData;
import com.moritzbergemann.myapplication.model.MapElement;
import com.moritzbergemann.myapplication.model.Structure;

import java.util.Locale;

/**
 * Fragment displaying additional details on a map element. Also allows for the setting of custom
 *  names and images.
 */
public class DetailsFragment extends Fragment {
    private static final int REQUEST_THUMBNAIL = 1;

    private MapElement mMapElement = null;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment DetailsFragment.
     */
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
        mMapElement = viewModel.getMapElementForDetails();

        if (mMapElement == null) {
            throw new IllegalArgumentException("Inspect menu unduly called - no map element exists");
        } else if (mMapElement.getStructure() == null) {
            throw new IllegalArgumentException("Inspect menu unduly called - no structure in map element");
        }

        EditText structureName = view.findViewById(R.id.structureName);
        if (mMapElement.getOwnerName() != null) {
            structureName.setText(mMapElement.getOwnerName());
        } else {
            //Set name to structure type by default
            structureName.setText(Structure.getTypeName(mMapElement.getStructure().getType()));
        }

        //Listen to changes to text to change building name
        structureName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                //Update the element's owner name (not done directly to allow database management
                //  etc)
                GameData.get().getMap().setElementOwnerName(editable.toString(),
                        mMapElement.getRowPos(), mMapElement.getColPos());
            }
        });

        TextView structureType = view.findViewById(R.id.structureType);
        structureType.setText(Structure.getTypeName(mMapElement.getStructure().getType()));

        TextView rowText = view.findViewById(R.id.rowValue);
        rowText.setText(String.format(Locale.US, "%d", mMapElement.getRowPos()));

        TextView colText = view.findViewById(R.id.columnValue);
        colText.setText(String.format(Locale.US, "%d", mMapElement.getColPos()));

        Button chooseImageButton = view.findViewById(R.id.chooseImageButton);
        chooseImageButton.setOnClickListener(clickedChooseImageButton -> {
            Intent thumbnailPhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(thumbnailPhotoIntent, REQUEST_THUMBNAIL);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_THUMBNAIL) { //When thumbnail photo taken
                Bitmap thumbnailPhoto = (Bitmap) data.getExtras().get("data");
                GameData.get().getMap().setElementSpecialImage(thumbnailPhoto,
                        mMapElement.getRowPos(), mMapElement.getColPos());
            }
        }
    }
}