package com.example.screenclock.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.screenclock.AlarmSoundService;
import com.example.screenclock.BroadcastReceiver0;

import com.example.screenclock.FileEmpty;
import android.Manifest;
import com.example.screenclock.PhoneFromFile;
import com.example.screenclock.R;
import com.example.screenclock.RingtonePlayingService;
import com.example.screenclock.TimerAlarmReceiver;
import com.example.screenclock.TimerControlReceiver;
import com.example.screenclock.databinding.FragmentHomeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdSize;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.common.YandexAds;
import android.content.Intent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private BannerAdView mBannerAd = null;
    BroadcastReceiver0 BroadcastReceiver;
    LinearLayout view;
    Button button3;
    public String phone;
   TextView text_home2;
    Button startButton;
    @SuppressLint("SdCardPath")
    private static final String APP_SD_PATH = "/data/data/com.example.screenclock";
     PowerManager powerManager;
    FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private CountDownTimer countDownTimer;
    //private static final long DURATION_MS = 60_000; // 60 секунд
    private static final long DURATION_MS = 1L * 60 * 1000; // 45 минут
    long durationMs;
    private NotificationManager notificationManager;
    // Например, 2 часа 45 минут = (2*60 + 45) * 60 * 1000
    //DURATION_MS = 2L * 60 * 60 * 1000 + 45L * 60 * 1000;
    private static final long TICK_INTERVAL_MS = 1_000; // обновлять каждую секунду
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static final int REQUEST_CODE = 123;
    private static final String PREFS_NAME = "timer_prefs";
    private static int nextNotificationId = 1001;
    private int notificationId = nextNotificationId++;
    private boolean isRunning = false;
    private boolean isTimerCancelled = false;
    SwitchCompat switch1;

    private String currentTimerId = String.valueOf(notificationId); // для SharedPreferences

    //private String currentTimerId; // храним ID текущего таймера

    @SuppressLint("UseRequireInsteadOfGet")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //button3 = root.findViewById(R.id.button3);
        text_home2 = root.findViewById(R.id.text_home);
        startButton = root.findViewById(R.id.startButton);
        switch1 = root.findViewById(R.id.switch1);
        switch1.setTextSize(20);
        switch1.setTypeface( Typeface.DEFAULT_BOLD );
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                               @SuppressLint("SetTextI18n")
                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                buttonView.setText("Включить отправку смс");
                                                   // checking if the eswitch is turned on
                                                   if (isChecked) {
                                                       getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                                                   }else {
                                                       getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                                                   }
                                               }
                                           });



        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //String message = prefs.getString("hours","" );
        editor.putString("hours", "0");
        editor.putString("minutes", "45");
        editor.apply();
// Для Android M+ (API 23+) getHour/getMinute, иначе getCurrentHour/getCurrentMinute
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            timePicker.setIs24HourView(true);
//        }

        notificationId = nextNotificationId++;              // int, гарантированно в пределах int
        currentTimerId = String.valueOf(notificationId);     // используем его же как ключ в SharedPreferences

        startButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
            @Override
            public void onClick(View v) {
                @SuppressLint("UseRequireInsteadOfGet")

                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                view = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_timer, null);
                //timePicker.setIs24HourView(true);
                Button btnStart = view.findViewById(R.id.btnStart);
                Button close = view.findViewById(R.id.close);
                Button stop = view.findViewById(R.id.button3);
                String h = prefs.getString("hours","" );
                String m = prefs.getString("minutes","" );
                System.out.println(h);
                System.out.println(m);
                TimePicker timePicker = view.findViewById(R.id.timePicker);
                timePicker.setIs24HourView(true);
                timePicker.setHour(Integer.parseInt(h));      // часы
                timePicker.setMinute(Integer.parseInt(m));   // минуты





                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                btnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();
                        SharedPreferences.Editor editor = prefs.edit();
                        //String message = prefs.getString("hours","" );
                        editor.putString("hours", String.valueOf(hour));
                        editor.putString("minutes", String.valueOf(minute));
                        editor.apply();

                        if (hour==0){
                            startTimer(minute);
                            //durationMs = minute * 1000L; // в миллисекундах
                        }else {
                            int m = hour*60+minute;
                            //durationMs = m * 1000L; // в миллисекундах
                            startTimer(m);
                        }
                        durationMs = ((hour * 60L + minute) * 60L) * 1000L;
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        //if (isRunning) return;
                        isTimerCancelled = false;  // <-- сначала флаг
                        countDownTimer = new CountDownTimer(durationMs, TICK_INTERVAL_MS) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                long totalSeconds = millisUntilFinished / 1000;

                                long hours = totalSeconds / 3600;
                                long minutes = (totalSeconds % 3600) / 60;
                                long seconds = totalSeconds % 60;
