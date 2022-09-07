package DouDiZhuClient.com.wowowo.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Login extends JFrame {
	 private JLabel usenameJLabel;
	 private JTextField usenameJTextField;
	 private JButton btnJButton;
	 private JButton cancelJbButton;
	 
	 public Login() {
		 
		 // ���������Ϣ
		 this.usenameJLabel = new JLabel("�û���");
		 this.usenameJTextField = new JTextField();
		 this.btnJButton = new JButton("��¼");
		 this.cancelJbButton = new JButton("ȡ��");
		 
		 // ���ƽ���
		 this.setSize(400, 300);
		 this.setVisible(true);
		 this.setLocationRelativeTo(null);
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setLayout(new GridLayout(2, 2));
		 
		 // ������������
		 this.add(usenameJLabel);
		 this.add(usenameJTextField);
		 this.add(btnJButton);
		 this.add(cancelJbButton);
		 
		 // ������¼��תҳ��
		 MyEvent myEvent = new MyEvent();
		 this.btnJButton.addActionListener(myEvent);
	 }
	 
	 class MyEvent implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			// ��ȡ�û���
			String uname = usenameJTextField.getText();
			
			// ����Socket
			try {
				Socket socket = new Socket("127.0.0.1", 8000);
				// ����������
				MainJFrame mainJFrame = new MainJFrame(uname, socket);
			}catch (Exception e1) {
				// TODO: handle exception
				e1.printStackTrace();
			}
			
		}
		 
	 }
}
