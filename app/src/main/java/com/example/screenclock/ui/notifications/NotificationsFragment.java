package com.example.screenclock.ui.notifications;

import static android.os.ParcelFileDescriptor.MODE_APPEND;

import static androidx.core.content.ContextCompat.getSystemService;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;

import com.example.screenclock.BroadcastReceiver0;
import com.example.screenclock.MainActivity;
import com.example.screenclock.MyBroadcastReceiver;
import com.example.screenclock.R;
import com.example.screenclock.ReviewTodayActivity;
import com.example.screenclock.RingtonePlayingService;
import com.example.screenclock.ScreenReceiver;
import com.example.screenclock.ServiceReceiver;
import com.example.screenclock.SmsService;
import com.example.screenclock.SmsWorker;
import com.example.screenclock.databinding.FragmentNotificationsBinding;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdSize;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.common.MobileAds;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.example.screenclock.PhoneFromFile;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    TimePicker timePicker;
    Button button2;
    Button button3;
    public TextView textView2;
     BroadcastReceiver batteryLevelReceiver;
    private BannerAdView mBannerAd = null;
    public String phone;
    public String message;
    MyBroadcastReceiver myBroadcastReceiver;
    BroadcastReceiver0 BroadcastReceiver;
    ScreenReceiver screenReceiver;
    PowerManager powerManager;
    ServiceReceiver serviceReceiver;
    LinearLayout view;
    Boolean sms;
    @SuppressLint("SdCardPath")
    private static final String APP_SD_PATH = "/data/data/com.example.screenclock";

    int brightness;
    public View onCreateView(@NonNull LayoutInflater inflater,
                                                       ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        timePicker = root.findViewById(R.id.timePicker);
        button2 = root.findViewById(R.id.button2);
        button3 = root.findViewById(R.id.button3);
        textView2 = root.findViewById(R.id.textView2);
        StringBuilder str = new StringBuilder("   Сигнализация включена. \n   Чтобы сигнализация сработала, отключите устройство от питания.\n   Чтобы отключить сигнал, нажмите на кнопку ВЫКЛЮЧИТЬ СИГНАЛ или перейдите на другую вкладку.\n   Чтобы отключить сигнализацию перейдите на другую вкладку или закройте  приложение.");
        textView2.setText(str);
        powerManager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);

        Toast.makeText(getActivity(), "Cигнализация включена!", Toast.LENGTH_SHORT).show();


        checkAndRPermission();
//смс отправляется через 5 сек после нажатия на вкладку сигнализация
        //scheduleSmsAfterUnplug(getActivity(), "+79156954581", "Sms sending!");


//        Intent serviceIntent = new Intent(getActivity(), SmsService.class);
//        serviceIntent.putExtra("phoneNumber", "+79156954581");
//        serviceIntent.putExtra("message", "sms sending!!");
//        getActivity().startService(serviceIntent);
        //sendSmsByManager("+79156954581", "смс отправлена!");
//        SwitchCompat switch1 = root.findViewById(R.id.switch1);
//        switch1.setTextSize(20);
//        switch1.setTypeface( Typeface.DEFAULT_BOLD );
        //switch1.setPadding(12,14,6,10 );
        //switch1.setBackgroundColor( Color.RED);
