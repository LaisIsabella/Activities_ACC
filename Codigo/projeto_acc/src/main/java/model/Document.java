package model;

import java.io.File;

public class Document {

    private String fileName;
    private String filePath;

    public Document(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }
    
    public File getFile() {
       return new File(filePath);
    }
}
