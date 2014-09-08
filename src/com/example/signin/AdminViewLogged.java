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
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class AdminViewLogged extends Activity {
	//Define the activity vars
	private ListView m_listview;
	public SQLiteDatabase specialPins;
	public SQLiteDatabase db;
	public SQLiteDatabase login;
	public SQLiteDatabase logout;
	public Calendar cal = Calendar.getInstance();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_view_logged);
		
		m_listview = (ListView) findViewById(R.id.ap_ViewLoggedList);
		
		//checkTable();
		initButtons();
		
		setListItems();
		
	    m_listview.setOnItemClickListener(new OnItemClickListener() 
	    {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				HashMap<String, String> item = (HashMap) m_listview.getItemAtPosition(position);
				
				Toast.makeText(getApplicationContext(),"You selected : " + item.get("name") ,Toast.LENGTH_SHORT).show();
				EditText numberToChange = (EditText)findViewById(R.id.admin_view_text_loginPin);
				
				numberToChange.setText(getPin(item.get("name")));
			}

	    });
	    
	    
	    
	    
		
		
	}
	
	public void setListItems() {
		List<Map<String, String>> blah = getItems();
		
	    SimpleAdapter adapter2 = new SimpleAdapter(this, blah,
	    			android.R.layout.simple_list_item_2,
                    new String[] {"name", "status"},
                    new int[] {android.R.id.text1,
                               android.R.id.text2});
	    
	    
	    m_listview.setAdapter(adapter2);
	}
	
	public void initButtons(){
		//////////////////buttons/////////////////////////
		Button setLogoffbtn = (Button) findViewById(R.id.admin_btn_logoff);
		setLogoffbtn.setOnClickListener(onClickListener);
		Button setReturnbtn = (Button) findViewById(R.id.admin_btn_return);//proceed button
		setReturnbtn.setOnClickListener(onClickListener);
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) { 
			
		///registers the buttons with their respective numbers///
			switch(v.getId()) {
				case R.id.admin_btn_return:
					Intent intent = new Intent(AdminViewLogged.this, AdminPanel.class);
					startActivity(intent);
					break;
				case R.id.admin_btn_logoff:
					logout();
					break;
					
		   }
		   ///////////////////////////////////////////////////////		   
	    }
	};
	
	public String getPin(String nameString){
		String name = nameString.substring((nameString.length()-5), nameString.length());
		return name;
	}

	public List<Map<String, String>> getItems() {
		//Open the databases
		login = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		logout = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		db = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		
		
		//define the cursors
		Cursor login_cursor = login.rawQuery("SELECT * from LoginTable WHERE isLoggedIn = 'true'", null);
		Cursor studioInfo;
		
		//Define the vars
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		String firstname;
		String lastname;
		String timestamp = (cal.getTime().toString());
		
		
		//move the cursor into position
		login_cursor.moveToFirst();
		
		if (login_cursor.getCount() != 0) {
			for (int i=0; i<login_cursor.getCount(); i++){
				//Get the pin from the table
				String pinNum = login_cursor.getString(login_cursor.getColumnIndex("pin"));
				
				//get the relevant data from the tables
				studioInfo = db.rawQuery("SELECT * FROM StudioInfo WHERE pin ='" + pinNum +"';", null);
				studioInfo.moveToFirst();
				
				//Check to see if the records are found
				if (studioInfo.getCount() > 0) {
					firstname = studioInfo.getString(studioInfo.getColumnIndex("firstname"));
					lastname = studioInfo.getString(studioInfo.getColumnIndex("lastname"));
				} else {
					firstname = "invalid";
					lastname = "invalid";
				}
				
				//get elapsed time since logon
				DateTimeFormatter format = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss zzz yyyy");
				org.joda.time.DateTime currentTime = format.parseDateTime(timestamp);
				org.joda.time.DateTime loginTime = format.parseDateTime(login_cursor.getString(login_cursor.getColumnIndex("timestamp")));
				Period elapsed = new Period (loginTime, currentTime);	
				
				
				
				//Add the info to the data stream
				Map<String, String> datum = new HashMap<String, String>(2);
		        datum.put("name", firstname + " " + lastname + " " + pinNum );
		        datum.put("status", "Logged in for: " + cleanNumber(elapsed.toStandardMinutes().toString()) + " minutes since " + loginTime );
		        data.add(datum);
				
				//Move the cursor to the next item
		        login_cursor.moveToNext();
			}
			
			
			//close the database
			db.close();
			login.close();
			logout.close();
			return data; 
		} else {
			//Populate the empty data list so it doesnt error
			Map<String, String> datum = new HashMap<String, String>(2);
	        datum.put("name", "Blank" );
	        datum.put("status", "Blank");
	        data.add(datum);
			
			Toast.makeText(getApplicationContext(), "No Current Proctors or Employees listed", Toast.LENGTH_SHORT).show();
			return data;
		}

	}
	
	public String cleanNumber(String numbertoclean){
		String cleaned ="";
		cleaned = numbertoclean.replaceAll("[^\\d.]", "");
		return cleaned;
	}
	
	public void logout() {
		//Log out the person indicated in the entry
		//Open the databases
		login = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		logout = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);

		//define the cursors
		Cursor login_cursor = login.rawQuery("SELECT * from LoginTable WHERE isLoggedIn = 'true'", null);
		login_cursor.moveToFirst();
		
		String timestamp = (cal.getTime().toString());
		String pinNum = "";
		EditText numberToChange = (EditText)findViewById(R.id.admin_view_text_loginPin);
		pinNum = numberToChange.getText().toString();
		
		//grab the elapsed time since login
		DateTimeFormatter format = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss zzz yyyy");
		org.joda.time.DateTime currentTime = format.parseDateTime(timestamp);
		org.joda.time.DateTime loginTime = format.parseDateTime(login_cursor.getString(login_cursor.getColumnIndex("timestamp")));
		Period elapsed = new Period (loginTime, currentTime);	
		
		
		String entry = ("(NULL, '" + pinNum + "','" + timestamp + "','" + 
				cleanNumber(elapsed.toStandardMinutes().toString())  + "','" + login_cursor.getString(login_cursor.getColumnIndex("id")) + "','" + login_cursor.getColumnIndex("timestamp") +"', 0)");
		
		//Run the query that logs the person out

        logout.execSQL("INSERT INTO LogoutTable VALUES" + entry + ";");
        
        //Set the persons login entry to logged out        
        login.execSQL("DELETE FROM LoginTable WHERE pin = '" + pinNum + "' AND isLoggedIn='true'");
        
        //Close the databases
        logout.close();
        login.close();
        
        Toast.makeText(getApplicationContext(), "Force logged off user with ID of " + pinNum, Toast.LENGTH_SHORT).show();
        numberToChange.setText("");

		setListItems();
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_view_logged, menu);
		return true;
	}

}
