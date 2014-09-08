/*Sign In Log of changes
 * 
 * =================================================================================================
 * Programmer: Randy Richardson
 * Date: 08/05/2014
 * 
 * Note of Changes:  I have added a chain of debugging statements within each major call to a method
 * 						so the applications patterns of success and failures can be tracked.
 * =================================================================================================
*/


package com.example.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Main_page extends Activity {
	public static String val1 = "";//first value of number picker
	public static String val2 = "";//second value of number picker
	public int i = 0;

	/*Randy Richardson 08/05/2014 START
	 * Instance of LogWrt class and a String used to save the Log*/

	LogWrt logWrt = new LogWrt();
	String str;

	//Randy Richardson 08/05/2014 END
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Main_page has been called");
				
		str = "Main_page has been called\n";
		logWrt.appF(str);
				
		//set the values to blank
		val1 = "";
		val2 = "";
		initButtons();
		
		Log.d("Debug","Main_page finished");
		
		str = "Main_page has finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
	}

	
	public void initButtons(){
		//////////////////buttons/////////////////////////

		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Main_page initButtons started");
		
		str = "Main_page initButtons started\n";
		logWrt.appF(str);
		
		Button one = (Button) findViewById(R.id.main_button1);//the rest are all key pad buttons
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

		Button signin = (Button) findViewById(R.id.signin);
		signin.setOnClickListener(onClickListener);
		///////////////////////////////////////////////////
		
		///////////////////text field////////////////////////
		TextView editText = (TextView)findViewById(R.id.main_sn);
		editText.setText(" ");
		
		Log.d("Debug","Main_page initButtons finished");
		
		str = "Main_page initButtons finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
		
	}

    private OnClickListener onClickListener = new OnClickListener() {
		  public void onClick(View v) { 
			
			//Randy Richardson 08/05/2014 START
				
			Log.d("Debug","Main_page onClickListener started");
			  
			str = "Main_page onClickListener started\n";
			logWrt.appF(str);
			
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
		   case R.id.main_button0: stars(0);
		   break;
		   case R.id.main_button10: stars(-1);//clears field
		   break;
		  case R.id.signin:stars (-2);
		   break;
		   //case R.id.tester: tester();
		   }
		   ///////////////////////////////////////////////////////	
		   
		   Log.d("Debug","Main_page onClickListener finished");
		   
		   str = "Main_page onClickListener finished\n";
			logWrt.appF(str);
		   
		   //Randy Richardson 08/05/2014 END
	    }
		
	};
	
	 public void signIn() {
		 
		 //proceeds to personal code page when button is clicked
 		
		//Randy Richardson 08/05/2014 START
			
		Log.d("Debug","Main_page signIn started calls SignMeIn");
		 
		str = "Main_page signIn started calls SignMeIn\n";
		logWrt.appF(str);
		
 		Intent intent = new Intent(this, SignMeIn.class);
 		startActivity(intent);
 		
 		//Randy Richardson 08/05/2014 END
 }
	 
	public void stars(int a) {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Main_page stars started");
		
		str = "Main_page stars started\n";
		logWrt.appF(str);
		
		TextView editText = (TextView)findViewById(R.id.main_sn);
		if (a == -1) {
			i = 0;
			val1 = "";
			val2 = "";
			
		}
		
		else if (a == -2) {
			if (i == 2) {
				signIn();
			}
		}
		
		else if(i == 0) {
			val1 = Integer.toString(a);
			i++;
		}
		
		
		else if (i == 1) {
			val2 = Integer.toString(a);
			i++;
		}
		
		if(i != 0) {
		editText.setText(val1 + val2);
		}
		else {
			editText.setText("");
		}
		
		Log.d("Debug","Main_page stars finished");
		
		str = "Main_page stars finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_page, menu);
		return true;
	}

}
