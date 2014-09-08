package com.example.signin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class RepeatingAlarm extends Activity {
	
	public SQLiteDatabase login;
	public SQLiteDatabase logout;
	public int length = 0;
	public String[] flags;
	public Cursor li;
	public static String last_update_string;
	public SQLiteDatabase last_update_db;
	
	

	
		@Override
		public void onCreate(Bundle savedInstanceState) {
			
			
			
			
			 super.onCreate(savedInstanceState);
				
				last_update_db = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
				last_update_db.execSQL("CREATE TABLE IF NOT EXISTS updatetable(id INTEGER PRIMARY KEY AUTOINCREMENT, lastupdate DATETIME)");
				//last_update_db.close();
				Cursor updateCursor = last_update_db.rawQuery("SELECT * from updatetable", null);
				updateCursor.moveToLast();
				
			 if(updateCursor.getCount() != 0) {
				last_update_string = updateCursor.getString(updateCursor.getColumnIndex("lastupdate"));
			}
			 	last_update_db.close();
			 
			 Calendar cal = Calendar.getInstance();
			 SQLiteDatabase pushLogin = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
			 li = pushLogin.rawQuery("SELECT * from LoginTable", null);
			Log.d("alarm", "alarm worked noob"); 

	   		new myAlarmz().execute();

			Intent intent = new Intent(this, StartActivity.class);
			startActivity(intent);
		   
			
		}
		public void pushData() throws JSONException, UnsupportedEncodingException {

			 Calendar cal = Calendar.getInstance();
		 		

		 	    SQLiteDatabase pushdb = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		 	    SQLiteDatabase pushLogin = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		 	    SQLiteDatabase pushLogout = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		 	    SQLiteDatabase updatedb = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);

		 	    pushLogin = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		 		pushLogin = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		 		last_update_db = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		 		
		 		
		 		HttpParams httpParameters = new BasicHttpParams();
		 		int timeoutConnection = 3000;
		 		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		 		int timeoutSocket = 5000;
		 		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		 		
		 		System.out.println("problem with url");
		 		String response2 = null;
		 		DefaultHttpClient client = new DefaultHttpClient();
		 		//HttpPost httppost2 = new HttpPost("http://10.242.11.143/update_tables.php");
		 		HttpPost httppost2 = new HttpPost("http://php.chashama.info/update_tables.php");
		 		HttpPost httppostUpdates = new HttpPost("http://php.chashama.info/update_visit.php");
		 		
		 		
		 		httppostUpdates.setParams(httpParameters);
		 		httppost2.setParams(httpParameters);

		 	     Cursor uc = last_update_db.rawQuery("SELECT * from updatetable", null);
		 	     

		 	     uc.moveToLast();
		 	     Cursor li = pushLogin.rawQuery("SELECT * from LoginTable WHERE timestamp >='" + uc.getString(uc.getColumnIndex("lastupdate")) +"'", null);
		 	     Cursor lo = pushLogout.rawQuery("SELECT * from LogoutTable WHERE timestamp >='" + uc.getString(uc.getColumnIndex("lastupdate")) +"'", null);
		 	     Cursor visitors = updatedb.rawQuery("SELECT * from GuestUpdate WHERE pushed = '0'", null);
		 	     li.moveToFirst();
		 	     lo.moveToFirst();
		 	     visitors.moveToFirst();
	
		 	  
		 	     
		 	     System.out.println("pushdata activated");
		 	    Log.d("pushdata", "Login Cursor is " + li.getCount() + " And Logout is " + lo.getCount() );
		 	     
		 	     for (int a = 0; a < li.getCount(); a++) {
		 	    	System.out.println("got for");
		 	    	List nvps = new ArrayList();
		 		    	nvps.add(new BasicNameValuePair("username", "kario"));
		 				nvps.add(new BasicNameValuePair("password", "password"));	
		 				nvps.add(new BasicNameValuePair("login", li.getString(li.getColumnIndex("timestamp"))));
		 				nvps.add(new BasicNameValuePair("logout", lo.getString(lo.getColumnIndex("timestamp"))));
		 				nvps.add(new BasicNameValuePair("PIN", li.getString(li.getColumnIndex("pin"))));
		 				nvps.add(new BasicNameValuePair("totalminutes", lo.getString(lo.getColumnIndex("totalminutes"))));
		 				
		 				System.out.println("worked " + Integer.toString(a) + " times");
		 				
		 				lo.moveToNext();
		 				li.moveToNext();
		 				UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
		 				httppost2.setEntity(p_entity);
		 			
		 				try {
		 					Log.d("response", "Init http request");
		 					ResponseHandler<String> responseHandler = new BasicResponseHandler();
		 					Log.d("response", "Get response for studio info data push");
		 					
		 					response2 = client.execute(httppost2, responseHandler);
		 					Log.d("response", "Got response, printing");
		 					//response.getEntity().consumeContent();
		 					Log.d("response", response2.toString());
		 			
		 				} catch (ClientProtocolException e) {
		 					e.printStackTrace();
		 				} catch (IOException e) {
		 					e.printStackTrace();
		 				}
		 	   
		 		 }
		 	     
		 	     
		 	    //cycle through the visitor table, push the data, and mark them as pushed if successful response
		 	    for (int i= 0; i < visitors.getCount(); i++ ) {
		 	    	//Make the data into a name value pair for transmission
		 	    	List nvps = new ArrayList(); 
	 		    	nvps.add(new BasicNameValuePair("username", "kario"));
	 				nvps.add(new BasicNameValuePair("password", "password"));	
	 				nvps.add(new BasicNameValuePair("visName", visitors.getString(visitors.getColumnIndex("visName"))));
	 				nvps.add(new BasicNameValuePair("visAddress", visitors.getString(visitors.getColumnIndex("visAddress"))));
	 				nvps.add(new BasicNameValuePair("artistName", visitors.getString(visitors.getColumnIndex("artistName"))));
	 				nvps.add(new BasicNameValuePair("purpose", visitors.getString(visitors.getColumnIndex("purpose"))));
	 		    	nvps.add(new BasicNameValuePair("phone", visitors.getString(visitors.getColumnIndex("phone"))));
	 				nvps.add(new BasicNameValuePair("email", visitors.getString(visitors.getColumnIndex("email"))));
	 				nvps.add(new BasicNameValuePair("studioNumber", visitors.getString(visitors.getColumnIndex("studioNumber"))));
	 				
	 				
	 				
	 				
	 				//HTTP request time
	 				UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
	 				httppost2.setEntity(p_entity);
	 				
	 				//Try the query and get a response
	 				try {
	 					Log.d("response", "Init http request");
	 					ResponseHandler<String> responseHandler = new BasicResponseHandler();
	 					Log.d("response", "Get response for guest info data send");
	 					
	 					response2 = client.execute(httppost2, responseHandler);
	 					Log.d("response", "Got response, printing");
	 					//response.getEntity().consumeContent();
	 					Log.d("response", response2.toString());
	 					if (response2.toString().equals("connected successfully")) {
	 						Log.d("response", "Set the guest info as checked");
	 						updatedb.execSQL("UPDATE GuestUpdate SET pushed='1' WHERE email = '" + visitors.getString(visitors.getColumnIndex("email")) + "'");
	 					} else {
	 						Log.d("response", "Guest info was not marked as sent");
	 					}
	 						
	 					
	 					
	 			
	 				} catch (ClientProtocolException e) {
	 					e.printStackTrace();
	 				} catch (IOException e) {
	 					e.printStackTrace();
	 				}
	 				//iterate the cursor
	 				visitors.moveToNext();
	 				
	 				
		 	    }
		 	     
		 	     
		 	    last_update_db.close();
		 	    updatedb.close();
		 	    pushLogin.close();
		 	    pushLogout.close();
		 	    pushdb.close();
		    }
		
		
		 public void logThemOut(Cursor licurs) {
			 length = 0;
			   
			 	//Toast.makeText(getApplicationContext(), "DEVICE IS UPDATING", Toast.LENGTH_LONG).show();
			 	SQLiteDatabase pushLogin = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);	
			 	pushLogin = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		    
		    	 Calendar cal = Calendar.getInstance();
				 licurs = pushLogin.rawQuery("SELECT * from LoginTable WHERE isLoggedIn = 'true'", null);
				 licurs.moveToFirst();
		    	
		    	for(int f = 0; f<licurs.getCount(); f++) {
		    	    System.out.println("checked login status:" + licurs.getString(licurs.getColumnIndex("isLoggedIn")));
					length++;
					licurs.moveToNext();
		    	}
		    	
		    	
		    	flags = new String[length];
		    	System.out.println("length = " + Integer.toString(length));
		    	
		    	
		    	licurs.moveToFirst();
		    	
					for(int x = 0; x < length; x++) {
						
						 System.out.println(licurs.getString(licurs.getColumnIndex("isLoggedIn")));
						
						 String pin = licurs.getString(licurs.getColumnIndex("pin")); 
					 	 String timestamp = (cal.getTime().toString());
					 	 
	 					 DateTimeFormatter format = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss zzz yyyy");
					 	 org.joda.time.DateTime currentTime = format.parseDateTime(timestamp);
 						 org.joda.time.DateTime loginTime = format.parseDateTime(licurs.getString(licurs.getColumnIndex("timestamp")));
					 	 Period elapsed = new Period (loginTime, currentTime);
					 	 System.out.println("GOT TO FOR");
	 					
						 logout(("(NULL, '" + pin + "','" + timestamp + "','" + Integer.toString(elapsed.getMinutes())  + "','" + licurs.getString(licurs.getColumnIndex("id")) +"')"), licurs, licurs.getString(licurs.getColumnIndex("id"))); 
						 System.out.println("logged someone out");	
							
							
						 flags[x] = "( NULL, '" + pin + "', " + "'" + timestamp + "'," +/* Long.toString(System.currentTimeMillis())+*/ "'true', 0)";
						 
							//if (licurs.getCount() != li.getCount()){
						 licurs.moveToNext();
							//}
					}
					//li = licurs;
					pushLogin.close();
				 }
			
			public void logThemBackIn() {
				System.out.println("logged someone back in");
				for (int count = 0; count < length; count++) {
					login(flags[count]);
					System.out.println("logging in " + count+1);
				}
				Arrays.fill(flags, null);
				//Toast.makeText(getApplicationContext(), "TABLET HAS BEEN UPDATED", Toast.LENGTH_LONG).show();

			}
			
			public void logout(String entry, Cursor loginCursor, String refid) {
				login = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
				logout = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		        logout.execSQL("INSERT INTO LogoutTable VALUES" + entry + ";");
		        
		       //Cursor logoutCursor =  logout.rawQuery("SELECT * from LogoutTable", null);;
		       //logoutCursor.moveToFirst();
		       
				login.execSQL("UPDATE LoginTable SET isLoggedIn='false' WHERE id = '" + refid + "'");
		        logout.close();
		        login.close();
			}
			
			public void login(String entry) {
				login = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
		        login.execSQL("INSERT INTO LoginTable VALUES" + entry + ";");
		        login.close();
			}
			
		   public void last_update_function(String date) {
				
				last_update_db = openOrCreateDatabase("studioApp_db", MODE_PRIVATE, null);
				last_update_db.execSQL("CREATE TABLE IF NOT EXISTS updatetable(id INTEGER PRIMARY KEY AUTOINCREMENT, lastupdate DATETIME)");
				last_update_db.execSQL("INSERT INTO updatetable VALUES(NULL,'" + date + "');");
				last_update_db.close();
	            
				
			}
			
	

