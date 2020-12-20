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

//		setBounds(200, 200, 510, 720); // 가로위치, 세로위치, 가로길이, 세로길이
		setBounds(0, 0, 520, 750); // 가로위치, 세로위치, 가로길이, 세로길이
//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // x눌러서 종료
		setLayout(null); // 배치 관리자 사용 안 함
//		setResizable(false); // maximize button disable
		Play("src/sound/2.wav");
		Font fnt14 = new Font("Gadugi", Font.PLAIN, 14); // 폰트 생성

		Undo = new Move(); // 무브 객체 설정
		pc_List = Piece.serialToPieces(desc); // 피스 리스트 생성
		board = new Board(6, pc_List, desc); // 보드 생성
//		setTitle("Puzzle " + puzIdx); // 타이틀 설정
		puzzlenum = puzIdx - 1;
		
		best = G.getRecord(puzzlenum);
		min_count = ExecGo.puzzleMinCount(board);

		if (puzIdx == 1 || puzIdx == 2 || puzIdx == 3 || puzIdx == 4) {
			panel = new ImagePanel(new ImageIcon("src/img/배경14.png").getImage()); // 배경 패널
			add(panel); // 프레임에 추가
		}
		if (puzIdx == 5 || puzIdx == 6 || puzIdx == 7 || puzIdx == 8) {
			panel = new ImagePanel(new ImageIcon("src/img/배경15.png").getImage()); // 배경 패널
			add(panel); // 프레임에 추가
		}
		if (puzIdx == 9 || puzIdx == 10 || puzIdx == 11 || puzIdx == 12) {
			panel = new ImagePanel(new ImageIcon("src/img/배경13.png").getImage()); // 배경 패널
			add(panel); // 프레임에 추가
		}

		Listen = new DrawListener(); // 리스너 생성
		btn = new JButton[4]; // 버튼 생성
		lbl = new JLabel[4]; // 라벨 생성

		GUI = G;
		menuBar = G.menuBar;

