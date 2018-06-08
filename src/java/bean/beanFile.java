package bean;

public class beanFile {
    private String prodCode = "";
    private String taskID = "";
    private String docName = "";
    private String docFile = "";
    private String docType = "";
    private String docCount = "";

    public beanFile(){}

    public void setProdCode(String prodCode){this.prodCode = prodCode;}
    public void setTaskID(String taskID){this.taskID = taskID;}
    public void setDocName(String docName){this.docName = docName;}
    public void setDocFile(String docFile){this.docFile = docFile;}
    public void setDocType(String docType){this.docType = docType;}
    public void setDocCount(String docCount){this.docCount = docCount;}

    public String getProdCode(){return this.prodCode;}
    public String getTaskID(){return this.taskID;}
    public String getDocName(){return this.docName;}
    public String getDocFile(){return this.docFile;}
    public String getDocType(){return this.docType;}
    public String getDocCount(){return this.docCount;}
}
