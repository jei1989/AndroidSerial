package skylife.android.serial;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gnu.io.*;
import skylife.android.log.*;

public class SerialMain {

	private SerialComm serialComm;
	private SerialWindow serialWindow;
	
	public SerialMain()
	{
		init();
	}
	
	public void init()
	{
		Log.MAINDIR = System.getProperty("user.dir");
		
		serialWindow = new SerialWindow();
		serialWindow.btnPORTScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setSerialPortName();
			}
		});
		
		serialComm = new SerialComm();
		setSerialPortName();
		
		
		/********************
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null},
			},
			new String[] {
				"New column"
			}
		));
		/*******************/
		
	}
	
	private void setSerialPortName()
	{
		new SerialPortScan().start();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new SerialMain();
	}
	
	class SerialPortScan extends Thread
	{
		public void run()
		{
			CommPortIdentifier portId = serialComm.getPortID();
			if( portId != null )
			{
				serialWindow.txtPORT.setText(portId.getName());
			}			
		}
	}	

}
