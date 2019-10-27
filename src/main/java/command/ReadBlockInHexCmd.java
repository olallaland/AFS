package main.java.command;

import main.java.block.BlockImpl;
import main.java.block.BlockManagerImpl;
import main.java.exception.ErrorCode;
import main.java.id.IntegerId;
import main.java.id.StringId;

public class ReadBlockInHexCmd extends Command {
	public ReadBlockInHexCmd(String[] cmds) {
		//1. 检查用户指令输入的的正确性（参数个数以及文件是否存在）
		if(cmds.length != 2) {
			throw new ErrorCode(7);
		} else {
			try {
				readBlockInHex(cmds[1]);
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}	
	}

	private void readBlockInHex(String blockId) {
		byte[] blockData = null;
		String bmStringId = "";
		BlockManagerImpl bm;
		BlockImpl block;
		int id;

		try {
			id = Integer.valueOf(blockId);
		} catch (Exception e ) {
			throw new ErrorCode(8);
		}

		try {
			bmStringId = findBm(blockId);
			bm = new BlockManagerImpl(new StringId(bmStringId));
			block = (BlockImpl)bm.getBlock(new IntegerId(id));
		} catch (ErrorCode e) {
			throw new ErrorCode(e.getErrorCode());
		}

		if(block.isValid()) {
			blockData = block.read();
			for(int i = 0; i < blockData.length; i++) {
				System.out.print("0x" + Integer.toHexString(blockData[i] & 0xFF) + " ");
				if((i + 1) % 16 == 0) {
					System.out.println();
				}
			}
			System.out.println();
		} else {
			throw new ErrorCode(2);
		}
	}

}
 