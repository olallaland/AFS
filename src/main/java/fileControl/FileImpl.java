package main.java.fileControl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import main.java.application.WriteFileCmd;
import main.java.blockControl.BlockImpl;
import main.java.blockControl.BlockManagerImpl;
import main.java.constant.FileConstant;
import main.java.util.FileUtil;

public class FileImpl implements File {
	String fileData;
	FileMeta fileMeta;
	long pointer = 0;
	
	public FileImpl(FileMeta fileMeta) {
		this.fileMeta = fileMeta;
	}
	@Override
	public Id getFileId() {
		return fileMeta.fileId;
	}

	@Override
	public FileManager getFileManager() {
		return new FileManagerImpl(fileMeta.fmId);
	}

	@Override
	public byte[] read(int length) {
		//�ļ��������ֽ�
		byte[] total = new byte[(int)size()];
		//�û���Ҫ��ȡ���ֽ�
		System.out.println("yonghuxiangyaoduqudezijieshu: " + (int)(size() - pointer));
		byte[] result = new byte[(int) (size() - pointer)];
		HashMap<Integer, LinkedList<BlockImpl>> logicBlocks = fileMeta.getLogicBlocks();
		for(Integer i : logicBlocks.keySet()) {
			LinkedList<BlockImpl> duplicates = logicBlocks.get(i);
			byte[] tempContent = null;
			for(int j = 0; j < duplicates.size(); j++) {
				BlockImpl block = duplicates.get(j);
				if(block.isValid()) {
					tempContent = FileUtil.reads(block.getPath() + FileConstant.DATA_SUFFIX);
				} else {
					duplicates.remove(j);
					continue;
				}	
				tempContent = FileUtil.reads(block.getPath() + FileConstant.DATA_SUFFIX);	
			}
			
			//�����logicBlock�µ�����block����Ч����ôΪ�ļ��ն�
			if(tempContent.length == 0) {
				tempContent = new byte[FileConstant.BLOCK_SIZE];
			}
			//ƴ��ÿ��block������
			System.arraycopy(tempContent, 0, total, i * FileConstant.BLOCK_SIZE, tempContent.length);
		}
		System.arraycopy(total, (int) pointer, result, 0, (int) (size() - pointer));
		return result;
	}

	@Override
	public void write(byte[] bytes) {
		//1. �Ƚ�pointer֮ǰ�ǲ�������д���·����block��
		//2. ����һ����ĩβ��ʼд���µ�����
		//3. ����µ����ݵ�size < ��֮ǰ���ݵ�size - pointer����
		//��ô��ʣ����ⲿ�ֽ���д���µ�block��
		//��������֮ǰfiledata��������Ȼ����ͷȥβ����ƴ�ӣ���д
		//1. ���pointer����filesize����ôpointer-filesizeΪ��Ҫ���0x00�Ĳ��֣�
		//���е�һ����Ϊ���һ��block�Ŀհ�size���ڶ�����Ϊ����blockΪ�գ�
		//��������Ϊһ��blockǰ��һ����sizeΪ�գ�Ȼ��ʼд����
		//2. ���pointerС��filesize����ô����䲻Ϊblocksize����������
		//��ô�Ӵ�һ��block���в���ʼд���Ƚ����ݶ���������ָ��λ�ÿ�ʼ�ı�blockdata��д�أ�
		//����������£����contentsize<filesize-pointer,��ô����ʣ�ಿ�ָ��Ƶ�content��ĩβ
		long fileSize = fileMeta.fileSize;
		HashMap<Integer, LinkedList<BlockImpl>> logicBlocks = fileMeta.getLogicBlocks();
		LinkedList<BlockImpl> duplicates;
		int blockCount = fileMeta.blockCount;
		if(pointer >= fileMeta.fileSize) {
			
			long fileHoleCount = pointer - fileSize;
			System.out.println("�ն�: " + fileHoleCount);
			//���һ�� content������ֱ����������һ��block��
			//��ȡ��block�����ݣ�Ȼ�󿽱�����һ��block
			if(fileSize % FileConstant.BLOCK_SIZE != 0) {
				System.out.println("��������ȷ�ķ���");
				BlockImpl lastBlock = logicBlocks.get(blockCount - 1).get(0);//���ﻹ����block�Ƿ�valid
				byte[] temp = lastBlock.read();
				System.out.println("the last block size is(should be less than 8): " + temp.length);
				//
				byte[] large = new byte[(int) (temp.length + fileHoleCount + bytes.length)];
				//�����һ��block�����ݸ��Ƶ�������Ŀ�ͷ
				System.arraycopy(temp, 0, large, 0, temp.length);
				//����ļ��ն�������
				for(int i = temp.length; i < fileHoleCount + temp.length; i++) {
					large[i] = 0x00;
				}
				//����Ҫд������ݵ�����������һ����
				System.arraycopy(bytes, 0, large, (int) (temp.length + fileHoleCount), bytes.length);
				
				//large��������������ļ�block index��blockCount - 1��ʼ������
				subWrite(large, blockCount - 1);
			} else if(fileSize % FileConstant.BLOCK_SIZE == 0){ //��һ�����ļ�д���������ڴ���
				byte[] large = new byte[(int) (fileHoleCount + bytes.length)];
				//����ļ��ն�������
				for(int i = 0; i < fileHoleCount; i++) {
					large[i] = 0x00;
				}
				//����Ҫд������ݵ�����������һ����
				System.arraycopy(bytes, 0, large, (int)fileHoleCount, bytes.length);
				subWrite(large, blockCount);
			}
		}
		
	}
	
