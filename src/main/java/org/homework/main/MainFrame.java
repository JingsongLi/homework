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

	ContentPanel contentPanel;

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 740, 736);

		//1.menu
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

		//2.下面的面板
		JPanel mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setLayout(new BorderLayout(0, 0));
		setContentPane(mainPane);

		//2.1 右显示
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBackground(Color.WHITE);
		mainPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		contentPanel = ContentPanel.getContentPanel();
		JScrollPane contentSP = new JScrollPane(contentPanel);
		panel_1.add(contentSP, BorderLayout.CENTER);

		//2.2 左目录
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setPreferredSize(new Dimension(280, -1));
		mainPane.add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBackground(Color.WHITE);
		panel_2.setPreferredSize(new Dimension(-1, 30));
		panel.add(panel_2, BorderLayout.NORTH);
		
		JLabel lblNewLabel_1 = new JLabel("\u9898\u5E93\u76EE\u5F55");
		panel_2.add(lblNewLabel_1);

		CatalogTree cata = new CatalogTree();
		JScrollPane scrollPane = new JScrollPane(cata.getTree());
		panel.add(scrollPane, BorderLayout.CENTER);



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
	public static void main(String[] args) throws InterruptedException {
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