class myAlarmz extends AsyncTask<Void,Void,Void>{

	@Override
	protected Void doInBackground(Void... params) {
		
		try {
			Calendar cal = Calendar.getInstance();
			System.out.println("HOUR: " + cal.get(Calendar.HOUR_OF_DAY));
			System.out.println("DAY: " + cal.get(Calendar.DAY_OF_WEEK));
			System.out.println("MINUTE: " + cal.get(Calendar.MINUTE));
			System.out.println("ToF: " + (cal.get(Calendar.MINUTE) %2 == 0));
			
			
			String admin = AdminPanel.active.toString();
			Log.d("Admin", "is admin is " + admin);
			
			
			if(cal.get(Calendar.HOUR_OF_DAY) < 23 && cal.get(Calendar.HOUR_OF_DAY) > 17 && cal.get(Calendar.DAY_OF_WEEK) == 1 || admin.equals("admin")) {
			//if(cal.get(Calendar.MINUTE)%2 == 0) {
			//Toast.makeText(getApplicationContext(), "DEVICE IS UPDATING", Toast.LENGTH_LONG).show();
			logThemOut(li);
			pushData();
			
			
			last_update_function(cal.getTime().toString());
			
			System.out.println("pushed data");
			logThemBackIn();
			//Toast.makeText(getApplicationContext(), "TABLET HAS BEEN UPDATED", Toast.LENGTH_LONG).show();
			
			
			} else {
				System.out.println("This aint sunday and you aint no admin");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AdminPanel.active = "";
		return null;
	} 
	
	
	
}
}
