package skylife.android.serial;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import skylife.android.log.Log;

public class SerialParser {

	private SerialMain serialMain;
	private Vector<Object> infoVec;
	private String[][] manufacture;
	private String[][] manufacture_PID;
	private String[][] model;
	
	public SerialParser(SerialMain serialMain)
	{
		this.serialMain = serialMain;
		this.setManufacture();
		this.setManufacture_PID();
		this.setModel();
		
		infoVec = new Vector<Object>();
		

	}
	
	public void setClearVec()
	{
		this.infoVec.removeAllElements();
	}
	
	public void requestParsing(String message)
	{
		synchronized(this){
			
			if( message.contains("manufacturerId"))
			{
				infoVec.add(this.requetManufacture((message.split("\\=")[1]).trim()));
				infoVec.add((message.split("\\=")[1]).trim());
			}
			if( message.contains("modelId"))
			{
				infoVec.add(this.requestModel((message.split("\\=")[1]).trim()));
				infoVec.add((message.split("\\=")[1]).trim());
			}
			if( message.contains("imageVersion"))
			{
				infoVec.add((message.split("\\=")[1]).trim());
			}
			if( message.contains("downloadPID"))
			{
				infoVec.add((message.split("\\=")[1]).trim());
				
				//reset manufactureID
				infoVec.setElementAt((this.requetManufacture_PID((message.split("\\=")[1]).trim())), 0);
				
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				infoVec.add(df.format(cal.getTime()));
				
				this.serialMain.setTableRow(infoVec);
				
			}
		}
	
	}
	
	
	
	public String requetManufacture(String message){
		
		String id = (message.split("\\(")[0]).trim();
		String returnMsg = "Factory TBD";
		
		for( int cnt = 0 ; cnt < this.manufacture.length ; cnt++ ){
			
			if( Integer.parseInt(id) == Integer.parseInt(this.manufacture[cnt][0], 16) ){
				returnMsg = this.manufacture[cnt][1];
				break;
			}
		}
		
		/***********
		if( Integer.parseInt(id) == Integer.parseInt("20", 16) ){
			returnMsg = "DMT";
		}
		if( Integer.parseInt(id) == Integer.parseInt("21", 16) ){
			returnMsg = "Celrun";
		}
		if( Integer.parseInt(id) == Integer.parseInt("40", 16) ){
			returnMsg = "Humax";
		}
		if( Integer.parseInt(id) == Integer.parseInt("60", 16) ){
			returnMsg = "LG";
		}
		if( Integer.parseInt(id) == Integer.parseInt("61", 16) ){
			returnMsg = "Kaon";
		}
		if( Integer.parseInt(id) == Integer.parseInt("A0", 16) ){
			returnMsg = "Samsung";
		}
		/*************/
		Log.log(this, returnMsg);
		
		return returnMsg;
		
	}

	public String requetManufacture_PID(String message){
		
		String id = (message.split("\\(")[0]).trim();
		String returnMsg = "Factory TBD";
		try{
			for( int cnt = 0 ; cnt < this.manufacture_PID.length ; cnt++ ){
				
				//if( Integer.parseInt(id) == Integer.parseInt(this.manufacture[cnt][0], 16) ){
				if( Integer.parseInt(id) == Integer.parseInt(this.manufacture_PID[cnt][0]) ){
					returnMsg = this.manufacture_PID[cnt][2];
					break;
				}
			}
			
			/***********
			if( Integer.parseInt(id) == Integer.parseInt("20", 16) ){
				returnMsg = "DMT";
			}
			if( Integer.parseInt(id) == Integer.parseInt("21", 16) ){
				returnMsg = "Celrun";
			}
			if( Integer.parseInt(id) == Integer.parseInt("40", 16) ){
				returnMsg = "Humax";
			}
			if( Integer.parseInt(id) == Integer.parseInt("60", 16) ){
				returnMsg = "LG";
			}
			if( Integer.parseInt(id) == Integer.parseInt("61", 16) ){
				returnMsg = "Kaon";
			}
			if( Integer.parseInt(id) == Integer.parseInt("A0", 16) ){
				returnMsg = "Samsung";
			}
			/*************/
			Log.log(this, returnMsg);
		}catch( Exception ex )
		{
			Log.errorLog(this, ex.toString());
			return null;
		}
		return returnMsg;
		
	}	
	
