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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignMeIn extends Activity {
	
	//Define all current variables
	public Calendar cal = Calendar.getInstance();
	
	public int hours;
	public int minutes;
	public int count = 0;
	
	public SQLiteDatabase db;
	public SQLiteDatabase login;
	public SQLiteDatabase logout;
	public SQLiteDatabase last_update_db;
	public SQLiteDatabase systemVars;
	
	public static String nameOfLogin;
	public String numberToCheckPIN ="";
	public static String pinCall;
	public static String snWithLetter;
	public static String StudioLocation;
	
	public static boolean offline = true;
	public static boolean incoming;	
	static int[] number = new int[5];//5 digit input for personal code
	
	//studio values from previous activity
	private int one = Integer.parseInt(Main_page.val1);//number picker 1 value
	private int two = Integer.parseInt(Main_page.val2);//number picker 2 value
	
	int counter = 0;
	int i = 0;//index for random array below
	int gasdf = 0;
	int length = 1;
	
	Toast mToast;	
	String a;//^^
	InputStream isr = null;
	String result = "";
	String studioNumber = Integer.toString(one) + Integer.toString(two);
	String basdf = "";
	String[] flags = new String[length];
	RepeatingAlarm  alarm;

	/*Randy Richardson 08/05/2014 START
	 * Instance of LogWrt class and a String used to save the Log*/

	LogWrt logWrt = new LogWrt();
	String str;

	//Randy Richardson 08/05/2014 END
	
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_me_in);
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","SignMeIn has been called");
		
		str = "SignMeIn has been called\n";
		logWrt.appF(str);
		
		//setup the buttons for the activity
		initButtons();
		
		
		//run the task that gets the data from the internet
		new GetDataTask().execute();
		//run and update the tables to create if not existing
		new UpdateTables().execute();	
		//Sync the login table with the server
		//new SyncLoginTask().execute();
		
		Log.d("Debug","SignMeIn finished");
		
		str = "SignMeIn has been finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
		
	}
	
	public void getLocation() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","SignMeIn getLocation started");
		
		str = "SignMeIn getLocation started\n";
		logWrt.appF(str);
		
		//Grab the location to store from the text box
		String location = "";
		EditText locationToStore = (EditText)findViewById(R.id.ap_numberToChange);
	
		//open the vars db
		systemVars = openOrCreateDatabase("System", MODE_PRIVATE, null);
		
		//Get the vars
		Cursor sysVars = systemVars.rawQuery("SELECT * from SystemVars", null);
		sysVars.moveToFirst();
		
		
		//Set the global variable
		StudioLocation = sysVars.getString(sysVars.getColumnIndex("location"));
		
		if (StudioLocation.equals("unset")){
			Toast.makeText(this.getApplicationContext(), "location not set, login will not work", Toast.LENGTH_SHORT).show();
		}
		
		systemVars.close();
		
		Log.d("Debug","SignMeIn getLocation finished");
		
		str = "SignMeIn getLocation finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
	}
	
	/*(public void startRepeatingTimer(View view) {
		
		     Context context = this.getApplicationContext();
		
		     if(alarm != null){
		
		     // alarm.setAlarm(context);
		
		     }
		     else{
		      
		    	 Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
		
		     }
		
		    }*/

	public void initButtons(){
		

		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","SignMeIn initButtons started");
		
		str = "SignMeIn initButtons started\n";
		logWrt.appF(str);
		
		//////////////////buttons/////////////////////////
		//Button tester = (Button) findViewById(R.id.tester);
		//tester.setOnClickListener(onClickListener);
		Button proceed = (Button) findViewById(R.id.goForward);//proceed button
		proceed.setOnClickListener(onClickListener);
		Button button = (Button) findViewById(R.id.testback);//back button
		button.setOnClickListener(onClickListener);
		Button one = (Button) findViewById(R.id.visitorButton);//the rest are all key pad buttons
		one.setOnClickListener(onClickListener);
		Button two = (Button) findViewById(R.id.artistLogin);
		two.setOnClickListener(onClickListener);
		Button three = (Button) findViewById(R.id.ap_quit);
		three.setOnClickListener(onClickListener);
		Button four = (Button) findViewById(R.id.button4);
		four.setOnClickListener(onClickListener);
		Button five = (Button) findViewById(R.id.button5);
		five.setOnClickListener(onClickListener);
		Button six = (Button) findViewById(R.id.button6);
		six.setOnClickListener(onClickListener);
		Button seven = (Button) findViewById(R.id.button7);
		seven.setOnClickListener(onClickListener);
		Button eight = (Button) findViewById(R.id.button8);
		eight.setOnClickListener(onClickListener);
		Button nine = (Button) findViewById(R.id.button9);
		nine.setOnClickListener(onClickListener);
		Button clear = (Button) findViewById(R.id.button10);
		clear.setOnClickListener(onClickListener);
		Button zero = (Button) findViewById(R.id.button0);
		zero.setOnClickListener(onClickListener);
		///////////////////////////////////////////////////
		
		///////////////////text field////////////////////////
		EditText editText = (EditText)findViewById(R.id.snf);
		editText.setText("Studio Number: "+ studioNumber);
		editText.setInputType(InputType.TYPE_NULL);
		EditText editText2 = (EditText) findViewById(R.id.visAddressField);
		editText2.setInputType(InputType.TYPE_NULL);
		////////////////////////////////////////////////////
		
		Log.d("Debug","SignMeIn initButtons finished");
		
		str = "SignMeIn initButtons finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
	}
	
	public void Invalid() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","SignMeIn Invalid started calls Main_page");
		
		str = "SignMeIn Invalid started calls Main_page\n";
		logWrt.appF(str);
		
		Intent intent = new Intent(this, Main_page.class);
		startActivity(intent);
	    Toast.makeText(getApplicationContext(), "invalid pin/studio combination", Toast.LENGTH_LONG).show();
	    
	    //Randy Richardson 08/05/2014 END
	}
	
	public String getLastTwo(String StudioNumberWithoutFirstLetter) {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","SignMeIn getLastTwo started");
		
		str = "SignMeIn getLastTwo started\n";
		logWrt.appF(str);
		
		char[] c = StudioNumberWithoutFirstLetter.toCharArray();
		String s = "";
		for(int i = 1; i < 3; i++ ){
			s = s +c[i];
		}
		
		Log.d("Debug","SignMeIn getLastTwo finished");
		
		str = "SignMeIn getLastTwo finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
		
		return s;
	}
		
	public void getData() throws IllegalStateException, IOException{
		
				//Randy Richardson 08/05/2014 START
		
				Log.d("Debug","SignMeIn getData started");
		    
				str = "SignMeIn getData started\n";
				logWrt.appF(str);
				
				String result = "";
				InputStream isr = null;
				HttpResponse response = null;
				
				//setup the http request for getting the php data
				//test version is 10.242.11.143, production is php.chashama.info
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost httppost = new HttpPost("http://php.chashama.info");
				
			
				try {
					
					//Add the authentication to the http request
					List nvps = new ArrayList();
					nvps.add(new BasicNameValuePair("username", "kario"));
					nvps.add(new BasicNameValuePair("password", "password"));	
					UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
					
					//Add the encoded variables to the post request
					httppost.setEntity(p_entity);
				
					//Get the resonse from the http request
					response = client.execute(httppost);
				
			
				} catch (Exception e){
					Log.e("getData", "Error: Login Failed: "+e.toString());
				}
				
				
				//get the content returned in the response				
				HttpEntity entity = response.getEntity();
			    isr = entity.getContent();

			    try{
			    	//Attempt to convert the content to readable string
			    	BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"iso-8859-1"),8);
			        StringBuilder sb = new StringBuilder();
			        String line = null;
			        while ((line = reader.readLine()) != null) {
			        	sb.append(line + "\n");
			        }
			        isr.close();
			        result=sb.toString();
			    }
			    catch(Exception e){
			        Log.e("getData", "Error: converting result: "+e.toString());
			    }

		    	try{           
		    		//Drop the existing table for studioinfo if it exists
		            db = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		            db.execSQL("DROP TABLE IF EXISTS StudioInfo;");
		            db.close();
		            
		            //Recreate the studio Info table from new data
		            db = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		            db.execSQL("CREATE TABLE StudioInfo(id INTEGER PRIMARY KEY AUTOINCREMENT, pid VARCHAR, firstname VARCHAR, lastname VARCHAR, studio_number VARCHAR, pin VARCHAR);");
   
			       	//Convert the result into a JSON object and extract the artist data from the object
		            JSONObject jobj = new JSONObject(result);
			       	JSONArray subArray = jobj.getJSONArray("artists");
			       	JSONObject subSubArray;
			       	
		       	    if(jobj.getInt("success") == (1)) {
		       	    	//If the object indicates success in pulling data from the MySQL table
		       	    	Log.d("getData", "JSON Object indicates success in acquiring data from the tables");
		       	    	for(int i = 0; i<subArray.length(); i++){ 
		       	    		//Run a loop for every entry in the artists tables
		       	    	
		       	    		//Get the current artist data row
		       	    		subSubArray = subArray.getJSONObject(i);
		       	    		
		       	    		//Create the new entry in the StudioInfo table
		       	    		db.execSQL("INSERT INTO StudioInfo VALUES" + 
		       	    				"(" +  "NULL, " +  "'" + subSubArray.getInt("pid".toString())+ "', " + 
		       	    				"'" + subSubArray.getString("firstname")+ "', "+ 
		       	    				"'" + subSubArray.getString("lastname") + "', " + 
		       	    				"'" + subSubArray.getString("studio_number") + "', " + 
		       	    				"'" + subSubArray.getString("pin") + "');");	  
		       	    	} 
		       		Log.d("getData", "getData ran successfully");
		       	  }
		     	  else {
		     		 //Throw an error if the php indicates an unsuccessful attempt to pull data
		     		 Log.e("getData", "Error in PHP script");
		     		  
		     	  }
		       	    
		       	  //Close out the databases
		       	  db.close();
		    	}
		    	catch(Exception e){
		    		//throw an error if you cant connect
		            Log.e("getData", "Error in http connection: "+e.toString());
		    	}
		    	
		    	Log.d("Debug","SignMeIn getData finished");
		    	
		    	str = "SignMeIn getData finished\n";
				logWrt.appF(str);
		    	
		    	//Randy Richardson 08/05/2014 END
	
		}
	private OnClickListener onClickListener = new OnClickListener() {
		  public void onClick(View v) { 
			  
			//Randy Richardson 08/05/2014 START
				
			Log.d("Debug","SignMeIn onClickListener started");
			
			str = "SignMeIn onClickListener started\n";
			logWrt.appF(str);
			
			///registers the buttons with their respective numbers///
		   switch(v.getId()) {
		   case R.id.testback: goBack(); 
		   break;
		   case R.id.visitorButton: stars(1);
		   break;
		   case R.id.artistLogin: stars(2);
		   break;
		   case R.id.ap_quit: stars(3);
		   break;
		   case R.id.button4: stars(4);
		   break;
		   case R.id.button5: stars(5);
		   break;
		   case R.id.button6: stars(6);
		   break;
		   case R.id.button7: stars(7);
		   break;
		   case R.id.button8: stars(8);
		   break;
		   case R.id.button9: stars(9);
		   break;
		   case R.id.button0: stars(0);
		   break;
		   case R.id.button10: stars(-1);//clears field
		   break;
		   case R.id.goForward:/* query.query2();*/if (i == 5){ SyncTask_function();}
		   break;
		   //case R.id.tester: tester();
		   }
		   
		   Log.d("Debug","SignMeIn onClickListener started");
		   
		   str = "SignMeIn onClickListener finished\n";
			logWrt.appF(str);
		   
		   //Randy Richardson 08/05/2014 END
	    }
	};
	
	public Void task() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","SignMeIn task started");
		
		str = "SignMeIn task started\n";
		logWrt.appF(str);
		
		new TransmitDataToServer().execute();
		
		Log.d("Debug","SignMeIn task finished");
		
		str = "SignMeIn task finished\n";
		logWrt.appF(str);
		
		//Randy Richards 08/05/2014 END
		
		return null;
	}
	
	public Void SyncTask_function() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","SignMeIn SyncTask_function started");
		
		str = "SignMeIn SyncTask_function started\n";
		logWrt.appF(str);
		
		Button proceed = (Button) findViewById(R.id.goForward);//proceed button
		proceed.setClickable(false);
		
		new SyncLoginTask().execute("check");
		//check();
		
		Log.d("Debug","SignMeIn SyncTask_function finished");
		
		str = "SignMeIn SyncTask_function finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
		
		return null;

	}
	
	public String getName(Cursor curs) {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","SignMeIn getName started");
		
		str = "SignMeIn getName started\n";
		logWrt.appF(str);
		
		//gets the name of person whose pin matches the login pin
		curs.moveToFirst();
		for(int xyz = 0; xyz < curs.getCount(); xyz++) {
			if (Integer.toString(curs.getColumnIndex("pin")).equals(numberToCheckPIN.toString())) {
				//Get the name of the artist whose pin matches and break the loop when found
				String a = curs.getString(curs.getColumnIndex("name"));
				break;
			}
			else {
				//Return a blank string if not found
				String a = "";
			}
			curs.moveToNext();
		}
		
		Log.d("Debug","SignMeIn getName finished");
		
		str = "SignMeIn getName finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
		
		return a;
	}
	
	public void logout(String entry, String id) {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","SignMeIn logout started");
		
		str = "SignMeIn logout started\n";
		logWrt.appF(str);
		
		//Log out the person indicated in the entry
		
		//Set whether the person is logging in or out
		incoming = false;
		
		//Open the databases
		login = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		logout = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		
		//Run the query that logs the person out
		Log.d("login/logout", "Logging in this value: " + entry);
        logout.execSQL("INSERT INTO LogoutTable VALUES" + entry + ";");
        
        //Delete the entry in the login table        
        login.execSQL("DELETE FROM LoginTable WHERE pin = '" + id + "'");
        
        //Close the databases
        logout.close();
        login.close();
        
        Log.d("Debug","SignMeIn logout finished");
        
        str = "SignMeIn logout finished\n";
		logWrt.appF(str);
        
        //Randy Richardson 08/05/2014 END        
        
	}
	
	public void login(String entry) {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","SignMeIn login started");
		
		str = "SignMeIn login started\n";
		logWrt.appF(str);
		
		//Set if the person is logging in or out
		incoming = true;
		
		//Log the person in
		login = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
        login.execSQL("INSERT INTO LoginTable VALUES" + entry + ";");
        
        //Close the login table
        login.close();
        
        Log.d("Debug","SignMeIn login finished");
        
        str = "SignMeIn login finished\n";
		logWrt.appF(str);
        
        //Randy Richardson 08/05/2014 END
        
	}
	
	public void makeTables() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","SignMeIn makeTables started");
		
		str = "SignMeIn makeTables started\n";
		logWrt.appF(str);
		
		//create the tables for use in the app
		try{
			//Create the updates table
			last_update_db = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
			last_update_db.execSQL("CREATE TABLE IF NOT EXISTS updatetable(id INTEGER PRIMARY KEY AUTOINCREMENT, lastupdate DATETIME)");
			Cursor updateCursor = last_update_db.rawQuery("SELECT * FROM updatetable", null);
			if (updateCursor.getCount() == 0) {
				//Populate if empty
				last_update_db.execSQL("INSERT INTO updatetable VALUES(NULL,'" + cal.getTime().toString() +"');");
			}
			
			
			//Create the logout table
			logout = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
			logout.execSQL("CREATE TABLE IF NOT EXISTS LogoutTable(id INTEGER PRIMARY KEY AUTOINCREMENT, pin INT, timestamp DATETIME, totalminutes INT, ref_id INT, login_timestamp DATETIME, pushed INT);");
			
		  
			//Create the login table
			login = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
			login.execSQL("CREATE TABLE IF NOT EXISTS LoginTable(id INTEGER PRIMARY KEY AUTOINCREMENT, pin INT, timestamp DATETIME," /*hours LONG,*/+ " isLoggedIn BOOLEAN, pushed INT);");
			
			//Create the guest table
			last_update_db.execSQL("CREATE TABLE IF NOT EXISTS GuestUpdate(visName VARCHAR, visAddress VARCHAR, artistName VARCHAR, purpose VARCHAR, phone VARCHAR, email VARCHAR, studioNumber INTEGER, pushed VARCHAR);");
			
			//Create the System Vars table
			systemVars = openOrCreateDatabase("System", MODE_PRIVATE, null);
			systemVars.execSQL("CREATE TABLE IF NOT EXISTS SystemVars(id INTEGER PRIMARY KEY AUTOINCREMENT, fresh_install INTEGER, last_update DATETIME, version VARCHAR(30), location VARCHAR) ");
			
			//Make initial values if there are none
			Cursor initialVal = systemVars.rawQuery("SELECT * FROM SystemVars", null);
			if (initialVal.getCount() == 0) {
				systemVars.execSQL("INSERT INTO SystemVars VALUES(NULL, 1, NULL, '1.2', 'unset' );");
			}
			
			systemVars.close();
			logout.close();
			last_update_db.close();
			login.close();

			Log.d("makeTable", "Successfully made the tables");
		}
		catch(Exception e) {
			//Throw an error if something goes wrong
			Log.e("makeTable", "Couldnt make the tables: "+e.toString());
	    }
		
		Log.d("Debug","SignMeIn makeTables finished");
		
		str = "SignMeIn makeTables finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
	}
		
