package skylife.android.serial;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;
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
	private SerialGPS serialGPS;
	
	private Image[] leftimage = new Image[5];
	
	private Vector imsiVec = new Vector();
	private StringBuffer strbuff = new StringBuffer();;
	DecimalFormat f1 = new DecimalFormat("0.00"); // 이것은 소숫점 이하 2자리 (자동 반올림)
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
				/**********************
				if( this.serialWindow.doc.getText(0, 100).equals(mlength)){
					count++;
				}else{
					count = 0;
				}
				if( count > 20){
					this.serialWindow.btnDisconn.doClick();
					Thread.sleep(500);
					//this.serialWindow.btnPORTScan.doClick();
					//Thread.sleep(100);
					//this.serialComm.connect(this.serialWindow.txtPORT.getText().trim());
					this.serialWindow.btnConnect.doClick();
					count = 0;
				}
				mlength = this.serialWindow.doc.getText(0, 100);
				/**********************/
				
				//panel.setBackground(new Color(30, 144, 255));
				//panel.setBackground(new Color(255, 153, 153));
				
				if( serialWindow.chckboxCapture.isSelected() ){
					serialWindow.chckboxCapture.setText("Capture ON");
				}else{
					serialWindow.chckboxCapture.setText("Capture OFF");
				}
				
				//System.out.println( "count = " + count  + " :: mlength == " + mlength + " :: " + this.serialWindow.doc.getText(0, 100));
			}catch(Exception ex){
				System.out.println(ex);
				//ex.printStackTrace();
				Log.errorLog(this, "run() :: " + ex.toString());
			}
			
			if( count > 50 ){
				count = 0;
			}
			try{
				if( count < 10 ){
					
					leftimage[0] = ImageIO.read(new File(Log.MAINDIR+"\\left1.jpg")).getScaledInstance( (int)(serialWindow.panel.getWidth()*0.95), serialWindow.panel.getHeight()/2, Image.SCALE_SMOOTH);
										
					serialWindow.lblLeft.setIcon(new ImageIcon(leftimage[0]));
				}else if( count >= 10 && count < 20 ){
					leftimage[1] = ImageIO.read(new File(Log.MAINDIR+"\\left2.jpg")).getScaledInstance((int)(serialWindow.panel.getWidth()*0.95), serialWindow.panel.getHeight()/2, Image.SCALE_SMOOTH);
					
					serialWindow.lblLeft.setIcon(new ImageIcon(leftimage[1]));
				}else if( count >= 20 && count < 30 ){
					leftimage[2] = ImageIO.read(new File(Log.MAINDIR+"\\left3.jpg")).getScaledInstance((int)(serialWindow.panel.getWidth()*0.95), serialWindow.panel.getHeight()/2, Image.SCALE_SMOOTH);
										
					serialWindow.lblLeft.setIcon(new ImageIcon(leftimage[2]));
				}else if( count >= 30 && count < 40 ){
					leftimage[3] = ImageIO.read(new File(Log.MAINDIR+"\\left4.jpg")).getScaledInstance((int)(serialWindow.panel.getWidth()*0.95), serialWindow.panel.getHeight()/2, Image.SCALE_SMOOTH);
										
					serialWindow.lblLeft.setIcon(new ImageIcon(leftimage[3]));
				}else if( count >= 40 && count < 50 ){
					leftimage[4] = ImageIO.read(new File(Log.MAINDIR+"\\left5.jpg")).getScaledInstance((int)(serialWindow.panel.getWidth()*0.95), serialWindow.panel.getHeight()/2, Image.SCALE_SMOOTH);					
					
					serialWindow.lblLeft.setIcon(new ImageIcon(leftimage[4]));
				}
				
				//serialWindow.lblLeft.setSize( serialWindow.panel.getWidth(), serialWindow.panel.getHeight() );
				
			}catch(Exception ex){
				skylife.android.log.Log.errorLog(this, ex.toString());
			}
			count++;
			
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
		
		if(  !this.serialWindow.chckbxDmt.isSelected()   ){//is not dmt signal checker
			serialWindow.tableModel.addColumn("Manufacture");
			serialWindow.tableModel.addColumn("Manufacture ID");
			serialWindow.tableModel.addColumn("Model Name");
			serialWindow.tableModel.addColumn("Model ID");
			serialWindow.tableModel.addColumn("Image Version");
			serialWindow.tableModel.addColumn("Download PID");
			serialWindow.tableModel.addColumn("Last CHK");
		}else{
			serialWindow.tableModel.addColumn("중계기");
			serialWindow.tableModel.addColumn("SNR(dB)");
			serialWindow.tableModel.addColumn("Power(dBm)");
			serialWindow.tableModel.addColumn("Minimum(dB)");
			serialWindow.tableModel.addColumn("Margin(dB)");
			serialWindow.tableModel.addColumn("GPS");
			serialWindow.tableModel.addColumn("Last CHK");			
		}
		
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
		/******************
		try{
			leftimage[0] = ImageIO.read(new File(Log.MAINDIR+"\\left1.jpg")).getScaledInstance(serialWindow.panel.getWidth(), serialWindow.panel.getHeight(), Image.SCALE_SMOOTH);
			leftimage[1] = ImageIO.read(new File(Log.MAINDIR+"\\left2.jpg")).getScaledInstance(serialWindow.panel.getWidth(), serialWindow.panel.getHeight(), Image.SCALE_SMOOTH);
			leftimage[2] = ImageIO.read(new File(Log.MAINDIR+"\\left3.jpg")).getScaledInstance(serialWindow.panel.getWidth(), serialWindow.panel.getHeight(), Image.SCALE_SMOOTH);
			leftimage[3] = ImageIO.read(new File(Log.MAINDIR+"\\left4.jpg")).getScaledInstance(serialWindow.panel.getWidth(), serialWindow.panel.getHeight(), Image.SCALE_SMOOTH);
			leftimage[4] = ImageIO.read(new File(Log.MAINDIR+"\\left5.jpg")).getScaledInstance(serialWindow.panel.getWidth(), serialWindow.panel.getHeight(), Image.SCALE_SMOOTH);
		}
		catch(Exception ex)
		{
			Log.errorLog(this, ex.toString());
		}
		/******************/
		
		serialGPS = new SerialGPS(this,serialWindow.txtGPSip.getText(), serialWindow.txtGPSport.getText());
		
		new Thread(serialGPS).start();
		
		
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
			/*************
			try{
				
				for( int cnt = 0 ; cnt < message.size() ; cnt++ ){
					Log.log(this, message.elementAt(cnt).toString());
				}
				
			}catch(Exception ex){
				Log.errorLog(this, ex.toString());
			}
			/*************/
			
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
					String strlog = "";
					for( int cnt = 0 ; cnt < message.size() ; cnt++ )
					{
						obj[cnt] = message.elementAt(cnt).toString();
						
								if(this.serialWindow.chckbxDmt.isSelected()) {
									if( cnt == message.size()-1 ){
										strlog = strlog + obj[cnt].toString();
									}else{
										strlog = strlog + obj[cnt].toString() + ",";
									}
									

									
								}
								
								
					}
					
					if(this.serialWindow.chckbxDmt.isSelected()) {
						Log.commonWrite(this, strlog ,  serialWindow.txtBoxSample.getText() + "_gps");
					}
					
					if( Double.parseDouble( obj[1].toString() ) == 0.0 ){
						Log.commonWrite(this, strlog ,  serialWindow.txtBoxSample.getText() + "_unlock_gps");
					}
					
					serialWindow.tableModel.addRow(obj);
					
					
				}
				try{
					if( serialWindow.tableModel.getRowCount() >= 100 )
					{
						serialWindow.tableModel.removeRow(0);
					}
					
					serialWindow.table.changeSelection(serialWindow.tableModel.getRowCount()-1, 0, false, false);
					
				}catch(Exception ex)
				{
					Log.errorLog(this, ex.toString());
				}
				serialProperty.setProperties(this.serialWindow.table);
				
				if(  !this.serialWindow.chckbxDmt.isSelected()   ){//is not dmt signal checker
					this.serialParser.setClearVec();
				}
			
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
			
			
			if(  !this.serialWindow.chckbxDmt.isSelected()   ){//is not dmt signal checker
			
				this.serialWindow.doc.insertString(0,message+"\r\n", null);
				
				serialParser.requestParsing(message);
				if( serialWindow.chckboxCapture.isSelected() ){
					Log.commonWrite(this,message , "capture");
					//serialWindow.chckboxCapture.setText("Capture ON");
				}else{
					//serialWindow.chckboxCapture.setText("Capture OFF");
				}
			
			}else if( this.serialWindow.chckbxDmt.isSelected()  ){// is dmt signal checker
				
				
				if( message.equals( "======<TP Info>======" )){
					
					if( strbuff != null ){
						
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							
						this.serialWindow.doc.insertString(0, "["+ df.format(cal.getTime())+"] " +strbuff.toString()+"\r\n", null);
						

						if( serialWindow.chckboxCapture.isSelected() ){
							Log.commonWrite(this,strbuff.toString() ,  serialWindow.txtBoxSample.getText() + "_signal");
						}						
						
						
						
					}
					//strbuff = new StringBuffer();
					strbuff.delete(0,strbuff.length());
					
					
				}else{
					if( strbuff != null ){
						
						String[] strsplite = message.split("\\ ");
						
						strbuff.append(strsplite[0]+ " "+ f1.format( Double.parseDouble(strsplite[2]) )+ " " + strsplite[3]+ " ");
						
						if( imsiVec.size() > 0 ){
							//imsiVec.removeAllElements();
							//imsiVec = null;
						}
						
						
						imsiVec.addElement(strsplite[0]);
						imsiVec.addElement( String.valueOf(f1.format( Double.parseDouble(strsplite[2]) ))  );
						imsiVec.addElement(strsplite[3]);
						
						if(  strsplite[0].equals("BS02") ||   strsplite[0].equals("BS06") || strsplite[0].equals("BS08") || strsplite[0].equals("BS12")  ){
							imsiVec.addElement("9.4");
							imsiVec.addElement( String.valueOf(f1.format(  Double.parseDouble(strsplite[2]) - 9.4 )) );
						}else if( strsplite[0].equals("BS04") ){
							imsiVec.addElement("7.4");
							imsiVec.addElement( String.valueOf(f1.format(  Double.parseDouble(strsplite[2]) - 7.4 )) );						
						}else if( strsplite[0].equals("BS10") ){
							imsiVec.addElement("6.6");
							imsiVec.addElement( String.valueOf(f1.format(  Double.parseDouble(strsplite[2]) - 6.6 )) );						
						}else{
							imsiVec.addElement("");
							imsiVec.addElement("");
						}
						imsiVec.addElement( this.serialGPS.getGPS() );
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
						
						imsiVec.addElement( String.valueOf(df.format(cal.getTime())) );
						setTableRow(imsiVec);
						imsiVec.removeAllElements();
					}
				}
				
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
