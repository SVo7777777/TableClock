package com.example.screenclock.ui.dashboard;

import android.content.Context;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.screenclock.R;
import com.example.screenclock.databinding.FragmentDashboardBinding;

import java.util.Calendar;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    TimePicker alarmTimePicker;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    ToggleButton toggleButton;
    BatteryManager myBatteryManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        alarmTimePicker = root.findViewById(R.id.timePicker);
        toggleButton = root.findViewById(R.id.toggleButton);
        myBatteryManager = (BatteryManager) getActivity().getSystemService(Context.BATTERY_SERVICE);



        alarmManager =(AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        //OnToggleClicked(toggleButton);
        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    public boolean isUSBCharging() {
        return myBatteryManager.isCharging();
    }





//            alarmManager.cancel(pendingIntent);
//            Toast.makeText(getContext(), "ALARM OFF", Toast.LENGTH_SHORT).show();



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}