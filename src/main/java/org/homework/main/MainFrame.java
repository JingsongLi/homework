package org.homework.main;

import org.homework.io.IoOperator;
import org.homework.student.CatalogTree;
import org.homework.student.ContentPanel;
import org.homework.student.SimulateDialog;
import org.homework.utils.MyButton;
import sun.plugin2.util.ColorUtil;

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
	public static final String MENU_IMPORT = "import.JPG";
	public static JPanel leftPanel;

	ContentPanel contentPanel;

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(Frame.MAXIMIZED_BOTH);

		//1.menu
		JMenuBar menuBar = new JMenuBar();
		menuBar.setMargin(new Insets(1, 1, 1, 1));
//		menuBar.setMaximumSize(new Dimension(60, 500));
//		menuBar.setMinimumSize(new Dimension(60, 100));
		menuBar.setPreferredSize(new Dimension(60, 40));

		setJMenuBar(menuBar);

		addMenu(menuBar);



		//2.ÏÂÃæµÄÃæ°å
		JPanel mainPane = new JPanel();
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPane.setLayout(new BorderLayout(0, 0));
		setContentPane(mainPane);

		//2.1 ÓÒÏÔÊ¾
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBackground(Color.WHITE);
		mainPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		contentPanel = ContentPanel.getContentPanel();
		panel_1.add(contentPanel.getScrollPane(), BorderLayout.CENTER);

		//2.2 ×óÄ¿Â¼
		leftPanel = new JPanel();
		leftPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setPreferredSize(new Dimension(280, -1));
		mainPane.add(leftPanel, BorderLayout.WEST);
		leftPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBackground(new Color(184, 208, 238));
		panel_2.setPreferredSize(new Dimension(-1, 30));
		leftPanel.add(panel_2, BorderLayout.NORTH);
		
		JLabel lblNewLabel_1 = new JLabel("Ìâ¿âÄ¿Â¼");
		lblNewLabel_1.setFont(new Font("ËÎÌå", Font.BOLD, 15));
		panel_2.add(lblNewLabel_1);

		CatalogTree cata = new CatalogTree();
		JScrollPane scrollPane = new JScrollPane(cata.getTree());
		leftPanel.add(scrollPane, BorderLayout.CENTER);



	}

	private void addMenu(JMenuBar menuBar){

		final MyButton testPaper = new MyButton(" ÊÔ Ìâ ¿â ");
		final MyButton simulate = new MyButton(" Ä£Äâ¿¼³¡ ");
		final MyButton favorite = new MyButton(" ÊÕ ²Ø ¼Ð ");
		final MyButton importL = new MyButton(" µ¼Èë³É¼¨ ");

//		JLabel testPaper = new JLabel("");
//		testPaper.setIcon(getIcon(PRE + MENU_TESTPAPER));
//
//		JLabel simulate = new JLabel("");
//		simulate.setIcon(getIcon(PRE + MENU_SIMULATE));
//		simulate.setPreferredSize(new Dimension(59, -1));
//		JLabel favorite = new JLabel("");
//		favorite.setIcon(getIcon(PRE + MENU_FAVORITE));
//		favorite.setPreferredSize(new Dimension(59, -1));
//		menuBar.add(favorite);
//		JLabel importL = new JLabel("");
//		importL.setIcon(getIcon(PRE + MENU_IMPORT));
//		importL.setPreferredSize(new Dimension(59, -1));
//		menuBar.add(importL);

		testPaper.setBorder(new EmptyBorder(7, 8, 7, 8));
		simulate.setBorder(new EmptyBorder(7, 8, 7, 8));
		favorite.setBorder(new EmptyBorder(7, 8, 7, 8));
		importL.setBorder(new EmptyBorder(7, 8, 7, 8));

		testPaper.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		simulate.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		favorite.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));
		importL.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 15));

		testPaper.setMinimumSize(new Dimension(100, 50));
		simulate.setPreferredSize(new Dimension(80, 59));
		favorite.setPreferredSize(new Dimension(80, 40));
		importL.setPreferredSize(new Dimension(80, 40));

		menuBar.add(testPaper);
		menuBar.add(simulate);
		menuBar.add(favorite);
		menuBar.add(importL);

		testPaper.addMyMouse(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				simulate.notClick();
				favorite.notClick();
				importL.notClick();
				testPaperClick();
			}
		});
		simulate.addMyMouse(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				simulateClick();
				if(MainFrame.leftPanel.isVisible())//Î´ÇÐ»»µ½Ä£Äâ¿¼ÊÔ
					simulate.notClick();
				else {
					testPaper.notClick();
					favorite.notClick();
					importL.notClick();
				}
			}
		});
		favorite.addMyMouse(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				testPaper.notClick();
				simulate.notClick();
				importL.notClick();
				favoriteClick();
			}
		});
		importL.addMyMouse(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				testPaper.notClick();
//				simulate.notClick();
//				favorite.notClick();
				importClick();
				importL.notClick();
			}
		});
	}

	private void testPaperClick() {
		MainFrame.leftPanel.setVisible(true);
		ContentPanel.isCollectPanel = false;
		CatalogTree.clickFirst();
	}

	private void simulateClick() {
		ContentPanel.isCollectPanel = false;
		SimulateDialog sd = new SimulateDialog(this);
	}

	private void favoriteClick() {
		MainFrame.leftPanel.setVisible(true);
		ContentPanel.isCollectPanel = true;
		CatalogTree.clickFirst();
	}

	private void importClick() {
		IoOperator.importScore();
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws InterruptedException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					} catch (Exception e) {
						e.printStackTrace();
					}
					MainFrame frame = new MainFrame();
					frame.setTitle("×÷ÒµÏµÍ³");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
