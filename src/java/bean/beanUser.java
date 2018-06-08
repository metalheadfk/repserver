package bean;

public class beanUser {
    private String empCode = "";
    private String empName = "";
    private String empDiv = "";
    private String empDep = "";
    private String empSec = "";
    private String empUser = "";
    private String empPass = "";
    private String empGrant = "";
    private String empGrantName = "";

    private String menuID = "";
    private String menuName = "";

    public beanUser(){}

    public void setEmpCode(String empCode){this.empCode = empCode;}
    public void setEmpName(String empName){this.empName = empName;}
    public void setEmpDiv(String empDiv){this.empDiv = empDiv;}
    public void setEmpDep(String empDep){this.empDep = empDep;}
    public void setEmpSec(String empSec){this.empSec = empSec;}
    public void setEmpUser(String empUser){this.empUser = empUser;}
    public void setEmpPass(String empPass){this.empPass = empPass;}
    public void setEmpGrant(String empGrant){this.empGrant = empGrant;}
    public void setEmpGrantName(String empGrantName){this.empGrantName = empGrantName;}

    public void setMenuID(String menuID){this.menuID = menuID;}
    public void setMenuName(String menuName){this.menuName = menuName;}

    public String getEmpCode(){return empCode;}
    public String getEmpName(){return empName;}
    public String getEmpDiv(){return empDiv;}
    public String getEmpDep(){return empDep;}
    public String getEmpSec(){return empSec;}
    public String getEmpUser(){return empUser;}
    public String getEmpPass(){return empPass;}
    public String getEmpGrant(){return empGrant;}
    public String getEmpGrantName(){return empGrantName;}

    public String getMenuID(){return menuID;}
    public String getMenuName(){return menuName;}
}
