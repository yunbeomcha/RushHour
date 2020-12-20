import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.sound.sampled.*;

public class Puzzle extends JPanel {
	private JButton btn[];
	private JLabel lbl[];
	private Draw draw;
	private Board board;
	private Piece[] pc_List;
	private Piece temp;
	private Move Undo;
	private int min_count, puzzlenum = 0, finish = 0;
	private static JLabel lblCount;
	private static int _count = 0;
	private ClearLabelThread1 lblClear;
	private ImagePanel panel;
	private DrawListener Listen;
	private int startx, starty, endx, endy, t_x, t_y, best = 0;
	private JMenuBar menuBar;
	private JMenu MenuSkin;
	private GUI GUI;
	private Clip clip;

	public Puzzle(String desc, int puzIdx, GUI G) {

//		setBounds(200, 200, 510, 720); // ������ġ, ������ġ, ���α���, ���α���
		setBounds(0, 0, 520, 750); // ������ġ, ������ġ, ���α���, ���α���
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // x������ ����
		setLayout(null); // ��ġ ������ ��� �� ��
//		setResizable(false); // maximize button disable
		Play("src/sound/2.wav");
		Font fnt14 = new Font("Gadugi", Font.PLAIN, 14); // ��Ʈ ����

		Undo = new Move(); // ���� ��ü ����
		pc_List = Piece.serialToPieces(desc); // �ǽ� ����Ʈ ����
		board = new Board(6, pc_List, desc); // ���� ����
//		setTitle("Puzzle " + puzIdx); // Ÿ��Ʋ ����
		puzzlenum = puzIdx - 1;
		
		best = G.getRecord(puzzlenum);
		min_count = ExecGo.puzzleMinCount(board);

		if (puzIdx == 1 || puzIdx == 2 || puzIdx == 3 || puzIdx == 4) {
			panel = new ImagePanel(new ImageIcon("src/img/���14.png").getImage()); // ��� �г�
			add(panel); // �����ӿ� �߰�
		}
		if (puzIdx == 5 || puzIdx == 6 || puzIdx == 7 || puzIdx == 8) {
			panel = new ImagePanel(new ImageIcon("src/img/���15.png").getImage()); // ��� �г�
			add(panel); // �����ӿ� �߰�
		}
		if (puzIdx == 9 || puzIdx == 10 || puzIdx == 11 || puzIdx == 12) {
			panel = new ImagePanel(new ImageIcon("src/img/���13.png").getImage()); // ��� �г�
			add(panel); // �����ӿ� �߰�
		}

		Listen = new DrawListener(); // ������ ����
		btn = new JButton[4]; // ��ư ����
		lbl = new JLabel[4]; // �� ����

		GUI = G;
		menuBar = G.menuBar;

//		setJMenuBar(menuBar);	//	�޴��� ����
		menuBar.setBackground(Color.white); // �޴��� ���� ����

		MenuSkin = new JMenu("Skin"); // �޴� ����
		MenuSkin.setHorizontalTextPosition(SwingConstants.CENTER); // ��ġ ����
		MenuSkin.setHorizontalAlignment(SwingConstants.CENTER); // ��ġ ����
		MenuSkin.setBounds(10, 10, 70, 50); // ũ�� �� ��ġ ����
		MenuSkin.setIconTextGap(51);
		MenuSkin.setFont(fnt14); // ��Ʈ ����
		menuBar.add(MenuSkin); // �޴��ٿ� �߰�

		String txtMenuItem[] = new String[] { "Redicon", "Blueicon", "Greenicon", "Redcar", "Bluecar", "Greencar" }; // �޴�
																														// ������
																														// ���ڿ�
																														// �迭��
																														// ����

		JMenuItem tItem; // �޴� ������ ���� ����
		for (int i = 0; i < txtMenuItem.length; i++) {
			String tmpUrl = "src/img/" + txtMenuItem[i] + ".png"; // �޴� ������ �̹��� ��Ʈ������ �ޱ�
			tItem = new JMenuItem(new ImageIcon(tmpUrl)); // �޴� ������ ����
			tItem.setText(txtMenuItem[i]); // �ؽ�Ʈ ����
			tItem.setForeground(Color.white); // ��Ʈ ���� ����
			tItem.addActionListener(Listen); // �����ʿ� �߰�
			tItem.setBackground(Color.white); // ��� ���� ����
			tItem.setSize(27, 5); // ������ ����
			tItem.setFont(fnt14); // ��Ʈ ����
			MenuSkin.add(tItem); // �޴��� ������ �߰�
		}

		btn[0] = new JButton("Hint"); // ��ư ����
		btn[0].setBounds(100, 10, 70, 50); // ��ġ �� ũ�� ����
		btn[0].setHorizontalTextPosition(SwingConstants.CENTER); // ��ġ ����
		btn[0].setHorizontalAlignment(SwingConstants.CENTER); // ��ġ ����
		btn[0].setIconTextGap(31);
		btn[0].setFont(fnt14); // ��Ʈ ����

		btn[1] = new JButton(new ImageIcon("src/img/reset5.png")); // ���� ��ư �̹��� ����
		btn[1].setBounds(128, 40, 50, 50); // ��ġ �� ũ�� ����

		btn[2] = new JButton(new ImageIcon("src/img/undo5.png")); // �ڷ� ��ư �̹��� ����
		btn[2].setBounds(291, 40, 50, 50); // ��ġ �� ũ�� ����

		btn[3] = new JButton(new ImageIcon("src/img/�ڷ�.jpg")); // �ڷ� ��ư �̹��� ����
		btn[3].setBounds(390, 40, 50, 50); // ��ġ �� ũ�� ����

		for (int i = 0; i < 4; i++) {
			btn[i].setBorderPainted(false); // �׵θ� �����
			btn[i].setContentAreaFilled(false); // ��ư ���� �����
			btn[i].setFocusPainted(false); // ���ý� �׵θ� �����
			btn[i].addActionListener(Listen); // �����ʿ��� �ν�
			panel.add(btn[i]); // �гο� �߰�
		}
		menuBar.add(btn[0]);
		// pn1.setBackground(Color.white);
		// add(pn1);

		lbl[0] = new JLabel("Reset"); // �� ����
		lbl[0].setFont(fnt14); // ��Ʈ ����
		lbl[0].setBounds(134, 70, 70, 70); // ��ġ �� ũ�� ����
		lbl[0].setForeground(Color.darkGray);

		lbl[1] = new JLabel("Undo"); // �� ����
		lbl[1].setFont(fnt14); // ��Ʈ ����
		lbl[1].setBounds(300, 70, 70, 70); // ��ġ �� ũ�� ����
		lbl[1].setForeground(Color.darkGray);

		_count = 0;

		lblCount = new JLabel("Count / Best : " + _count + " / " + best); // Count �� ����
		lblCount.setBounds(320, 160, 180, 40); // �� ��ġ, ������
		lblCount.setFont(fnt14); // font����
		lblCount.setForeground(Color.darkGray);
		panel.add(lblCount);

		lbl[2] = new JLabel("Min: " + min_count); // Count �� ����
		lbl[2].setBounds(80, 160, 180, 40); // �� ��ġ, ������
		lbl[2].setFont(fnt14); // font����
		lbl[2].setForeground(Color.darkGray);

		lbl[3] = new JLabel("Menu"); // Count �� ����
		lbl[3].setBounds(400, 70, 70, 70); // �� ��ġ, ������
		lbl[3].setFont(fnt14); // font����
		lbl[3].setForeground(Color.darkGray); // ��Ʈ ����

		lblClear = new ClearLabelThread1("CLEAR"); // Ŭ���� ��
		lblClear.setBounds(120, 250, 250, 60); // ��ġ �� ũ�� ����
		lblClear.setFont(new Font("Verdata", Font.BOLD, 50)); // ��Ʈ ����
		lblClear.setHorizontalAlignment(SwingConstants.CENTER); // ��ġ ����
		lblClear.setVisible(false); // �� �Ⱥ��̰� �ϱ�
		panel.add(lblClear);

		for (int i = 0; i < 4; i++)
			panel.add(lbl[i]);
		draw = new Draw(board); // Draw �г� ����
		draw.addMouseListener(Listen); // ���콺 �����ʿ��� �ν�
		draw.addMouseMotionListener(Listen); // ���콺 �����ʿ��� �ν�
		panel.add(draw); // �гο� draw �г� �߰�
		draw.addKeyListener(Listen); // Ű ������ �ν�
		setVisible(true); // ���̰� �ϱ�
	}