	void subWrite(byte[] bytes, int beginBlock) {
		int position = 0;
		String filename = "";
		String content = "";
		BlockManagerImpl bm;
		int blockCount = fileMeta.blockCount;
		long fileSize = fileMeta.fileSize;
		
		int newBlockCount = (bytes.length % FileConstant.BLOCK_SIZE) == 0
				? (bytes.length / FileConstant.BLOCK_SIZE)
				: (bytes.length / FileConstant.BLOCK_SIZE) + 1;
		System.out.println("new block count : " + newBlockCount);
		
		for(int i = 0; i < newBlockCount; i++) {
			LinkedList<BlockImpl> duplicates = new LinkedList<BlockImpl>();
			byte[] temp;
			int begin = i * FileConstant.BLOCK_SIZE;
			int end = (i + 1) * FileConstant.BLOCK_SIZE;
			//System.out.println("copy begins at: " + begin + "\n ends at: " + end);
			if(end > bytes.length) {
				temp = Arrays.copyOfRange(bytes, begin, bytes.length);			
			} else {
				temp = Arrays.copyOfRange(bytes, begin, end);
			}		
			System.out.println("д����ֽ�: " + new String(temp));
			bm = WriteFileCmd.allocBm();
			BlockImpl block = (BlockImpl) bm.newBlock(temp);
			try {
				duplicates.add(block);
				//�޸�fileMeta��logicBlock��
				fileMeta.setLogicBlocks(i + beginBlock, duplicates);
			} catch(Exception e) {
				System.out.println("Add block to fileMeta error : " + e);
			}
		}
		
		fileMeta.setFileSize(fileMeta.fileSize + bytes.length - fileMeta.fileSize % FileConstant.BLOCK_SIZE);
		//���������⣡����
		fileMeta.setBlockCount(fileMeta.blockCount + newBlockCount);
		
		//4. д����ɺ󣬽�fileMeta������д�ض�Ӧ�ļ�
		try {
			fileMeta.write();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("д��filemetaʱ����" + e1);
		}
		try {
			
			fileMeta.read();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("��ȡfileMetaʱ��������" + e);
		}

	}

	@Override
	public long move(long offset, int where) {
		switch(where) {
			case MOVE_HEAD:
				pointer = offset;
				break;
			case MOVE_CURR:
				pointer += offset;
				break;
			case MOVE_TAIL:
				pointer = size() + offset;
				break;
			default:
				throw new RuntimeException("�����ָ���ƶ�λ�ò�������");
		}
		return 0;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public long size() {
		// TODO Auto-generated method stub
		return fileMeta.fileSize;
	}

	@Override
	public void setSize(long newSize) {
		// TODO Auto-generated method stub

	}

}
