package bean;

public class beanShipment {
    private String projCode = "";
    private String projName = "";
    private String custCode = "";
    private String custName = "";
    private String saleCode = "";
    private String saleName = "";
    private String qty = "";
    private String contractType = "";
    private String budgetCost = "";
    private String budgetRevenue = "";

    private String code = "";
    private String prodDate = "";
    private String shipDateP = "";
    private String shipDateA = "";
    private String siteDateP = "";
    private String siteDateA = "";
    private String remark = "";

    private String description = "";

    public beanShipment(){}

    public void setProjCode(String projCode){this.projCode = projCode;}
    public void setProjName(String projName){this.projName = projName;}
    public void setCustCode(String custCode){this.custCode = custCode;}
    public void setCustName(String custName){this.custName = custName;}
    public void setSaleCode(String saleCode){this.saleCode = saleCode;}
    public void setSaleName(String saleName){this.saleName = saleName;}
    public void setQty(String qty){this.qty = qty;}
    public void setContractType(String contractType){this.contractType = contractType;}
    public void setBudgetCost(String budgetCost){this.budgetCost = budgetCost;}
    public void setBudgetRevenue(String budgetRevenue){this.budgetRevenue = budgetRevenue;}

    public void setCode(String code){this.code = code;}
    public void setProdDate(String prodDate){this.prodDate = prodDate;}
    public void setShipDateP(String shipDateP){this.shipDateP = shipDateP;}
    public void setShipDateA(String shipDateA){this.shipDateA = shipDateA;}
    public void setSiteDateP(String siteDateP){this.siteDateP = siteDateP;}
    public void setSiteDateA(String siteDateA){this.siteDateA = siteDateA;}
    public void setRemark(String remark){this.remark = remark;}

    public void setDescription(String description){this.description = description;}

    public String getProjCode(){return projCode;}
    public String getProjName(){return projName;}
    public String getCustCode(){return custCode;}
    public String getCustName(){return custName;}
    public String getSaleCode(){return saleCode;}
    public String getSaleName(){return saleName;}
    public String getQty(){return qty;}
    public String getContractType(){return contractType;}
    public String getBudgetCost(){return budgetCost;}
    public String getBudgetRevenue(){return budgetRevenue;}

    public String getCode(){return code;}
    public String getProdDate(){return prodDate;}
    public String getShipDateP(){return shipDateP;}
    public String getShipDateA(){return shipDateA;}
    public String getSiteDateP(){return siteDateP;}
    public String getSiteDateA(){return siteDateA;}
    public String getRemark(){return remark;}

    public String getDescription(){return description;}
}
