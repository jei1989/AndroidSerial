package skylife.android.serial;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

import gnu.io.*;
import skylife.android.log.*;

public class SerialMain implements Runnable{

	private SerialComm serialComm;
	private SerialWindow serialWindow;
	private SerialParser serialParser;
	private SerialProperty serialProperty;
	
	public SerialMain()
	{
		init();
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int count = 0;
		String mlength = "";
		while(true)
		{
			try{
				Thread.sleep(500);
				if( this.serialWindow.doc.getText(0, 100).equals(mlength)){
					count++;
				}else{
					count = 0;
				}
				if( count > 20){
					this.serialWindow.btnDisconn.doClick();
					Thread.sleep(500);
					this.serialWindow.btnPORTScan.doClick();
					Thread.sleep(100);
					//this.serialComm.connect(this.serialWindow.txtPORT.getText().trim());
					this.serialWindow.btnConnect.doClick();
					count = 0;
				}
				mlength = this.serialWindow.doc.getText(0, 100);
				//System.out.println( "count = " + count  + " :: mlength == " + mlength + " :: " + this.serialWindow.doc.getText(0, 100));
			}catch(Exception ex){
				System.out.println(ex);
				Log.errorLog(this, "run() :: " + ex.toString());
			}
			
			
		}
	}	
	
	
	public void init()
	{
		Log.MAINDIR = System.getProperty("user.dir");
		
		serialProperty = new SerialProperty();
		serialWindow = new SerialWindow();
		serialWindow.btnPORTScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setSerialPortName();
			}
		});
		serialWindow.btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					serialComm.connect(serialWindow.txtPORT.getText().trim());
					serialWindow.btnDisconn.setEnabled(true);
					serialWindow.btnConnect.setEnabled(false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					skylife.android.log.Log.errorLog(this, e.toString());
				}
			}
		});
		serialWindow.btnDisconn.setEnabled(false);
		serialWindow.btnDisconn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					serialComm.disconnect();
					serialWindow.btnDisconn.setEnabled(false);
					serialWindow.btnConnect.setEnabled(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					skylife.android.log.Log.errorLog(this, e.toString());
				}
			}
		});
		serialWindow.table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				System.out.println(serialWindow.table.getRowCount() +  " :: serialWindow.table.getSelectedRow() == " + serialWindow.table.getSelectedRow()  );
				
			}
		});
		
		serialWindow.btnRemoveRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					if( serialWindow.table.getSelectedRow() >= 0 ){
						int dialogButton = JOptionPane.YES_NO_OPTION;
						int dialogResult = JOptionPane.showConfirmDialog(serialWindow.getContentPane(), "Will be removed this row!");
						if( dialogResult == 0 )
						{
							serialWindow.tableModel.removeRow(serialWindow.table.getSelectedRow());
							serialProperty.setProperties(serialWindow.table);
						}
					}
				}catch(Exception ex)
				{
					Log.errorLog(this, ex.toString());
				}
				
			}
		});
		
		serialWindow.btnInsertRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector imVec = new Vector();
				imVec.add("test01");
				imVec.add("test02");
				imVec.add("test03");
				imVec.add("test04");
				imVec.add("test05");
				imVec.add("test06");
				imVec.add("test07");
				serialWindow.tableModel.addRow(imVec);
				
			}
		});
		
		serialComm = new SerialComm(this);
		setSerialPortName();
		
		serialParser = new SerialParser(this);
		
		
		serialWindow.tableModel.addColumn("Manufacture");
		serialWindow.tableModel.addColumn("Manufacture ID");
		serialWindow.tableModel.addColumn("Model Name");
		serialWindow.tableModel.addColumn("Model ID");
		serialWindow.tableModel.addColumn("Image Version");
		serialWindow.tableModel.addColumn("Download PID");
		serialWindow.tableModel.addColumn("Last CHK");
		
		try{
		
			if( serialProperty.getProperties().size() != 0 ){
				
				for( int cnt = 0; cnt < serialProperty.getProperties().size() ; cnt++ )
				{
					String[] row = serialProperty.getProperties().elementAt(cnt).toString().split("\\#");
					serialWindow.tableModel.addRow(row);				
				}
				
			}
		}catch(Exception ex)
		{
			Log.errorLog(this, ex.toString());
		}
		
		
		try{
			new Thread(this).start();
		}
		catch(Exception ex)
		{
			Log.errorLog(this, ex.toString());
		}		
		
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
		
		setLeftImage();

		try{
			Thread.sleep(5000);
			this.serialWindow.btnConnect.doClick();
		}
		catch(Exception ex)
		{
			Log.errorLog(this, ex.toString());
		}		
		
	}
	
	public void setLeftImage()
	{
		
		try{
			Image image = ImageIO.read(new File(Log.MAINDIR+"\\left.jpg")).getScaledInstance(serialWindow.lblLeft.getWidth(), serialWindow.lblLeft.getHeight(), Image.SCALE_SMOOTH);
			
			serialWindow.lblLeft.setIcon(new ImageIcon(image));
			
			
		}catch(Exception ex)
		{
			Log.errorLog(this, ex.toString());
		}
		
	}
	
	public void setObjectMessage(String message, String type)
	{
		if( type.equals("status"))
		{
			this.serialWindow.lblStatus.setText(message);
		}
	}
	
	public void setTableRow(Vector<Object> message){
		
		synchronized(this){
			try{
				for( int cnt = 0 ; cnt < message.size() ; cnt++ ){
					Log.log(this, message.elementAt(cnt).toString());
				}
				
			}catch(Exception ex){
				Log.errorLog(this, ex.toString());
			}
			
			try{
				int isExist = 0;
				/*************
				try{
					for( int cnt = 0 ; cnt < serialWindow.tableModel.getRowCount(); cnt++)
					{
							if( message.elementAt(1).equals(serialWindow.tableModel.getValueAt(cnt, 1)) && message.elementAt(2).equals(serialWindow.tableModel.getValueAt(cnt, 2)) )
							{
								isExist = 1;
								for( int scnt = 0 ; scnt < message.size() ; scnt++ ){
									serialWindow.tableModel.setValueAt(message.elementAt(scnt), cnt, scnt);
								}
								break;
							}
					}
				}catch(Exception ex)
				{
					Log.errorLog(this, ex.toString());
				}
				/*************/
				if( isExist == 0){
					//serialWindow.tableModel.insertRow(this.serialWindow.table.getRowCount(), message);
					String[] obj = new String[message.size()];
					for( int cnt = 0 ; cnt < message.size() ; cnt++ )
					{
						obj[cnt] = message.elementAt(cnt).toString();
					}
					
					serialWindow.tableModel.addRow(obj);
				}
				try{
					if( serialWindow.tableModel.getRowCount() > 30 )
					{
						serialWindow.tableModel.removeRow(0);
					}
				}catch(Exception ex)
				{
					Log.errorLog(this, ex.toString());
				}
				serialProperty.setProperties(this.serialWindow.table);
				
				this.serialParser.setClearVec();
			
			}catch(Exception ex){
				Log.errorLog(this, ex.toString());
			}
	
		}
	}

	private void setSerialPortName()
	{
		new SerialPortScan().start();
	}
	
	public void setStatusMessage(String message)
	{
		new StatusMessage(message).start();
		
	}
	
	public void setTextPane(String message){
		
		try {
			this.serialWindow.doc.insertString(0,message+"\r\n", null);
			serialParser.requestParsing(message);
			if( serialWindow.chckboxCapture.isSelected() ){
				Log.commonWrite(this,message , "capture");
				serialWindow.chckboxCapture.setText("Capture ON");
			}else{
				serialWindow.chckboxCapture.setText("Capture OFF");
			}
			if( serialWindow.doc.getLength() > 5000 ){
				serialWindow.doc.remove(5000, serialWindow.doc.getLength()-5000);
			}
			
			//System.out.println(message);
			//System.out.println(serialWindow.doc.getLength());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			skylife.android.log.Log.errorLog(this, e.toString());
		}
		
	}
	
	public String getTextPane()
	{
		String ret = "";
		try{
			ret = this.serialWindow.textPane.getText();
		}catch(Exception ex){
			Log.errorLog(this, ex.toString());
		}
		return ret;
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
	
	class StatusMessage extends Thread
	{
		String message;
		public StatusMessage(String message)
		{
			this.message = message;
		}
		public void run()
		{
			serialWindow.lblStatus.setText(this.message);
		}
	}


}
