package skylife.android.serial;

import gnu.io.*;
import skylife.android.log.Log;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Enumeration;

public class SerialComm {

	private SerialMain serialMain;
	private CommPortIdentifier portId;
	private boolean isRunning = true;
	private CommPortIdentifier portIdentifier;
	private CommPort commPort;
	private OutputStream out;
	
	public SerialComm(SerialMain serialMain)
	{
		this.serialMain = serialMain;
		portScan();
	}
	
	private void init()
	{
		
	}
	
	public CommPortIdentifier getPortID()
	{
		return this.portScan();
	}
	
	private CommPortIdentifier portScan()
	{
    	Enumeration portenum = CommPortIdentifier.getPortIdentifiers();
    	while( portenum.hasMoreElements() )
    	{
    		
    		portId = (CommPortIdentifier) portenum.nextElement();
    		if( portId.getPortType() == CommPortIdentifier.PORT_SERIAL )
    		{
    			
    			break;
    		}
    		
    	}
    	return portId;
	}
	
	public void disconnect()
	{
		isRunning = false;
		this.commPort.close();
		
	}
	
	private void outWriter(OutputStream out)
	{
		try{
			//this.out.write(("\r\n\r\n").getBytes());
			//this.out.flush();
			out.write((int)(KeyEvent.VK_ENTER));
			out.flush();
			//System.out.println((int)KeyEvent.VK_ENTER);
			out.write(("logcat\r\n").getBytes());
			out.flush();
			//System.out.println(("logcat\r\n").getBytes());
		}catch(Exception ex){
			Log.errorLog(this, ex.toString());
			//System.out.println("=========="+ ex);
		}
	}
	
    public void connect ( String portName ) throws Exception
    {
        
    	isRunning = true;
    	/********************/
    	portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        /********************/
        if ( portIdentifier.isCurrentlyOwned() )
        {
        	serialMain.setObjectMessage("Error : Port is currently in use", "status");
            skylife.android.log.Log.errorLog(this, "Error: Port is currently in use");
        }
        else
        {
            commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams( 115200,
                               SerialPort.DATABITS_8,
                               SerialPort.STOPBITS_1,
                               SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                
                outWriter(out);
                //out.write(("logcat"+ "\r\n").getBytes());
                
                (new Thread(new SerialReader(in))).start();
                //(new Thread(new SerialWriter(out))).start();
                
                serialMain.setObjectMessage(portName + " Connected.","status");
                Log.log(this, portName + " Connected.");

            }
            else
            {
            	serialMain.setObjectMessage("Error :  Only serial ports are handled by this example.", "status");
            	skylife.android.log.Log.errorLog(this, "Error: Only serial ports are handled by this example.");
            }
        }     
        /********************/
    }
    
    /** */
    public class SerialReader implements Runnable 
    {
        InputStream in;
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
            	String temp;
            	
            	BufferedReader buff = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                //while ( ( len = this.in.read(buffer)) > -1 )
            	//while( (temp = buff.readLine() ) != null )
            	while(isRunning)
                {
            		try{
            		 temp = buff.readLine();  
                	//serialMain.setTextPane(new String(buffer,0,len));
            		serialMain.setTextPane(temp);
                    //System.out.println(new String(buffer,0,len) + " :: " + temp);
            		}catch(Exception ex)
            		{
            			//ex.printStackTrace();
            		}
                    
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }

    /** */
    public class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {                
                int c = 0;
                //while ( ( c = System.in.read()) > -1 )
                //System.out.println("=================   " + String.valueOf(c));
                
                BufferedOutputStream buffout = new BufferedOutputStream(out);
                while(true)
                {
                	
                	//String temp = serialMain.getTextPane().trim();
                	//if( temp.equals("logcat") ){
	                //	buffout.write(temp.getBytes());
	                //	buffout.flush();
	                //	temp = "";
	                //  this.out.write(c);
	                    //this.out.flush();
                	//}
                }                
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }            
        }
    }
	
}
