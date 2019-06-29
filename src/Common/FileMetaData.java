package Common;

import java.io.Serializable;

/**
 * An object to hold the meta data about a file
 */
public class FileMetaData implements Serializable {
    private String fileName;
    private int size;
    private String owner;
    private Boolean public_access;
    private Boolean write_access;

    public FileMetaData(String fileName, int size, String owner, Boolean public_access, Boolean write_access){
        this.fileName = fileName;
        this.owner = owner;
        this.size = size;
        this.public_access = public_access;
        this.write_access = write_access;
    }

    public String getFileName(){
        return fileName;
    }

    public int getSize(){
        return size;
    }

    public String getOwner(){
        return owner;
    }

    public  Boolean getPublic_access(){
        return public_access;
    }

    public Boolean getWrite_access(){
        return write_access;
    }
}
