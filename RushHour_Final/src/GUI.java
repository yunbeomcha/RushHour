import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.sound.sampled.*;
import javax.sound.sampled.AudioSystem.*;


public class GUI extends JFrame {
	private ImagePanel pn1;
	private JButton btn[], clear[];
	private Puzzle gm;
	private JLabel lbl;
	private JLabel[] lblArr;
	private int lblIdx, intArr[];
	public JMenuBar menuBar;
	private GUI G;
	private String name;
	private Clip clip;
	private int record[];
	
	static String puzzleSerials[] = { "BBJooxHoJDDMHAAooMHoKEEMIoKLFFIGGLoo", "KBBxDDKooNEELAANoOLxGGoOooMoHHIIMJJJ",
			"HoKBBBHoKLoMIAALoMICCDDMoJEEooxJGGoo", "BBCCCMDDEEoMAAoooNJFFLoNJoKLGGJoKHHx",
			"oBBoLMxHIoLMGHIAANGDDKoNGoJKEEFFJooo", "HBBLoxHoJLxoAAJoMNoIEEMNoIKFFNGGKooo",
			"BBCCoxJEEFFNJAAMoNGGLMooKoLHHoKIIIoo", "oJoxCCoJDDMooAALMoxoKLFFIoKGGNIoHHoN",
			"BBBKLMHCCKLMHoAALMDDJooooIJEEooIFFGG", "GBBoLoGHIoLMGHIAAMCCCKoMooJKDDEEJFFo",
			"BBoKMxDDDKMoIAALooIoJLEEooJFFNoGGoxN", "IBBxooIooLDDJAALooJoKEEMFFKooMGGHHHM" };

	public GUI() {
		super("Rush hour"); // 타이틀 설정
		layInit();
		setVisible(true); // 보이도록 함
		setResizable(false); // maximize button disable

		setBounds(600, 100, 510, 690); // 가로위치, 세로위치, 가로길이, 세로길이
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // x표시로 종료
		G = this;
		Play("src/sound/1.wav");
	} // 생성자

