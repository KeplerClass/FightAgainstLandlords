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
		 
		 // 创建组件信息
		 this.usenameJLabel = new JLabel("用户名");
		 this.usenameJTextField = new JTextField();
		 this.btnJButton = new JButton("登录");
		 this.cancelJbButton = new JButton("取消");
		 
		 // 绘制界面
		 this.setSize(400, 300);
		 this.setVisible(true);
		 this.setLocationRelativeTo(null);
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setLayout(new GridLayout(2, 2));
		 
		 // 添加组件到界面
		 this.add(usenameJLabel);
		 this.add(usenameJTextField);
		 this.add(btnJButton);
		 this.add(cancelJbButton);
		 
		 // 单击登录跳转页面
		 MyEvent myEvent = new MyEvent();
		 this.btnJButton.addActionListener(myEvent);
	 }
	 
	 class MyEvent implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			// 获取用户名
			String uname = usenameJTextField.getText();
			
			// 传入Socket
			try {
				Socket socket = new Socket("127.0.0.1", 8000);
				// 传入主窗口
				MainJFrame mainJFrame = new MainJFrame(uname, socket);
			}catch (Exception e1) {
				// TODO: handle exception
				e1.printStackTrace();
			}
			
		}
		 
	 }
}
