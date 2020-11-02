package com.moritzbergemann.myapplication;

import androidx.lifecycle.ViewModel;

/**
 * ViewModel for main menu.
 */
public class MenuViewModel extends ViewModel {
    private boolean continueGameOptionShown;

    public boolean isContinueGameOptionShown() {
        return continueGameOptionShown;
    }

    public void setContinueGameOptionShown(boolean continueGameOptionShown) {
        this.continueGameOptionShown = continueGameOptionShown;
    }
}
