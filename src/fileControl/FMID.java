package fileControl;

class FMID implements Id {
	//countÿ�δ��ļ���ȡ
	private static int count = 0;
	private String id; 
	FMID() {
		count++;
		//count++�����ļ��е�count
	}
	
	String getID() {
		return id;
	}
	private int setID() {
		return 0;
		//������
	}
}