//
                                @SuppressLint("DefaultLocale")
                                String timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                                text_home2.setText(timeText);
                            }

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onFinish() {
                                // Проверяем, не отменил ли пользователь до завершения
                                if (isTimerCancelled) return;
                                if (shouldStopTimer()) {
                                    text_home2.setText("00:00:00");
                                    return;
                                }
                                //isRunning = false;
                                text_home2.setText("00:00:00");
                                Toast.makeText(getActivity(), "Время вышло!", Toast.LENGTH_LONG).show();

//                                // Останавливаем сервис звука и уведомления
//                                Intent stopIntent = new Intent(getActivity(), AlarmSoundService.class);
//                                stopIntent.putExtra("action", "stop");
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                    getActivity().startForegroundService(stopIntent);
//                                } else {
//                                    getActivity().startService(stopIntent);
//                                }

                            }
                        }.start();
                        //isRunning = true;
                        alertDialog.dismiss();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //isRunning = false;
                        alertDialog.dismiss();
                    }
                });
                stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        isTimerCancelled = true;  // <-- сначала флаг
                        // 1. Останавливаем таймер
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            countDownTimer = null;
                        }
                        // 2. ОТМЕНЯЕМ AlarmManager — это главное!
                        if (alarmManager != null && pendingIntent != null) {
                            alarmManager.cancel(pendingIntent);
                        }
                        // stop service
                        Intent stopIntent = new Intent(getActivity(), AlarmSoundService.class);
                        stopIntent.putExtra("action", "stop");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            getActivity().startForegroundService(stopIntent); // на Android 8+ для корректной доставки
                        } else {
                            getActivity().startService(stopIntent);
                        }
                        // 4. Записываем флаг для shouldStopTimer()
                        prefs.edit()
                                .putBoolean("stop_requested_" + currentTimerId, true)
                                .apply();
//
                        Toast.makeText(getActivity(), "Таймер остановлен!", Toast.LENGTH_SHORT).show();
                        text_home2.setText("00:00:00");
                        // 3. Закрываем диалог
                        alertDialog.dismiss();

                    }
                });
            }

        });

//        startButton.setOnClickListener(v -> {
//
//
//                startTimer(1);
//                if (countDownTimer != null) {
//                    countDownTimer.cancel();
//                }
//                countDownTimer = new CountDownTimer(DURATION_MS, TICK_INTERVAL_MS) {
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//                        long totalSeconds = millisUntilFinished / 1000;
//
//                        long hours = totalSeconds / 3600;
//                        long minutes = (totalSeconds % 3600) / 60;
//                        long seconds = totalSeconds % 60;
////                    long minutes = millisUntilFinished / (1000 * 60);
////                    long seconds = (millisUntilFinished / 1000) % 60;
////
////                    String timeText = String.format("%02d:%02d", minutes, seconds);
////                    timerTextView.setText(timeText);
//                        @SuppressLint("DefaultLocale")
//                        String timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds);
//                        text_home2.setText(timeText);
//                    }
//
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public void onFinish() {
//                        if (shouldStopTimer()) {
//                            text_home2.setText("00:00:00");
//                            return;
//                        }
//                        text_home2.setText("00:00:00");
//                        Toast.makeText(getActivity(), "Время вышло!", Toast.LENGTH_LONG).show();
//                        closeNotification(notificationId);
//                    }
//                }.start();
//
//        });
//        button3.setText("Отправка смс выключена");
//        powerManager = (PowerManager) Objects.requireNonNull(Objects.requireNonNull(getActivity())).getSystemService(Context.POWER_SERVICE);
        powerManager = (PowerManager) Objects.requireNonNull(getActivity()).getSystemService(Context.POWER_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (checkLocationPermission()) {
            getLastLocation();
        } else {
            requestLocationPermission();
        }

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        BroadcastReceiver = new BroadcastReceiver0();
        IntentFilter filter0 = new IntentFilter();
        filter0.addAction("android.provider.Telephony.SMS_RECEIVED");

        boolean exists = FileEmpty.fileExistsInSD("phone.txt");
//        String data = String.valueOf(textMultiline.getText());
//        System.out.println(data.length());
        if (exists) {
            System.out.println("exist");
        }else {
            System.out.println(" not exist");
            try (FileOutputStream fos = getActivity().openFileOutput("phone.txt", Context.MODE_PRIVATE);
                 OutputStreamWriter osw = new OutputStreamWriter(fos)) {
                //String data = String.valueOf(textMultiline.getText());
                osw.write("number \noff");
                //            Toast.makeText(getActivity(), "Телефон "+name1+" сохранён!",
                //                    Toast.LENGTH_LONG).show();
                //вывод диалогового окна, что запись внесена
                //                                CustomDialogFragment dialog2 = new CustomDialogFragment();
                //                                dialog2.show(getSupportFragmentManager(), "custom");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        String sFolder =  APP_SD_PATH + "/files";
        String sFile=sFolder+"/"+"phone.txt";
        String[] number = PhoneFromFile.phoneFromFile(sFile);
        //start128-407
// Register the receiver using the activity context.
        getActivity().registerReceiver(BroadcastReceiver, filter0);

        binding.adContainerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        binding.adContainerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        mBannerAd = loadBannerAd(getAdSize());
                    }
                }
        );
        //
        YandexAds.initialize(getActivity(), () -> {
            // now you can use ads
            System.out.println("yandex secseful");
        });


        return root;


    }
