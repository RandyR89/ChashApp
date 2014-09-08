package com.example.signin;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;





public class Main extends Activity  {

	//placeholders for numberpicker values
	public static int val1;//first value of number picker
	public static int val2;//second value of number picker
	
	public int i = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initButtons();
        setContentView(R.layout.activity_main);
      
        
    	

    }

    public void signIn() {//proceeds to personal code page when button is clicked
    		
    		Intent intent = new Intent(this, SignMeIn.class);
    		startActivity(intent);
    		
    	
    }
    public void initButtons(){
		//////////////////buttons/////////////////////////
    	
    	Button main_btn1 = (Button) findViewById(R.id.main_button1);
    	main_btn1.setOnClickListener(onClickListener);
/*		Button one = (Button) findViewById(R.id.main_button1);//the rest are all key pad buttons
		one.setOnClickListener(onClickListener);
		Button two = (Button) findViewById(R.id.main_button2);
		two.setOnClickListener(onClickListener);
		Button three = (Button) findViewById(R.id.main_button3);
		three.setOnClickListener(onClickListener);
		Button four = (Button) findViewById(R.id.main_button4);
		four.setOnClickListener(onClickListener);
		Button five = (Button) findViewById(R.id.main_button5);
		five.setOnClickListener(onClickListener);
		Button six = (Button) findViewById(R.id.main_button6);
		six.setOnClickListener(onClickListener);
		Button seven = (Button) findViewById(R.id.main_button7);
		seven.setOnClickListener(onClickListener);
		Button eight = (Button) findViewById(R.id.main_button8);
		eight.setOnClickListener(onClickListener);
		Button nine = (Button) findViewById(R.id.main_button9);
		nine.setOnClickListener(onClickListener);
		Button clear = (Button) findViewById(R.id.main_button10);
		clear.setOnClickListener(onClickListener);
		Button zero = (Button) findViewById(R.id.main_button0);
		zero.setOnClickListener(onClickListener);
		///////////////////////////////////////////////////
		
		///////////////////text field////////////////////////
		EditText editText = (EditText)findViewById(R.id.main_sn);
		editText.setText("Studio Number: ");*/
		
		////////////////////////////////////////////////////
	}

    private OnClickListener onClickListener = new OnClickListener() {
		  public void onClick(View v) { 
			
			///registers the buttons with their respective numbers///
		   switch(v.getId()) {
		  
		   case R.id.main_button1: stars(1);
		   break;
		   case R.id.main_button2: stars(2);
		   break;
		   case R.id.main_button3: stars(3);
		   break;
		   case R.id.main_button4: stars(4);
		   break;
		   case R.id.main_button5: stars(5);
		   break;
		   case R.id.main_button6: stars(6);
		   break;
		   case R.id.main_button7: stars(7);
		   break;
		   case R.id.main_button8: stars(8);
		   break;
		   case R.id.main_button9: stars(9);
		   break;
		   case R.id.main_button10: stars(0);
		   break;
		   case R.id.main_button0: stars(-1);//clears field
		   break;
		  case R.id.goForward:stars (-2);
		   break;
		   //case R.id.tester: tester();
		   }
		   ///////////////////////////////////////////////////////		   
	    }
		
	};
	

public void stars(int a) {
	EditText editText = (EditText)findViewById(R.id.main_sn);
	if (a == -1) {
		i = 0;
	}
	
	else if (a == -2) {
		if (i == 2) {
			signIn();
		}
	}
	
	else if(i == 0) {
		val1 = a;
		i++;
	}
	
	
	else if (i == 1) {
		val2 = a;
		i++;
	}
	
	if(i != 0) {
	editText.setText(Integer.toString(val1) + val2);
	}
	else {
		editText.setText("Studio Number");
	}
}
	
    
    public void setupAlarm(int seconds) {
  	  //Create an offset from the current time in which the alarm will go off.
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.SECOND, 5);
      boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("com.example.Main"), PendingIntent.FLAG_NO_CREATE) != null);
      //Create a new PendingIntent and add it to the AlarmManager
      Intent intent = new Intent(this, RepeatingAlarm.class);
      PendingIntent pendingIntent = PendingIntent.getActivity(this,
          12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
      AlarmManager am = 
          (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
      am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000*seconds,
              pendingIntent);
      Log.d("alarm", "set alarm");
  }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    
}
