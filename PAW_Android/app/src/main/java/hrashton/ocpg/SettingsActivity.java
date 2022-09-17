package hrashton.ocpg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.MaskFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.database.sqlite.SQLiteDatabase;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Arrays;


public class SettingsActivity extends Activity implements View.OnClickListener,
        ToggleButton.OnCheckedChangeListener {
    SharedPreferences mSettings;
    SharedPreferences.Editor editor;
    Boolean isAppsAction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Задаём фон
        ((ImageView)findViewById(R.id.background_settings)).setImageDrawable(MainActivity.background);

        findViewById(R.id.settings_cb_save_history).setOnClickListener(this);
        ((ToggleButton)findViewById(R.id.toggleButton)).setOnCheckedChangeListener(this);

        mSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = mSettings.edit();
        ((CheckBox) findViewById(R.id.settings_cb_save_history)).setChecked(mSettings.getBoolean("Save_history", true));
        if (!mSettings.contains("pin"))
            ((ToggleButton) findViewById(R.id.toggleButton)).setChecked(false);


        ((EditText) findViewById(R.id.editText2)).addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        editor.putString(
                                "key", DesEncrypter.encrypt(
                                        s.toString(),
                                        MD5.md5(mSettings.getString("pin", "") + Build.SERIAL + "salt")
                                )
                        );
                        editor.apply();
                    }
                }
        );


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_cb_save_history:
                editor.putBoolean("Save_history", ((CheckBox) v).isChecked());
                editor.apply();
                break;
        }
    }

    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        if (isAppsAction) {
            isAppsAction = false;
            return;
        }
        if (buttonView.getId() == R.id.toggleButton) {
            if (isChecked) {
                ((ToggleButton) buttonView).setChecked(false);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setNegativeButton(R.string.settings_set_pin_abort, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                final EditText input = new EditText(this);
                final AlertDialog alert = alertBuilder.create();

                alert.setTitle(R.string.settings_set_pin_dialog_title);
                input.setRawInputType(InputType.TYPE_CLASS_PHONE);
                input.setMaxLines(1);
                alert.setView(input);

                input.setOnKeyListener(
                        new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {
                                if (input.getText().length() < 4)
                                    return false;

                                String value = input.getText().toString();
                                editor = mSettings.edit();
                                editor.putString("pin", MD5.md5(value + Build.SERIAL));
                                editor.apply();

                                isAppsAction = true;
                                ((ToggleButton) buttonView).setChecked(true);
                                findViewById(R.id.editText2).setEnabled(true);
                                alert.dismiss();
                                return true;
                            }
                        }
                );

                alert.show();
            } else {
                editor.remove("pin");
                editor.remove("key");
                editor.apply();
                findViewById(R.id.editText2).setEnabled(false);
            }
        }
    }
}
