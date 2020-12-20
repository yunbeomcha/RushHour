
public class Move {
	private int[] obj;
	private int[] move;
	private int index;

	public Move() {
		obj = new int[100000]; // ũ�� 200�� ���� �迭 ���� - ������ �ǽ��� �ø��� ����
		move = new int[100000]; // ũ�� 200�� ���� �迭 ���� - ������ ĭ�� ����
		index = -1;
	} // ������

	public void doMove(int Serial, int Move) { // �����϶����� �迭��� ����
		this.index++; // �ε��� �� ����
		this.obj[index] = Serial; // �迭�� �ε��� ��ġ�� �ǽ��� �ø��� ����
		this.move[index] = Move; // �迭�� �ε��� ��ġ�� �ǽ��� ������ ĭ �� ����
	} // �ǽ� �̵� ���

	public int undoObj() {
		return this.obj[index];
	} // �ǵ��� ��ü Serial ��ȯ

	public int undoMove() {
		return this.move[index];
	} // �ǵ��� ũ�� ��ȯ

	public Piece findPiece(Board Board, int Serial) {
		for (Piece piece : Board.getPieces()) { // �ǽ� �迭��� �ݺ�
			if (piece.getSerial() == Serial) { // �ø��� ��ȣ�� ���ٸ�
				return piece; // �ǽ� ��ȯ
			}
		}
		return null;
	} // Serial�� ���� �ǽ� ã��

	public void reset(Board Board, String desc) {
		Board.setPieces(Piece.serialToPieces(desc)); // �ǽ� �迭�� �ʱ�ȭ
		Board.updateBoard(); // �ǽ� ������Ʈ
		index = -1;
	} // ���� �ʱ�ȭ

	public int[] getObj() {
		return obj;
	};

	public int[] getMove() {
		return move;
	};

	public int getIndex() {
		return index;
	};

	public void setObj(int[] i) {
		obj = i;
	};

	public void setMove(int[] i) {
		move = i;
	};

	public void setIndex(int i) {
		index = i;
	};
	
	public void setUndo(int m, int serial) {
		index++;
		obj[index] = serial;
		move[index] = m;
	}
}
