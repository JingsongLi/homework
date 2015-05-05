package org.homework.main;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import static org.homework.utils.Utils.*;


public class MainFrame extends JFrame {

	public static final String PRE = "images\\";
	public static final String MENU_TESTPAPER = "testPaper.JPG";
	public static final String MENU_SIMULATE = "simulate.JPG";
	public static final String MENU_FAVORITE = "favorite.JPG";


	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 740, 736);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setMargin(new Insets(1, 1, 1, 1));
		menuBar.setPreferredSize(new Dimension(-1, 74));
	
		setJMenuBar(menuBar);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(getIcon(PRE + MENU_TESTPAPER));
		lblNewLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				testPaperClick();
			}
		});
		lblNewLabel.setPreferredSize(new Dimension(59, -1));
		menuBar.add(lblNewLabel);
		
		JLabel label = new JLabel("");
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				simulateClick();
			}
		});
		label.setIcon(getIcon(PRE + MENU_SIMULATE));
		label.setPreferredSize(new Dimension(59, -1));
		menuBar.add(label);
		
		JLabel label_1 = new JLabel("");
		label_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				favoriteClick();
			}
		});
		label_1.setIcon(getIcon(PRE + MENU_FAVORITE));
		label_1.setPreferredSize(new Dimension(59, -1));
		menuBar.add(label_1);

		JLabel label_2 = new JLabel("");
		label_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				importClick();
			}
		});
		label_2.setIcon(getIcon(PRE + MENU_FAVORITE));
		label_2.setPreferredSize(new Dimension(59, -1));
		menuBar.add(label_2);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setPreferredSize(new Dimension(280, -1));
		contentPane.add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBackground(Color.WHITE);
		panel_2.setPreferredSize(new Dimension(-1, 30));
		panel.add(panel_2, BorderLayout.NORTH);
		
		JLabel lblNewLabel_1 = new JLabel("\u9898\u5E93\u76EE\u5F55");
		panel_2.add(lblNewLabel_1);

		JTree tree = new JTree();
		JScrollPane scrollPane = new JScrollPane(tree);
		panel.add(scrollPane, BorderLayout.CENTER);


		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBackground(Color.WHITE);
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel contentPanel = new JPanel();
		JScrollPane contentSP = new JScrollPane(contentPanel);
		panel_1.add(contentSP, BorderLayout.CENTER);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_3.setPreferredSize(new Dimension(-1, 10));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_4.setPreferredSize(new Dimension(-1, 374));
		
		
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.add(panel_3);
		
		JLabel label_3 = new JLabel("\u5355\u9879\u9009\u62E9\u9898");
		panel_3.add(label_3);
		contentPanel.add(panel_4);
		panel_4.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("1.\u7EC4\u6210\u4EBA\u4F53\u6700\u57FA\u672C\u7684\u7ED3\u6784\u548C\u529F\u80FD\u5355\u4F4D\u662F        ");
		lblNewLabel_2.setBounds(10, 39, 304, 15);
		panel_4.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("\u7EC6\u80DE ");
		lblNewLabel_3.setBounds(46, 74, 54, 15);
		panel_4.add(lblNewLabel_3);
		
		JLabel label_4 = new JLabel("\u7EC6\u80DE ");
		label_4.setBounds(46, 96, 54, 15);
		panel_4.add(label_4);
		
		JLabel label_5 = new JLabel("\u7EC6\u80DE ");
		label_5.setBounds(46, 123, 54, 15);
		panel_4.add(label_5);
		
		JLabel label_6 = new JLabel("\u7EC6\u80DE ");
		label_6.setBounds(46, 149, 54, 15);
		panel_4.add(label_6);
		
		JLabel lblNewLabel_4 = new JLabel("\u663E\u793A\u7B54\u6848");
		lblNewLabel_4.setBounds(174, 179, 54, 15);
		panel_4.add(lblNewLabel_4);
		
		JLabel label_7 = new JLabel("\u6536\u85CF\u672C\u9898");
		label_7.setBounds(238, 179, 54, 15);
		panel_4.add(label_7);
		
		JLabel label_8 = new JLabel("\u4F5C\u4E1A\u7B14\u8BB0");
		label_8.setBounds(307, 179, 54, 15);
		panel_4.add(label_8);
		
	}

	private void testPaperClick() {
		System.out.println("fuck you~ testPaperClick!");
	}

	private void simulateClick() {
		System.out.println("fuck you~ simulateClick!");
	}

	private void favoriteClick() {
		System.out.println("fuck you~ favoriteClick!");
	}

	private void importClick() {
		System.out.println("fuck you~ importClick!");
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setExtendedState(Frame.MAXIMIZED_BOTH);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
