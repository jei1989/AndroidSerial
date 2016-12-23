package skylife.android.serial;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.JTable;

import skylife.android.log.Log;

public class SerialProperty {

	public SerialProperty()
	{
		
	}
	
	public Vector getProperties()
	{
		Vector vec = new Vector();
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(Log.MAINDIR+Log.PROPFILE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.errorLog(this, e.toString());
		}
		BufferedReader buff = new BufferedReader( new InputStreamReader(fin) );
		String temp = "";
		try {
			while(( temp = buff.readLine()) != null){
				vec.add(temp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.errorLog(this, e.toString());
		}
		
		try{
			buff.close();
			fin.close();
		}catch(Exception e){
			Log.errorLog(this, e.toString());
		}
		return vec;
	}
	
	public void setProperties(JTable table)
	{
		String prop = "";
		for( int cnt = 0; cnt < table.getRowCount() ; cnt++ )
		{
			for( int scnt = 0; scnt < table.getColumnCount() ; scnt++ )
			{
				
				prop = prop + table.getValueAt(cnt, scnt);
				if( scnt != table.getColumnCount()-1 ){
					prop = prop + "#";
				}
				
			}
			prop = prop + "\r\n";
		}
		
		FileWriter fos = null;
		try {
			fos = new FileWriter(Log.MAINDIR+Log.PROPFILE,false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.errorLog(this, e.toString());
		}
		try {
			fos.write(prop);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.errorLog(this, e.toString());
		}
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.errorLog(this, e.toString());
		}
	}
	
}
