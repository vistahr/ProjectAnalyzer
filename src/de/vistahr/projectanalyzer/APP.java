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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class APP {
	
	private GUI gui;
	private boolean analyzeStarted = false;
	private ExecutorService exec = Executors.newSingleThreadExecutor();
	
	
	public GUI getGUI() {
		return this.gui;
	}
	
	public static void main(String[] args) {
		new APP();
	}
	

	public APP() {
		// init GUI
		this.gui = new GUI();
		this.getGUI().getMainframe().setVisible(true);
		// listener
		this.getGUI().btnAnalyze.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) { 

				if(e.getActionCommand().equals(GUI.APP_BTN_ANALYZE_ABORT)) {
					exec.shutdownNow();
					// restart service
					exec = Executors.newSingleThreadExecutor();
					
				} else {
					//exec = Executors.newSingleThreadExecutor();
					Runnable r = new Runnable() {
						@Override
						public void run() {
							analyzePressed(null);
						}
					};
					exec.execute(r);
				}
				
			}
		});
	}
	

	
	public void analyzePressed(ActionEvent e) {
		// toggle
		this.analyzeStarted = !this.analyzeStarted;
		
		getGUI().enableLoading();
		
		// split filetype input into hashset
		HashSet<String> filter = new HashSet<String>(Arrays.asList(getGUI().areaFiletype.getText().split(",")));			
		
		try {
			// execute analyzing in thread
			Analyzer ra = new Analyzer(new AnalyzerInput(this.getGUI().txtFile.getText(), getGUI().chkRecursive.isSelected(), filter));
			Thread t = new Thread(ra);
			t.start();
			t.join();
			
			// analyzing ended
			this.getGUI().disableLoading();
			
			// show results
			this.getGUI().showResultDialog("<html>Directories: " + ra.getOutput().getDirectoryCount() +" <br /> Files: " + ra.getOutput().getFileCount() + " </html>");
			
		} catch (Exception ex) {
			this.getGUI().disableLoading();
			getGUI().showMessageDialog("interrupted");
		}
	}
	
	
	
}


