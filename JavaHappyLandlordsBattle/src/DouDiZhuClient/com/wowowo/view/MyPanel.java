package DouDiZhuClient.com.wowowo.view;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
	public MyPanel(){
		this.setLayout(null);
	}
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		// »æÖÆÍ¼Ïñ
		Image image = new ImageIcon(".//images//bg//bg2.png").getImage();
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
	}

}