	private void Play(String fileName) {
		 try
	        {
	            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(fileName));
	            clip = AudioSystem.getClip();
	            //clip.stop();
	            clip.open(ais);
	            clip.start();
	            clip.loop(-1);
	        }
	        catch (Exception ex)
	        {
	        }
		
	}
	private void Stop(String fileName) {
		clip.stop();
		clip.close();
	}

	public class ImagePanel extends JPanel {
		private Image backgroundimg; // �̹��� ����

		public ImagePanel(Image img) {
			this.backgroundimg = img; // �̹��� ����
			setSize(new Dimension(img.getWidth(null), img.getHeight(null))); // ������ ����
			setPreferredSize(new Dimension(491, 643)); // ���� ����
			setLayout(null); // ��ġ ������ ��� �� ��
		}

		public void paintComponent(Graphics g) {
			g.drawImage(backgroundimg, 0, 0, null); // ��� �̹��� �׸���
			draw.repaint(); // draw �ٽ� �׸���
		}
	}

	public static int eventSelect(Board basic, int Serial, int m) {
		int finish = 0;

		try {
			Piece tmp = basic.findPiece(Serial); // �ǽ� ����
			tmp.movePiece(m); // �ǽ� �̵�
			finish = basic.updateBoard(); // ���� ������Ʈ
//			boolean minus = basic.couldMoveMinus(tmp); // �ڷ� �̵� ���� ����
//			boolean plus = basic.couldMovePlus(tmp); // ������ �̵� ���� ����
//			if (m == -1 && minus || m == 1 && plus) { // ���̳� �ڷ� �̵� �����ϴٸ�
//				tmp.movePiece(m); // �ǽ� �̵�
//				finish = basic.updateBoard(); // ���� ������Ʈ
//			} else {
//				throw new Exception();
//			}

		} catch (Exception e) {
//			_count--; // ī��Ʈ ����
//			lblCount.setText("Count / Best : " + _count + " / " + best); // ī��Ʈ �ؽ�Ʈ ����
//			System.out.println("�����߻�");
		}
		return finish; // ���� ��ȣ ��ȯ
	} // �̺�Ʈ ����

	public class DrawListener implements MouseListener, MouseMotionListener, KeyListener, ActionListener {

		@Override
		public void mouseClicked(MouseEvent e) { // ���콺�� Ŭ���Ǹ�
			int signal = 0; // �ñ׳� ����
			for (Piece piece : pc_List) { // �ǽ� �ݺ�
				if (piece.getSize() == 1) {
					break;
				} else { // �ǽ� ����� 2���
					if ((e.getX() / 70 >= piece.getX1() && (e.getX() / 70 <= piece.getX2())
							&& (e.getY() / 70 >= piece.getY1() && e.getY() / 70 <= piece.getY2()))) { // Ŭ���� ������ �ǽ���
																										// ���ԵǸ�
						temp = piece; // �ǽ� ����
						signal = 1; // �ñ׳� ����
						break;
					}
				}
			}
			if (signal == 0) { // �ñ׳��� 0�̶��
				draw.setTemp(-1); // ���õ� �� ����
				temp = null;
			} else {// 1�̶��
				draw.setTemp(temp.getSerial()); // ���õ� �ǽ� ����
			}
			draw.repaint(); // �ٽ� �׸���
			draw.requestFocus();
			setFocusable(true);
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void actionPerformed(ActionEvent Ev) {
			Object obj = Ev.getSource(); // ��ư ��ü �ν�
			if (btn[2] == obj) { // �ڷ� ��ư �̶��
				int undoObj;
				int undoMove;
				try {
					undoObj = Undo.undoObj(); // �ǵ��� �ǽ��� �ø��� ����
					undoMove = Undo.undoMove(); // �ǵ��� �ǽ��� ������ ����
					Piece undoPiece = Undo.findPiece(board, undoObj); // �ǽ� ����
					if (undoMove > 0) {
						for (int i = 0; i < undoMove; i++)
							undoPiece.movePiece(-1);
					} else {
						for (int i = 0; i > undoMove; i--)
							undoPiece.movePiece(1);
					}
					board.updateBoard(); // ���� ������Ʈ
					Undo.setIndex(Undo.getIndex() - 1); // �ε��� ����
					_count--; // ī��Ʈ ����
					lblCount.setText("Count / Best : " + _count + " / " + best); // ī��Ʈ �ؽ�Ʈ ����
				} catch (ArrayIndexOutOfBoundsException e) { // ó�����¶��
//					lblCount.setText("Count: " + _count); // ī��Ʈ �ؽ�Ʈ ����
//					JOptionPane bb = new JOptionPane();
//					bb.showMessageDialog(null, "ó�� ���� �Դϴ�."); // ���̾�α� ����
				}
				draw.setTemp(-1);
				draw.repaint();
			} else if (obj == btn[1]) { // ���� ��ư�̶��
				try {
					Undo.reset(board, board.get_serial()); // ���� ȣ��
					pc_List = board.getPieces(); // �ǽ� ����Ʈ �ٽ� ����
					temp = null; // ���� ����
					_count = 0; // ī��Ʈ �ʱ�ȭ
					lblCount.setText("Count / Best : " + _count + " / " + best); // ī��Ʈ �ؽ�Ʈ ����
				} catch (ArrayIndexOutOfBoundsException e) { // ó�����¶��
//					lblCount.setText("Count: " + _count); // ī��Ʈ �ؽ�Ʈ ����
//					JOptionPane bb = new JOptionPane();
//					bb.showMessageDialog(null, "ó�� ���� �Դϴ�."); // ���̾�α� ����
				}
				draw.setTemp(-1);
				draw.repaint(); // �ٽ� �׸���
			} else if (obj == btn[0]) {
				try {
					if (Undo.getIndex() == 99999) {
//						JOptionPane bb = new JOptionPane();
//						bb.showMessageDialog(null, "�̵��Ұ�.");
						throw new Exception();
					}

					// System.out.println("called");
					String hint = ExecGo.getNextSolution(board);
					int pieceSerial = hint.charAt(0) - 'A';
					int m = hint.charAt(2) - '0';
					pieceSerial += board.getWallNum(pieceSerial);

					int dir = hint.charAt(1) == '+' ? m : -m;
					temp = board.findPiece(pieceSerial);

					finish = eventSelect(board, temp.getSerial(), dir);
					Undo.setUndo(dir, temp.getSerial());
					_count++;
					lblCount.setText("Count / Best : " + _count + " / " + best);

					board.updateBoard();
					draw.repaint();

					if (finish == 1) {
						lblClear.setVisible(true);
						lblClear.setDelayTime(100);
						lblClear.start();
						JOptionPane bb = new JOptionPane();
						bb.showMessageDialog(null, "���� �ذ�!");
						setVisible(false);
						finish = 1;
						GUI.menuBar.remove(MenuSkin);
						GUI.menuBar.remove(btn[0]);
						GUI.Clear();
						Stop("src/sound/2.wav");
						Play("src/sound/1.wav");

//						dispose();
					}
				} catch (Exception E) {

				}
			} else if (obj == btn[3]) {
				int exitOption = JOptionPane.showConfirmDialog(null, "�޴��� ���ư��ðڽ��ϱ�?", "", JOptionPane.YES_NO_OPTION);
				if (exitOption == JOptionPane.YES_OPTION) {
					setVisible(false);
					finish = 0;
					GUI.menuBar.remove(MenuSkin);
					GUI.menuBar.remove(btn[0]);
					GUI.Clear();
					Stop("src/sound/2.wav");
					Play("src/sound/1.wav");
				}
			} else {
				JMenuItem mi = (JMenuItem) (Ev.getSource()); // ������Ʈ�� ���������� �޾ƿ�
				switch (mi.getText()) { // ������Ʈ�� �ؽ�Ʈ�� ����
				case "Redicon":
					draw.changeSkin(new Color(214, 66, 80), 0);
					draw.setSkin(0);
					break;
				case "Blueicon":
					draw.changeSkin(new Color(5, 128, 229), 1);
					draw.setSkin(0);
					break;
				case "Greenicon":
					draw.changeSkin(new Color(97, 154, 72), 2);
					draw.setSkin(0);
					break;
				case "Redcar":
					draw.changeSkin(redcar);
					draw.setSkin(1);
					break;
				case "Bluecar":
					draw.changeSkin(bluecar);
					draw.setSkin(1);
					break;
				case "Greencar":
					draw.changeSkin(greencar);
					draw.setSkin(1);
					break;
				}
				draw.repaint();
			}

		}

		@Override
		public void mousePressed(MouseEvent e) {
			int signal = 0; // �ñ׳� ����
			for (Piece piece : pc_List) { // �ǽ� �ݺ�
				if (piece.getSize() == 1) {
					break;
				} else { // �ǽ� ����� 2���
					if ((e.getX() / 70 >= piece.getX1() && (e.getX() / 70 <= piece.getX2())
							&& (e.getY() / 70 >= piece.getY1() && e.getY() / 70 <= piece.getY2()))) { // Ŭ���� ������ �ǽ���
																										// ���ԵǸ�
						temp = piece; // �ǽ� ����
						signal = 1; // �ñ׳� ����
						break;
					}
				}
			}
			if (signal == 0) { // �ñ׳��� 0�̶��
				draw.setTemp(-1); // ���õ� �� ����
				temp = null;
			} else {// 1�̶��
				draw.setTemp(temp.getSerial()); // ���õ� �ǽ� ����
				startx = e.getX();
				starty = e.getY();
				//System.out.println(startx + ", " + starty);
				draw.setTxy((temp.getX1() + 1) * 70 + 10 - 70, (temp.getY1() + 1) * 70 + 10 - 70);
			}
			draw.repaint(); // �ٽ� �׸���
			draw.requestFocus();
			setFocusable(true);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (draw.getTemp() != -1) {
				endx = e.getX();
				endy = e.getY();
				if (temp.getStride() == 0) {
					t_x = (temp.getX1() + 1) * 70 - 70 + endx - startx;
					if (endx - startx > getSpace(1) * 70) {
						t_x = (temp.getX1() + 1) * 70 - 70 + getSpace(1) * 70;
					}
					if (endx - startx < getSpace(-1) * 70) {
						t_x = (temp.getX1() + 1) * 70 - 70 + getSpace(-1) * 70;
					}
					draw.setTxy(t_x + 10, 0);
				} else {
					t_y = (temp.getY1() + 1) * 70 - 70 + endy - starty;
					if (endy - starty > getSpace(1) * 70) {
						t_y = (temp.getY1() + 1) * 70 - 70 + getSpace(1) * 70;
					}
					if (endy - starty < getSpace(-1) * 70) {
						t_y = (temp.getY1() + 1) * 70 - 70 + getSpace(-1) * 70;
					}
					draw.setTxy(0, t_y + 10);
				}
				//System.out.println(t_x + ", " + t_y);
				draw.repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (draw.getTemp() != -1) {
				int cnt = 0;
				endx = e.getX();
				endy = e.getY();
				if (temp.getStride() == 0) {
					t_x = (temp.getX1() + 1) * 70 - 70 + 10;
					if (endx - startx > getSpace(1) * 70) {
						cnt = getSpace(1);
					} else if (endx - startx < getSpace(-1) * 70) {
						cnt = getSpace(-1);
					} else
						cnt = (endx - startx) / 70;
					draw.setTxy(t_x + cnt * 70, 0);
				} else {
					t_y = (temp.getY1() + 1) * 70 - 70 + 10;
					if (endy - starty > getSpace(1) * 70) {
						cnt = getSpace(1);
					} else if (endy - starty < getSpace(-1) * 70) {
						cnt = getSpace(-1);
					} else
						cnt = (endy - starty) / 70;
					draw.setTxy(0, t_y + cnt * 70);
				}
				if (temp.getStride() == 0) {
					if (endx - startx < 0) {
						if (getSpace(-1) > cnt)
							cnt = getSpace(-1);
						for (int i = 0; i > cnt; i--) {
							finish = eventSelect(board, temp.getSerial(), -1); // �ǽ� �̵� �� ������Ʈ
							board.updateBoard();
						}
					} else {
						if (getSpace(1) < cnt)
							cnt = getSpace(1);
						for (int i = 0; i < cnt; i++) {
							finish = eventSelect(board, temp.getSerial(), 1); // �ǽ� �̵� �� ������Ʈ
							board.updateBoard();
						}
					}
					Undo.setUndo(cnt, temp.getSerial());
				} else {
					if (endy - starty < 0) {
						if (getSpace(-1) > cnt)
							cnt = getSpace(-1);
						for (int i = 0; i > cnt; i--) {
							finish = eventSelect(board, temp.getSerial(), -1); // �ǽ� �̵� �� ������Ʈ
							board.updateBoard();
						}
					} else {
						if (getSpace(1) < cnt)
							cnt = getSpace(1);
						for (int i = 0; i < cnt; i++) {
							finish = eventSelect(board, temp.getSerial(), 1); // �ǽ� �̵� �� ������Ʈ
							board.updateBoard();
						}
					}
					Undo.setUndo(cnt, temp.getSerial());
				}
				//System.out.println(t_x + ", " + t_y + ", " + startx + ", " + starty + ", " + endx + ", " + endy + ", "+ cnt + ", " + getSpace(1) + ", " + getSpace(-1) + ", " + temp.getX1() + ", " + temp.getY1());
				draw.repaint();
				draw.setTemp(-1);
				draw.repaint();
				if (cnt != 0)
					_count++;
			} // if
			lblCount.setText("Count / Best : " + _count + " / " + best);
			if (finish == 1) {
				lblClear.setVisible(true);
				lblClear.setDelayTime(100);
				lblClear.start();
				JOptionPane bb = new JOptionPane();
				bb.showMessageDialog(null, "���� �ذ�!");
				setVisible(false);
				finish = 1;
				GUI.menuBar.remove(MenuSkin);
				GUI.menuBar.remove(btn[0]);
				GUI.Clear();
//				dispose();
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	// private Color currentColor = Color.RED;//

	Image bluecar = Toolkit.getDefaultToolkit().getImage("src/img/bluecar1.jpg"); // �̹��� ����
	Image redcar = Toolkit.getDefaultToolkit().getImage("src/img/redcar1.jpg"); // �̹��� ����
	Image greencar = Toolkit.getDefaultToolkit().getImage("src/img/greencar1.jpg"); // �̹��� ����

	// menuItem[] -> new Item[6] image 0, 1, 2, ..., 5
	// color [3] = {rgb(0, 0,0), ... }

//	public Board getBoard() {
//		return board;
//	}
//
//	public Piece getPiece() {
//		return temp;
//	}
//
//	public Piece[] getList() {
//		return pc_List;
//	}
//
//	public int getNum() {
//		return puzzlenum;
//	}
//
//	public Move getUndo() {
//		return Undo;
//	}

	public void setPnum(int i) {
		puzzlenum = i;
	}

	public int getPnum() {
		return puzzlenum;
	}

	public int getClear() {
		return finish;
	}
	
	public int getCount() {
		return _count;
	}

	public int getSpace(int dir) {
		int space = 0;
		if (dir < 0) {
			if (temp.getStride() == 0) {
				for (int i = temp.getX1() - 1; i >= 0; i--) {
					if (board.getBoard()[temp.getY1()][i] == 0)
						space--;
					else
						break;
				}
			} else {
				for (int i = temp.getY1() - 1; i >= 0; i--) {
					if (board.getBoard()[i][temp.getX1()] == 0)
						space--;
					else
						break;
				}
			}
		} else {
			if (temp.getStride() == 0) {
				for (int i = temp.getX2() + 1; i < 6; i++) {
					if (board.getBoard()[temp.getY1()][i] == 0)
						space++;
					else
						break;
				}
			} // �����϶�
			else {
				for (int i = temp.getY2() + 1; i < 6; i++) {
					if (board.getBoard()[i][temp.getX1()] == 0)
						space++;
					else
						break;
				}
			} // ������ ��
		}
		return space;
	}
}
