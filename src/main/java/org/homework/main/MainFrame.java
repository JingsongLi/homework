package org.homework.main;

import org.homework.db.model.User;
import org.homework.io.IoOperator;
import org.homework.student.CatalogTree;
import org.homework.student.ContentPanel;
import org.homework.student.SimulateDialog;
import org.homework.teacher.TScoreQuery;
import org.homework.login.LoginPanel;
import org.homework.utils.MyButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MainFrame extends JFrame {

	public static final String PRE = "images\\";
	public static final String MENU_TESTPAPER = "testPaper.JPG";
	public static final String MENU_SIMULATE = "simulate.JPG";
	public static final String MENU_FAVORITE = "favorite.JPG";
	public static final String MENU_IMPORT = "import.JPG";
	private static TeacherPanel teacherPane;
	private static StudentPanel studentPanel;
	private User user;


	/**
	 * Create the frame.
	 */
	public MainFrame(User user) {
		this.user = user;
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

		//2.下面的面板
		studentPanel = new StudentPanel();
		teacherPane = new TeacherPanel();
		setContentPane(studentPanel);
	}

	private void addMenu(JMenuBar menuBar){

		final MyButton testPaper = new MyButton(" 试 题 库 ");
		final MyButton simulate = new MyButton(" 模拟考场 ");
		final MyButton favorite = new MyButton(" 收 藏 夹 ");
		final MyButton importL = new MyButton(" 导入成绩 ");


		testPaper.setBorder(new EmptyBorder(7, 8, 7, 8));
		simulate.setBorder(new EmptyBorder(7, 8, 7, 8));
		favorite.setBorder(new EmptyBorder(7, 8, 7, 8));
		importL.setBorder(new EmptyBorder(7, 8, 7, 8));

		testPaper.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		simulate.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		favorite.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		importL.setFont(new Font("微软雅黑", Font.PLAIN, 15));

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
				setContentPane(studentPanel);
			}
		});
		simulate.addMyMouse(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				simulateClick();
				if (StudentPanel.leftPanel.isVisible())//未切换到模拟考试
					simulate.notClick();
				else {
					testPaper.notClick();
					favorite.notClick();
					importL.notClick();
					setContentPane(studentPanel);
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
				setContentPane(studentPanel);
			}
		});
		importL.addMyMouse(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				importClick();
				importL.notClick();
			}
		});



		final MyButton correctHomework = new MyButton(" 批改作业 ");
		correctHomework.addMyMouse(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setContentPane(teacherPane);
			}
		});
		menuBar.add(correctHomework);

		final MyButton createTestPaper = new MyButton(" 生成试卷 ");
        createTestPaper.addMyMouse(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setContentPane(teacherPane);
			}
		});
		menuBar.add(createTestPaper);

        final MyButton scoreQuery = new MyButton(" 成绩查询 ");
        scoreQuery.addMyMouse(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //setContentPane(teacherPane);
                scoreQueryClick();
            }
        });
        menuBar.add(scoreQuery);
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

    private void scoreQueryClick() {
        TScoreQuery tScoreQ = new TScoreQuery(this);
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
					new LoginPanel();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
