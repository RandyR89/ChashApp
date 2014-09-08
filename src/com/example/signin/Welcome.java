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

import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class Welcome extends Activity {
	public TextView welcomeOrBye;
	public TextView nameOrGuest; 
	public TextView lightSwitch;
	public TextView hoursLogged;
	public String pin_num = "";
	public boolean checkSection = false;
	public String section;
	public boolean guest;
	
	/*Randy Richardson 08/05/2014 START
	 * Instance of LogWrt class and a String used to save the Log*/

	LogWrt logWrt = new LogWrt();
	String str;

	//Randy Richardson 08/05/2014 END
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome has been called");
		
		str = "Welcome has been called\n";
		logWrt.appF(str);
		
		Intent intent = getIntent();
		pin_num = intent.getStringExtra("pin_num");
		guest = intent.getBooleanExtra("guestCheck", false);
		
//		if(SignMeIn.nameOfLogin!= null) {
//			Toast.makeText(getApplicationContext(), SignMeIn.nameOfLogin, Toast.LENGTH_LONG).show();
//		}
//		
//		else {
//			Toast.makeText(getApplicationContext(), "name is null", Toast.LENGTH_LONG).show();
//		}
//		
		welcomeOrBye  = (TextView) findViewById(R.id.welcomeOrBye);
		nameOrGuest = (TextView) findViewById(R.id.nameOrGuest);
		hoursLogged = (TextView) findViewById(R.id.hoursLogged);
		lightSwitch = (TextView) findViewById(R.id.lightSwitch);
		
		//new stopz().execute();
		
		setGreeting();
		
		if (!SignMeIn.incoming  ){
			if(!guest) {
				lightMessage();
			}
		}
		
		returnToStart(10000);
	
		Log.d("Debug","Welcome has finished");
		
		str = "Welcome has finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
		
	}
	public void returnToStart(int delay){
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome returnToStart started");
		
		str = "Welcome returnToStart started\n";
		logWrt.appF(str);
		
		Handler handler = new Handler(); 
	    handler.postDelayed(new Runnable() { 
	         public void run() { 
	        	 finishjapple();
	         } 
	    }, delay); 
	    
	    Log.d("Debug","Welcome returnToStart finished");
	    
	    str = "Welcome returnToStart finished\n";
		logWrt.appF(str);
	    
	    //Randy Richardson 08/05/2014 END
	}

	public void setGreeting() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome setGreeting started");
		
		str = "Welcome setGreeting started\n";
		logWrt.appF(str);
		
		if (SignMeIn.incoming == true && Confirm.guest == false) {
			welcomeOrBye.setText("Welcome, ");
			nameOrGuest.setText(SignMeIn.nameOfLogin + "!");
			
			double totalminutes = getHoursLogged();
			int hours = (int) Math.floor(totalminutes / 60); 
			int minutes = (int)totalminutes - (hours*60);
			hoursLogged.setText("You have logged " + hours + " hours and " + minutes +" minutes this month.");
		}
		
		else if (SignMeIn.incoming == false && Confirm.guest == false) {
			//System.out.println(getHoursLogged() + "THIS IS THE HOURS LOGGED");
			welcomeOrBye.setText("Goodbye, ");
			nameOrGuest.setText(SignMeIn.nameOfLogin + "!");
			
			double totalminutes = getHoursLogged();
			int hours = (int) Math.floor(totalminutes / 60); 
			int minutes = (int)totalminutes - (hours*60);
			hoursLogged.setText("You have logged " + hours + " hours and " + minutes +" minutes this month.");
		}
		
		else if (Confirm.guest == true) {
			welcomeOrBye.setText("WELCOME, ");
		    nameOrGuest.setText("GUEST!");
		    Confirm.guest = false;
		}
		
		Log.d("Debug","Welcome setGreeting finished");
		
		str = "Welcome setGreeting finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
		
	}
	
	public String lightMessage() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome lightMessage started");		

		str = "Welcome lightMessage started\n";
		logWrt.appF(str);
		
		System.out.println("Ran lightmessage");
		if((getfirst(getSN()) == ('b') || getfirst(getSN()) == ('B'))&& lastOneOut()) {
			
			if (section.length() > 1){
				lightSwitch.setText("You are the last one out in the building, dont forget to turn off " + section + "!");
			} else {
				lightSwitch.setText("You are the last one out, dont forget to turn off switch " + section + "!");
			}
			
			Log.d("Debug","Welcome lightMessage finished");
			
			str = "Welcome lightMessage finished\n";
			logWrt.appF(str);
			
			return "Lights off message displayed";
		} 
		else {
			
			Log.d("Debug","Welcome lightMessage finished");
			
			str = "Welcome lightMessage finished\n";
			logWrt.appF(str);
			
			return "Lights off message not displayed";
		}
		
		//Randy Richardson 08/05/2014 END
		
	}
	
	public void finishjapple() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome finishjapple started calls StartActivity");
		 
		str = "Welcome finishjapple started calls StartActivity\n";
		logWrt.appF(str);
		
		 Intent intent = new Intent(this, StartActivity.class);
		 
		 startActivity(intent);
		 
		 //Randy Richardson 08/05/2014 END
		
	}
	
	public char getfirst(String sn) {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome getFirst started");
		
		str = "Welcome getFirst started\n";
		logWrt.appF(str);
		
	char[] c = sn.toCharArray();
	char s = c[0];
	
	Log.d("Debug","Welcome getFirst finished");
	
	str = "Welcome getFirst finished\n";
	logWrt.appF(str);
	
	return s;
	
	//Randy Richardson 08/05/2014 END
	
	}
	
	public String getMonth(String ms) {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome getMonth started");
		
		str = "Welcome getMonth started\n";
		logWrt.appF(str);
		
		char[] c = ms.toCharArray();
		String a = "";
		for (int i = 0; i <3; i++) {
			a+=c[i];
		}
		//System.out.println(a + "hfgdhfd");
		
		Log.d("Debug","Welcome getMonth finished");
		
		str = "Welcome getMonth finished\n";
		logWrt.appF(str);
		
		return a;
		
		//Randy Richardson 08/05/2014 END
	}
	
	public String getYear(String ys) {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome getYear started");
		
		str = "Welcome getYear started\n";
		logWrt.appF(str);
		
		char[] c = ys.toCharArray();
		String a = "";
		for(int i = 0; i < 4; i++) {
			a+=c[c.length-4+i];
		}
		
		Log.d("Debug","Welcome getYear finished");
		
		str = "Welcome getYear finished\n";
		logWrt.appF(str);
		
		return a;
		
		//Randy Richardson 08/05/2014 END
	}
	
	public String getLastTwo(String StudioNumberWithoutFirstLetter) {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome getLastTwo started");
		
		str = "Welcome getLastTwo started\n";
		logWrt.appF(str);
		
		char[] c = StudioNumberWithoutFirstLetter.toCharArray();
		String s = "";
		for(int i = 1; i < 3; i++ ){
			s = s +c[i];
		}
		
		Log.d("Debug","Welcome getLastTwo finished");
		
		str = "Welcome getLastTwo finished\n";
		logWrt.appF(str);
		
		return s;
		
		//Randy Richardson 08/05/2014 END
	}
	
	public String[] getPins (String[] secs, int size) {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome getPins started");
		
		str = "Welcome getPins started\n";
		logWrt.appF(str);
		
		String[] b = new String[size];
		
		for(int xyz = 0; xyz < secs.length; xyz++) {
			//System.out.println("/"+secs[xyz]+"/");
			String currvalue = (secs[xyz].toString()).replaceAll("\\s","");
			SQLiteDatabase studioInfo = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
			//System.out.println("SELECT * from StudioInfo where studio_number = '"+currvalue+"'");
			Cursor sNCursor =  studioInfo.rawQuery("SELECT * from StudioInfo where studio_number = '"+currvalue+"'" , null);
			
			sNCursor.moveToFirst();
			
			if (sNCursor.getCount() > 0) {
			System.out.println("data inside table looks like this: /" + sNCursor.getString(sNCursor.getColumnIndexOrThrow("studio_number")) + "/");
			b[xyz] = sNCursor.getString(sNCursor.getColumnIndexOrThrow("pin"));
			} else {
				b[xyz] = "99999";
			}
			
			
			studioInfo.close();
		}
		//System.out.println("got pins");
		
		Log.d("Debug","Welcome getPins finished");
		
		str = "Welcome getPins finished\n";
		logWrt.appF(str);
		
		return b;
		
		//Randy Richardson 08/05/2014 END
	}
	
	public String parse(String[] sec) {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome parse started");
		
		str = "Welcome parse started\n";
		logWrt.appF(str);
		
		String query = "";
		for(int xyz = 0; xyz < sec.length; xyz++) {
			query = query + "or pin = '" + (sec[xyz]).toString() +"'";
		}
		
		Log.d("Debug","Welcome parse finished");
		
		str = "Welcome pars finished\n";
		logWrt.appF(str);
		
		return query;
		
		//Randy Richardson 08/05/2014 END
		
	}
	
	public boolean lastOneOut() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome lastOneOut started");
		
		str = "Welcome lastOneOut started\n";
		logWrt.appF(str);
		
		section = "";
		
		String[] sec1 = new String[]{"B47" , "B48" , "B49" , "B50" , "B51" , "B52" , "B53" , "B54" , "B55" , "B56" , "B57" , "B58" , "B59" , "B60" , "B61" , "B62" , "B63" , "B64" , "B65" , "B66" , "B67"};
		String[] sec2 = new String[]{"B07" , "B08" , "B12" , "B13" , "B17" , "B18" , "B23" , "B24" , "B29" , "B30" , "B36" , "B37" , "B42" , "B43"};
		String[] sec3 = new String[]{"B09" , "B11" , "B19" , "B20" , "B21" , "B22" , "B31" , "B32" , "B33" , "B34" , "B35" , "B44" , "B45" , "B46"};
		String[] sec4 = new String[]{"B05" , "B06" , "B10" , "B14" , "B15" , "B16" , "B25", "B26", "B27", "B28", "B38", "B39", "B40", "B41"};
		
		SQLiteDatabase login = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		
		//sn = getSN();
		
		
		

		String[] sec1Pins = getPins(sec1, sec1.length);
		String[] sec2Pins = getPins(sec2, sec2.length);
		String[] sec3Pins = getPins(sec3, sec3.length);
		String[] sec4Pins = getPins(sec4, sec4.length);
		
		Cursor loginCursor =  login.rawQuery("SELECT pin from LoginTable WHERE isLoggedIn = 'true'" , null);
			loginCursor.moveToFirst();
		
		
		
		String[] loggedInPins = new String[loginCursor.getCount()];
		//System.out.println(loginCursor.getCount());
			
		if(loginCursor.getCount() == 0) {
			section = "All of the Lights";
			
			Log.d("Debug","Welcome lastOneOut finished");
			
			str = "Welcome lastOneOut finished\n";
			logWrt.appF(str);
			
			return true;
		}
		
		else {
		
			for(int j = 0; j < loginCursor.getCount(); j++) {
				loggedInPins[j] = loginCursor.getString(loginCursor.getColumnIndexOrThrow("pin"));
				//System.out.println(loginCursor.getString(loginCursor.getColumnIndexOrThrow("pin")) + " this is the logged in pin its storing in licurs");
				loginCursor.moveToNext();
			}
			System.out.println("started section loops");
			System.out.println("Number of people logged in: "+loggedInPins.length);
			
			if(loop(sec1Pins, pin_num)) {
				System.out.println("started section 1 loop");
				checkSection = !loop2(sec1Pins, loggedInPins);
				if(checkSection) { section = "1"; }
			}

			if(loop(sec2Pins, pin_num)) {
				System.out.println("started section 2 loop");
				checkSection = !loop2(sec2Pins, loggedInPins);
				if(checkSection) { section = "2"; }
			}

			if(loop(sec3Pins, pin_num)) {
				System.out.println("started section 3 loop");
				checkSection = !loop2(sec3Pins, loggedInPins);
				if(checkSection) { section = "3"; }
			}

			if(loop(sec4Pins, pin_num)) {
				System.out.println("started section 4 loop");
				checkSection = !loop2(sec4Pins, loggedInPins);
				if(checkSection) { section = "4"; }
			}
			
			Log.d("Debug","Welcome lastOneOut finished");
			
			str = "Welcome lastOneOut finished\n";
			logWrt.appF(str);
			
			return checkSection;
		
		}
		
		//Randy Richardson 08/05/2014 END
		
	}
	
	public String getSN() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome getSN started");
		
		str = "Welcome getSN started\n";
		logWrt.appF(str);
		
		String sn = "";
		SQLiteDatabase studioInfo = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		Cursor sNCursor =  studioInfo.rawQuery("SELECT * from StudioInfo WHERE pin = '"+ pin_num + "'" , null);
		
		sNCursor.moveToFirst();
		sn = sNCursor.getString(sNCursor.getColumnIndexOrThrow("studio_number"));
		//System.out.println("got getSN");
		//System.out.println("Returns " + sn );
		studioInfo.close();
		
		Log.d("Debug","Welcome getSN finished");
		
		str = "Welcome getSN finished\n";
		logWrt.appF(str);
		
		return sn;
		
		//Randy Richardson 08/05/2014 END
	}
	
	
	public boolean loop(String[] cs, String pn) {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome loop started");
		
		str = "Welcome loop started\n";
		logWrt.appF(str);
		
		for(int i = 0; i < cs.length ; i++ ) {
			if (cs[i].equals(pn)) {
				return true;
			}
		}
		
		Log.d("Debug","Welcome loop finished ");
		
		str = "Welcome loop finished\n";
		logWrt.appF(str);
		
		return false;
		
		//Randy Richardson 08/05/2014 END 
		
	}	
	
	public boolean loop2(String[] cs, String[] pn) {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome loop2 started");
		
		str = "Welcome loop2 started\n";
		logWrt.appF(str);
		
		for(int i = 0; i < cs.length ; i++ ) {
			System.out.println("Looping through" + (i+1) + " times" );
			
			for(int jl = 0; jl < pn.length; jl++) {
				System.out.println("Checking " + pn[jl] + " against " + cs[i]);
				if (cs[i].equals(pn[jl])) {
					System.out.println("csi = pnjl" );
					
					Log.d("Debug","Welcome loop2 finished");
					
					str = "Welcome loop2 finished\n";
					logWrt.appF(str);
					
					return true;
				}
			}
		}
		
		Log.d("Debug","Welcome loop2 finished");
		
		str = "Welcome loop2 finished\n";
		logWrt.appF(str);
		
		return false;
		
		//Randy Richardson 08/05/2014 END
		
	}
	
	public double getHoursLogged() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","Welcome getHoursLogged started");

		str = "Welcome getHoursLogged started\n";
		logWrt.appF(str);
		
		double monthHours = 0;
		
		//get the current date
        Calendar cal = Calendar.getInstance();
        
        //get the current month into its own variable at 12am
        Calendar aCalendar = Calendar.getInstance();
	    aCalendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH) );
	    aCalendar.set(Calendar.DATE, 1);
	    aCalendar.set(Calendar.HOUR, 0);
	    aCalendar.set(Calendar.MINUTE, 0);
        
	    //convert the current month to datetime
        Date currentMonth = aCalendar.getTime();
        DateTime currentMonthDateTime = new DateTime(currentMonth);
        
        //Open the table with the logged time
		SQLiteDatabase logout = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		
		//Define the timestamp format from the databases
		DateTimeFormatter format = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss zzz yyyy");
		
		//get all the entries for the artist
	 	Cursor lo = logout.rawQuery("SELECT * from LogoutTable WHERE pin = " + pin_num , null);
	 	
	 	//Set the cursor to the first position in the returned array
	 	lo.moveToFirst();
	 	System.out.println("THE COUNT IS: " + lo.getCount());
		
		
		 for (int i = 0; i < lo.getCount(); i ++) {
			 //Get the timestamp from the current item
			 DateTime timestamp = format.parseDateTime(lo.getString(lo.getColumnIndex("timestamp")));
			 
			 //Check to see if the time from log is from the current year and is after the first of the month.
			 if (cal.get(Calendar.YEAR) == Integer.parseInt(getYear(lo.getString(lo.getColumnIndex("timestamp")))) 
					 && (timestamp.isAfter(currentMonthDateTime))) { 
				System.out.println("Added: " + lo.getString(lo.getColumnIndexOrThrow("totalminutes")));
			 	monthHours += Integer.parseInt(lo.getString(lo.getColumnIndex("totalminutes")));
			 }
			 else {
				monthHours +=0;
				System.out.println("added nothing because pin doesnt match");
			 }
			 lo.moveToNext();
		 }
		 
		 logout.close();
		 System.out.println("total minutes logged in is:  " + monthHours );
		 System.out.println("should've returned:  " + monthHours/60 );
		 
		 Log.d("Debug","Welcome getHoursLogged finished");
		 
		 str = "Welcome getHoursLogged finished\n";
		 logWrt.appF(str);
		 
		return monthHours;
		
		//Randy Richardson 08/05/2014 END
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}
	
	class stopz extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			
			//Randy Richardson 08/05/2014 START
			
			Log.d("Debug","Welcome stopz called doInBackground started");
			
			str = "Welcome stopz called doInBackground started\n";
			logWrt.appF(str);
			
			 try {
				 	Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			finishjapple();
			
			Log.d("Debug","Welcome stopz called doInBackground finished");
			
			str = "Welcome stopz called doInBackground finished\n";
			logWrt.appF(str);
			
			return null;
			
			//Randy Richardson 08/05/2014 END
		} 
	}

	//Randy Richardson 08/05/2014 END
	
}
