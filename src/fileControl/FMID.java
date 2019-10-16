package fileControl;

class FMID implements Id {
	//count每次从文件读取
	private static int count = 0;
	private String id; 
	FMID() {
		count++;
		//count++更新文件中的count
	}
	
	String getID() {
		return id;
	}
	private int setID() {
		return 0;
		//错误处理
	}
}
