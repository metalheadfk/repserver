package bean;

public class beanProjMovex {
    private String projCode = "";
    private String projName = "";
    private String custCode = "";
    private String custName = "";
    private String saleCode = "";
    private String saleName = "";
    private String coorCode = "";
    private String coorName = "";
    private String projQty = "";
    private String startProject = "";
    private String endProject = "";
    private String priority = "";
    private String status = "";

    private String priorityName = "";

    public beanProjMovex(){}

    public void setProjCode(String projCode){this.projCode = projCode;}
    public void setProjName(String projName){this.projName = projName;}
    public void setCustCode(String custCode){this.custCode = custCode;}
    public void setCustName(String custName){this.custName = custName;}
    public void setSaleCode(String saleCode){this.saleCode = saleCode;}
    public void setSaleName(String saleName){this.saleName = saleName;}
    public void setCoorCode(String coorCode){this.coorCode = coorCode;}
    public void setCoorName(String coorName){this.coorName = coorName;}
    public void setProjQty(String projQty){this.projQty = projQty;}
    public void setStartProject(String startProject){this.startProject = startProject;}
    public void setEndProject(String endProject){this.endProject = endProject;}
    public void setPriority(String priority){this.priority = priority;}
    public void setPriorityName(String priorityName){this.priorityName = priorityName;}
    public void setStatus(String status){this.status = status;}

    public String getProjCode(){return projCode;}
    public String getProjName(){return projName;}
    public String getCustCode(){return custCode;}
    public String getCustName(){return custName;}
    public String getSaleCode(){return saleCode;}
    public String getSaleName(){return saleName;}
    public String getCoorCode(){return coorCode;}
    public String getCoorName(){return coorName;}
    public String getProjQty(){return projQty;}
    public String getStartProject(){return startProject;}
    public String getEndProject(){return endProject;}
    public String getPriority(){return priority;}
    public String getStatus(){return status;}

    public String getPriorityName(){return priorityName;}
}
