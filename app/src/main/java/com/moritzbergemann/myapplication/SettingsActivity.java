package com.moritzbergemann.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.moritzbergemann.myapplication.model.GameData;
import com.moritzbergemann.myapplication.model.Settings;
import com.moritzbergemann.myapplication.model.Structure;
import com.moritzbergemann.myapplication.model.ValidationException;

import java.util.Locale;

/**
 * Activity for changing game settings. Also contains a button for resetting the game.
 */
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static Intent makeIntent(Activity callingActivity) {
        return new Intent(callingActivity, SettingsActivity.class);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //All TextWatchers are placed in onPostCreate so that 'changing' (automatic maintaining) of
        //  EditText contents on a screen rotate do not trigger them

        Settings settings = GameData.get().getSettings();

        EditText mapWidthValue = findViewById(R.id.mapWidthValue);
        if (settings.getMapWidth() != -1) {
            mapWidthValue.setText(String.format(Locale.US, "%d", settings.getMapWidth()));
        }
        mapWidthValue.addTextChangedListener(new IntegerTextValidator(mapWidthValue,
                Settings.MIN_MAP_WIDTH, Settings.MAX_MAP_WIDTH) {
            @Override
            public void useIntegerValue(int value) throws ValidationException {
                if (!GameData.get().isGameStarted()) {
                    settings.setMapWidth(value);
                } else {
                    throw new ValidationException("Game has already been started, can no longer " +
                            "change map width");
                }
            }
        });

        //**MAP HEIGHT AND WIDTH (NOT ADJUSTABLE AFTER GAME START)**
        EditText mapHeightValue = findViewById(R.id.mapHeightValue);
        if (settings.getMapHeight() != -1) {
            mapHeightValue.setText(String.format(Locale.US, "%d", settings.getMapHeight()));
        }
        mapHeightValue.addTextChangedListener(new IntegerTextValidator(mapHeightValue,
                Settings.MIN_MAP_HEIGHT, Settings.MAX_MAP_HEIGHT) {
            @Override
            public void useIntegerValue(int value) throws ValidationException {
                if (!GameData.get().isGameStarted()) {
                    settings.setMapHeight(value);
                } else {
                    throw new ValidationException("Game has already been started, can no longer " +
                            "change map height");
                }
            }
        });

        EditText initialMoneyValue = findViewById(R.id.initialMoneyValue);
        if (settings.getInitialMoney() != -1) {
            initialMoneyValue.setText(String.format(Locale.US, "%d", settings.getInitialMoney()));
        }
        initialMoneyValue.addTextChangedListener(new IntegerTextValidator(initialMoneyValue,
                Settings.MIN_INITIAL_MONEY, Settings.MAX_INITIAL_MONEY) {
            @Override
            public void useIntegerValue(int value) throws ValidationException {
                if (!GameData.get().isGameStarted()) {
                    settings.setInitialMoney(value);
                } else {
                    throw new ValidationException("Game has already been started, can no longer " +
                            "change initial money");
                }
            }
        });

        EditText cityNameValue = findViewById(R.id.cityNameValue);
        if (settings.getCityName() != null) {
            cityNameValue.setText(settings.getCityName());
        }
        cityNameValue.addTextChangedListener(new TextValidator(cityNameValue) {
            @Override
            public void useValue(String textValue) throws ValidationException {
                if (textValue.length() > 0) {
                    if (textValue.length() <= 15) {
                        settings.setCityName(textValue);
                    } else {
                        throw new ValidationException("City name cannot be more than 15 characters");
                    }
                } else {
                    throw new ValidationException("City name cannot be empty");
                }
            }
        });

        EditText familySizeValue = findViewById(R.id.familySizeValue);
        if (settings.getFamilySize() != -1) {
            familySizeValue.setText(String.format(Locale.US, "%d", settings.getFamilySize()));
        }
        familySizeValue.addTextChangedListener(new IntegerTextValidator(familySizeValue,
                Settings.MIN_FAMILY_SIZE, Settings.MAX_FAMILY_SIZE) {
            @Override
            public void useIntegerValue(int value) throws ValidationException {
                settings.setFamilySize(value);
            }
        });

        EditText shopSizeValue = findViewById(R.id.shopSizeValue);
        if (settings.getShopSize() != -1) {
            shopSizeValue.setText(String.format(Locale.US, "%d", settings.getShopSize()));
        }
        shopSizeValue.addTextChangedListener(new IntegerTextValidator(shopSizeValue,
                Settings.MIN_SHOP_SIZE, Settings.MAX_SHOP_SIZE) {
            @Override
            public void useIntegerValue(int value) throws ValidationException {
                settings.setShopSize(value);
            }
        });

        EditText salaryValue = findViewById(R.id.salaryValue);
        if (settings.getSalary() != -1) {
            salaryValue.setText(String.format(Locale.US, "%d", settings.getSalary()));
        }
        salaryValue.addTextChangedListener(new IntegerTextValidator(salaryValue,
                Settings.MIN_SALARY, Settings.MAX_SALARY) {
            @Override
            public void useIntegerValue(int value) throws ValidationException {
                settings.setSalary(value);
            }
        });

        EditText taxRateValue = findViewById(R.id.taxRateValue);
        if (settings.getTaxRate() >= 0.0) {
            taxRateValue.setText(String.format(Locale.US, "%.2f", settings.getTaxRate()));
        }
        taxRateValue.addTextChangedListener(new TextValidator(taxRateValue) {
            @Override
            public void useValue(String textValue) throws ValidationException {
                try {
                    double value = Double.parseDouble(textValue);
                    if (value >= Settings.MIN_TAX_RATE && value <= Settings.MAX_TAX_RATE) {
                        settings.setTaxRate(value);
                    } else {
                        throw new ValidationException(String.format(Locale.US, "Value must" +
                                " be between %.2f and %.2f", Settings.MIN_TAX_RATE,
                                Settings.MAX_TAX_RATE));
                    }
                } catch (NumberFormatException n) {
                    throw new ValidationException("Input must be a decimal number");
                }
            }
        });

        EditText serviceCostValue = findViewById(R.id.serviceCostValue);
        if (settings.getServiceCost() != -1) {
            serviceCostValue.setText(String.format(Locale.US, "%d", settings.getServiceCost()));
        }
        serviceCostValue.addTextChangedListener(new IntegerTextValidator(serviceCostValue,
                Settings.MIN_SERVICE_COST, Settings.MAX_SERVICE_COST) {
            @Override
            public void useIntegerValue(int value) throws ValidationException {
                settings.setServiceCost(value);
            }
        });

        EditText roadCostValue = findViewById(R.id.roadCostValue);
        if (settings.getStructureCost(Structure.Type.ROAD) != -1) {
            roadCostValue.setText(String.format(Locale.US, "%d", settings.getStructureCost(Structure.Type.ROAD)));
        }
        roadCostValue.addTextChangedListener(new IntegerTextValidator(roadCostValue,
                Settings.MIN_STRUCTURE_COST, Settings.MAX_STRUCTURE_COST) {
            @Override
            public void useIntegerValue(int value) throws ValidationException {
                settings.setStructureCost(Structure.Type.ROAD, value);
            }
        });

        EditText residentialCostValue = findViewById(R.id.residentialCostValue);
        if (settings.getStructureCost(Structure.Type.RESIDENTIAL) != -1) {
            residentialCostValue.setText(String.format(Locale.US, "%d", settings.getStructureCost(Structure.Type.RESIDENTIAL)));
        }
        residentialCostValue.addTextChangedListener(new IntegerTextValidator(residentialCostValue,
                Settings.MIN_STRUCTURE_COST, Settings.MAX_STRUCTURE_COST) {
            @Override
            public void useIntegerValue(int value) throws ValidationException {
                settings.setStructureCost(Structure.Type.RESIDENTIAL, value);
            }
        });

        EditText commercialCostValue = findViewById(R.id.commercialCostValue);
        if (settings.getStructureCost(Structure.Type.COMMERCIAL) != -1) {
            commercialCostValue.setText(String.format(Locale.US, "%d", settings.getStructureCost(Structure.Type.COMMERCIAL)));
        }
        commercialCostValue.addTextChangedListener(new IntegerTextValidator(commercialCostValue,
                Settings.MIN_STRUCTURE_COST, Settings.MAX_STRUCTURE_COST) {
            @Override
            public void useIntegerValue(int value) throws ValidationException {
                settings.setStructureCost(Structure.Type.COMMERCIAL, value);
            }
        });


        //*** RESET BUTTON SETUP ***
        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(clickedResetButton -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.resetGameDialogTitle)
                    .setMessage(R.string.resetGameDialogMessage)
                    .setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
                        //Reset EVERYTHING
                        GameData.resetAll(SettingsActivity.this.getApplicationContext());
                        finishActivity();
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create().show();
        });
    }

    /**Ends this activity and returns to the caller.
     */
    private void finishActivity() {
        finish();
    }
}