//        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            String message = isChecked ? "Switch1:ON" : "Switch1:OFF";
//            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//        });
//        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onCheckedChanged (CompoundButton buttonView, boolean isChecked){
////                buttonView.setText("Включить отправку смс");
//                // checking if the switch is turned on
//                if (isChecked) {
//                    String line1 = null;
//                    String line2 = null;
//                    StringBuilder sb = new StringBuilder();
//                    try (FileInputStream fis = getActivity().openFileInput("phone.txt");
//                         InputStreamReader isr = new InputStreamReader(fis);
//                         BufferedReader br = new BufferedReader(isr)) {
//
//                        String line = br.readLine();
//                        if (line != null) {
//                            line1 = line;
//                            line = br.readLine();
//                            if (line != null) {
//                                line2 = line;
//                            }
//                        }
//
//                        System.out.println(line1);
//                        System.out.println(line2);
//
//
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    try (FileOutputStream fos = getActivity().openFileOutput("phone.txt", Context.MODE_PRIVATE);
//                         OutputStreamWriter osw = new OutputStreamWriter(fos)) {
//                        //String data = String.valueOf(textMultiline.getText());
//                        osw.write(line1+"\n"+"on");
//
//                        System.out.println("on");
//                        Toast.makeText(getActivity(), "Отправка СМС включена!!",
//                                Toast.LENGTH_LONG).show();
//                        //вывод диалогового окна, что запись внесена
////                                CustomDialogFragment dialog2 = new CustomDialogFragment();
////                                dialog2.show(getSupportFragmentManager(), "custom");
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    // setting theme to night mode
////                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    System.out.println("Отправка смс включена");
//                    buttonView.setText("Отправка смс включена");
//                    switch1.setTextColor( Color.RED);
//                }
//
//                // if the above condition turns false
//                // it means switch is turned off
//                // by-default the switch will be off
//                else {
//
//                    // setting theme to light theme
////                    AppCompatDelegate.setDefaultNightMode (AppCompatDelegate.MODE_NIGHT_NO);
//                    String line1 = null;
//                    String line2 = null;
//                    StringBuilder sb = new StringBuilder();
//                    try (FileInputStream fis = getActivity().openFileInput("phone.txt");
//                         InputStreamReader isr = new InputStreamReader(fis);
//                         BufferedReader br = new BufferedReader(isr)) {
//
//                        String line = br.readLine();
//                        if (line != null) {
//                            line1 = line;
//                            line = br.readLine();
//                            if (line != null) {
//                                line2 = line;
//                            }
//                        }
//
//                        System.out.println(line1);
//                        System.out.println(line2);
//
//
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    try (FileOutputStream fos = getActivity().openFileOutput("phone.txt", Context.MODE_PRIVATE);
//                         OutputStreamWriter osw = new OutputStreamWriter(fos)) {
//                        //String data = String.valueOf(textMultiline.getText());
//                        osw.write(line1+"\n"+"off");
//                        Toast.makeText(getActivity(), "Отправка СМС выключена!!",
//                                Toast.LENGTH_LONG).show();
//                        //вывод диалогового окна, что запись внесена
////                                CustomDialogFragment dialog2 = new CustomDialogFragment();
////                                dialog2.show(getSupportFragmentManager(), "custom");
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    buttonView.setText("Отправка смс выключена");
//                    switch1.setTextColor( Color.WHITE);
//                }
//            }
//        });
        //
//        brightness =
//                Settings.System.getInt(getActivity().getContentResolver(),
//                        Settings.System.SCREEN_BRIGHTNESS, 0);
//        Settings.System.putInt(getActivity().getContentResolver(),
//                Settings.System.SCREEN_BRIGHTNESS, 200);
        //turnOnScreen();
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
       filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
       filter.addAction(Intent.ACTION_POWER_CONNECTED);
//////        // Register the receiver using the activity context.
        getActivity().registerReceiver(myBroadcastReceiver, filter);

        BroadcastReceiver = new BroadcastReceiver0();
        IntentFilter filter0 = new IntentFilter();
        filter0.addAction("android.provider.Telephony.SMS_RECEIVED");

//////        // Register the receiver using the activity context.
        getActivity().registerReceiver(BroadcastReceiver, filter0);

        serviceReceiver = new ServiceReceiver();
        IntentFilter filter01 = new IntentFilter();
        filter01.addAction("android.provider.Telephony.SMS_RECEIVED");

//////        // Register the receiver using the activity context.
        getActivity().registerReceiver(serviceReceiver, filter0) ;

        screenReceiver = new ScreenReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(Intent.ACTION_SCREEN_OFF);
        filter1.addAction(Intent.ACTION_SCREEN_ON);
//////        // Register the receiver using the activity context.
        getActivity().registerReceiver(screenReceiver, filter1);
//        Intent intent4 = new Intent(getContext(), ReviewTodayActivity.class);
//        startActivity(intent4);

        button2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
            @Override
            public void onClick(View v) {
                Intent i5= new Intent(getActivity(), RingtonePlayingService.class);
                getActivity().stopService(i5);
            }
        });
        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        String sFolder =  APP_SD_PATH + "/files";
        String sFile=sFolder+"/"+"phone.txt";
        String[] number = PhoneFromFile.phoneFromFile(sFile);
        if (number[1].equals("on")){
            button3.setTextColor(Color.RED);
            System.out.println(number[1]);
            button3.setText("Отправка смс включена");
        }else {
            button3.setTextColor(Color.WHITE);
            System.out.println(number[1]);
            button3.setText("Отправка смс выключена");
        }

        button3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
            @Override
            public void onClick(View v) {
//                @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf1 = new SimpleDateFormat("EE dd-MM-yyyy");
//                calendar.set(current_year, current_month, current_day);
//                String sDate_now = sdf1.format(calendar.getTime());
//                System.out.println("sDate_now=" + sDate_now);
//                String data = current_month+" "+current_year;
//                String month3 = monthNames[current_month];
//                System.out.println(month3);
//                String month_year = month3 + " "+current_year;


                @SuppressLint("UseRequireInsteadOfGet")
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                view = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_add_employee, null);
//                TextView time = view.findViewById(R.id.textView);
//                time.setText("");
                TextView month = view.findViewById(R.id.month);
                Button add = view.findViewById(R.id.button);
                Button close = view.findViewById(R.id.close);
                Button delete = view.findViewById(R.id.button3);
                EditText employee_name1 = view.findViewById(R.id.editTextName1);
                SwitchCompat switch1 = view.findViewById(R.id.switch1);

                String line1 = null;
                String line2 = null;
                StringBuilder sb = new StringBuilder();
                try (FileInputStream fis = getActivity().openFileInput("phone.txt");
                     InputStreamReader isr = new InputStreamReader(fis);
                     BufferedReader br = new BufferedReader(isr)) {

                    String line = br.readLine();
                    if (line != null) {
                        line1 = line;
                        line = br.readLine();
                        if (line != null) {
                            line2 = line;
                        }
                    }
                    if (Objects.equals(line1, "number")){
                        System.out.println("number");
                    }else {
                        employee_name1.setText(line1);
                    }
                    if (Objects.equals(line, "on")){
                        System.out.println("on");
                        switch1.setChecked(true);
                        switch1.setTextColor( Color.RED);
                        switch1.setText("Отправка смс включена");
                        button3.setTextColor(Color.RED);
                        button3.setText("Отправка смс включена");
                        //sendSmsByManager("+79156954581", "смс отправлена!");
                    }else {
                        button3.setTextColor(Color.WHITE);
                        button3.setText("Отправка смс выключена");
                    }

                    System.out.println(line1);
                    System.out.println(line2);


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

//                SwitchCompat switch1 = view.findViewById(R.id.switch1);

                switch1.setTextSize(20);
                switch1.setTypeface( Typeface.DEFAULT_BOLD );
                switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onCheckedChanged (CompoundButton buttonView, boolean isChecked){
//                buttonView.setText("Включить отправку смс");
                        // checking if the switch is turned on
                        if (isChecked) {
                            String line1 = null;
                            String line2 = null;
                            StringBuilder sb = new StringBuilder();
                            try (FileInputStream fis = getActivity().openFileInput("phone.txt");
                                 InputStreamReader isr = new InputStreamReader(fis);
                                 BufferedReader br = new BufferedReader(isr)) {

                                String line = br.readLine();
                                if (line != null) {
                                    line1 = line;
                                    line = br.readLine();
                                    if (line != null) {
                                        line2 = line;
                                    }
                                }

                                System.out.println(line1);
                                System.out.println(line2);


                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            try (FileOutputStream fos = getActivity().openFileOutput("phone.txt", Context.MODE_PRIVATE);
                                 OutputStreamWriter osw = new OutputStreamWriter(fos)) {
                                //String data = String.valueOf(textMultiline.getText());
                                osw.write(line1+"\n"+"on");
                                //sendSmsByManager("+79156954581", "смс отправлена!");
                                System.out.println("on");
                                Toast.makeText(getActivity(), "Отправка СМС включена!!",
                                        Toast.LENGTH_LONG).show();
                                //вывод диалогового окна, что запись внесена
//                                CustomDialogFragment dialog2 = new CustomDialogFragment();
//                                dialog2.show(getSupportFragmentManager(), "custom");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            // setting theme to night mode
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            System.out.println("Отправка смс включена");
                            // setting theme to night mode
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            buttonView.setText("Отправка смс включена");
                            switch1.setTextColor( Color.RED);
                            button3.setTextColor(Color.RED);
                            button3.setText("Отправка смс включена");
                        }

                        // if the above condition turns false
                        // it means switch is turned off
                        // by-default the switch will be off
                        else {
                            String line1 = null;
                            String line2 = null;
                            StringBuilder sb = new StringBuilder();
                            try (FileInputStream fis = getActivity().openFileInput("phone.txt");
                                 InputStreamReader isr = new InputStreamReader(fis);
                                 BufferedReader br = new BufferedReader(isr)) {

                                String line = br.readLine();
                                if (line != null) {
                                    line1 = line;
                                    line = br.readLine();
                                    if (line != null) {
                                        line2 = line;
                                    }
                                }

                                System.out.println(line1);
                                System.out.println(line2);


                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            try (FileOutputStream fos = getActivity().openFileOutput("phone.txt", Context.MODE_PRIVATE);
                                 OutputStreamWriter osw = new OutputStreamWriter(fos)) {
                                //String data = String.valueOf(textMultiline.getText());
                                osw.write(line1+"\n"+"off");
                                Toast.makeText(getActivity(), "Отправка СМС выключена!!",
                                        Toast.LENGTH_LONG).show();
                                //вывод диалогового окна, что запись внесена
//                                CustomDialogFragment dialog2 = new CustomDialogFragment();
//                                dialog2.show(getSupportFragmentManager(), "custom");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            // setting theme to light theme
//                    AppCompatDelegate.setDefaultNightMode (AppCompatDelegate.MODE_NIGHT_NO);
                            buttonView.setText("Отправка смс выключена");
                            switch1.setTextColor( Color.WHITE);
                            button3.setTextColor(Color.WHITE);
                            button3.setText("Отправка смс выключена");
                        }
                    }
                });
//                EditText employee_name2 = view.findViewById(R.id.editTextName2);
//                EditText employee_phone = view.findViewById(R.id.editTextPhon2);
//                EditText employee_address = view.findViewById(R.id.editTextAdress2);

//                String name = (String) button_employee.getText();
//                String[] str = name.split(" ");
//                employee_name1.setText(str[0]);
//                employee_name2.setText(str[1]);

                //int id = mydb.GetIdEmployee(name,  DatabaseHelperLess.TABLE);
//                String ph = mydb.getPhone(name,DatabaseHelperLess.TABLE);
//                String ad = mydb.getAddress(name, DatabaseHelperLess.TABLE);
//                employee_phone.setText(ph);
//                employee_address.setText(ad);

                //student_payment.setText(((String) payment.getText()).substring(10));


                employee_name1.requestFocus();
                employee_name1.setSelection(employee_name1.getText().length());
                //вывод клавиатуры после нажатия на дату
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.showSoftInput(employee_name1, InputMethodManager.SHOW_IMPLICIT);

                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                add.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View view) {

                        String name1 = String.valueOf(employee_name1.getText());
//                        String name2 = String.valueOf(employee_name2.getText());
//                        String phone = String.valueOf(employee_phone.getText());
//                        String address = String.valueOf(employee_address.getText());
                        String name_employee = name1+" ";//+name2;

                        if (employee_name1.getText().toString().trim().isEmpty()){// || employee_name2.getText().toString().trim().isEmpty()) {
                            Toast.makeText(getActivity(), "Заполните поля!", Toast.LENGTH_LONG).show();

                        } else {

                            try (FileOutputStream fos = getActivity().openFileOutput("phone.txt", Context.MODE_PRIVATE);
                                 OutputStreamWriter osw = new OutputStreamWriter(fos)) {
                                //String data = String.valueOf(textMultiline.getText());
                                osw.write(name1+"\noff");
                                Toast.makeText(getActivity(), "Телефон "+name1+" сохранён!",
                                        Toast.LENGTH_LONG).show();
                                phone = name1;
                                //вывод диалогового окна, что запись внесена
//                                CustomDialogFragment dialog2 = new CustomDialogFragment();
//                                dialog2.show(getSupportFragmentManager(), "custom");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            alertDialog.dismiss();

                        }


                        //обновление виджета
//                        Intent intentq = new Intent(getActivity(), MyWidget2.class);
//                        intentq.setAction("android.appwidget.action.APPWIDGET_UPDATE");
//                        int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), MyWidget2.class));
//                        intentq.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
//                        getActivity().sendBroadcast(intentq);
                        //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();//display the text of button1
                    }

                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        int id = mydb.GetIdEmployee(name,  DatabaseHelperLess.TABLE);
//                        mydb.deleteContact1(id);
//                        mydb.deleteContact(id);
//                        list.removeView(ln);
                        employee_name1.setText("");
                        //alertDialog.dismiss();
                    }
                });
            };
        });


    final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        binding.adContainerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        binding.adContainerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        mBannerAd = loadBannerAd(getAdSize());
                    }
                }
        );
        MobileAds.initialize(getActivity(), () -> {
            // now you can use ads
            System.out.println("yandex secseful");
        });

        return root;
    }

    private void scheduleSmsAfterUnplug(Context context, String phone_number, String  message) {
        Data inputData = new Data.Builder()
                .putString("phone_number", phone_number) // Замените на нужный номер
                .putString("message", message)
                .build();

        OneTimeWorkRequest smsWork = new OneTimeWorkRequest.Builder(SmsWorker.class)
                .setInputData(inputData)
                .setInitialDelay(5, TimeUnit.SECONDS) // Задержка 30 секунд
                .addTag("sms_after_unplug")
                .build();

        WorkManager.getInstance(context).enqueue(smsWork);
    }
    public void sendSmsBySIntent() {
        // add the phone number in the data
        Uri uri = Uri.parse("smsto:" + "+79156954581");

        Intent smsSIntent = new Intent(Intent.ACTION_SENDTO, uri);
        // add the message at the sms_body extra field
        smsSIntent.putExtra("sms_body", "hello, sveta from sintent!");
        try{
            startActivity(smsSIntent);
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Your sms has failed...",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    public void sendSmsByVIntent() {

        Intent smsVIntent = new Intent(Intent.ACTION_VIEW);
        // prompts only sms-mms clients
        smsVIntent.setType("vnd.android-dir/mms-sms");

        // extra fields for number and message respectively
        smsVIntent.putExtra("address", "+79156954581");
        smsVIntent.putExtra("sms_body", "hello, sveta from vintent!");
        try{
            startActivity(smsVIntent);
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Your sms has failed...",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

    }
    public void sendSmsByManager(String number, String text) {
        try {
            // Get the default instance of the SmsManager
            SmsManager smsManager = SmsManager.getDefault();
            PendingIntent piSend = PendingIntent.getBroadcast(getActivity(), 0, new Intent(android.Manifest.permission.SEND_SMS), PendingIntent.FLAG_IMMUTABLE);
            // PendingIntent piDelivered = PendingIntent.getBroadcast(this, 0, new Intent(Manifest.SMS_DELIVERED), 0);

            smsManager.sendTextMessage(number,
                    null,
                    text,
                    piSend,
                    null);
            Toast.makeText(getActivity(), "СМС успешно отправлено!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getActivity(),"Your sms has failed...",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();

        }
    }
    protected void checkAndRPermission() {
//        phoneNo = "+79156954581";
//        message = "hello!!!!!";
        System.out.println("checkAndRPermission");
        int permissionStatus = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            //sendSmsByManager();
            System.out.println("checkAndRPermission2");
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.SEND_SMS},
                    101);
            System.out.println("checkAndRPermission3");
        }
//        if (ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.SEND_SMS)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                    Manifest.permission.SEND_SMS)) {
//
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{Manifest.permission.SEND_SMS},
//                        101);
//            }else{
//                System.out.println("checkAndRPermission");
//                sendSmsByManager();
//            }
//        }else {
//            System.out.println("checkAndRPermission2");
//        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //SmsManager smsManager = SmsManager.getDefault();
                    //sendSmsByManager();
                    System.out.println("разрешение");
//                    smsManager.sendTextMessage("+79156954581", null, "hello!!!!!", null, null);
//                    Toast.makeText(getActivity(), "SMS sent.",
//                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

        @SuppressLint("SetTextI18n")
    public void setText(String item) {
        TextView view = (TextView) getView().findViewById(R.id.textView2);
        String t = (String) view.getText();
        if (t.isEmpty()){
            view.setText(item);
        }else{
            view.setText(t+"/n"+item);
        }

    }

    @NonNull
    private BannerAdSize getAdSize() {
        final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        // Calculate the width of the ad, taking into account the padding in the ad container.
        int adWidthPixels = binding.adContainerView.getWidth();
        if (adWidthPixels == 0) {
            // If the ad hasn't been laid out, default to the full screen width
            adWidthPixels = displayMetrics.widthPixels;
        }
        final int adWidth = Math.round(adWidthPixels / displayMetrics.density);

        return BannerAdSize.stickySize(getActivity(), adWidth);
    }
    @NonNull
    private BannerAdView loadBannerAd(@NonNull final BannerAdSize adSize) {
        final BannerAdView bannerAd = binding.adContainerView;
        bannerAd.setAdSize(adSize);
        bannerAd.setAdUnitId("R-M-18319832-1");//"demo-banner-yandex"
        bannerAd.setBannerAdEventListener(new BannerAdEventListener() {
            @Override
            public void onAdLoaded() {
                // If this callback occurs after the activity is destroyed, you
                // must call destroy and return or you may get a memory leak.
                // Note `isDestroyed` is a method on Activity.
                try {
                    if (requireActivity().isDestroyed() && mBannerAd != null) {
                        mBannerAd.destroy();
                    }
                } catch (IllegalStateException e) { // обрабатывать исключение
                    Activity activity = getActivity();
                    if (isAdded() && activity != null) {
                        mBannerAd.destroy();
                        System.out.println("MyApp " + "Error: not attached to an activity " + e);
                    }

                }
                System.out.println("yandex onadloaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull final AdRequestError adRequestError) {
                // Ad failed to load with AdRequestError.
                // Attempting to load a new ad from the onAdFailedToLoad() method is strongly discouraged.
                System.out.println("onAdFailedToLoad");
            }

            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                System.out.println("onAdClicked");
            }

            @Override
            public void onLeftApplication() {
                // Called when user is about to leave application (e.g., to go to the browser), as a result of clicking on the ad.
                System.out.println("onLeftApplication");
            }

            @Override
            public void onReturnedToApplication() {
                // Called when user returned to application after click.
                System.out.println("onReturnedToApplication");
            }

            @Override
            public void onImpression(@Nullable ImpressionData impressionData) {
                // Called when an impression is recorded for an ad.
                if (impressionData != null)
                    System.out.println("onImpression");
            }
        });
        final AdRequest adRequest = new AdRequest.Builder()
                // Methods in the AdRequest.Builder class can be used here to specify individual options settings.
                .build();
        bannerAd.loadAd(adRequest);
        return bannerAd;
    }

    @Override
    public void onDestroyView() {
        if (myBroadcastReceiver != null) {
            getActivity().unregisterReceiver(myBroadcastReceiver);
            myBroadcastReceiver = null;
            Toast.makeText(getActivity(), "Cигнализация выключена!", Toast.LENGTH_LONG).show();
            Intent i2= new Intent(getActivity(), RingtonePlayingService.class);
            getActivity().stopService(i2);

        }
        if (screenReceiver != null) {
            getActivity().unregisterReceiver(screenReceiver);
            screenReceiver = null;
        }
        if (BroadcastReceiver != null) {
            getActivity().unregisterReceiver(BroadcastReceiver);
            BroadcastReceiver = null;
        }
        super.onDestroyView();
        binding = null;
    }
}