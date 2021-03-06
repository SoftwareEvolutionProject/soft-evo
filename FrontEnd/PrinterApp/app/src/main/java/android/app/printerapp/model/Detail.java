package android.app.printerapp.model;

import java.util.Date;

/**
 * Created by Johan on 11/1/2017.
 */

public class Detail implements DataEntry {
    private String id;
    private String name;
    private String creationDate;
    private int companyId;
    private String fileId;
    private int projectId;
//    private Date creationDate;
    private String comment;


    public String getId() {
        return id;
    }

    public void setCreationDate(String creationDate){
        this.creationDate = creationDate;
    }

    @Override
    public String getCreationDate() {
        return creationDate;
    }

    @Override
    public String getIdName() {
        return "D" + id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

//    public String getOriginalFileName() {
//        return fileId;
//    }
//
//    public void setOriginalFileName(String fileId) {
//        this.fileId = fileId;
//    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

//    public Date getCreationDate() {
//        return creationDate;
//    }
//
//    public void setCreationDate(Date creationDate) {
//        this.creationDate = creationDate;
//    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
