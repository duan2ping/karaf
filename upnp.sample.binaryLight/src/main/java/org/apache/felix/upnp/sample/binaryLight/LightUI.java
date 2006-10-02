/* 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.felix.upnp.sample.binaryLight;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.osgi.framework.BundleException;

/* 
* @author <a href="mailto:felix-dev@incubator.apache.org">Felix Project Team</a>
*/

public class LightUI extends JFrame implements PropertyChangeListener {
    private final static ImageIcon LIGHT_ON = LightUI.loadIcon("LightOn.gif","ON");
    private final static ImageIcon LIGHT_OFF = LightUI.loadIcon("LightOff.gif","OFF");
    private final static ImageIcon LIGHT_FAIL = LightUI.loadIcon("LightFail.gif","FAILURE");
	private final JLabel label = new JLabel();
    private LightModel model;
   
	public LightUI(LightModel model)   {
		super("Felix UPnP BinaryLight");
		this.model = model;
		setSize(150,150);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(doMainPanel(),BorderLayout.CENTER);
		panel.add(doControlPanel(),BorderLayout.SOUTH);
		getContentPane().add(panel);
		model.addPropertyChangeListener(this);

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) 
			{
				try {
					Activator.context.getBundle().stop();
				} catch (BundleException ex) {
					ex.printStackTrace();
				}
			}
		});			
	       try {
	            URL eventIconUrl = LightUI.class.getResource("images/logo.gif");           
	            ImageIcon icon=  new ImageIcon(eventIconUrl,"logo");
	            setIconImage(icon.getImage());
	       }
	        catch (Exception ex){
	                System.out.println("Resource: IMAGES/logo.gif not found : " + ex.toString());
	        }
	
		pack();
		show();
    }
	
	private JPanel doMainPanel(){
		JPanel panel = new JPanel();
		label.setIcon(LIGHT_OFF);
		//label.setSize(new Dimension(32,32));
	    label.addMouseListener(new MouseAdapter(){          
	          public void mouseClicked(MouseEvent e){
	            if (SwingUtilities.isLeftMouseButton(e)){
	              if (e.getClickCount()==1){
	              Icon icon = label.getIcon();
	              if (icon == LIGHT_ON)
	                  model.switchOff();
	              else 
	                  model.switchOn();
	              }
	            }
	          }
	    });
	    panel.add(label);
	    return panel;
	}
	          
	private JPanel doControlPanel(){
		JPanel panel = new JPanel();
		JButton btnSwitch = new JButton("On");
		JButton btnFailure = new JButton("Failure");
		panel.add(btnSwitch);
		panel.add(btnFailure);
		return panel;
	}
	
    public  static ImageIcon loadIcon(String path,String title)
    {
        try {
            URL eventIconUrl = LightUI.class.getResource("images/" + path);
            return new ImageIcon(eventIconUrl,title);
        }
        catch (Exception ex){
			System.out.println("Resource:" + path + " not found : " + ex.toString());
            return null;
        }
    }

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
    public void propertyChange(PropertyChangeEvent evt) {
    	String property = evt.getPropertyName();
    	System.out.println("Light changed property::"+property);
    	boolean value = ((Boolean) evt.getNewValue()).booleanValue();
    	if (property.equals("Status")){
    		if (value)
    			label.setIcon(LIGHT_ON);
    		else
    			label.setIcon(LIGHT_OFF);
    	}
    	else if (property.equals("Failure")){            
    		if (value)
    			label.setIcon(LIGHT_FAIL);
    		else
    			label.setIcon(LIGHT_OFF);
    	}
    	getContentPane().validate();
    	repaint();
		
	}

	
}
