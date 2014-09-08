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

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Confirm extends Activity {
	
	public static String visName;
	public static String address;
	public static String purpose;
	public static String phone;
	public static String artname;
	public static String email;
	public static String SNF;

public static boolean guest = false;

/*Randy Richardson 08/05/2014 START
 * Instance of LogWrt class and a String used to save the Log*/

LogWrt logWrt = new LogWrt();
String str;

//Randy Richardson 08/05/2014 END

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm);
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Confirm has been called");
		
		str = "Confirm has been called\n";
		logWrt.appF(str);
		
		//get variables from intent, if they exist
		Intent intent = getIntent();
		visName = intent.getStringExtra("visName");
		address = intent.getStringExtra("address");
		purpose = intent.getStringExtra("purpose");
		phone = intent.getStringExtra("phone");
		artname = intent.getStringExtra("artname");
		email = intent.getStringExtra("email");
		SNF = intent.getStringExtra("SNF");
		
		checkVars();
		
		TextView visNameField = (TextView) findViewById(R.id.confirm_visName);
		TextView visAddressField = (TextView) findViewById(R.id.confirm_visAdd);
		TextView artNameField = (TextView) findViewById(R.id.confirm_artName);
		TextView purposeField = (TextView) findViewById(R.id.confirm_purp);
		TextView phoneField = (TextView) findViewById(R.id.confirm_phone);
		TextView emailField = (TextView) findViewById(R.id.confirm_email);
		TextView SNField = (TextView) findViewById(R.id.confirm_studNum);
		
		visNameField.setText(visName.toString());
		visAddressField.setText(address.toString());
		artNameField.setText(artname.toString());
		purposeField.setText(purpose.toString());
		phoneField.setText(phone.toString());
		emailField.setText(email.toString());
		SNField.setText(SNF.toString());
		
		initButtons();
		
		Log.d("Debug","Confirm has been finished");
		
		str = "Confirm finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
	}
	
private void checkVars() {
	
		//Randy Richardson 08/05/2014 START
	
		Log.d("Debug","Confirm checkVars started");
		
		str = "Confirm checkVars started\n";
		logWrt.appF(str);
		
		if (visName.equals("")) {
			visName = "N/A";
		}
		if (address.equals("")) {
			address = "N/A";
		}
		if (purpose.equals("")) {
			purpose = "N/A";
		}
		if (phone.equals("")) {
			phone = "N/A";
		}
		if (email.equals("")) {
			email = "N/A";
		}
		
		Log.d("Debug","Confirm has been called");
		
		str = "Confirm checkVars finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
				
	}

public void initButtons(){
		
		//Randy Richardson 08/05/2014 START
	
		Log.d("Debug","Confirm initButtons started");
		
		str = "Confirm initButtons started\n";
		logWrt.appF(str);
		
		Button reEnter = (Button) findViewById(R.id.reEnter);
		reEnter.setOnClickListener(onClickListener);
		Button confirm = (Button) findViewById(R.id.confirm);
		confirm.setOnClickListener(onClickListener);
				
		Log.d("Debug","Confirm initButtons finished");
		
		str = "Confirm initButtons finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
	
}

private OnClickListener onClickListener = new OnClickListener() {
	  public void onClick(View v) { 
		
		///registers the buttons with their respective numbers///
	   switch(v.getId()) {
	   case R.id.reEnter: goBack();
	   break;
	   case R.id.confirm: goForward();//next page with login to sqlite
	   break;
	   }
	   ///////////////////////////////////////////////////////		   
    }
};

public void goForward() {
	
	//Randy Richardson 08/05/2014 START
	
	Log.d("Debug","Confirm goForward started calls Welcome");
	
	str = "Confirm goForward started calls Welcome\n";
	logWrt.appF(str);
	
	//make login table page here
	guest = true;
	
	new push().execute();
	Intent intent = new Intent(this, Welcome.class);
	intent.putExtra("guestCheck", guest);
	
	startActivity(intent);

	//Randy Richardson 08/05/2014 END
}

public void goBack() {
	
	//Randy Richardson 08/05/2014 START
	
	Log.d("Debug","Confirm goBack started calls GuestInfo");
	
	str = "Confirm goBack started calls GuestInfo\n";
	logWrt.appF(str);
	
	//Toast.makeText(getApplicationContext(), "activated goback", Toast.LENGTH_LONG).show();
	//System.out.println("ASFJDKAJSKDFJ");
	Intent intent = new Intent(this, GuestInfo.class);
	
	startActivity(intent);
	
	//Randy Richardson 08/05/2014 END
}

public void pushData() {
	
	//Randy Richardson 08/05/2014 START
	
	Log.d("Debug","Confirm pushData started");
	
	str = "Confirm pushData started\n";
	logWrt.appF(str);
	
    SQLiteDatabase updatedb = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
    updatedb.execSQL("CREATE TABLE IF NOT EXISTS GuestUpdate(visName VARCHAR, visAddress VARCHAR, artistName VARCHAR, purpose VARCHAR, phone VARCHAR, email VARCHAR, studioNumber INTEGER, pushed VARCHAR);");
  
  
    
    updatedb.execSQL("INSERT INTO GuestUpdate VALUES('" + visName + "','" + address + "','" + artname + "','" + purpose + "','" + phone + "','" + email + "','" + SNF + "', '0');");
	updatedb.close();
	
	Log.d("Debug","Confirm pushData finished");
	
	str = "Confirm pushData finished\n";
	logWrt.appF(str);
	
	//Randy Richardson 08/05/2014 END
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.confirm, menu);
		return true;
	}
	
	class push extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			
			//Randy Richardson 08/05/2014 START
			
			Log.d("Debug","Confirm push called doInBackground started");
			
			str = "Confirm push called doInBackground started\n";
			logWrt.appF(str);
			
			System.out.println("pushData reached");
			pushData();
			System.out.println("pushData ran");
			
			Log.d("Debug","Confirm push called doInBackground finished");
			
			str = "Confirm push called doInBackground finished\n";
			logWrt.appF(str);
			
			//Randy Richardson 08/05/2014 END
			
			return null;
		} 
		
		
		
	}

	//Randy Richardson 08/05/2014 END
	
}



