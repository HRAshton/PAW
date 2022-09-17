package hrashton.ocpg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;


public class MainActivity extends Activity {
    private static SharedPreferences mSettings;
    private static SharedPreferences.Editor editor;
    private static StringBuffer password = new StringBuffer();
    private TranslateAnimation animUp, animDown;
    private Button btnGet;
    private static Runnable runnableGetPassword;
    private static Intent shareIntent;
    private static HashSet<String> history;
    public static Drawable background;
    long backPressedTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGet = ((Button) findViewById(R.id.main_btn_get));
        mSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        animUp = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.anim_get_button_up);
        animUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                btnGet.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btnGet.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        //Задаём фоновое изображение
        TypedArray ta = getResources().obtainTypedArray(R.array.random_imgs);
        Calendar date = java.util.Calendar.getInstance();
        String number = MD5.md5(
                Integer.toString(date.get(Calendar.YEAR) + date.get(Calendar.DAY_OF_YEAR))
            ).charAt(0) + "";
        byte bDailyIndex = (byte)(Byte.parseByte(number, 16) % ta.length());
        background = ta.getDrawable(bDailyIndex);
        ImageView layout = (ImageView)findViewById(R.id.background_main);
        layout.setImageDrawable(background);
        ta.recycle();

        animDown = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.anim_get_button_down);
        animDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { btnGet.setEnabled(true); }

            @Override
            public void onAnimationEnd(Animation animation) { btnGet.setVisibility(View.VISIBLE); }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        runnableGetPassword = new Runnable() {
            @Override
            public void run() {
                String serviceName = ((EditText) findViewById(R.id.main_service)).getText().toString();

                //Получаем пароль
                getPassword.get(
                        serviceName,
                        ((EditText) findViewById(R.id.main_key)).getText().toString(),
                        password
                );

                //Добавляем при надобности новый пункт в список названий
                if (mSettings.getBoolean("Save_history", true)) {
                    history.add(serviceName);

                    editor = mSettings.edit();
                    editor.putStringSet("History", history);
                    editor.apply();
                    editor = null;

                    ((AutoCompleteTextView) findViewById(R.id.main_service)).setAdapter(
                            new ArrayAdapter<>(
                                    MainActivity.this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    history.toArray(new String[history.size()])
                            )
                    );
                }
            }
        };

        //При нажатии на кнопку надо запросить ПИН и заполнить поле Ключа
        findViewById(R.id.main_btn_paste).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Создаем диалог, спрашивающий ПИН
                        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                        alertBuilder.setNegativeButton(R.string.settings_set_pin_abort, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                        final AlertDialog alert = alertBuilder.create();
                        final EditText input = new EditText(alert.getContext());

                        alert.setTitle(R.string.main_get_pin_dialog_title);
                        input.setRawInputType(InputType.TYPE_CLASS_PHONE);
                        input.setOnKeyListener(
                                new View.OnKeyListener() {
                                    @Override
                                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                                        if (input.getText().length() < 4)
                                            return false;
                                        String value = input.getText().toString();
                                        //Пароль состоит из ПИНа, серийного номера устройства и соли
                                        String password = MD5.md5(mSettings.getString("pin", "") + Build.SERIAL + "salt");
                                        //Если ПИН введён верно, расшифровываем Ключ и вставляем в поле
                                        if (MD5.md5(value + Build.SERIAL).equals(mSettings.getString("pin", ""))) {
                                            ((EditText) findViewById(R.id.main_key)).setText(
                                                    DesEncrypter.decrypt(mSettings.getString("key", ""), password)
                                            );
                                            alert.dismiss();

                                        } else {
                                            Toast.makeText(MainActivity.this, R.string.settings_wrong_pin_toast, Toast.LENGTH_SHORT).show();
                                            input.getText().clear();
                                        }
                                        return true;
                                    }
                                }
                        );

                        alert.setView(input);
                        alert.show();

                        //Для удобства сразу открываем виртуальную клавиатуру
                        InputMethodManager imm =
                                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
        );

        btnGet.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((EditText) findViewById(R.id.main_service)).getText().length() == 0) {
                            findViewById(R.id.main_service).startAnimation(
                                    AnimationUtils.loadAnimation(MainActivity.this,
                                            R.anim.anim_service_empty)
                            );
                        } else {
                            runnableGetPassword.run();
                            findViewById(R.id.main_linear_buttons).setEnabled(true);
                            btnGet.startAnimation(animUp);
                        }
                    }
                }
        );

        findViewById(R.id.main_btn_show).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((TextView) findViewById(R.id.textView)).setText(password);
                    }
                }
        );

        findViewById(R.id.main_btn_copy).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), R.string.toast_copied, Toast.LENGTH_SHORT)
                                .show();
                        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(
                                ClipData.newPlainText(null, password.toString())
                        );
                    }
                }
        );

        findViewById(R.id.main_btn_destroy).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        breakPassword();
                    }
                }
        );

        findViewById(R.id.main_btn_share).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (shareIntent == null) {
                            shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                        }
                        shareIntent.putExtra(Intent.EXTRA_TEXT, password.toString());
                        startActivity(Intent.createChooser(shareIntent, null));
                    }
                }
        );

        findViewById(R.id.main_btn_clear).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((EditText) findViewById(R.id.main_service)).getText().clear();
                        if (password.length() > 0)
                            breakPassword();
                    }
                }
        );

        ((EditText) findViewById(R.id.main_service)).addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int i1, int i2, int i3) { }

                    @Override
                    public void onTextChanged(CharSequence s, int i1, int i2, int i3) { }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (password.length() > 0)
                            breakPassword();
                    }
                }
        );

        ((EditText) findViewById(R.id.main_key)).addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int i1, int i2, int i3) { }

                    @Override
                    public void onTextChanged(CharSequence s, int i1, int i2, int i3) { }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (password.length() > 0)
                            breakPassword();
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();

        //Если назначены пин И ключ (мало ли), то активируем кнопку вставки ключа
        findViewById(R.id.main_btn_paste).
                setEnabled(mSettings.contains("pin") && mSettings.contains("key"));

        //Прикрепляем список автозаполнения названий сервисов
        history = (HashSet<String>) mSettings.getStringSet("History", new HashSet<String>());
        ((AutoCompleteTextView) findViewById(R.id.main_service)).setAdapter(
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_dropdown_item_1line,
                        history.toArray(new String[history.size()])
                )
        );
    }

    @Override
    public void onStop() {
        super.onStop();

        findViewById(R.id.main_linear_buttons).setEnabled(true);
        breakPassword();
        ((EditText) findViewById(R.id.main_service)).getText().clear();
        ((EditText) findViewById(R.id.main_key)).getText().clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menu_list:
                startActivity(new Intent(this, HistoryActivity.class));
                return true;
            case R.id.menu_about:
                startActivity(new Intent(this, About.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.main_linear_buttons).isEnabled()) {
            breakPassword();
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(getBaseContext(), R.string.toast_back_once, Toast.LENGTH_SHORT).show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }

    private void breakPassword() {
        if (findViewById(R.id.main_linear_buttons).isEnabled()) {
            btnGet.startAnimation(animDown);
            findViewById(R.id.main_linear_buttons).setEnabled(false);
        }
        password.delete(0, password.length());

        ((TextView) findViewById(R.id.textView)).setText("");
    }
}
