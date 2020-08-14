package com.kim9212.ex63alrammanagertest;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ViewUtils;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //알람매니저 객체 소환!!
        alarmManager= (AlarmManager)getSystemService(Context.ALARM_SERVICE);

    }

    public void clickBtn(View view) {
        //10초 후 실행할 Component(Activity, Service, BR)를 지정

        //먼저 알람에 설정할 PendingIntent객체 생성
        Intent intent= new Intent(this, AlarmActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //M버전 부터 Doz(낮잠)모드가 있어서 깨우고 알람을 실행하도록..
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000, pendingIntent);
        }else{
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000, pendingIntent);
        }
    }

    public void clickBtn2(View view) {
        //10초 후에 알람 울리고... 15초후에 재 알람 ( 매번 토스트 보이도록 )
        //재알람 기능이 없음...없어서.. 행운의 편지..기법 사용

        //10초후에 실행할 작업
        Intent intent= new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(this, 11, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000, pendingIntent);
        }else{
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000, pendingIntent);
        }

    }

    public void clickBtn3(View view) {
        if(alarmManager!=null){
            Intent intent= new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent= PendingIntent.getBroadcast(this, 11, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(pendingIntent);
        }
    }

    //멤버변수
    int Year, Month, Day;
    int Hour, Min;

    public void clickBtn4(View view) {
        //오늘 날짜와 시간을 가진 객체 생성
        GregorianCalendar calendar= new GregorianCalendar();
        int y= calendar.get(Calendar.YEAR);   //2020
        int m= calendar.get(Calendar.MONTH);  //6월 (5)
        int d= calendar.get(Calendar.DAY_OF_MONTH); //8일 - 8

        //특정 날짜 지정할 수 있도록 다이얼로그 보이도록
        DatePickerDialog dialog= new DatePickerDialog(this, dateSetListener, y, m, d);
        dialog.show();
    }

    //날짜선택 리스너 객체 생성
    DatePickerDialog.OnDateSetListener dateSetListener= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            //다이얼로그에서 날짜를 선택하면 선택된 값을 파라미터로 전달받음
            Year= year;
            Month= month;
            Day= dayOfMonth;

            //시간선택 다이얼로그 보이기
            GregorianCalendar calendar= new GregorianCalendar();
            int h= calendar.get(Calendar.HOUR_OF_DAY);
            int m= calendar.get(Calendar.MINUTE);
            TimePickerDialog dialog= new TimePickerDialog(MainActivity.this, timeSetListener, h, m, true);
            dialog.show();
        }
    };


    //시간 선택리스너 객체 생성
    TimePickerDialog.OnTimeSetListener timeSetListener= new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Hour= hourOfDay;
            Min= minute;

            //설정한 시간을 기준으로 알람 지정
            GregorianCalendar calendar= new GregorianCalendar(Year, Month, Day, Hour, Min);

            Intent intent= new Intent(MainActivity.this, AlarmActivity.class);
            PendingIntent pendingIntent= PendingIntent.getActivity(MainActivity.this, 15, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }else{
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }

        }
    };


}
