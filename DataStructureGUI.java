package teamproject;

import javax.swing.*;
import java.awt.*;

public class DataStructureGUI extends JFrame {

    public DataStructureGUI() {
        setTitle("자료구조");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        
        JButton btn1 = new JButton("Stack");
        c.add(btn1);
        JButton btn2 = new JButton("Queue");
        c.add(btn2);
        
        MyPanel panel = new MyPanel();
        c.add(panel);
        
        setVisible(true);
    }

    class MyPanel extends JPanel{
    	private JLabel message = new JLabel("보고싶은 구조를 선택하세요");

    	 private MyPanel() {

             message.setFont(new Font("고딕", Font.PLAIN, 20));
             add(message);
             
    	 }
    }
    
    public static void main(String[] args) {
        new DataStructureGUI();
    }
}


