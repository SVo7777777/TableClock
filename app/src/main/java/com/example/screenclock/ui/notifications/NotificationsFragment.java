package com.example.screenclock.ui.notifications;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.screenclock.MyBroadcastReceiver;
import com.example.screenclock.R;
import com.example.screenclock.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    TimePicker timePicker;
    Button button2;
    public TextView textView2;
     BroadcastReceiver batteryLevelReceiver;
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

       myBroadcastReceiver = new MyBroadcastReceiver();
        Toast.makeText(getActivity(), "Cигнализация включена!", Toast.LENGTH_LONG).show();

        IntentFilter filter = new IntentFilter();
       filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
       filter.addAction(Intent.ACTION_POWER_CONNECTED);
//////        // Register the receiver using the activity context.
        getActivity().registerReceiver(myBroadcastReceiver, filter);

        Vibrator vib = myBroadcastReceiver.vibrator;

        Ringtone rin =myBroadcastReceiver.ringtone;

        button2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
            @Override
            public void onClick(View v) {
                if ((rin != null) && (vib != null)){
                    vib.cancel();
                    rin.stop();
                    System.out.println("here");
                }
            }
        });
//
//        button2.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
//            @Override
//            public void onClick(View v) {
//                onStop();
//
//            }
//        });




        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }




   // @Override
//    public void onStart() {
//        super.onStart();
//        if (myBroadcastReceiver == null) {
//            IntentFilter filter = new IntentFilter();
//            filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
//            filter.addAction(Intent.ACTION_POWER_CONNECTED);
//            // Register the receiver using the activity context.
//            getActivity().registerReceiver(myBroadcastReceiver, filter);
//
//
//            System.out.println("here");
//        }
//
//
//        Toast.makeText(getActivity(), "sratrt receiver", Toast.LENGTH_LONG).show();
//        System.out.println("sratrt");
//    }

//    @Override
//    public void onStop() {
//        if (myBroadcastReceiver != null) {
//            getActivity().unregisterReceiver(myBroadcastReceiver);
//            myBroadcastReceiver = null;
//            System.out.println("here");
//        }
//        super.onStop();
//
//    }



    @Override
    public void onDestroyView() {
        if (myBroadcastReceiver != null) {
            getActivity().unregisterReceiver(myBroadcastReceiver);
            myBroadcastReceiver = null;
            Toast.makeText(getActivity(), "Cигнализация выключена!", Toast.LENGTH_LONG).show();

        }
        super.onDestroyView();
        binding = null;
    }
}