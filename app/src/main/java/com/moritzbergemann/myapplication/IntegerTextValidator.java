package com.moritzbergemann.myapplication;

import android.widget.EditText;

import com.moritzbergemann.myapplication.model.ValidationException;

import java.util.Locale;

public abstract class IntegerTextValidator extends TextValidator {
    private int minValue;
    private int maxValue;

    public IntegerTextValidator(EditText editText, int minValue, int maxValue) {
        super(editText);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Performs actual action with integer value (after it is known to be in valid range).
     * Can still throw exception if the value is invalid for another (contextual) reason.
     */
    public abstract void useIntegerValue(int value) throws ValidationException;

    @Override
    public void useValue(String textValue) throws ValidationException {
        try {
            int value = Integer.parseInt(textValue);

            if (value >= minValue && value <= maxValue) {
                useIntegerValue(value);
            } else {
                throw new ValidationException(String.format(Locale.US, "Input value must " +
                        "be between %d & %d!", minValue, maxValue));
            }
        } catch (NumberFormatException n) {
            throw new ValidationException("Input value must be an integer!");
        }
    }
}
