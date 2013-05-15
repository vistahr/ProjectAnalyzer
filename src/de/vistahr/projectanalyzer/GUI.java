/**
 * 
 * 	Copyright 2012 Vince. All rights reserved.
 * 	
 * 	Redistribution and use in source and binary forms, with or without modification, are
 * 	permitted provided that the following conditions are met:
 * 	
 * 	   1. Redistributions of source code must retain the above copyright notice, this list of
 * 	      conditions and the following disclaimer.
 * 	
 * 	   2. Redistributions in binary form must reproduce the above copyright notice, this list
 * 	      of conditions and the following disclaimer in the documentation and/or other materials
 * 	      provided with the distribution.
 * 	
 * 	THIS SOFTWARE IS PROVIDED BY Vince ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * 	WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * 	FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Vince OR
 * 	CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * 	CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * 	SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * 	ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * 	NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * 	ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 	
 * 	The views and conclusions contained in the software and documentation are those of the
 * 	authors and should not be interpreted as representing official policies, either expressed
 * 	or implied, of Vince.
 */
package de.vistahr.projectanalyzer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;



public class GUI {

	{ // Systemlook
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public final static String APP_NAME = "ProjectAnalyzer";
	
	public final static String APP_BTN_ANALYZE_START = "start analyzing...";
	public final static String APP_BTN_ANALYZE_ABORT = "stop analyzing!";

	public JFileChooser fchooseDirectory = new JFileChooser();
	public JTextField txtFile = new JTextField(20);
	public JButton btnLookupDir = new JButton("browse");
	public JCheckBox chkRecursive = new JCheckBox();
	
	public JComboBox comboFTExInclude = new JComboBox();
	public JTextArea areaFiletype = new JTextArea(5,20);

	public JButton btnAnalyze = new JButton(APP_BTN_ANALYZE_START);

	
	
	public JProgressBar loader = new JProgressBar();
	
	
	private JFrame frame = new JFrame(APP_NAME);
	
	
	public JFrame getMainframe() {
		return this.frame;
	}
	

	
	/**
	 * Build GUI
	 */
	public GUI() {
		
		this.frame.setIconImage(new ImageIcon(getClass().getResource("/res/analyze.png")).getImage());
		
		
		// Contentpanel
		JPanel conentPanel = new JPanel();
		conentPanel.setLayout(new BoxLayout(conentPanel, BoxLayout.PAGE_AXIS));
		
		
		// top - description
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(new JLabel("<html><font size='2'>Choose a path and set the specific filetypes.</font></html>"));
		
		
		// path
		JPanel pathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pathPanel.setBorder(BorderFactory.createTitledBorder("Path"));
		pathPanel.add(new JLabel("Location:"));
		pathPanel.add(txtFile);
		pathPanel.add(btnLookupDir);
		
		// file chooser dialog
		btnLookupDir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				fchooseDirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				//fchooseDirectory.setAcceptAllFileFilterUsed(false);
				if (fchooseDirectory.showOpenDialog(btnLookupDir) == JFileChooser.APPROVE_OPTION) {
					File selDir = fchooseDirectory.getSelectedFile();
					txtFile.setText(selDir.toString());
				}
			}
		});
		txtFile.setEditable(false);
		txtFile.setText(System.getProperty("user.home"));
		chkRecursive.setSelected(true);
		pathPanel.add(chkRecursive);
		pathPanel.add(new JLabel("subfolder?"));
		
		// filetype
		JPanel filetypePanel = new JPanel();
		filetypePanel.setLayout(new BoxLayout(filetypePanel, BoxLayout.PAGE_AXIS));
		filetypePanel.setBorder(BorderFactory.createTitledBorder("Filetype"));
		
		
		JPanel ftComponentsSelect = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ftComponentsSelect.add(comboFTExInclude);
		comboFTExInclude.addItem(new String("Include:"));
		//comboFTExInclude.addItem(new String("Exclude:")); TODO
		
		JPanel ftComponentsArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ftComponentsArea.add(areaFiletype);
		areaFiletype.setText("txt");
		ftComponentsArea.add(new JLabel("<html><div style='margin-left:10px;'><font size='2'>Use commas to seperate<br /> two or more filetypes.</font></div></html>"));
		//areaFiletype.setToolTipText("Excluded or Included filetypes. Only Plain/Text files are allowed.");
		
		
		filetypePanel.add(ftComponentsSelect);
		filetypePanel.add(ftComponentsArea);
		//filetypePanel.add(lblCoArea);
		

		// bottom - submit
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(loader);
		loader.setVisible(false);
		loader.setIndeterminate(true);
		bottomPanel.add(btnAnalyze);
		
		conentPanel.add(topPanel);
		conentPanel.add(pathPanel);
		conentPanel.add(filetypePanel);
		conentPanel.add(bottomPanel);
		
		
		// mainframe
		this.frame.add(conentPanel, BorderLayout.PAGE_START);
		
		this.frame.setSize(500,350);
		this.frame.setResizable(true);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.frame.setLocationRelativeTo(null);
		
		this.frame.pack();
	}
	
	
	public void enableLoading() {
		this.loader.setVisible(true);
		this.btnAnalyze.setText(GUI.APP_BTN_ANALYZE_ABORT);
	}
	
	
	public void disableLoading() {
		this.loader.setVisible(false);
		this.btnAnalyze.setText(GUI.APP_BTN_ANALYZE_START);
	}
	
	
	/**
	 * Creates an warning dialog box
	 * @param message
	 * 			Message that will shown in the Dialogbox
	 */
	public void showMessageDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.WARNING_MESSAGE);
	}
	
	
	/**
	 * Creates an result dialog box
	 * @param message
	 * 			Message that will shown in the Dialogbox
	 */
	public void showResultDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Results", JOptionPane.PLAIN_MESSAGE);
	}	
}
