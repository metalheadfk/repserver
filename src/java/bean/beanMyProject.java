package bean;

public class beanMyProject {
    private String projCode = "";
    private String prodCode = "";
    private String prodDesc = "";
    private String prodConf = "";
    private String startTask = "";
    private String endTask = "";
    private String templateCode = "";
    private String templateName = "";

    private String startTaskA = "";
    private String endTaskA = "";
    private String prodCarNo = "";
    private String prodType = "";

    public beanMyProject(){}

    public void setProjCode(String projCode){this.projCode = projCode;}
    public void setProdCode(String prodCode){this.prodCode = prodCode;}
    public void setProdDesc(String prodDesc){this.prodDesc = prodDesc;}
    public void setProdConf(String prodConf){this.prodConf = prodConf;}
    public void setStartTask(String startTask){this.startTask = startTask;}
    public void setEndTask(String endTask){this.endTask = endTask;}
    public void setTemplateCode(String templateCode){this.templateCode = templateCode;}
    public void setTemplateName(String templateName){this.templateName = templateName;}
    public void setStartTaskA(String startTaskA){this.startTaskA = startTaskA;}
    public void setEndTaskA(String endTaskA){this.endTaskA = endTaskA;}
    public void setProdCarNo(String prodCarNo){this.prodCarNo = prodCarNo;}
    public void setProdType(String prodType){this.prodType = prodType;}

    public String getProjCode(){return projCode;}
    public String getProdCode(){return prodCode;}
    public String getProdDesc(){return prodDesc;}
    public String getProdConf(){return prodConf;}
    public String getStartTask(){return startTask;}
    public String getEndTask(){return endTask;}
    public String getTemplateCode(){return templateCode;}
    public String getTemplateName(){return templateName;}
    public String getStartTaskA(){return startTaskA;}
    public String getEndTaskA(){return endTaskA;}
    public String getProdCarNo(){return prodCarNo;}
    public String getProdType(){return prodType;}
}