	public void layInit() {

		pn1 = new ImagePanel(); // 이미지 패널 생성
		pn1.setBounds(0, 0, 491, 643); // 위치 및 크기 설정
		pn1.setLayout(null); // 배치관리자 없음
		pn1.start();

		Font fnt14 = new Font("Bahnschrift Light SemiCondensed", Font.PLAIN, 14); // 폰트 생성

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
//		setJMenuBar(menuBar);	//	메뉴바 설정
		menuBar.setBackground(Color.white); // 메뉴바 배경색 설정

		menuBar.setVisible(false);

		intArr = new int[12];
		clear = new JButton[12];

		for (int i = 0; i < 12; i++)
			intArr[i] = 0;

		lblArr = new JLabel[20];
		lblIdx = 0;

		for (int i = 0; i < 4; i++) {
			int fnt = 0;
			if (i == 0) {
				lblArr[lblIdx] = new JLabel("첨 두 시"); // 라벨 설정
				lblArr[lblIdx].setBounds(182, 140, 300, 30); // 위치 및 크기 설정
				// lblArr[lblIdx].setHorizontalAlignment(SwingConstants.CENTER);
				// lblArr[lblIdx].setHorizontalTextPosition(SwingConstants.CENTER);
				fnt = 30;
			} else {
				if (i == 1)
					lblArr[lblIdx] = new JLabel("下");
				else if (i == 2)
					lblArr[lblIdx] = new JLabel("中");
				else
					lblArr[lblIdx] = new JLabel("上");
				lblArr[lblIdx].setBounds(70, 135 + i * 69, 270, 30); // 위치 및 크기 설정
				fnt = 25;
			}
			lblArr[lblIdx].setFont(new Font("한컴 고딕", Font.PLAIN, fnt)); // 폰트 설정
			lblArr[lblIdx].setForeground(Color.white); // 글자 색상 설정
			pn1.add(lblArr[lblIdx]); // 패널에 추가
			lblIdx++;
		}

		btn = new JButton[13];
		JButton tBtn;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				String url = new String("src/img/그림" + (i * 4 + j + 1) + ".png");
				// System.out.println(url);
				tBtn = new JButton(new ImageIcon(url)); // 버튼 생성
				tBtn.setBounds(145 + (j * 45), 195 + (i * 70), 45, 45); // 위치 및 크기 설정
				tBtn.setBorderPainted(false); // 테두리 지우기
				tBtn.setContentAreaFilled(false); // 버튼 내부 지우기
				tBtn.setFocusPainted(false); // 선택시 생기는 테두리 지움
				tBtn.addActionListener(new MainListener()); // 리스너에서 버튼 인식

				btn[i * 4 + j] = tBtn;
				pn1.add(tBtn); // 패널에 추가

				if (i * 4 + j + 1 == 13) {
					tBtn.setBounds(190, 422, 90, 90); // 위치 및 크기 설정
					break;
				}
			}
		}

		add(pn1); // 프레임에 패널 추가

		record = new int[12];
		
		try {
			FileOutputStream output = new FileOutputStream("src/txt/record.txt");
			for(int i=0; i<12; i++) {
				String data = i + " " + 0 + "\r\n";
				output.write(data.getBytes());
				record[i] = 0;
        }
        output.close();
		} catch( Exception e) {}
	}

	public static void main(String[] args) {

		GUI gui = new GUI(); // GUI 생성
		
		gui.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) { // 윈도우를 닫으면
				System.exit(0); // 시스템 종료
			}
		});

	} // 메인 클래스

	public class ImagePanel extends JPanel implements Runnable {
		private Image backgroundimg, img[]; // 이미지 생성
		private int _start, _finish;
		private Thread myThread;
		private int _delayTime;

		public ImagePanel() {
			img = new Image[3];
			img[0] = new ImageIcon("src/img/GUI.png").getImage();
			img[1] = new ImageIcon("src/img/GUI1.png").getImage();
			img[2] = new ImageIcon("src/img/GUI2.png").getImage();
			this.backgroundimg = img[0]; // 이미지
			setSize(new Dimension(img[0].getWidth(null), img[0].getHeight(null))); // 사이즈 설정
			setPreferredSize(new Dimension(510, 720)); // 사이즈 설정
			setLayout(null); // 배치 관리자 사용 안 함

			_start = 0;
			_finish = 50;
			_delayTime = 1500;
			myThread = null;
		}

		public void paintComponent(Graphics g) {
			g.drawImage(backgroundimg, 0, 0, null); // 배경에 이미지 생성
		}

		public void start() {
			if (myThread == null) {
				myThread = new Thread(this); // this는 LabelThread를 가리킴
			}
			myThread.start();
		}

		@Override
		public void run() {

			for (int i = _start; i < _finish; i++) {
				this.repaint(0);
				try {
					myThread.sleep(_delayTime);
				} catch (Exception e) {
				}
				repaint(1);
				try {
					myThread.sleep(_delayTime);
				} catch (Exception e) {
				}
				repaint(2);
				try {
					myThread.sleep(_delayTime);
				} catch (Exception e) {
				}
			} // for
		}

		public void repaint(int i) {
			backgroundimg = img[i];
			repaint();
		}
	}

	private class MainListener implements ActionListener { // 퍼즐 추가 요망

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton obj = (JButton) e.getSource();
			for (int i = 0; i < 13; i++) {
				if (obj == btn[i]) {
					if (i == 12) {
						int ran = (int) (Math.random() * 12); // ->12
						gm = new Puzzle(puzzleSerials[ran], ran + 1, G);
						add(gm);
						pn1.setVisible(false);
						menuBar.setVisible(true);
					} else {
						gm = new Puzzle(puzzleSerials[i], i + 1, G);
						add(gm);
						pn1.setVisible(false);
						menuBar.setVisible(true);
					}
					Stop("src/sound/1.wav");
					break;
				}
			}
		}

	}

	public void Clear() {
		intArr[gm.getPnum()] = gm.getClear();
		for (int i = 0; i < 12; i++) {
			if (intArr[i] == 1) {
				btn[i].setIcon(new ImageIcon("src/img/m_missionclear.png"));
			}
		}
		if(gm.getClear() == 1) {
			modifier();
		}
		this.remove(gm);
		System.gc();
		pn1.setVisible(true);
	}

	public void Play(String fileName)
    {
        try
        {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(fileName));
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
            clip.loop(-1);
        }
        catch (Exception ex)
        {
        	 ex.printStackTrace();
        }
    }
	public void Stop(String fileName) {
		clip.stop();
		clip.close();
	}
	
	public void modifier() {
		if(record[gm.getPnum()] == 0 || gm.getCount() < record[gm.getPnum()])
			record[gm.getPnum()] = gm.getCount();
		try {
			FileOutputStream output = new FileOutputStream("src/txt/record.txt");
			for(int i=0; i<12; i++) {
				String data = i + " " + record[i] + "\r\n";
				output.write(data.getBytes());
        }
        output.close();
		} catch( Exception e) {}
	}
	
	public int getRecord(int i) {
		return record[i];
	}
}