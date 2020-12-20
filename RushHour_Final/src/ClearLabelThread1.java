import java.awt.Color;

import javax.swing.JLabel;

public class ClearLabelThread1 extends JLabel implements Runnable {
	
	private int _start, _finish;
	private Thread myThread;
	private int _delayTime;
	private String _clear;
	
	public ClearLabelThread1() {
		_clear = "CLEAR";
		_delayTime = 100;
		myThread = null;
	} //Construct
	
	
	public ClearLabelThread1(String clear) {
		_clear = clear;
		_delayTime = 100;
		myThread = null;
	}
	
	public void ClearLabelThread(String label) {
		_clear = "CLEAR";
		_delayTime = 100;
		myThread = null;
	} //호출 되는 쪽
	
	public ClearLabelThread1(String label, String Clear) {
		super(label);
		_clear = Clear;
		_delayTime = 100;
		myThread = null;
	} 
	
	public String getClear() {
		return _clear;
	}
	
	public int getDelayTime() {
		return _delayTime;
	}
	public void setClear(String clear) {
		_clear = clear;
	}
	
	public void setDelayTime(int delay) {
		_delayTime = delay;
	}
	
	public void start() {
		if(myThread == null) {
			myThread = new Thread(this); //this는 LabelThread를 가리킴
		}
		myThread.start();
	}
	
	@Override
	public void run() {
		setForeground(Color.red);
		for(int i = _start; i <= _finish; i++) {
			setText("CLEAR!!");
			try {
				myThread.sleep(_delayTime); //0.1초를 뜻함
			} catch(Exception e) {}
		} //for
		
		for(int i= 0; i<50; i++) {
			setVisible(false);
			try {
				myThread.sleep(_delayTime+300);
			}catch(Exception e) {}
			setVisible(true);
			try {
				myThread.sleep(_delayTime+300);
			}catch(Exception e) {}
		}//for
	}
}
