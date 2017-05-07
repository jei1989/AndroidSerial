package skylife.android.serial;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SerialGPS implements Runnable {

	private SerialMain serialMain;
	private Socket socket;
	private Scanner scan;
	private InputStream in;
	private String gpsip;
	private String gpsport;
	
	private String gps;
	
	
	public SerialGPS(SerialMain serialMain, String gpsip, String gpsport)
	{
		this.serialMain = serialMain;
		this.gpsip = gpsip;
		this.gpsport = gpsport;
	}
	
	public void run()
	{
		initialize();
		
	}
	
	public String getGPS(){
		return this.gps;
	}
	
	private void initialize()
	{
		if( socket  == null){
			
			try {
				socket = new Socket(this.gpsip, Integer.parseInt(this.gpsport));
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//if
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			//System.out.println(conn.getContent().toString());
			in = socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);

		}
		
		try {
			//System.out.println(conn.getContent().toString());
			scan = new Scanner(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);

		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		
		while(scan.hasNext())
		{
			try{
			String str = scan.nextLine();
			

			if( str.substring(1, 6).equals("GPRMC")) {

				String[] arrStr = str.split("\\,");
				
					if( !arrStr[1].equals("") && arrStr[1] != null){
						gps = str;
						System.out.println(gps);
						
					}
					
				//13
				//$GPRMC,181629.0,A,3731.713055,N,12652.301501,E,0.0,,040517,6.1,W,A*3B
				//System.out.println((line++) + " : " + arrStr.length + " : " + str);
				}
			
			}catch(Exception ex){
				
			}
		}
		scan.close();
		
		
	}
}
