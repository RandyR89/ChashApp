/*Sign In Log of changes
 * 
 * =================================================================================================
 * Programmer: Randy Richardson
 * Date: 08/05/2014
 * 
 * Purpose:  This class was originally created to create a Logfile in which the applications patterns 
 * 				can be tracked without connecting the utilized device to an IDE.  The text file created
 * 				here will be stored on the utilized devices external storage, and overwritten with each
 * 				new instance of the application.  It is important to create a copy of the LogFile to track
 * 				new instances of the application.
 * =================================================================================================
*/


package com.example.signin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import android.os.Environment;


public class LogWrt 
{

	File SignIn0 = Environment.getExternalStorageDirectory();
	FileWriter wrt ;
	BufferedWriter bwrt;
	File SignIn1;
	
	public LogWrt ()
	{
		this.SignIn1 = new File (SignIn0, "SignIn_Log.txt");
		
	}
	
	protected void appF (String str)
	{
		try
		{
			wrt = new FileWriter(this.SignIn1);
			bwrt = new BufferedWriter(wrt);
			bwrt.append("\n" + str);
			bwrt.close();
		}
		
		catch(Exception e)
		{
			
		}
	}
	
	
} 
