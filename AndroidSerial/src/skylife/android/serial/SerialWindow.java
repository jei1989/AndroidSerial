package skylife.android.serial;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import net.miginfocom.swing.MigLayout;
import skylife.android.log.Log;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.SystemColor;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SerialWindow extends JFrame {

	private JPanel contentPane;

	public JTable table;
	public DefaultTableModel tableModel;
	
	public JTextPane textPane;
	public Document doc;
	public JTextField txtPORT;
	
	public JButton btnPORTScan;
	
	public JLabel lblStatus;
	public JButton btnConnect;
	public JButton btnDisconn;
	private JLabel lblCapture;
	public JCheckBox chckboxCapture;
	public JButton btnRemoveRow;
	public JButton btnInsertRow;
	public JLabel lblLeft;

	/**
	 * Launch the application.
	 */
	
	/***********************
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SerialWindow frame = new SerialWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/***********************/
	
	/**
	 * Create the frame.
	 */
	public SerialWindow() {
		setTitle("OTA Image Monitering V1.1");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Log.MAINDIR+"\\satellite.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 600);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		this.setLocation( (int)(dim.getWidth()/2 - this.getWidth()/2)  , (int)(dim.getHeight()/2 - this.getHeight()/2));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		lblStatus = new JLabel("<<<  Status Message >>>");
		lblStatus.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		contentPane.add(lblStatus, BorderLayout.SOUTH);
		
		JSplitPane splitPane_1 = new JSplitPane();
		contentPane.add(splitPane_1, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		splitPane_1.setLeftComponent(panel);
		panel.setPreferredSize(new Dimension(200,0));
		panel.setLayout(new MigLayout("", "[30][14.00][107.00,grow]", "[15.00][][][][][][][][][][][][][][][][][grow]"));
		
		lblCapture = new JLabel("  Log Capture");
		lblCapture.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		lblCapture.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		panel.add(lblCapture, "cell 0 0,growx,aligny center");
		
		chckboxCapture = new JCheckBox("Capture OFF");
		chckboxCapture.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		panel.add(chckboxCapture, "cell 2 0");
		
		btnRemoveRow = new JButton("Remove Row ==>");
		
		btnRemoveRow.setBackground(new Color(255, 165, 0));
		btnRemoveRow.setForeground(new Color(255, 250, 205));
		btnRemoveRow.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		panel.add(btnRemoveRow, "cell 0 1 3 1,growx");
		
		btnPORTScan = new JButton("Scan");
		btnPORTScan.setBackground(SystemColor.textHighlight);
		btnPORTScan.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		btnPORTScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		btnInsertRow = new JButton("Insert Row ==>");
		
		btnInsertRow.setForeground(new Color(255, 250, 205));
		btnInsertRow.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		btnInsertRow.setBackground(new Color(255, 165, 0));
		panel.add(btnInsertRow, "cell 0 2 3 1,growx");
		panel.add(btnPORTScan, "cell 0 3,growx");
		
		txtPORT = new JTextField();
		panel.add(txtPORT, "cell 2 3,growx");
		txtPORT.setColumns(10);
		
		btnConnect = new JButton("CONNECT");
		btnConnect.setBackground(new Color(100, 149, 237));
		btnConnect.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		
		panel.add(btnConnect, "cell 0 4");
		
		btnDisconn = new JButton("DISCONN");
		btnDisconn.setBackground(new Color(65, 105, 225));
		
		btnDisconn.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		panel.add(btnDisconn, "cell 2 4");
		
		lblLeft = new JLabel("");
		panel.add(lblLeft, "cell 0 14 3 4,grow");
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane_1.setRightComponent(tabbedPane);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tabbedPane.addTab("OTA Image", null, splitPane, null);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		
		tableModel = new DefaultTableModel();
		table = new JTable();
		
		table.setModel(tableModel);
		table.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 14));
		
		
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
		//table.setPreferredSize(new Dimension(0,100));
		scrollPane.setViewportView(table);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);
		
		textPane = new JTextPane();
		textPane.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 12));
		doc = textPane.getDocument();
		
		scrollPane_1.setViewportView(textPane);
		
		this.setVisible(true);
	}

}
