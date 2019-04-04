package client.panel;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class HelpPanel extends JDialog{
	public HelpPanel() {
		setLayout(null);
		setSize(750, 599);
		
		JLabel lblNewLabel = new JLabel(new ImageIcon(ClassLoader.getSystemResource("image/help.png")));
		lblNewLabel.setBounds(0, 0, 750, 599);
		add(lblNewLabel);
	}
}
