package ourplugin.action;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.nomagic.magicdraw.actions.MDAction;

import ourplugin.util.LogHelperClass;
import ourplugin.util.LogPanel;

/** Action that activate code generation */
@SuppressWarnings("serial")
public class ViewLogAction extends MDAction{
	
	
	public ViewLogAction(String name) {			
		super("", name, null, null);		
	}

	public void actionPerformed(ActionEvent evt) {
//		JOptionPane.showMessageDialog(null, "Works in development mode does not work in production");
		try {
			List<String> logs = LogHelperClass.getLogHelperClass().loadFromFile();
			String logText = String.join("\n", logs);

	        JFrame frame = new JFrame("View logs");
	        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();
			int width = screenSize.width;
			int height = screenSize.height;
			frame.setSize(width*3/4, height*3/4);
	        frame.setLocationRelativeTo(null);
	        
	        JPanel panel = new LogPanel(logText, width / 2, height / 2);
	        panel.setOpaque(true);
	        frame.setContentPane(panel);
	        frame.pack();
	        frame.setVisible(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
	}
	

}