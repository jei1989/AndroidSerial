package skylife.android.log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {

	public static String MAINDIR = "";
	public static String LOGDIR ="\\logs";
	public static String RESDIR = "\\result";
	public static String PROPFILE = "\\prop";
	public static String ManufactureFILE = "\\Manufacture";
	public static String ManufactureFILE_PID = "\\Manufacture_PID";
	public static String ModelFILE = "\\Model";
	
	public static void log(Object obj, String message)
	{
		System.out.println(obj + " : " + message);
		commonWrite(obj, message, "log");
	}
	
	public static void errorLog(Object obj, String message)
	{
		System.out.println(obj + " : " + message);
		commonWrite(obj, message, "error");
	}
	
	public static void captureLog(Object obj,String message){
		commonWrite(obj,message,"capture");
	}
	
	public static void commonWrite(Object obj, String message, String type)
	{
		existDirCHK();	
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if( type.equals("signal")){
			message = df.format(cal.getTime())+" " + message;
		}else{
			message = "["+df.format(cal.getTime())+"] " + message;
		}
		
		SimpleDateFormat dfname = new SimpleDateFormat("yyyy-MM-dd");
		FileWriter fos = null;
		try{
			if( type.equals("log")){
				fos = new FileWriter(MAINDIR+LOGDIR+ "\\" + String.valueOf(dfname.format(cal.getTime()))+"_log",true );
			}else if( type.equals("error")){
				fos = new FileWriter(MAINDIR+LOGDIR+ "\\" + String.valueOf(dfname.format(cal.getTime()))+"_error",true );
			}else if( type.equals("capture")){
				fos = new FileWriter(MAINDIR+LOGDIR+ "\\" + String.valueOf(dfname.format(cal.getTime()))+"_capture",true );
			}else{
				fos = new FileWriter(MAINDIR+LOGDIR+ "\\" + String.valueOf(dfname.format(cal.getTime()))+"_"+type,true );
			}
					
			fos.write(message+"\r\n");
			fos.close();
		}catch(Exception ex){
			System.out.println("LOG : " + ex);
		}
		
		
	}
	
	public static void existDirCHK()
	{
		File mfile = new File(MAINDIR+LOGDIR);
		if( !mfile.exists() )
		{
			mfile.mkdir();
		}
	}	
}
