package Common;

import java.io.Serializable;

/**
 * An object to store file meta data.
 */
public class MetaData implements Serializable {
    private String fileName;
    private Integer size;
    private String owner;
    private Boolean public_access;
    private Boolean write_access;

    public MetaData(String fileName, Integer size, String owner, Boolean public_access, Boolean write_access){
        this.fileName = fileName;
        this.owner = owner;
        this.size = size;
        this.public_access = public_access;
        this.write_access = write_access;
    }

    public String getFileName(){
        return fileName;
    }

    public Integer getSize(){
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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setPublic_access(Boolean public_access) {
        this.public_access = public_access;
    }

    public void setWrite_access(Boolean write_access) {
        this.write_access = write_access;
    }

    public void printData(){
        System.out.println("----------------");
        System.out.println(fileName + ":");
        System.out.println("----------------");
        System.out.println("Owner: " + owner);
        System.out.println("Size: " + size);
        if(public_access){
            System.out.println("Public access: Yes");
        }else{
            System.out.println("Public access: No");
        }
        if(write_access){
            System.out.println("Write access: Yes");
        }else{
            System.out.println("Write access: No");
        }
        System.out.println("----------------");
    }
}
