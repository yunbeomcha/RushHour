import java.awt.*;
import javax.swing.*;

public class Draw extends JPanel {

	private Board board;
	private int temp_serial = -1; // ���� ���õ� �ǽ��� �ø��� ��ȣ
	private int skinflag = 0, t_x, t_y;

	private Color t_main, main, colors[]; // ���� ����

	private Image skinimage; // �̹��� ���� ����

	public Draw(Board basic) {
		board = basic;
		setBounds(35, 190, board.getSize() * 70 + 1, board.getSize() * 70 + 1); // ���� ������ �� ����� �г� ����
		colors = new Color[8];
		colors[0] = new Color(214, 66, 80); // ���� ����
		colors[1] = new Color(158, 34, 46); // ���� ���� ����
		colors[2] = new Color(5, 128, 229);	// ���� �Ķ�
		colors[3] = new Color(3, 88, 159);	// ���� �Ķ� ����
		colors[4] = new Color(97, 154, 72);	//	 ���� �ʷ�
		colors[5] = new Color(76, 120, 56);	// ���� �ʷ� ����
		colors[6] = new Color(3, 173, 141); // ���� ���� ����
		colors[7] = new Color(2, 120, 98); // ���� �ǽ� ����
		main = colors[0];
		t_main = colors[1];
		setOpaque(false); // ��� ����
		setBackground(new Color(0, 0, 0, 0)); // ���� ����
	}

	public void paint(Graphics g) {
		super.paint(g);

		for (int i = 0; i < board.getSize(); i++) { // ���� ũ�⸸ŭ �ݺ�
			for (int j = 0; j < board.getSize(); j++) { // ���� ũ�⸸ŭ �ݺ�
				g.setColor(new Color(0, 0, 0)); // �� ���� ����
				g.fillOval(70 * i + 35, 70 * j + 35, 5, 5); // �� ���
			}
		} // �����

		for (Piece piece : board.getPieces()) { // �ǽ� �ݺ�
			int x1 = (piece.getX1() + 1) * 70 + 10 - 70; // �ǽ� ��ǥ
			int y1 = (piece.getY1() + 1) * 70 + 10 - 70; // �ǽ� ��ǥ
			int width = 0;
			int height = 0;
			int drawsignal = 0;
			if (piece.getSize() == 1) { // 1ĭ -> immutable Piece
				g.setColor(Color.gray);
				width = 50;
				height = 50;
			} else if (piece.getSize() == 2) { // �ΰ�¥�����
				if (piece.getStride() == 0) // �����̶��
				{
					if (piece.getPri() == true) { // ���� �ǽ����
						int x[] = { board.getSize() * 70 - 7, board.getSize() * 70 - 7, board.getSize() * 70 + 1 }; // x��ǥ
						int y[] = { (piece.getY1() + 1) * 70 - 28, (piece.getY1() + 1) * 70 - 42,
								(piece.getY1() + 1) * 70 - 35 }; // y��ǥ �迭 ����
						g.setColor(new Color(245, 100, 85)); // �ﰢ�� ���� ����
						g.fillPolygon(x, y, 3); // �ﰢ�� �׸���

						if (skinflag == 0) { // flag�� 0�̶��
							g.setColor(main); // ���� ����
							if (piece.getSerial() == temp_serial) { // ���� ���õ� �ǽ����
								x1 = t_x;
								y1 = t_y;
								g.setColor(t_main); // ���� ����
							}
						} else {
							drawsignal = 1;
							if (piece.getSerial() == temp_serial) {
								x1 = t_x;
								y1 = t_y;
								g.drawImage(skinimage, x1, y1, 120, 50, this); // ���� �ǽ� ��Ų������
								g.drawImage(new ImageIcon("src/img/star.png").getImage(), x1 + 50, y1 + 15, 20, 20,
										this); // ����
							}
							else
								g.drawImage(skinimage, x1, y1, 120, 50, this); // ���� �ǽ� ��Ų������
						}
					} else {
						g.setColor(colors[6]); // ���� ����
						if (piece.getSerial() == temp_serial) { // ���� ���õ� �ǽ����
							x1 = t_x;
							y1 = t_y;
							g.setColor(colors[7]); // ���� ����
						}
					}
					width = 120;
					height = 50;

				} else // �����̶��
				{
					g.setColor(colors[6]); // ���� ����
					if (piece.getSerial() == temp_serial) { // ���� ���õ� �ǽ����
						x1 = t_x;
						y1 = t_y;
						g.setColor(colors[7]); // ���� ����
					}
					width = 50;
					height = 120;
				}
			} else { // ����¥�����
				if (piece.getStride() == 0) // �����̶��
				{
					width = 190;
					height = 50;
				} else // �����̶��
				{
					width = 50;
					height = 190;
				}
				g.setColor(colors[6]); // ���� ����
				if (piece.getSerial() == temp_serial) { // ���� ���õ� �ǽ����
					x1 = t_x;
					y1 = t_y;
					g.setColor(colors[7]); // ���� ����
				}
			}
			if (drawsignal == 0)
				g.fillRoundRect(x1, y1, width, height, 10, 10);
		} // for �ǽ� �ݺ�
	}

	public void setSkin(int flag) {
		skinflag = flag; // �÷��� ����
	}

	public void changeSkin(Image pick) {
		skinimage = pick; // �̹��� ����
		setTemp(-1);
	}

	public void changeSkin(Color pick, int i) {
		main = pick; // ���� ����
		if(i == 0)
		{
			t_main = colors[1];
		}
		else if(i == 1) {
			t_main = colors[3];
		}
		else {
			t_main = colors[5];
		}
		setTemp(-1);
	}
	
	public void repaint() {
		super.repaint(); // �ٽ� �׸���
		setOpaque(false); // ��� ���ֱ�
	}

	public void setTemp(int i) {
		temp_serial = i; // ���� ���õ� �ǽ��� �ø��� ����
	}
	
	public int getTemp() {
		return temp_serial; // ���� ���õ� �ǽ��� �ø��� ����
	}
	
	public void setTxy(int x, int y) {
		if(x != 0)
			t_x = x;
		if(y != 0)
			t_y = y;
	}
}
