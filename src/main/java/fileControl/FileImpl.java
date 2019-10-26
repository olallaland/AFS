package main.java.fileControl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import main.java.application.WriteFileCmd;
import main.java.blockControl.BlockImpl;
import main.java.blockControl.BlockManagerImpl;
import main.java.constant.FileConstant;
import main.java.exception.ErrorCode;
import main.java.id.Id;
import main.java.util.FileUtil;

public class FileImpl implements File {
	String fileData;
	FileMeta fileMeta;
	long pointer = 0;
	Id fileId;
	
	public FileImpl(FileMeta fileMeta) {
		this.fileMeta = fileMeta;
		System.out.println("pointer when create new file object: " + pointer);
	}
	
	public FileImpl(Id fileId) {
		this.fileId = fileId;
	}
	
	@Override
	public Id getFileId() {
		return fileMeta.fileId;
	}

	@Override
	public FileManager getFileManager() {
		return new FileManagerImpl(fileMeta.getFmId());
	}

	@Override
	public byte[] read(int length) {
		System.out.println("�û���ȡ��������ʼָ��: " + pointer);
		if(length + pointer > size()) {
			throw new ErrorCode(9);
		}
		//�ļ��������ֽ�
		byte[] total = new byte[(int)size()];
		//�û���Ҫ��ȡ���ֽ�
		System.out.println("�û���Ҫ��ȡ���ֽ���: " + length);
		byte[] result = new byte[length];
		
		HashMap<Integer, LinkedList<BlockImpl>> logicBlocks = fileMeta.getLogicBlocks();
		for(Integer i : logicBlocks.keySet()) {
			LinkedList<BlockImpl> duplicates = logicBlocks.get(i);
			byte[] tempContent = null;
			while(duplicates.size() != 0) {
				BlockImpl block = duplicates.get(0);
				if(block.isValid()) {
					tempContent = FileUtil.reads(block.getPath() + FileConstant.DATA_SUFFIX);
					break;
				} else {
					duplicates.remove(0);
				}
			}
			
			//�����logicBlock�µ�����block����Ч����ôΪ�ļ��ն�
			if(duplicates.size() == 0) {
				tempContent = new byte[FileConstant.BLOCK_SIZE];
				throw new ErrorCode(2);
			}
			//ƴ��ÿ��block������
			System.arraycopy(tempContent, 0, total, i * FileConstant.BLOCK_SIZE, tempContent.length);
		}
		System.arraycopy(total, (int) pointer, result, 0, length);
		
		move(length, 0);
		try {
			fileMeta.write();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
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
		int blockCount = fileMeta.blockCount;
		
		/**
		 * pointer��file��β����β��֮��
		 */
		if(pointer >= fileMeta.fileSize) {
			
			long fileHoleCount = pointer - fileSize;
			System.out.println("�ն�: " + fileHoleCount);
			System.out.println("pointer��file��β����β��֮��");
			//���һ�� ֮ǰ�ļ�λռ��block
			//��Ҫ��ȡ֮ǰblock������
			if(fileSize % FileConstant.BLOCK_SIZE != 0) {
				System.out.println("��Ҫ��ȡ֮ǰblock�����ݣ�");
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
				subWrite(large, blockCount - 1, fileSize + large.length - fileSize % FileConstant.BLOCK_SIZE);
				
			//���һ��block������������ֱ��д
			} else if(fileSize % FileConstant.BLOCK_SIZE == 0){ 
				System.out.println("���һ��block������������ֱ��д��");
				byte[] large = new byte[(int) (fileHoleCount + bytes.length)];
				//����ļ��ն�������
				for(int i = 0; i < fileHoleCount; i++) {
					large[i] = 0x00;
				}
				//����Ҫд������ݵ�����������һ����
				System.arraycopy(bytes, 0, large, (int)fileHoleCount, bytes.length);
				subWrite(large, blockCount, fileSize + large.length);
			}
		
		/**
		 * pointer��file�в�
		 */
		} else {
			System.out.println("pointer��file�в�");
			int preBlockCount = (int) (pointer / FileConstant.BLOCK_SIZE);
			long newSize = 0;
			byte[] large;
			byte[] larger;
			
			//��Ҫ��ȡ֮ǰblock������
			if(pointer % FileConstant.BLOCK_SIZE != 0) {
				System.out.println("��Ҫ��ȡ֮ǰblock������");
				int leftInBlock = (int) (pointer % FileConstant.BLOCK_SIZE);
				BlockImpl blockBeforeWrite = logicBlocks.get(preBlockCount).get(0);
				byte[] temp = blockBeforeWrite.read();
				newSize = leftInBlock + bytes.length;
				large = new byte[(int) newSize];
				System.arraycopy(temp, 0, large, 0, leftInBlock);
				System.arraycopy(bytes, 0, large, leftInBlock, bytes.length);
				
			//����ֱ��д
			} else {
				System.out.println("����ֱ��д");
				newSize = bytes.length;
				large = new byte[bytes.length];
				System.arraycopy(bytes, 0, large, 0, bytes.length);
			}
			
			if((bytes.length < fileSize - pointer) && newSize % FileConstant.BLOCK_SIZE != 0) {
				System.out.println("δ����fileSizeĩβ��Ҫ����");
				BlockImpl blockInTail = logicBlocks.get(preBlockCount + (int)(newSize / FileConstant.BLOCK_SIZE)).get(0);
				byte[] blockInTailContent = blockInTail.read();
				int addToTail = (int) (blockInTailContent.length - newSize % FileConstant.BLOCK_SIZE);
				larger = new byte[large.length + addToTail];
				System.arraycopy(large, 0, larger, 0, large.length);
				System.arraycopy(blockInTailContent, (int) (newSize % FileConstant.BLOCK_SIZE), larger, large.length, addToTail);
				
				subWrite(larger, preBlockCount, fileSize);
			} else if(bytes.length >= fileSize - pointer){
				
				System.out.println("����fileSize");
				subWrite(large, preBlockCount, bytes.length + pointer);
			} else {
				System.out.println("δ����fileSizeĩβ��������Ҫ����");
				subWrite(large, preBlockCount, fileSize);
			}
		}
		move(bytes.length, 0);
		System.out.println("after write the pointer is: " + pointer);
		
	}
	
	void subWrite(byte[] bytes, int beginBlock, long totalFileSize) {
		BlockManagerImpl bm;
		
		int newBlockCount = (bytes.length % FileConstant.BLOCK_SIZE) == 0
				? (bytes.length / FileConstant.BLOCK_SIZE)
				: (bytes.length / FileConstant.BLOCK_SIZE) + 1;
		System.out.println("new block count : " + newBlockCount);
		
		for(int i = 0; i < newBlockCount; i++) {
			LinkedList<BlockImpl> duplicates = new LinkedList<BlockImpl>();
		
			int begin = i * FileConstant.BLOCK_SIZE;
			int end = (i + 1) * FileConstant.BLOCK_SIZE;
			//System.out.println("copy begins at: " + begin + "\n ends at: " + end);
			
			//���������ͬ�ĸ���
			for(int j = 0; j < FileConstant.DUPLICATION_COUNT; j++) {
				byte[] temp;
				if(end > bytes.length) {
					temp = Arrays.copyOfRange(bytes, begin, bytes.length);			
				} else {
					temp = Arrays.copyOfRange(bytes, begin, end);
				}		
				System.out.println("д����ֽ�: " + new String(temp));
				bm = WriteFileCmd.allocBm();
				System.out.println("in fileImpl creating block manager id: " + bm.getStringBmId());
				BlockImpl block = (BlockImpl) bm.newBlock(temp);
				
				duplicates.add(block);
			}
			
				fileMeta.addLogicBlocks(i + beginBlock, duplicates);
			
		}
		
		fileMeta.setFileSize(totalFileSize);
		
		fileMeta.setBlockCount((int) ((totalFileSize % FileConstant.BLOCK_SIZE) == 0
				? (totalFileSize / FileConstant.BLOCK_SIZE)
				: (totalFileSize / FileConstant.BLOCK_SIZE) + 1));
		
		//4. д����ɺ󣬽�fileMeta������д�ض�Ӧ�ļ�
		try {
			fileMeta.write();
		} catch (Exception e1) {		
			System.out.println(e1.getMessage());
		}
		
		try {
			
			fileMeta.read();
		} catch (Exception e) {		
			System.out.println(e.getMessage());
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
		return fileMeta.fileSize;
	}

	@Override
	public void setSize(long newSize) {
		byte[] oldContent = read((int) fileMeta.fileSize);
		byte[] newContent = new byte[(int) newSize];
		long oldSize = oldContent.length;
		int diff = (int) (newSize - oldSize);
		
		//ԭ�ļ�����С�ڻ�����µĳ��ȣ��ں����0x00
		if(diff >= 0) {
			System.arraycopy(oldContent, 0, newContent, 0, (int) oldSize);
			for(int i = 0; i < diff; i++) {
				newContent[(int) (oldSize + i)] = 0x00;
			}
		} else {
			System.arraycopy(oldContent, 0, newContent, 0, (int) newSize);
		}
		fileMeta.logicBlocks.clear();
		subWrite(newContent, 0, newSize);

	}
	public void setFileMeta(FileMeta fileMeta) {
		this.fileMeta = fileMeta;	
	}

}
