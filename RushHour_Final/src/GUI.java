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
		super("Rush hour"); // Ÿ��Ʋ ����
		layInit();
		setVisible(true); // ���̵��� ��
		setResizable(false); // maximize button disable

		setBounds(600, 100, 510, 690); // ������ġ, ������ġ, ���α���, ���α���
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // xǥ�÷� ����
		G = this;
		Play("src/sound/1.wav");
	} // ������

	public void layInit() {

		pn1 = new ImagePanel(); // �̹��� �г� ����
		pn1.setBounds(0, 0, 491, 643); // ��ġ �� ũ�� ����
		pn1.setLayout(null); // ��ġ������ ����
		pn1.start();

		Font fnt14 = new Font("Bahnschrift Light SemiCondensed", Font.PLAIN, 14); // ��Ʈ ����

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
//		setJMenuBar(menuBar);	//	�޴��� ����
		menuBar.setBackground(Color.white); // �޴��� ���� ����

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
				lblArr[lblIdx] = new JLabel("÷ �� ��"); // �� ����
				lblArr[lblIdx].setBounds(182, 140, 300, 30); // ��ġ �� ũ�� ����
				// lblArr[lblIdx].setHorizontalAlignment(SwingConstants.CENTER);
				// lblArr[lblIdx].setHorizontalTextPosition(SwingConstants.CENTER);
				fnt = 30;
			} else {
				if (i == 1)
					lblArr[lblIdx] = new JLabel("��");
				else if (i == 2)
					lblArr[lblIdx] = new JLabel("��");
				else
					lblArr[lblIdx] = new JLabel("߾");
				lblArr[lblIdx].setBounds(70, 135 + i * 69, 270, 30); // ��ġ �� ũ�� ����
				fnt = 25;
			}
			lblArr[lblIdx].setFont(new Font("���� ���", Font.PLAIN, fnt)); // ��Ʈ ����
			lblArr[lblIdx].setForeground(Color.white); // ���� ���� ����
			pn1.add(lblArr[lblIdx]); // �гο� �߰�
			lblIdx++;
		}

		btn = new JButton[13];
		JButton tBtn;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				String url = new String("src/img/�׸�" + (i * 4 + j + 1) + ".png");
				// System.out.println(url);
				tBtn = new JButton(new ImageIcon(url)); // ��ư ����
				tBtn.setBounds(145 + (j * 45), 195 + (i * 70), 45, 45); // ��ġ �� ũ�� ����
				tBtn.setBorderPainted(false); // �׵θ� �����
				tBtn.setContentAreaFilled(false); // ��ư ���� �����
				tBtn.setFocusPainted(false); // ���ý� ����� �׵θ� ����
				tBtn.addActionListener(new MainListener()); // �����ʿ��� ��ư �ν�

				btn[i * 4 + j] = tBtn;
				pn1.add(tBtn); // �гο� �߰�

				if (i * 4 + j + 1 == 13) {
					tBtn.setBounds(190, 422, 90, 90); // ��ġ �� ũ�� ����
					break;
				}
			}
		}

		add(pn1); // �����ӿ� �г� �߰�

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

		GUI gui = new GUI(); // GUI ����
		
		gui.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) { // �����츦 ������
				System.exit(0); // �ý��� ����
			}
		});

	} // ���� Ŭ����

	public class ImagePanel extends JPanel implements Runnable {
		private Image backgroundimg, img[]; // �̹��� ����
		private int _start, _finish;
		private Thread myThread;
		private int _delayTime;

		public ImagePanel() {
			img = new Image[3];
			img[0] = new ImageIcon("src/img/GUI.png").getImage();
			img[1] = new ImageIcon("src/img/GUI1.png").getImage();
			img[2] = new ImageIcon("src/img/GUI2.png").getImage();
			this.backgroundimg = img[0]; // �̹���
			setSize(new Dimension(img[0].getWidth(null), img[0].getHeight(null))); // ������ ����
			setPreferredSize(new Dimension(510, 720)); // ������ ����
			setLayout(null); // ��ġ ������ ��� �� ��

			_start = 0;
			_finish = 50;
			_delayTime = 1500;
			myThread = null;
		}

		public void paintComponent(Graphics g) {
			g.drawImage(backgroundimg, 0, 0, null); // ��濡 �̹��� ����
		}

		public void start() {
			if (myThread == null) {
				myThread = new Thread(this); // this�� LabelThread�� ����Ŵ
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

	private class MainListener implements ActionListener { // ���� �߰� ���

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