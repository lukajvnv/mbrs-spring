package ourplugin.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2221287090308769922L;

	public LogPanel(String text, int width, int height) {
        initializeUI(text, width, height);
    }

    private void initializeUI(String text, int width, int height) {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(width, height));

        JTextArea textArea = new JTextArea();
        
        textArea.setText(text);
        textArea.setFont(new Font("Arial", Font.BOLD, 18));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.add(scrollPane, BorderLayout.CENTER);
    }
}
