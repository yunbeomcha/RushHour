
public class Move {
	private int[] obj;
	private int[] move;
	private int index;

	public Move() {
		obj = new int[100000]; // 크기 200의 정수 배열 선언 - 움직인 피스의 시리얼 저장
		move = new int[100000]; // 크기 200의 정수 배열 선언 - 움직인 칸수 저장
		index = -1;
	} // 생성자

	public void doMove(int Serial, int Move) { // 움직일때마다 배열요소 저장
		this.index++; // 인덱스 값 증가
		this.obj[index] = Serial; // 배열의 인덱스 위치에 피스의 시리얼 저장
		this.move[index] = Move; // 배열의 인덱스 위치에 피스의 움직인 칸 수 저장
	} // 피스 이동 기록

	public int undoObj() {
		return this.obj[index];
	} // 되돌릴 객체 Serial 반환

	public int undoMove() {
		return this.move[index];
	} // 되돌릴 크기 반환

	public Piece findPiece(Board Board, int Serial) {
		for (Piece piece : Board.getPieces()) { // 피스 배열요소 반복
			if (piece.getSerial() == Serial) { // 시리얼 번호가 같다면
				return piece; // 피스 반환
			}
		}
		return null;
	} // Serial을 통해 피스 찾기

	public void reset(Board Board, String desc) {
		Board.setPieces(Piece.serialToPieces(desc)); // 피스 배열을 초기화
		Board.updateBoard(); // 피스 업데이트
		index = -1;
	} // 보드 초기화

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
