package bean;

public class beanMenu {
    private String menuCode = "";
    private String menuHref = "";
    private String menuName = "";

    public beanMenu(){}

    public void setMenuCode(String menuCode){this.menuCode = menuCode;}
    public void setMenuHref(String menuHref){this.menuHref = menuHref;}
    public void setMenuName(String menuName){this.menuName = menuName;}

    public String getMenuCode(){return menuCode;}
    public String getMenuHref(){return menuHref;}
    public String getMenuName(){return menuName;}
}
