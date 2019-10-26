package main.java.exception;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode extends RuntimeException {
	
	public static final int IO_EXCEPTION = 1;
	public static final int CHECKSUM_CHECK_FAILED = 2;
	public static final int CREATE_EXISTED_FILE = 3;
	public static final int FILE_NOT_EXIST = 4;
	public static final int BLOCK_NOT_EXIST = 5;
	public static final int UNSUPPORTED_ENCODING = 6;
	public static final int INCORRECT_ARGUMENT_FORMAT = 7;
	public static final int NUMBER_FORMAT_EXCEPTION = 8;
	public static final int READ_OUT_OF_BOUND = 9;
	public static final int COMMAND_NOT_FOUND = 10;
	
	public static final int UNKNOWN = 1000;
	
	private static final Map<Integer, String> ErrorCodeMap = new HashMap<>();
	static {
		ErrorCodeMap.put(IO_EXCEPTION, "IO exception");
		ErrorCodeMap.put(CHECKSUM_CHECK_FAILED, "block checksum check failed");
		ErrorCodeMap.put(CREATE_EXISTED_FILE, "create a existed file exception");
		ErrorCodeMap.put(FILE_NOT_EXIST, "file not exists exception");
		ErrorCodeMap.put(BLOCK_NOT_EXIST, "block not exists exception");
		ErrorCodeMap.put(UNSUPPORTED_ENCODING, "unsupported encoding exception");
		ErrorCodeMap.put(INCORRECT_ARGUMENT_FORMAT, "incorrect arguments format");
		ErrorCodeMap.put(NUMBER_FORMAT_EXCEPTION, "number format exception");
		ErrorCodeMap.put(READ_OUT_OF_BOUND, "read out of bound");
		ErrorCodeMap.put(COMMAND_NOT_FOUND, "command not found");
		
		ErrorCodeMap.put(UNKNOWN, "unknown");
	}
	
	public static String getErrorText(int errorCode) {
		return ErrorCodeMap.getOrDefault(errorCode, "invalid");
	}
	
	private int errorCode;
	
	public ErrorCode(int errorCode) {
		super(String.format("error code '%d' \"%s\"", errorCode, getErrorText(errorCode)));
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
}
