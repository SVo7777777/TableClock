package com.example.screenclock.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.screenclock.BroadcastReceiver0;

import com.example.screenclock.FileEmpty;
import com.example.screenclock.databinding.FragmentHomeBinding;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdSize;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.common.MobileAds;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private BannerAdView mBannerAd = null;
    BroadcastReceiver0 BroadcastReceiver;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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