//		setJMenuBar(menuBar);	//	메뉴바 설정
		menuBar.setBackground(Color.white); // 메뉴바 배경색 설정

		MenuSkin = new JMenu("Skin"); // 메뉴 생성
		MenuSkin.setHorizontalTextPosition(SwingConstants.CENTER); // 위치 설정
		MenuSkin.setHorizontalAlignment(SwingConstants.CENTER); // 위치 설정
		MenuSkin.setBounds(10, 10, 70, 50); // 크기 및 위치 설정
		MenuSkin.setIconTextGap(51);
		MenuSkin.setFont(fnt14); // 폰트 설정
		menuBar.add(MenuSkin); // 메뉴바에 추가

		String txtMenuItem[] = new String[] { "Redicon", "Blueicon", "Greenicon", "Redcar", "Bluecar", "Greencar" }; // 메뉴
																														// 아이템
																														// 문자열
																														// 배열로
																														// 생성

		JMenuItem tItem; // 메뉴 아이템 변수 선언
		for (int i = 0; i < txtMenuItem.length; i++) {
			String tmpUrl = "src/img/" + txtMenuItem[i] + ".png"; // 메뉴 아이템 이미지 스트링으로 받기
			tItem = new JMenuItem(new ImageIcon(tmpUrl)); // 메뉴 아이템 생성
			tItem.setText(txtMenuItem[i]); // 텍스트 설정
			tItem.setForeground(Color.white); // 폰트 색상 설정
			tItem.addActionListener(Listen); // 리스너에 추가
			tItem.setBackground(Color.white); // 배경 색상 설정
			tItem.setSize(27, 5); // 사이즈 설정
			tItem.setFont(fnt14); // 폰트 설정
			MenuSkin.add(tItem); // 메뉴에 아이템 추가
		}

		btn[0] = new JButton("Hint"); // 버튼 생성
		btn[0].setBounds(100, 10, 70, 50); // 위치 및 크기 생성
		btn[0].setHorizontalTextPosition(SwingConstants.CENTER); // 위치 설정
		btn[0].setHorizontalAlignment(SwingConstants.CENTER); // 위치 설정
		btn[0].setIconTextGap(31);
		btn[0].setFont(fnt14); // 폰트 설정

		btn[1] = new JButton(new ImageIcon("src/img/reset5.png")); // 리셋 버튼 이미지 생성
		btn[1].setBounds(128, 40, 50, 50); // 위치 및 크기 설정

		btn[2] = new JButton(new ImageIcon("src/img/undo5.png")); // 뒤로 버튼 이미지 생성
		btn[2].setBounds(291, 40, 50, 50); // 위치 및 크기 설정

		btn[3] = new JButton(new ImageIcon("src/img/뒤로.jpg")); // 뒤로 버튼 이미지 생성
		btn[3].setBounds(390, 40, 50, 50); // 위치 및 크기 설정

		for (int i = 0; i < 4; i++) {
			btn[i].setBorderPainted(false); // 테두리 지우기
			btn[i].setContentAreaFilled(false); // 버튼 내부 지우기
			btn[i].setFocusPainted(false); // 선택시 테두리 지우기
			btn[i].addActionListener(Listen); // 리스너에서 인식
			panel.add(btn[i]); // 패널에 추가
		}
		menuBar.add(btn[0]);
		// pn1.setBackground(Color.white);
		// add(pn1);

		lbl[0] = new JLabel("Reset"); // 라벨 생성
		lbl[0].setFont(fnt14); // 폰트 설정
		lbl[0].setBounds(134, 70, 70, 70); // 위치 및 크기 설정
		lbl[0].setForeground(Color.darkGray);

		lbl[1] = new JLabel("Undo"); // 라벨 생성
		lbl[1].setFont(fnt14); // 폰트 생성
		lbl[1].setBounds(300, 70, 70, 70); // 위치 및 크기 설정
		lbl[1].setForeground(Color.darkGray);

		_count = 0;

		lblCount = new JLabel("Count / Best : " + _count + " / " + best); // Count 라벨 생성
		lblCount.setBounds(320, 160, 180, 40); // 라벨 위치, 사이즈
		lblCount.setFont(fnt14); // font설정
		lblCount.setForeground(Color.darkGray);
		panel.add(lblCount);

		lbl[2] = new JLabel("Min: " + min_count); // Count 라벨 생성
		lbl[2].setBounds(80, 160, 180, 40); // 라벨 위치, 사이즈
		lbl[2].setFont(fnt14); // font설정
		lbl[2].setForeground(Color.darkGray);

		lbl[3] = new JLabel("Menu"); // Count 라벨 생성
		lbl[3].setBounds(400, 70, 70, 70); // 라벨 위치, 사이즈
		lbl[3].setFont(fnt14); // font설정
		lbl[3].setForeground(Color.darkGray); // 폰트 색상

		lblClear = new ClearLabelThread1("CLEAR"); // 클리어 라벨
		lblClear.setBounds(120, 250, 250, 60); // 위치 및 크기 설정
		lblClear.setFont(new Font("Verdata", Font.BOLD, 50)); // 폰트 설정
		lblClear.setHorizontalAlignment(SwingConstants.CENTER); // 위치 설정
		lblClear.setVisible(false); // 라벨 안보이게 하기
		panel.add(lblClear);

		for (int i = 0; i < 4; i++)
			panel.add(lbl[i]);
		draw = new Draw(board); // Draw 패널 생성
		draw.addMouseListener(Listen); // 마우스 리스너에서 인식
		draw.addMouseMotionListener(Listen); // 마우스 리스너에서 인식
		panel.add(draw); // 패널에 draw 패널 추가
		draw.addKeyListener(Listen); // 키 리스너 인식
		setVisible(true); // 보이게 하기
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
		private Image backgroundimg; // 이미지 선언

		public ImagePanel(Image img) {
			this.backgroundimg = img; // 이미지 대입
			setSize(new Dimension(img.getWidth(null), img.getHeight(null))); // 사이즈 설정
			setPreferredSize(new Dimension(491, 643)); // 영역 설정
			setLayout(null); // 배치 관리자 사용 안 함
		}

		public void paintComponent(Graphics g) {
			g.drawImage(backgroundimg, 0, 0, null); // 배경 이미지 그리기
			draw.repaint(); // draw 다시 그리기
		}
	}

	public static int eventSelect(Board basic, int Serial, int m) {
		int finish = 0;

		try {
			Piece tmp = basic.findPiece(Serial); // 피스 선언
			tmp.movePiece(m); // 피스 이동
			finish = basic.updateBoard(); // 보드 업데이트
//			boolean minus = basic.couldMoveMinus(tmp); // 뒤로 이동 가능 여부
//			boolean plus = basic.couldMovePlus(tmp); // 앞으로 이동 가능 여부
//			if (m == -1 && minus || m == 1 && plus) { // 앞이나 뒤로 이동 가능하다면
//				tmp.movePiece(m); // 피스 이동
//				finish = basic.updateBoard(); // 보드 업데이트
//			} else {
//				throw new Exception();
//			}

		} catch (Exception e) {
//			_count--; // 카운트 감소
//			lblCount.setText("Count / Best : " + _count + " / " + best); // 카운트 텍스트 설정
//			System.out.println("문제발생");
		}
		return finish; // 끝냄 신호 반환
	} // 이벤트 선택

	public class DrawListener implements MouseListener, MouseMotionListener, KeyListener, ActionListener {

		@Override
		public void mouseClicked(MouseEvent e) { // 마우스가 클릭되면
			int signal = 0; // 시그널 설정
			for (Piece piece : pc_List) { // 피스 반복
				if (piece.getSize() == 1) {
					break;
				} else { // 피스 사이즈가 2라면
					if ((e.getX() / 70 >= piece.getX1() && (e.getX() / 70 <= piece.getX2())
							&& (e.getY() / 70 >= piece.getY1() && e.getY() / 70 <= piece.getY2()))) { // 클릭한 영역이 피스에
																										// 포함되면
						temp = piece; // 피스 설정
						signal = 1; // 시그널 변경
						break;
					}
				}
			}
			if (signal == 0) { // 시그널이 0이라면
				draw.setTemp(-1); // 선택된 것 없음
				temp = null;
			} else {// 1이라면
				draw.setTemp(temp.getSerial()); // 선택된 피스 설정
			}
			draw.repaint(); // 다시 그리기
			draw.requestFocus();
			setFocusable(true);
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void actionPerformed(ActionEvent Ev) {
			Object obj = Ev.getSource(); // 버튼 객체 인식
			if (btn[2] == obj) { // 뒤로 버튼 이라면
				int undoObj;
				int undoMove;
				try {
					undoObj = Undo.undoObj(); // 되돌릴 피스의 시리얼 대입
					undoMove = Undo.undoMove(); // 되돌릴 피스의 움직임 대입
					Piece undoPiece = Undo.findPiece(board, undoObj); // 피스 대입
					if (undoMove > 0) {
						for (int i = 0; i < undoMove; i++)
							undoPiece.movePiece(-1);
					} else {
						for (int i = 0; i > undoMove; i--)
							undoPiece.movePiece(1);
					}
					board.updateBoard(); // 보드 업데이트
					Undo.setIndex(Undo.getIndex() - 1); // 인덱스 감소
					_count--; // 카운트 감소
					lblCount.setText("Count / Best : " + _count + " / " + best); // 카운트 텍스트 설정
				} catch (ArrayIndexOutOfBoundsException e) { // 처음상태라면
//					lblCount.setText("Count: " + _count); // 카운트 텍스트 설정
//					JOptionPane bb = new JOptionPane();
//					bb.showMessageDialog(null, "처음 상태 입니다."); // 다이얼로그 생성
				}
				draw.setTemp(-1);
				draw.repaint();
			} else if (obj == btn[1]) { // 리셋 버튼이라면
				try {
					Undo.reset(board, board.get_serial()); // 리셋 호출
					pc_List = board.getPieces(); // 피스 리스트 다시 설정
					temp = null; // 템프 비우기
					_count = 0; // 카운트 초기화
					lblCount.setText("Count / Best : " + _count + " / " + best); // 카운트 텍스트 설정
				} catch (ArrayIndexOutOfBoundsException e) { // 처음상태라면
//					lblCount.setText("Count: " + _count); // 카운트 텍스트 설정
//					JOptionPane bb = new JOptionPane();
//					bb.showMessageDialog(null, "처음 상태 입니다."); // 다이얼로그 생성
				}
				draw.setTemp(-1);
				draw.repaint(); // 다시 그리기
			} else if (obj == btn[0]) {
				try {
					if (Undo.getIndex() == 99999) {
//						JOptionPane bb = new JOptionPane();
//						bb.showMessageDialog(null, "이동불가.");
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
						bb.showMessageDialog(null, "퍼즐 해결!");
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
				int exitOption = JOptionPane.showConfirmDialog(null, "메뉴로 돌아가시겠습니까?", "", JOptionPane.YES_NO_OPTION);
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
				JMenuItem mi = (JMenuItem) (Ev.getSource()); // 오브젝트를 아이템으로 받아옴
				switch (mi.getText()) { // 오브젝트의 텍스트를 조건
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
			int signal = 0; // 시그널 설정
			for (Piece piece : pc_List) { // 피스 반복
				if (piece.getSize() == 1) {
					break;
				} else { // 피스 사이즈가 2라면
					if ((e.getX() / 70 >= piece.getX1() && (e.getX() / 70 <= piece.getX2())
							&& (e.getY() / 70 >= piece.getY1() && e.getY() / 70 <= piece.getY2()))) { // 클릭한 영역이 피스에
																										// 포함되면
						temp = piece; // 피스 설정
						signal = 1; // 시그널 변경
						break;
					}
				}
			}
			if (signal == 0) { // 시그널이 0이라면
				draw.setTemp(-1); // 선택된 것 없음
				temp = null;
			} else {// 1이라면
				draw.setTemp(temp.getSerial()); // 선택된 피스 설정
				startx = e.getX();
				starty = e.getY();
				//System.out.println(startx + ", " + starty);
				draw.setTxy((temp.getX1() + 1) * 70 + 10 - 70, (temp.getY1() + 1) * 70 + 10 - 70);
			}
			draw.repaint(); // 다시 그리기
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
							finish = eventSelect(board, temp.getSerial(), -1); // 피스 이동 및 업데이트
							board.updateBoard();
						}
					} else {
						if (getSpace(1) < cnt)
							cnt = getSpace(1);
						for (int i = 0; i < cnt; i++) {
							finish = eventSelect(board, temp.getSerial(), 1); // 피스 이동 및 업데이트
							board.updateBoard();
						}
					}
					Undo.setUndo(cnt, temp.getSerial());
				} else {
					if (endy - starty < 0) {
						if (getSpace(-1) > cnt)
							cnt = getSpace(-1);
						for (int i = 0; i > cnt; i--) {
							finish = eventSelect(board, temp.getSerial(), -1); // 피스 이동 및 업데이트
							board.updateBoard();
						}
					} else {
						if (getSpace(1) < cnt)
							cnt = getSpace(1);
						for (int i = 0; i < cnt; i++) {
							finish = eventSelect(board, temp.getSerial(), 1); // 피스 이동 및 업데이트
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
				bb.showMessageDialog(null, "퍼즐 해결!");
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

	Image bluecar = Toolkit.getDefaultToolkit().getImage("src/img/bluecar1.jpg"); // 이미지 설정
	Image redcar = Toolkit.getDefaultToolkit().getImage("src/img/redcar1.jpg"); // 이미지 설정
	Image greencar = Toolkit.getDefaultToolkit().getImage("src/img/greencar1.jpg"); // 이미지 설정

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
			} // 수평일때
			else {
				for (int i = temp.getY2() + 1; i < 6; i++) {
					if (board.getBoard()[i][temp.getX1()] == 0)
						space++;
					else
						break;
				}
			} // 수직일 때
		}
		return space;
	}
}
