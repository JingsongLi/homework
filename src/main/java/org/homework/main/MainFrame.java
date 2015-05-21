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
		JPanel mainPane = new StudentPanel();
		setContentPane(mainPane);
	}

	private void addMenu(JMenuBar menuBar){

		final MyButton testPaper = new MyButton(" ÊÔ Ìâ ¿â ");
		final MyButton simulate = new MyButton(" Ä£Äâ¿¼³¡ ");
		final MyButton favorite = new MyButton(" ÊÕ ²Ø ¼Ð ");
		final MyButton importL = new MyButton(" µ¼Èë³É¼¨ ");

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
				if(StudentPanel.leftPanel.isVisible())//Î´ÇÐ»»µ½Ä£Äâ¿¼ÊÔ
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
				importClick();
				importL.notClick();
			}
		});
	}

	private void testPaperClick() {
		StudentPanel.leftPanel.setVisible(true);
		ContentPanel.isCollectPanel = false;
		CatalogTree.clickFirst();
	}

	private void simulateClick() {
		ContentPanel.isCollectPanel = false;
		SimulateDialog sd = new SimulateDialog(this);
	}

	private void favoriteClick() {
		StudentPanel.leftPanel.setVisible(true);
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
