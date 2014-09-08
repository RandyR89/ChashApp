package com.example.signin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

public class AdminPanel extends Activity {
	
	private ListView m_listview;
	public SQLiteDatabase sysVars_db;
	public SQLiteDatabase specialPins;
	public SQLiteDatabase db;
	public static String active;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_panel);
		m_listview = (ListView) findViewById(R.id.ap_procList);
		
		checkTable();
		initButtons();
		
		setListItems();
		active = "admin";
	    m_listview.setOnItemClickListener(new OnItemClickListener() 
	    {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				HashMap<String, String> item = (HashMap) m_listview.getItemAtPosition(position);
				
				Toast.makeText(getApplicationContext(),"You selected : " + item.get("name") ,Toast.LENGTH_SHORT).show();
				EditText numberToChange = (EditText)findViewById(R.id.ap_numberToChange);
				
				numberToChange.setText(getPin(item.get("name")));
			}

	    });
		
	}
	
	public void setListItems () {
		List<Map<String, String>> blah = getItems();
		
	    SimpleAdapter adapter2 = new SimpleAdapter(this, blah,
	    			android.R.layout.simple_list_item_2,
                    new String[] {"name", "status"},
                    new int[] {android.R.id.text1,
                               android.R.id.text2});
	    
	    
	    m_listview.setAdapter(adapter2);
	}
	
	public String getPin(String nameString){
		String name = nameString.substring((nameString.length()-5), nameString.length());
		return name;
	}
	
	public void checkTable() {
		//Make sure the table exists when you open the activity
		
		//Open the database
		specialPins = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		specialPins.execSQL("CREATE TABLE IF NOT EXISTS flaggedPins (id INTEGER PRIMARY KEY AUTOINCREMENT, pin INTEGER, status VARCHAR)");
		specialPins.close();
	}

	public List<Map<String, String>> getItems() {
		//Open the database
		db = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		//define the cursors
		Cursor flaggedPins = db.rawQuery("SELECT * from flaggedPins", null);
		Cursor studioInfo;
		//Define the vars
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		String firstname;
		String lastname;
		
		
		//move the cursor into position
		flaggedPins.moveToFirst();
		
		if (flaggedPins.getCount() != 0) {
			for (int i=0; i<flaggedPins.getCount(); i++){
				//Get the pin from the table
				String pinNum = flaggedPins.getString(flaggedPins.getColumnIndex("pin"));
				
				//get the relevant data from the studio info table
				studioInfo = db.rawQuery("SELECT * FROM StudioInfo WHERE pin ='" + pinNum +"';", null);
				studioInfo.moveToFirst();
				
				if(studioInfo.getCount()!=0) {
					firstname = studioInfo.getString(studioInfo.getColumnIndex("firstname"));
					lastname = studioInfo.getString(studioInfo.getColumnIndex("lastname"));
				} else {
					firstname = "invalid_first_name";
					lastname = "invalid_last_name";
				}
				//Add the info to the data stream
				Map<String, String> datum = new HashMap<String, String>(2);
		        datum.put("name", firstname + " " + lastname + " " + pinNum );
		        datum.put("status", flaggedPins.getString(flaggedPins.getColumnIndex("status")));
		        data.add(datum);
				
				//Move the cursor to the next item
				flaggedPins.moveToNext();
			}
			
			
			//close the database
			db.close();
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
	
	public void initButtons(){
		//////////////////buttons/////////////////////////
		Button setProcbtn = (Button) findViewById(R.id.ap_setProctor);
		setProcbtn.setOnClickListener(onClickListener);
		Button setChabtn = (Button) findViewById(R.id.ap_setChashama);//proceed button
		setChabtn.setOnClickListener(onClickListener);
		Button removebtn = (Button) findViewById(R.id.removeItem);//proceed button
		removebtn.setOnClickListener(onClickListener);
		Button quitBtn = (Button) findViewById(R.id.ap_quit);//back button
		quitBtn.setOnClickListener(onClickListener);
		Button viewLoggedBtn = (Button) findViewById(R.id.ap_btn_viewLogged);//back button
		viewLoggedBtn.setOnClickListener(onClickListener);
		Button updatebtn = (Button) findViewById(R.id.updateBtn);
		updatebtn.setOnClickListener(onClickListener);
		Button setlocationbtn = (Button) findViewById(R.id.setLocationBtn);
		setlocationbtn.setOnClickListener(onClickListener);

	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) { 
			
		///registers the buttons with their respective numbers///
			switch(v.getId()) {
				case R.id.ap_quit:
					active = "";
					Intent intent = new Intent(AdminPanel.this, StartActivity.class);
					startActivity(intent);
					break;
				case R.id.ap_setProctor:
					setPin("proctor");
					break;
				case R.id.ap_setChashama:
					setPin("chashama");
					break;
				case R.id.removeItem:
					removePin();
					break;
				case R.id.ap_btn_viewLogged:
					Intent logged_intent = new Intent(AdminPanel.this, AdminViewLogged.class);
					startActivity(logged_intent);					
					break;
				case R.id.updateBtn: forceUpdate();
				    break;
				case R.id.setLocationBtn: 
					setLocation();
			    	break;
					
		   }
		   ///////////////////////////////////////////////////////		   
	    }
	};
	
	public void forceUpdate() {
		Intent intent = new Intent(this, RepeatingAlarm.class);
		intent.putExtra("adminForce", "admin");
		startActivity(intent);
	}
	
	public void removePin () {
		String pinNum = "";
		EditText numberToChange = (EditText)findViewById(R.id.ap_numberToChange);
		pinNum = numberToChange.getText().toString();
		
		specialPins = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		
		Log.d("setProctor", "removing proctor for pin: " + pinNum );
        
		//flag the pin in the database
		specialPins.execSQL("DELETE FROM flaggedPins WHERE pin = '" + pinNum + "';");
		
		//Tell the user they set the proctor
		Toast.makeText(getApplicationContext(), "Removed " + pinNum + " from list" , Toast.LENGTH_SHORT).show();
		
		//close the database and clear the text
		specialPins.close();
		numberToChange.setText("");
		
		setListItems();
	
	}
	
	public void setLocation () {
		//Grab the location to store from the text box
		String location;
		EditText locationToStore = (EditText)findViewById(R.id.ap_numberToChange);
		location = locationToStore.getText().toString();
		
		
		//open the vars db
		sysVars_db = openOrCreateDatabase("System", MODE_PRIVATE, null);
		
		//Set the vars
		sysVars_db.execSQL("UPDATE SystemVars SET location ='" + location + "' WHERE id =1");
		
		Toast.makeText(getApplicationContext(), "Set Location", Toast.LENGTH_SHORT).show();
		
		sysVars_db.close();
	}
	
	public void setPin (String status) {
		//Grab the pin number to flag from the text box
		String pinNum = "";
		EditText numberToChange = (EditText)findViewById(R.id.ap_numberToChange);
		pinNum = numberToChange.getText().toString();
		
		if (!pinNum.equals("") && pinNum.length() > 4 && pinNum.length() < 6){
			//If the pin number isnt blank or less than 5 characters
			
			//Check to see if the database exists yet
			checkTable();
			
			//Open the database
			specialPins = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
			
			Log.d("setProctor", "Setting proctor for pin: " + pinNum );
	        
			//flag the pin in the database
			if(!pinNum.equals("Blank")) {
			specialPins.execSQL("INSERT INTO flaggedPins VALUES" +	"(" +  "NULL, " + "'"+ pinNum +"', '" + status +"');");
			
			//Tell the user they set the proctor
			Toast.makeText(getApplicationContext(), "Set " + pinNum + " as a " + status , Toast.LENGTH_SHORT).show();
			}
			
			//close the database and clear the text
			specialPins.close();
			numberToChange.setText("");
			
			setListItems();
			
			
			
			
		} else {
			//Clear the pin and send a warning toast
			
			Toast.makeText(getApplicationContext(), "Incorrect Pin length", Toast.LENGTH_SHORT).show();
		
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_panel, menu);
		return true;
	}

}
