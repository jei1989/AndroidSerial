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
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SerialWindow extends JFrame {

	private JPanel contentPane;

	public JTable table;
	public DefaultTableModel tableModel;
	
	public JTextPane textPane;
	public Document doc;
	public JTextField txtPORT;
	
	public JButton btnPORTScan;
	

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 600);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		this.setLocation( (int)(dim.getWidth()/2 - this.getWidth()/2)  , (int)(dim.getHeight()/2 - this.getHeight()/2));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JSplitPane splitPane_1 = new JSplitPane();
		contentPane.add(splitPane_1, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		splitPane_1.setLeftComponent(panel);
		panel.setPreferredSize(new Dimension(200,0));
		panel.setLayout(new MigLayout("", "[30][14.00][107.00,grow]", "[15.00][][][][][]"));
		
		btnPORTScan = new JButton("Scan");
		btnPORTScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		panel.add(btnPORTScan, "cell 0 1");
		
		txtPORT = new JTextField();
		panel.add(txtPORT, "cell 2 1,growx");
		txtPORT.setColumns(10);
		
		JButton btnNewButton = new JButton("CONNECT");
		panel.add(btnNewButton, "cell 0 2");
		
		JButton btnDisconn = new JButton("DISCONN");
		panel.add(btnDisconn, "cell 2 2");
		
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
		doc = textPane.getDocument();
		scrollPane_1.setViewportView(textPane);
		
		this.setVisible(true);
	}

}
