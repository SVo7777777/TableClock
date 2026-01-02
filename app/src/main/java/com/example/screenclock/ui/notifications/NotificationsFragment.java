package com.example.screenclock.ui.notifications;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.screenclock.MyBroadcastReceiver;
import com.example.screenclock.R;
import com.example.screenclock.ReviewTodayActivity;
import com.example.screenclock.RingtonePlayingService;
import com.example.screenclock.databinding.FragmentNotificationsBinding;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdSize;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.common.MobileAds;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    TimePicker timePicker;
    Button button2;
    public TextView textView2;
     BroadcastReceiver batteryLevelReceiver;
    private BannerAdView mBannerAd = null;
    MyBroadcastReceiver myBroadcastReceiver;
    public View onCreateView(@NonNull LayoutInflater inflater,
                                                       ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        timePicker = root.findViewById(R.id.timePicker);
        button2 = root.findViewById(R.id.button2);
        textView2 = root.findViewById(R.id.textView2);
        StringBuilder str = new StringBuilder("   Сигнализация включена. \n   Чтобы сигнализация сработала, отключите устройство от питания.\n   Чтобы отключить сигнал, нажмите на кнопку ВЫКЛЮЧИТЬ СИГНАЛ или перейдите на другую вкладку.\n   Чтобы отключить сигнализацию перейдите на другую вкладку или закройте  приложение.");
        textView2.setText(str);


        myBroadcastReceiver = new MyBroadcastReceiver();
        Toast.makeText(getActivity(), "Cигнализация включена!", Toast.LENGTH_LONG).show();

        IntentFilter filter = new IntentFilter();
       filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
       filter.addAction(Intent.ACTION_POWER_CONNECTED);
//////        // Register the receiver using the activity context.
        getActivity().registerReceiver(myBroadcastReceiver, filter);

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
        super.onDestroyView();
        binding = null;
    }
}