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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class StartActivity extends Activity {

	public ListView listview;
	public SQLiteDatabase specialPins;
	public SQLiteDatabase loginStatus;
	public SQLiteDatabase db;
	public String firstname;
	public String studioNumber;
	public boolean stati;
	int counter = 0;
	
	/*Randy Richardson 08/05/2014 START
	 * Instance of LogWrt class and a String used to save the Log*/
	
	LogWrt logWrt = new LogWrt();
	String str;
	
	//Randy Richardson 08/05/2014 END
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		listview = (ListView) findViewById(R.id.somethingUseful);
		checkTable();
		initButtons();
		setListItems();
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","StartActivity has been called");
		str = "StartActivity main has been called\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END

		
	}
	
	@Override//disables back button
	public void onBackPressed() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","StartActivity onBackPressed started \n");
		
		str = "StartActivity onBackPressed started \nStartActivity onBackPressed finished";
		logWrt.appF(str);
		
		Log.d("Debug","StartActivity onBackPressed finished");
		
		//Randy Richardson 08/05/2014 END
	}
	
	public void initButtons(){
		
			//Randy Richardson 08/05/2014 START
		
			Log.d("Debug","StartActivity initButtons started");
			
			str = "StartActivity initButtons started\n";
			logWrt.appF(str);
			
			Button artist = (Button) findViewById(R.id.artistLogin);
			artist.setOnClickListener(onClickListener);
			Button guest = (Button) findViewById(R.id.guestLogin);
			guest.setOnClickListener(onClickListener);
			
			Log.d("Debug","StartActivity initButtons finished");
			
			str = "StartActivity initButtons finished\n";
			logWrt.appF(str);
	
			//Randy Richardson 08/05/2014 END
		
	}
	public void checkTable() {
		//Make sure the table exists when you open the activity
		
		//Open the database
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","StartActivity checkTable started");
		
		str = "StartActivity checkTable started\n";
		logWrt.appF(str);
		
		specialPins = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		specialPins.execSQL("CREATE TABLE IF NOT EXISTS flaggedPins (id INTEGER PRIMARY KEY AUTOINCREMENT, pin INTEGER, status VARCHAR)");
		specialPins.execSQL("CREATE TABLE IF NOT EXISTS StudioInfo(id INTEGER PRIMARY KEY AUTOINCREMENT, pid VARCHAR, firstname VARCHAR, lastname VARCHAR, studio_number VARCHAR, pin VARCHAR);");
		specialPins.close();
		
		//Create the login table
		loginStatus = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		loginStatus.execSQL("CREATE TABLE IF NOT EXISTS LoginTable(id INTEGER PRIMARY KEY AUTOINCREMENT, pin INT, timestamp DATETIME," /*hours LONG,*/+ " isLoggedIn BOOLEAN, pushed INT);");
		loginStatus.close();
		
		Log.d("Debug","StartActivity checkTable finished");
		
		str = "StartActivity checkTable finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
		
	}
	
	public void setListItems () {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","StartActivity setListItems started");
		
		str = "StartActivity setListItems started\n";
		logWrt.appF(str);
		
		List<Map<String, String>> blah = getItems();
		
	    SimpleAdapter adapter2 = new SimpleAdapter(this, blah,
	    			android.R.layout.simple_list_item_2,
                    new String[] {"name", "status"},
                    new int[] {android.R.id.text1,
                               android.R.id.text2});
	    
	    
	    listview.setAdapter(adapter2);
	    
	    Log.d("Debug","StartActivity setListItems finished");
	    
	    str = "StartActivity setListItems finished\n";
		logWrt.appF(str);
	    
	  //Randy Richardson 08/05/2014 END
	}
	
	
	private OnClickListener onClickListener = new OnClickListener() {
		  public void onClick(View v) { 
			
		  //Randy Richardson 08/05/2014 START
				
				Log.d("Debug","StartActivity onCLickListener started");
				
				str = "StartActivity onClickListener started\n";
				logWrt.appF(str);
				
			///registers the buttons with their respective numbers///
		   switch(v.getId()) {
		   case R.id.artistLogin: artistLogin();
		   break;
		   case R.id.guestLogin: guestLogin();
		   break;
		   }

		   		Log.d("Debug","StartActivity onClickListener finished"); 
		   		
		   		str = "StartActivity onCLickListner finished\n";
				logWrt.appF(str);
			
		 //Randy Richardson 08/05/2014 END
			  
	    }
	};

	public void artistLogin() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","StartActivity artistLogin started calls Main_page");
		
		str = "StartActivity artistLogin started calls Main_page\n";
		logWrt.appF(str);
		
		Intent intent = new Intent(this, Main_page.class);
		startActivity(intent);
		
		//Randy Richardson 08/05/2014 END
	}

	
	public List<Map<String, String>> getItems() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","StartActivity getItems started");
		
		str = "StartActivity getItems started\n";
		logWrt.appF(str);
				
		//Open the database
		db = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		loginStatus = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null); 
		
		//define the cursors
		Cursor flaggedPins = db.rawQuery("SELECT * from flaggedPins WHERE status = 'proctor';", null);
		Cursor studioInfo = db.rawQuery("SELECT * from StudioInfo", null);
		Cursor employees = db.rawQuery("SELECT * from flaggedPins WHERE status = 'chashama';", null);
		Cursor logged_in = db.rawQuery("SELECT * from LoginTable", null);
		
		//Define the vars
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		
		
		//move the cursor into position
		logged_in.moveToFirst();
		flaggedPins.moveToFirst();
		studioInfo.moveToFirst();
		employees.moveToFirst();
		
		// For Proctors
		//Log.e("getData", "This many flagged pins " + flaggedPins.getColumnCount());
		if(flaggedPins.getCount() != 0 && studioInfo.getCount() != 0) {
			for(int flagnumcounter = 0; flagnumcounter < flaggedPins.getCount(); flagnumcounter++){
				for(int i = 0; i < studioInfo.getCount(); i ++) {				
					
					if (flaggedPins.getString(flaggedPins.getColumnIndex("pin")).equals(studioInfo.getString(studioInfo.getColumnIndex("pin")))) {
						
						Cursor absolutely = loginStatus.rawQuery("SELECT * from LoginTable WHERE pin = '" + studioInfo.getString(studioInfo.getColumnIndex("pin")) + "' AND isLoggedIn = 'true';", null);
						//absolutely.moveToFirst();
						firstname = studioInfo.getString(studioInfo.getColumnIndex("firstname"));
						studioNumber = studioInfo.getString(studioInfo.getColumnIndex("studio_number"));
						//Log.e("getData", "This is a firstname: " + firstname);
						String state ="";
						
						if(absolutely.getCount() != 0) {
							stati = true;
							state ="IN";
							
						}
						else {
							stati = false;
							state ="OUT";
						}
						
						Map<String, String> datum = new HashMap<String, String>(2);
						datum.put("name", firstname + "( " + studioNumber + ") is " + state );
						datum.put("status", flaggedPins.getString(flaggedPins.getColumnIndex("status")));
						data.add(datum);
						counter++;
						
						studioInfo.moveToFirst();
						flaggedPins.moveToNext();
						if( counter == flaggedPins.getCount()){
							break;
						}
						
					}
					
					else {
						studioInfo.moveToNext();
					}
				}
			}
			
			//For all logged in people		
			//define the cursors
			Cursor login_cursor = db.rawQuery("SELECT * from LoginTable WHERE isLoggedIn = 'true'", null);
			
			//Define the vars
			String firstname;
			String lastname;
			
			
			
			//move the cursor into position
			login_cursor.moveToFirst();
			
			if (login_cursor.getCount() != 0) {
				String state = "IN";
				for (int i=0; i<login_cursor.getCount(); i++){
					//Get the pin from the table
					String pinNum = login_cursor.getString(login_cursor.getColumnIndex("pin"));
					
					//get the relevant data from the tables
					studioInfo = db.rawQuery("SELECT * FROM StudioInfo WHERE pin ='" + pinNum +"';", null);
					studioInfo.moveToFirst();
					studioNumber = studioInfo.getString(studioInfo.getColumnIndex("studio_number"));
					
					//Check to see if the records are found
					if (studioInfo.getCount() > 0) {
						firstname = studioInfo.getString(studioInfo.getColumnIndex("firstname"));
						lastname = studioInfo.getString(studioInfo.getColumnIndex("lastname"));
					} else {
						firstname = "invalid";
						lastname = "invalid";
					}
					
					//Add the info to the data stream
					Map<String, String> datum = new HashMap<String, String>(2);
			        datum.put("name",  firstname + "( " + studioNumber + ") is " + state );
			        datum.put("status", "artist" );
			        data.add(datum);
					
					//Move the cursor to the next item
			        login_cursor.moveToNext();
				}	
			}	
			
			
			
			
			counter = 0;
			//flaggedPins.moveToFirst();
			employees.moveToFirst();	
			studioInfo.moveToFirst();
			if(employees.getCount() != 0 && studioInfo.getCount() != 0) {
				for(int a = 0; a < studioInfo.getCount(); a ++) {				
					
					if (employees.getString(employees.getColumnIndex("pin")).equals(studioInfo.getString(studioInfo.getColumnIndex("pin")))) {
						
						Cursor absolutely = loginStatus.rawQuery("SELECT * from LoginTable WHERE pin = '" + studioInfo.getString(studioInfo.getColumnIndex("pin")) + "' AND isLoggedIn = 'true';", null);
						
						
						//absolutely.moveToFirst();
						firstname = studioInfo.getString(studioInfo.getColumnIndex("firstname"));
						studioNumber = studioInfo.getString(studioInfo.getColumnIndex("studio_number"));
						
						String state ="";
						
						if(absolutely.getCount() != 0) {
							stati = true;
							state ="IN";
							Map<String, String> datum = new HashMap<String, String>(2);
							datum.put("name", firstname + "( " + studioNumber + ") is " + state );
							datum.put("status", employees.getString(employees.getColumnIndex("status")));
							data.add(datum);
						}
						
						
						
						counter++;
						
						studioInfo.moveToFirst();
						employees.moveToNext();
						if( counter == employees.getCount()){
							break;
						}
						
					}
					
					else {
						studioInfo.moveToNext();
					}
					
					
					
				}
			}
			
			
			
			
			
		}

		else {
			//Populate the empty data list so it doesnt error
			Map<String, String> datum = new HashMap<String, String>(2);
	        datum.put("name", "Blank" );
	        datum.put("status", "Blank");
	        data.add(datum);
			
			Toast.makeText(getApplicationContext(), "No Current Proctors or Employees listed", Toast.LENGTH_SHORT).show();
		}
		
		//close the database
		db.close();
		
		Log.d("Debug","StartActivity getItems finished");
		
		str = "StartActivity getItems finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
		
		return data; 
	
		
	}
	
	public void guestLogin() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","StartActivity guestLogin started calls GuestLogin");
		
		str = "StartActivity guestLogin started calls GuestLogin\n";
		logWrt.appF(str);
		
		Intent intent = new Intent(this, GuestLogin.class);
		startActivity(intent);
		
		//Randy Richardson 08/05/2014 END
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

}
