package pt.ismai.hungryme.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.widget.CompoundButton;

import pt.ismai.hungryme.HelpingClass.SQLiteOpenHelper;
import pt.ismai.hungryme.R;
import pt.ismai.hungryme.ui.UI.BaseActivity;

import java.util.Calendar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
SettingsActivity creates the settings screen and all of the activities that are on it.
The user gets her by clicking on settings in the menu
The user can set up meal times and
changing the app theme.
 */
public class SettingsActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    SwitchCompat switchCompatRed, switchCompatGreen, switchCompatBlue; //for the settings themes switches
    private TextView tvDisplayTime,tvDisplayTime2;
    private TimePicker timePicker1, timePicker2;
    private Button btnChangeTime, btnChangeTime2; //"Set Lunch Time" and "Set Dinner Time"
    private Button clearFavorites;
    private int hour, hour2;
    private int minute, minute2;
    public static final String PREFS = "userPrefs";

    /**
     * on create sets up the settings layout and the action listeners
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        BaseActivity.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_settings);
        setupToolbar();

        switchCompatRed = (SwitchCompat) findViewById(R.id.switch_red);
        switchCompatRed.setSwitchPadding(40);
        switchCompatRed.setOnCheckedChangeListener(this);

        switchCompatGreen = (SwitchCompat) findViewById(R.id.switch_green);
        switchCompatGreen.setSwitchPadding(40);
        switchCompatGreen.setOnCheckedChangeListener(this);

        switchCompatBlue = (SwitchCompat) findViewById(R.id.switch_blue);
        switchCompatBlue.setSwitchPadding(40);
        switchCompatBlue.setOnCheckedChangeListener(this);

        //set lunch time area
        setCurrentTimeOnView();
        addListenerOnButton();

        //set dinner time area
        setCurrentTimeOnView2();
        addListenerOnButton2();

        // Set button for clearing favorites
        addListenerOnClear();
    }

    /**
     View for setting up dinner time
     Gets current time that the user set and fills in the time with that
     */
    public void setCurrentTimeOnView2(){
        tvDisplayTime2 = (TextView) findViewById(R.id.tvTime2);
        timePicker2 = (TimePicker) findViewById(R.id.timePickerdinner);

        final Calendar c = Calendar.getInstance();
        hour2 = c.get(Calendar.HOUR_OF_DAY);
        minute2 = c.get(Calendar.MINUTE);

        tvDisplayTime2.setText(new StringBuilder().append(pad(hour2)).append(":").append(pad(minute2)));

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        timePicker2.setIs24HourView(true);
        timePicker2.setCurrentHour(prefs.getInt("hour2", 1));
        timePicker2.setCurrentMinute(prefs.getInt("minute2", 01));
    }

    /**
     creates listener for "Change Dinner Time" button, sets the changes the user made to the current time view and
     stores them in SharedPreferences for later use
     */
    public void addListenerOnButton2(){
        btnChangeTime2 = (Button) findViewById(R.id.buttondinner);
        btnChangeTime2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putInt("hour2", timePicker2.getCurrentHour());
                editor.putInt("minute2", timePicker2.getCurrentMinute());
                editor.apply();
                Toast.makeText(getBaseContext(), "Submitted Dinner Time", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
    same as setCurrentTimeOnView2 but for lunch timing
     */
    public void setCurrentTimeOnView() {
        tvDisplayTime = (TextView) findViewById(R.id.tvTime);
        timePicker1 = (TimePicker) findViewById(R.id.timePickerlunch);
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        tvDisplayTime.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)));

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        timePicker1.setIs24HourView(true);
        timePicker1.setCurrentHour(prefs.getInt("hour", 1));
        timePicker1.setCurrentMinute(prefs.getInt("minute", 01));
    }

    /**
    same as addListenerOnButton but for lunch timing
     */
    public void addListenerOnButton() {
        btnChangeTime = (Button) findViewById(R.id.buttonlunch);
        btnChangeTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putInt("hour", timePicker1.getCurrentHour());
                editor.putInt("minute", timePicker1.getCurrentMinute());
                editor.commit();
                Toast.makeText(getBaseContext(), "Submitted Lunch Time", Toast.LENGTH_SHORT).show();

                Calendar calendar = Calendar.getInstance();
                calendar.set(calendar.HOUR_OF_DAY,getPreferences(MODE_PRIVATE).getInt("hour", 1));
                calendar.set(calendar.MINUTE,getPreferences(MODE_PRIVATE).getInt("minute", 1));

                Intent intent = new Intent(getApplicationContext(),NotificationReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
            }
        });
    }

    /**
     * Add Listener to Reset Database button
     * Only really used for Dev, shouldn't be there in final
     */
    public void addListenerOnClear(){
        clearFavorites = (Button) findViewById(R.id.buttonResetDb);
        clearFavorites.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteOpenHelper helper = new SQLiteOpenHelper(getApplicationContext());
                helper.resetDb();
                Toast.makeText(getApplicationContext(), "Favorites Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * makes the hour and minute section look nice with a 0 in front if its only 1 digit so 1 -> 01
     * @param c
     * @return
     */
    private static String pad(int c) {
        if (c >= 10) {
            return String.valueOf(c);
        }
        else {
            return "0" + String.valueOf(c);
        }
    }

    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_settings;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }

    /**
     * Whenever a new theme switch is selected it calls Utils.changeToTheme to propagate the them change app wide
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_red:
                BaseActivity.changeToTheme(this, BaseActivity.THEME_RED);
                break;

            case R.id.switch_green:
                BaseActivity.changeToTheme(this, BaseActivity.THEME_GREEN);
                break;

            case R.id.switch_blue:
                BaseActivity.changeToTheme(this, BaseActivity.THEME_BLUE);
                break;

        }
    }
}
