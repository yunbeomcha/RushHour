import java.awt.*;
import javax.swing.*;

public class Draw extends JPanel {

	private Board board;
	private int temp_serial = -1; // 현재 선택된 피스의 시리얼 번호
	private int skinflag = 0, t_x, t_y;

	private Color t_main, main, colors[]; // 색상 설정

	private Image skinimage; // 이미지 변수 설정

	public Draw(Board basic) {
		board = basic;
		setBounds(35, 190, board.getSize() * 70 + 1, board.getSize() * 70 + 1); // 보드 사이즈 에 비례한 패널 생성
		colors = new Color[8];
		colors[0] = new Color(214, 66, 80); // 메인 빨강
		colors[1] = new Color(158, 34, 46); // 메인 빨강 선택
		colors[2] = new Color(5, 128, 229);	// 메인 파랑
		colors[3] = new Color(3, 88, 159);	// 메인 파랑 선택
		colors[4] = new Color(97, 154, 72);	//	 메인 초록
		colors[5] = new Color(76, 120, 56);	// 메인 초록 선택
		colors[6] = new Color(3, 173, 141); // 방해 색상 생성
		colors[7] = new Color(2, 120, 98); // 방해 피스 선택
		main = colors[0];
		t_main = colors[1];
		setOpaque(false); // 배경 없앰
		setBackground(new Color(0, 0, 0, 0)); // 배경색 지정
	}

	public void paint(Graphics g) {
		super.paint(g);

		for (int i = 0; i < board.getSize(); i++) { // 보드 크기만큼 반복
			for (int j = 0; j < board.getSize(); j++) { // 보드 크기만큼 반복
				g.setColor(new Color(0, 0, 0)); // 점 색상 설정
				g.fillOval(70 * i + 35, 70 * j + 35, 5, 5); // 점 찍기
			}
		} // 점찍기

		for (Piece piece : board.getPieces()) { // 피스 반복
			int x1 = (piece.getX1() + 1) * 70 + 10 - 70; // 피스 좌표
			int y1 = (piece.getY1() + 1) * 70 + 10 - 70; // 피스 좌표
			int width = 0;
			int height = 0;
			int drawsignal = 0;
			if (piece.getSize() == 1) { // 1칸 -> immutable Piece
				g.setColor(Color.gray);
				width = 50;
				height = 50;
			} else if (piece.getSize() == 2) { // 두개짜리라면
				if (piece.getStride() == 0) // 수평이라면
				{
					if (piece.getPri() == true) { // 메인 피스라면
						int x[] = { board.getSize() * 70 - 7, board.getSize() * 70 - 7, board.getSize() * 70 + 1 }; // x좌표
						int y[] = { (piece.getY1() + 1) * 70 - 28, (piece.getY1() + 1) * 70 - 42,
								(piece.getY1() + 1) * 70 - 35 }; // y좌표 배열 설정
						g.setColor(new Color(245, 100, 85)); // 삼각형 색상 설정
						g.fillPolygon(x, y, 3); // 삼각형 그리기

						if (skinflag == 0) { // flag가 0이라면
							g.setColor(main); // 색상 설정
							if (piece.getSerial() == temp_serial) { // 현재 선택된 피스라면
								x1 = t_x;
								y1 = t_y;
								g.setColor(t_main); // 색상 설정
							}
						} else {
							drawsignal = 1;
							if (piece.getSerial() == temp_serial) {
								x1 = t_x;
								y1 = t_y;
								g.drawImage(skinimage, x1, y1, 120, 50, this); // 메인 피스 스킨입히기
								g.drawImage(new ImageIcon("src/img/star.png").getImage(), x1 + 50, y1 + 15, 20, 20,
										this); // 메인
							}
							else
								g.drawImage(skinimage, x1, y1, 120, 50, this); // 메인 피스 스킨입히기
						}
					} else {
						g.setColor(colors[6]); // 색상 설정
						if (piece.getSerial() == temp_serial) { // 현재 선택된 피스라면
							x1 = t_x;
							y1 = t_y;
							g.setColor(colors[7]); // 색상 설정
						}
					}
					width = 120;
					height = 50;

				} else // 수직이라면
				{
					g.setColor(colors[6]); // 색상 설정
					if (piece.getSerial() == temp_serial) { // 현재 선택된 피스라면
						x1 = t_x;
						y1 = t_y;
						g.setColor(colors[7]); // 색상 설정
					}
					width = 50;
					height = 120;
				}
			} else { // 세개짜리라면
				if (piece.getStride() == 0) // 수평이라면
				{
					width = 190;
					height = 50;
				} else // 수직이라면
				{
					width = 50;
					height = 190;
				}
				g.setColor(colors[6]); // 색상 설정
				if (piece.getSerial() == temp_serial) { // 현재 선택된 피스라면
					x1 = t_x;
					y1 = t_y;
					g.setColor(colors[7]); // 색상 설정
				}
			}
			if (drawsignal == 0)
				g.fillRoundRect(x1, y1, width, height, 10, 10);
		} // for 피스 반복
	}

	public void setSkin(int flag) {
		skinflag = flag; // 플래그 설정
	}

	public void changeSkin(Image pick) {
		skinimage = pick; // 이미지 설정
		setTemp(-1);
	}

	public void changeSkin(Color pick, int i) {
		main = pick; // 색상 설정
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
		super.repaint(); // 다시 그리기
		setOpaque(false); // 배경 없애기
	}

	public void setTemp(int i) {
		temp_serial = i; // 현재 선택된 피스의 시리얼 설정
	}
	
	public int getTemp() {
		return temp_serial; // 현재 선택된 피스의 시리얼 설정
	}
	
	public void setTxy(int x, int y) {
		if(x != 0)
			t_x = x;
		if(y != 0)
			t_y = y;
	}
}
