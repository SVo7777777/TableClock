package com.example.screenclock.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.PowerManager;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.screenclock.BroadcastReceiver0;

import com.example.screenclock.FileEmpty;
import android.Manifest;
import com.example.screenclock.PhoneFromFile;
import com.example.screenclock.R;
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
import com.yandex.mobile.ads.common.MobileAds;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Objects;
import java.util.concurrent.Executor;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private BannerAdView mBannerAd = null;
    BroadcastReceiver0 BroadcastReceiver;
    LinearLayout view;
    Button button3;
    public String phone;
   TextView text_home2;
    @SuppressLint("SdCardPath")
    private static final String APP_SD_PATH = "/data/data/com.example.screenclock";
     PowerManager powerManager;
    FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    @SuppressLint("UseRequireInsteadOfGet")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //button3 = root.findViewById(R.id.button3);
        text_home2 = root.findViewById(R.id.textView2);
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
//        if (number[1].equals("on")){
//            button3.setTextColor(Color.RED);
//            System.out.println(number[1]);
//            button3.setText("Отправка геолокации включена");
//        }else {
//            button3.setTextColor(Color.WHITE);
//            System.out.println(number[1]);
//            button3.setText("Отправка геолокации выключена");
//        }
//        button3.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
//            @Override
//            public void onClick(View v) {
////                @SuppressLint("SimpleDateFormat") final SimpleDateFormat sdf1 = new SimpleDateFormat("EE dd-MM-yyyy");
////                calendar.set(current_year, current_month, current_day);
////                String sDate_now = sdf1.format(calendar.getTime());
////                System.out.println("sDate_now=" + sDate_now);
////                String data = current_month+" "+current_year;
////                String month3 = monthNames[current_month];
////                System.out.println(month3);
////                String month_year = month3 + " "+current_year;
//
//
//                @SuppressLint("UseRequireInsteadOfGet")
//                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
//                view = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_add_employee, null);
////                TextView time = view.findViewById(R.id.textView);
////                time.setText("");
//                TextView month = view.findViewById(R.id.month);
//                Button add = view.findViewById(R.id.button);
//                Button close = view.findViewById(R.id.close);
//                Button delete = view.findViewById(R.id.button3);
//                EditText employee_name1 = view.findViewById(R.id.editTextName1);
//                SwitchCompat switch1 = view.findViewById(R.id.switch1);
//                month.setText("Введите номер телефона, на который нужно отправить СМС с геолокацией телефона");
//
//                String line1 = null;
//                String line2 = null;
//                StringBuilder sb = new StringBuilder();
//                try (FileInputStream fis = getActivity().openFileInput("phone.txt");
//                     InputStreamReader isr = new InputStreamReader(fis);
//                     BufferedReader br = new BufferedReader(isr)) {
//
//                    String line = br.readLine();
//                    if (line != null) {
//                        line1 = line;
//                        line = br.readLine();
//                        if (line != null) {
//                            line2 = line;
//                        }
//                    }
//                    if (Objects.equals(line1, "number")){
//                        System.out.println("number");
//                    }else {
//                        employee_name1.setText(line1);
//                    }
//                    if (Objects.equals(line, "on")){
//                        System.out.println("on");
//                        switch1.setChecked(true);
//                        switch1.setTextColor( Color.RED);
//                        switch1.setText("Отправка смс включена");
//                        button3.setTextColor(Color.RED);
//                        button3.setText("Геолокация включена");
//                        //sendSmsByManager("+79156954581", "смс отправлена!");
//                    }else {
//                        button3.setTextColor(Color.WHITE);
//                        button3.setText("Отправка геолокации выключена");
//                    }
//
//                    System.out.println(line1);
//                    System.out.println(line2);
//
//
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
////                SwitchCompat switch1 = view.findViewById(R.id.switch1);
//
//                switch1.setTextSize(20);
//                switch1.setTypeface( Typeface.DEFAULT_BOLD );
//                switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public void onCheckedChanged (CompoundButton buttonView, boolean isChecked){
////                buttonView.setText("Включить отправку смс");
//                        // checking if the switch is turned on
//                        if (isChecked) {
//                            String line1 = null;
//                            String line2 = null;
//                            StringBuilder sb = new StringBuilder();
//                            try (FileInputStream fis = getActivity().openFileInput("phone.txt");
//                                 InputStreamReader isr = new InputStreamReader(fis);
//                                 BufferedReader br = new BufferedReader(isr)) {
//
//                                String line = br.readLine();
//                                if (line != null) {
//                                    line1 = line;
//                                    line = br.readLine();
//                                    if (line != null) {
//                                        line2 = line;
//                                    }
//                                }
//
//                                System.out.println(line1);
//                                System.out.println(line2);
//
//
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                            try (FileOutputStream fos = getActivity().openFileOutput("phone.txt", Context.MODE_PRIVATE);
//                                 OutputStreamWriter osw = new OutputStreamWriter(fos)) {
//                                //String data = String.valueOf(textMultiline.getText());
//                                osw.write(line1+"\n"+"on");
//                                //sendSmsByManager("+79156954581", "смс отправлена!");
//                                System.out.println("on");
//                                Toast.makeText(getActivity(), "Отправка СМС включена!!",
//                                        Toast.LENGTH_LONG).show();
//                                //вывод диалогового окна, что запись внесена
////                                CustomDialogFragment dialog2 = new CustomDialogFragment();
////                                dialog2.show(getSupportFragmentManager(), "custom");
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//
//                            // setting theme to night mode
////                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                            System.out.println("Отправка смс включена");
//                            // setting theme to night mode
////                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                            buttonView.setText("Отправка смс включена");
//                            switch1.setTextColor( Color.RED);
//                            button3.setTextColor(Color.RED);
//                            button3.setText("Геолокация включена");
//                        }
//
//                        // if the above condition turns false
//                        // it means switch is turned off
//                        // by-default the switch will be off
//                        else {
//                            String line1 = null;
//                            String line2 = null;
//                            StringBuilder sb = new StringBuilder();
//                            try (FileInputStream fis = getActivity().openFileInput("phone.txt");
//                                 InputStreamReader isr = new InputStreamReader(fis);
//                                 BufferedReader br = new BufferedReader(isr)) {
//
//                                String line = br.readLine();
//                                if (line != null) {
//                                    line1 = line;
//                                    line = br.readLine();
//                                    if (line != null) {
//                                        line2 = line;
//                                    }
//                                }
//
//                                System.out.println(line1);
//                                System.out.println(line2);
//
//
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                            try (FileOutputStream fos = getActivity().openFileOutput("phone.txt", Context.MODE_PRIVATE);
//                                 OutputStreamWriter osw = new OutputStreamWriter(fos)) {
//                                //String data = String.valueOf(textMultiline.getText());
//                                osw.write(line1+"\n"+"off");
//                                Toast.makeText(getActivity(), "Отправка СМС выключена!!",
//                                        Toast.LENGTH_LONG).show();
//                                //вывод диалогового окна, что запись внесена
////                                CustomDialogFragment dialog2 = new CustomDialogFragment();
////                                dialog2.show(getSupportFragmentManager(), "custom");
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//
//                            // setting theme to light theme
////                    AppCompatDelegate.setDefaultNightMode (AppCompatDelegate.MODE_NIGHT_NO);
//                            buttonView.setText("Отправка смс выключена");
//                            switch1.setTextColor( Color.WHITE);
//                            button3.setTextColor(Color.WHITE);
//                            button3.setText("Отправка геолокации выключена");
//                        }
//                    }
//                });
////                EditText employee_name2 = view.findViewById(R.id.editTextName2);
////                EditText employee_phone = view.findViewById(R.id.editTextPhon2);
////                EditText employee_address = view.findViewById(R.id.editTextAdress2);
//
////                String name = (String) button_employee.getText();
////                String[] str = name.split(" ");
////                employee_name1.setText(str[0]);
////                employee_name2.setText(str[1]);
//
//                //int id = mydb.GetIdEmployee(name,  DatabaseHelperLess.TABLE);
////                String ph = mydb.getPhone(name,DatabaseHelperLess.TABLE);
////                String ad = mydb.getAddress(name, DatabaseHelperLess.TABLE);
////                employee_phone.setText(ph);
////                employee_address.setText(ad);
//
//                //student_payment.setText(((String) payment.getText()).substring(10));
//
//
//                employee_name1.requestFocus();
//                employee_name1.setSelection(employee_name1.getText().length());
//                //вывод клавиатуры после нажатия на дату
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                assert imm != null;
//                imm.showSoftInput(employee_name1, InputMethodManager.SHOW_IMPLICIT);
//
//                builder.setView(view);
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//
//                add.setOnClickListener(new View.OnClickListener() {
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public void onClick(View view) {
//
//                        String name1 = String.valueOf(employee_name1.getText());
////                        String name2 = String.valueOf(employee_name2.getText());
////                        String phone = String.valueOf(employee_phone.getText());
////                        String address = String.valueOf(employee_address.getText());
//                        String name_employee = name1+" ";//+name2;
//
//                        if (employee_name1.getText().toString().trim().isEmpty()){// || employee_name2.getText().toString().trim().isEmpty()) {
//                            Toast.makeText(getActivity(), "Заполните поля!", Toast.LENGTH_LONG).show();
//
//                        } else {
//
//                            try (FileOutputStream fos = getActivity().openFileOutput("phone.txt", Context.MODE_PRIVATE);
//                                 OutputStreamWriter osw = new OutputStreamWriter(fos)) {
//                                //String data = String.valueOf(textMultiline.getText());
//                                osw.write(name1+"\noff");
//                                Toast.makeText(getActivity(), "Телефон "+name1+" сохранён!",
//                                        Toast.LENGTH_LONG).show();
//                                phone = name1;
//                                //вывод диалогового окна, что запись внесена
////                                CustomDialogFragment dialog2 = new CustomDialogFragment();
////                                dialog2.show(getSupportFragmentManager(), "custom");
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                            alertDialog.dismiss();
//
//                        }
//
//
//                        //обновление виджета
////                        Intent intentq = new Intent(getActivity(), MyWidget2.class);
////                        intentq.setAction("android.appwidget.action.APPWIDGET_UPDATE");
////                        int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), MyWidget2.class));
////                        intentq.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
////                        getActivity().sendBroadcast(intentq);
//                        //Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();//display the text of button1
//                    }
//
//                });
//                close.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//                delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        int id = mydb.GetIdEmployee(name,  DatabaseHelperLess.TABLE);
////                        mydb.deleteContact1(id);
////                        mydb.deleteContact(id);
////                        list.removeView(ln);
//                        employee_name1.setText("");
//                        //alertDialog.dismiss();
//                    }
//                });
//            };
//        });
        //finish128-407
//
//        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
//
//        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
//            readContacts();
//        } else {
//            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS},
//                    REQUEST_CODE_PERMISSION_READ_CONTACTS);
//        }

//////        // Register the receiver using the activity context.
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
        MobileAds.initialize(getActivity(), () -> {
            // now you can use ads
            System.out.println("yandex secseful");
        });


        return root;


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
        if (BroadcastReceiver != null) {
            getActivity().unregisterReceiver(BroadcastReceiver);
            BroadcastReceiver = null;
        }
        super.onDestroyView();
        binding = null;
    }
}