//    private void cancelNotification() {
//        notificationManager.cancel(1001);
//        if (notificationManager != null) {
//            notificationManager.cancel(1001);
//            //countDownTimer = null;
//        }
//    }
    @SuppressLint("SetTextI18n")
    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        // Останавливаем сервис звука и уведомления
        Intent stopIntent = new Intent(getActivity(), AlarmSoundService.class);
        stopIntent.putExtra("action", "stop");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(stopIntent);
        } else {
            getActivity().startService(stopIntent);
        }
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
        isRunning = false;
        text_home2.setText("00:00");
    }
    private void closeNotification(int id) {
        NotificationManager nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) nm.cancel(id);
    }
    private boolean shouldStopTimer() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        boolean stopRequested = prefs.getBoolean("stop_requested_" + currentTimerId, false);
        if (stopRequested) {
            // Сбросим флаг, чтобы не срабатывало .повторно
            prefs.edit().remove("stop_requested_" + currentTimerId).apply();
            return true;
        }
        return false;
    }


    @SuppressLint("ScheduleExactAlarm")
    private void startTimer(int minutes) {
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), TimerAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                getActivity(),
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        long delayMs = minutes * 60_000L; // минуты -> миллисекунды
        long triggerTime = SystemClock.elapsedRealtime() + delayMs;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    pendingIntent
            );
        } else {
            alarmManager.setExact(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    pendingIntent
            );
        }
    }

    private void cancelTimer1() {
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CODE_PERMISSION_READ_CONTACTS:
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission granted
//                    readContacts();
//                } else {
//                    // permission denied
//                }
//                return;
//        }
//    }
    @SuppressLint("UseRequireInsteadOfGet")
    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }
    @SuppressLint("UseRequireInsteadOfGet")
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                Objects.requireNonNull(getActivity()),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(getActivity(), "Разрешение на геолокацию не предоставлено", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLastLocation() {
        @SuppressLint("MissingPermission")
        Task<Location> task = fusedLocationClient.getLastLocation();

        task.addOnSuccessListener( new OnSuccessListener<Location>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(android.location.Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
//                    button3.setText("Широта: " + latitude + "\nДолгота: " + longitude);
//                } else {
//                    button3.setText("Последнее местоположение недоступно");
                }
            }
        });
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

        return BannerAdSize.sticky(getActivity(), adWidth);
    }
    @NonNull
    private BannerAdView loadBannerAd(@NonNull final BannerAdSize adSize) {
        final BannerAdView bannerAd = binding.adContainerView;
        bannerAd.setAdSize(adSize);
        //bannerAd.setAdUnitId("R-M-18319832-1");//"demo-banner-yandex"
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

            public void onLeftApplication() {
                // Called when user is about to leave application (e.g., to go to the browser), as a result of clicking on the ad.
                System.out.println("onLeftApplication");
            }

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
        final AdRequest adRequest = new AdRequest.Builder("R-M-18319832-1")//R-M-18319832-1
                // Methods in the AdRequest.Builder class can be used here to specify individual options settings.
                .build();
        bannerAd.loadAd(adRequest);
        return bannerAd;
    }

    @Override
    public void onDestroyView() {
        if (BroadcastReceiver != null) {
            getActivity().unregisterReceiver(BroadcastReceiver);
            BroadcastReceiver = null;
        }
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//            countDownTimer = null;
//        }
//        if (TimerAlarmReceiver != null) {
//            getActivity().unregisterReceiver(TimerAlarmReceiver);
//            BroadcastReceiver = null;
//        }
        //stopTimer();
        //cancelTimer();
        super.onDestroyView();
        binding = null;
    }
}