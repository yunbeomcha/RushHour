import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Piece {
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private boolean is_Primary;
	private int Serial;
	private int Size;

	public Piece(int x1, int y1, int x2, int y2, boolean primary, int Serial, int Size) { // �ǽ� ������
		this.x1 = x1; // x1 ��ǥ ����
		this.y1 = y1; // y1 ��ǥ ����
		this.x2 = x2; // x2 ��ǥ ����
		this.y2 = y2; // y2 ��ǥ ����
		this.is_Primary = primary; // ���� �ǽ� ����
		this.Serial = Serial; // �ø��� �ѹ� ����
		this.Size = Size; // �ǽ� ������ ����
	}

	public int getStride() { // �ǽ��� ����
		if (x2 - x1 != 0) {
			return 0; // horizontal
		} else {
			return 1; // vertical
		}
	}

	public void movePiece(int stride) { // �ǽ� �̵� �Լ�
		if (getStride() == 0) { // �����̶��
			x1 += stride; // x��ǥ ����
			x2 += stride; // x��ǥ ����
		} else { // �����̶��
			y1 += stride; // y��ǥ ����
			y2 += stride; // y��ǥ ����
		} // if
	}

	public static Piece[] serialToPieces(String desc) {
		ArrayList<Piece> pcList = new ArrayList<Piece>();
		int intBoardSize = 6; // 1. ������ ���ڷ� ����

		HashMap<Character, ArrayList<Integer>> positions = new HashMap<Character, ArrayList<Integer>>();
		for (var i = 0; i < desc.length(); i++) {
			var label = desc.charAt(i);
			if (!positions.containsKey(label)) {
				positions.put(label, new ArrayList<Integer>());
			}
			positions.get(label).add(i);
		}

		// sort piece labels
		Character[] labels = positions.keySet().toArray(new Character[0]);
		Arrays.sort(labels);

		// add pieces
		for (Character label : labels) {
			if (label == '.' || label == 'o') {
				continue;
			}
			if (label == 'x') {
				continue;
			}
			ArrayList<Integer> ps = positions.get(label);
			int Size = ps.size();
			int x1 = ps.get(0) % intBoardSize;
			int y1 = ps.get(0) / intBoardSize;
			int x2 = ps.get(Size-1) % intBoardSize;
			int y2 = ps.get(Size-1) / intBoardSize;
			boolean primary = label == 'A';
			int Serial = label - 'A';

			Piece piece = new Piece(x1, y1, x2, y2, primary, Serial, Size);
			//System.out.println(piece.toString() + "Label: " + label + " Size: " + Size);
			pcList.add(piece);
		}

		// add walls
		if (positions.containsKey('x')) {
			ArrayList<Integer> ps = positions.get('x');
			for (int i = 0; i < ps.size(); i++) {
				int x = ps.get(i) % intBoardSize;
				int y = ps.get(i) / intBoardSize;
				int Serial = 'x';
				Piece piece = new Piece(x, y, x, y, false, Serial, 1);
				//System.out.println("<wall>" + piece.toString());
				pcList.add(piece);
			}
		}

		return pcList.toArray(new Piece[pcList.size()]);
	}

	public String toString() {
		return String.format("(%d, %d) - (%d, %d), is_Primary:%b, Serial:%d, Size:%d", x1, y1, x2, y2, is_Primary,
				Serial, Size);
	}

	public int getX1() {
		return x1;
	}

	public int getX2() {
		return x2;
	}

	public int getY1() {
		return y1;
	}

	public int getY2() {
		return y2;
	}

	public void setX1(int i) {
		x1 = i;
	}

	public void setX2(int i) {
		x2 = i;
	}

	public void setY1(int i) {
		y1 = i;
	}

	public void setY2(int i) {
		y2 = i;
	}

	public boolean getPri() {
		return is_Primary;
	}

	public int getSerial() {
		return Serial;
	}

	public int getSize() {
		return Size;
	}

	public void setPri(boolean i) {
		is_Primary = i;
	}

	public void setSerial(int i) {
		Serial = i;
	}

	public void setSize(int i) {
		Size = i;
	}
}
