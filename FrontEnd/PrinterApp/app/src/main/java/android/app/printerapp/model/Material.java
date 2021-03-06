package android.app.printerapp.model;

/**
 * Created by SAMSUNG on 2017-12-04.
 */

public class Material implements DataEntry{
    private String id;
    private String creationDate;
    private String pdfname;

    public String getPdfName(){
        return pdfname;
    }

    @Override
    public String getName() {
        return getIdName();
    }
    @Override
    public String getId(){
        return id;
    }

    @Override
    public String getCreationDate(){
        return creationDate;
    }


    @Override
    public String getIdName() {
        return "M" + id;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setCreationDate(String creationDate){
        this.creationDate = creationDate;
    }

    public void setPdfName(String pdfName){
        this.pdfname = pdfName;
    }
}
