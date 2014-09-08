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
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class GuestLogin extends Activity {
	
	/*Randy Richardson 08/05/2014 START
	 * Instance of LogWrt class and a String used to save the Log*/
	
	LogWrt logWrt = new LogWrt();
	String str;
	
	//Randy Richardson 08/05/2014 END

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guest_login);
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","GuestLogin has been called");
		
		str = "StartActivity GuestLogin has been called\n";
		logWrt.appF(str);
		
		initButtons();
		String waiver = "By checking below, you represent, warrant, and agree: " +
"<p>a) that you are visiting or assisting the above artist on the above listed date during his/her occupancy;" +
"<p>b) to the fullest extent permitted by law, to indemnify and hold harmless the NYCEDC, the City of NewYork and Apple Industrial Development Corporation (referred to hereafter in combination as the �Landlord�) and chashama, inc. (referred to hereafter as �Licensor�) and their respective affiliates, officers, partners, agents, employees, servants and assignees from and against allliability claims and demands on account of injury to persons, including death resulting there from, and damage or loss to propertyarising out of the performance, or lack of performance by Licensee, Landlord, Licensor, their employees, agents or assigns."+ 
"<p>c) you agree to indemnify and hold harmless Landlord and Licensor from and against all claims, obligations, fines,liens, penalties, actions, damages, liabilities, costs, charges and expenses in connection with or arising from your visit."+
"<p>d) In the event that Landlord or Licensor is made a party to any litigation commenced by or against vendor, or arising fromthe acts and omissions of Licensee, then you shall indemnify, defend, and hold Licensor and Landlord harmlessthere from and shall pay all judgment, claims, damages, liabilities and litigation (including, without limit, attorneys� feesand disbursements) in connection with litigation, unless it is determined that Landlord or Licensor was solely negligent orbreached their responsibilities hereunder. The indemnity contained herein shall survive the termination of this agreement.";
		TextView waiver_view = (TextView) findViewById(R.id.waiver);
		waiver_view.setText(Html.fromHtml(waiver));
		
		//Randy Richardson 08/05/2014 END
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guest_login, menu);
		return true;
	}

	public void proceed() {
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","GuestLogin procceed started calls GuestInfo");
		
		str = "GuestLogin proceed started calls GuestInfo\n";
		logWrt.appF(str);
		
		Intent intent = new Intent(this, GuestInfo.class);
		startActivity(intent);
		
		//Randy Richardson 08/05/2014 END
	}
	
	public void initButtons(){
		
		//Randy Richardson 08/05/2014 START
		
		Log.d("Debug","GuestLogin initButtons started");
		
		str = "GuestLogin initButtons started\n";
		logWrt.appF(str);
		
		final CheckBox check = (CheckBox) findViewById(R.id.liabilityCheck);
		Button proceed = (Button) findViewById(R.id.proceed);
		
		

	
		proceed.setOnClickListener(new View.OnClickListener() {
			
			/////////////////////////////////////

			///////////on button click///////////////
			   public void onClick(View v) {
				   if(check.isChecked()) {
						proceed();
				   }
				   
				   else {
					   (Toast.makeText(getApplicationContext(), "Please Agree to Continue", Toast.LENGTH_LONG)).show();
				   }
			
			    }
			});
	
		Log.d("Debug","GuestLogin initButtons finished");
		
		str = "GuestLogin initButtons finished\n";
		logWrt.appF(str);
		
		//Randy Richardson 08/05/2014 END
	}
}

