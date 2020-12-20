public class Board {
	// 보드 현황
	// 피스가 주어졌을 때 이동 가능한 위치
	// 피스 객체 선택 및 이동 업데이트
	private int[][] m_Board;
	private int m_Size;
	private Piece[] m_Pieces;
	private String _serial;

	public Board(int size, Piece[] Pieces, String desc) {
		m_Size = size;
		m_Board = new int[size][size];
		m_Pieces = Pieces; // reference?
		_serial = desc;
		updateBoard();

	} // 생성자

	public int updateBoard() {
		int i, j;
		int signal = 0; // 신호 변수
		m_Board = new int[m_Size][m_Size]; // 2차원 배열 보드 생성

		for (Piece piece : m_Pieces) { // 피스 반복
			if (piece.getSize() == 1) {
				m_Board[piece.getY1()][piece.getX1()] = 1; // 보드의 해당 좌표를 1로 바꿈
			} else { // 피스가 두칸짜리라면
				if (piece.getStride() == 0) { // 수평이라면
					for (i = piece.getX1(); i <= piece.getX2(); i++) { // 피스의 x좌표로 반복
						m_Board[piece.getY1()][i] = 1; // 보드의 해당 좌표를 1로 바꿈
					}
				} else { // 수직이라면
					for (j = piece.getY1(); j <= piece.getY2(); j++) { // 피스의 y좌표로 반복
						m_Board[j][piece.getX1()] = 1; // 보드의 해당 좌표를 1로 바꿈
					}
				}
				if (piece.getPri() == true && piece.getX2() == m_Size - 1) { // 메인 피스가 보드의 끝에 도달하면
					signal = 1; // 게임이 끝났음을 알림
				}
			} 
		}

		return signal; // 신호 리턴
	} // 각 피스 추가 및 업데이트 시 호출

	public Piece findPiece(int s) {
		for (Piece piece : m_Pieces) { // 피스 배열 반복
			if (piece.getSerial() == s && piece.getSize() != 1) { // 입력 값과 시리얼이 같다면
				return piece; // 피스 반환
			}
		}
		return null;
	} // 좌표 주어졌을 때 piece 찾기 /

	public boolean couldMoveMinus(Piece piece) {
		int stride = piece.getStride(); // 피스 방향
		int x = piece.getX1(); // 피스의 첫 좌표
		int y = piece.getY1(); // 피스의 첫 좌표
		if (stride == 0) { // 피스 수평
			x -= 1; // 첫좌표 -1
		} else { // 수직
			y -= 1; // 첫좌표 -1
		}
		if (0 <= x && 0 <= y && m_Board[y][x] == 0) { // 보드의 범위안에서 첫좌표가 0이라면
			return true; // 참
		} else {
			return false; // 거짓
		}
	} // 피스가 뒤로 이동 가능한지 여부 리턴

	public boolean couldMovePlus(Piece piece) {
		int stride = piece.getStride(); // 피스 방향
		int x = piece.getX2(); // 피스의 끝좌표
		int y = piece.getY2(); // 피스의 끝좌표

		if (stride == 0) { // 수평
			x += 1; // 끝좌표 + 1
		} else {
			y += 1; // 끝좌표 + 1
		}

		if (x < m_Size && y < m_Size && this.m_Board[y][x] == 0) { // 보드의 사이즈 범위안에서 끝좌표가 0이라면
			return true; // 참
		} else {
			return false; // 거짓
		}
	} // 피스가 앞으로 이동 가능한지 여부 리턴

	public int[][] getBoard() {
		return m_Board;
	};

	public int getSize() {
		return m_Size;
	}

	public Piece[] getPieces() {
		return m_Pieces;
	}

	public String get_serial() {
		return _serial;
	}
	
	public int getWallNum(int serial) {
		int count = 0;
		for(int i=0;i<=serial;i++) {
			if(findPiece(i)==null) {
				count++;
			}			
		}
		if(findPiece(serial+count)==null) {
			count = getWallNum(serial+count);
		}
		
		return count;
	}

	public void setBoard(int[][] i) {
		m_Board = i;
	};

	public void setSize(int i) {
		m_Size = i;
	}

	public void setPieces(Piece[] i) {
		m_Pieces = i;
	}

	public void set_serial(String i) {
		_serial = i;
	}
}
