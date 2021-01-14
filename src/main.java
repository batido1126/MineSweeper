import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class main {
	static JFrame frame = new JFrame("��a�p");
	static JPanel panel = new JPanel();
	static JButton btns[][];
	static JButton restart = new JButton("Restart");
	static boolean first_step = true;
	static int data[][];
	static int mark[][];
	static int row = 9;
	static int col = 9;
	final static double difficult[] = { 8.1, 6.4, 5.8 };
	static double density = difficult[0];

	static JMenuBar menubar = new JMenuBar();

	static JMenu menu_game = new JMenu("�C��");
	static JMenu menu_gamesize = new JMenu("���e");
	static JMenu menu_difficult = new JMenu("����");
	static JMenu menu_score = new JMenu("����");

	static JMenuItem menuitem_Restart = new JMenuItem("���s�}�l");
	static JMenuItem menuitem_Rule = new JMenuItem("�ާ@�P�W�h");
	static JMenuItem menuitem_9x9 = new JMenuItem("9x9");
	static JMenuItem menuitem_12x12 = new JMenuItem("12x12");
	static JMenuItem menuitem_15x15 = new JMenuItem("15x15");
	static JMenuItem menuitem_18x18 = new JMenuItem("18x18");
	static JMenuItem menuitem_Easy = new JMenuItem("²��");
	static JMenuItem menuitem_Medium = new JMenuItem("����");
	static JMenuItem menuitem_Hard = new JMenuItem("�x��");
	static JMenuItem menuitem_Scoreboard = new JMenuItem("�C������");

	static MouseListener listener_ActLis = new ActLis();
	static ActionListener listener_menuGame = new MenuActionListenerGame();
	static ActionListener listener_menuGamesize = new MenuActionListenerGamesize();
	static ActionListener listener_menuDifficult = new MenuActionListenerDifficult();
	static ActionListener listener_menuScore = new MenuActionListenerScore();

	/* Thread time */
	static MultiThread t2;
	static int time = 0;
	static int finaltime = 0;

	/* Text about time */
	static JLabel label = new JLabel("");

	/* �ާ@�P�W�h */
	static JDialog dialogRule = new JDialog(frame);
	static JTextArea txa = new JTextArea(
			"\n����G�I�}��l\n�k��G�аO�a�p\n\n�W�h�p�U�G\n\n�I����l\n��ܼƦr�N�O�P��K��i�����ê��a�p��\n�Q��k��X�Ҧ����a�p�a�I");

	/* ��� */
	static JDialog dialogWin = new JDialog(frame);
	static JPanel panel2 = new JPanel();
	static JTextField txf = new JTextField(10);
	static JButton btnOK = new JButton("�T�w");
	static OK ok = new OK();

	/* �Ʀ�] */
	static JDialog dialogScore = new JDialog(frame);
	static JTextArea txaScore = new JTextArea();

	public static void main(String args[]) {
		initFrame();
		initGame();
	}

	public static void initFrame() {
		frame.setJMenuBar(menubar);
		menubar.add(menu_game);
		menubar.add(menu_gamesize);
		menubar.add(menu_difficult);
		menubar.add(menu_score);

		menu_game.add(menuitem_Rule);
		menu_game.add(menuitem_Restart);
		menuitem_Rule.addActionListener(listener_menuGame);
		menuitem_Restart.addActionListener(listener_menuGame);

		menu_gamesize.add(menuitem_9x9);
		menu_gamesize.add(menuitem_12x12);
		menu_gamesize.add(menuitem_15x15);
		menu_gamesize.add(menuitem_18x18);
		menuitem_9x9.addActionListener(listener_menuGamesize);
		menuitem_12x12.addActionListener(listener_menuGamesize);
		menuitem_15x15.addActionListener(listener_menuGamesize);
		menuitem_18x18.addActionListener(listener_menuGamesize);

		menu_difficult.add(menuitem_Easy);
		menu_difficult.add(menuitem_Medium);
		menu_difficult.add(menuitem_Hard);
		menuitem_Easy.addActionListener(listener_menuDifficult);
		menuitem_Medium.addActionListener(listener_menuDifficult);
		menuitem_Hard.addActionListener(listener_menuDifficult);

		menu_score.add(menuitem_Scoreboard);
		menuitem_Scoreboard.addActionListener(listener_menuScore);

		GridBagLayout layout = new GridBagLayout();
		frame.setLayout(layout);
		frame.setSize(row * 50, col * 50);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* �ާ@�P�W�h */
		txa.setBackground(SystemColor.control);
		txa.setFont(new Font("�L�n������", Font.PLAIN, 15));
		dialogRule.setSize(300, 300);
		dialogRule.setLocationRelativeTo(null);
		dialogRule.add(txa);
		dialogRule.setTitle("�W�h����");

		/* ��� */
		dialogWin.setTitle("���ߡI�п�J�m�W");
		dialogWin.setSize(250, 100);
		dialogWin.setLocationRelativeTo(null);
		dialogWin.add(panel2);
		panel2.add(txf);
		panel2.add(btnOK);
		txf.setHorizontalAlignment(JTextField.CENTER);
		btnOK.addActionListener(ok);

		/* �Ʀ�] */
		dialogScore.setTitle("�Ʀ�]");
		dialogScore.setSize(500, 750);
		dialogScore.setLocationRelativeTo(null);
		dialogScore.add(txaScore, BorderLayout.NORTH);
		txaScore.setBackground(SystemColor.control);
		txaScore.setFont(new Font("�L�n������", Font.PLAIN, 20));

		/* �ɶ�label */
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		layout.setConstraints(label, gbc);
		frame.add(label);

		frame.setVisible(true);
	}

	public static void initGame() {
		initButton();
		setScenery();
		refreshTheButtonOfGamePanel();
	}

	public static void initButton() {
		// ���s��l��
		panel.setLayout(new GridLayout(row, col));
		panel.setPreferredSize(new Dimension(row * 39, col * 39));

		JButton temp[][] = new JButton[row + 2][col + 2];
		for (int i = 1; i <= row; i++) {
			for (int j = 1; j <= col; j++) {
				temp[i][j] = new JButton();
				temp[i][j].setActionCommand(i + "_" + j); // ���W�r
				temp[i][j].addMouseListener(listener_ActLis);
				panel.add(temp[i][j]);
			}
		}
		btns = temp;

		frame.add(panel);
	}

	/*
	 * Basic setting.
	 * 
	 * It's Initialize the data array to memory the
	 * location(edge=-2,unclicked=-1). It's Initialize the mark array to memory
	 * the mark location.
	 */
	public static void setScenery() {
		int datatemp[][] = new int[row + 2][col + 2];
		int marktemp[][] = new int[row + 2][col + 2];

		for (int i = 0; i < row + 2; i++) {
			for (int j = 0; j < col + 2; j++) {
				if (i == 0 || j == 0 || i == row + 1 || j == col + 1) {
					datatemp[i][j] = -2;
				} else {
					datatemp[i][j] = -1;
				}
			}
		}

		data = datatemp;

		for (int i = 0; i < row + 2; i++) {
			for (int j = 0; j < col + 2; j++) {
				marktemp[i][j] = -1;
			}
		}

		mark = marktemp;
	}

	// Bombs setting(bomb=99).
	public static void setBombs(int click_x, int click_y) {
		int x, y;
		int bombscount = (int) (((double) (row * col)) / density);
		int count = 0; // �ثe�]�m���u�ƶq(�קK����)

		while (true) {
			x = (int) (Math.random() * row) + 1;
			y = (int) (Math.random() * col) + 1;

			if (data[x][y] != 99
					&& !(x >= click_x - 1 && x <= click_x + 1
							&& y >= click_y - 1 && y <= click_y + 1)) {
				data[x][y] = 99;
				count++;
			}
			if (count == bombscount) {
				break;
			}
		}
	}

	// Refresh the game page.
	public static void refreshTheButtonOfGamePanel() {
		for (int i = 1; i <= row; i++) {
			for (int j = 1; j <= col; j++) {
				if (mark[i][j] == 1) {
					btns[i][j].setIcon(new ImageIcon("img/flag.png"));
				} else if (data[i][j] == -1 || data[i][j] == 99) {
					btns[i][j].setIcon(new ImageIcon("img/unclicked.png"));
				} else {
					btns[i][j].setIcon(new ImageIcon("img/" + data[i][j]
							+ ".png"));
				}
			}
		}
	}

	// ���o�P�򬵼u��
	public static int getBombsQuantity(int x, int y) {
		int count = 0;

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				// �D��� �åB�� ���u
				if (data[x + i][y + j] != -2 && data[x + i][y + j] == 99) {
					count++;
				}
			}
		}

		return count;
	}

	// click on the empty area.
	public static void stepOnEmptyArea(int x, int y) {
		if (getBombsQuantity(x, y) == 0) {
			data[x][y] = 0;
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (data[x + i][y + j] == -1) {
						stepOnEmptyArea(x + i, y + j);
					}
				}
			}
		} else {
			data[x][y] = getBombsQuantity(x, y);
		}
	}

	public static void gameOver() {
		for (int i = 1; i <= row; i++) {
			for (int j = 1; j <= col; j++) {
				btns[i][j].removeMouseListener(listener_ActLis);
			}
		}

		// �������
		for (int i = 1; i <= row; i++) {
			for (int j = 1; j <= col; j++) {
				if (data[i][j] == 99) {
					btns[i][j].setIcon(new ImageIcon("img/bomb.png"));
				} else {
					int no = getBombsQuantity(i, j);

					btns[i][j].setIcon(new ImageIcon("img/" + no + ".png"));
				}
			}
		}

		t2.interrupt();
	}

	public static void winJudge() {
		boolean flag = true;

		for (int i = 1; i <= row; i++) {
			for (int j = 1; j <= col; j++) {
				if (data[i][j] == -1) {
					flag = false;
				}
			}
		}

		if (flag) {
			gameOver();
			dialogWin.setVisible(true);
		}
	}

	// ��ܦa��(���ե�, �зǿ�X)
	public static void Test_displayBombs() {
		// �W���и�
		System.out.print("X/Y  ");
		for (int i = 1; i < col + 1; i++) {
			System.out.printf("%3d", i);
		}
		System.out.println();

		for (int i = 0; i < row + 2; i++) {
			for (int j = 0; j < col + 2; j++) {
				// ����s��
				if (j == 0 && i != 0 && i < row + 1) {
					System.out.printf("%2d", i);
				} else if (j == 0) {
					System.out.print("  ");
				}

				// �a�p�Х�
				if (data[i][j] == -1) {
					System.out.print(" _ ");
				} else {
					System.out.printf("%3d", data[i][j]);
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	// ��ܦa��(���ե�, �зǿ�X)
	public static void Test_displayWithoutBombs() {
		// �W���s��
		System.out.print("X/Y  ");
		for (int i = 1; i < col + 1; i++) {
			System.out.printf("%3d", i);
		}
		System.out.println();

		for (int i = 0; i < row + 2; i++) {
			for (int j = 0; j < col + 2; j++) {
				// ����s��
				if (j == 0 && i != 0 && i < row + 1) {
					System.out.printf("%2d", i);
				} else if (j == 0) {
					System.out.print("  ");
				}

				// �a�p�Х�
				if (data[i][j] == -1 || data[i][j] == 99) {
					System.out.print(" _ ");
				} else {
					System.out.printf("%3d", data[i][j]);
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	static class MultiThread extends Thread {
		MultiThread(String name) {
			super(name);
		}

		public void run() {
			if (getName().equals("thread 2")) {
				while (true) {
					try {
						Thread.sleep(1000);
						time++;
						label.setText("�ɶ��G" + time);
					} catch (InterruptedException e) {
						finaltime = time;
						time = 0;
						break;
					}
				}
			}
		}
	}

	// ��ť��(����:��U�@�Ӱϰ� �k��:�аO�a�p)
	static class ActLis implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			int click = e.getButton();

			// ���o�����m
			JButton btn = (JButton) e.getSource();
			String cmd = btn.getActionCommand();
			String temp[] = cmd.split("_");
			int x = Integer.parseInt(temp[0]);
			int y = Integer.parseInt(temp[1]);

			if (click == MouseEvent.BUTTON1) { // �P�_�O�ƹ�������U
				if (first_step) {
					setBombs(x, y);
					t2 = new MultiThread("thread 2");
					t2.start();
					stepOnEmptyArea(x, y);
					refreshTheButtonOfGamePanel();
					first_step = false;
				} else if (mark[x][y] == -1) {
					if (data[x][y] == -1) {
						stepOnEmptyArea(x, y);
						refreshTheButtonOfGamePanel();
						winJudge();
					} else if (data[x][y] == 99) {
						gameOver();
					}
				}

			}

			if (click == MouseEvent.BUTTON3) {// �P�_�O�ƹ��k����U
				if (data[x][y] == -1 || data[x][y] == 99) {
					mark[x][y] *= -1;

					refreshTheButtonOfGamePanel();
				}
			}

			Test_displayBombs(); // Test
		}
	}

	static class MenuActionListenerGame implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String select = e.getActionCommand();

			if (select.equals("���s�}�l")) {
				first_step = true;
				frame.setSize(row * 50, col * 50);
				frame.setLocationRelativeTo(null);
				panel.removeAll();
				label.setText("�ɶ��G" + time);
				initGame();
			} else if (select.equals("�ާ@�P�W�h")) {
				dialogRule.setModal(true); /* dialog�����~�i�H�^��frame */
				dialogRule.setVisible(true);/* ���dialog */
			}
		}
	}

	static class MenuActionListenerGamesize implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String select[] = e.getActionCommand().split("x");

			row = Integer.parseInt(select[0]);
			col = Integer.parseInt(select[1]);
		}
	}

	static class MenuActionListenerDifficult implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String select = e.getActionCommand();

			if (select.equals("²��")) {
				density = difficult[0];
			} else if (select.equals("����")) {
				density = difficult[1];
			} else if (select.equals("�x��")) {
				density = difficult[2];
			}
		}
	}

	static class MenuActionListenerScore implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String select = e.getActionCommand();

			if (select.equals("�C������")) {
				String uri = "data/final.txt";
				File file = new File(uri);

				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

				try {
					String str = "";
					Scanner reader = new Scanner(file);
					while (reader.hasNext()) {
						str += reader.nextLine() + "\n";
					}
					txaScore.setText(str);
					dialogScore.setVisible(true);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}

	/* �s�� JDialog press OK */
	static class OK implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String uri = "data/final.txt";
			File file = new File(uri);

			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			try {
				String str = "";
				Scanner reader = new Scanner(file);
				while (reader.hasNext()) {
					str += reader.nextLine() + "\n";
				}
				PrintWriter out = new PrintWriter(file);
				str += txf.getText() + "�G" + finaltime + "��\n";
				out.println(str);
				txf.setText("");
				out.close();
				dialogWin.setVisible(false);
				System.out.println("�s�ɦ��\�I");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
