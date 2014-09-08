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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GuestInfo extends Activity {

public static String visName;
public static String address;
public static String purpose;
public static String phone;
public static String artname;
public static String email;
public static String SNF;

/*Randy Richardson 08/05/2014 START
 * Instance of LogWrt class and a String used to save the Log*/

LogWrt logWrt = new LogWrt();
String str;

//Randy Richardson 08/05/2014 END



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guest_info);
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","GuestInfo has been called");
		
		str = "GuestInfo has been called\n";
		logWrt.appF(str);
		
		initButtons();
		
		Log.d("Debug","GuestInfo finished");
		
		str = "GuestInfo finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
	}

	public void initButtons(){
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","GuestInfo initButtons started");
		
		str = "GuestInfo initButtons started\n";
		logWrt.appF(str);
		
		TextView visNameField = (TextView) findViewById(R.id.guest_visNameField);
		
		Button backToMain = (Button) findViewById(R.id.backToMain);
		backToMain.setOnClickListener(onClickListener);
		Button forward = (Button) findViewById(R.id.proceed);
		forward.setOnClickListener(onClickListener);

		visNameField.requestFocus();
		
		Log.d("Debug","GuestInfo initButtons finished");
		
		str = "GuestInfo initButtons finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
	
}

private OnClickListener onClickListener = new OnClickListener() {
	
	  public void onClick(View v) { 
		
		///registers the buttons with their respective numbers///
	   switch(v.getId()) {
	   case R.id.backToMain: goBack();
	   break;
	   case R.id.proceed: goForward();
	   break;
	   }
	   ///////////////////////////////////////////////////////		   
    }
};

	public void goForward() {
		
		//Randy Richardson 08/05/2014
		
		Log.d("Debug","GuestInfo goForward started");
		
		str = "GuestInfo goForward started\n";
		logWrt.appF(str);
		
		TextView visNameField = (TextView) findViewById(R.id.guest_visNameField);
		TextView visAddressField = (TextView) findViewById(R.id.guest_visAddressField);
		TextView artNameField = (TextView) findViewById(R.id.guest_artNameField);
		TextView purposeField = (TextView) findViewById(R.id.guest_purposeField);
		TextView phoneField = (TextView) findViewById(R.id.guest_phoneField);
		TextView emailField = (TextView) findViewById(R.id.guest_emailField);
		TextView SNField = (TextView) findViewById(R.id.guest_SNField);
		
		visName = visNameField.getText().toString();
		address =  visAddressField.getText().toString();
		purpose =  purposeField.getText().toString();
		phone =  phoneField.getText().toString();
		artname =  artNameField.getText().toString();
		email =  emailField.getText().toString();
	    SNF =  SNField.getText().toString();
		
		if (!filled_out(artNameField, emailField, SNField)) {
			Toast.makeText(getApplicationContext(), "Please fill out Artist Name, Email, and Studio Number to continue", Toast.LENGTH_LONG).show();
			visNameField.requestFocus();
		} else if (!isEmailValid(email =  emailField.getText().toString())){
			Toast.makeText(getApplicationContext(), "Please fill out correct email", Toast.LENGTH_LONG).show();
			emailField.requestFocus();
		} else if (!(isStudioValid(SNF =  SNField.getText().toString()))) {
			Toast.makeText(getApplicationContext(), "Please fill out correct studio number Ex: B42", Toast.LENGTH_LONG).show();
			SNField.requestFocus();
		} else {	

			
			
			Intent intent = new Intent(this, Confirm.class);
			intent.putExtra("visName", visName);
			intent.putExtra("address", address);
			intent.putExtra("purpose", purpose);
			intent.putExtra("phone", phone);
			intent.putExtra("artname", artname);
			intent.putExtra("email", email);
			intent.putExtra("SNF", SNF);
			
			startActivity(intent);
		}

		
		Log.d("Debug","GuestInfo goforward finished");
		
		str = "GuestInfo goForward finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
		
	}
	
	public Boolean isStudioValid(String studio_num){
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","GuestInfo isStudioValid started");
		
		str = "GuestInfo isStudioValid started\n";
		logWrt.appF(str);
		
		String char1 = studio_num.substring(0, 1);
		String char2 = studio_num.substring(1, 2);
		String char3 = studio_num.substring(1, 2);
		boolean isChar = char1.matches("[a-zA-z]{1}");
		boolean isDigit = char2.matches("\\d{1}");
		boolean isDigit2 = char3.matches("\\d{1}");
		
		if (studio_num.length() == 3 && isChar == true && isDigit == true && isDigit2 == true ) {
			return true;
		} 
		else {

			Log.d("Debug","GuestInfo isStudioValid finished");
			
			str = "GuestInfo isStudioValid finished\n";
			logWrt.appF(str);
			
			return false;
		}
		
		//Randy Richardson 08/05/2014 END
	}
	
	public Boolean filled_out(TextView artNameField, TextView emailField, TextView SNField  ) {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","GuestInfo filled_out started");
		
		str = "GuestInfo filled_out started\n";
		logWrt.appF(str);
		
		if( !isEmpty(artNameField)  && !isEmpty(emailField) && !isEmpty(SNField)){
			return true;
		} 
		else {
			
			Log.d("Debug","GuestInfo filled_out finished");
			
			str = "GuestInfo filled_out finished\n";
			logWrt.appF(str);
			
			return false;
		}
		
		//Randy Richardson 08/05/2014 END
		
	}
	
	public static boolean isEmailValid(String email) {
		
		//Randy Richardson 08/05/2014 START
		//Unable to Log this function in file because of static modifier
		
		Log.d("Debug","GuestInfo isEmailValid started");
				
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	   
		Log.d("Debug","GuestInfo isEmailValid finished");
		
		//Randy Richardson 08/05/2014 END
		
	    return isValid;
	}
	
	
	public void goBack() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","GuestInfo goBack started calls StartActivity");
		
		str = "GuestInfo goBack started calls StartActivity\n";
		logWrt.appF(str);
		
		Intent intent = new Intent(this, StartActivity.class);
		startActivity(intent);
		
		//Randy Richardson 08/05/2014 END
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guest_info, menu);
		return true;
	}
	
	private boolean isEmpty(TextView etText) {
		
		//Randy Richardson 08/05/2014
		
		Log.d("Debug","GuestInfo isEmpty started");
		
		str = "GuestInfo isEmpty started\n";
		logWrt.appF(str);
		
		if (etText.getText().toString().trim().length() > 0) {
			
			Log.d("Debug","GuestInfo isEmpty finished ");
			
			str = "GuestInfo isEmpty finished\n";
			logWrt.appF(str);
			
			return false;
		} 
		else {
			
			Log.d("Debug","GuestInfo isEmpty finished ");
			
			str = "GuestInfo isEmpty finished\n";
			logWrt.appF(str);
			
			return true;
		}
	}

	//Randy Richardson 08/05/2014 END
}
