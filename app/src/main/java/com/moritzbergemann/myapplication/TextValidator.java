package com.moritzbergemann.myapplication;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

/**
 * The concept for this class was adapted from https://stackoverflow.com/questions/2763022/android-how-can-i-validate-edittext-input/11838715#11838715
 * Class for validating text input into an editable text element.
 */
public abstract class TextValidator implements TextWatcher {
    private static final String TAG = "TextValidator";

    /**
     * Validate the string content of the editable text
     * @param textValue text to validate
     * @return True if content is valid, false if not
     */
    public abstract boolean validate(String textValue);

    /**
     * Uses the input value if it has been found to be valid
     * @param textValue the value to use
     */
    public abstract void useValue(String textValue);

    /**
     * Resets the text field if the input value has been found to be invalid.
     */
    public abstract void resetValue(Editable editable);

    @Override
    public void afterTextChanged(Editable editable) {
        String textValue = editable.toString();
        Log.v(TAG, "AfterTextChange called");
        if (validate(textValue)) {
            Log.v(TAG, "Value was valid! Using...");

            useValue(textValue);
        } else {
            resetValue(editable);

            Log.v(TAG, "Value was invalid! Resetting text...");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
}
