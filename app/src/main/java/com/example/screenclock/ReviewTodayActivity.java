package com.example.screenclock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReviewTodayActivity extends Activity {
    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;

    LinearLayout ln;
    TextView ev;
    TextView day;
    String wd;
    Calendar calendar = Calendar.getInstance();
    int current_month =  calendar.get(Calendar.MONTH);

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.x = -20;
        params.height = 100;
        params.width = 850;
        params.y = -10;
        this.getWindow().setAttributes(params);
        setContentView(R.layout.activity_review_today);

        TextView event = findViewById(R.id.editTextTextMultiLine);
        //Button close = findViewById(R.id.close);

        StringBuilder str = new StringBuilder("Сигнализация включена. \nЧтобы сигнализация сработала, отключите устройство от питания.\nЧтобы отключить сигнал, нажмите на кнопку &quot;выключить сигнал&quot; или подключите устройство к питанию или перейдите на другую вкладку.\nЧтобы отключить сигнализацию выйдите из вкладки или закройте  приложение.");
        event.setText(str);





    }
}
