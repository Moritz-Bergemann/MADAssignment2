package com.moritzbergemann.myapplication.model;

import android.text.Editable;

import com.moritzbergemann.myapplication.TextValidator;

public abstract class IntegerTextValidator extends TextValidator {
    private int minValue;
    private int maxValue;

    public IntegerTextValidator(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public boolean validate(String textValue) {
        boolean valid = false;

        try {
            int newMapWidth = Integer.parseInt(textValue);

            if (newMapWidth >= minValue && newMapWidth <= maxValue) {
                valid = true;
            }
        } catch (NumberFormatException n) {
            //Do nothing, just exit with false value
        }

        return valid;
    }

    @Override
    public void useValue(String textValue) {
        int newMapWidth = Integer.parseInt(textValue);

        setNewValue(newMapWidth);
    }

    @Override
    public void resetValue(Editable editable) {
        editable.clear();
//        editable.append(Integer.toString(getOriginalValue()));
    }

    public abstract void setNewValue(int newValue);

    public abstract int getOriginalValue();
}
