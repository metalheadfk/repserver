package bean;

public class beanLog {
    private String prodCode = "";
    private String taskID = "";
    private String comment = "";
    private String workDate = "";
    private String progress = "";
    private String color = "";

    public beanLog(){}

    public void setProdCode(String prodCode){this.prodCode = prodCode;}
    public void setTaskID(String taskID){this.taskID = taskID;}
    public void setComment(String comment){this.comment = comment;}
    public void setWorkDate(String workDate){this.workDate = workDate;}
    public void setProgress(String progress){this.progress = progress;}
    public void setColor(String color){this.color = color;}

    public String getProdCode(){return this.prodCode;}
    public String getTaskID(){return this.taskID;}
    public String getComment(){return this.comment;}
    public String getWorkDate(){return this.workDate;}
    public String getProgress(){return this.progress;}
    public String getColor(){return this.color;}
}
