package bean;

public class beanListItem {
    private String Text = "";
    private String Value = "";

    public beanListItem(){}

    public void setText(String text){this.Text = text;}
    public void setValue(String value){this.Value = value;}

    public String getText(){return this.Text;}
    public String getValue(){return this.Value;}
}
