package com.moritzbergemann.myapplication;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.moritzbergemann.myapplication.model.ValidationException;

/**
 * The concept for this class was adapted from https://stackoverflow.com/questions/2763022/android-how-can-i-validate-edittext-input/11838715#11838715
 * Class for validating text input into an editable text element.
 */
public abstract class TextValidator implements TextWatcher {
    private static final String TAG = "TextValidator";
    private EditText editText;

    public TextValidator(EditText editText) {
        this.editText = editText;
    }

    /**
     * Uses the input value if it has been found to be valid
     * @param textValue the value to use
     * @throws ValidationException if the input value is not valid
     */
    public abstract void useValue(String textValue) throws ValidationException;

    @Override
    public void afterTextChanged(Editable editable) {
        String textValue = editable.toString();
        Log.v(TAG, "AfterTextChange called");

        try {
            useValue(textValue);
        } catch (ValidationException v) {
            editText.setError(v.getMessage());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
}
