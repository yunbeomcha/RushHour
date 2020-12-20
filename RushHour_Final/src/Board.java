public class Board {
	// ���� ��Ȳ
	// �ǽ��� �־����� �� �̵� ������ ��ġ
	// �ǽ� ��ü ���� �� �̵� ������Ʈ
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

	} // ������

	public int updateBoard() {
		int i, j;
		int signal = 0; // ��ȣ ����
		m_Board = new int[m_Size][m_Size]; // 2���� �迭 ���� ����

		for (Piece piece : m_Pieces) { // �ǽ� �ݺ�
			if (piece.getSize() == 1) {
				m_Board[piece.getY1()][piece.getX1()] = 1; // ������ �ش� ��ǥ�� 1�� �ٲ�
			} else { // �ǽ��� ��ĭ¥�����
				if (piece.getStride() == 0) { // �����̶��
					for (i = piece.getX1(); i <= piece.getX2(); i++) { // �ǽ��� x��ǥ�� �ݺ�
						m_Board[piece.getY1()][i] = 1; // ������ �ش� ��ǥ�� 1�� �ٲ�
					}
				} else { // �����̶��
					for (j = piece.getY1(); j <= piece.getY2(); j++) { // �ǽ��� y��ǥ�� �ݺ�
						m_Board[j][piece.getX1()] = 1; // ������ �ش� ��ǥ�� 1�� �ٲ�
					}
				}
				if (piece.getPri() == true && piece.getX2() == m_Size - 1) { // ���� �ǽ��� ������ ���� �����ϸ�
					signal = 1; // ������ �������� �˸�
				}
			} 
		}

		return signal; // ��ȣ ����
	} // �� �ǽ� �߰� �� ������Ʈ �� ȣ��

	public Piece findPiece(int s) {
		for (Piece piece : m_Pieces) { // �ǽ� �迭 �ݺ�
			if (piece.getSerial() == s && piece.getSize() != 1) { // �Է� ���� �ø����� ���ٸ�
				return piece; // �ǽ� ��ȯ
			}
		}
		return null;
	} // ��ǥ �־����� �� piece ã�� /

	public boolean couldMoveMinus(Piece piece) {
		int stride = piece.getStride(); // �ǽ� ����
		int x = piece.getX1(); // �ǽ��� ù ��ǥ
		int y = piece.getY1(); // �ǽ��� ù ��ǥ
		if (stride == 0) { // �ǽ� ����
			x -= 1; // ù��ǥ -1
		} else { // ����
			y -= 1; // ù��ǥ -1
		}
		if (0 <= x && 0 <= y && m_Board[y][x] == 0) { // ������ �����ȿ��� ù��ǥ�� 0�̶��
			return true; // ��
		} else {
			return false; // ����
		}
	} // �ǽ��� �ڷ� �̵� �������� ���� ����

	public boolean couldMovePlus(Piece piece) {
		int stride = piece.getStride(); // �ǽ� ����
		int x = piece.getX2(); // �ǽ��� ����ǥ
		int y = piece.getY2(); // �ǽ��� ����ǥ

		if (stride == 0) { // ����
			x += 1; // ����ǥ + 1
		} else {
			y += 1; // ����ǥ + 1
		}

		if (x < m_Size && y < m_Size && this.m_Board[y][x] == 0) { // ������ ������ �����ȿ��� ����ǥ�� 0�̶��
			return true; // ��
		} else {
			return false; // ����
		}
	} // �ǽ��� ������ �̵� �������� ���� ����

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
