package skylife.android.serial;

import gnu.io.*;
import java.io.*;
import java.util.Enumeration;

public class SerialComm {

	private CommPortIdentifier portId;
	public SerialComm()
	{
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
	
    void connect ( String portName ) throws Exception
    {
        

    	/********************
    	CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifiers();
        /********************/
        if ( portId.isCurrentlyOwned() )
        {
            skylife.android.log.Log.errorLog(this, "Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portId.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams( 57600,
                               SerialPort.DATABITS_8,
                               SerialPort.STOPBITS_1,
                               SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                
                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
        /********************/
    }
    
    /** */
    public static class SerialReader implements Runnable 
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
                while ( ( len = this.in.read(buffer)) > -1 )
                {
                    System.out.print(new String(buffer,0,len));
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }

    /** */
    public static class SerialWriter implements Runnable 
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
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }
	
}