///////////////backs up when button is clicked////////////
    public void goBack() {
    	
    	//Randy Richardson 08/05/2014 START
		
    	Log.d("Debug","SignMeIn goBack started calls StartActivity");
    	
    	str = "SignMeIn goBack started calls StartActivity\n";
		logWrt.appF(str);
    	
    	Intent intent = new Intent(this, StartActivity.class);
    	startActivity(intent);
    	
    	//Randy Richardson 08/05/2014 END   	
    }
    
    public void setupAlarm(int seconds) {
    	
    	//Randy Richardson 08/05/2014 START
		
    	Log.d("Debug","SignMeIn setupAlarm started");
    	
    	str = "SignMeIn setupAlarm started\n";
		logWrt.appF(str);
    	
    	//Create an offset from the current time in which the alarm will go off.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 5);

        //Create a new PendingIntent and add it to the AlarmManager
        Intent intent = new Intent(this, RepeatingAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
            12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = 
            (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000*seconds,
                pendingIntent);
        
        //Log.d("setupAlarm", "Alarm is set");
        
        Log.d("Debug","SignMeIn setupAlarm finished");
        
        str = "SignMeIn setupAlarm finished\n";
		logWrt.appF(str);
        
        //Randy Richardson 08/05/2014 END
    }
    	/*  AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    	  Intent intent = new Intent(this, RepeatingAlarm.class);
    	  PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);
    	 
    	     
    	 
    	 
    	  // Getting current time and add the seconds in it
    	  Calendar cal = Calendar.getInstance();
    	  cal.add(Calendar.SECOND, seconds);
    	 
    	  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000*seconds, pendingIntent);
    	
    	  Log.d("alarm", "Setup the alarm");*/
    	  
    //	}
 //////////////////////////////////////////////////////
    
    //creates *'s when number is clicked and clears them when clear is clicked///
    public void stars(int num) {
    	
    	//Randy Richardson 08/05/2014 START
		
    	Log.d("Debug","SignMeIn stars started");
    	
    	str = "SignMeIn stars started\n";
		logWrt.appF(str);
    	
    	//set the field for inserting the stars
    	EditText stars = (EditText) findViewById(R.id.starField);
    	stars.setInputType(InputType.TYPE_NULL);
    	
    	
    	if(num != -1 && i<5) {
    		//Set the number from the key into the array and increment to the next item in the array
    		number[i] = num;
    			if(i<=4){ i++;}    			
    	}
    	else if(num==-1) {
    		//Clear the numbers from the array if the value -1 is entered
    		i=0;
    	}
    		
    	//Set the visual stars for viewing when entering the pin
    	switch(i) {
    		case 1: 
    			stars.setText("*");
    			break;
    		case 2: 
    			stars.setText("* *");
    			break;
    		case 3: 
    			stars.setText("* * *");
    			break;
    		case 4: 
    			stars.setText("* * * *");
    			break;
    		case 5: 
    			stars.setText("* * * * *");
    			break;
    		case 0: 
    			stars.setText("PIN");
    			break;
    		case -1: 
    			stars.setText ("PIN");//displays PIN when no value entered
    			break;
    	}
		
    	Log.d("Debug","SignMeIn stars finished");
    	
    	str = "SignMeIn stars finished\n";
		logWrt.appF(str);
    	
    	//Randy Richardson 08/05/2014 END
    	
    }
    
    //print number to toast for debugging, not used in production code
    public void printNmbr(int[] take) {
    	
    	//Randy Richardson 08/05/2014 START
		
    	Log.d("Debug","SignMeIn printNmbr started");
    	
    	str = "SignMeIn printNmbr started\n";
		logWrt.appF(str);
    	
    	String a = "";
    	String b = "";
    	for (int x =0; x<number.length; x++) {
    		b = Integer.toString(number[x]);
    		a = a+b;	
    	}
    	Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();
    	
    	Log.d("Debug","SignMeIn printNmbr started");
    	
    	str = "SignMeIn printNmbr finished\n";
		logWrt.appF(str);
    	
    	//Randy Richardson 08/05/2014 END
    	
    	
    }
    
    public void check(){
    	
    	//Randy Richardson 08/05/2014 START
		
    	Log.d("Debug","SignMeIn check started");
    	
    	str = "SignMeIn check started\n";
		logWrt.appF(str);
    	
      getLocation();	
    	System.out.println("Running Check");
      //gets the studio number they entered
      String numberToCheckSN = Integer.toString(one) + Integer.toString(two);
    	
      for (int i = 0; i < number.length; i++){
    	numberToCheckPIN += Integer.toString(number[i]);//gets the pin they entered
      }
      //Clear all white space from entered pin
      numberToCheckPIN = numberToCheckPIN.replaceAll("\\s","");
      System.out.println(numberToCheckPIN.toString() + " " + numberToCheckSN.toString());
      if (numberToCheckPIN.equals("98712") && numberToCheckSN.equals("99")){
	      //proceeds to admin page when code is entered correctly
    	  
    	  Log.d("Debug","SignMeIn check finished");
    	  
    	  str = "SignMeIn check finished\n";
  		  logWrt.appF(str);
    	  
		  Intent intent = new Intent(this, AdminPanel.class);
		  startActivity(intent);
      } 
      else {
      
	      //Open the various database connections and run the querys
	 	  db = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
	 	  logout = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
	 	  login = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
	 	 
	 	  //Define the cursors for the searched
	 	  Cursor li = login.rawQuery("SELECT * from LoginTable WHERE pin = '" + numberToCheckPIN.toString() + "'", null);
	 	  Cursor lo = logout.rawQuery("SELECT * from LogoutTable", null);
	 	  System.out.println("TEST");
	 	  Cursor c = db.rawQuery("SELECT * from StudioInfo", null); 
	 	  System.out.println("END TEST");
	 	  //Move the cursors into place
	 	  lo.moveToFirst();
	 	  li.moveToLast();
	 	  c.moveToFirst();
	 	  
	 	  //Get the current time as a string
	 	  String timestamp = (cal.getTime().toString());
	 	  
	 	  //Run a loop on every item from the studio info table
	 	  Log.d("check", "Running a loop..");
	 	  for (int i  =  0; i <c.getCount(); i++)
	 	  {
	 		 //Log.d("check", "Running a loop.. " + i + " times..");
	 		  //get the pin from the current location of the studio info table
	 		  String pin = c.getString(c.getColumnIndex("pin")); 
	 		  // remove all white space from the string
	 		  pin.replaceAll("\\s","");
	 			  			
	 		if (pin.equals(numberToCheckPIN) && getLastTwo((c.getString(c.getColumnIndex("studio_number")))).equals(numberToCheckSN)) { 
	 			 //matches entered pin/SN with table
	 			 //Toast.makeText(getApplicationContext(), c.getString(c.getColumnIndex("firstname")), Toast.LENGTH_LONG).show();
	 			 nameOfLogin = c.getString(c.getColumnIndex("firstname"));
	 				
	 			 if(li.getCount() != 0) {
	 				 ///for(int a = 0; a < li.getCount(); a++){	 
	 				 if ((li.getString(li.getColumnIndex("isLoggedIn"))).equals("true") && li.getString(li.getColumnIndex("pin")).equals(numberToCheckPIN)){
	 					 //If the person is logged in and the pins match
	 					 //Log the person out of their studio
	 					 Log.d("login/logout", "Logging the artist out");
	 					 
	 					 //define the amount of time that has elapsed since login
	 					 DateTimeFormatter format = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss zzz yyyy");
	 					 org.joda.time.DateTime currentTime = format.parseDateTime(timestamp);
	 					 org.joda.time.DateTime loginTime = format.parseDateTime(li.getString(li.getColumnIndex("timestamp")));
	 					 Period elapsed = new Period (loginTime, currentTime);	
	 					 
	 					 //Log the person out with the elapsed minutes
	 					 logout(("(NULL, '" + pin + "','" + timestamp + "','" + cleanNumber(elapsed.toStandardMinutes().toString())  + "','" + li.getString(li.getColumnIndex("id")) + "','" + li.getString(li.getColumnIndex("timestamp")) +"', 0)"), pin );
	 					 Log.d("login/logout", "Logged the artist out"); 						
	 					 Log.d("login/logout", ("current time: " + currentTime + " login time: " + loginTime + "MINUTES ELAPSED: " + cleanNumber(elapsed.toStandardMinutes().toString())));
	 					 
	 					 //Transmit data to the server
	 					 new TransmitDataToServer().execute();

	 					 //TODO Logout Sync 
	 					 new SyncLoginTask().execute("logout");
	 					 
	 					 //Proceed to the welcome/exit screen and exit out of the for loop
	 					 proceed();
	 					 break;	
	 					}
					 else if(li.getString(li.getColumnIndex("isLoggedIn")).equals("false") && li.getString(li.getColumnIndex("pin")).equals(numberToCheckPIN))  {
						 //If the person is not logged in and the pins match
						 //Log the person into their studio
	 					 login("( NULL, '" + pin + "', " + "'" + timestamp + "'," + /*Long.toString(System.currentTimeMillis())((cal.get(Calendar.HOUR_OF_DAY)))+ */  "'true', 0)");
	 				     Log.d("login/logout", "Logged the artist in at " + timestamp ); 
	 				     
	 				     //Proceed to the welcome/exit screen and exit out of the for loop
	 				     new SyncLoginTask().execute();
	 				     proceed();
	 					 break;
	 					}
	 			 }
	 			  
	 			  else if(li.getCount() == 0) {
	 				//If no one is logged in, log in this person because there is no list to check again
					login("( NULL, '" + pin + "', " + "'" + timestamp + "'," +/* Long.toString(System.currentTimeMillis())+*/ "'true', 0)");
					Log.d("login/logout", "Logged the artist in at " + timestamp);
	 				
					//Get the pin number and studio number and store it in a global variable for some reason				
	 				pinCall = c.getString(c.getColumnIndex("pin"));
	 				snWithLetter = c.getString(c.getColumnIndex("studio_number"));
	 				
	 				//Proceed to the welcome/exit screen
	 				new SyncLoginTask().execute();
	 				proceed();
	 			  }			
	 			 	
	 			 //Break out of the loop once a match is found
	 			 Log.d("login/logout", "Matched an entry in the table"); 
	 			 break;  
	 		}
	 		else if(i+1 == c.getCount() && !(pin.equals(numberToCheckPIN))) { 				
	 		  //If the count of the studio table is equal to the current  studioInfo item number and the pin doesnt match
	 		  //Throw an invalid
	 		  Invalid();
	 		}
	 		
	 		//Move to the next item in the studioInfo table for checking
	 		c.moveToNext();
	 	  }
	 	  
	 	  //Close out the databases
	 	  db.close();
	 	  logout.close();
	 	  login.close();
      }
      
      Log.d("Debug","SignMeIn check finished");
      
      str = "SignMeIn check finished\n";
      logWrt.appF(str);
      
      //Randy Richardson 08/05/2014 END
      
    }
    
    public void checkConnection () {
    	
    	//Randy Richardson 08/05/2014
    	
    	Log.d("Debug","SignMeIn checkConnection started");
    	
    	str = "SignMeIn checkConnection started\n";
		logWrt.appF(str);
    	
    	//Check to see if the internet is active
    	ConnectivityManager connMgr = (ConnectivityManager) 
        getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            offline = false;
        } else {
            offline = true;
        }
        
        Log.d("Debug","SignMeIn checkConnection finished");
        
        str = "SignMeIn checkConnection finished\n";
		logWrt.appF(str);
        
        //Randy Richardson 08/05/2014 END

    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean checkServer() throws JSONException, UnsupportedEncodingException  {
    	
    	//Randy Richardson 08/05/2014 START
    	
    	Log.d("Debug","SignMeIn checkServer started");
    	
    	str = "SignMeIn checkServer started\n";
		logWrt.appF(str);
    	
    	//define the vars for this task
    	DefaultHttpClient client = new DefaultHttpClient();
    	HttpResponse response = null;
    	String responseString = null;
    	Boolean responseCheck = null;
    	
    	HttpPost checkConnection = generateHttpPost("http://php.chashama.info/check_connect.php");
    	
 		//Set the username and password
 		List nvps = generateJsonData(null);
		
		//Encode the data for transmission
		UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
		
		//Append the data to the request
		checkConnection.setEntity(p_entity);
    	
 		//Try the query and get a response
			try {
				Log.d("Server Check", "Init http request");
				
				Log.d("Server Check", "Get response for guest info data send");
				
				response = client.execute(checkConnection);
				Log.d("Server Check", "Got response, printing");
				
				//handle the response
				responseString = EntityUtils.toString(response.getEntity());
				StatusLine status = response.getStatusLine();
				Log.d("Server Check", responseString);
				
				Log.d("Server Check", response.getStatusLine().toString());
				if ((status.getStatusCode() == HttpStatus.SC_OK) && (responseString.equals("connected successfully"))) {
					responseCheck = true;
				} else {
					responseCheck = false;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				responseCheck = false;
			} catch (ConnectTimeoutException e){
				e.printStackTrace();
				responseCheck = false;
			} catch (IOException e) {
				e.printStackTrace();
				responseCheck = false;
			}
			
			Log.d("Debug","SignMeIn checkServer finished");
			
			str = "SignMeIn checkServer finished\n";
			logWrt.appF(str);
			
			//Randy Richardson 08/05/2014 END
			
			return responseCheck;
    }
    
    public HttpPost generateHttpPost(String url){
    	
    	//Randy Richardson 08/05/2014 START
    	
    	Log.d("Debug","SignMeIn generateHttpPost started");
    	
    	str = "SignMeIn generateHttpPost started\n";
		logWrt.appF(str);
    	
    	//Set the url to use
    	HttpPost checkConnection = new HttpPost(url);
    	
    	//Define the timeouts for the connection
    	HttpParams httpParameters = new BasicHttpParams();
 		int timeoutConnection = 3000;
 		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
 		int timeoutSocket = 5000;
 		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
 		
 		//Set the params for the check
 		checkConnection.setParams(httpParameters);
 		
 		Log.d("Debug","SignMeIn generateHttpPost finished");
 		
 		str = "SignMeIn generateHttpPost finished\n";
		logWrt.appF(str);
 		
 		//Randy Richardson 08/05/2014 END
 		
 		return checkConnection;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List generateJsonData(String[] foobar) {
    	
    	//Randy Richardson 08/05/2014 START
    	
    	Log.d("Debug","SignMeIn generateJsonData started");
    	
    	str = "SignMeIn generateJsonData started\n";
		logWrt.appF(str);
		
    		List nvps = new ArrayList();
	    	//Add basic username
    		nvps.add(new BasicNameValuePair("username", "kario"));
			nvps.add(new BasicNameValuePair("password", "password"));	
			Log.d("GenerateJSON", "Added basic password auth");
			
			//Add the extras
			if(foobar!=null && foobar.length>0){
				for (int b = 0; b < foobar.length; b++) {
					String[] parts = foobar[b].split(",");
					nvps.add(new BasicNameValuePair(parts[0], parts[1]));
					Log.d("GenerateJSON", "Added Extra Value: " + parts[0] + "," +parts[1]);
				}
    		}
			
			Log.d("Debug","SignMeIn generateJsonData finished");
			
			str = "SignMeIn generateJsonData finished\n";
			logWrt.appF(str);
			
			//Randy Richardson 08/05/2014 END
			
    	return nvps;    	
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void loginSync_f(String check) throws IllegalStateException, IOException, ParseException {
    	
    	//Randy Richardson 08/05/2014 START
    	
    	Log.d("Debug","SignMeIn loginSync_f started");
    	
    	str = "SignMeIn loginSync_f started\n";
		logWrt.appF(str);
    	
    	//Log.d("LoginSync", "Beginning Login Sync");
    	//Open the database
    	db = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
    	
    	//Check to see if there are any offline logins or a logout
    	Cursor li;
    	li = db.rawQuery("SELECT * from LoginTable WHERE pushed = '0'", null);
    	li.moveToFirst();
    	
    	//Check to see if there are any entries in the cursor or if the logout was called
    	if (li.getCount() > 0 || check.equals("logout")) {
    		
    		Log.d("LoginSync", "Offline Records Found, Dumping data on server");
    		//If there are any offline logins, Dump existing table on the server. Local database is always considered most accurate in this regard
    		dumpServerData(StudioLocation);
    		
    		//Get all current logins from the local database
    		Log.d("LoginSync", "Offline Records Found, Pushing local data to server");
    		Cursor logins;
        	logins = db.rawQuery("SELECT * from LoginTable", null);
        	logins.moveToFirst();
    		
	    	HttpPost httppost = generateHttpPost("http://php.chashama.info/update_tables.php");
   	
	    	//Push new data to the server
	    	for (int a = 0; a < logins.getCount(); a++) {
	    		Log.d("LoginSync", "Compiling Record: Offline");
	    			
	    			//Add all the values into the array
	    			String details[] = {
	    				"timestamp," + logins.getString(logins.getColumnIndex("timestamp")), 
	    				"logged_in_pin," + logins.getString(logins.getColumnIndex("pin")),
	    				"location," + StudioLocation,
	    			};
	    			
	    			//Define the data to send, and generate based on values from the array;	    			List nvps = new ArrayList();	
	    			nvps = 	generateJsonData(details);
	 				
	 				System.out.println("Compiled " + Integer.toString(a+1) + " times");
	 				
	 				UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
	 				httppost.setEntity(p_entity);
	 				
	 				Boolean sent = sendData(httppost);
	 				if (sent == true){
	 					db.execSQL("UPDATE LoginTable SET pushed=1 WHERE id = '" + logins.getString(logins.getColumnIndex("id")) + "'");
	 				}
	 				logins.moveToNext();
	 				
	 				
	    	}
	    	retreiveData(StudioLocation);
    	
    	} else {
        	//Download entire table from server and replace existing entirely if applicable
        	//This should allow for device wiping of current data without disrupting existing logged in users
    		//should also allow for re-installation of the app without data loss
    		
    		
        	//Check to see if there are any logins at all
        	Cursor logins;
        	logins = db.rawQuery("SELECT * from LoginTable", null);
        	Log.d("LoginSync", "Got all logins... Found " + logins.getCount() + " records.");
        	logins.moveToFirst();
        	Log.d("LoginSync", "Moved cursor to front...");
        	
        	if (logins.getCount()== 0){
        		Log.d("LoginSync", "0 Logins found, checking to see if fresh install...");
    			systemVars = openOrCreateDatabase("System", MODE_PRIVATE, null);
    			Cursor sysVars = systemVars.rawQuery("SELECT * FROM SystemVars", null);
    			sysVars.moveToLast();
    			
    			Log.d("LoginSync", sysVars.getString(sysVars.getColumnIndex("fresh_install")));
    			
    			
    			if (sysVars.getInt(sysVars.getColumnIndex("fresh_install")) == 1) {
    				Log.d("LoginSync", "No one currently logged in, Fresh Install, Download from Server");
    				retreiveData(StudioLocation);
    				
    				//Set the fresh_install data to 0
    				systemVars.execSQL("UPDATE SystemVars SET fresh_install =0 WHERE id =1");
    			} else {
    				Log.d("LoginSync", "No one currently logged in, Dump Server Table.");
    				dumpServerData(StudioLocation);
    			}
        		
        		systemVars.close();
        	}else {
        		//If there are no offline records, presume the data on the server to be accurate without change
        		Log.d("LoginSync", "No offline records found, Proceeding.");
        		retreiveData(StudioLocation);
        	}
    	}
    	

    	db.close();
    	
    	//Log.d("LoginSync", "Syncing Complete");
    	
    	Log.d("Debug","SignMeIn loginSync_f finished");
    	
    	str = "SignMeIn loginSync_f finished\n";
		logWrt.appF(str);
    	
    	//Randy Richardson 08/05/2014 END
    }
    
    private void dumpServerData(String location) throws UnsupportedEncodingException {
    	
    	//Randy Richardson 08/05/2014 START
    	
    	Log.d("Debug","SignMeIn dumpServerData started");
    	
    	str = "SignMeIn dumpServerData started\n";
		logWrt.appF(str);
    	
    	//Log.d("ServerDump", "Dumping Server Table");
    	//Set Basic details and tell the server to dump the table and create an empty one
    	String details[] = { "location," + location };
    	HttpPost httppost = generateHttpPost("http://php.chashama.info/drop_table.php");
		
    	//Define the data to send, and generate based on values from the array;
		List nvps = new ArrayList();	
		nvps = 	generateJsonData(details);
		
		UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
		httppost.setEntity(p_entity);
    	
		sendData(httppost);
		//Log.d("ServerDump", "Dumping Complete");
		
		Log.d("Debug","SignMeIn dumpServerData finished");
		
		str = "SignMeIn dumpServerData finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
	}

	public void retreiveData(String location) throws IllegalStateException, IOException {
		
		//Randy Richardson 08/05/2014 START
    	
    	Log.d("Debug","SignMeIn retrieveData started");
    	
    	str = "SignMeIn retrieveData started\n";
		logWrt.appF(str);
    	
		//Log.d("RetrieveData", "Running Data retrieval");
		String result = "";
		InputStream isr = null;
		HttpResponse response = null;
		
    	String details[] = { "location," + location };
    	HttpPost httppost = generateHttpPost("http://php.chashama.info/retreive_login.php");
    	
    	//Define the data to send, and generate based on values from the array;
    	List nvps = new ArrayList();	
    	nvps = 	generateJsonData(details);
    	
    	UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
		httppost.setEntity(p_entity);
		
		response = sendDataResponse(httppost);
		
		//get the content returned in the response				
		HttpEntity entity = response.getEntity();
	    isr = entity.getContent();

	    try{
	    	//Attempt to convert the content to readable string
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"iso-8859-1"),8);
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	        	sb.append(line + "\n");
	        }
	        isr.close();
	        result=sb.toString();
	    }
	    catch(Exception e){
	        Log.e("RetrieveData", "Error: converting result: "+e.toString());
	    }

    	try{
        
	       	//Convert the result into a JSON object and extract the artist data from the object
            JSONObject jobj = new JSONObject(result);
	       	JSONArray subArray = jobj.getJSONArray("artists");
	       	JSONObject subSubArray;
	       	
       	    if(jobj.getInt("success") == (1)) {
	    	
	    		//Drop the existing table for studioinfo if it exists
	            db = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
	            db.execSQL("DROP TABLE IF EXISTS LoginTable;");
	            db.close();
	            
				//Create the login table
				login = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
				login.execSQL("CREATE TABLE IF NOT EXISTS LoginTable(id INTEGER PRIMARY KEY AUTOINCREMENT, pin INT, timestamp DATETIME," /*hours LONG,*/+ " isLoggedIn BOOLEAN, pushed INT);");
				login.close();
		
	            //Fill the login table from new data
	            db = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
       	    	
       	    	//If the object indicates success in pulling data from the MySQL table
       	    	Log.d("RetrieveData", "JSON Object indicates success in acquiring data from the tables");
       	    	for(int i = 0; i<subArray.length(); i++){ 
       	    		//Run a loop for every entry in the artists tables

       	    		//Get the current artist data row
       	    		subSubArray = subArray.getJSONObject(i);
       	    		
       	    		String timestamp = subSubArray.getString("login_timestamp");
       	    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
       	    		Date dateStamp = dateFormat.parse(timestamp);
       	    		
       	    		//Create the new entry in the LoginTable table
       	    		db.execSQL("INSERT INTO LoginTable VALUES" +
       	    				"(NULL,'" +
       	    				subSubArray.getString("logged_in_pin")+ "','" +
       	    				dateStamp + "','true', 1);"
       	    				);	  
       	    	} //TODO change this value to 1
       		Log.d("RetrieveData", "Data Retreived");
       	  }
     	  else {
     		 //Throw an error if the php indicates an unsuccessful attempt to pull data
     		 Log.e("RetrieveData", "Error in PHP script");
     		  
     	  }
       	    
       	  //Close out the databases
       	  db.close();
    	}
    	catch(Exception e){
    		//throw an error if you cant connect
            Log.e("RetrieveData", "Error in http connection: "+e.toString());
    	}
		
    	//Log.d("RetrieveData", "Retrieval function completed");
    	
    	Log.d("Debug","SignMeIn retrieveData finished");
    	
    	str = "SignMeIn retrieveData finished\n";
		logWrt.appF(str);
    	
    	//Randy Richardson 08/05/2014 END
    }
    
    private HttpResponse sendDataResponse(HttpPost httppost) {
    	
    	//Randy Richardson 08/05/2014 START
    	
    	Log.d("Debug","SignMeIn sendDataResponse started");
    	
    	str = "SignMeIn sendDataResponse started\n";
		logWrt.appF(str);

    	//define the vars for this task
    	DefaultHttpClient client = new DefaultHttpClient();
    	HttpResponse response = null;
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
    	
			try {
				Log.d("sendDataResponse", "Init http request");
				Log.d("sendDataResponse", "Get response for data inquiry");
				
				response = client.execute(httppost);
				Log.d("sendDataResponse", "Got response");
			
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			

			Log.d("Debug","SignMeIn sendDataRespons finished");
			
			str = "SignMeIn sendDataResponse finished\n";
			logWrt.appF(str);
			
			//Randy Richardson 08/05/2014 END
			
			return response;
		
	}
    
    private Boolean sendData(HttpPost httppost) {
    	
    	//Randy Richardson 08/05/2014 START
    	
    	Log.d("Debug","SignMeIn sendData started");

    	str = "SignMeIn sendData started\n";
		logWrt.appF(str);
    	
    	//define the vars for this task
    	DefaultHttpClient client = new DefaultHttpClient();
    	String response = null;
    	Boolean result = false;
    	
			try {
				Log.d("sendData", "Init http request");
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				Log.d("sendData", "Get response for data push");
				
				response = client.execute(httppost, responseHandler);
				Log.d("sendData", "Got response, printing");
				//response.getEntity().consumeContent();
				Log.d("sendData", response.toString());
				
				if (response.toString().equals("connected successfully")) {
					Log.d("sendData", "Data sent successfully");
					result = true;
				} else {
					Log.d("sendData", "You bore me, also, it didnt work");
				}
				
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d("sendData", "You should never see this message");
			
			Log.d("Debug","SignMeIn sendData finished");
			
			str = "SignMeIn sendData finished\n";
			logWrt.appF(str);
			
			//Randy Richardson 08/05/2014 END
			
			return result;
		
	}
	
    public void transmitData_f() throws UnsupportedEncodingException {
    	
    	//Randy Richardson 08/05/2014 START
    	
    	Log.d("Debug","SignMeIn transmitData_f started");
    	
    	str = "SignMeIn transmitData_f started\n";
		logWrt.appF(str);
    	
    	Log.d("Transmit Data", "Preparing to transmit data...");
    	SQLiteDatabase pushLogout = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
    	last_update_db = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
    	Boolean response = null;
    	
    	HttpPost httppost = generateHttpPost("http://php.chashama.info/update_tables.php");
    	
    	Cursor visitors = last_update_db.rawQuery("SELECT * from GuestUpdate WHERE pushed = '0'", null);
    	Cursor uc = last_update_db.rawQuery("SELECT * from updatetable", null);
    	uc.moveToLast();
    	visitors.moveToFirst();
    	
    	Cursor lo = pushLogout.rawQuery("SELECT * from LogoutTable WHERE pushed = '0'", null);
    	lo.moveToFirst();
    	Log.d("Transmit Data", "Preparing to transmit logout info...");
    	
    	//Get location
    	systemVars = openOrCreateDatabase("System", MODE_PRIVATE, null);
		Cursor sysVars = systemVars.rawQuery("SELECT * FROM SystemVars", null);
		sysVars.moveToLast();
		
    	
    	for (int a = 0; a < lo.getCount(); a++) {
 	    	System.out.println("Compiling data... " + (a+1) + " times.");
 	    	
			//Add all the values into the array
			String details[] = {
				"login," + lo.getString(lo.getColumnIndex("login_timestamp")), 
				"logout," + lo.getString(lo.getColumnIndex("timestamp")),
				"PIN," + lo.getString(lo.getColumnIndex("pin")),
				"totalminutes," + lo.getString(lo.getColumnIndex("totalminutes")),
				"locationVal," + sysVars.getString(sysVars.getColumnIndex("location"))
			};
			
			//Define the data to send, and generate based on values from the array;
			List nvps = new ArrayList();	
			nvps = 	generateJsonData(details);
			
			//Mark data as sent
			
	
 	    	
 				
 			UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
 			httppost.setEntity(p_entity);
 			 			
 			response = sendData(httppost);
 			
 			if (response == true) {
				Log.d("Transmit Data", "Set the logout info as pushed");
				pushLogout.execSQL("UPDATE LogoutTable SET pushed=1 WHERE id = '" + lo.getString(lo.getColumnIndex("id")) + "'");
			} else {
				Log.d("Transmit Data", "logout info was not marked as pushed");
			}
 			//iterate the cursor
 			lo.moveToNext();
 		 }
    	
    	
 	    //cycle through the visitor table, push the data, and mark them as pushed if successful response
 	    for (int i= 0; i < visitors.getCount(); i++ ) {
 	    	System.out.println("Compiling data... " + (i+1) + " times.");
 	    	
 	    	//Add all the values into the array
			String details[] = {
				"visName," + visitors.getString(visitors.getColumnIndex("visName")), 
				"visAddress," + visitors.getString(visitors.getColumnIndex("visAddress")),
				"artistName," + visitors.getString(visitors.getColumnIndex("artistName")),
				"purpose," + visitors.getString(visitors.getColumnIndex("purpose")),
				"phone," + visitors.getString(visitors.getColumnIndex("phone")),
				"email," + visitors.getString(visitors.getColumnIndex("email")),
				"studioNumber," + visitors.getString(visitors.getColumnIndex("studioNumber"))
			};
 	    	
			//Define the data to send, and generate based on values from the array;
 	    	List nvps = new ArrayList();
 	    	nvps = 	generateJsonData(details); 
 	    	
			//HTTP request time
			UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
			httppost.setEntity(p_entity);
			
			response = sendData(httppost);
			
			if (response == true) {
				Log.d("response", "Set the guest info as checked");
				last_update_db.execSQL("UPDATE GuestUpdate SET pushed='1' WHERE email = '" + visitors.getString(visitors.getColumnIndex("email")) + "'");
			} else {
				Log.d("response", "Guest info was not marked as sent");
			}
			
			//iterate the cursor
			visitors.moveToNext();
				
				
 	    }
    	
 	   Log.d("Debug","SignMeIn transmitData_f finished");
 	   
 	   	str = "SignMeIn transmitData_f finished\n";
		logWrt.appF(str);
 	   
 	   //Randy Richardson 08/05/2014 END
    	
	}

	public void SyncLogin(String check) throws JSONException, IllegalStateException, IOException, ParseException {
		
		//Randy Richardson 08/05/2014 START
    	
    	Log.d("Debug","SignMeIn SyncLogin started");
    	
    	str = "SignMeIn SyncLogin started\n";
		logWrt.appF(str);
    	
    	//Make sure the offline flag is properly marked
    	Log.d("connection", "Checking Connection");
    	checkConnection();
    	Log.d("connection", "Checking Connection Completed");
    	
    	Log.d("connection", "Checking offline var");
    	if (offline == false) {
	    	//Make sure the server is online and accepting commands
    		Log.d("connection", "Checking Server Alive?");
	    	if (checkServer() == true) {
	    		Log.d("connection", "Server Alive");
	    		//Run the login syncer
	    		loginSync_f(check);
	    	} 
    	}
    
    	Log.d("Debug","SignMeIn SyncLogin finished");
    	
    	str = "SignMeIn SyncLogin finished\n";
		logWrt.appF(str);
    	
    	//Randy Richardson 08/05/2014 END
    	
    	
    }
	
	public void transmitData() throws JSONException, IllegalStateException, IOException, ParseException {
		
		//Randy Richardson 08/05/2014 START
    	
    	Log.d("Debug","SignMeIn transmitData started");
    	
    	str = "SignMeIn transmitData started\n";
		logWrt.appF(str);
    	
    	//Make sure the offline flag is properly marked
    	Log.d("connection", "Checking Connection");
    	checkConnection();
    	Log.d("connection", "Checking Connection Completed");
    	
    	Log.d("connection", "Checking offline var");
    	if (offline == false) {
	    	//Make sure the server is online and accepting commands
    		Log.d("connection", "Checking Server Alive?");
	    	if (checkServer() == true) {
	    		Log.d("connection", "Server Alive");
	    		//Run the login syncer
	    		transmitData_f();
	    	} 
    	}
    	
    	Log.d("Debug","SignMeIn transmitData finished");
    	
    	str = "SignMeIn transmitData finished\n";
		logWrt.appF(str);
    	
    	//Randy Richardson 08/05/2014 END
    	
    	
    }
    
	public String cleanNumber(String numbertoclean){
		
		//Randy Richardson 08/05/2014 START
    	
    	Log.d("Debug","SignMeIn cleanNumber started");
		
    	str = "SignMeIn cleanNumber started\n";
		logWrt.appF(str);
    	
		String cleaned ="";
		cleaned = numbertoclean.replaceAll("[^\\d.]", "");
		
		Log.d("Debug","SignMeIn cleanNumber finished");
		
		str = "SignMeIn cleanNumber finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
		
		return cleaned;
	}
   
    public void proceed() {
    	
    	//Randy Richardson 08/05/2014 START
    	
    	Log.d("Debug","SignMeIn proceed started");
    	
    	str = "SignMeIn proceed started\n";
		logWrt.appF(str);
    	
    	//start the welcome/goodbye screen and send over the needed vars
    	Intent intent = new Intent(this, Welcome.class);
    	intent.putExtra("pin_num", numberToCheckPIN);
    	startActivity(intent);
    	
    	Log.d("Debug","SignMeIn proceed finished");
    	
    	str = "SignMeIn proceed finished\n";
		logWrt.appF(str);
    	
    	//Randy Richardson 08/05/2014 END
    }
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_me_in, menu);
		return true;
	}
	
	
	private class GetDataTask extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			
			//Randy Richardson 08/05/2014 START
	    	
	    	Log.d("Debug","SignMeIn GetDataTask called doInBackground started");
			
	    	str = "SignMeIn GetDataTask called doInBackground started\n";
			logWrt.appF(str);
	    	
			try {
				try {
					getData();
				} catch (IllegalStateException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			Log.d("Debug","SignMeIn GetDataTask called doInBackground finished");
			
			str = "SignMeIn GetDataTask called doInBackground finished\n";
			logWrt.appF(str);
			
			return null;
			
		} 
		
		protected void onPostExecute(Void result) {
			

			Log.d("Debug","SignMeIn GetDataTask called onPostExecution started");
			
			str = "SignMeIn GetDataTask called onPostExecution started\n";
			logWrt.appF(str);
			
			//Toast.makeText(getApplicationContext(), "Retreived Tables", Toast.LENGTH_SHORT).show();
			
			Log.d("Debug","SignMeIn GetDataTask called onPostExecution finished");
			
			str = "SignMeIn GetDataTask called onPostExecution finished\n";
			logWrt.appF(str);
			
		}
		
		//Randy Richardson 08/05/2014 END
		
	}
	
	private class TransmitDataToServer extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			
			//Randy Richardson 08/05/2014 START
	    	
	    	Log.d("Debug","SignMeIn TransmitDataToServer called doInBackground started");
	    	
	    	str = "SignMeIn TransmitDataToServer called doInBackground started\n";
			logWrt.appF(str);
			
			try {
				transmitData();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Log.d("Debug","SignMeIn TransmitDataToServer called doInBackground finished");
			
			str = "SignMeIn TransmitDataServer called doInBackground finished\n";
			logWrt.appF(str);
			
			return null;
		}

		protected void onPostExecute(Void result) {
			Log.d("Debug","SignMeIn TransmitDataToServer called onPostExcecute started");
			
			str = "SignMeIn TransmitDataToServer called onPostExecute started\n";
			logWrt.appF(str);
			
			//Toast.makeText(getApplicationContext(), "Transmit Data completed", Toast.LENGTH_SHORT).show();
			
			Log.d("Debug","SignMeIn TransmitDataToServer called onPostExcecute finished");
			
			str = "SignMeIn TransmitDataToServer called onPostExecute finished\n";
			logWrt.appF(str);
		}

		protected void onPreExecute() {
			
			Log.d("Debug","SignMeIn TransmitDataToServer called onPreExcecute started");
			
			str = "SignMeIn TransmitDataToServer called onPreExecute started\n";
			logWrt.appF(str);
			
			//Toast.makeText(getApplicationContext(), "Preparing to Transmit Data to Server", Toast.LENGTH_SHORT).show();
			
			Log.d("Debug","SignMeIn TransmitDataToServer called onPreExcecute finished");
			
			str = "SignMeIn TransmitDataToServer called onPreExecute finished\n";
			logWrt.appF(str);
		}
		
		//Randy Richardson 08/05/2014 END
		
	}
	
	private class SyncLoginTask extends AsyncTask<String, String, String>{
		@Override
		protected String doInBackground(String... params){
			
			//Randy Richardson 08/05/2014 START
	    	
	    	Log.d("Debug","SignMeIn SyncLoginTask called doInBackground started");
	    	
	    	str = "SignMeIn SyncLoginTask called doInBackground started\n";
			logWrt.appF(str);
			
			try {
				if (params.length > 0 && params[0].equals("logout")) {
					SyncLogin("logout");
				} else {
					SyncLogin("none");
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//check if theres anything in params, if so pass it on, if not, pass a null
			String result = (params.length > 0) ? params[0] : null;
			
			Log.d("Debug","SignMeIn SyncLoginTask called doInBackground finished");
			
			str = "SignMeIn SyncLoginTask called doInBackground finished\n";
			logWrt.appF(str);
			
			return result;			
		}

		protected void onPostExecute(String result) {
			
			Log.d("Debug","SignMeIn SyncLoginTask called onPostExcecute started");
			
			str = "SignMeIn SyncLoginTask called onPostExecute started\n";
			logWrt.appF(str);
			
			Toast.makeText(getApplicationContext(), "Synced Login Data with server", Toast.LENGTH_SHORT).show();
			if (result != null) { 
				if (result.equals("check")) {
					check();
				}
			}
			
			Log.d("Debug","SignMeIn SyncLoginTask called doInBackground finished");
			
			str = "SignMeIn SyncLoginTask called onPostExecute finished\n";
			logWrt.appF(str);
			
		}
		protected void onPreExecute() {
			
			Log.d("Debug","SignMeIn SyncLoginTask called onPreExecute started");
			
			str = "SignMeIn SyncLoginTask called onPreExecute started\n";
			logWrt.appF(str);
			
			Toast.makeText(getApplicationContext(), "Syncing Login Data with server", Toast.LENGTH_SHORT).show();
		
			Log.d("Debug","SignMeIn SyncLoginTask called onPreExecute finished");
			
			str = "SignMeIn SyncLoginTask called onPreExecute finished\n";
			logWrt.appF(str);
			
		}
		
		//Randy Richardson 08/05/2014 END
	
	}
	
	private class UpdateTables extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			
			//Randy Richardson 08/05/2014 START
	    	
	    	Log.d("Debug","SignMeIn UpdateTables called doInBackground started");
	    	
	    	str = "SignMeIn UpdateTables called doInBackground started\n";
			logWrt.appF(str);
			
			makeTables();
			
			Log.d("Debug","SignMeIn UpdateTables called doInBackground finished");
			
			str = "SignMeIn UpdateTables called doInBackground finished\n";
			logWrt.appF(str);
			
			return null;
		}
		
		protected void onPostExecute(Void result) {
			
			Log.d("Debug","SignMeIn UpdateTables called onPostExecute started");
			
			str = "SignMeIn UpdateTables called onPostExecute started\n";
			logWrt.appF(str);
			
			getLocation();
			
			Log.d("Debug","SignMeIn UpdateTables called onPostExecute finished");
			
			str = "SignMeIn UpdateTables called onPostExecute finished\n";
			logWrt.appF(str);
		}
		
	}

	//Randy Richardson 08/05/2014 END
	
}
		
		/*
		public void onReceive(Context context, Intent intent) {
			 
		     Log.d("alarm", "BroadcastReceiver, in onReceive:");
		 
		  }
		
		public void setAlarm(Context context){
			//context.registerReceiver();
			Log.d("alarm", "alarm set");
			Intent i = new Intent(context, Alarm.class);
			PendingIntent pintent = PendingIntent.getBroadcast(context, 2, i, 0);
			AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),/*System.currentTimeMillis() **///*1000 * 5/**60*60*24*7*//*, pintent);
		/*}
		public void CancelAlarm(Context context) {
			Intent intent = new Intent(context, Alarm.class);
			PendingIntent sender = PendingIntent.getBroadcast(context,0,intent,0);
			AlarmManager alarmmanager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			
		}
	}
	*/
	


