package main.java.command;

import main.java.exception.ErrorCode;
import main.java.file.FileImpl;
import main.java.file.FileManagerImpl;
import main.java.id.StringId;

public class DeleteFileCmd extends Command {
    public DeleteFileCmd(String[] cmds) {
        if(cmds.length != 2) {
            throw new ErrorCode(7);
        } else {
            try {
                deleteFile(cmds[1]);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void deleteFile(String filename) {
        FileImpl file = null;
        String fmStringId = "";
        FileManagerImpl fm = null;

        try{
            fmStringId = findFm(filename);
            fm = getFmById(new StringId(fmStringId));
            file = (FileImpl)fm.getFile(new StringId(filename));
            System.out.println(fm.deleteFile(file));

        } catch (ErrorCode e) {
            throw new ErrorCode(e.getErrorCode());
        }
    }

}