	public String requestModel(String message)
	{
		String id = (message.split("\\(")[0]).trim();
		String returnMsg = "Model TBD";
		
		for( int cnt = 0 ; cnt < this.model.length ; cnt++ ){
		
			if( Integer.parseInt(id) == Integer.parseInt(this.model[cnt][0], 16) ){
				returnMsg = this.model[cnt][1];
				break;
			}
			/************
			if( Integer.parseInt(id) == Integer.parseInt("20", 16) ){
				returnMsg = "KT하이브리드";
			}
			if( Integer.parseInt(id) == Integer.parseInt("30", 16) ){
				returnMsg = "안드로이드";
			}
			if( Integer.parseInt(id) == Integer.parseInt("14", 16) ){
				returnMsg = "UHD";
			}
			/************/
			
		}
		Log.log(this, returnMsg);
		return returnMsg;
	}
	
	private void setManufacture()
	{
		FileInputStream fin = null;
		BufferedReader buff = null;
		Vector imVec = new Vector();
		try {
			fin = new FileInputStream(Log.MAINDIR+Log.ManufactureFILE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.errorLog(this, e.toString());
		}
		
		try {
			buff = new BufferedReader( new InputStreamReader( fin,"UTF-8" ));
			String temp = "";
			int clm = 0;
			while( (temp = buff.readLine()) != null){
				clm = temp.split("\\|").length;
				imVec.add(temp);
			}
			
			this.manufacture = new String[imVec.size()][clm];
			//buff = new BufferedReader( new InputStreamReader( fin ));
			//System.out.println("test");
			//while( (temp = buff.readLine()) != null){
			for( int cnt = 0 ; cnt < imVec.size() ; cnt++ ){
				
				for( int scnt = 0 ; scnt < clm ; scnt++ ){
					//this.manufacture[row][cnt] = temp.split("|")[cnt];
					this.manufacture[cnt][scnt] = imVec.elementAt(cnt).toString().split("\\|")[scnt];
					//System.out.println("row = " + row + " clm = " + clm + " this.manufacture[row][scnt] = " + this.manufacture[row][scnt]);
				}
			}
			
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			Log.errorLog(this, ex.toString());
		}
		
		try{
			buff.close();
			fin.close();
		}
		catch(Exception eex)
		{
			Log.errorLog(this, eex.toString());
		}
		
	}
	private void setManufacture_PID()
	{
		FileInputStream fin = null;
		BufferedReader buff = null;
		Vector imVec = new Vector();
		try {
			fin = new FileInputStream(Log.MAINDIR+Log.ManufactureFILE_PID);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.errorLog(this, e.toString());
		}
		
		try {
			buff = new BufferedReader( new InputStreamReader( fin,"UTF-8" ));
			String temp = "";
			int clm = 0;
			while( (temp = buff.readLine()) != null){
				clm = temp.split("\\|").length;
				imVec.add(temp);
			}
			
			this.manufacture_PID = new String[imVec.size()][clm];
			//buff = new BufferedReader( new InputStreamReader( fin ));
			//System.out.println("test");
			//while( (temp = buff.readLine()) != null){
			for( int cnt = 0 ; cnt < imVec.size() ; cnt++ ){
				
				for( int scnt = 0 ; scnt < clm ; scnt++ ){
					//this.manufacture[row][cnt] = temp.split("|")[cnt];
					this.manufacture_PID[cnt][scnt] = imVec.elementAt(cnt).toString().split("\\|")[scnt];
					//System.out.println("row = " + row + " clm = " + clm + " this.manufacture[row][scnt] = " + this.manufacture[row][scnt]);
				}
			}
			
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			Log.errorLog(this, ex.toString());
		}
		
		try{
			buff.close();
			fin.close();
		}
		catch(Exception eex)
		{
			Log.errorLog(this, eex.toString());
		}
		
	}
	
	private void setModel()
	{
		FileInputStream fin = null;
		BufferedReader buff = null;
		Vector imVec = new Vector();
		try {
			fin = new FileInputStream(Log.MAINDIR+Log.ModelFILE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.errorLog(this, e.toString());
		}
		
		try {
			buff = new BufferedReader( new InputStreamReader( fin,"UTF-8" ));
			String temp = "";
			int clm = 0;
			while( (temp = buff.readLine()) != null){
				clm = temp.split("\\|").length;
				imVec.add(temp);
			}
			
			this.model = new String[imVec.size()][clm];
			//buff = new BufferedReader( new InputStreamReader( fin ));
			for( int cnt = 0 ; cnt < imVec.size() ; cnt++ ){
				
				for( int scnt = 0 ; scnt < clm ; scnt++ ){
					this.model[cnt][scnt] = imVec.elementAt(cnt).toString().split("\\|")[scnt];
					//System.out.println("row = " + row + " clm = " + clm + " this.model[row][scnt] = " + this.model[row][scnt]);
				}
				
			}
			
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			Log.errorLog(this, ex.toString());
		}
		
		try{
			buff.close();
			fin.close();
		}
		catch(Exception eex)
		{
			Log.errorLog(this, eex.toString());
		}
		
	}	
	

	
}
