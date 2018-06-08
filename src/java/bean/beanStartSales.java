package bean;

public class beanStartSales {
    private String projCode = "";
    private String projName = "";
    private String custCode = "";
    private String custName = "";
    private String status = "";
    private String centerCode = "";

    public beanStartSales(){}

    public void setProjCode(String projCode){this.projCode = projCode;}
    public void setProjName(String projName){this.projName = projName;}
    public void setCustCode(String custCode){this.custCode = custCode;}
    public void setCustName(String custName){this.custName = custName;}
    public void setProjStatus(String status){this.status = status;}
    public void setCenterCode(String centerCode){this.centerCode = centerCode;}

    public String getProjCode(){return projCode;}
    public String getProjName(){return projName;}
    public String getCustCode(){return custCode;}
    public String getCustName(){return custName;}
    public String getProjStatus(){return status;}
    public String getCenterCode(){return centerCode;}
}
