package skylife.android.log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {

	public static String MAINDIR = "";
	public static String LOGDIR ="\\logs";
	public static String RESDIR = "\\result";
	
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
	
	public static void commonWrite(Object obj, String message, String type)
	{
		existDirCHK();	
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		message = "["+df.format(cal.getTime())+"] " + message;
		
		SimpleDateFormat dfname = new SimpleDateFormat("yyyy-mm-dd");
		FileWriter fos = null;
		try{
			if( type.equals("log")){
				fos = new FileWriter(MAINDIR+LOGDIR+ "\\" + String.valueOf(dfname.format(cal.getTime()))+"_log",true );
			}else if( type.equals("error")){
				fos = new FileWriter(MAINDIR+LOGDIR+ "\\" + String.valueOf(dfname.format(cal.getTime()))+"_error",true );
			}
			fos.write(message);
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
