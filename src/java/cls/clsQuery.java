package cls;

import bean.*;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import sun.net.smtp.SmtpClient;

public class clsQuery {

//<editor-fold defaultstate="collapsed" desc="Login">
    public String[] chkLogin(String login_name, String login_pwd) {
        try {
            String sql = "select trim(emp_code) as emp_code, trim(user_id) as user_id, trim(user_pwd) as user_pwd, "
                    + " trim(emp_name) as emp_name, trim(email) as email, trim(emp_phone) as emp_phone, "
                    + " trim(emp_sex) as emp_sex, trim(emp_age) as emp_age, trim(division) as division, "
                    + " trim(department) as department, trim(section) as section, trim(emp_team) as emp_team, "
                    + " trim(emp_position) as emp_position, trim(position_name) as position_name, "
                    + " emp_flag, emp_grant, boss_code ,level "
                    + " FROM stxemphr   "
                    + " WHERE user_id = '" + login_name + "' and user_pwd = '" + login_pwd + "' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            String[] arr_login = new String[18];
            while (rs.next()) {
                arr_login[0] = clsManage.chkNull(rs.getString("emp_code"));
                arr_login[1] = clsManage.chkNull(rs.getString("user_id"));
                arr_login[2] = clsManage.chkNull(rs.getString("user_pwd"));
                arr_login[3] = clsManage.chkNull(rs.getString("emp_name"));
                arr_login[4] = clsManage.chkNull(rs.getString("email"));
                arr_login[5] = clsManage.chkNull(rs.getString("emp_phone"));
                arr_login[6] = clsManage.chkNull(rs.getString("emp_sex"));
                arr_login[7] = clsManage.chkNull(rs.getString("emp_age"));
                arr_login[8] = clsManage.chkNull(rs.getString("division"));
                arr_login[9] = clsManage.chkNull(rs.getString("department"));
                arr_login[10] = clsManage.chkNull(rs.getString("section"));
                arr_login[11] = clsManage.chkNull(rs.getString("emp_team"));
                arr_login[12] = clsManage.chkNull(rs.getString("emp_position"));
                arr_login[13] = clsManage.chkNull(rs.getString("position_name"));
                arr_login[14] = clsManage.chkNull(rs.getString("emp_flag"));
                arr_login[15] = clsManage.chkNull(rs.getString("emp_grant"));
                arr_login[16] = clsManage.chkNull(rs.getString("boss_code"));
                arr_login[17] = clsManage.chkNull(rs.getString("level"));
            }
            cConnect.Close();
            return arr_login;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="User">
    public List Get_UserCount(String searchCode, String searchName, String searchGrant, String searchDiv, String searchDep, String searchSec, String sCode, String sGrant) {
        try {
            String where = "";

            if (sGrant.equals("SA")) {
                where = "1=1";

                if (!searchCode.equals("")) {
                    where = where + " AND emp_code LIKE '%" + searchCode + "%' ";
                }
                if (!searchName.equals("")) {
                    where = where + " AND emp_name LIKE '%" + searchName + "%' ";
                }
                if (!searchGrant.equals("")) {
                    where = where + " AND emp_grant = '" + searchGrant + "' ";
                }
                if (!searchDiv.equals("")) {
                    where = where + " AND division = '" + searchDiv + "' ";
                }
                if (!searchDep.equals("")) {
                    where = where + " AND department = '" + searchDep + "' ";
                }
                if (!searchSec.equals("")) {
                    where = where + " AND section = '" + searchSec + "' ";
                }
            } else {
                where = "emp_code = '" + sCode + "'";
            }

            String sql = "SELECT COUNT(emp_code) AS countRows "
                    + " FROM stxemphr "
                    + " WHERE " + where;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("countRows")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_User(String searchCode, String searchName, String searchGrant, String searchDiv, String searchDep, String searchSec, String sCode, String sGrant, String sortField, String sortDescAsc, String startRow, String limit) {
        try {

            String where = "";

            if (sGrant.equals("SA")) {
                where = "1=1";

                if (!searchCode.equals("")) {
                    where = where + " AND emp_code LIKE '%" + searchCode + "%' ";
                }
                if (!searchName.equals("")) {
                    where = where + " AND emp_name LIKE '%" + searchName + "%' ";
                }
                if (!searchGrant.equals("")) {
                    where = where + " AND emp_grant = '" + searchGrant + "' ";
                }
                if (!searchDiv.equals("")) {
                    where = where + " AND division = '" + searchDiv + "' ";
                }
                if (!searchDep.equals("")) {
                    where = where + " AND department = '" + searchDep + "' ";
                }
                if (!searchSec.equals("")) {
                    where = where + " AND section = '" + searchSec + "' ";
                }
            } else {
                where = "emp_code = '" + sCode + "'";
            }

            String sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
                    + " TRIM(emp_code) AS empCode, TRIM(emp_name) AS empName, "
                    + " TRIM(user_id) AS empUser, TRIM(user_pwd) AS empPass, "
                    + " TRIM(division) AS empDiv, TRIM(department) AS empDep,  "
                    + " TRIM(section) AS empSec, TRIM(emp_grant) AS empGrant "
                    + " FROM stxemphr "
                    + " WHERE " + where
                    + " ORDER BY " + sortField + " " + sortDescAsc;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanUser bUser = new beanUser();
                bUser.setEmpCode(clsManage.chkNull(rs.getString("empCode")));
                bUser.setEmpName(clsManage.chkNull(rs.getString("empName")));
                bUser.setEmpDiv(clsManage.chkNull(rs.getString("empDiv")));
                bUser.setEmpDep(clsManage.chkNull(rs.getString("empDep")));
                bUser.setEmpSec(clsManage.chkNull(rs.getString("empSec")));
                bUser.setEmpUser(clsManage.chkNull(rs.getString("empUser")));
                bUser.setEmpPass(clsManage.chkNull(rs.getString("empPass")));
                bUser.setEmpGrant(clsManage.getAuthenName(clsManage.chkNull(rs.getString("empGrant"))));
                arrList.add(bUser);
                bUser = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_UserViewCount(String empCode, String sCode, String sGrant) {
        try {
            String sql = "SELECT COUNT(stxemphr_code) AS countRows "
                    + " FROM stjgrmnv "
                    + " WHERE stxemphr_code = '" + empCode + "'";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("countRows")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_UserView(String empCode, String sortField, String sortDescAsc, String startRow, String limit) {
        try {
            String sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
                    + " TRIM(e.emp_code) AS empCode, TRIM(e.emp_name) AS empName, "
                    + " TRIM(e.division) AS empDiv, TRIM(e.department) AS empDep,  "
                    + " TRIM(e.section) AS empSec, TRIM(e.emp_grant) AS empGrant "
                    + " FROM stjgrmnv AS v "
                    + " LEFT JOIN stxemphr AS e ON v.stxemphr_view = e.emp_code "
                    + " WHERE v.stxemphr_code = '" + empCode + "'"
                    + " ORDER BY " + sortField + " " + sortDescAsc;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanUser bUser = new beanUser();
                bUser.setEmpCode(clsManage.chkNull(rs.getString("empCode")));
                bUser.setEmpName(clsManage.chkNull(rs.getString("empName")));
                bUser.setEmpDiv(clsManage.chkNull(rs.getString("empDiv")));
                bUser.setEmpDep(clsManage.chkNull(rs.getString("empDep")));
                bUser.setEmpSec(clsManage.chkNull(rs.getString("empSec")));
                bUser.setEmpGrant(clsManage.getAuthenName(clsManage.chkNull(rs.getString("empGrant"))));
                arrList.add(bUser);
                bUser = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_UserMenuCount(String empCode, String sCode, String sGrant) {
        try {
            String sql = "SELECT COUNT(stxemphr_code) AS countRows "
                    + " FROM stjgrmnm "
                    + " WHERE stxemphr_code = '" + empCode + "'";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("countRows")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_UserMenu(String empCode, String sortField, String sortDescAsc, String startRow, String limit) {
        try {
            String sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
                    + " m.stxmenus_id AS menuID, TRIM(m.stxmenus_name) AS menuName "
                    + " FROM stjgrmnm AS g "
                    + " LEFT JOIN stxmenus AS m ON g.stxmenus_id = m.stxmenus_id "
                    + " WHERE g.stxemphr_code = '" + empCode + "'"
                    + " ORDER BY " + sortField + " " + sortDescAsc;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanUser bUser = new beanUser();
                bUser.setMenuID(clsManage.chkNull(rs.getString("menuID")));
                bUser.setMenuName(clsManage.chkNull(rs.getString("menuName")));
                arrList.add(bUser);
                bUser = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int Set_UserAdd(String empCode, String empName, String empUser, String empPass, String empDiv, String empDep, String empSec, String empGrant, String manageBy) {
        try {

            String dupCode = "";
            String sqlDup = "SELECT TRIM(emp_code) AS empCode FROM stxemphr WHERE emp_code = '" + empCode + "' ";
            clsConnect cConnDup = new clsConnect();
            ResultSet rs = cConnDup.getResultSet(sqlDup);
            while (rs.next()) {
                dupCode = rs.getString("empCode");
            }

            if (dupCode.equals("")) {
                String sql = "INSERT INTO stxemphr (emp_code, emp_name, user_id, user_pwd, division, department, section, emp_grant) "
                        + "VALUES ('" + empCode + "','" + empName + "','" + empUser + "','" + empPass + "MMG','" + empDiv + "','" + empDep + "','" + empSec + "','" + empGrant + "')";
                clsConnect cConnect = new clsConnect();
                int res = cConnect.ExecuteQuery(sql);
                cConnect.Close();

                //Log
                setLog(manageBy, "Insert", "METPlan", "Insert stxemphr: " + empCode, "Manage User:Set_UserAdd", res);
                //End Log

                if (res == 1) {
                    //Grant View
                    if (empGrant.equals("CO")) {
                        Set_GrantViewAdd(empCode, empCode, manageBy);
                    }

                    //Grant Menu
                    Set_GrantMenuAdd(empCode, empGrant, manageBy);
                }

                return res;
            } else {
                setLog(manageBy, "Insert", "METPlan", "Insert stxemphr: " + empCode + " Dupplicate", "Manage User:Set_UserAdd", -1);
                return -1;
            }

        } catch (Exception ex) {
            return -1;
        }
    }

    public int Set_GrantViewAdd(String empCode, String empView, String manageBy) {
        try {
            String sql = "INSERT INTO stjgrmnv (stxemphr_code, stxemphr_view) "
                    + "VALUES ('" + empCode + "','" + empView + "')";
            clsConnect cConnect = new clsConnect();
            int res = cConnect.ExecuteQuery(sql);
            cConnect.Close();

            //Log
            setLog(manageBy, "Insert", "METPlan", "Insert stjgrmnv: " + empCode, "Manage User:Set_GrantViewAdd", res);
            //End Log

            return res;
        } catch (Exception ex) {
            return -1;
        }
    }

    public int Set_GrantMenuAdd(String empCode, String empGrant, String manageBy) {
        try {
            int res = -1;
            String sqlMenu = "SELECT * FROM stxmenus WHERE stxmenus_grant = '" + empGrant + "' ";
            clsConnect cConnMenu = new clsConnect();
            ResultSet rs = cConnMenu.getResultSet(sqlMenu);
            while (rs.next()) {
                int menuID = rs.getInt("stxmenus_id");

                String sql = "INSERT INTO stjgrmnm (stxemphr_code, stxmenus_id) "
                        + "VALUES ('" + empCode + "'," + menuID + ")";
                clsConnect cConnect = new clsConnect();
                res = cConnect.ExecuteQuery(sql);
                cConnect.Close();

                //Log
                setLog(manageBy, "Insert", "METPlan", "Insert stjgrmnm: " + empCode + " menu:" + menuID, "Manage User:Set_GrantMenuAdd", res);
                //End Log

            }

            return res;
        } catch (Exception ex) {
            return -1;
        }
    }

    public int Set_UserEdit(String empCode, String empName, String empUser, String empPass, String empDiv, String empDep, String empSec, String empGrant, String manageBy) {
        try {
            String sql = "UPDATE stxemphr SET emp_name = '" + empName + "', user_id = '" + empUser + "', user_pwd = '" + empPass + "MMG', division = '" + empDiv + "', department = '" + empDep + "', section = '" + empSec + "', emp_grant = '" + empGrant + "' "
                    + "WHERE emp_code = '" + empCode + "' ";
            clsConnect cConnect = new clsConnect();
            int res = cConnect.ExecuteQuery(sql);
            cConnect.Close();

            //Log
            setLog(manageBy, "Update", "METPlan", "Update stxemphr: " + empCode, "Manager User:Set_UserEdit", res);
            //End Log

            return res;
        } catch (Exception ex) {
            return -1;
        }
    }

    public int Set_ViewAdd(String empCode, String empCodeView, String manageBy) {
        try {
            String dupCode = "";
            String sqlDup = "SELECT TRIM(stxemphr_code) AS empCode FROM stjgrmnv WHERE stxemphr_code = '" + empCode + "' AND stxemphr_view = '" + empCodeView + "' ";
            clsConnect cConnDup = new clsConnect();
            ResultSet rs = cConnDup.getResultSet(sqlDup);
            while (rs.next()) {
                dupCode = rs.getString("empCode");
            }

            if (dupCode.equals("")) {
                String sql = "INSERT INTO stjgrmnv (stxemphr_code, stxemphr_view) VALUES ('" + empCode + "','" + empCodeView + "')";
                clsConnect cConnect = new clsConnect();
                int res = cConnect.ExecuteQuery(sql);
                cConnect.Close();

                //Log
                setLog(manageBy, "Insert", "METPlan", "Insert stjgrmnv: " + empCode, "Manage User:Set_ViewAdd", res);
                //End Log

                return res;
            } else {
                setLog(manageBy, "Insert", "METPlan", "Insert stjgrmnv: " + empCode + " Dupplicate", "Manage User:Set_ViewAdd", -1);
                return -9;
            }

        } catch (Exception ex) {
            return -1;
        }
    }

    public int Set_ViewDel(String empCode, String empCodeView, String manageBy) {
        try {
            String sql = "DELETE FROM stjgrmnv WHERE stxemphr_code = '" + empCode + "' AND stxemphr_view = '" + empCodeView + "' ";
            clsConnect cConnect = new clsConnect();
            int res = cConnect.ExecuteQuery(sql);
            cConnect.Close();

            //Log
            setLog(manageBy, "Delete", "METPlan", "Delete stjgrmnv: " + empCode + " : " + empCodeView, "Manage Template:Set_ViewDel", res);
            //End Log

            return res;
        } catch (Exception ex) {
            return -1;
        }
    }

    public int Set_MenuAdd(String empCode, String menuID, String manageBy) {
        try {
            String dupCode = "";
            String sqlDup = "SELECT TRIM(stxemphr_code) AS empCode FROM stjgrmnm WHERE stxemphr_code = '" + empCode + "' AND stxmenus_id = '" + menuID + "' ";
            clsConnect cConnDup = new clsConnect();
            ResultSet rs = cConnDup.getResultSet(sqlDup);
            while (rs.next()) {
                dupCode = rs.getString("empCode");
            }

            if (dupCode.equals("")) {
                String sql = "INSERT INTO stjgrmnm (stxemphr_code, stxmenus_id) VALUES ('" + empCode + "','" + menuID + "')";
                clsConnect cConnect = new clsConnect();
                int res = cConnect.ExecuteQuery(sql);
                cConnect.Close();

                //Log
                setLog(manageBy, "Insert", "METPlan", "Insert stjgrmnv: " + empCode + ":" + menuID, "Manage User:Set_MenuAdd", res);
                //End Log

                return res;
            } else {
                setLog(manageBy, "Insert", "METPlan", "Insert stjgrmnv: " + empCode + ":" + menuID + " Dupplicate", "Manage User:Set_MenuAdd", -1);
                return -9;
            }

        } catch (Exception ex) {
            return -1;
        }
    }

    public int Set_MenuDel(String empCode, String menuID, String manageBy) {
        try {
            String sql = "DELETE FROM stjgrmnm WHERE stxemphr_code = '" + empCode + "' AND stxmenus_id = '" + menuID + "' ";
            clsConnect cConnect = new clsConnect();
            int res = cConnect.ExecuteQuery(sql);
            cConnect.Close();

            //Log
            setLog(manageBy, "Delete", "METPlan", "Delete stjgrmnm: " + empCode + " : " + menuID, "Manage Template:Set_MenuDel", res);
            //End Log

            return res;
        } catch (Exception ex) {
            return -1;
        }
    }

    public List getUserDetail(String empCode) {
        try {
            String sql = "SELECT TRIM(emp_code) AS empCode, TRIM(emp_name) AS empName, "
                    + " TRIM(user_id) AS empUser, TRIM(user_pwd) AS empPass, "
                    + " TRIM(division) AS empDiv, TRIM(department) AS empDep,  "
                    + " TRIM(section) AS empSec, TRIM(emp_grant) AS empGrant "
                    + " FROM stxemphr "
                    + " WHERE emp_code = '" + empCode + "' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beanUser bUser = new beanUser();
                bUser.setEmpCode(clsManage.chkNull(rs.getString("empCode")));
                bUser.setEmpName(clsManage.chkNull(rs.getString("empName")));
                bUser.setEmpDiv(clsManage.chkNull(rs.getString("empDiv")));
                bUser.setEmpDep(clsManage.chkNull(rs.getString("empDep")));
                bUser.setEmpSec(clsManage.chkNull(rs.getString("empSec")));
                bUser.setEmpUser(clsManage.chkNull(rs.getString("empUser")));
                String pass = clsManage.chkNull(rs.getString("empPass"));
                if (pass.equals("")) {
                    bUser.setEmpPass("");
                } else {
                    String newPass = "";
                    newPass = pass.replace("MMG", "");
                    bUser.setEmpPass(newPass);
                }
                bUser.setEmpGrant(clsManage.chkNull(rs.getString("empGrant")));
                bUser.setEmpGrantName(clsManage.getAuthenName(clsManage.chkNull(rs.getString("empGrant"))));
                arrList.add(bUser);
                bUser = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Menu">
    public List GET_GrantMenu_Menu(String empCode) {
        try {
            String sql = "SELECT TRIM(m.stxmenus_code) AS stxmenus_code, TRIM(m.stxmenus_href) AS stxmenus_href, TRIM(m.stxmenus_name) AS stxmenus_name "
                    + " FROM stjgrmnm AS g "
                    + " LEFT JOIN stxmenus AS m ON g.stxmenus_id = m.stxmenus_id "
                    + " WHERE g.stxemphr_code = '" + empCode + "' "
                    + " ORDER BY m.stxmenus_name";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanMenu bMenu = new beanMenu();
                bMenu.setMenuCode(rs.getString("stxmenus_code"));
                bMenu.setMenuHref(rs.getString("stxmenus_href"));
                bMenu.setMenuName(rs.getString("stxmenus_name"));
                arrList.add(bMenu);
                bMenu = null; //clear data
            }
            cConnect.Close();
            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="List Box">
    public List Get_Employee_ListBox(String searchGrant) {
        try {
            String where = "1=1";
            if (!searchGrant.equals("")) {
                where = where + " AND emp_grant = '" + searchGrant + "' ";
            }
            String sql = "SELECT trim(emp_code) AS emp_code, trim(emp_name) AS emp_name "
                    + " FROM stxemphr "
                    + " WHERE " + where
                    + " ORDER BY emp_name ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanListItem bListItem = new beanListItem();
                bListItem.setText(clsManage.chkNull(rs.getString("emp_name")));
                bListItem.setValue(clsManage.chkNull(rs.getString("emp_code")));
                arrList.add(bListItem);
                bListItem = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_Division_ListBox() {
        try {

            String sql = "SELECT src_char "
                    + " FROM stxinfor "
                    + " WHERE src_char IN ('1.NS','2.MA','3.MZ','4.BM','5.MP','8.OT','9.SP') "
                    + " GROUP BY src_char "
                    + " ORDER BY src_char";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanListItem bListItem = new beanListItem();
                bListItem.setText(clsManage.chkNull(rs.getString("src_char").trim()));
                bListItem.setValue(clsManage.chkNull(rs.getString("src_char").trim()));
                arrList.add(bListItem);
                bListItem = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_Department_ListBox() {
        try {

            String sql = "SELECT src_key "
                    + " FROM stxinfor "
                    + " WHERE src_char IN ('1.NS','2.MA','3.MZ','4.BM','5.MP','8.OT','9.SP') "
                    + " GROUP BY src_key "
                    + " ORDER BY src_key";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanListItem bListItem = new beanListItem();
                bListItem.setText(clsManage.chkNull(rs.getString("src_key")).trim());
                bListItem.setValue(clsManage.chkNull(rs.getString("src_key")).trim());
                arrList.add(bListItem);
                bListItem = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_Section_ListBox() {
        try {

            String sql = "SELECT src_key "
                    + " FROM stxinfor "
                    + " WHERE src_type = 'SCENTER' "
                    + " ORDER BY src_key";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanListItem bListItem = new beanListItem();
                bListItem.setText(clsManage.chkNull(rs.getString("src_key")).trim());
                bListItem.setValue(clsManage.chkNull(rs.getString("src_key")).trim());
                arrList.add(bListItem);
                bListItem = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_Menu_ListBox() {
        try {
            String sql = "select stxmenus_id, stxmenus_name from stxmenus order by stxmenus_name";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanListItem bListItem = new beanListItem();
                bListItem.setText(clsManage.chkNull(rs.getString("stxmenus_name")));
                bListItem.setValue(clsManage.chkNull(rs.getString("stxmenus_id")));
                arrList.add(bListItem);
                bListItem = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_section() {
        try {
            String sql = "select src_key ,src_desc from stxinfor where src_type='SECTION' order by src_key asc ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanListItem bListItem = new beanListItem();
                bListItem.setText(clsManage.chkNull(rs.getString("src_desc").trim()));
                bListItem.setValue(clsManage.chkNull(rs.getString("src_key").trim()));
                arrList.add(bListItem);
                bListItem = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_level_ListBox() {
        try {

            String sql = "SELECT src_key,src_desc "
                    + " FROM stxinfor "
                    + " WHERE src_type = 'LEVEL' "
                    + " ORDER BY src_num asc";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanListItem bListItem = new beanListItem();
                bListItem.setText(clsManage.chkNull(rs.getString("src_desc")).trim());
                bListItem.setValue(clsManage.chkNull(rs.getString("src_key")).trim());
                arrList.add(bListItem);
                bListItem = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Match Template">
    public List Get_ProjectCount_MatchTemplate(String searchFrom, String search, String searchSign, String searchDate, String searchPriority, String searchStatus, String manageBy) {
        try {
            String where = "1=1";
            if (!searchFrom.equals("")) {
                if (searchFrom.equals("Proj")) {
                    where = where + " AND (p.proj_code LIKE '%" + search + "%' OR p.proj_lname LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Cust")) {
                    where = where + " AND (p.cust_code LIKE '%" + search + "%' OR c.cust_name LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Sale")) {
                    where = where + " AND (p.sales_code LIKE '%" + search + "%' OR s.emp_name LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Coor")) {
                    where = where + " AND (p.stxemphr_coordinator LIKE '%" + search + "%' OR e.emp_name LIKE '%" + search + "%') ";
                }
            }
            if (searchStatus.equals("M")) { //M=Match Template
                where = where + " AND (p.stjmasth_status = 'N' OR p.stjmasth_status = 'O') AND p.stjmasth_task < p.stjmasth_unit";
            } else { //E=Edit Task
                where = where + " AND p.stjmasth_task >= 1 AND (p.stjmasth_status = 'N' OR p.stjmasth_status = 'O') ";
            }
            if (!searchPriority.equals("")) {
                where = where + " AND p.stjmasth_priority = '" + searchPriority + "' ";
            }
            if (!searchDate.equals("")) {
                String dateDMY = searchDate.replace("-", "/");
                where = where + " AND p.stjmasth_startplan " + searchSign + " '" + dateDMY + "' ";
            }
            String sql = "SELECT COUNT(p.proj_code) AS countRows "
                    + " FROM stjmasth p "
                    + " LEFT JOIN stxcustr c ON p.cust_code = c.cust_code "
                    + " LEFT JOIN stxemphr s ON p.sales_code = s.emp_code "
                    + " LEFT JOIN stxemphr e ON p.stxemphr_coordinator = e.emp_code"
                    + " WHERE " + where;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("countRows")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_Project_MatchTemplate(String searchFrom, String search, String searchSign, String searchDate, String searchPriority, String searchStatus, String sortField, String sortDescAsc, int startRow, String limit, String manageBy) {
        try {
            String where = "1=1";
            if (!searchFrom.equals("")) {
                if (searchFrom.equals("Proj")) {
                    where = where + " AND (p.proj_code LIKE '%" + search + "%' OR p.proj_lname LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Cust")) {
                    where = where + " AND (p.cust_code LIKE '%" + search + "%' OR c.cust_name LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Sale")) {
                    where = where + " AND (p.sales_code LIKE '%" + search + "%' OR s.emp_name LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Coor")) {
                    where = where + " AND (p.stxemphr_coordinator LIKE '%" + search + "%' OR e.emp_name LIKE '%" + search + "%') ";
                }
            }
            if (searchStatus.equals("M")) { //M=Match Template
                where = where + " AND (p.stjmasth_status = 'N' OR p.stjmasth_status = 'O') AND p.stjmasth_task < p.stjmasth_unit";
            } else { //E=Edit Task
                where = where + "  AND p.stjmasth_task >= 1 AND (p.stjmasth_status = 'N' OR p.stjmasth_status = 'O')";
            }
            if (!searchPriority.equals("")) {
                where = where + " AND p.stjmasth_priority = '" + searchPriority + "' ";
            }
            if (!searchDate.equals("")) {
                String dateDMY = searchDate.replace("-", "/");
                where = where + " AND p.stjmasth_startplan " + searchSign + " '" + dateDMY + "' ";
            }
            String sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
                    + " trim(p.proj_code) AS projCode, trim(p.proj_lname) AS projName, "
                    + " p.stjmasth_startplan AS startPlan, p.stjmasth_endplan AS endPlan, "
                    + " p.stjmasth_status AS status, p.stjmasth_priority AS priority, "
                    + " trim(c.cust_code) AS custCode, trim(c.cust_name) AS custName, "
                    + " trim(s.emp_code) AS saleCode, trim(s.emp_name) AS saleName, "
                    + " trim(e.emp_code) AS coorCode, trim(e.emp_name) AS coorName, "
                    + " p.stjmasth_unit AS qty, p.stjmasth_task AS qtyT "
                    + " FROM stjmasth p "
                    + " LEFT JOIN stjplanp l ON p.proj_code = l.proj_code "
                    + " LEFT JOIN stxcustr c ON p.cust_code = c.cust_code "
                    + " LEFT JOIN stxemphr s ON p.sales_code = s.emp_code "
                    + " LEFT JOIN stxemphr e ON p.stxemphr_coordinator = e.emp_code"
                    + " WHERE " + where
                    + " GROUP BY p.proj_code, p.proj_lname, p.stjmasth_startplan, p.stjmasth_endplan, "
                    + " p.stjmasth_status, p.stjmasth_priority, c.cust_code, c.cust_name, "
                    + " s.emp_code, s.emp_name, e.emp_code, e.emp_name, p.stjmasth_unit, p.stjmasth_task "
                    + " ORDER BY " + sortField + " " + sortDescAsc;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanProject bProject = new beanProject();
                bProject.setProjCode(rs.getString("projCode"));
                bProject.setProjName(clsManage.chkNull(rs.getString("projName")));
                bProject.setCustCode(clsManage.chkNull(rs.getString("custCode")));
                bProject.setCustName(clsManage.chkNull(rs.getString("custName")));
                bProject.setSaleCode(clsManage.chkNull(rs.getString("saleCode")));
                bProject.setSaleName(clsManage.chkNull(rs.getString("saleName")));
                bProject.setCoorCode(clsManage.chkNull(rs.getString("coorCode")));
                bProject.setCoorName(clsManage.chkNull(rs.getString("coorName")));
                bProject.setProjQty(rs.getString("qty"));
                bProject.setProjQtyT(rs.getString("qtyT"));

                String sPlan = clsManage.chkNull(rs.getString("startPlan"));
                if (sPlan.equals("")) {
                    bProject.setStartProject("");
                } else {
                    String tempStartPlan = rs.getString("startPlan").replace("/", "-");
                    bProject.setStartProject(clsManage.manageYear4DMY(tempStartPlan, -1));
                }

                String ePlan = clsManage.chkNull(rs.getString("endPlan"));
                if (ePlan.equals("")) {
                    bProject.setEndProject("");
                } else {
                    String tempEndPlan = rs.getString("endPlan").replace("/", "-");
                    bProject.setEndProject(clsManage.manageYear4DMY(tempEndPlan, -1));
                }

                bProject.setPriority(clsManage.convPriorityCode2Name(clsManage.chkNull(rs.getString("priority"))));
                bProject.setStatus(clsManage.convStatusCode2Name(clsManage.chkNull(rs.getString("status"))));
                arrList.add(bProject);
                bProject = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_ProjectDetail_MatchTemplate(String Proj) {
        try {
            String sql = "SELECT trim(p.proj_code) AS projCode, trim(p.proj_lname) AS projName, "
                    + " p.stjmasth_priority AS priority, p.stjmasth_status AS status, "
                    + " trim(c.cust_code) AS custCode, trim(c.cust_name) AS custName, "
                    + " trim(s.emp_code) AS saleCode, trim(s.emp_name) AS saleName, "
                    + " trim(e.emp_code) AS coorCode, trim(e.emp_name) AS coorName, "
                    + " p.stjmasth_startplan AS startPlan, p.stjmasth_endplan AS endPlan, "
                    + " p.stjmasth_unit AS qty, p.stjmasth_task AS qtyT "
                    + " FROM stjmasth p "
                    + " LEFT JOIN stjplanp l ON p.proj_code = l.proj_code "
                    + " LEFT JOIN stxcustr c ON p.cust_code = c.cust_code "
                    + " LEFT JOIN stxemphr s ON p.sales_code = s.emp_code "
                    + " LEFT JOIN stxemphr e ON p.stxemphr_coordinator = e.emp_code"
                    + " WHERE p.proj_code = '" + Proj + "' "
                    + " GROUP BY p.proj_code, p.proj_lname, "
                    + " p.stjmasth_priority, p.stjmasth_status, "
                    + " c.cust_code, c.cust_name, "
                    + " s.emp_code, s.emp_name, e.emp_code, e.emp_name, "
                    + " p.stjmasth_startplan, p.stjmasth_endplan, "
                    + " p.stjmasth_unit, p.stjmasth_task ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanProject bProject = new beanProject();
                bProject.setProjCode(rs.getString("projCode"));
                bProject.setProjName(clsManage.chkNull(rs.getString("projName")));
                bProject.setCustCode(clsManage.chkNull(rs.getString("custCode")));
                bProject.setCustName(clsManage.chkNull(rs.getString("custName")));
                bProject.setSaleCode(clsManage.chkNull(rs.getString("saleCode")));
                bProject.setSaleName(clsManage.chkNull(rs.getString("saleName")));
                bProject.setCoorCode(clsManage.chkNull(rs.getString("coorCode")));
                bProject.setCoorName(clsManage.chkNull(rs.getString("coorName")));
                bProject.setProjQty(rs.getString("qty"));
                bProject.setProjQtyT(rs.getString("qtyT"));

                String sPlan = clsManage.chkNull(rs.getString("startPlan"));
                if (sPlan.equals("")) {
                    bProject.setStartProject("");
                } else {
                    String tempStartPlan = rs.getString("startPlan").replace("/", "-");
                    bProject.setStartProject(clsManage.manageYear4DMY(tempStartPlan, -1));
                }

                String ePlan = clsManage.chkNull(rs.getString("endPlan"));
                if (ePlan.equals("")) {
                    bProject.setEndProject("");
                } else {
                    String tempEndPlan = rs.getString("endPlan").replace("/", "-");
                    bProject.setEndProject(clsManage.manageYear4DMY(tempEndPlan, -1));
                }

                bProject.setPriority(clsManage.chkNull(rs.getString("priority")));
                bProject.setPriorityName(clsManage.convPriorityCode2Name(clsManage.chkNull(rs.getString("priority"))));
                bProject.setStatus(clsManage.convStatusCode2Name(clsManage.chkNull(rs.getString("status"))));
                arrList.add(bProject);
                bProject = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_Product_MatchTemplate(String projectCode, String searchStatus) {
        try {
            String sql = "";
            if (searchStatus.equals("M")) { //Match
                sql = "SELECT trim(d.proj_code) AS projCode, trim(d.stjplanp_code) AS prodCode, "
                        + " TRIM(d.stjplanp_carno) AS stjplanp_carno, TRIM(d.stjplanp_desc) AS stjplanp_desc, "
                        + " d.stjplanp_startplan, d.stjplanp_endplan, d.stjplanp_startactual, "
                        + " d.stjplanp_endactual, d.stjplanp_type, "
                        + " t.stjtmplh_code, t.stjtmplh_name "
                        + " FROM stjplanp AS d "
                        + " LEFT JOIN stjtmplh AS t ON d.stjtmplh_code = t.stjtmplh_code "
                        + " WHERE d.proj_code = '" + projectCode + "' AND (d.stjtmplh_code = '' OR d.stjtmplh_code is null) "
                        + " ORDER BY d.stjplanp_code ";
            } else { //Edit
                sql = "SELECT trim(d.proj_code) AS projCode, trim(d.stjplanp_code) AS prodCode, "
                        + " TRIM(d.stjplanp_carno) AS stjplanp_carno, TRIM(d.stjplanp_desc) AS stjplanp_desc, "
                        + " d.stjplanp_startplan, d.stjplanp_endplan, d.stjplanp_startactual, "
                        + " d.stjplanp_endactual, d.stjplanp_type, "
                        + " t.stjtmplh_code, t.stjtmplh_name "
                        + " FROM stjplanp AS d "
                        + " LEFT JOIN stjtmplh AS t ON d.stjtmplh_code = t.stjtmplh_code "
                        + " WHERE d.proj_code = '" + projectCode + "' AND (d.stjtmplh_code <> '') "
                        + " ORDER BY d.stjplanp_code ";
            }

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanProduct bProduct = new beanProduct();
                bProduct.setProjCode(rs.getString("projCode"));
                bProduct.setProdCode(rs.getString("prodCode"));
                bProduct.setProdCarNo(clsManage.chkNull(rs.getString("stjplanp_carno")));
                bProduct.setProdDesc(clsManage.chkNull(rs.getString("stjplanp_desc")));
                bProduct.setProdType(clsManage.chkNull(rs.getString("stjplanp_type")));

                String sPlan = clsManage.chkNull(rs.getString("stjplanp_startplan"));
                if (sPlan.equals("")) {
                    bProduct.setStartTask("");
                } else {
                    String tempStartPlan = rs.getString("stjplanp_startplan").replace("/", "-");
                    bProduct.setStartTask(clsManage.manageYear4DMY(tempStartPlan, -1));
                }

                String ePlan = clsManage.chkNull(rs.getString("stjplanp_endplan"));
                if (ePlan.equals("")) {
                    bProduct.setEndTask("");
                } else {
                    String tempEndPlan = rs.getString("stjplanp_endplan").replace("/", "-");
                    bProduct.setEndTask(clsManage.manageYear4DMY(tempEndPlan, -1));
                }

                String sActual = clsManage.chkNull(rs.getString("stjplanp_startactual"));
                if (sActual.equals("")) {
                    bProduct.setStartTaskA("");
                } else {
                    String tempStartActual = rs.getString("stjplanp_startactual").replace("/", "-");
                    bProduct.setStartTaskA(clsManage.manageYear4DMY(tempStartActual, -1));
                }

                String eActual = clsManage.chkNull(rs.getString("stjplanp_endactual"));
                if (eActual.equals("")) {
                    bProduct.setEndTaskA("");
                } else {
                    String tempEndActual = rs.getString("stjplanp_endactual").replace("/", "-");
                    bProduct.setEndTaskA(clsManage.manageYear4DMY(tempEndActual, -1));
                }

                bProduct.setTemplateCode(clsManage.chkNull(rs.getString("stjtmplh_code")));
                bProduct.setTemplateName(clsManage.chkNull(rs.getString("stjtmplh_Name")));

                int maxProcess = 0;
                int maxTime = 0;
                maxProcess = Get_CountProcess("stjplanp_code", "stjplant", "stjplanp_code", rs.getString("prodCode"), "stjplant_flag");
                maxTime = Get_CountTime("stjplant_time", "stjplant", "stjplanp_code", rs.getString("prodCode"), "stjplant_flag");
                bProduct.setTemplateTime(String.valueOf(maxTime));
                bProduct.setTemplateProcess(String.valueOf(maxProcess));

                arrList.add(bProduct);
                bProduct = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_ProductDetail_MatchTemplate(String productCode) {
        try {
            String sql = "SELECT trim(d.proj_code) AS projCode, trim(d.stjplanp_code) AS prodCode, "
                    + " d.stjplanp_startplan, d.stjplanp_endplan, "
                    + " d.stjplanp_startactual, d.stjplanp_endactual, "
                    + " t.stjtmplh_code, t.stjtmplh_name, d.stjplanp_type, "
                    + " TRIM(d.stjplanp_carno) AS stjplanp_carno, TRIM(d.stjplanp_desc) AS stjplanp_desc "
                    + " FROM stjplanp d "
                    + " LEFT JOIN stjtmplh t on d.stjtmplh_code = t.stjtmplh_code "
                    + " WHERE d.stjplanp_code = '" + productCode + "' "
                    + " ORDER BY d.stjplanp_code ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanProduct bProduct = new beanProduct();
                bProduct.setProjCode(rs.getString("projCode"));
                bProduct.setProdCode(rs.getString("prodCode"));
                bProduct.setStartTask(clsManage.chkNull(rs.getString("stjplanp_startplan")));
                bProduct.setEndTask(clsManage.chkNull(rs.getString("stjplanp_endplan")));
                bProduct.setTemplateCode(clsManage.chkNull(rs.getString("stjtmplh_code")));
                bProduct.setTemplateName(clsManage.chkNull(rs.getString("stjtmplh_name")));
                bProduct.setProdCarNo(clsManage.chkNull(rs.getString("stjplanp_carno")));
                bProduct.setProdDesc(clsManage.chkNull(rs.getString("stjplanp_desc")));
                bProduct.setProdType(clsManage.chkNull(rs.getString("stjplanp_type")));

                String sPlan = clsManage.chkNull(rs.getString("stjplanp_startplan"));
                if (sPlan.equals("")) {
                    bProduct.setStartTask("");
                } else {
                    String tempStartPlan = rs.getString("stjplanp_startplan").replace("/", "-");
                    bProduct.setStartTask(clsManage.manageYear4DMY(tempStartPlan, -1));
                }

                String ePlan = clsManage.chkNull(rs.getString("stjplanp_endplan"));
                if (ePlan.equals("")) {
                    bProduct.setEndTask("");
                } else {
                    String tempEndPlan = rs.getString("stjplanp_endplan").replace("/", "-");
                    bProduct.setEndTask(clsManage.manageYear4DMY(tempEndPlan, -1));
                }

                String sActual = clsManage.chkNull(rs.getString("stjplanp_startactual"));
                if (sActual.equals("")) {
                    bProduct.setStartTask("");
                } else {
                    String tempStartActual = rs.getString("stjplanp_startactual").replace("/", "-");
                    bProduct.setStartTask(clsManage.manageYear4DMY(tempStartActual, -1));
                }

                String eActual = clsManage.chkNull(rs.getString("stjplanp_endactual"));
                if (eActual.equals("")) {
                    bProduct.setEndTask("");
                } else {
                    String tempEndActual = rs.getString("stjplanp_endactual").replace("/", "-");
                    bProduct.setEndTask(clsManage.manageYear4DMY(tempEndActual, -1));
                }

                arrList.add(bProduct);
                bProduct = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int Set_TemplateToProduct_MatchTemplate(String projCode, String prodCode, String tempCode, String manageBy, String startTask, String saturday, String sunday, String special) {
        try {
            clsConnect cConnect = new clsConnect();
            String types = tempCode.substring(0, 3); //หา Type

            String sqlIN = "";
            Date startProjD = new Date();

            //หาวันที่เริ่มโปรเจค เปลี่ยนเป็นให้ User กำหนดวันเริ่มโปรเจคเอง
//            String sqlGetStartProj = "SELECT stjmasth_startplan FROM stjmasth WHERE proj_code = '" + projCode + "' ";
//            ResultSet rsGetStartProj = cConnect.getResultSet(sqlGetStartProj);
//            while (rsGetStartProj.next()) {
//                startProjD = rsGetStartProj.getDate("stjmasth_startplan");
//            }
//            String startP = clsManage.ConvertDateToString(startProjD, "dd/MM/yyyy");
            String startProjS = startTask.replace("-", "/");
            startProjD = clsManage.ConvertStringToDate(startProjS, "dd/MM/yyyy");
            int res = 0;
            //วนลูปใส่ stjplant
            String sqlGetTask = "SELECT stjtmpld_name, stjtmpld_sort, stjtmpld_time, stjtmpld_checkpoint, "
                    + " stxemphr_responsible1, stxemphr_responsible2, stxemphr_responsible3, stxemphr_responsible4, stxemphr_responsible5"
                    + " FROM stjtmpld "
                    + " WHERE stjtmplh_code = '" + tempCode + "' and stjtmpld_flag = 'Y' "
                    + " ORDER BY stjtmpld_sort";
            ResultSet rsGetTask = cConnect.getResultSet(sqlGetTask);
            while (rsGetTask.next()) {
                String name = rsGetTask.getString("stjtmpld_name");
                String sort = rsGetTask.getString("stjtmpld_sort");
                String time = rsGetTask.getString("stjtmpld_time");
                String checkpoint = rsGetTask.getString("stjtmpld_checkpoint");
                String response1 = rsGetTask.getString("stxemphr_responsible1");
                String response2 = rsGetTask.getString("stxemphr_responsible2");
                String response3 = rsGetTask.getString("stxemphr_responsible3");
                String response4 = rsGetTask.getString("stxemphr_responsible4");
                String response5 = rsGetTask.getString("stxemphr_responsible5");

                String varStartEnd = Get_StartEndTask(startProjD, time, saturday, sunday, special);
                String arr[];
                if (varStartEnd.equals("")) {
                    return -1;
                } else {
                    arr = varStartEnd.split(":"); //Start End Next
                }
                //------------

                //Insert into DB
                int max = getMaxID("stjplant_id", "stjplant");
                sqlIN = "INSERT INTO stjplant (stjplant_id, stjplanp_code, stjplant_name, stjplant_sort, stjplant_time, stjplant_checkpoint, "
                        + "stxemphr_responsible1, stxemphr_responsible2, stxemphr_responsible3, stxemphr_responsible4, stxemphr_responsible5, "
                        + "stjplant_status, stjplant_flag, stxemphr_createid, stjplant_createdate, stjplant_startplan, stjplant_endplan) VALUES (" + max + ", "
                        + "'" + prodCode + "', '" + name + "', " + sort + ", " + time + ", "
                        + "'" + checkpoint + "', '" + response1 + "', '" + response2 + "', '" + response3 + "', '" + response4 + "', '" + response5 + "', 'N', 'Y', "
                        + "'" + manageBy + "', CURRENT, '" + arr[0] + "', '" + arr[1] + "' );";
                clsConnect cConnectA = new clsConnect();
                res = cConnectA.ExecuteQuery(sqlIN);
                startProjD = clsManage.ConvertStringToDate(arr[2], "dd/MM/yyyy");
            }

            String endP = clsManage.ConvertDateToString(startProjD, "dd/MM/yyyy");

            //Log
            setLog(manageBy, "Insert", "METPlan", "Insert stjplant: " + prodCode, "Match Template:Set_TemplateToProduct_MatchTemplate", res);
            //End Log
            if (res < 0) {
                return res;
            }

            //Update ลง Project
            String sqlA = "UPDATE stjmasth set stjmasth_task=stjmasth_task+1 where proj_code = '" + projCode + "' ";
            clsConnect cConnectProj = new clsConnect();
            int resA = cConnectProj.ExecuteQuery(sqlA);

            //Log
            setLog(manageBy, "Update", "METPlan", "Update stjmasth: " + projCode, "Match Template:Set_TemplateToProduct_MatchTemplate", resA);
            //End Log
            //----------

            //Update ลง Product
            String startPlan = "";
            String endPlan = "";
            String sqlGetStartEnd = "SELECT MIN(stjplant_startplan) AS minP, MAX(stjplant_endplan) AS maxP "
                    + " FROM stjplant "
                    + " WHERE stjplanp_code = '" + prodCode + "' AND stjplant_flag = 'Y'";
            clsConnect connGetStartEnd = new clsConnect();
            ResultSet rsGetStartEnd = connGetStartEnd.getResultSet(sqlGetStartEnd);
            while (rsGetStartEnd.next()) {
                String tempStartPlan = rsGetStartEnd.getString("minP").replace("/", "-");
                tempStartPlan = clsManage.manageYear4DMY(tempStartPlan, -1);
                startPlan = tempStartPlan.replace("-", "/");

                String tempEndPlan = rsGetStartEnd.getString("maxP").replace("/", "-");
                tempEndPlan = clsManage.manageYear4DMY(tempEndPlan, -1);
                endPlan = tempEndPlan.replace("-", "/");
            }

            String sqlUP = "UPDATE stjplanp SET stjtmplh_code = '" + tempCode + "', stjplanp_type = '" + types + "', "
                    + " stjplanp_startPlan = '" + startPlan + "', stjplanp_endplan = '" + endPlan + "',"
                    + " stjplanp_saturday = '" + saturday + "', stjplanp_sunday = '" + sunday + "', stjplanp_special = '" + special + "' "
                    + " WHERE stjplanp_code = '" + prodCode + "' and proj_code = '" + projCode + "' ";
            clsConnect cConnectProd = new clsConnect();
            int resUP = cConnectProd.ExecuteQuery(sqlUP);

            //Log
            setLog(manageBy, "Update", "METPlan", "Update stjplanp: " + prodCode, "Match Template:Set_TemplateToProduct_MatchTemplate", resUP);
            //End Log
            //----------

            return resUP;
        } catch (Exception ex) {
            return -1;
        }
    }

    public String Get_StartEndTask(Date startProjD, String time, String saturday, String sunday, String special) {
        try {
            int countTime = Integer.parseInt(time) - 1;
            int plusCountTime = 0;
            String endProjS;
            String nextProjS;
            String startProjS;

            startProjS = clsManage.ConvertDateToString(startProjD, "dd/MM/yyyy");

            //ตรวจสอบว่าวันที่เริ่มเป็นวันหยุด หรือวันหยุดพิเศษหรือไม่ //1 = Sunday, 2 = Monday, 3 = Tuesday, 4 = Wednesday, 4 = Thursday, 6 = Friday, 7 = Saturday
            int chkStartHoliday = Chk_Holiday(startProjS, saturday, sunday, special);
            if (chkStartHoliday >= 1) {
                startProjS = clsManage.AddDays(startProjD, chkStartHoliday, "dd/MM/yyyy");
                startProjD = clsManage.ConvertStringToDate(startProjS, "dd/MM/yyyy");
            }
            //------------

            //หลังจากได้วันเริ่มแล้ว ก็หาวันสิ้นสุด
            if (countTime >= 1) {
                endProjS = clsManage.AddDays(startProjD, countTime, "dd/MM/yyyy");
                nextProjS = clsManage.AddDays(startProjD, Integer.parseInt(time), "dd/MM/yyyy");
            } else { //0
                endProjS = startProjS;
                nextProjS = clsManage.AddDays(startProjD, Integer.parseInt(time), "dd/MM/yyyy");
            }
            //------------

            if (saturday.equals("Y") || sunday.equals("Y")) {
                //นำวันเริ่ม และ วันสิ้นสุดมาหาวันหยุดเสาร์อาทิตย์
                int chkHoliday = clsManage.chkHoliday(startProjS, endProjS, saturday, sunday);
                if (chkHoliday >= 1) { //ให้เลื่อนวันหยุดไปตามจำนวน chkHoliday
                    countTime = countTime + chkHoliday;
                    endProjS = clsManage.AddDays(startProjD, countTime, "dd/MM/yyyy");

                    plusCountTime = countTime + 1;
                    nextProjS = clsManage.AddDays(startProjD, plusCountTime, "dd/MM/yyyy");
                }
                //------------
            }

            if (special.equals("Y")) {
                //นำวันเริ่ม และ วันสิ้นสุดมาหาวันหยุดพิเศษ
                int chkHolidayDB = Chk_HolidayDB(startProjS, endProjS);
                if (chkHolidayDB >= 1) {
                    countTime = countTime + chkHolidayDB;
                    endProjS = clsManage.AddDays(startProjD, countTime, "dd/MM/yyyy");

                    plusCountTime = countTime + 1;
                    nextProjS = clsManage.AddDays(startProjD, plusCountTime, "dd/MM/yyyy");
                }
            }

            //ตรวจสอบว่าวันที่สิ้นสุดเป็นวันหยุด หรือวันหยุดพิเศษหรือไม่ //1 = Sunday, 2 = Monday, 3 = Tuesday, 4 = Wednesday, 4 = Thursday, 6 = Friday, 7 = Saturday
            int chkEndHoliday = Chk_Holiday(endProjS, saturday, sunday, special);
            if (chkEndHoliday >= 1) {
                countTime = countTime + chkEndHoliday;
                endProjS = clsManage.AddDays(startProjD, countTime, "dd/MM/yyyy");

                plusCountTime = countTime + 1;
                nextProjS = clsManage.AddDays(startProjD, plusCountTime, "dd/MM/yyyy");
            }
            //------------
            return startProjS + ":" + endProjS + ":" + nextProjS;
        } catch (Exception ex) {
            return "";
        }

    }

    public int Chk_HolidayDB(String startDate, String endDate) { //dd/MM/yyyy
        try {
            String subStringStartDate[] = startDate.split("/");
            String dateS = subStringStartDate[0];
            String monthS = subStringStartDate[1];
            String yearS = subStringStartDate[2];

            String subStringEndDate[] = endDate.split("/");
            String dateE = subStringEndDate[0];
            String monthE = subStringEndDate[1];
            String yearE = subStringEndDate[2];

            String strStart = yearS + "" + monthS + "" + dateS;
            String strEnd = yearE + "" + monthE + "" + dateE;

            String strPointDay = "";
            String mergeDate = "";
            Date nextDateD;
            String nextDateS;
            int countHoliday = 0;
            while (Integer.parseInt(strStart) <= Integer.parseInt(strEnd)) {
                strPointDay = strStart;
                String splitY = strPointDay.substring(0, 4);
                String splitM = strPointDay.substring(4, 6);
                String splitD = strPointDay.substring(6, 8);

                mergeDate = splitD + "/" + splitM + "/" + splitY; //dd/MM/YYYY

                String sql = "SELECT * FROM stxholid WHERE public_holiday = '" + mergeDate + "'";
                clsConnect cConnect = new clsConnect();
                ResultSet rs = cConnect.getResultSet(sql);
                while (rs.next()) {
                    countHoliday = countHoliday + 1;
                }

                nextDateD = clsManage.ConvertStringToDate(mergeDate, "dd/MM/yyyy");
                nextDateS = clsManage.AddDays(nextDateD, 1, "dd/MM/yyyy");
                String subStringNextDateS[] = nextDateS.split("/");
                dateS = subStringNextDateS[0];
                monthS = subStringNextDateS[1];
                yearS = subStringNextDateS[2];
                strStart = yearS + "" + monthS + "" + dateS;
            }

            return countHoliday;
        } catch (Exception ex) {
            return 0;
        }

    }

    public int Chk_Holiday(String startDate, String saturday, String sunday, String special) { //dd/MM/yyyy
        try {
            Date nextDateD;
            String nextDateS;
            int countHoliday = 0;

            nextDateS = startDate;

            for (int i = 1; i < 10; i++) { //เผื่อเอาไว้ 10 วัน ต้องไม่ชนวันหยุด ชัวร์
                if (saturday.equals("Y")) {
                    if (clsManage.chkDay(nextDateS) == 7) { //1=Sunday 7=Saturday
                        countHoliday = countHoliday + 1;
                        nextDateD = clsManage.ConvertStringToDate(nextDateS, "dd/MM/yyyy");
                        nextDateS = clsManage.AddDays(nextDateD, 1, "dd/MM/yyyy");
                    }
                }

                if (sunday.equals("Y")) {
                    if (clsManage.chkDay(nextDateS) == 1) { //1=Sunday 7=Saturday
                        countHoliday = countHoliday + 1;
                        nextDateD = clsManage.ConvertStringToDate(nextDateS, "dd/MM/yyyy");
                        nextDateS = clsManage.AddDays(nextDateD, 1, "dd/MM/yyyy");
                    }
                }

                if (special.equals("Y")) {
                    String status = "";
                    String sql = "SELECT fisyear FROM stxholid WHERE public_holiday = '" + nextDateS + "'";
                    clsConnect cConnect = new clsConnect();
                    ResultSet rs = cConnect.getResultSet(sql);
                    while (rs.next()) {
                        status = rs.getString("fisyear");
                    }
                    if (status.equals("")) {
                        return countHoliday;
                    } else {
                        countHoliday = countHoliday + 1;
                        nextDateD = clsManage.ConvertStringToDate(nextDateS, "dd/MM/yyyy");
                        nextDateS = clsManage.AddDays(nextDateD, 1, "dd/MM/yyyy");
                    }
                }
            }
            return countHoliday;
        } catch (Exception ex) {
            return 0;
        }
    }

    public int Set_TaskAdd_MatchTemplate(String prodCode, String taskName, int taskTime, int taskSort, String taskPoint, String respon1, String respon2, String respon3, String respon4, String respon5, String manageBy) {
        try {
            Date startProjD = new Date();
            String startProjS = "";
            String arr[] = null;
            int res = -1;

            String saturday = "";
            String sunday = "";
            String special = "";
            //หาว่าผลิตภัณฑ์นี้กำหนดวันหยุดไว้อย่างไร
            String sqlHoliday = "SELECT * FROM stjplanp WHERE stjplanp_code = '" + prodCode + "'";
            clsConnect connHoliday = new clsConnect();
            ResultSet rsGetHoliday = connHoliday.getResultSet(sqlHoliday);
            while (rsGetHoliday.next()) {
                saturday = rsGetHoliday.getString("stjplanp_saturday");
                sunday = rsGetHoliday.getString("stjplanp_sunday");
                special = rsGetHoliday.getString("stjplanp_special");
            }

            //หา Max
            int max = getMaxID("stjplant_id", "stjplant");

            int maxSort = 0;
            int mSort = getMaxSortTask("stjplant_sort", "stjplant", "stjplant_flag", "stjplanp_code", prodCode);
            if (taskSort <= 0) {
                maxSort = mSort + 1;

                String sqlStartDate = "SELECT SKIP 0 FIRST 1 stjplant_endplan FROM stjplant where stjplanp_code = '" + prodCode + "' and stjplant_flag = 'Y' order by stjplant_sort desc ";
                clsConnect connectStartDate = new clsConnect();
                ResultSet rsGetStartProj = connectStartDate.getResultSet(sqlStartDate);
                while (rsGetStartProj.next()) {
                    startProjD = rsGetStartProj.getDate("stjplant_endplan");
                }
                startProjS = clsManage.AddDays(startProjD, 1, "dd/MM/yyyy"); //ต้อง + 1 ก่อนเพราะเราเอา End มาเป็น Start
                startProjD = clsManage.ConvertStringToDate(startProjS, "dd/MM/yyyy");

                String varStartEnd = Get_StartEndTask(startProjD, String.valueOf(taskTime), saturday, sunday, special);
                if (varStartEnd.equals("")) {
                    return -1;
                } else {
                    arr = varStartEnd.split(":"); //Start End Next
                }

                String sql = "INSERT INTO stjplant (stjplant_id, stjplanp_code, stjplant_name, stjplant_sort, "
                        + "stjplant_time, stjplant_checkpoint, stjplant_status, stjplant_flag, stxemphr_responsible1, stxemphr_responsible2, stxemphr_responsible3, "
                        + "stxemphr_responsible4, stxemphr_responsible5, stxemphr_createid, stjplant_createdate, stjplant_startplan, stjplant_endplan) "
                        + "VALUES (" + max + ",'" + prodCode + "','" + taskName + "'," + maxSort
                        + "," + taskTime + ",'" + taskPoint + "','N', 'Y','" + respon1 + "','" + respon2 + "','" + respon3 + "','" + respon4 + "','"
                        + respon5 + "','" + manageBy + "',CURRENT, '" + arr[0] + "', '" + arr[1] + "')";
                clsConnect cConnectInsert = new clsConnect();
                res = cConnectInsert.ExecuteQuery(sql);
                //Log
                setLog(manageBy, "Insert", "METPlan", "Insert stjplant: " + max, "Match Template:Set_TaskAdd_MatchTemplate", res);
                //End Log

                connectStartDate.Close();
                cConnectInsert.Close();
            } else if (taskSort <= mSort) {
                maxSort = taskSort;
                String sqlGetID = "SELECT stjplant_id, stjplant_sort FROM stjplant where stjplanp_code = '" + prodCode + "' and stjplant_sort >= " + taskSort + " and stjplant_flag = 'Y' ORDER BY stjplant_sort";
                clsConnect cConnectGetID = new clsConnect();
                ResultSet rs = cConnectGetID.getResultSet(sqlGetID);
                while (rs.next()) {
                    int taskID = rs.getInt("stjplant_id");
                    int sort = rs.getInt("stjplant_sort");
                    sort = sort + 1;
                    String sqlPlusOne = "UPDATE stjplant SET stjplant_sort = " + sort + " where stjplanp_code = '" + prodCode + "' and stjplant_id = " + taskID + " ";
                    clsConnect cConnectUpdate = new clsConnect();
                    cConnectUpdate.ExecuteQuery(sqlPlusOne);
                }

                String sqlIns = "INSERT INTO stjplant (stjplant_id, stjplanp_code, stjplant_name, stjplant_sort, "
                        + "stjplant_time, stjplant_checkpoint, stjplant_status, stjplant_flag, stxemphr_responsible1, stxemphr_responsible2, stxemphr_responsible3, "
                        + "stxemphr_responsible4, stxemphr_responsible5, stxemphr_createid, stjplant_createdate) "
                        + "VALUES (" + max + ",'" + prodCode + "','" + taskName + "'," + maxSort
                        + "," + taskTime + ",'" + taskPoint + "','N', 'Y','" + respon1 + "','" + respon2 + "','" + respon3 + "','" + respon4 + "','"
                        + respon5 + "','" + manageBy + "',CURRENT)";
                clsConnect cConnectIn = new clsConnect();
                res = cConnectIn.ExecuteQuery(sqlIns);
                //Log
                setLog(manageBy, "Insert", "METPlan", "Insert stjplant: " + max, "Match Template:Set_TaskAdd_MatchTemplate", res);
                //End Log

                cConnectIn.Close();

                //-------------------- Insert เสร็จแล้วก็ Update Date
                if (taskSort == 1) {
                    String sqlStartDate = "SELECT SKIP 0 FIRST 1 stjplant_startplan FROM stjplant where stjplanp_code = '" + prodCode + "' and stjplant_sort > " + taskSort + " and stjplant_flag = 'Y' ORDER BY stjplant_sort";
                    clsConnect connectStartDate = new clsConnect();
                    ResultSet rsGetStartProj = connectStartDate.getResultSet(sqlStartDate);
                    while (rsGetStartProj.next()) {
                        startProjD = rsGetStartProj.getDate("stjplant_startplan");
                    }
                    startProjS = clsManage.ConvertDateToString(startProjD, "dd/MM/yyyy");
                } else {
                    String sqlStartDate = "SELECT SKIP 0 FIRST 1 stjplant_endplan FROM stjplant where stjplanp_code = '" + prodCode + "' and stjplant_sort < " + taskSort + " and stjplant_flag = 'Y' ORDER BY stjplant_sort desc ";
                    clsConnect connectStartDate = new clsConnect();
                    ResultSet rsGetStartProj = connectStartDate.getResultSet(sqlStartDate);
                    while (rsGetStartProj.next()) {
                        startProjD = rsGetStartProj.getDate("stjplant_endplan");
                    }
                    startProjS = clsManage.AddDays(startProjD, 1, "dd/MM/yyyy"); //ต้อง + 1 ก่อนเพราะเราเอา End มาเป็น Start
                    startProjD = clsManage.ConvertStringToDate(startProjS, "dd/MM/yyyy");
                }

                String sqlGetIDAgain = "SELECT stjplant_id, stjplant_time FROM stjplant where stjplanp_code = '" + prodCode + "' and stjplant_sort >= " + taskSort + " and stjplant_flag = 'Y' ORDER BY stjplant_sort";
                clsConnect cConnectAgain = new clsConnect();
                ResultSet rsB = cConnectAgain.getResultSet(sqlGetIDAgain);
                while (rsB.next()) {
                    String id = rsB.getString("stjplant_id");
                    String times = rsB.getString("stjplant_time");

                    String varStartEnd = Get_StartEndTask(startProjD, times, saturday, sunday, special);
                    if (varStartEnd.equals("")) {
                        return -1;
                    } else {
                        arr = varStartEnd.split(":"); //Start End Next
                    }

                    String sqlUP = "UPDATE stjplant set stjplant_startplan = '" + arr[0] + "', stjplant_endplan = '" + arr[1] + "' where stjplanp_code = '" + prodCode + "' and stjplant_id = '" + id + "' ";
                    clsConnect cConnectUp = new clsConnect();
                    res = cConnectUp.ExecuteQuery(sqlUP);
                    startProjD = clsManage.ConvertStringToDate(arr[2], "dd/MM/yyyy");
                    //Log
                    setLog(manageBy, "Update", "METPlan", "Insert stjplant: " + id, "Match Template:Set_TaskAdd_MatchTemplate", res);
                    //End Log
                    cConnectUp.Close();

                }
            } else {
                maxSort = mSort + 1;

                String sqlStartDate = "SELECT SKIP 0 FIRST 1 stjplant_endplan FROM stjplant where stjplanp_code = '" + prodCode + "' and stjplant_flag = 'Y' order by stjplant_sort desc ";
                clsConnect connectStartDate = new clsConnect();
                ResultSet rsGetStartProj = connectStartDate.getResultSet(sqlStartDate);
                while (rsGetStartProj.next()) {
                    startProjD = rsGetStartProj.getDate("stjplant_endplan");
                }
                startProjS = clsManage.AddDays(startProjD, 1, "dd/MM/yyyy"); //ต้อง + 1 ก่อนเพราะเราเอา End มาเป็น Start
                startProjD = clsManage.ConvertStringToDate(startProjS, "dd/MM/yyyy");
                String varStartEnd = Get_StartEndTask(startProjD, String.valueOf(taskTime), saturday, sunday, special);

                if (varStartEnd.equals("")) {
                    return -1;
                } else {
                    arr = varStartEnd.split(":"); //Start End Next
                }

                String sql = "INSERT INTO stjplant (stjplant_id, stjplanp_code, stjplant_name, stjplant_sort, "
                        + "stjplant_time, stjplant_checkpoint, stjplant_status, stjplant_flag, stxemphr_responsible1, stxemphr_responsible2, stxemphr_responsible3, "
                        + "stxemphr_responsible4, stxemphr_responsible5, stxemphr_createid, stjplant_createdate, stjplant_startplan, stjplant_endplan) "
                        + "VALUES (" + max + ",'" + prodCode + "','" + taskName + "'," + maxSort
                        + "," + taskTime + ",'" + taskPoint + "','N', 'Y','" + respon1 + "','" + respon2 + "','" + respon3 + "','" + respon4 + "','"
                        + respon5 + "','" + manageBy + "',CURRENT, '" + arr[0] + "', '" + arr[1] + "')";
                clsConnect cConnectInsert = new clsConnect();
                res = cConnectInsert.ExecuteQuery(sql);
                //Log
                setLog(manageBy, "Insert", "METPlan", "Insert stjplant: " + max, "Match Template:Set_TaskAdd_MatchTemplate", res);
                //End Log

                cConnectInsert.Close();

            }

            //----
            String startPlan = "";
            String endPlan = "";
            String sqlGetStartEnd = "SELECT MIN(stjplant_startplan) AS minP, MAX(stjplant_endplan) AS maxP "
                    + " FROM stjplant "
                    + " WHERE stjplanp_code = '" + prodCode + "' AND stjplant_flag = 'Y'";
            clsConnect connGetStartEnd = new clsConnect();
            ResultSet rsGetStartEnd = connGetStartEnd.getResultSet(sqlGetStartEnd);
            while (rsGetStartEnd.next()) {
                String tempStartPlan = rsGetStartEnd.getString("minP").replace("/", "-");
                tempStartPlan = clsManage.manageYear4DMY(tempStartPlan, -1);
                startPlan = tempStartPlan.replace("-", "/");

                String tempEndPlan = rsGetStartEnd.getString("maxP").replace("/", "-");
                tempEndPlan = clsManage.manageYear4DMY(tempEndPlan, -1);
                endPlan = tempEndPlan.replace("-", "/");
            }

            String sqlSetStartEnd = "UPDATE stjplanp SET stjplanp_startplan = '" + startPlan + "', "
                    + " stjplanp_endplan = '" + endPlan + "', "
                    + " stjplanp_saturday = '" + saturday + "', stjplanp_sunday = '" + sunday + "', stjplanp_special = '" + special + "' "
                    + "where stjplanp_code = '" + prodCode + "' ";
            clsConnect cConnSetSE = new clsConnect();
            int resP = cConnSetSE.ExecuteQuery(sqlSetStartEnd);
            //Log
            setLog(manageBy, "Update", "METPlan", "Update stjplanp: " + prodCode, "Match Template:Set_TaskAdd_MatchTemplate", resP);
            //End Log
            cConnSetSE.Close();

            return resP;
        } catch (Exception ex) {
            return -1;
        }
    }

    public int Set_TaskEdit_MatchTemplate(String prodCode, String taskID, String taskName, String taskDate, int taskTime, int taskSort, String taskPoint, String respon1, String respon2, String respon3, String respon4, String respon5, String saturday, String sunday, String special, String manageBy) {
        try {

            int maxSort = 0;
            int masterSort = 0;
            int masterTime = 0;
            String masterDate = "";
            String arr[] = null;
            Date startProjD = new Date();
            String startProjS = "";

            clsConnect cConnUPData = new clsConnect();
            String sqlUPData = "";
            int exeUPData = -1;

            clsConnect cConnect = new clsConnect();
            String sqlGet = "";

            //find master sort
            String sqlM = "SELECT * FROM stjplant where stjplanp_code = '" + prodCode + "' and stjplant_id = " + taskID;
            ResultSet rsM = cConnect.getResultSet(sqlM);
            while (rsM.next()) {
                String tempStartPlan = rsM.getString("stjplant_startplan").replace("/", "-");
                tempStartPlan = clsManage.manageYear4DMY(tempStartPlan, -1);
                masterDate = tempStartPlan.replace("-", "/");
                masterSort = rsM.getInt("stjplant_sort");
                masterTime = rsM.getInt("stjplant_time");
            }

            if (taskSort == 0) { //เพื่อหาลำดับที่แท้จริง
                maxSort = masterSort;
            } else {
                maxSort = taskSort;
            }

            if (maxSort == masterSort) {
                if (!clsManage.chkNull(taskDate).equals("")) {
                    startProjS = taskDate.replace("-", "/");
                }
                startProjD = clsManage.ConvertStringToDate(startProjS, "dd/MM/yyyy");
            } else if (masterSort == 1) { //เปลี่ยนจาก 1 มาเป็นอย่างอื่น
                startProjS = masterDate;
                startProjD = clsManage.ConvertStringToDate(masterDate, "dd/MM/yyyy");
            } else {
                if (!clsManage.chkNull(taskDate).equals("")) {
                    startProjS = taskDate.replace("-", "/");
                }
                startProjD = clsManage.ConvertStringToDate(startProjS, "dd/MM/yyyy");
            }

            if (maxSort == masterSort) {
                if (startProjS.equals(masterDate)) {
//                    if (taskTime == masterTime) {
//                        sqlUPData = "UPDATE stjplant SET stjplant_name = '" + taskName + "', "
//                                + " stxemphr_responsible1 = '" + respon1 + "', stxemphr_responsible2 = '" + respon2 + "', "
//                                + " stxemphr_responsible3 = '" + respon3 + "', stxemphr_responsible4 = '" + respon4 + "', "
//                                + " stxemphr_responsible5 = '" + respon5 + "', stxemphr_updateid = '" + manageBy + "', "
//                                + " stjplant_updatedate = CURRENT "
//                                + " WHERE stjplant_id = '" + taskID + "' ";
//                        exeUPData = cConnUPData.ExecuteQuery(sqlUPData);
//                    } else {
                    sqlUPData = "UPDATE stjplant SET stjplant_name = '" + taskName + "', stjplant_time = '" + taskTime + "', "
                            + " stxemphr_responsible1 = '" + respon1 + "', stxemphr_responsible2 = '" + respon2 + "', "
                            + " stxemphr_responsible3 = '" + respon3 + "', stxemphr_responsible4 = '" + respon4 + "', "
                            + " stxemphr_responsible5 = '" + respon5 + "', stxemphr_updateid = '" + manageBy + "', "
                            + " stjplant_updatedate = CURRENT "
                            + " WHERE stjplanp_code = '" + prodCode + "' and stjplant_id = '" + taskID + "' ";
                    exeUPData = cConnUPData.ExecuteQuery(sqlUPData);
                    if (exeUPData < 0) {
                        return -1;
                    } else {
                        sqlGet = "SELECT * FROM stjplant WHERE stjplanp_code = '" + prodCode + "' and stjplant_sort >= '" + masterSort + "' AND stjplant_flag = 'Y' ORDER BY stjplant_sort";
                        ResultSet rsGet = cConnect.getResultSet(sqlGet);
                        while (rsGet.next()) {
                            String id = rsGet.getString("stjplant_id");
                            String times = rsGet.getString("stjplant_time");

                            String varStartEnd = Get_StartEndTask(startProjD, times, saturday, sunday, special);
                            if (varStartEnd.equals("")) {
                                return -1;
                            } else {
                                arr = varStartEnd.split(":"); //Start End Next
                            }

                            String sqlUP = "UPDATE stjplant set stjplant_startplan = '" + arr[0] + "', stjplant_endplan = '" + arr[1] + "' where stjplanp_code = '" + prodCode + "' and stjplant_id = '" + id + "' ";
                            clsConnect cConnectUp = new clsConnect();
                            int resUP = cConnectUp.ExecuteQuery(sqlUP);
                            startProjD = clsManage.ConvertStringToDate(arr[2], "dd/MM/yyyy");
                            //Log
                            setLog(manageBy, "Update", "METPlan", "Update stjplant: " + id, "Match Template:Set_TaskDel_MatchTemplate", resUP);
                            //End Log
                            cConnectUp.Close();
                        }
                    }
//                    }
                } else {
                    sqlUPData = "UPDATE stjplant SET stjplant_name = '" + taskName + "', stjplant_time = '" + taskTime + "', "
                            + " stjplant_startplan = '" + startProjS + "', "
                            + " stxemphr_responsible1 = '" + respon1 + "', stxemphr_responsible2 = '" + respon2 + "', "
                            + " stxemphr_responsible3 = '" + respon3 + "', stxemphr_responsible4 = '" + respon4 + "', "
                            + " stxemphr_responsible5 = '" + respon5 + "', stxemphr_updateid = '" + manageBy + "', "
                            + " stjplant_updatedate = CURRENT "
                            + " WHERE stjplanp_code = '" + prodCode + "' and stjplant_id = '" + taskID + "' ";
                    exeUPData = cConnUPData.ExecuteQuery(sqlUPData);
                    if (exeUPData < 0) {
                        return -1;
                    } else {
                        sqlGet = "SELECT * FROM stjplant WHERE stjplanp_code = '" + prodCode + "' and stjplant_sort >= '" + masterSort + "' AND stjplant_flag = 'Y' ORDER BY stjplant_sort";
                        ResultSet rsGet = cConnect.getResultSet(sqlGet);
                        while (rsGet.next()) {
                            String id = rsGet.getString("stjplant_id");
                            String times = rsGet.getString("stjplant_time");

                            String varStartEnd = Get_StartEndTask(startProjD, times, saturday, sunday, special);
                            if (varStartEnd.equals("")) {
                                return -1;
                            } else {
                                arr = varStartEnd.split(":"); //Start End Next
                            }

                            String sqlUP = "UPDATE stjplant set stjplant_startplan = '" + arr[0] + "', stjplant_endplan = '" + arr[1] + "' where stjplanp_code = '" + prodCode + "' and stjplant_id = '" + id + "' ";
                            clsConnect cConnectUp = new clsConnect();
                            int resUP = cConnectUp.ExecuteQuery(sqlUP);
                            startProjD = clsManage.ConvertStringToDate(arr[2], "dd/MM/yyyy");
                            //Log
                            setLog(manageBy, "Update", "METPlan", "Update stjplant: " + id, "Match Template:Set_TaskDel_MatchTemplate", resUP);
                            //End Log
                            cConnectUp.Close();
                        }
                    }
                }
            } else if (taskSort > masterSort) {
                String sqlA = "SELECT stjplant_id, stjplant_sort FROM stjplant where stjplanp_code = '" + prodCode + "' and stjplant_sort > " + masterSort + " and stjplant_sort <= " + taskSort + " and stjplant_flag = 'Y' order by stjplant_sort";
                ResultSet rsA = cConnect.getResultSet(sqlA);
                while (rsA.next()) {
                    int taskIDInDB = rsA.getInt("stjplant_id");
                    int sort = rsA.getInt("stjplant_sort");
                    sort = sort - 1;
                    String sqlAOne = "UPDATE stjplant SET stjplant_sort = " + sort + " where stjplanp_code = '" + prodCode + "' and stjplant_id = " + taskIDInDB + " ";
                    clsConnect cConnectA = new clsConnect();
                    cConnectA.ExecuteQuery(sqlAOne);
                }

                sqlUPData = "UPDATE stjplant SET stjplant_name = '" + taskName + "', stjplant_time = '" + taskTime + "', "
                        + " stjplant_startplan = '" + startProjS + "', stjplant_sort = '" + taskSort + "', "
                        + " stxemphr_responsible1 = '" + respon1 + "', stxemphr_responsible2 = '" + respon2 + "', "
                        + " stxemphr_responsible3 = '" + respon3 + "', stxemphr_responsible4 = '" + respon4 + "', "
                        + " stxemphr_responsible5 = '" + respon5 + "', stxemphr_updateid = '" + manageBy + "', "
                        + " stjplant_updatedate = CURRENT "
                        + " WHERE stjplanp_code = '" + prodCode + "' and stjplant_id = '" + taskID + "' ";
                exeUPData = cConnUPData.ExecuteQuery(sqlUPData);
                if (exeUPData < 0) {
                    return -1;
                } else {
                    sqlGet = "SELECT * FROM stjplant WHERE stjplanp_code = '" + prodCode + "' and stjplant_sort >= '" + masterSort + "' AND stjplant_flag = 'Y' ORDER BY stjplant_sort";
                    ResultSet rsGet = cConnect.getResultSet(sqlGet);
                    while (rsGet.next()) {
                        String id = rsGet.getString("stjplant_id");
                        String times = rsGet.getString("stjplant_time");

                        String varStartEnd = Get_StartEndTask(startProjD, times, saturday, sunday, special);
                        if (varStartEnd.equals("")) {
                            return -1;
                        } else {
                            arr = varStartEnd.split(":"); //Start End Next
                        }

                        String sqlUP = "UPDATE stjplant set stjplant_startplan = '" + arr[0] + "', stjplant_endplan = '" + arr[1] + "' where stjplanp_code = '" + prodCode + "' and stjplant_id = '" + id + "' ";
                        clsConnect cConnectUp = new clsConnect();
                        int resUP = cConnectUp.ExecuteQuery(sqlUP);
                        startProjD = clsManage.ConvertStringToDate(arr[2], "dd/MM/yyyy");
                        //Log
                        setLog(manageBy, "Update", "METPlan", "Update stjplant: " + id, "Match Template:Set_TaskDel_MatchTemplate", resUP);
                        //End Log
                        cConnectUp.Close();
                    }
                }

            } else if (taskSort < masterSort) {
                String sqlA = "SELECT stjplant_id, stjplant_sort FROM stjplant WHERE stjplanp_code = '" + prodCode + "' and stjplant_sort >= " + taskSort + " and stjplant_sort < " + masterSort + " and stjplant_flag = 'Y' order by stjplant_sort";
                ResultSet rsA = cConnect.getResultSet(sqlA);
                while (rsA.next()) {
                    int taskIDInDB = rsA.getInt("stjplant_id");
                    int sort = rsA.getInt("stjplant_sort");
                    sort = sort + 1;
                    String sqlAOne = "UPDATE stjplant SET stjplant_sort = " + sort + " where stjplanp_code = '" + prodCode + "' and stjplant_id = " + taskIDInDB + " ";
                    clsConnect cConnectA = new clsConnect();
                    cConnectA.ExecuteQuery(sqlAOne);
                }

                sqlUPData = "UPDATE stjplant SET stjplant_name = '" + taskName + "', stjplant_time = '" + taskTime + "', "
                        + " stjplant_startplan = '" + startProjS + "', stjplant_sort = '" + taskSort + "', "
                        + " stxemphr_responsible1 = '" + respon1 + "', stxemphr_responsible2 = '" + respon2 + "', "
                        + " stxemphr_responsible3 = '" + respon3 + "', stxemphr_responsible4 = '" + respon4 + "', "
                        + " stxemphr_responsible5 = '" + respon5 + "', stxemphr_updateid = '" + manageBy + "', "
                        + " stjplant_updatedate = CURRENT "
                        + " WHERE stjplanp_code = '" + prodCode + "' and stjplant_id = '" + taskID + "' ";
                exeUPData = cConnUPData.ExecuteQuery(sqlUPData);
                if (exeUPData < 0) {
                    return -1;
                } else {
                    sqlGet = "SELECT * FROM stjplant WHERE stjplanp_code = '" + prodCode + "' and stjplant_sort >= '" + taskSort + "' AND stjplant_flag = 'Y' ORDER BY stjplant_sort";
                    ResultSet rsGet = cConnect.getResultSet(sqlGet);
                    while (rsGet.next()) {
                        String id = rsGet.getString("stjplant_id");
                        String times = rsGet.getString("stjplant_time");

                        String varStartEnd = Get_StartEndTask(startProjD, times, saturday, sunday, special);
                        if (varStartEnd.equals("")) {
                            return -1;
                        } else {
                            arr = varStartEnd.split(":"); //Start End Next
                        }

                        String sqlUP = "UPDATE stjplant set stjplant_startplan = '" + arr[0] + "', stjplant_endplan = '" + arr[1] + "' where stjplanp_code = '" + prodCode + "' and stjplant_id = '" + id + "' ";
                        clsConnect cConnectUp = new clsConnect();
                        int resUP = cConnectUp.ExecuteQuery(sqlUP);
                        startProjD = clsManage.ConvertStringToDate(arr[2], "dd/MM/yyyy");
                        //Log
                        setLog(manageBy, "Update", "METPlan", "Update stjplant: " + id, "Match Template:Set_TaskDel_MatchTemplate", resUP);
                        //End Log
                        cConnectUp.Close();
                    }
                }
            }

            //----
            String startPlan = "";
            String endPlan = "";
            String sqlGetStartEnd = "SELECT MIN(stjplant_startplan) AS minP, MAX(stjplant_endplan) AS maxP "
                    + " FROM stjplant "
                    + " WHERE stjplanp_code = '" + prodCode + "' AND stjplant_flag = 'Y'";
            clsConnect connGetStartEnd = new clsConnect();
            ResultSet rsGetStartEnd = connGetStartEnd.getResultSet(sqlGetStartEnd);
            while (rsGetStartEnd.next()) {
                String tempStartPlan = rsGetStartEnd.getString("minP").replace("/", "-");
                tempStartPlan = clsManage.manageYear4DMY(tempStartPlan, -1);
                startPlan = tempStartPlan.replace("-", "/");

                String tempEndPlan = rsGetStartEnd.getString("maxP").replace("/", "-");
                tempEndPlan = clsManage.manageYear4DMY(tempEndPlan, -1);
                endPlan = tempEndPlan.replace("-", "/");
            }

            String sqlSetStartEnd = "UPDATE stjplanp SET stjplanp_startplan = '" + startPlan + "', "
                    + " stjplanp_endplan = '" + endPlan + "', "
                    + " stjplanp_saturday = '" + saturday + "', stjplanp_sunday = '" + sunday + "', stjplanp_special = '" + special + "' "
                    + " where stjplanp_code = '" + prodCode + "' ";
            clsConnect cConnSetSE = new clsConnect();
            int resP = cConnSetSE.ExecuteQuery(sqlSetStartEnd);
            //Log
            setLog(manageBy, "Update", "METPlan", "Update stjplanp: " + prodCode, "Match Template:Set_TaskAdd_MatchTemplate", resP);
            //End Log
            cConnSetSE.Close();

            return resP;
        } catch (Exception ex) {
            return -1;
        }
    }

    public int Set_TaskDel_MatchTemplate(String prodCode, String taskID, String saturday, String sunday, String special, String manageBy) {
        try {
            clsConnect cConnect = new clsConnect();
            int taskSort = 0;
            Date startProjD = new Date();
            String startProjS = "";

            String sqlM = "SELECT stjplant_sort, stjplant_startplan FROM stjplant where stjplanp_code = '" + prodCode + "' and stjplant_id = " + taskID;
            ResultSet rsM = cConnect.getResultSet(sqlM);
            while (rsM.next()) {
                taskSort = rsM.getInt("stjplant_sort");
                startProjD = rsM.getDate("stjplant_startplan");
            }

            String sqlA = "SELECT stjplant_id, stjplant_sort FROM stjplant WHERE stjplanp_code = '" + prodCode + "' and stjplant_sort > " + taskSort + " and stjplant_flag = 'Y' ORDER BY stjplant_sort";
            ResultSet rsA = cConnect.getResultSet(sqlA);
            while (rsA.next()) {
                int taskIDInDB = rsA.getInt("stjplant_id");
                int sort = rsA.getInt("stjplant_sort");
                sort = sort - 1;
                String sqlAOne = "UPDATE stjplant SET stjplant_sort = " + sort + " where stjplanp_code = '" + prodCode + "' and stjplant_id = " + taskIDInDB + " ";
                clsConnect cConnectA = new clsConnect();
                cConnectA.ExecuteQuery(sqlAOne);
            }

            String sql = "UPDATE stjplant SET stjplant_flag = 'N', stxemphr_updateid = '" + manageBy + "', stjplant_updatedate = CURRENT "
                    + "WHERE stjplanp_code = '" + prodCode + "' and stjplant_id = '" + taskID + "' ";
            int resA = cConnect.ExecuteQuery(sql);
            cConnect.Close();

            //Log
            setLog(manageBy, "Delete", "METPlan", "Delete stjplant: " + taskID, "Match Template:Set_TaskDel_MatchTemplate", resA);
            //End Log

            //ขยับวัน
            String arr[] = null;
            String sqlGetIDAgain = "SELECT stjplant_id, stjplant_time FROM stjplant where stjplanp_code = '" + prodCode + "' and stjplant_sort >= " + taskSort + " and stjplant_flag = 'Y' ORDER BY stjplant_sort";
            clsConnect cConnectAgain = new clsConnect();
            ResultSet rsB = cConnectAgain.getResultSet(sqlGetIDAgain);
            while (rsB.next()) {
                String id = rsB.getString("stjplant_id");
                String times = rsB.getString("stjplant_time");

                String varStartEnd = Get_StartEndTask(startProjD, times, saturday, sunday, special);
                if (varStartEnd.equals("")) {
                    return -1;
                } else {
                    arr = varStartEnd.split(":"); //Start End Next
                }

                String sqlUP = "UPDATE stjplant set stjplant_startplan = '" + arr[0] + "', stjplant_endplan = '" + arr[1] + "' where stjplanp_code = '" + prodCode + "' and stjplant_id = '" + id + "' ";
                clsConnect cConnectUp = new clsConnect();
                int resB = cConnectUp.ExecuteQuery(sqlUP);
                startProjD = clsManage.ConvertStringToDate(arr[2], "dd/MM/yyyy");
                //Log
                setLog(manageBy, "Update", "METPlan", "Update stjplant: " + id, "Match Template:Set_TaskDel_MatchTemplate", resB);
                //End Log
                cConnectUp.Close();

            }

            //----
            String startPlan = "";
            String endPlan = "";
            String sqlGetStartEnd = "SELECT MIN(stjplant_startplan) AS minP, MAX(stjplant_endplan) AS maxP "
                    + " FROM stjplant "
                    + " WHERE stjplanp_code = '" + prodCode + "' AND stjplant_flag = 'Y'";
            clsConnect connGetStartEnd = new clsConnect();
            ResultSet rsGetStartEnd = connGetStartEnd.getResultSet(sqlGetStartEnd);
            while (rsGetStartEnd.next()) {
                String tempStartPlan = rsGetStartEnd.getString("minP").replace("/", "-");
                tempStartPlan = clsManage.manageYear4DMY(tempStartPlan, -1);
                startPlan = tempStartPlan.replace("-", "/");

                String tempEndPlan = rsGetStartEnd.getString("maxP").replace("/", "-");
                tempEndPlan = clsManage.manageYear4DMY(tempEndPlan, -1);
                endPlan = tempEndPlan.replace("-", "/");
            }

            String sqlSetStartEnd = "UPDATE stjplanp SET stjplanp_startplan = '" + startPlan + "', "
                    + " stjplanp_endplan = '" + endPlan + "', "
                    + " stjplanp_saturday = '" + saturday + "', stjplanp_sunday = '" + sunday + "', stjplanp_special = '" + special + "' "
                    + "where stjplanp_code = '" + prodCode + "' ";
            clsConnect cConnSetSE = new clsConnect();
            int resP = cConnSetSE.ExecuteQuery(sqlSetStartEnd);
            //Log
            setLog(manageBy, "Update", "METPlan", "Update stjplanp: " + prodCode, "Match Template:Set_TaskAdd_MatchTemplate", resP);
            //End Log
            cConnSetSE.Close();

            return resP;
        } catch (Exception ex) {
            return -1;
        }
    }

    public List Get_TaskDetail_MatchTemplate(String templateCode, String taskCode, String oDate) {
        try {
            String sql = "SELECT h.stjtmplh_code, h.stjtmplh_name, h.stjtmplh_type, "
                    + " d.stjplant_id, TRIM(d.stjplant_name) AS stjplant_name, d.stjplant_sort, d.stjplant_time, "
                    + " trim(d.stxemphr_responsible1) AS coor1, trim(d.stxemphr_responsible2) AS coor2, trim(d.stxemphr_responsible3) AS coor3, "
                    + " trim(d.stxemphr_responsible4) AS coor4, trim(d.stxemphr_responsible5) AS coor5, d.stjplant_startplan, "
                    + " p.stjplanp_saturday, p.stjplanp_sunday, p.stjplanp_special "
                    + " FROM stjplant AS d "
                    + " LEFT JOIN stjplanp AS p ON d.stjplanp_code = p.stjplanp_code "
                    + " LEFT JOIN stjtmplh AS h on p.stjtmplh_code = h.stjtmplh_code "
                    + " WHERE d.stjplanp_code = '" + templateCode + "' and d.stjplant_id = " + taskCode;

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanTemplate bTemplate = new beanTemplate();
                bTemplate.setTemplateCode(rs.getString("stjtmplh_code"));
                bTemplate.setTemplateName(rs.getString("stjtmplh_name"));
                bTemplate.setTemplateTypeCode(rs.getString("stjtmplh_type"));
                bTemplate.setTaskID(rs.getString("stjplant_id"));
                bTemplate.setTaskName(rs.getString("stjplant_name"));
                bTemplate.setTaskSort(rs.getString("stjplant_sort"));
                bTemplate.setTaskTime(rs.getString("stjplant_time"));
                bTemplate.setTaskResponsible1(rs.getString("coor1"));
                bTemplate.setTaskResponsible2(rs.getString("coor2"));
                bTemplate.setTaskResponsible3(rs.getString("coor3"));
                bTemplate.setTaskResponsible4(rs.getString("coor4"));
                bTemplate.setTaskResponsible5(rs.getString("coor5"));
                bTemplate.setChkSaturday(clsManage.chkNull(rs.getString("stjplanp_saturday")));
                bTemplate.setChkSunday(clsManage.chkNull(rs.getString("stjplanp_sunday")));
                bTemplate.setChkSpecial(clsManage.chkNull(rs.getString("stjplanp_special")));

                String sPlan = clsManage.chkNull(rs.getString("stjplant_startplan"));
                if (sPlan.equals("")) {
                    bTemplate.setTaskStart("");
                } else {
                    String tempStartPlan = rs.getString("stjplant_startplan").replace("/", "-");
                    bTemplate.setTaskStart(clsManage.manageYear4DMY(tempStartPlan, -1));
                }

                arrList.add(bTemplate);
                bTemplate = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_Task_MatchTemplate(String prodCode, String templateCode) {
        try {
            String sql = "SELECT s.stjplant_id, TRIM(s.stjplant_name) AS stjplant_name, "
                    + " s.stjplant_time, s.stjplant_sort, s.stjplant_checkpoint, "
                    + " s.stjplant_startplan, s.stjplant_endplan, "
                    + " TRIM(e1.emp_name) AS e1Name, TRIM(e2.emp_name) AS e2Name, "
                    + " TRIM(e3.emp_name) AS e3Name, TRIM(e4.emp_name) AS e4Name, "
                    + " TRIM(e5.emp_name) AS e5Name "
                    + " FROM stjplant s "
                    + " LEFT JOIN stxemphr e1 ON s.stxemphr_responsible1 = e1.emp_code "
                    + " LEFT JOIN stxemphr e2 ON s.stxemphr_responsible2 = e2.emp_code "
                    + " LEFT JOIN stxemphr e3 ON s.stxemphr_responsible3 = e3.emp_code "
                    + " LEFT JOIN stxemphr e4 ON s.stxemphr_responsible4 = e4.emp_code "
                    + " LEFT JOIN stxemphr e5 ON s.stxemphr_responsible5 = e5.emp_code "
                    + " WHERE s.stjplanp_code = '" + prodCode + "' and s.stjplant_flag = 'Y' "
                    + " ORDER BY s.stjplant_sort";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanTemplate bTemplate = new beanTemplate();
                String taskID = rs.getString("stjplant_id");
                bTemplate.setTaskID(rs.getString("stjplant_id"));
                bTemplate.setTaskName(rs.getString("stjplant_name"));

                String sPlan = clsManage.chkNull(rs.getString("stjplant_startplan"));
                if (sPlan.equals("")) {
                    bTemplate.setTaskStart("");
                    bTemplate.setYYYYMMDD(rs.getString("99999999"));
                } else {
                    String tempStartPlan = rs.getString("stjplant_startplan").replace("/", "-");
                    bTemplate.setTaskStart(clsManage.manageYear4DMY(tempStartPlan, -1));

                    String tempA = clsManage.manageYear4DMY(tempStartPlan, -1); //01-01-2555 > 01-01-2012
                    String tempB = clsManage.convDMY2YYYYMMDD(tempA);
                    bTemplate.setYYYYMMDD(tempB);
                }

                String ePlan = clsManage.chkNull(rs.getString("stjplant_endplan"));
                if (ePlan.equals("")) {
                    bTemplate.setTaskEnd("");
                } else {
                    String tempEndPlan = rs.getString("stjplant_endplan").replace("/", "-");
                    bTemplate.setTaskEnd(clsManage.manageYear4DMY(tempEndPlan, -1));
                }

                bTemplate.setTaskTime(rs.getString("stjplant_time"));
                bTemplate.setTaskSort(rs.getString("stjplant_sort"));
                bTemplate.setTaskCheckPoint(clsManage.convChkPointCode2Name(rs.getString("stjplant_checkpoint")));
                bTemplate.setTaskResponsible1(rs.getString("e1Name"));
                bTemplate.setTaskResponsible2(rs.getString("e2Name"));
                bTemplate.setTaskResponsible3(rs.getString("e3Name"));
                bTemplate.setTaskResponsible4(rs.getString("e4Name"));
                bTemplate.setTaskResponsible5(rs.getString("e5Name"));
                bTemplate.setCountLog(Get_CountLog(taskID));
                arrList.add(bTemplate);
                bTemplate = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String Get_CountLog(String taskID) {
        try {
            String countR = "0";
            String sql = "SELECT COUNT(stjplant_id) AS countRows FROM stjplanl WHERE stjplant_id = '" + taskID + "' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            while (rs.next()) {
                countR = rs.getString("countRows");
            }
            return countR;
        } catch (Exception ex) {
            return "0";
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="My Project">
    public String Get_View(String empCode) {
        try {
            String arrEmpCode = "";
            String sql = "SELECT TRIM(stxemphr_view) AS eCode "
                    + " FROM stjgrmnv "
                    + " WHERE stxemphr_code = '" + empCode + "' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            while (rs.next()) {
                if (arrEmpCode.equals("")) {
                    arrEmpCode = "'" + rs.getString("eCode") + "'";
                } else {
                    arrEmpCode = arrEmpCode + ",'" + rs.getString("eCode") + "'";
                }
            }
            return arrEmpCode;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "'" + empCode + "'";
        }
    }

    public List Get_ProjectCount_MyProj(String searchFrom, String search, String searchSign, String searchDate, String searchPriority, String searchStatus, String manageBy) {
        try {
            String view = Get_View(manageBy);
            String where = "1=1 AND stxemphr_coordinator IN (" + view + ") ";
            if (!searchFrom.equals("")) {
                if (searchFrom.equals("Proj")) {
                    where = where + " AND (p.proj_code LIKE '%" + search + "%' OR p.proj_lname LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Cust")) {
                    where = where + " AND (p.cust_code LIKE '%" + search + "%' OR c.cust_name LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Sale")) {
                    where = where + " AND (p.sales_code LIKE '%" + search + "%' OR s.emp_name LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Coor")) {
                    where = where + " AND (p.stxemphr_coordinator LIKE '%" + search + "%' OR e.emp_name LIKE '%" + search + "%') ";
                }
            }
            if (searchStatus.equals("")) {
                where = where + " AND (p.stjmasth_status = '" + searchStatus + "' OR p.stjmasth_status IS NULL) ";
            } else {
                where = where + " AND p.stjmasth_status = '" + searchStatus + "' ";
            }
            if (!searchPriority.equals("")) {
                where = where + " AND p.stjmasth_priority = '" + searchPriority + "' ";
            }
            if (!searchDate.equals("")) {
                String dateDMY = searchDate.replace("-", "/");
                where = where + " AND p.stjmasth_startplan " + searchSign + " '" + dateDMY + "' ";
            }
            String sql = "SELECT COUNT(p.proj_code) AS countRows "
                    + " FROM stjmasth p "
                    + " LEFT JOIN stxcustr c ON p.cust_code = c.cust_code "
                    + " LEFT JOIN stxemphr s ON p.sales_code = s.emp_code "
                    + " LEFT JOIN stxemphr e ON p.stxemphr_coordinator = e.emp_code"
                    + " WHERE " + where;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("countRows")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_Project_MyProj(String searchFrom, String search, String searchSign, String searchDate, String searchPriority, String searchStatus, String sortField, String sortDescAsc, int startRow, String limit, String manageBy) {
        try {
            String view = Get_View(manageBy);
            String where = "1=1 AND stxemphr_coordinator IN (" + view + ") ";
            if (!searchFrom.equals("")) {
                if (searchFrom.equals("Proj")) {
                    where = where + " AND (p.proj_code LIKE '%" + search + "%' OR p.proj_lname LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Cust")) {
                    where = where + " AND (p.cust_code LIKE '%" + search + "%' OR c.cust_name LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Sale")) {
                    where = where + " AND (p.sales_code LIKE '%" + search + "%' OR s.emp_name LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Coor")) {
                    where = where + " AND (p.stxemphr_coordinator LIKE '%" + search + "%' OR e.emp_name LIKE '%" + search + "%') ";
                }
            }
            if (searchStatus.equals("")) {
                where = where + " AND (p.stjmasth_status = '" + searchStatus + "' OR p.stjmasth_status IS NULL) ";
            } else {
                where = where + " AND p.stjmasth_status = '" + searchStatus + "' ";
            }
            if (!searchPriority.equals("")) {
                where = where + " AND p.stjmasth_priority = '" + searchPriority + "' ";
            }
            if (!searchDate.equals("")) {
                String dateDMY = searchDate.replace("-", "/");
                where = where + " AND p.stjmasth_startplan " + searchSign + " '" + dateDMY + "' ";
            }
            String sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
                    + " trim(p.proj_code) AS projCode, trim(p.proj_lname) AS projName, "
                    + " p.stjmasth_startplan AS startPlan, p.stjmasth_endplan AS endPlan, "
                    + " p.stjmasth_status AS status, p.stjmasth_priority AS priority, "
                    + " trim(c.cust_code) AS custCode, trim(c.cust_name) AS custName, "
                    + " trim(s.emp_code) AS saleCode, trim(s.emp_name) AS saleName, "
                    + " trim(e.emp_code) AS coorCode, trim(e.emp_name) AS coorName, "
                    + " p.stjmasth_unit AS qty, p.stjmasth_task AS qtyT "
                    + " FROM stjmasth p "
                    + " LEFT JOIN stjplanp l ON p.proj_code = l.proj_code "
                    + " LEFT JOIN stxcustr c ON p.cust_code = c.cust_code "
                    + " LEFT JOIN stxemphr s ON p.sales_code = s.emp_code "
                    + " LEFT JOIN stxemphr e ON p.stxemphr_coordinator = e.emp_code"
                    + " WHERE " + where
                    + " GROUP BY p.proj_code, p.proj_lname, p.stjmasth_startplan, p.stjmasth_endplan, "
                    + " p.stjmasth_status, p.stjmasth_priority, c.cust_code, c.cust_name, "
                    + " s.emp_code, s.emp_name, e.emp_code, e.emp_name, p.stjmasth_unit, p.stjmasth_task"
                    + " ORDER BY " + sortField + " " + sortDescAsc;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanProject bProject = new beanProject();
                bProject.setProjCode(rs.getString("projCode"));
                bProject.setProjName(clsManage.chkNull(rs.getString("projName")));
                bProject.setCustCode(clsManage.chkNull(rs.getString("custCode")));
                bProject.setCustName(clsManage.chkNull(rs.getString("custName")));
                bProject.setSaleCode(clsManage.chkNull(rs.getString("saleCode")));
                bProject.setSaleName(clsManage.chkNull(rs.getString("saleName")));
                bProject.setCoorCode(clsManage.chkNull(rs.getString("coorCode")));
                bProject.setCoorName(clsManage.chkNull(rs.getString("coorName")));
                bProject.setProjQty(rs.getString("qty"));
                bProject.setProjQtyT(rs.getString("qtyT"));

                String sPlan = clsManage.chkNull(rs.getString("startPlan"));
                if (sPlan.equals("")) {
                    bProject.setStartProject("");
                } else {
                    String tempStartPlan = rs.getString("startPlan").replace("/", "-");
                    bProject.setStartProject(clsManage.manageYear4DMY(tempStartPlan, -1));
                }

                String ePlan = clsManage.chkNull(rs.getString("endPlan"));
                if (ePlan.equals("")) {
                    bProject.setEndProject("");
                } else {
                    String tempEndPlan = rs.getString("endPlan").replace("/", "-");
                    bProject.setEndProject(clsManage.manageYear4DMY(tempEndPlan, -1));
                }

                bProject.setPriority(clsManage.convPriorityCode2Name(clsManage.chkNull(rs.getString("priority"))));
                bProject.setStatus(clsManage.convStatusCode2Name(clsManage.chkNull(rs.getString("status"))));
                arrList.add(bProject);
                bProject = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_ProjectDetail_MyProj(String projectCode) {
        try {
            String sql = "SELECT trim(p.proj_code) AS projCode, trim(p.proj_lname) AS projName, "
                    + " p.stjmasth_priority AS priority, p.stjmasth_status AS status, "
                    + " trim(c.cust_code) AS custCode, trim(c.cust_name) AS custName, "
                    + " trim(s.emp_code) AS saleCode, trim(s.emp_name) AS saleName, "
                    + " trim(e.emp_code) AS coorCode, trim(e.emp_name) AS coorName, "
                    + " p.stjmasth_startplan AS startPlan, p.stjmasth_endplan AS endPlan, "
                    + " p.stjmasth_unit AS qty, p.stjmasth_task AS qtyT "
                    + " FROM stjmasth p "
                    + " LEFT JOIN stjplanp l ON p.proj_code = l.proj_code "
                    + " LEFT JOIN stxcustr c ON p.cust_code = c.cust_code "
                    + " LEFT JOIN stxemphr s ON p.sales_code = s.emp_code "
                    + " LEFT JOIN stxemphr e ON p.stxemphr_coordinator = e.emp_code"
                    + " WHERE p.proj_code = '" + projectCode + "' "
                    + " GROUP BY p.proj_code, p.proj_lname, "
                    + " p.stjmasth_priority, p.stjmasth_status, "
                    + " c.cust_code, c.cust_name, "
                    + " s.emp_code, s.emp_name, e.emp_code, e.emp_name, "
                    + " p.stjmasth_startplan, p.stjmasth_endplan, "
                    + " p.stjmasth_unit, p.stjmasth_task ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanProject bProject = new beanProject();
                bProject.setProjCode(rs.getString("projCode"));
                bProject.setProjName(clsManage.chkNull(rs.getString("projName")));
                bProject.setCustCode(clsManage.chkNull(rs.getString("custCode")));
                bProject.setCustName(clsManage.chkNull(rs.getString("custName")));
                bProject.setSaleCode(clsManage.chkNull(rs.getString("saleCode")));
                bProject.setSaleName(clsManage.chkNull(rs.getString("saleName")));
                bProject.setCoorCode(clsManage.chkNull(rs.getString("coorCode")));
                bProject.setCoorName(clsManage.chkNull(rs.getString("coorName")));
                bProject.setProjQty(rs.getString("qty"));
                bProject.setProjQtyT(rs.getString("qtyT"));

                String sPlan = clsManage.chkNull(rs.getString("startPlan"));
                if (sPlan.equals("")) {
                    bProject.setStartProject("");
                } else {
                    String tempStartPlan = rs.getString("startPlan").replace("/", "-");
                    bProject.setStartProject(clsManage.manageYear4DMY(tempStartPlan, -1));
                }

                String ePlan = clsManage.chkNull(rs.getString("endPlan"));
                if (ePlan.equals("")) {
                    bProject.setEndProject("");
                } else {
                    String tempEndPlan = rs.getString("endPlan").replace("/", "-");
                    bProject.setEndProject(clsManage.manageYear4DMY(tempEndPlan, -1));
                }

                bProject.setPriority(clsManage.chkNull(rs.getString("priority")));
                bProject.setPriorityName(clsManage.convPriorityCode2Name(clsManage.chkNull(rs.getString("priority"))));
                bProject.setStatus(clsManage.convStatusCode2Name(clsManage.chkNull(rs.getString("status"))));
                arrList.add(bProject);
                bProject = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_Product_MyProj(String projectCode) {
        try {
            String sql = "SELECT trim(d.proj_code) AS projCode, trim(d.stjplanp_code) AS prodCode, "
                    + " TRIM(d.stjplanp_carno) AS stjplanp_carno, TRIM(d.stjplanp_desc) AS stjplanp_desc, "
                    + " d.stjplanp_startplan, d.stjplanp_endplan, d.stjplanp_startactual, "
                    + " d.stjplanp_endactual, d.stjplanp_type, "
                    + " t.stjtmplh_code, t.stjtmplh_name, d.stjplanp_progress "
                    + " FROM stjplanp AS d "
                    + " LEFT JOIN stjtmplh AS t ON d.stjtmplh_code = t.stjtmplh_code "
                    + " WHERE d.proj_code = '" + projectCode + "' AND (d.stjtmplh_code <> '') "
                    + " ORDER BY d.stjplanp_code ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanProduct bProduct = new beanProduct();

                String color = "blue";
                String tempEndPlan = "";
                String tempEndActual = "";
                String taskProgress = clsManage.chkNull(rs.getString("stjplanp_progress"));

                bProduct.setProjCode(rs.getString("projCode"));
                bProduct.setProdCode(rs.getString("prodCode"));
                bProduct.setProdCarNo(clsManage.chkNull(rs.getString("stjplanp_carno")));
                bProduct.setProdDesc(clsManage.chkNull(rs.getString("stjplanp_desc")));
                bProduct.setProdType(clsManage.chkNull(rs.getString("stjplanp_type")));

                String sPlan = clsManage.chkNull(rs.getString("stjplanp_startplan"));
                if (sPlan.equals("")) {
                    bProduct.setStartTask("");
                } else {
                    String tempStartPlan = rs.getString("stjplanp_startplan").replace("/", "-");
                    bProduct.setStartTask(clsManage.manageYear4DMY(tempStartPlan, -1));
                }

                String ePlan = clsManage.chkNull(rs.getString("stjplanp_endplan"));
                if (ePlan.equals("")) {
                    bProduct.setEndTask("");
                } else {
                    String tEndPlan = rs.getString("stjplanp_endplan").replace("/", "-");
                    bProduct.setEndTask(clsManage.manageYear4DMY(tEndPlan, -1));
                    tempEndPlan = clsManage.convDMY2YYYYMMDD(tEndPlan);
                }

                String sActual = clsManage.chkNull(rs.getString("stjplanp_startactual"));
                if (sActual.equals("")) {
                    bProduct.setStartTaskA("");
                } else {
                    String tStartActual = rs.getString("stjplanp_startactual").replace("/", "-");
                    bProduct.setStartTaskA(clsManage.manageYear4DMY(tStartActual, -1));
                }

                String eActual = clsManage.chkNull(rs.getString("stjplanp_endactual"));
                if (eActual.equals("")) {
                    color = "blue";
                    bProduct.setEndTaskA("");
                    tempEndActual = "0";
                    taskProgress = "0";
                } else {
                    String tEndActual = rs.getString("stjplanp_endactual").replace("/", "-");
                    bProduct.setEndTaskA(clsManage.manageYear4DMY(tEndActual, -1));
                    tempEndActual = clsManage.convDMY2YYYYMMDD(tEndActual);
                }

                if (Integer.parseInt(tempEndActual) == 0) {
                    color = "blue";
                } else if (Integer.parseInt(tempEndActual) > Integer.parseInt(tempEndPlan)) {
                    color = "red";
                } else {
                    color = "green";
                }
                bProduct.setColor(color);
                bProduct.setProgress(taskProgress);
                bProduct.setTemplateCode(clsManage.chkNull(rs.getString("stjtmplh_code")));
                bProduct.setTemplateName(clsManage.chkNull(rs.getString("stjtmplh_Name")));

                int maxProcess = 0;
                int maxTime = 0;
                maxProcess = Get_CountProcess("stjplanp_code", "stjplant", "stjplanp_code", rs.getString("prodCode"), "stjplant_flag");
                maxTime = Get_CountTime("stjplant_time", "stjplant", "stjplanp_code", rs.getString("prodCode"), "stjplant_flag");
                bProduct.setTemplateTime(String.valueOf(maxTime));
                bProduct.setTemplateProcess(String.valueOf(maxProcess));

                arrList.add(bProduct);
                bProduct = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_ProductDetail_MyProj(String productCode) {
        try {
            String sql = "SELECT trim(d.proj_code) AS projCode, trim(d.stjplanp_code) AS prodCode, "
                    + " d.stjplanp_startplan, d.stjplanp_endplan, "
                    + " d.stjplanp_startactual, d.stjplanp_endactual, "
                    + " t.stjtmplh_code, t.stjtmplh_name, d.stjplanp_type, "
                    + " TRIM(d.stjplanp_carno) AS stjplanp_carno, TRIM(d.stjplanp_desc) AS stjplanp_desc "
                    + " FROM stjplanp d "
                    + " LEFT JOIN stjtmplh t on d.stjtmplh_code = t.stjtmplh_code "
                    + " WHERE d.stjplanp_code = '" + productCode + "' "
                    + " ORDER BY d.stjplanp_code ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanProduct bProduct = new beanProduct();
                bProduct.setProjCode(rs.getString("projCode"));
                bProduct.setProdCode(rs.getString("prodCode"));
                bProduct.setStartTask(clsManage.chkNull(rs.getString("stjplanp_startplan")));
                bProduct.setEndTask(clsManage.chkNull(rs.getString("stjplanp_endplan")));
                bProduct.setTemplateCode(clsManage.chkNull(rs.getString("stjtmplh_code")));
                bProduct.setTemplateName(clsManage.chkNull(rs.getString("stjtmplh_name")));
                bProduct.setProdCarNo(clsManage.chkNull(rs.getString("stjplanp_carno")));
                bProduct.setProdDesc(clsManage.chkNull(rs.getString("stjplanp_desc")));
                bProduct.setProdType(clsManage.chkNull(rs.getString("stjplanp_type")));

                String sPlan = clsManage.chkNull(rs.getString("stjplanp_startplan"));
                if (sPlan.equals("")) {
                    bProduct.setStartTask("");
                } else {
                    String tempStartPlan = rs.getString("stjplanp_startplan").replace("/", "-");
                    bProduct.setStartTask(clsManage.manageYear4DMY(tempStartPlan, -1));
                }

                String ePlan = clsManage.chkNull(rs.getString("stjplanp_endplan"));
                if (ePlan.equals("")) {
                    bProduct.setEndTask("");
                } else {
                    String tempEndPlan = rs.getString("stjplanp_endplan").replace("/", "-");
                    bProduct.setEndTask(clsManage.manageYear4DMY(tempEndPlan, -1));
                }

                String sActual = clsManage.chkNull(rs.getString("stjplanp_startactual"));
                if (sActual.equals("")) {
                    bProduct.setStartTask("");
                } else {
                    String tempStartActual = rs.getString("stjplanp_startactual").replace("/", "-");
                    bProduct.setStartTask(clsManage.manageYear4DMY(tempStartActual, -1));
                }

                String eActual = clsManage.chkNull(rs.getString("stjplanp_endactual"));
                if (eActual.equals("")) {
                    bProduct.setEndTask("");
                } else {
                    String tempEndActual = rs.getString("stjplanp_endactual").replace("/", "-");
                    bProduct.setEndTask(clsManage.manageYear4DMY(tempEndActual, -1));
                }

                arrList.add(bProduct);
                bProduct = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_TemplateListBox() {
        try {
            String sql = "SELECT trim(stjtmplh_code) AS stjtmplh_code, trim(stjtmplh_name) AS stjtmplh_name "
                    + " FROM stjtmplh "
                    + " WHERE stjtmplh_flag = 'Y' "
                    + " ORDER BY stjtmplh_name ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanListItem bListItem = new beanListItem();
                bListItem.setText(rs.getString("stjtmplh_code") + " : " + rs.getString("stjtmplh_name"));
                bListItem.setValue(clsManage.chkNull(rs.getString("stjtmplh_code")));
                arrList.add(bListItem);
                bListItem = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_Task_MyProject(String prodCode) {
        try {
            String sql = "SELECT s.stjplant_id, TRIM(s.stjplant_name) AS stjplant_name, "
                    + " s.stjplant_time, s.stjplant_sort, s.stjplant_checkpoint, "
                    + " s.stjplant_startplan, s.stjplant_endplan, "
                    + " TRIM(e1.emp_name) AS e1Name, TRIM(e2.emp_name) AS e2Name, "
                    + " TRIM(e3.emp_name) AS e3Name, TRIM(e4.emp_name) AS e4Name, "
                    + " TRIM(e5.emp_name) AS e5Name, s.stjplant_progress, s.stjplant_endactual "
                    + " FROM stjplant s "
                    + " LEFT JOIN stxemphr e1 ON s.stxemphr_responsible1 = e1.emp_code "
                    + " LEFT JOIN stxemphr e2 ON s.stxemphr_responsible2 = e2.emp_code "
                    + " LEFT JOIN stxemphr e3 ON s.stxemphr_responsible3 = e3.emp_code "
                    + " LEFT JOIN stxemphr e4 ON s.stxemphr_responsible4 = e4.emp_code "
                    + " LEFT JOIN stxemphr e5 ON s.stxemphr_responsible5 = e5.emp_code "
                    + " WHERE s.stjplanp_code = '" + prodCode + "' and s.stjplant_flag = 'Y' "
                    + " ORDER BY s.stjplant_sort";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanTemplate bTemplate = new beanTemplate();
                String color = "blue";
                String tempEndPlan = "";
                String tempEndActual = "";
                String taskProgress = rs.getString("stjplant_progress");

                String taskID = rs.getString("stjplant_id");
                bTemplate.setTaskID(rs.getString("stjplant_id"));
                bTemplate.setTaskName(rs.getString("stjplant_name"));

                String sPlan = clsManage.chkNull(rs.getString("stjplant_startplan"));
                if (sPlan.equals("")) {
                    bTemplate.setTaskStart("");
                    bTemplate.setYYYYMMDD(rs.getString("99999999"));
                } else {
                    String tempStartPlan = rs.getString("stjplant_startplan").replace("/", "-");
                    bTemplate.setTaskStart(clsManage.manageYear4DMY(tempStartPlan, -1));

                    String tempA = clsManage.manageYear4DMY(tempStartPlan, -1); //01-01-2555 > 01-01-2012
                    String tempB = clsManage.convDMY2YYYYMMDD(tempA);
                    bTemplate.setYYYYMMDD(tempB);
                }

                String ePlan = clsManage.chkNull(rs.getString("stjplant_endplan"));
                if (ePlan.equals("")) {
                    bTemplate.setTaskEnd("");
                } else {
                    String tEndPlan = rs.getString("stjplant_endplan").replace("/", "-");
                    bTemplate.setTaskEnd(clsManage.manageYear4DMY(tEndPlan, -1));
                    tempEndPlan = clsManage.convDMY2YYYYMMDD(tEndPlan);
                }

                String eActual = clsManage.chkNull(rs.getString("stjplant_endactual"));
                if (eActual.equals("")) {
                    color = "blue";
                    tempEndActual = "0";
                    taskProgress = "0";
                } else {
                    String tEndActual = rs.getString("stjplant_endactual").replace("/", "-");
                    tempEndActual = clsManage.convDMY2YYYYMMDD(tEndActual);
                }

                if (Integer.parseInt(tempEndActual) == 0) {
                    color = "blue";
                } else if (Integer.parseInt(tempEndActual) > Integer.parseInt(tempEndPlan)) {
                    color = "red";
                } else {
                    color = "green";
                }

                bTemplate.setTaskTime(rs.getString("stjplant_time"));
                bTemplate.setTaskSort(rs.getString("stjplant_sort"));
                bTemplate.setTaskCheckPoint(clsManage.convChkPointCode2Name(rs.getString("stjplant_checkpoint")));
                bTemplate.setTaskResponsible1(rs.getString("e1Name"));
                bTemplate.setTaskResponsible2(rs.getString("e2Name"));
                bTemplate.setTaskResponsible3(rs.getString("e3Name"));
                bTemplate.setTaskResponsible4(rs.getString("e4Name"));
                bTemplate.setTaskResponsible5(rs.getString("e5Name"));
                bTemplate.setTaskProgress(taskProgress);
                bTemplate.setColor(color);
                bTemplate.setCountLog(Get_CountLog(taskID));
                arrList.add(bTemplate);
                bTemplate = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_Log_MyProject(String prodCode, String taskCode) {
        try {
            String sql = "SELECT t.stjplant_endplan, l.* "
                    + " FROM stjplanl l "
                    + " LEFT JOIN stjplant t ON l.stjplant_id = t.stjplant_id "
                    + " WHERE l.stjplanp_code = '" + prodCode + "' AND l.stjplant_id = '" + taskCode + "' "
                    + " ORDER BY l.stjplanl_workdate, l.stjplanl_progress";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanLog bLog = new beanLog();
                String color = "";
                String tempEndPlan = "";
                String tempStartActual = "";
                bLog.setProdCode(rs.getString("stjplanp_code"));
                bLog.setTaskID(rs.getString("stjplant_id"));

                String ePlan = clsManage.chkNull(rs.getString("stjplant_endplan"));
                if (ePlan.equals("")) {
                    bLog.setWorkDate("");
                } else {
                    String tEndPlan = rs.getString("stjplant_endplan").replace("/", "-");
                    tempEndPlan = clsManage.convDMY2YYYYMMDD(tEndPlan);
                }

                String sActual = clsManage.chkNull(rs.getString("stjplanl_workdate"));
                if (sActual.equals("")) {
                    bLog.setWorkDate("");
                } else {
                    String startActual = rs.getString("stjplanl_workdate").replace("/", "-");
                    tempStartActual = clsManage.convDMY2YYYYMMDD(startActual);
                    bLog.setWorkDate(clsManage.manageYear4DMY(startActual, -1));
                }

                if (Integer.parseInt(tempStartActual) > Integer.parseInt(tempEndPlan)) {
                    color = "red";
                } else {
                    color = "green";
                }

                bLog.setComment(rs.getString("stjplanl_comment"));
                bLog.setProgress(rs.getString("stjplanl_progress"));
                bLog.setColor(color);
                arrList.add(bLog);
                bLog = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_File_MyProject(String prodCode, String taskCode) {
        try {
            String sql = "SELECT TRIM(o.stjplanp_code) AS prodCode, o.stjplant_id, "
                    + " TRIM(o.stjplano_name) AS docName, TRIM(o.stjplano_file) AS docFile, "
                    + " o.stjplano_type, o.stjplano_download"
                    + " FROM stjplano AS o "
                    + " LEFT JOIN stjplant AS t ON o.stjplant_id = t.stjplant_id "
                    + " WHERE o.stjplanp_code = '" + prodCode + "' AND o.stjplant_id = '" + taskCode + "' "
                    + " ORDER BY o.stjplano_createdate";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanFile bFile = new beanFile();
                bFile.setProdCode(rs.getString("prodCode"));
                bFile.setTaskID(rs.getString("stjplant_id"));
                bFile.setDocName(rs.getString("docName"));
                bFile.setDocFile(rs.getString("docFile"));
                bFile.setDocType(clsManage.convDocType(rs.getString("stjplano_type")));
                bFile.setDocCount(rs.getString("stjplano_download"));
                arrList.add(bFile);
                bFile = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String[] Get_HeadLog_MyProject(String taskCode) {
        try {
            String sql = "SELECT s.stjplant_id, s.stjplant_sort, TRIM(s.stjplant_name) AS stjplant_name, "
                    + " s.stjplant_startplan, s.stjplant_endplan, s.stjplant_time, "
                    + " TRIM(e1.emp_name) AS e1Name, TRIM(e2.emp_name) AS e2Name, "
                    + " TRIM(e3.emp_name) AS e3Name, TRIM(e4.emp_name) AS e4Name, "
                    + " TRIM(e5.emp_name) AS e5Name, s.stjplant_progress "
                    + " FROM stjplant AS s "
                    + " LEFT JOIN stxemphr e1 ON s.stxemphr_responsible1 = e1.emp_code "
                    + " LEFT JOIN stxemphr e2 ON s.stxemphr_responsible2 = e2.emp_code "
                    + " LEFT JOIN stxemphr e3 ON s.stxemphr_responsible3 = e3.emp_code "
                    + " LEFT JOIN stxemphr e4 ON s.stxemphr_responsible4 = e4.emp_code "
                    + " LEFT JOIN stxemphr e5 ON s.stxemphr_responsible5 = e5.emp_code "
                    + " WHERE s.stjplant_id = '" + taskCode + "' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            String[] arr_login = new String[12];

            while (rs.next()) {
                arr_login[0] = clsManage.chkNull(rs.getString("stjplant_id"));
                arr_login[1] = clsManage.chkNull(rs.getString("stjplant_name"));
                String startPlan = rs.getString("stjplant_startplan").replace("/", "-");
                arr_login[2] = clsManage.chkNull(clsManage.manageYear4DMY(startPlan, -1));
                String endPlan = rs.getString("stjplant_endplan").replace("/", "-");
                arr_login[3] = clsManage.chkNull(clsManage.manageYear4DMY(endPlan, -1));
                arr_login[4] = clsManage.chkNull(rs.getString("stjplant_time"));
                arr_login[5] = clsManage.chkNull(rs.getString("e1Name"));
                arr_login[6] = clsManage.chkNull(rs.getString("e2Name"));
                arr_login[7] = clsManage.chkNull(rs.getString("e3Name"));
                arr_login[8] = clsManage.chkNull(rs.getString("e4Name"));
                arr_login[9] = clsManage.chkNull(rs.getString("e5Name"));
                arr_login[10] = clsManage.chkNull(rs.getString("stjplant_progress"));
                arr_login[11] = clsManage.chkNull(rs.getString("stjplant_sort"));
            }

            cConnect.Close();
            return arr_login;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String[] Get_HeadAddLog_MyProject(String taskCode) {
        try {
            String sql = "SELECT TRIM(m.proj_code) AS projCode, TRIM(m.proj_lname) AS projName, "
                    + " TRIM(p.stjplanp_code) AS prodCode, "
                    + " TRIM(c.cust_code) AS custCode, TRIM(c.cust_name) AS custName, "
                    + " t.stjplant_id AS taskCode, TRIM(t.stjplant_name) AS taskName "
                    + " FROM stjplant AS t "
                    + " LEFT JOIN stjplanp AS p ON t.stjplanp_code = p.stjplanp_code "
                    + " LEFT JOIN stjmasth AS m ON p.proj_code = m.proj_code "
                    + " LEFT JOIN stxcustr AS c ON m.cust_code = c.cust_code "
                    + " WHERE t.stjplant_id = '" + taskCode + "' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            String[] arr_login = new String[7];

            while (rs.next()) {
                arr_login[0] = clsManage.chkNull(rs.getString("projCode"));
                arr_login[1] = clsManage.chkNull(rs.getString("projName"));
                arr_login[2] = clsManage.chkNull(rs.getString("prodCode"));
                arr_login[3] = clsManage.chkNull(rs.getString("custCode"));
                arr_login[4] = clsManage.chkNull(rs.getString("custName"));
                arr_login[5] = clsManage.chkNull(rs.getString("taskCode"));
                arr_login[6] = clsManage.chkNull(rs.getString("taskName"));
            }

            cConnect.Close();
            return arr_login;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getTemplateDetailMyProject(String templateCode) {
        try {
            String sql = "SELECT stjtmplh_code, stjtmplh_name, stjtmplh_type"
                    + " FROM stjtmplh "
                    + " WHERE stjtmplh_code = '" + templateCode + "' and stjtmplh_flag = 'Y' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanTemplate bTemplate = new beanTemplate();
                bTemplate.setTemplateCode(rs.getString("stjtmplh_code"));
                bTemplate.setTemplateName(rs.getString("stjtmplh_name"));
                bTemplate.setTemplateTypeCode(rs.getString("stjtmplh_type"));
                bTemplate.setTemplateTypeName(clsManage.convProductCode2Name(rs.getString("stjtmplh_type")));
                arrList.add(bTemplate);
                bTemplate = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int Set_LogAdd_MyProject(String taskID, String projCode, String prodCode, String logDate, String logProgress, String logComment, String manageBy) {
        try {
            //หา Max
            int code = getMaxID("stjplanl_id", "stjplanl");

            String lDate = "";
            if (!clsManage.chkNull(logDate).equals("")) {
                lDate = logDate.replace("-", "/");
            }

            String sql = "INSERT INTO stjplanl (stjplanl_id, stjplanp_code, stjplant_id, stjplanl_comment, stjplanl_workdate, stjplanl_progress, stxemphr_createid, stjplanl_createdate) "
                    + "VALUES ('" + code + "','" + prodCode + "','" + taskID + "','" + logComment + "','" + lDate + "','" + logProgress + "','" + manageBy + "',CURRENT)";
            clsConnect cConnect = new clsConnect();
            int res = cConnect.ExecuteQuery(sql);
            cConnect.Close();
            setLog(manageBy, "Insert", "METPlan", "Insert stjplanl: " + code, "My Project:Set_LogAdd_MyProject", res);

            String startPlan = "";
            String endPlan = "";
            String sqlProgress = "SELECT MIN(stjplanl_workdate) AS startProject, MAX(stjplanl_workdate) AS endProject FROM stjplanl WHERE stjplanp_code = '" + prodCode + "' AND stjplant_id = '" + taskID + "' ";
            clsConnect cConnProgress = new clsConnect();
            ResultSet rsProgress = cConnProgress.getResultSet(sqlProgress);
            while (rsProgress.next()) {
                String tempStartPlan = rsProgress.getString("startProject").replace("/", "-");
                tempStartPlan = clsManage.manageYear4DMY(tempStartPlan, -1);
                startPlan = tempStartPlan.replace("-", "/");

                String tempEndPlan = rsProgress.getString("endProject").replace("/", "-");
                tempEndPlan = clsManage.manageYear4DMY(tempEndPlan, -1);
                endPlan = tempEndPlan.replace("-", "/");
            }
            if (logProgress.equals("100")) {
                //update task เปลี่ยน status เป็น S
                String sqlProgressUp = "UPDATE stjplant SET stjplant_startactual = '" + startPlan + "', stjplant_endactual = '" + endPlan + "', stjplant_status = 'S', stjplant_progress = '100' WHERE stjplant_id = '" + taskID + "' AND stjplanp_code = '" + prodCode + "' ";
                clsConnect cConnProgressUp = new clsConnect();
                int resProgressUp = cConnProgressUp.ExecuteQuery(sqlProgressUp);
                cConnect.Close();
                setLog(manageBy, "Update", "METPlan", "Update stjplant: " + prodCode, "My Project:Set_LogAdd_MyProject", resProgressUp);

            } else {
                //update task เปลี่ยน status เป็น o
                String sqlProgressUp = "UPDATE stjplant SET stjplant_startactual = '" + startPlan + "', stjplant_endactual = '" + endPlan + "', stjplant_status = 'O', stjplant_progress = '" + logProgress + "' WHERE stjplant_id = '" + taskID + "' AND stjplanp_code = '" + prodCode + "' ";
                clsConnect cConnProgressUp = new clsConnect();
                int resProgressUp = cConnProgressUp.ExecuteQuery(sqlProgressUp);
                cConnect.Close();
                setLog(manageBy, "Update", "METPlan", "Update stjplant: " + prodCode, "My Project:Set_LogAdd_MyProject", resProgressUp);
            }

            //หา %Progress ของ Product ให้หาที่ Task
            int progressTask = 0;
            String sqlMaxTask = "SELECT COUNT(stjplanp_code) AS countTask, SUM(stjplant_progress) AS sumProgressTask FROM stjplant WHERE stjplanp_code = '" + prodCode + "' AND stjplant_status <> 'C'";
            clsConnect cConnMaxTask = new clsConnect();
            ResultSet rsMaxTask = cConnMaxTask.getResultSet(sqlMaxTask);
            while (rsMaxTask.next()) {
                int cTask = rsMaxTask.getInt("countTask");
                int sumTask = rsMaxTask.getInt("sumProgressTask");
                progressTask = sumTask / cTask;
            }

            startPlan = "";
            endPlan = "";

            String sqlProgressTask = "SELECT MIN(stjplant_startactual) AS startProject, MAX(stjplant_endactual) AS endProject FROM stjplant WHERE stjplanp_code = '" + prodCode + "' AND stjplant_status <> 'C' ";
            clsConnect cConnProgressTask = new clsConnect();
            ResultSet rsProgressTask = cConnProgressTask.getResultSet(sqlProgressTask);
            while (rsProgressTask.next()) {
                String tempStartPlan = rsProgressTask.getString("startProject").replace("/", "-");
                tempStartPlan = clsManage.manageYear4DMY(tempStartPlan, -1);
                startPlan = tempStartPlan.replace("-", "/");

                String tempEndPlan = rsProgressTask.getString("endProject").replace("/", "-");
                tempEndPlan = clsManage.manageYear4DMY(tempEndPlan, -1);
                endPlan = tempEndPlan.replace("-", "/");
            }

            String sqlProgressUp = "UPDATE stjplanp SET stjplanp_startactual = '" + startPlan + "', stjplanp_endactual = '" + endPlan + "', stjplanp_progress = '" + progressTask + "' WHERE stjplanp_code = '" + prodCode + "' ";
            clsConnect cConnProgressUp = new clsConnect();
            int resProgressUp = cConnProgressUp.ExecuteQuery(sqlProgressUp);
            cConnect.Close();
            setLog(manageBy, "Update", "METPlan", "Update stjplanp: " + prodCode, "My Project:Set_LogAdd_MyProject", resProgressUp);

            //หา %Progress ของ Project ให้หาที่ Product
            int progressProd = 0;
            String sqlMaxProd = "SELECT COUNT(proj_code) AS countProd, SUM(stjplanp_progress) AS sumProgressProd FROM stjplanp WHERE proj_code = '" + projCode + "'";
            clsConnect cConnMaxProd = new clsConnect();
            ResultSet rsMaxProd = cConnMaxProd.getResultSet(sqlMaxProd);
            while (rsMaxProd.next()) {
                int cProd = rsMaxProd.getInt("countProd");
                int sumProd = rsMaxProd.getInt("sumProgressProd");
                progressProd = sumProd / cProd;
            }

            //เปลี่ยน Status ใน stjmasth
            startPlan = "";
            endPlan = "";
            String sqlProgressProduct = "SELECT MIN(stjplanp_startactual) AS startProject, MAX(stjplanp_endactual) AS endProject FROM stjplanp WHERE proj_code = '" + projCode + "'";
            clsConnect cConnProgressProduct = new clsConnect();
            ResultSet rsProgressProduct = cConnProgressProduct.getResultSet(sqlProgressProduct);
            while (rsProgressProduct.next()) {
                String tempStartPlan = rsProgressProduct.getString("startProject").replace("/", "-");
                tempStartPlan = clsManage.manageYear4DMY(tempStartPlan, -1);
                startPlan = tempStartPlan.replace("-", "/");

                String tempEndPlan = rsProgressProduct.getString("endProject").replace("/", "-");
                tempEndPlan = clsManage.manageYear4DMY(tempEndPlan, -1);
                endPlan = tempEndPlan.replace("-", "/");
            }

            String statusProj = "O";
            if (progressProd == 100) {
                statusProj = "S";
            } else {
                statusProj = "O";
            }

            String sqlProgressProject = "UPDATE stjmasth SET stjmasth_status = '" + statusProj + "', stjmasth_startactual = '" + startPlan + "', stjmasth_endactual = '" + endPlan + "', stjmasth_progress = '" + progressProd + "' WHERE proj_code = '" + projCode + "' ";
            clsConnect cConnProgressProject = new clsConnect();
            int resProgressProject = cConnProgressProject.ExecuteQuery(sqlProgressProject);
            cConnect.Close();
            setLog(manageBy, "Update", "METPlan", "Update stjplanp: " + prodCode, "My Project:Set_LogAdd_MyProject", resProgressProject);

            return res;
        } catch (Exception ex) {
            return -1;
        }
    }

    public int Set_FileAdd_MyProject(String taskID, String projCode, String prodCode, String fileName, String fileType, String fileDoc, String fileLink, String manageBy) {
        try {
            //หา Max
            int code = getMaxID("stjplano_id", "stjplano");

            String name = "";
            if (fileType.equals("D")) {
                name = fileDoc;
            } else {
                name = fileLink;
            }

            String sql = "INSERT INTO stjplano (stjplano_id, stjplanp_code, stjplant_id, stjplano_name, stjplano_file, stjplano_type, stjplano_download, stxemphr_createid, stjplano_createdate) "
                    + "VALUES ('" + code + "','" + prodCode + "','" + taskID + "','" + fileName + "','" + name + "','" + fileType + "','0','" + manageBy + "',CURRENT)";
            clsConnect cConnect = new clsConnect();
            int res = cConnect.ExecuteQuery(sql);
            cConnect.Close();
            setLog(manageBy, "Insert", "METPlan", "Insert stjplano: " + code, "My Project:Set_FileAdd_MyProject", res);

            return res;
        } catch (Exception ex) {
            return -1;
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Assign Coordinator">
    public List Get_ProjectCount_AssCoor(String searchFrom, String search, String searchSign, String searchDate, String searchPriority, String searchStatus) {
        try {
            String where = "1=1 AND d.stjplanp_tosite >= '01/04/2012'";
            if (!searchFrom.equals("")) {
                if (searchFrom.equals("Proj")) {
                    where = where + " AND (p.proj_code LIKE '%" + search + "%' OR p.proj_lname LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Cust")) {
                    where = where + " AND (p.cust_code LIKE '%" + search + "%' OR c.cust_name LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Sale")) {
                    where = where + " AND (p.sales_code LIKE '%" + search + "%' OR s.emp_name LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Coor")) {
                    where = where + " AND (p.stxemphr_coordinator LIKE '%" + search + "%' OR e.emp_name LIKE '%" + search + "%') ";
                }
            }
            if (searchStatus.equals("")) {
                where = where + " AND (p.stjmasth_status = '" + searchStatus + "' OR p.stjmasth_status IS NULL) ";
            } else {
                where = where + " AND p.stjmasth_status = '" + searchStatus + "' ";
            }
            if (!searchPriority.equals("")) {
                where = where + " AND p.stjmasth_priority = '" + searchPriority + "' ";
            }
            if (!searchDate.equals("")) {
                String dateDMY = searchDate.replace("-", "/");
                where = where + " AND p.stjmasth_startplan " + searchSign + " '" + dateDMY + "' ";
            }
            String sql = "SELECT COUNT(p.proj_code) AS countRows "
                    + " FROM stjmasth p "
                    + " LEFT JOIN stjplanp d ON p.proj_code = d.proj_code "
                    + " LEFT JOIN stxcustr c ON p.cust_code = c.cust_code "
                    + " LEFT JOIN stxemphr s ON p.sales_code = s.emp_code "
                    + " LEFT JOIN stxemphr e ON p.stxemphr_coordinator = e.emp_code"
                    + " WHERE " + where;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("countRows")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_Project_AssCoor(String searchFrom, String search, String searchSign, String searchDate, String searchPriority, String searchStatus, String sortField, String sortDescAsc, int startRow, String limit) {
        try {
            String where = "1=1 AND d.stjplanp_tosite >= '01/04/2012'";
            if (!searchFrom.equals("")) {
                if (searchFrom.equals("Proj")) {
                    where = where + " AND (p.proj_code LIKE '%" + search + "%' OR p.proj_lname LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Cust")) {
                    where = where + " AND (p.cust_code LIKE '%" + search + "%' OR c.cust_name LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Sale")) {
                    where = where + " AND (p.sales_code LIKE '%" + search + "%' OR s.emp_name LIKE '%" + search + "%') ";
                } else if (searchFrom.equals("Coor")) {
                    where = where + " AND (p.stxemphr_coordinator LIKE '%" + search + "%' OR e.emp_name LIKE '%" + search + "%') ";
                }
            }
            if (searchStatus.equals("")) {
                where = where + " AND (p.stjmasth_status = '" + searchStatus + "' OR p.stjmasth_status IS NULL) ";
            } else {
                where = where + " AND p.stjmasth_status = '" + searchStatus + "' ";
            }
            if (!searchPriority.equals("")) {
                where = where + " AND p.stjmasth_priority = '" + searchPriority + "' ";
            }
            if (!searchDate.equals("")) {
                String dateDMY = searchDate.replace("-", "/");
                where = where + " AND p.stjmasth_startplan " + searchSign + " '" + dateDMY + "' ";
            }
            String sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
                    + " trim(p.proj_code) AS projCode, trim(p.proj_lname) AS projName, "
                    + " p.stjmasth_startplan AS startPlan, p.stjmasth_endplan AS endPlan, "
                    + " p.stjmasth_status AS status, p.stjmasth_priority AS priority, "
                    + " trim(c.cust_code) AS custCode, trim(c.cust_name) AS custName, "
                    + " trim(s.emp_code) AS saleCode, trim(s.emp_name) AS saleName, "
                    + " trim(e.emp_code) AS coorCode, trim(e.emp_name) AS coorName, "
                    + " count(l.proj_code) AS qty "
                    + " FROM stjmasth p "
                    + " LEFT JOIN stjplanp d ON p.proj_code = d.proj_code "
                    + " LEFT JOIN stjplanp l ON p.proj_code = l.proj_code "
                    + " LEFT JOIN stxcustr c ON p.cust_code = c.cust_code "
                    + " LEFT JOIN stxemphr s ON p.sales_code = s.emp_code "
                    + " LEFT JOIN stxemphr e ON p.stxemphr_coordinator = e.emp_code"
                    + " WHERE " + where
                    + " GROUP BY p.proj_code, p.proj_lname, p.stjmasth_startplan, p.stjmasth_endplan, "
                    + " p.stjmasth_status, p.stjmasth_priority, c.cust_code, c.cust_name, "
                    + " s.emp_code, s.emp_name, e.emp_code, e.emp_name"
                    + " ORDER BY " + sortField + " " + sortDescAsc;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanProject bProject = new beanProject();
                bProject.setProjCode(rs.getString("projCode"));
                bProject.setProjName(clsManage.chkNull(rs.getString("projName")));
                bProject.setCustCode(clsManage.chkNull(rs.getString("custCode")));
                bProject.setCustName(clsManage.chkNull(rs.getString("custName")));
                bProject.setSaleCode(clsManage.chkNull(rs.getString("saleCode")));
                bProject.setSaleName(clsManage.chkNull(rs.getString("saleName")));
                bProject.setCoorCode(clsManage.chkNull(rs.getString("coorCode")));
                bProject.setCoorName(clsManage.chkNull(rs.getString("coorName")));
                bProject.setProjQty(rs.getString("qty"));

                String sPlan = clsManage.chkNull(rs.getString("startPlan"));
                if (sPlan.equals("")) {
                    bProject.setStartProject("");
                } else {
                    String tempStartPlan = rs.getString("startPlan").replace("/", "-");
                    bProject.setStartProject(clsManage.manageYear4DMY(tempStartPlan, -1));
                }

                String ePlan = clsManage.chkNull(rs.getString("endPlan"));
                if (ePlan.equals("")) {
                    bProject.setEndProject("");
                } else {
                    String tempEndPlan = rs.getString("endPlan").replace("/", "-");
                    bProject.setEndProject(clsManage.manageYear4DMY(tempEndPlan, -1));
                }

                bProject.setPriority(clsManage.convPriorityCode2Name(clsManage.chkNull(rs.getString("priority"))));
                bProject.setStatus(clsManage.convStatusCode2Name(clsManage.chkNull(rs.getString("status"))));
                arrList.add(bProject);
                bProject = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_ProjectDetail_AssCoor(String Proj) {
        try {
            String sql = "SELECT trim(p.proj_code) AS projCode, trim(p.proj_lname) AS projName, "
                    + " p.stjmasth_priority AS priority, p.stjmasth_status AS status, "
                    + " trim(c.cust_code) AS custCode, trim(c.cust_name) AS custName, "
                    + " trim(s.emp_code) AS saleCode, trim(s.emp_name) AS saleName, "
                    + " trim(e.emp_code) AS coorCode, trim(e.emp_name) AS coorName, "
                    + " p.stjmasth_startplan AS startPlan, p.stjmasth_endplan AS endPlan, "
                    + " count(l.proj_code) AS qty "
                    + " FROM stjmasth p "
                    + " LEFT JOIN stjplanp l ON p.proj_code = l.proj_code "
                    + " LEFT JOIN stxcustr c ON p.cust_code = c.cust_code "
                    + " LEFT JOIN stxemphr s ON p.sales_code = s.emp_code "
                    + " LEFT JOIN stxemphr e ON p.stxemphr_coordinator = e.emp_code"
                    + " WHERE p.proj_code = '" + Proj + "' "
                    + " GROUP BY p.proj_code, p.proj_lname, "
                    + " p.stjmasth_priority, p.stjmasth_status, "
                    + " c.cust_code, c.cust_name, "
                    + " s.emp_code, s.emp_name, e.emp_code, e.emp_name, "
                    + " p.stjmasth_startplan, p.stjmasth_endplan, "
                    + " p.stjmasth_unit, p.stjmasth_task ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanProject bProject = new beanProject();
                bProject.setProjCode(rs.getString("projCode"));
                bProject.setProjName(clsManage.chkNull(rs.getString("projName")));
                bProject.setCustCode(clsManage.chkNull(rs.getString("custCode")));
                bProject.setCustName(clsManage.chkNull(rs.getString("custName")));
                bProject.setSaleCode(clsManage.chkNull(rs.getString("saleCode")));
                bProject.setSaleName(clsManage.chkNull(rs.getString("saleName")));
                bProject.setCoorCode(clsManage.chkNull(rs.getString("coorCode")));
                bProject.setCoorName(clsManage.chkNull(rs.getString("coorName")));
                bProject.setProjQty(rs.getString("qty"));

                String sPlan = clsManage.chkNull(rs.getString("startPlan"));
                if (sPlan.equals("")) {
                    bProject.setStartProject("");
                } else {
                    String tempStartPlan = rs.getString("startPlan").replace("/", "-");
                    bProject.setStartProject(clsManage.manageYear4DMY(tempStartPlan, -1));
                }

                String ePlan = clsManage.chkNull(rs.getString("endPlan"));
                if (ePlan.equals("")) {
                    bProject.setEndProject("");
                } else {
                    String tempEndPlan = rs.getString("endPlan").replace("/", "-");
                    bProject.setEndProject(clsManage.manageYear4DMY(tempEndPlan, -1));
                }

                bProject.setPriority(clsManage.chkNull(rs.getString("priority")));
                bProject.setPriorityName(clsManage.convPriorityCode2Name(clsManage.chkNull(rs.getString("priority"))));
                bProject.setStatus(clsManage.convStatusCode2Name(clsManage.chkNull(rs.getString("status"))));
                arrList.add(bProject);
                bProject = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int Set_Coordinator(String projCode, String coordinator, String startProject, String endProject, String priority, String coorCode, String manageBy) {
        try {

            clsConnect cConnect = new clsConnect();

            String varStartProject = "";
            if (!clsManage.chkNull(startProject).equals("")) {
                varStartProject = startProject.replace("-", "/");
            }
            String varEndProject = "";
            if (!clsManage.chkNull(endProject).equals("")) {
                varEndProject = endProject.replace("-", "/");
            }

            int cUnit = 0;
            //Count Product
            String sqlProd = "SELECT COUNT(proj_code) AS countUnit FROM stjplanp WHERE proj_code = '" + projCode + "' ";
            ResultSet rs = cConnect.getResultSet(sqlProd);
            while (rs.next()) {
                cUnit = Integer.parseInt(rs.getString("countUnit"));
            }

            String sql = "";
            if (coorCode.equals("")) {
                sql = "UPDATE stjmasth SET stxemphr_coordinator = '" + coordinator + "', stjmasth_startplan = '" + varStartProject + "', stjmasth_endplan = '" + varEndProject + "', "
                        + " stjmasth_priority = '" + priority + "', stjmasth_unit = " + cUnit + ", stjmasth_status = 'N', stjmasth_task = 0, "
                        + " stxemphr_createid = '" + manageBy + "', stjmasth_createdate = CURRENT, stxemphr_updateid = '" + manageBy + "', stjmasth_updatedate = CURRENT"
                        + " WHERE proj_code = '" + projCode + "'";
            } else {
                sql = "UPDATE stjmasth SET stxemphr_coordinator = '" + coordinator + "', stjmasth_startplan = '" + varStartProject + "', stjmasth_endplan = '" + varEndProject + "', "
                        + " stjmasth_priority = '" + priority + "', stjmasth_unit = " + cUnit + ", stjmasth_status = 'N', "
                        + " stxemphr_updateid = '" + manageBy + "', stjmasth_updatedate = CURRENT"
                        + " WHERE proj_code = '" + projCode + "'";
            }
            int res = cConnect.ExecuteQuery(sql);
            cConnect.Close();

            //Log
            setLog(manageBy, "UPDATE", "METPlan", "Update stjmasth: " + projCode, "ManageProject:updateCoordinator", res);
            //End Log

            return res;
        } catch (Exception ex) {
            return 99999;
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Template & Task">
    public List Get_Template(String searchTemplate, String searchType, String sortField, String sortDescAsc, String startRow, String limit) {
        try {
            String where = "stjtmplh_flag = 'Y'";
            if (!searchTemplate.equals("")) {
                where = where + " AND stjtmplh_name LIKE '%" + searchTemplate + "%' ";
            }
            if (!searchType.equals("")) {
                where = where + " AND stjtmplh_type = '" + searchType + "' ";
            }
            String sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
                    + " stjtmplh_code, stjtmplh_name, "
                    + " stjtmplh_type "
                    + " FROM stjtmplh "
                    + " WHERE " + where
                    + " ORDER BY " + sortField + " " + sortDescAsc;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanTemplate bTemplate = new beanTemplate();
                String templateCode = rs.getString("stjtmplh_code");
                bTemplate.setTemplateCode(rs.getString("stjtmplh_code"));
                bTemplate.setTemplateName(rs.getString("stjtmplh_name"));
                int maxTime = Get_CountTime("stjtmpld_time", "stjtmpld", "stjtmplh_code", templateCode, "stjtmpld_flag");
                bTemplate.setTemplateTime(String.valueOf(maxTime));
                int maxProcess = Get_CountProcess("stjtmplh_code", "stjtmpld", "stjtmplh_code", templateCode, "stjtmpld_flag");
                bTemplate.setTemplateProcess(String.valueOf(maxProcess));
                bTemplate.setTemplateTypeCode(rs.getString("stjtmplh_type"));
                bTemplate.setTemplateTypeName(clsManage.convProductCode2Name(rs.getString("stjtmplh_type")));
                arrList.add(bTemplate);
                bTemplate = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_TemplateCount(String searchTemplate, String searchType) {
        try {
            String where = "stjtmplh_flag = 'Y'";
            if (!searchTemplate.equals("")) {
                where = where + " AND stjtmplh_name LIKE '%" + searchTemplate + "%' ";
            }
            if (!searchType.equals("")) {
                where = where + " AND stjtmplh_type = '" + searchType + "' ";
            }
            String sql = "SELECT COUNT(stjtmplh_code) AS countRows "
                    + " FROM stjtmplh "
                    + " WHERE " + where;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("countRows")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int Set_TemplateAdd(String templateName, String templateType, String manageBy) {
        try {
            //หา Max
            String code = Get_TemplateMax(templateType);

            String sql = "INSERT INTO stjtmplh (stjtmplh_code, stjtmplh_name, stjtmplh_type, stjtmplh_time, stjtmplh_process, stjtmplh_flag, stxemphr_createid, stjtmplh_createdate, stxemphr_updateid, stjtmplh_updatedate) "
                    + "VALUES ('" + code + "','" + templateName + "','" + templateType + "','5','5','Y','" + manageBy + "',CURRENT,'" + manageBy + "',CURRENT)";
            clsConnect cConnect = new clsConnect();
            int res = cConnect.ExecuteQuery(sql);
            cConnect.Close();

            //Log
            setLog(manageBy, "Insert", "METPlan", "Insert stjtmplh: " + code, "Manage Template:setTemplate", res);
            //End Log

            //Production
            Set_TaskAdd(code, "Production", 1, 1, "PD", "", "", "", "", "", manageBy);

            //Ship Date
            Set_TaskAdd(code, "Ship Date", 1, 2, "SH", "", "", "", "", "", manageBy);

            //Start Project
            Set_TaskAdd(code, "First-CO", 1, 3, "SP", "", "", "", "", "", manageBy);

            //To Site Date
            Set_TaskAdd(code, "To Site Date", 1, 4, "TS", "", "", "", "", "", manageBy);

            //Finish Project
            Set_TaskAdd(code, "Finish Project", 1, 5, "FP", "", "", "", "", "", manageBy);

            return res;
        } catch (Exception ex) {
            return -1;
        }
    }

    public int Set_TemplateEdit(String templateCode, String templateName, String templateType, String manageBy) {
        try {
            String sql = "UPDATE stjtmplh SET stjtmplh_name = '" + templateName + "', stxemphr_updateid='" + manageBy + "', stjtmplh_updatedate = CURRENT "
                    + "WHERE stjtmplh_code = '" + templateCode + "' ";
            clsConnect cConnect = new clsConnect();
            int res = cConnect.ExecuteQuery(sql);
            cConnect.Close();

            //Log
            setLog(manageBy, "Update", "METPlan", "Update stjtmplh: " + templateCode, "Manage Template:editTemplate", res);
            //End Log

            return res;
        } catch (Exception ex) {
            return -1;
        }
    }

    public int Set_TemplateDel(String templateCode, String manageBy) {
        try {
            String sql = "UPDATE stjtmplh SET stjtmplh_flag = 'N', stxemphr_updateid = '" + manageBy + "', stjtmplh_updatedate = CURRENT "
                    + "WHERE stjtmplh_code = '" + templateCode + "' ";
            clsConnect cConnect = new clsConnect();
            int res = cConnect.ExecuteQuery(sql);
            cConnect.Close();

            //Log
            setLog(manageBy, "Delete", "METPlan", "Delete stjtmplh: " + templateCode, "Manage Template:delTemplate", res);
            //End Log

            //Del Task
            String sqlTask = "UPDATE stjtmpld SET stjtmpld_flag = 'N', stxemphr_updateid = '" + manageBy + "', stjtmpld_updatedate = CURRENT "
                    + "WHERE stjtmplh_code = '" + templateCode + "' ";
            clsConnect cConnectTask = new clsConnect();
            int resTask = cConnectTask.ExecuteQuery(sqlTask);
            cConnectTask.Close();

            //Log
            setLog(manageBy, "Delete", "METPlan", "Delete stjtmpld: " + templateCode, "Manage Template:delTemplate", resTask);
            //End Log
            //End Del Task

            return res;
        } catch (Exception ex) {
            return -1;
        }
    }

    public List Get_Task(String templateCode) {
        try {
            String sql = "SELECT s.stjtmpld_id, s.stjtmpld_name, "
                    + " s.stjtmpld_time, s.stjtmpld_sort, s.stjtmpld_checkpoint, "
                    + " TRIM(e1.emp_name) AS e1Name, TRIM(e2.emp_name) AS e2Name, "
                    + " TRIM(e3.emp_name) AS e3Name, TRIM(e4.emp_name) AS e4Name, "
                    + " TRIM(e5.emp_name) AS e5Name "
                    + " FROM stjtmpld s "
                    + " LEFT JOIN stxemphr e1 ON s.stxemphr_responsible1 = e1.emp_code "
                    + " LEFT JOIN stxemphr e2 ON s.stxemphr_responsible2 = e2.emp_code "
                    + " LEFT JOIN stxemphr e3 ON s.stxemphr_responsible3 = e3.emp_code "
                    + " LEFT JOIN stxemphr e4 ON s.stxemphr_responsible4 = e4.emp_code "
                    + " LEFT JOIN stxemphr e5 ON s.stxemphr_responsible5 = e5.emp_code "
                    + " WHERE s.stjtmplh_code = '" + templateCode + "' and s.stjtmpld_flag = 'Y' "
                    + " ORDER BY s.stjtmpld_sort";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanTemplate bTemplate = new beanTemplate();
                bTemplate.setTaskID(rs.getString("stjtmpld_id"));
                bTemplate.setTaskName(rs.getString("stjtmpld_name"));
                bTemplate.setTaskTime(rs.getString("stjtmpld_time"));
                bTemplate.setTaskSort(rs.getString("stjtmpld_sort"));
                bTemplate.setTaskCheckPoint(clsManage.convChkPointCode2Name(rs.getString("stjtmpld_checkpoint")));
                bTemplate.setTaskResponsible1(rs.getString("e1Name"));
                bTemplate.setTaskResponsible2(rs.getString("e2Name"));
                bTemplate.setTaskResponsible3(rs.getString("e3Name"));
                bTemplate.setTaskResponsible4(rs.getString("e4Name"));
                bTemplate.setTaskResponsible5(rs.getString("e5Name"));
                arrList.add(bTemplate);
                bTemplate = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int Set_TaskAdd(String templateCode, String taskName, int taskTime, int taskSort, String taskPoint, String respon1, String respon2, String respon3, String respon4, String respon5, String manageBy) {
        try {

            //หา Max
            int max = getMaxID("stjtmpld_id", "stjtmpld");

            int maxSort = 0;
            int mSort = getMaxSortTask("stjtmpld_sort", "stjtmpld", "stjtmpld_flag", "stjtmplh_code", templateCode);
            if (taskSort <= 0) {
                maxSort = mSort + 1;
            } else if (taskSort <= mSort) {
                maxSort = taskSort;
                String sqlGetID = "SELECT stjtmpld_id, stjtmpld_sort FROM stjtmpld where stjtmplh_code = '" + templateCode + "' and stjtmpld_sort >= " + taskSort + " and stjtmpld_flag = 'Y'";
                clsConnect cConnect = new clsConnect();
                ResultSet rs = cConnect.getResultSet(sqlGetID);
                while (rs.next()) {
                    int taskID = rs.getInt("stjtmpld_id");
                    int sort = rs.getInt("stjtmpld_sort");
                    sort = sort + 1;
                    String sqlPlusOne = "UPDATE stjtmpld SET stjtmpld_sort = " + sort + " where stjtmpld_id = " + taskID + " ";
                    clsConnect cConnectA = new clsConnect();
                    cConnectA.ExecuteQuery(sqlPlusOne);
                }
                cConnect.Close();
            } else {
                maxSort = mSort + 1;
            }

            String sql = "INSERT INTO stjtmpld (stjtmpld_id, stjtmplh_code, stjtmpld_name, stjtmpld_sort, "
                    + "stjtmpld_time, stjtmpld_checkpoint, stjtmpld_flag, stxemphr_responsible1, stxemphr_responsible2, stxemphr_responsible3, "
                    + "stxemphr_responsible4, stxemphr_responsible5, stxemphr_createid, stjtmpld_createdate) "
                    + "VALUES (" + max + ",'" + templateCode + "','" + taskName + "'," + maxSort
                    + "," + taskTime + ",'" + taskPoint + "','Y','" + respon1 + "','" + respon2 + "','" + respon3 + "','" + respon4 + "','"
                    + respon5 + "','" + manageBy + "',CURRENT)";
            clsConnect cConnect = new clsConnect();
            int res = cConnect.ExecuteQuery(sql);
            cConnect.Close();

            //Log
            setLog(manageBy, "Insert", "METPlan", "Insert stjtmpld: " + max, "Manage Template:setTask", res);
            //End Log

            return res;
        } catch (Exception ex) {
            return -1;
        }
    }

    public int Set_TaskEdit(String templateCode, String taskID, String taskName, int taskTime, int taskSort, String taskPoint, String respon1, String respon2, String respon3, String respon4, String respon5, String manageBy) {
        try {
            clsConnect cConnect = new clsConnect();

            int maxSort = 0;
            int masterSort = 0;
            //find master sort
            String sqlM = "SELECT stjtmpld_sort FROM stjtmpld where stjtmpld_id = " + taskID;
            ResultSet rsM = cConnect.getResultSet(sqlM);
            while (rsM.next()) {
                masterSort = rsM.getInt("stjtmpld_sort");
            }
            if (taskSort <= 0) {
                maxSort = masterSort;
            } else if (taskSort > masterSort) {
                String sqlA = "SELECT stjtmpld_id, stjtmpld_sort FROM stjtmpld where stjtmplh_code = '" + templateCode + "' and stjtmpld_sort > " + masterSort + " and stjtmpld_sort <= " + taskSort + " and stjtmpld_flag = 'Y' order by stjtmpld_sort";
                ResultSet rsA = cConnect.getResultSet(sqlA);
                while (rsA.next()) {
                    int taskIDInDB = rsA.getInt("stjtmpld_id");
                    int sort = rsA.getInt("stjtmpld_sort");
                    sort = sort - 1;
                    String sqlAOne = "UPDATE stjtmpld SET stjtmpld_sort = " + sort + " where stjtmpld_id = " + taskIDInDB + " ";
                    clsConnect cConnectA = new clsConnect();
                    cConnectA.ExecuteQuery(sqlAOne);
                }
                maxSort = taskSort;
            } else if (taskSort < masterSort) {
                String sqlA = "SELECT stjtmpld_id, stjtmpld_sort FROM stjtmpld where stjtmplh_code = '" + templateCode + "' and stjtmpld_sort >= " + taskSort + " and stjtmpld_sort < " + masterSort + " and stjtmpld_flag = 'Y' order by stjtmpld_sort";
                ResultSet rsA = cConnect.getResultSet(sqlA);
                while (rsA.next()) {
                    int taskIDInDB = rsA.getInt("stjtmpld_id");
                    int sort = rsA.getInt("stjtmpld_sort");
                    sort = sort + 1;
                    String sqlAOne = "UPDATE stjtmpld SET stjtmpld_sort = " + sort + " where stjtmpld_id = " + taskIDInDB + " ";
                    clsConnect cConnectA = new clsConnect();
                    cConnectA.ExecuteQuery(sqlAOne);
                }
                maxSort = taskSort;
            } else {
                maxSort = taskSort;
            }

            String sql = "UPDATE stjtmpld SET stjtmpld_name = '" + taskName + "', "
                    + " stjtmpld_sort = " + maxSort + ", stjtmpld_time = " + taskTime + ", "
                    + " stxemphr_responsible1 = '" + respon1 + "', stxemphr_responsible2 = '" + respon2 + "', "
                    + " stxemphr_responsible3 = '" + respon3 + "', stxemphr_responsible4 = '" + respon4 + "', "
                    + " stxemphr_responsible5 = '" + respon5 + "', stxemphr_updateid = '" + manageBy + "', "
                    + " stjtmpld_updatedate = CURRENT "
                    + " WHERE stjtmpld_id = '" + taskID + "' ";
            int res = cConnect.ExecuteQuery(sql);

            cConnect.Close();
            //Log
            setLog(manageBy, "Update", "METPlan", "Update stjtmpld: " + taskID, "Manage Task:editTask", res);
            //End Log

            return res;
        } catch (Exception ex) {
            return -1;
        }
    }

    public int Set_TaskDel(String templateCode, String taskID, String manageBy) {
        try {
            clsConnect cConnect = new clsConnect();
            int masterSort = 0;
            String chkPoint = "";
            //find master sort
            String sqlM = "SELECT stjtmpld_sort, TRIM(stjtmpld_checkpoint) AS stjtmpld_checkpoint FROM stjtmpld where stjtmpld_id = " + taskID;
            ResultSet rsM = cConnect.getResultSet(sqlM);
            while (rsM.next()) {
                masterSort = rsM.getInt("stjtmpld_sort");
                chkPoint = clsManage.chkNull(rsM.getString("stjtmpld_checkpoint"));
            }

            if (chkPoint.equals("")) {
                String sqlA = "SELECT stjtmpld_id, stjtmpld_sort FROM stjtmpld where stjtmplh_code = '" + templateCode + "' and stjtmpld_sort > " + masterSort + " and stjtmpld_flag = 'Y' order by stjtmpld_sort";
                ResultSet rsA = cConnect.getResultSet(sqlA);
                while (rsA.next()) {
                    int taskIDInDB = rsA.getInt("stjtmpld_id");
                    int sort = rsA.getInt("stjtmpld_sort");
                    sort = sort - 1;
                    String sqlAOne = "UPDATE stjtmpld SET stjtmpld_sort = " + sort + " where stjtmpld_id = " + taskIDInDB + " ";
                    clsConnect cConnectA = new clsConnect();
                    cConnectA.ExecuteQuery(sqlAOne);
                }

                String sql = "UPDATE stjtmpld SET stjtmpld_flag = 'N', stxemphr_updateid = '" + manageBy + "', stjtmpld_updatedate = CURRENT "
                        + "WHERE stjtmpld_id = '" + taskID + "' ";
                int res = cConnect.ExecuteQuery(sql);
                cConnect.Close();

                //Log
                setLog(manageBy, "Delete", "METPlan", "Delete stjtmpld: " + taskID, "Manage Template:delTask", res);
                //End Log

                return res;
            } else { //ถ้ามี Check Point ห้ามลบเด็ดขาด
                return -1;
            }

        } catch (Exception ex) {
            return -1;
        }
    }

    private String Get_TemplateMax(String templateType) {
        try {
            String sql = "SELECT MAX(stjtmplh_code) AS max FROM stjtmplh where stjtmplh_type = '" + templateType + "' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            String max = "";
            while (rs.next()) {
                max = rs.getString("max");
            }
            int countNum = Integer.parseInt(max.substring(3));
            countNum += 1;
            String code = "";
            if (String.valueOf(countNum).length() == 1) {
                code = templateType + "00" + countNum;
            } else if (String.valueOf(countNum).length() == 1) {
                code = templateType + "0" + countNum;
            } else {
                code = templateType + countNum;
            }
            cConnect.Close();
            return code;
        } catch (Exception ex) {
            return templateType + "001";
        }
    }

    public List Get_TaskDetail(String templateCode, String taskCode) {
        try {
            String sql = "SELECT h.stjtmplh_code, h.stjtmplh_name, h.stjtmplh_type, "
                    + " d.stjtmpld_id, d.stjtmpld_name, d.stjtmpld_sort, d.stjtmpld_time, "
                    + " trim(d.stxemphr_responsible1) AS coor1, trim(d.stxemphr_responsible2) AS coor2, trim(d.stxemphr_responsible3) AS coor3, "
                    + " trim(d.stxemphr_responsible4) AS coor4, trim(d.stxemphr_responsible5) AS coor5 "
                    + " FROM stjtmplh AS h "
                    + " LEFT JOIN stjtmpld AS d ON h.stjtmplh_code = d.stjtmplh_code "
                    + " WHERE d.stjtmplh_code = '" + templateCode + "' and stjtmpld_id = " + taskCode;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanTemplate bTemplate = new beanTemplate();
                bTemplate.setTemplateCode(rs.getString("stjtmplh_code"));
                bTemplate.setTemplateName(rs.getString("stjtmplh_name"));
                bTemplate.setTemplateTypeCode(rs.getString("stjtmplh_type"));
                bTemplate.setTaskID(rs.getString("stjtmpld_id"));
                bTemplate.setTaskName(rs.getString("stjtmpld_name"));
                bTemplate.setTaskSort(rs.getString("stjtmpld_sort"));
                bTemplate.setTaskTime(rs.getString("stjtmpld_time"));
                bTemplate.setTaskResponsible1(rs.getString("coor1"));
                bTemplate.setTaskResponsible2(rs.getString("coor2"));
                bTemplate.setTaskResponsible3(rs.getString("coor3"));
                bTemplate.setTaskResponsible4(rs.getString("coor4"));
                bTemplate.setTaskResponsible5(rs.getString("coor5"));

                arrList.add(bTemplate);
                bTemplate = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getTemplateDetail(String templateCode) {
        try {
            String sql = "SELECT stjtmplh_code, stjtmplh_name, stjtmplh_type"
                    + " FROM stjtmplh "
                    + " WHERE stjtmplh_code = '" + templateCode + "' and stjtmplh_flag = 'Y' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanTemplate bTemplate = new beanTemplate();
                bTemplate.setTemplateCode(rs.getString("stjtmplh_code"));
                bTemplate.setTemplateName(rs.getString("stjtmplh_name"));
                bTemplate.setTemplateTypeCode(rs.getString("stjtmplh_type"));
                bTemplate.setTemplateTypeName(clsManage.convProductCode2Name(rs.getString("stjtmplh_type")));
                arrList.add(bTemplate);
                bTemplate = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Recognition">
    public List getProjRecognition(String searchProj, String searchCC, String searchCY, String sortField, String sortDescAsc, String startRow, String limit) {
        try {
            String where = "p.last_docno = (select max(last_docno) from stjmastr1)";
            if (!searchProj.equals("")) {
                if (searchCY.equals("Y")) {
                    where = where + " AND (p.proj_code LIKE '%" + searchProj + "%' OR c.proj_ename LIKE '%" + searchProj + "%')";
                } else {
                    where = where + " AND (p.proj_code LIKE '%" + searchProj + "%' OR p.proj_lname LIKE '%" + searchProj + "%')";
                }
            }
            if (!searchCC.equals("")) {
                where = where + " AND p.dept_code LIKE '" + searchCC + "%' ";
            }
            String sql = "";
            if (startRow.equals("99999") || limit.equals("99999")) {
                if (searchCY.equals("Y")) {
                    sql = "SELECT trim(p.proj_code) AS projCode, trim(c.proj_ename) AS projName, "
                            + " p.proj_cost AS projCost, p.proj_value AS projValue, "
                            + " p.inv_amt AS invAmt, p.actual_costp AS actualCostP, "
                            + " p.actual_costc AS actualCostC, p.actual_revp AS actualRevP, "
                            + " p.actual_revc AS actualRevC, trim(p.proj_status) AS projStatus, "
                            + " trim(p.cost_status) AS costStatus, trim(p.dept_code) AS centerCode, "
                            + " p.actual_costm AS actualCostM, p.actual_expp AS actualExpP, "
                            + " p.actual_expc AS actualExpC, p.actual_expm AS actualExpM, "
                            + " p.actual_revm AS actualRevM, p.actual_adv AS actualAdvance, "
                            + " p.actual_unbill AS actualUnbill "
                            + " FROM stjmastr1 AS p "
                            + " RIGHT OUTER JOIN stjcurre c on p.proj_code = c.proj_id "
                            + " WHERE " + where
                            + " ORDER BY " + sortField + " " + sortDescAsc;
                } else {
                    sql = "SELECT trim(p.proj_code) AS projCode, trim(p.proj_lname) AS projName, "
                            + " p.proj_cost AS projCost, p.proj_value AS projValue, "
                            + " p.inv_amt AS invAmt, p.actual_costp AS actualCostP, "
                            + " p.actual_costc AS actualCostC, p.actual_revp AS actualRevP, "
                            + " p.actual_revc AS actualRevC, trim(p.proj_status) AS projStatus, "
                            + " trim(p.cost_status) AS costStatus, trim(p.dept_code) AS centerCode, "
                            + " p.actual_costm AS actualCostM, p.actual_expp AS actualExpP, "
                            + " p.actual_expc AS actualExpC, p.actual_expm AS actualExpM, "
                            + " p.actual_revm AS actualRevM, p.actual_adv AS actualAdvance, "
                            + " p.actual_unbill AS actualUnbill "
                            + " FROM stjmastr1 AS p "
                            + " WHERE " + where
                            + " ORDER BY " + sortField + " " + sortDescAsc;
                }
            } else if (searchCY.equals("Y")) {
                sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
                        + " trim(p.proj_code) AS projCode, trim(c.proj_ename) AS projName, "
                        + " p.proj_cost AS projCost, p.proj_value AS projValue, "
                        + " p.inv_amt AS invAmt, p.actual_costp AS actualCostP, "
                        + " p.actual_costc AS actualCostC, p.actual_revp AS actualRevP, "
                        + " p.actual_revc AS actualRevC, trim(p.proj_status) AS projStatus, "
                        + " trim(p.cost_status) AS costStatus, trim(p.dept_code) AS centerCode, "
                        + " p.actual_costm AS actualCostM, p.actual_expp AS actualExpP, "
                        + " p.actual_expc AS actualExpC, p.actual_expm AS actualExpM, "
                        + " p.actual_revm AS actualRevM, p.actual_adv AS actualAdvance, "
                        + " p.actual_unbill AS actualUnbill "
                        + " FROM stjmastr1 AS p "
                        + " RIGHT OUTER JOIN stjcurre AS c on p.proj_code = c.proj_id "
                        + " WHERE " + where
                        + " ORDER BY " + sortField + " " + sortDescAsc;
            } else {
                sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
                        + " trim(p.proj_code) AS projCode, trim(p.proj_lname) AS projName, "
                        + " p.proj_cost AS projCost, p.proj_value AS projValue, "
                        + " p.inv_amt AS invAmt, p.actual_costp AS actualCostP, "
                        + " p.actual_costc AS actualCostC, p.actual_revp AS actualRevP, "
                        + " p.actual_revc AS actualRevC, trim(p.proj_status) AS projStatus, "
                        + " trim(p.cost_status) AS costStatus, trim(p.dept_code) AS centerCode, "
                        + " p.actual_costm AS actualCostM, p.actual_expp AS actualExpP, "
                        + " p.actual_expc AS actualExpC, p.actual_expm AS actualExpM, "
                        + " p.actual_revm AS actualRevM, p.actual_adv AS actualAdvance, "
                        + " p.actual_unbill AS actualUnbill "
                        + " FROM stjmastr1 AS p "
                        + " WHERE " + where
                        + " ORDER BY " + sortField + " " + sortDescAsc;
            }
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
//                DecimalFormat df = new DecimalFormat();
//                df.applyPattern("#,###.00");
//
//                DecimalFormat pc = new DecimalFormat();
//                pc.applyPattern("#.00");
                beanRecognition bRecognition = new beanRecognition();
                bRecognition.setProjCode(rs.getString("projCode"));
                bRecognition.setProjName(rs.getString("projName"));
                bRecognition.setCenterCode(rs.getString("centerCode"));
                bRecognition.setBudgetCost(clsManage.numberFormat(rs.getDouble("projCost")));
                bRecognition.setBudgetRevenue(clsManage.numberFormat(rs.getDouble("projValue")));
                bRecognition.setInvoice(clsManage.numberFormat(rs.getDouble("invAmt")));
                bRecognition.setActualCostPrevious(clsManage.numberFormat(rs.getDouble("actualCostP")));
                bRecognition.setActualCostCurrent(clsManage.numberFormat(rs.getDouble("actualCostC")));
                bRecognition.setActualCostMonth(clsManage.numberFormat(rs.getDouble("actualCostM")));
                bRecognition.setActualExpensePrevious(clsManage.numberFormat(rs.getDouble("actualExpP")));
                bRecognition.setActualExpenseCurrent(clsManage.numberFormat(rs.getDouble("actualExpC")));
                bRecognition.setActualExpenseMonth(clsManage.numberFormat(rs.getDouble("actualExpM")));
                double actualRevPre = (rs.getDouble("actualRevP") * -1);
                bRecognition.setActualRevenuePrevious(clsManage.numberFormat(actualRevPre));
                double actualRevCur = (rs.getDouble("actualRevC") * -1);
                bRecognition.setActualRevenueCurrent(clsManage.numberFormat(actualRevCur));
                double actualRevMon = (rs.getDouble("actualRevM") * -1);
                bRecognition.setActualRevenueMonth(clsManage.numberFormat(actualRevMon));
                bRecognition.setActualAdvance(clsManage.numberFormat(rs.getDouble("actualAdvance")));
                bRecognition.setActualUnbill(clsManage.numberFormat(rs.getDouble("actualUnbill")));
                double budgetCost = rs.getDouble("ProjCost");
                double actualCostPrevious = rs.getDouble("actualCostP");
                double actualCostCurrent = rs.getDouble("actualCostC");
                double compCostPrev = 0;
                double compCostCurr = 0;
                if (budgetCost <= 0) {
                    compCostPrev = 0;
                    compCostCurr = 0;
                } else {
                    //try { compCostPrev = actualCostPrevious/budgetCost; } catch (Exception ex) { compCostPrev = 0; }
                    //try { compCostCurr = actualCostCurrent/budgetCost; } catch (Exception ex) { compCostCurr = 0; }
                    compCostPrev = (actualCostPrevious / budgetCost) * 100;
                    compCostCurr = (actualCostCurrent / budgetCost) * 100;
                }

                bRecognition.setCompletionPrevious(clsManage.percentage(compCostPrev) + "%");
                bRecognition.setCompletionCurrent(clsManage.percentage(compCostCurr) + "%");
                bRecognition.setProjStatus(rs.getString("projStatus"));
                bRecognition.setCostStatus(rs.getString("costStatus"));
                arrList.add(bRecognition);
                bRecognition = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getCountProjRecognition(String searchProj, String searchCC, String searchCY) {
        try {
            String where = "p.last_docno = (select max(last_docno) from stjmastr1)";
            //if (!searchProj.equals("") || searchProj != null) {
            if (!searchProj.equals("")) {
                if (searchCY.equals("Y")) {
                    where = where + " AND (p.proj_code LIKE '%" + searchProj + "%' OR c.proj_ename LIKE '%" + searchProj + "%')";
                } else {
                    where = where + " AND (p.proj_code LIKE '%" + searchProj + "%' OR p.proj_lname LIKE '%" + searchProj + "%')";
                }
            }
            //if (!searchCC.equals("") || searchCC != null) {
            if (!searchCC.equals("")) {
                where = where + " AND p.dept_code LIKE '" + searchCC + "%' ";
            }
            String sql = "";
            if (searchCY.equals("Y")) {
                sql = "SELECT COUNT(p.proj_code) AS countRows "
                        + " FROM stjmastr1 AS p "
                        + " RIGHT OUTER JOIN stjcurre AS c on p.proj_code = c.proj_id "
                        + " WHERE " + where;
            } else {
                sql = "SELECT COUNT(p.proj_code) AS countRows "
                        + " FROM stjmastr1 AS p "
                        + " WHERE " + where;
            }
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("countRows")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Billing">
    public List getBilling(String searchProj, String searchCust, String searchInv, String searchRef, String sortField, String sortDescAsc, String startRow, String limit) {
        try {
            String where = "1=1 AND H.REIVNO > 0 AND P.LAELNO = 1";
            if (!searchProj.equals("")) {
                where = where + " AND (H.REPROJ LIKE '%" + searchProj + "%' OR P.LATX40 LIKE '%" + searchProj + "%') ";
            }
            if (!searchCust.equals("")) {
                where = where + " AND (H.REPYNO LIKE '%" + searchCust + "%' OR C.OKCUNM LIKE '%" + searchCust + "%') ";
            }
            if (!searchInv.equals("")) {
                where = where + " AND (H.REIVNO LIKE '%" + searchInv + "%') ";
            }
            if (!searchRef.equals("")) {
                where = where + " AND (H.REELNO LIKE '%" + searchRef + "%' OR H.RECUOR LIKE '%" + searchRef + "%') ";
            }
            String sql = "SELECT H.REPROJ AS PROJCODE, H.REIVNO AS INVNO, H.REIVDT AS INVDATE, "
                    + " H.REELNO AS REFNO, H.RECUOR AS REFNO2, P.LATX40 AS PROJNAME, "
                    + " H.REPYNO AS CUSTCODE, C.OKCUNM AS CUSTNAME, SUM(D.RFAIGA) AS INVAMOUNT "
                    + " FROM MVXCDTPROD.BPINHE H "
                    + " LEFT JOIN MVXCDTPROD.BPROJS P ON H.REPROJ = P.LAPROJ "
                    + " LEFT JOIN MVXCDTPROD.OCUSMA C ON H.REPYNO = C.OKCUNO "
                    + " INNER JOIN MVXCDTPROD.BPINLN D ON H.REPROJ = D.RFPROJ AND H.REELNO = D.RFELNO "
                    + " WHERE " + where
                    + " GROUP BY H.REPROJ, H.REIVNO, H.REIVDT, H.REELNO, H.RECUOR, H.REDLIX, P.LATX40, H.REPYNO, C.OKCUNM "
                    + " ORDER BY H.REIVDT DESC " //H.REDLIX
                    + " FETCH FIRST 20 ROWS ONLY ";
            clsConnectDB2 cConnectDB2 = new clsConnectDB2();
            ResultSet rs = cConnectDB2.getResultSet(sql);
            List arrList = new ArrayList();

//            DecimalFormat df = new DecimalFormat();
//            df.applyPattern("#,###.00");
            while (rs.next()) {
                beanBilling bBilling = new beanBilling();
                String invNo = "0" + rs.getString("INVNO").trim();
                bBilling.setProjCode(rs.getString("PROJCODE"));
                bBilling.setProjName(rs.getString("PROJNAME"));
                bBilling.setCustCode(rs.getString("CUSTCODE"));
                bBilling.setCustName(rs.getString("CUSTNAME"));
                bBilling.setInvNo(invNo);
                bBilling.setInvDate(rs.getString("INVDATE"));
                bBilling.setRefNo(rs.getString("REFNO"));
                bBilling.setRefNo2(rs.getString("REFNO2"));
                bBilling.setInvAmount(clsManage.numberFormat(rs.getDouble("INVAMOUNT")));

                arrList.add(bBilling);
                bBilling = null; //clear data
            }

            cConnectDB2.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getReceive(String invNo) {
        try {
            String sql = "SELECT F.ESYEA4 AS YEARS, F.ESCINO AS INVNO, F.ESDTP5 AS RECEIPTDATE, "
                    + " F.ESPYNO AS CUSTCODE, F.ESCUAM AS RECEIPTAMOUNT, F.ESVTAM AS VATAMOUNT, "
                    + " F.ESVONO AS VOURCHERNO, F.ESACDT AS ACCOUNTINGDATE, F.ESRMBL AS REMARK, "
                    + " X.CGTNID "
                    + " FROM MVXCDTPROD.FSLEDG F "
                    + " LEFT JOIN MTHCDTPROD.XTHMTD X ON F.ESVONO = X.CGVONO AND F.ESPYNO = X.CGPYNO AND F.ESCINO = X.CGCINO "
                    + " WHERE F.ESCINO = '" + invNo + "' ";
            clsConnectDB2 cConnectDB2 = new clsConnectDB2();
            ResultSet rs = cConnectDB2.getResultSet(sql);
            List arrList = new ArrayList();

//            DecimalFormat df = new DecimalFormat();
//            df.applyPattern("#,###.00");
            while (rs.next()) {
                beanBilling bBilling = new beanBilling();
                bBilling.setYears(rs.getString("YEARS"));
                bBilling.setRecDate(rs.getString("RECEIPTDATE"));
                bBilling.setAccDate(rs.getString("ACCOUNTINGDATE"));
                bBilling.setRecAmount(clsManage.numberFormat(rs.getDouble("RECEIPTAMOUNT")));
                bBilling.setVat(clsManage.numberFormat(rs.getDouble("VATAMOUNT")));
                bBilling.setVourNo(rs.getString("VOURCHERNO"));
                bBilling.setRemark(rs.getString("REMARK"));

                arrList.add(bBilling);
                bBilling = null; //clear data
            }

            cConnectDB2.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Shipment">
    public List getShipment(String searchProj, String searchCust, String sortField, String sortDescAsc, String startRow, String limit) {
        try {
            String where = "1=1";
            if (!searchProj.equals("")) {
                where = where + " AND (p.proj_code LIKE '%" + searchProj + "%' OR p.proj_lname LIKE '%" + searchProj + "%') ";
            }
            if (!searchCust.equals("")) {
                where = where + " AND (p.cust_code LIKE '%" + searchCust + "%' OR c.cust_name LIKE '%" + searchCust + "%') ";

            }
            String sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
                    + " trim(p.proj_code) AS projCode, trim(p.proj_lname) AS projName, "
                    + " trim(p.cust_code) AS custCode, trim(c.cust_name) AS custName, "
                    + " trim(p.sales_code) AS saleCode, trim(e.emp_name) AS saleName, "
                    + " trim(p.cont_type) AS contractType, p.proj_cost AS budgetCost, "
                    + " p.proj_value AS budgetRevenue, count(d.proj_code) AS qty "
                    + " FROM stjmasth AS p "
                    + " LEFT JOIN stjplanp AS d ON p.proj_code = d.proj_code"
                    + " LEFT JOIN stxcustr AS c ON p.cust_code = c.cust_code "
                    + " LEFT JOIN stxemphr AS e ON p.sales_code = e.emp_code"
                    + " WHERE " + where
                    + " GROUP BY p.proj_code, p.proj_lname, p.cust_code, c.cust_name, p.sales_code, e.emp_name, p.cont_type, p.proj_cost, p.proj_value"
                    + " ORDER BY " + sortField + " " + sortDescAsc;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanShipment bShipment = new beanShipment();
                bShipment.setProjCode(rs.getString("projCode"));
                bShipment.setProjName(clsManage.chkNull(rs.getString("projName")));
                bShipment.setCustCode(clsManage.chkNull(rs.getString("custCode")));
                bShipment.setCustName(clsManage.chkNull(rs.getString("custName")));
                bShipment.setSaleCode(clsManage.chkNull(rs.getString("saleCode")));
                bShipment.setSaleName(clsManage.chkNull(rs.getString("saleName")));
                bShipment.setQty(rs.getString("qty"));
                bShipment.setContractType(clsManage.chkNull(rs.getString("contractType")));
                bShipment.setBudgetCost(clsManage.numberFormat(Double.parseDouble(clsManage.chkNull(rs.getString("budgetCost")))));
                bShipment.setBudgetRevenue(clsManage.numberFormat(Double.parseDouble(clsManage.chkNull(rs.getString("budgetRevenue")))));
                arrList.add(bShipment);
                bShipment = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getCountShipment(String searchProj, String searchCust) {
        try {
            String where = "1=1";
            if (!searchProj.equals("")) {
                where = where + " AND (proj_code LIKE '%" + searchProj + "%' OR proj_lname LIKE '%" + searchProj + "%') ";
            }
            if (!searchCust.equals("")) {
                where = where + " AND (p.cust_code LIKE '%" + searchCust + "%' OR c.cust_name LIKE '%" + searchCust + "%') ";

            }
            String sql = "SELECT COUNT(proj_code) AS countRows "
                    + " FROM stjmasth "
                    + " WHERE " + where;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("countRows")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getShipmentDetail(String projCode) {
        try {
            String sql = "SELECT trim(stjplanp_code) AS code, stjplanp_production AS prodDate, "
                    + " stjplanp_shipdatep AS shipDateP, stjplanp_shipdate AS shipDateA, "
                    + " stjplanp_tositep AS siteDateP, stjplanp_tosite AS siteDateA, "
                    + " stjplanp_note AS remark "
                    + " FROM stjplanp "
                    + " WHERE proj_code = '" + projCode + "' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanShipment bShipment = new beanShipment();
                bShipment.setCode(rs.getString("code"));

                String prodDate = clsManage.chkNull(rs.getString("prodDate"));
                if (prodDate.equals("")) {
                    bShipment.setProdDate("");
                } else {
                    String tmpProdDate = rs.getString("prodDate").replace("/", "-");
                    bShipment.setProdDate(clsManage.manageYear4DMY(tmpProdDate, -1));
                }

                String shipDateP = clsManage.chkNull(rs.getString("shipDateP"));
                if (shipDateP.equals("")) {
                    bShipment.setShipDateP("");
                } else {
                    String tmpShipDateP = rs.getString("shipDateP").replace("/", "-");
                    bShipment.setShipDateP(clsManage.manageYear4DMY(tmpShipDateP, -1));
                }

                String shipDateA = clsManage.chkNull(rs.getString("shipDateA"));
                if (shipDateA.equals("")) {
                    bShipment.setShipDateA("");
                } else {
                    String tmpShipDateA = rs.getString("shipDateA").replace("/", "-");
                    bShipment.setShipDateA(clsManage.manageYear4DMY(tmpShipDateA, -1));
                }

                String siteDateP = clsManage.chkNull(rs.getString("siteDateP"));
                if (siteDateP.equals("")) {
                    bShipment.setSiteDateP("");
                } else {
                    String tmpSiteDateP = siteDateP.replace("/", "-");
                    bShipment.setSiteDateP(clsManage.manageYear4DMY(tmpSiteDateP, -1));
                }

                String siteDateA = clsManage.chkNull(rs.getString("siteDateA"));
                if (siteDateA.equals("")) {
                    bShipment.setSiteDateA("");
                } else {
                    String tmpSiteDateA = siteDateA.replace("/", "-");
                    bShipment.setSiteDateA(clsManage.manageYear4DMY(tmpSiteDateA, -1));
                }

                bShipment.setRemark(rs.getString("remark"));

                arrList.add(bShipment);
                bShipment = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getShipment4Update(String projCode) {
        try {
            String sql = "SELECT trim(p.proj_code) AS projCode, trim(p.proj_lname) AS projName, "
                    + " trim(c.cust_code) AS custCode, trim(c.cust_name) AS custName, "
                    + " trim(p.sales_code) AS saleCode, trim(s.emp_name) AS saleName, "
                    + " trim(d.stjplanp_desc) AS projDesc, d.stjplanp_production AS prodDate, "
                    + " d.stjplanp_shipdatep AS shipDateP, d.stjplanp_shipdate AS shipDateA, "
                    + " d.stjplanp_tositep AS siteDateP, d.stjplanp_tosite AS siteDateA, "
                    + " count(d.proj_code) AS qty, trim(d.stjplanp_note) AS remark"
                    + " FROM stjmasth p "
                    + " LEFT JOIN stjplanp d ON p.proj_code = d.proj_code "
                    + " LEFT JOIN stxcustr c ON p.cust_code = c.cust_code "
                    + " LEFT JOIN stxemphr s ON p.sales_code = s.emp_code "
                    + " WHERE d.stjplanp_code = '" + projCode + "' "
                    + " GROUP BY p.proj_code, p.proj_lname, d.stjplanp_note, "
                    + " c.cust_code, c.cust_name, p.sales_code, s.emp_name, "
                    + " d.stjplanp_desc, d.stjplanp_production, d.stjplanp_shipdatep, "
                    + " d.stjplanp_shipdate, d.stjplanp_tositep, d.stjplanp_tosite ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanShipment bShipment = new beanShipment();
                bShipment.setProjCode(rs.getString("projCode"));
                bShipment.setProjName(rs.getString("projName"));
                bShipment.setCustCode(clsManage.chkNull(rs.getString("custCode")));
                bShipment.setCustName(clsManage.chkNull(rs.getString("custName")));
                bShipment.setSaleCode(clsManage.chkNull(rs.getString("saleCode")));
                bShipment.setSaleName(clsManage.chkNull(rs.getString("saleName")));
                bShipment.setQty(clsManage.chkNull(rs.getString("qty")));
                bShipment.setCode(projCode);
                bShipment.setDescription(rs.getString("projDesc"));
                String prodDate = clsManage.chkNull(rs.getString("prodDate"));
                if (prodDate.equals("")) {
                    bShipment.setProdDate("");
                } else {
                    String tmpProdDate = rs.getString("prodDate").replace("/", "-");
                    bShipment.setProdDate(clsManage.manageYear4DMY(tmpProdDate, -1));
                }
                String shipDateP = clsManage.chkNull(rs.getString("shipDateP"));
                if (shipDateP.equals("")) {
                    bShipment.setShipDateP("");
                } else {
                    String tmpShipDateP = rs.getString("shipDateP").replace("/", "-");
                    bShipment.setShipDateP(clsManage.manageYear4DMY(tmpShipDateP, -1));
                }
                String shipDateA = clsManage.chkNull(rs.getString("shipDateA"));
                if (shipDateA.equals("")) {
                    bShipment.setShipDateA("");
                } else {
                    String tmpShipDateA = rs.getString("shipDateA").replace("/", "-");
                    bShipment.setShipDateA(clsManage.manageYear4DMY(tmpShipDateA, -1));
                }
                String siteDateP = clsManage.chkNull(rs.getString("siteDateP"));
                if (siteDateP.equals("")) {
                    bShipment.setSiteDateP("");
                } else {
                    String tmpSiteDateP = rs.getString("siteDateP").replace("/", "-");
                    bShipment.setSiteDateP(clsManage.manageYear4DMY(tmpSiteDateP, -1));
                }
                String siteDateA = clsManage.chkNull(rs.getString("siteDateA"));
                if (siteDateA.equals("")) {
                    bShipment.setSiteDateA("");
                } else {
                    String tmpSiteDateP = rs.getString("siteDateA").replace("/", "-");
                    bShipment.setSiteDateA(clsManage.manageYear4DMY(tmpSiteDateP, -1));
                }
                bShipment.setRemark(clsManage.chkNull(rs.getString("remark")));
                arrList.add(bShipment);
                bShipment = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int updateShipment(String projCode, String prodDate, String shipDateP, String shipDateA, String siteDateP, String siteDateA, String remark, String manageBy) {
        try {
            String varProdDate = "";
            if (!clsManage.chkNull(prodDate).equals("")) {
                varProdDate = prodDate.replace("-", "/");
            }
            String varShipDateP = "";
            if (!clsManage.chkNull(shipDateP).equals("")) {
                varShipDateP = shipDateP.replace("-", "/");
            }
            String varShipDateA = "";
            if (!clsManage.chkNull(shipDateA).equals("")) {
                varShipDateA = shipDateA.replace("-", "/");
            }
            String varSiteDateP = "";
            if (!clsManage.chkNull(siteDateP).equals("")) {
                varSiteDateP = siteDateP.replace("-", "/");
            }
            String varSiteDateA = "";
            if (!clsManage.chkNull(siteDateA).equals("")) {
                varSiteDateA = siteDateA.replace("-", "/");
            }

            String sql = "UPDATE stjplanp SET stjplanp_production = '" + varProdDate + "', stjplanp_shipdatep = '" + varShipDateP + "', "
                    + " stjplanp_shipdate = '" + varShipDateA + "', stjplanp_tositep = '" + varSiteDateP + "', stjplanp_tosite = '" + varSiteDateA + "', "
                    + " stjplanp_note = '" + remark + "' "
                    + " WHERE stjplanp_code = '" + projCode + "'";

            clsConnect cConnect = new clsConnect();
            int res = cConnect.ExecuteQuery(sql);
            cConnect.Close();

            //Log
            setLog(manageBy, "Update", "METPlan", "Update stjplanp: " + projCode, "Shipment:updateShipment", res);
            //End Log

            return res;
        } catch (Exception ex) {
            return -1;
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="StartSales">
    public List getStartSales(String searchProj, String searchCust, String sortField, String sortDescAsc) {
        try {
            String where = "1=1 AND LAELNO = '1'";
            if (!searchProj.equals("")) {
                where = where + " AND (P.LAPROJ LIKE '%" + searchProj + "%' OR P.LATX40 LIKE '%" + searchProj + "%') ";
            }
            if (!searchCust.equals("")) {
                where = where + " AND (P.LACUNO LIKE '%" + searchCust + "%' OR C.OKCUNM LIKE '%" + searchCust + "%') ";
            }
            String sql = "SELECT TRIM(P.LAPROJ) AS PROJCODE, TRIM(P.LATX40) AS PROJNAME, "
                    + " TRIM(P.LACUNO) AS CUSTCODE, TRIM(C.OKCUNM) AS CUSTNAME, "
                    + " P.LASTAT AS PROJSTATUS, P.LAFRE2 AS CENTERCODE "
                    + " FROM MVXCDTPROD.BPROJS P "
                    + " LEFT JOIN MVXCDTPROD.OCUSMA C ON P.LACUNO = C.OKCUNO "
                    + " WHERE " + where
                    + " ORDER BY P.LAPROJ DESC " //H.REDLIX
                    + " FETCH FIRST 10 ROWS ONLY ";
            clsConnectDB2 cConnectDB2 = new clsConnectDB2();
            ResultSet rs = cConnectDB2.getResultSet(sql);
            List arrList = new ArrayList();

            DecimalFormat df = new DecimalFormat();
            df.applyPattern("#,###.00");

            while (rs.next()) {
                beanStartSales bStartSales = new beanStartSales();
                String pCode = rs.getString("PROJCODE");
                bStartSales.setProjCode(clsManage.chkNull(rs.getString("PROJCODE")));
                bStartSales.setProjName(clsManage.chkNull(rs.getString("PROJNAME")));
                bStartSales.setCustCode(clsManage.chkNull(rs.getString("CUSTCODE")));
                bStartSales.setCustName(clsManage.chkNull(rs.getString("CUSTNAME")));

                String sqlStatus = "SELECT P.LAELST AS PROJSTATUS, P.LAFRE2 AS CENTERCODE "
                        + " FROM MVXCDTPROD.BPROJS P "
                        + " WHERE P.LAPROJ = '" + pCode + "' and P.LAELNO = '12110' ";
                ResultSet rsStatus = cConnectDB2.getResultSet(sqlStatus);
                while (rsStatus.next()) {
                    bStartSales.setProjStatus(clsManage.chkNull(rsStatus.getString("PROJSTATUS")));
                    bStartSales.setCenterCode(clsManage.chkNull(rsStatus.getString("CENTERCODE")));
                }
                arrList.add(bStartSales);
                bStartSales = null; //clear data
            }

            cConnectDB2.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int setStartSales(String projCode, String manageBy) {
        try {

            String sql = "UPDATE MVXCDTPROD.BPROJS SET LAELST = '20', LAESTL = '20', LAESTH = '20' "
                    + " WHERE LAPROJ = '" + projCode + "' and LAELNO = '12110'";

            clsConnectDB2 cConnectDB2 = new clsConnectDB2();
            int res = cConnectDB2.ExecuteQuery(sql);
            cConnectDB2.Close();

            //Log
            setLog(manageBy, "UPDATE", "METPlan", "Update bprojs: " + projCode, "Start Sales:setStartSales", res);
            //End Log

            return res;
        } catch (Exception ex) {
            return -1;
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Log">
    public int setLog(String who, String what, String where, String how, String func, int result) {
        try {
            String log = "";
            if (result >= 1) {
                log = "Success";
            } else {
                log = "Error";
            }
            String sql = "INSERT INTO stxlogtr (userid, logtype, logproject, logdesc, logfunction, logstatus) VALUES ('" + who + "','" + what + "','" + where + "','" + how + "','" + func + "','" + log + "')";
            clsConnect cConnect = new clsConnect();
            int res = cConnect.ExecuteQuery(sql);
            cConnect.Close();
            return res;
        } catch (Exception ex) {
            return -1;
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Get Max ID">
    public Integer getMaxID(String field, String table) {
        try {
            String sql = "select max(" + field + ") as maxID from " + table;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            String stringMax = "";
            int intMax = 1;

            while (rs.next()) {
                stringMax = clsManage.chkNull(rs.getString("maxID"));
            }

            if (stringMax.equals("")) {
                intMax = 1;
            } else {
                intMax = Integer.parseInt(stringMax) + 1;
            }

            cConnect.Close();
            return intMax;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 1;
        }
    }

    public Integer getMaxSortTask(String maxField, String table, String flag, String whereField, String field) {
        try {

            String sql = "select max(" + maxField + ") as maxID from " + table + " where " + whereField + " = '" + field + "' and " + flag + " = 'Y'";
            //select max(stjtmpld_sort) as maxID from stjtmpld where stjtmplh_code = ''
            //select max(stjplant_sort) as maxID from stjplant where stjplanp_code = ''
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            String stringMax = "";
            int intMax = 0;

            while (rs.next()) {
                stringMax = clsManage.chkNull(rs.getString("maxID"));
            }

            if (stringMax.equals("")) {
                intMax = 0;
            } else {
                intMax = Integer.parseInt(stringMax);
            }

            cConnect.Close();
            return intMax;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    private Integer Get_CountProcess(String countField, String table, String filedWhere, String id, String fieldStatus) {
        try {
            //stjtmplh_code stjplanp_code
            //stjtmpld stjplant
            String sql = "SELECT COUNT(" + countField + ") AS maxID FROM " + table + " where " + filedWhere + " = '" + id + "' and " + fieldStatus + " = 'Y'";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            String stringMax = "";
            int intMax = 0;

            while (rs.next()) {
                stringMax = clsManage.chkNull(rs.getString("maxID"));
            }

            if (stringMax.equals("")) {
                intMax = 0;
            } else {
                intMax = Integer.parseInt(stringMax);
            }

            cConnect.Close();
            return intMax;
        } catch (Exception ex) {
            return 0;
        }
    }

    private Integer Get_CountTime(String countField, String table, String filedWhere, String id, String fieldStatus) {
        try {
            //stjtmpld_time stjplant_time
            //stjtmpld stjplant
            String sql = "SELECT SUM(" + countField + ") AS maxID FROM " + table + " where " + filedWhere + " = '" + id + "' and " + fieldStatus + " = 'Y'";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            String stringMax = "";
            int intMax = 0;

            while (rs.next()) {
                stringMax = clsManage.chkNull(rs.getString("maxID"));
            }

            if (stringMax.equals("")) {
                intMax = 0;
            } else {
                intMax = Integer.parseInt(stringMax);
            }

            cConnect.Close();
            return intMax;
        } catch (Exception ex) {
            return 0;
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Report Project Follow Up Business Plan">
    public String Get_Data_RepProj() {
        try {
            String dataArr = "";
            String sql = "SELECT SKIP 0 FIRST 1 * FROM a4acct:projfollowbp WHERE 1=1";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                dataArr = rs.getString("anpmyr") + "[]" + rs.getString("planyear") + "[]" + rs.getString("months") + "[]" + rs.getString("years");
            }
            cConnect.Close();

            return dataArr;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public List Get_ProjectCurrent_RepProj(String searchProj, String searchDiv, String searchCC, String unit, String sortField, String sortDescAsc, String startRow, String limit) {
        try {
            Double searchUnit = Double.parseDouble(unit);
            String where = "1=1 AND proj_code <> '000'";

            if (!searchProj.equals("")) {
                where = where + " AND (proj_code LIKE '%" + searchProj + "%' OR proj_name LIKE '%" + searchProj + "%')";
            }

            if (!searchDiv.equals("")) {
                where = where + " AND division_code = '" + searchDiv + "' ";
            }

            if (!searchCC.equals("")) {
                where = where + " AND center_code = '" + searchCC + "' ";
            }

            String sql = "";
            if (startRow.equals("99999") || limit.equals("99999")) {
                sql = "SELECT TRIM(proj_code) AS projCode, TRIM(proj_name) AS projName, "
                        + " TRIM(division_code) AS divisionCode, TRIM(center_code) AS centerCode, "
                        + " budget_revenue AS budgetRev, budget_cost AS budgetCost, "
                        + " actual_revenue_m AS actRevM, actual_revenue_c AS actRevC, actual_revenue_p AS actRevP, "
                        + " actual_cost_m AS actCostM, actual_cost_c AS actCostC, actual_cost_p AS actCostP, "
                        + " plan_revenue_m AS planRevM, plan_revenue_c AS planRevC, plan_revenue_p AS planRevP, "
                        + " plan_cost_m AS planCostM, plan_cost_c AS planCostC, plan_cost_p AS planCostP "
                        + " FROM a4acct:projfollowbp "
                        + " WHERE " + where
                        + " ORDER BY " + sortField + " " + sortDescAsc;
            } else {
                sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
                        + " TRIM(proj_code) AS projCode, TRIM(proj_name) AS projName, "
                        + " TRIM(division_code) AS divisionCode, TRIM(center_code) AS centerCode, "
                        + " budget_revenue AS budgetRev, budget_cost AS budgetCost, "
                        + " actual_revenue_m AS actRevM, actual_revenue_c AS actRevC, actual_revenue_p AS actRevP, "
                        + " actual_cost_m AS actCostM, actual_cost_c AS actCostC, actual_cost_p AS actCostP, "
                        + " plan_revenue_m AS planRevM, plan_revenue_c AS planRevC, plan_revenue_p AS planRevP, "
                        + " plan_cost_m AS planCostM, plan_cost_c AS planCostC, plan_cost_p AS planCostP "
                        + " FROM a4acct:projfollowbp "
                        + " WHERE " + where
                        + " ORDER BY " + sortField + " " + sortDescAsc;
            }
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanProjFollowBP bProjFollowBP = new beanProjFollowBP();
                double planRevM = rs.getDouble("planRevM") / searchUnit;                                                                                                    //a
                double actRevM = rs.getDouble("actRevM") / searchUnit;                                                                                                      //b
                double diffRevM = (rs.getDouble("actRevM") - rs.getDouble("planRevM")) / searchUnit;                                                                        //c = b-a
                double planCostM = rs.getDouble("planCostM") / searchUnit;                                                                                                  //d
                double actCostM = rs.getDouble("actCostM") / searchUnit;                                                                                                    //e
                double diffCostM = (rs.getDouble("actCostM") - rs.getDouble("planCostM")) / searchUnit;                                                                     //f = e-d
                double planRevP = rs.getDouble("planRevP") / searchUnit;                                                                                                    //g
                double planRevC = rs.getDouble("planRevC") / searchUnit;                                                                                                    //h
                double actRevC = rs.getDouble("actRevC") / searchUnit;                                                                                                      //i
                double diffRevC = (rs.getDouble("actRevC") - rs.getDouble("planRevC")) / searchUnit;                                                                        //j = i-h
                double perAchieveRev = (rs.getDouble("actRevC") / ((rs.getDouble("planRevP") == 0) ? 1 : rs.getDouble("planRevP"))) * 100;                                    //k = i/g
                double planCostP = rs.getDouble("planCostP") / searchUnit;                                                                                                  //l
                double planCostC = rs.getDouble("planCostC") / searchUnit;                                                                                                  //m
                double actCostC = rs.getDouble("actCostC") / searchUnit;                                                                                                    //n
                double diffCostC = (rs.getDouble("actCostC") - rs.getDouble("planCostC")) / searchUnit;                                                                     //o = n-m
                double perAchieveCost = (rs.getDouble("actCostC") / ((rs.getDouble("planCostP") == 0) ? 1 : rs.getDouble("planCostP"))) * 100;                                //p = n/l
                double gpPlanC = (rs.getDouble("planRevC") - rs.getDouble("planCostC")) / searchUnit;                                                                       //q = h-m
                double gpActualC = (rs.getDouble("actRevC") - rs.getDouble("actCostC")) / searchUnit;                                                                       //r = i-n
                double gpPerPlanC = ((rs.getDouble("planRevC") - rs.getDouble("planCostC")) / ((rs.getDouble("planRevC") == 0) ? 1 : rs.getDouble("planRevC"))) * 100;        //s = q/h
                double gpPerActualC = ((rs.getDouble("actRevC") - rs.getDouble("actCostC")) / ((rs.getDouble("actRevC") == 0) ? 1 : rs.getDouble("actRevC"))) * 100;          //t = r/i
                double budgetRev = rs.getDouble("budgetRev") / searchUnit;                                                                                                  //u
                double actRevP = rs.getDouble("actRevP") / searchUnit;                                                                                                      //v
                double budgetCost = rs.getDouble("budgetCost") / searchUnit;                                                                                                //w
                double actCostP = rs.getDouble("actCostP") / searchUnit;                                                                                                    //x
                double perWPCost = (rs.getDouble("actCostP") / ((rs.getDouble("budgetCost") == 0) ? 1 : rs.getDouble("budgetCost"))) * 100;                                   //y = x/w
                double gpBudget = (rs.getDouble("budgetRev") - rs.getDouble("budgetCost")) / searchUnit;                                                                    //z = u-w
                double gpActual = (rs.getDouble("actRevP") - rs.getDouble("actCostP")) / searchUnit;                                                                        //aa = v-x
                double gpPerBudget = ((rs.getDouble("budgetCost") - rs.getDouble("budgetRev")) / ((rs.getDouble("budgetRev") == 0) ? 1 : rs.getDouble("budgetRev"))) * 100;   //ab = z/u
                double gpPerActual = ((rs.getDouble("actRevP") - rs.getDouble("actCostP")) / ((rs.getDouble("actRevP") == 0) ? 1 : rs.getDouble("actRevP"))) * 100;           //ac = aa/v

                bProjFollowBP.setProjCode(rs.getString("projCode"));
                bProjFollowBP.setProjName(rs.getString("projName"));
                bProjFollowBP.setDivisionCode(rs.getString("divisionCode"));
                bProjFollowBP.setCenterCode(rs.getString("centerCode"));
                bProjFollowBP.setPlanRevenueM(clsManage.numberFormat(planRevM));
                bProjFollowBP.setActualRevenueM(clsManage.numberFormat(actRevM));
                bProjFollowBP.setDiffRevenueM(clsManage.numberFormat(diffRevM));
                bProjFollowBP.setPlanCostM(clsManage.numberFormat(planCostM));
                bProjFollowBP.setActualCostM(clsManage.numberFormat(actCostM));
                bProjFollowBP.setDiffCostM(clsManage.numberFormat(diffCostM));
                bProjFollowBP.setPlanRevenueP(clsManage.numberFormat(planRevP));
                bProjFollowBP.setPlanRevenueC(clsManage.numberFormat(planRevC));
                bProjFollowBP.setActualRevenueC(clsManage.numberFormat(actRevC));
                bProjFollowBP.setDiffRevenueC(clsManage.numberFormat(diffRevC));
                bProjFollowBP.setPerAchieveRev(clsManage.numberFormat(perAchieveRev) + "%");
                bProjFollowBP.setPlanCostP(clsManage.numberFormat(planCostP));
                bProjFollowBP.setPlanCostC(clsManage.numberFormat(planCostC));
                bProjFollowBP.setActualCostC(clsManage.numberFormat(actCostC));
                bProjFollowBP.setDiffCostC(clsManage.numberFormat(diffCostC));
                bProjFollowBP.setPerAchieveCost(clsManage.numberFormat(perAchieveCost) + "%");
                bProjFollowBP.setGPPlanC(clsManage.numberFormat(gpPlanC));
                bProjFollowBP.setGPActualC(clsManage.numberFormat(gpActualC));
                bProjFollowBP.setGPPerPlanC(clsManage.numberFormat(gpPerPlanC) + "%");
                bProjFollowBP.setGPPerActualC(clsManage.numberFormat(gpPerActualC) + "%");
                bProjFollowBP.setBudgetRev(clsManage.numberFormat(budgetRev));
                bProjFollowBP.setActualRevenueP(clsManage.numberFormat(actRevP));
                bProjFollowBP.setBudgetCost(clsManage.numberFormat(budgetCost));
                bProjFollowBP.setActualCostP(clsManage.numberFormat(actCostP));
                bProjFollowBP.setPerWPCost(clsManage.numberFormat(perWPCost) + "%");
                bProjFollowBP.setGPBudget(clsManage.numberFormat(gpBudget));
                bProjFollowBP.setGPActual(clsManage.numberFormat(gpActual));
                bProjFollowBP.setGPPerBudget(clsManage.numberFormat(gpPerBudget) + "%");
                bProjFollowBP.setGPPerActual(clsManage.numberFormat(gpPerActual) + "%");
                arrList.add(bProjFollowBP);
                bProjFollowBP = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_ProjectCurrentCount_RepProj(String searchProj, String searchDiv, String searchCC, String searchUnit) {
        try {
            String where = "1=1";

            if (!searchProj.equals("")) {
                where = where + " AND (proj_code LIKE '%" + searchProj + "%' OR proj_name LIKE '%" + searchProj + "%')";
            }

            if (!searchDiv.equals("")) {
                where = where + " AND division_code = '" + searchDiv + "' ";
            }

            if (!searchCC.equals("")) {
                where = where + " AND center_code = '" + searchCC + "' ";
            }

            String sql = "";
            sql = "SELECT COUNT(proj_code) AS countRows "
                    + " FROM a4acct:projfollowbp "
                    + " WHERE " + where;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("countRows")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

//    public List Get_SumProjectCurrent_RepProj(String searchProj, String searchDiv, String searchCC, String unit, String sortField, String sortDescAsc, String startRow, String limit) {
//        try {
//            Double searchUnit = Double.parseDouble(unit);
//            String where = "1=1 AND proj_code <> '000'";
//
//            if (!searchProj.equals("")) {
//                where = where + " AND (proj_code LIKE '%" + searchProj + "%' OR proj_name LIKE '%" + searchProj + "%')";
//            }
//
//            if (!searchDiv.equals("")) {
//                where = where + " AND division_code = '" + searchDiv + "' ";
//            }
//
//            if (!searchCC.equals("")) {
//                where = where + " AND center_code = '" + searchCC + "' ";
//            }
//
//            String sql = "SELECT SUM(budget_revenue) AS budgetRev, SUM(budget_cost) AS budgetCost, "
//                    + " SUM(actual_revenue_m) AS actRevM, SUM(actual_revenue_c) AS actRevC, SUM(actual_revenue_p) AS actRevP, "
//                    + " SUM(actual_cost_m) AS actCostM, SUM(actual_cost_c) AS actCostC, SUM(actual_cost_p) AS actCostP, "
//                    + " SUM(plan_revenue_m) AS planRevM, SUM(plan_revenue_c) AS planRevC, SUM(plan_revenue_p) AS planRevP, "
//                    + " SUM(plan_cost_m) AS planCostM, SUM(plan_cost_c) AS planCostC, SUM(plan_cost_p) AS planCostP "
//                    + " FROM a4acct:projfollowbp "
//                    + " WHERE " + where;
//            clsConnect cConnect = new clsConnect();
//            ResultSet rs = cConnect.getResultSet(sql);
//            List arrList = new ArrayList();
//
//            while (rs.next()) {
//                beanProjFollowBP bProjFollowBP = new beanProjFollowBP();
//                double planRevM = rs.getDouble("planRevM") / searchUnit;                                                                                                    //a
//                double actRevM = rs.getDouble("actRevM") / searchUnit;                                                                                                      //b
//                double diffRevM = (rs.getDouble("actRevM") - rs.getDouble("planRevM")) / searchUnit;                                                                        //c = b-a
//                double planCostM = rs.getDouble("planCostM") / searchUnit;                                                                                                  //d
//                double actCostM = rs.getDouble("actCostM") / searchUnit;                                                                                                    //e
//                double diffCostM = (rs.getDouble("actCostM") - rs.getDouble("planCostM")) / searchUnit;                                                                     //f = e-d
//                double planRevP = rs.getDouble("planRevP") / searchUnit;                                                                                                    //g
//                double planRevC = rs.getDouble("planRevC") / searchUnit;                                                                                                    //h
//                double actRevC = rs.getDouble("actRevC") / searchUnit;                                                                                                      //i
//                double diffRevC = (rs.getDouble("actRevC") - rs.getDouble("planRevC")) / searchUnit;                                                                        //j = i-h
//                double perAchieveRev = (rs.getDouble("actRevC") / ((rs.getDouble("planRevP")==0) ? 1 : rs.getDouble("planRevP"))) * 100;                                    //k = i/g
//                double planCostP = rs.getDouble("planCostP") / searchUnit;                                                                                                  //l
//                double planCostC = rs.getDouble("planCostC") / searchUnit;                                                                                                  //m
//                double actCostC = rs.getDouble("actCostC") / searchUnit;                                                                                                    //n
//                double diffCostC = (rs.getDouble("actCostC") - rs.getDouble("planCostC")) / searchUnit;                                                                     //o = n-m
//                double perAchieveCost = (rs.getDouble("actCostC") / ((rs.getDouble("planCostP")==0) ? 1 : rs.getDouble("planCostP"))) * 100;                                //p = n/l
//                double gpPlanC = (rs.getDouble("planRevC") - rs.getDouble("planCostC")) / searchUnit;                                                                       //q = h-m
//                double gpActualC = (rs.getDouble("actRevC") - rs.getDouble("actCostC")) / searchUnit;                                                                       //r = i-n
//                double gpPerPlanC = ((rs.getDouble("planRevC") - rs.getDouble("planCostC")) / ((rs.getDouble("planRevC")==0) ? 1 : rs.getDouble("planRevC"))) * 100;        //s = q/h
//                double gpPerActualC = ((rs.getDouble("actRevC") - rs.getDouble("actCostC")) / ((rs.getDouble("actRevC")==0) ? 1 : rs.getDouble("actRevC"))) * 100;          //t = r/i
//                double budgetRev = rs.getDouble("budgetRev") / searchUnit;                                                                                                  //u
//                double actRevP = rs.getDouble("actRevP") / searchUnit;                                                                                                      //v
//                double budgetCost = rs.getDouble("budgetCost") / searchUnit;                                                                                                //w
//                double actCostP = rs.getDouble("actCostP") / searchUnit;                                                                                                    //x
//                double perWPCost = (rs.getDouble("actCostP") / ((rs.getDouble("budgetCost")==0) ? 1 : rs.getDouble("budgetCost"))) * 100;                                   //y = x/w
//                double gpBudget = (rs.getDouble("budgetCost") - rs.getDouble("budgetRev")) / searchUnit;                                                                    //z = w-u
//                double gpActual = (rs.getDouble("actCostP") - rs.getDouble("actRevP")) / searchUnit;                                                                        //aa = x-v
//                double gpPerBudget = ((rs.getDouble("budgetCost") - rs.getDouble("budgetRev")) / ((rs.getDouble("budgetRev")==0) ? 1 : rs.getDouble("budgetRev"))) * 100;   //ab = z/u
//                double gpPerActual = ((rs.getDouble("actCostP") - rs.getDouble("actRevP")) / ((rs.getDouble("budgetCost")==0) ? 1 : rs.getDouble("budgetCost"))) * 100;     //ac = aa/w
//
//                bProjFollowBP.setPlanRevenueM(clsManage.numberFormat(planRevM));
//                bProjFollowBP.setActualRevenueM(clsManage.numberFormat(actRevM));
//                bProjFollowBP.setDiffRevenueM(clsManage.numberFormat(diffRevM));
//                bProjFollowBP.setPlanCostM(clsManage.numberFormat(planCostM));
//                bProjFollowBP.setActualCostM(clsManage.numberFormat(actCostM));
//                bProjFollowBP.setDiffCostM(clsManage.numberFormat(diffCostM));
//                bProjFollowBP.setPlanRevenueP(clsManage.numberFormat(planRevP));
//                bProjFollowBP.setPlanRevenueC(clsManage.numberFormat(planRevC));
//                bProjFollowBP.setActualRevenueC(clsManage.numberFormat(actRevC));
//                bProjFollowBP.setDiffRevenueC(clsManage.numberFormat(diffRevC));
//                bProjFollowBP.setPerAchieveRev(clsManage.numberFormat(perAchieveRev) + "%");
//                bProjFollowBP.setPlanCostP(clsManage.numberFormat(planCostP));
//                bProjFollowBP.setPlanCostC(clsManage.numberFormat(planCostC));
//                bProjFollowBP.setActualCostC(clsManage.numberFormat(actCostC));
//                bProjFollowBP.setDiffCostC(clsManage.numberFormat(diffCostC));
//                bProjFollowBP.setPerAchieveCost(clsManage.numberFormat(perAchieveCost) + "%");
//                bProjFollowBP.setGPPlanC(clsManage.numberFormat(gpPlanC));
//                bProjFollowBP.setGPActualC(clsManage.numberFormat(gpActualC));
//                bProjFollowBP.setGPPerPlanC(clsManage.numberFormat(gpPerPlanC) + "%");
//                bProjFollowBP.setGPPerActualC(clsManage.numberFormat(gpPerActualC) + "%");
//                bProjFollowBP.setBudgetRev(clsManage.numberFormat(budgetRev));
//                bProjFollowBP.setActualRevenueP(clsManage.numberFormat(actRevP));
//                bProjFollowBP.setBudgetCost(clsManage.numberFormat(budgetCost));
//                bProjFollowBP.setActualCostP(clsManage.numberFormat(actCostP));
//                bProjFollowBP.setPerWPCost(clsManage.numberFormat(perWPCost) + "%");
//                bProjFollowBP.setGPBudget(clsManage.numberFormat(gpBudget));
//                bProjFollowBP.setGPActual(clsManage.numberFormat(gpActual));
//                bProjFollowBP.setGPPerBudget(clsManage.numberFormat(gpPerBudget) + "%");
//                bProjFollowBP.setGPPerActual(clsManage.numberFormat(gpPerActual) + "%");
//                arrList.add(bProjFollowBP);
//                bProjFollowBP = null; //clear data
//            }
//
//            cConnect.Close();
//
//            return arrList;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }
    public List Get_SumProjectCurrent_RepProj(String searchProj, String searchDiv, String searchCC, String unit, String sortField, String sortDescAsc, String startRow, String limit) {
        try {
            Double searchUnit = Double.parseDouble(unit);
            String where = "1=1 AND proj_code <> '000'";

            if (!searchProj.equals("")) {
                where = where + " AND (proj_code LIKE '%" + searchProj + "%' OR proj_name LIKE '%" + searchProj + "%')";
            }

            if (!searchDiv.equals("")) {
                where = where + " AND division_code = '" + searchDiv + "' ";
            }

            if (!searchCC.equals("")) {
                where = where + " AND center_code = '" + searchCC + "' ";
            }

            String sql = "SELECT count(*) AS totoRows "
                    + " FROM a4acct:projfollowbp "
                    + " WHERE " + where;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanProjFollowBP bProjFollowBP = new beanProjFollowBP();
//                double planRevM = rs.getDouble("planRevM") / searchUnit;                                                                                                    //a
//                double actRevM = rs.getDouble("actRevM") / searchUnit;                                                                                                      //b
//                double diffRevM = (rs.getDouble("actRevM") - rs.getDouble("planRevM")) / searchUnit;                                                                        //c = b-a
//                double planCostM = rs.getDouble("planCostM") / searchUnit;                                                                                                  //d
//                double actCostM = rs.getDouble("actCostM") / searchUnit;                                                                                                    //e
//                double diffCostM = (rs.getDouble("actCostM") - rs.getDouble("planCostM")) / searchUnit;                                                                     //f = e-d
//                double planRevP = rs.getDouble("planRevP") / searchUnit;                                                                                                    //g
//                double planRevC = rs.getDouble("planRevC") / searchUnit;                                                                                                    //h
//                double actRevC = rs.getDouble("actRevC") / searchUnit;                                                                                                      //i
//                double diffRevC = (rs.getDouble("actRevC") - rs.getDouble("planRevC")) / searchUnit;                                                                        //j = i-h
//                double perAchieveRev = (rs.getDouble("actRevC") / ((rs.getDouble("planRevP")==0) ? 1 : rs.getDouble("planRevP"))) * 100;                                    //k = i/g
//                double planCostP = rs.getDouble("planCostP") / searchUnit;                                                                                                  //l
//                double planCostC = rs.getDouble("planCostC") / searchUnit;                                                                                                  //m
//                double actCostC = rs.getDouble("actCostC") / searchUnit;                                                                                                    //n
//                double diffCostC = (rs.getDouble("actCostC") - rs.getDouble("planCostC")) / searchUnit;                                                                     //o = n-m
//                double perAchieveCost = (rs.getDouble("actCostC") / ((rs.getDouble("planCostP")==0) ? 1 : rs.getDouble("planCostP"))) * 100;                                //p = n/l
//                double gpPlanC = (rs.getDouble("planRevC") - rs.getDouble("planCostC")) / searchUnit;                                                                       //q = h-m
//                double gpActualC = (rs.getDouble("actRevC") - rs.getDouble("actCostC")) / searchUnit;                                                                       //r = i-n
//                double gpPerPlanC = ((rs.getDouble("planRevC") - rs.getDouble("planCostC")) / ((rs.getDouble("planRevC")==0) ? 1 : rs.getDouble("planRevC"))) * 100;        //s = q/h
//                double gpPerActualC = ((rs.getDouble("actRevC") - rs.getDouble("actCostC")) / ((rs.getDouble("actRevC")==0) ? 1 : rs.getDouble("actRevC"))) * 100;          //t = r/i
//                double budgetRev = rs.getDouble("budgetRev") / searchUnit;                                                                                                  //u
//                double actRevP = rs.getDouble("actRevP") / searchUnit;                                                                                                      //v
//                double budgetCost = rs.getDouble("budgetCost") / searchUnit;                                                                                                //w
//                double actCostP = rs.getDouble("actCostP") / searchUnit;                                                                                                    //x
//                double perWPCost = (rs.getDouble("actCostP") / ((rs.getDouble("budgetCost")==0) ? 1 : rs.getDouble("budgetCost"))) * 100;                                   //y = x/w
//                double gpBudget = (rs.getDouble("budgetCost") - rs.getDouble("budgetRev")) / searchUnit;                                                                    //z = w-u
//                double gpActual = (rs.getDouble("actCostP") - rs.getDouble("actRevP")) / searchUnit;                                                                        //aa = x-v
//                double gpPerBudget = ((rs.getDouble("budgetCost") - rs.getDouble("budgetRev")) / ((rs.getDouble("budgetRev")==0) ? 1 : rs.getDouble("budgetRev"))) * 100;   //ab = z/u
//                double gpPerActual = ((rs.getDouble("actCostP") - rs.getDouble("actRevP")) / ((rs.getDouble("budgetCost")==0) ? 1 : rs.getDouble("budgetCost"))) * 100;     //ac = aa/w

                int sumRows = 7;
                int startRows = 14;
                int endRows = rs.getInt("totoRows") + startRows;

                bProjFollowBP.setPlanRevenueM("=SUM(C" + startRows + ":C" + endRows + ")");
                bProjFollowBP.setActualRevenueM("=SUM(D" + startRows + ":D" + endRows + ")");
                bProjFollowBP.setDiffRevenueM("=D" + sumRows + "-C" + sumRows);
                bProjFollowBP.setPlanCostM("=SUM(F" + startRows + ":F" + endRows + ")");
                bProjFollowBP.setActualCostM("=SUM(G" + startRows + ":G" + endRows + ")");
                bProjFollowBP.setDiffCostM("=G" + sumRows + "-F" + sumRows);
                bProjFollowBP.setPlanRevenueP("=SUM(I" + startRows + ":I" + endRows + ")");
                bProjFollowBP.setPlanRevenueC("=SUM(J" + startRows + ":J" + endRows + ")");
                bProjFollowBP.setActualRevenueC("=SUM(K" + startRows + ":K" + endRows + ")");
                bProjFollowBP.setDiffRevenueC("=K" + sumRows + "-J" + sumRows);
                bProjFollowBP.setPerAchieveRev("=IF(I" + sumRows + "=0,((K" + sumRows + "/1)*100),((K" + sumRows + "/I" + sumRows + ")*100))");
                bProjFollowBP.setPlanCostP("=SUM(N" + startRows + ":N" + endRows + ")");
                bProjFollowBP.setPlanCostC("=SUM(O" + startRows + ":O" + endRows + ")");
                bProjFollowBP.setActualCostC("=SUM(P" + startRows + ":P" + endRows + ")");
                bProjFollowBP.setDiffCostC("=P" + sumRows + "-O" + sumRows);
                bProjFollowBP.setPerAchieveCost("=IF(N" + sumRows + "=0,((P" + sumRows + "/1)*100),((P" + sumRows + "/N" + sumRows + ")*100))");
                bProjFollowBP.setGPPlanC("=J" + sumRows + "-O" + sumRows);
                bProjFollowBP.setGPActualC("=K" + sumRows + "-P" + sumRows);
                bProjFollowBP.setGPPerPlanC("=IF(J" + sumRows + "=0,((S" + sumRows + "/1)*100),((S" + sumRows + "/J" + sumRows + ")*100))");
                bProjFollowBP.setGPPerActualC("=IF(K" + sumRows + "=0,((T" + sumRows + "/1)*100),((T" + sumRows + "/K" + sumRows + ")*100))");
                bProjFollowBP.setBudgetRev("=SUM(W" + startRows + ":W" + endRows + ")");
                bProjFollowBP.setActualRevenueP("=SUM(X" + startRows + ":X" + endRows + ")");
                bProjFollowBP.setBudgetCost("=SUM(Y" + startRows + ":Y" + endRows + ")");
                bProjFollowBP.setActualCostP("=SUM(Z" + startRows + ":Z" + endRows + ")");
                bProjFollowBP.setPerWPCost("=IF(Y" + sumRows + "=0,((Z" + sumRows + "/1)*100),((Z" + sumRows + "/Y" + sumRows + ")*100))");
                bProjFollowBP.setGPBudget("=W" + sumRows + "-Y" + sumRows);
                bProjFollowBP.setGPActual("=X" + sumRows + "-Z" + sumRows);
                bProjFollowBP.setGPPerBudget("=IF(W" + sumRows + "=0,((AB" + sumRows + "/1)*100),((AB" + sumRows + "/W" + sumRows + ")*100))");
                bProjFollowBP.setGPPerActual("=IF(X" + sumRows + "=0,((AC" + sumRows + "/1)*100),((AC" + sumRows + "/X" + sumRows + ")*100))");
                arrList.add(bProjFollowBP);
                bProjFollowBP = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_CCOnProjectCurrent_RepProj() {
        try {
            String sql = "SELECT center_code "
                    + " FROM projfollowbp "
                    + " WHERE center_code <> '' "
                    + " GROUP BY center_code "
                    + " ORDER BY center_code ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanListItem bListItem = new beanListItem();
                bListItem.setText(clsManage.chkNull(rs.getString("center_code")));
                bListItem.setValue(clsManage.chkNull(rs.getString("center_code")));
                arrList.add(bListItem);
                bListItem = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List Get_DivOnProjectCurrent_RepProj() {
        try {
            String sql = "SELECT division_code "
                    + " FROM projfollowbp "
                    + " WHERE division_code <> '' "
                    + " GROUP BY division_code "
                    + " ORDER BY division_code ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {
                beanListItem bListItem = new beanListItem();
                bListItem.setText(clsManage.chkNull(rs.getString("division_code")));
                bListItem.setValue(clsManage.chkNull(rs.getString("division_code")));
                arrList.add(bListItem);
                bListItem = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String GET_BP4AnpMyr() {
        try {
            String yearAnpMyr = "";
            String sql = "select * from db4businessplan:anpmyr";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                yearAnpMyr = rs.getString("anpmyr_year") + "[]" + rs.getString("anpmyr_type");
            }
            cConnect.Close();

            return yearAnpMyr;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Travelling">
    public int setTabledetail(String inDocno) {
        try {
            //หา Max
            //String code = Get_TemplateMax(templateType);

            String sql = "INSERT INTO stjtmplh (stjtmplh_code, stjtmplh_name, stjtmplh_type, stjtmplh_time, stjtmplh_process, stjtmplh_flag, stxemphr_createid, stjtmplh_createdate, stxemphr_updateid, stjtmplh_updatedate) ";
            //     + "VALUES ('" + inDocno + "','" + templateName + "','" + templateType + "','5','5','Y','" + manageBy + "',CURRENT,'" + manageBy + "',CURRENT)";
            clsConnect cConnect = new clsConnect();
            int res = cConnect.ExecuteQuery(sql);
            cConnect.Close();

            return res;
        } catch (Exception ex) {
            return -1;
        }
    }

    public Boolean getDochead(String docNo) {
        try {
            int p_cnt = 0;
            String sql = "SELECT count(*) AS cnt "
                    + " FROM stytrave "
                    + " WHERE doc_no = '" + docNo + "' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            while (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    //insert header

                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List getCounthead(String str) {

        try {

            String sql = "SELECT COUNT(*) AS scount "
                    + " FROM stytrave   ";
            if (!str.equals("")) {
                sql = sql + str;
            }

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("scount")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getCountheadv(String str) {

        try {

            String sql = "SELECT COUNT(*) AS scount "
                    + " FROM stytrave E   ";
            if (!str.equals("")) {
                sql = sql + str;
            }

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("scount")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getCountadvancelist(String str) {

        try {

            String sql = "SELECT COUNT(*) AS scount "
                    + " FROM advance_log  E  ";
            if (!str.equals("")) {
                sql = sql + str;
            }

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("scount")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String CheckOutfit(String str) {

        try {

            Integer icount = 0;
            Date date = new Date();
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
            String syear = simpleDateformat.format(date);

            String sql = "SELECT COUNT(*) AS scount "
                    + " FROM stytrave  where emp_code='" + str + "'  and country_code not in ('TH','LA','KH','MM') and  year(de_date) = '" + syear + "' and doc_status='APPROVE'   ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {

                icount = Integer.parseInt(rs.getString("scount"));
            }
            cConnect.Close();

            if (icount > 0) {
                return null;
            } else {
                return "OK";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public String CheckOutfit2(String str, String xyear) {

        try {

            Integer icount = 0;
            Date date = new Date();
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
            String syear = simpleDateformat.format(date);

            String sql = "SELECT COUNT(*) AS scount "
                    + " FROM stytrave  where emp_code='" + str + "'  and country_code not in ('TH','LA','KH','MM') and  year(de_date) = '" + xyear + "' and doc_status='APPROVE'   ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {

                icount = Integer.parseInt(rs.getString("scount"));
            }
            cConnect.Close();

            if (icount > 0) {
                return null;
            } else {
                return "OK";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public String getallowrate(String sco, String sjc, String strip, String sdate, String szone, String prov) {

        try {
            String sql = "";
            String ofrate = "0";
            Integer dcount = 0;

            String zone = "";

            String sqld = "SELECT zone  "
                    + " FROM stxprovd  where country_code='" + sco + "' and prov_code='" + prov + "' ";

            clsConnect cConnectd = new clsConnect();
            ResultSet rsd = cConnectd.getResultSet(sqld);

            while (rsd.next()) {
                zone = rsd.getString("zone");
            }

            cConnectd.Close();

            if (sco.equals("TH") || sco.equals("LA") || sco.equals("KH")) {

                if (zone == null || zone.equals("")) {
                    sql = "SELECT allow_ntax15 as allow1 ,allow_tax15 as  allow2,meal  "
                            + " FROM stxtrate  where country_code='" + sco + "'  and jc_code='" + sjc + "' and trip_code='" + strip + "'   ";

                } else {

                    sql = "SELECT allow_ntax15 as allow1 ,allow_tax15 as  allow2,meal  "
                            + " FROM stxtrate  where country_code='" + sco + "' and zone_code='" + zone + "'  and jc_code='" + sjc + "' and trip_code='" + strip + "'   ";

                }

            } else {

                dcount = Integer.parseInt(sdate);

                if (dcount > 15) {
                    sql = "SELECT allow_ntax30 as allow1 ,allow_tax30 as  allow2,meal   "
                            + " FROM stxtrate  where country_code='" + sco + "'  and jc_code='" + sjc + "' and trip_code='" + strip + "'  ";

                } else {
                    sql = "SELECT allow_ntax15 as allow1 ,allow_tax15 as  allow2,meal  "
                            + " FROM stxtrate  where country_code='" + sco + "'  and jc_code='" + sjc + "' and trip_code='" + strip + "'  ";

                }

            }

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                ofrate = rs.getString("allow1") + "|" + rs.getString("allow2") + "|" + rs.getString("meal");
            }
            cConnect.Close();
            return ofrate;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
    }

    public String getOutfitrate(String sco, String sjc, String strip) {

        try {
            String sql = "";
            String ofrate = "0";

            sql = "SELECT outfit  "
                    + " FROM stxtrate  where country_code='" + sco + "' and jc_code='" + sjc + "' and trip_code='" + strip + "'  ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                ofrate = rs.getString("outfit");
            }
            cConnect.Close();
            return ofrate;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
    }

    public String getallcom(String shotel, String sco) {

        try {
            String sql = "";
            String alcom = "0";

            sql = "SELECT  price_hi,zone  "
                    + " FROM stxhotel  where country_code='" + sco + "' and hotel_code='" + shotel + "' ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                alcom = rs.getString("price_hi") + "|" + rs.getString("zone");
            }
            cConnect.Close();
            return alcom;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
    }

    public List gettravel(String sortField, String sortDescAsc, String startRow, String limit, String str) {
        try {

            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

            String sql = "";

            if (startRow.equals("99999") || limit.equals("99999")) {
                sql = "SELECT E.doc_no ,E.doc_status ,E.doc_date,E.emp_code,E.dept_code,E.trip_code,E.de_provcode,E.re_provcode,E.total_amt,C.src_desc,E.de_provname ,E.re_provname,E.advance_status,E.emp_tname,E.emp_ename,E.de_date,E.re_date,E.cc_code,E.country_code  "
                        + " from stytrave   "
                        + " left outer join stxinfor C on C.src_key = E.country_code and C.src_type='COUNTRY'";
            } else {
                sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
                        + " E.doc_no ,E.doc_status ,E.doc_date,E.emp_code,E.dept_code,E.trip_code,E.de_provcode,E.re_provcode,E.total_amt,C.src_desc,E.de_provname ,E.re_provname,E.advance_status ,E.emp_tname,E.emp_ename,E.de_date,E.re_date,E.cc_code ,E.country_code  "
                        + " from stytrave E   "
                        + " left outer join stxinfor C on C.src_key = E.country_code and C.src_type='COUNTRY' ";

            }
            sql = sql + str + " ORDER BY  doc_no desc ";
//            sql = "SELECT   "
//                    + " from stytrave  "
//                    + " ORDER BY " + sortField + " " + sortDescAsc;  

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            clsManage cMn = new clsManage();

            while (rs.next()) {

                beantravel btrav = new beantravel();
                btrav.setdocNo(rs.getString("doc_no"));
                btrav.setdoctype(rs.getString("doc_status"));
                // btrav.setdocdate(rs.getString("doc_date"));
                Date doc_date = rs.getDate("doc_date");
                if (doc_date != null) {
                    btrav.setdocdate(fmt.format(doc_date));
                } else {
                    btrav.setdocdate("");
                }
                btrav.setemptname(rs.getString("emp_code"));
                btrav.setdeptcode(rs.getString("dept_code"));
                btrav.settripcode(rs.getString("trip_code"));
                btrav.setcountrycode(rs.getString("src_desc"));
                btrav.setdeprovince(rs.getString("de_provname"));
                btrav.setreprovince(rs.getString("re_provname"));
                btrav.settotamt(rs.getString("total_amt"));
                btrav.setempename(rs.getString("emp_ename"));

                btrav.setadvance(rs.getString("advance_status"));

                //  btrav.setdedate(rs.getString("de_date"));
                Date de_date = rs.getDate("de_date");
                if (de_date != null) {
                    btrav.setdedate(fmt.format(de_date));
                } else {
                    btrav.setdedate("");
                }

                Date re_date = rs.getDate("re_date");
                if (re_date != null) {
                    btrav.setredate(fmt.format(re_date));
                } else {
                    btrav.setredate("");
                }
                btrav.setcccode(rs.getString("cc_code"));

                String date1 = rs.getString("de_date");
                String date2 = rs.getString("re_date");
                String format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                Date dateObj1 = sdf.parse(date1);
                Date dateObj2 = sdf.parse(date2);
                long diff = dateObj2.getTime() - dateObj1.getTime();

                int diffDays = (int) (diff / (24 * 1000 * 60 * 60));

                btrav.setvisafee(String.valueOf(diffDays + 1));
                // btrav.setredate(rs.getString("re_date"));
                btrav.setappbal(rs.getString("country_code"));

                arrList.add(btrav);
                btrav = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getadvancelist(String sortField, String sortDescAsc, String startRow, String limit, String str) {
        try {
            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

            String sql = "";

            if (startRow.equals("99999") || limit.equals("99999")) {
                sql = "SELECT E.*,T.emp_tname,T.doc_date  "
                        + " from advance_log E   "
                        + " inner join stytrave T on E.doc_no = T.doc_no and T.doc_status='APPROVE' ";
                sql = sql + str + " ORDER BY " + sortField + " " + sortDescAsc;
            } else {
                sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
                        + " E.*,T.emp_tname,T.doc_date "
                        + " from advance_log E   "
                        + " inner join stytrave T on E.doc_no = T.doc_no and T.doc_status='APPROVE' ";
                sql = sql + str + " ORDER BY " + sortField + " " + sortDescAsc;
            }
//            sql = "SELECT   "
//                    + " from stytrave  "
//                    + " ORDER BY " + sortField + " " + sortDescAsc;  

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beantravel btrav = new beantravel();
                btrav.setdocNo(rs.getString("doc_no"));
                Date doc_date = rs.getDate("doc_date");
                if (doc_date != null) {
                    btrav.setdocdate(fmt.format(doc_date));
                } else {
                    btrav.setdocdate("");
                }
                btrav.setempcode(rs.getString("emp_code"));
                btrav.setempename(rs.getString("emp_tname"));

                btrav.setadvance(rs.getString("app_status"));

                btrav.setrermk(rs.getString("remark"));

                Date app_date = rs.getDate("app_date");
                if (app_date != null) {
                    btrav.setappdate(fmt.format(app_date));
                } else {
                    btrav.setappdate("");
                }

                arrList.add(btrav);
                btrav = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {

            ex.printStackTrace();
            return null;

        }
    }

    public List getEmployeecode(String Userid) {
        try {

            String sql = "";
            sql = "SELECT SKIP 0 FIRST 20  "
                    + "  emp_code  "
                    + "  from stxemphr  where emp_code like '%" + Userid + "%' order by emp_code asc  ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beanListItem itemList = new beanListItem();
                itemList.setText(rs.getString("emp_code"));
                itemList.setValue(rs.getString("emp_code"));
                arrList.add(itemList);

            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getEmployeecodeapppr(String Userid, String step, String level, String dept) {
        try {

            Integer stp = Integer.parseInt(step);
            Integer sle = 0;

            String xsql = " select * from stxinfor where src_type='LEVEL' and src_key='" + level + "' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rsc = cConnect.getResultSet(xsql);

            while (rsc.next()) {

//                beanListItem itemList = new beanListItem();
//                itemList.setText(rs.getString("emp_code"));
//                itemList.setValue(rs.getString("emp_code"));
//                arrList.add(itemList);
                sle = rsc.getInt("src_num");

            }

            String sql = "";
            if (stp == 1) {
//               sql = "SELECT SKIP 0 FIRST 20  "
//                    + "  emp_code  "
//                    + "  from stxemphr  where emp_code like '%" + Userid + "%'  order by emp_code  asc  ";
//               
                sql = " select SKIP 0 FIRST 20 e.emp_code from stxemphr e "
                        + "inner join stxinfor f on f.src_key = e.level and f.src_type='LEVEL' ";
                if (sle < 5) { //manager >>

                    sql += "where e.emp_code like '%" + Userid + "%' and e.department = '" + dept + "' and f.src_num > " + sle + "   ";

                } else {

                    sql += "where e.emp_code like '%" + Userid + "%'  and f.src_num >= " + sle + "   ";

                }

            } else {

//               sql = "SELECT SKIP 0 FIRST 20  "
//                    + "  emp_code  "
//                    + "  from stxemphr  where emp_code like '%" + Userid + "%' and position_name not in ('DIRECTOR','MD','GENERAL MANAGER') order by emp_code  asc  ";
//              
                sql = " select SKIP 0 FIRST 20 e.emp_code from stxemphr e "
                        + "inner join stxinfor f on f.src_key = e.level and f.src_type='LEVEL' ";
                sql += "where e.emp_code like '%" + Userid + "%'  and f.src_num >= " + sle + " and e.position_name not in ('DIRECTOR','MD','GENERAL MANAGER') order by e.emp_code  asc ";

            }

            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beanListItem itemList = new beanListItem();
                itemList.setText(rs.getString("emp_code"));
                itemList.setValue(rs.getString("emp_code"));
                arrList.add(itemList);

            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getCountry() {
        try {

            String sql = "";
            sql = "SELECT src_key,src_desc  "
                    + "  from stxinfor  where  src_type='COUNTRY' order by src_desc asc  ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beanListItem itemList = new beanListItem();
                itemList.setText(rs.getString("src_desc").trim());
                itemList.setValue(rs.getString("src_key").trim());
                arrList.add(itemList);

            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List gettripdesc() {
        try {

            String sql = "";
            //   sql = "select src_key,src_desc from stxinfor where src_type='TRAVELLING'  order by src_key asc ";
            sql = " select a.src_key, a.src_desc,b.src_desc as th from stxinfor a ";
            sql += "inner join stxinfor b on a.src_char = b.src_key ";
            sql += " where a.src_type='TRAVELLING' order by a.src_key asc ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beanListItem itemList = new beanListItem();
                String x = rs.getString("src_desc").trim() + "(" + rs.getString("th").trim() + ")";
                itemList.setText(x);

                itemList.setValue(rs.getString("src_key").trim());
                arrList.add(itemList);

            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getoiltype() {
        try {

            String sql = "";
            sql = "select src_key,src_desc from stxinfor where src_type='OILTYPE'  order by src_key asc ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beanListItem itemList = new beanListItem();
                itemList.setText(rs.getString("src_desc").trim());
                itemList.setValue(rs.getString("src_key").trim());
                arrList.add(itemList);

            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getProvincedata(String sco) {
        try {
            String sql = "";
            if (sco.equals("TH") || sco.equals("KH") || sco.equals("LA")) {

                sql = "SELECT prov_code,prov_tname as name "
                        + "  from stxprovd where country_code='" + sco.trim() + "'  order by prov_tname asc  ";

            } else {

                sql = "SELECT prov_code,prov_ename as name  "
                        + "  from stxprovd where country_code='" + sco.trim() + "'  order by prov_ename asc  ";

            }

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beanListItem itemList = new beanListItem();
                itemList.setText(rs.getString("name").trim());
                itemList.setValue(rs.getString("prov_code").trim());
                arrList.add(itemList);

            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getHoteldata(String sco, String pro) {
        try {
            String sql = "";

            sql = "SELECT hotel_code,hotel_name  "
                    + "  from stxhotel where country_code='" + sco.trim() + "' and  prov_name like '%" + pro.trim() + "%'   order by hotel_name asc  ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beanListItem itemList = new beanListItem();
                itemList.setText(rs.getString("hotel_name").trim());
                itemList.setValue(rs.getString("hotel_code").trim());
                arrList.add(itemList);

            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getEmployeedata(String Userid) {
        try {

            String sql = "";
            sql = " SELECT * from stxemphr  where emp_code='" + Userid + "'  ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beanUser itemuser = new beanUser();
                itemuser.setEmpName(rs.getString("emp_name") + "^" + rs.getString("spouse_name") + "^" + rs.getString("department") + "^" + rs.getString("section") + "^" + rs.getString("job_class") + "^" + rs.getString("email") + "^" + rs.getString("boss_code") + "^" + rs.getString("position_name") + "^" + rs.getString("level"));
                //  itemuser.setEmpNameen(rs.getString("spouse_name"));
                arrList.add(itemuser);
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getMylevel(String empcode) {

        try {
            String sql = "";
            String level = "";

            sql = "SELECT  emp_age  "
                    + " FROM stxemphr  where emp_code='" + empcode + "' ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                level = rs.getString("emp_age");
            }
            cConnect.Close();
            return level;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
    }

    public String getcurr(String co) {
        String sql = "";
        String rate = "";
        try {

            sql = " select  src_char  from stxinfor  where  src_type='COUNTRY' "
                    + " and  src_key='" + co + "' ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                rate = rs.getString("src_char");
            }
            cConnect.Close();

            if (rate == null) {
                rate = "";
            }

            return rate;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String getmailboss(String empcode) {

        try {
            String sql = "";
            String email = "";

            sql = "SELECT  *  "
                    + " FROM apvstep  where step='1' and emp_code=" + empcode + "' ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                email = rs.getString("app_code") + rs.getString("app_name") + "|" + rs.getString("app_email");
            }
            cConnect.Close();
            return email;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String getmailapprove(String empcode, String step) {

        try {
            String sql = "";
            String email = "";

            sql = "SELECT  *  "
                    + " FROM apvstep  where stepid='" + step + "' and emp_code='" + empcode + "' ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                email = rs.getString("app_code");
            }
            cConnect.Close();
            return email;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String getoilfee(String syear, String oiltype) {

        try {
            String sql = "";
            String rate = "";

            sql = "select * from stxoilrate where oiltype='" + oiltype + "' and oildate='" + syear + "' ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                rate = rs.getString("oilrate");
            }
            cConnect.Close();
            return rate;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String getdept(String scc) {

        try {
            String sql = "";
            String dept = "";

            sql = "SELECT  src_char  "
                    + " FROM stxinfor  where src_type='DEPARTMENT'  and src_key='" + scc + "' ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                dept = rs.getString("src_char");
            }
            cConnect.Close();
            return dept;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public List getEmpunderlevel(String Level, String Dept) {
        try {

            String sql = "";
            sql = " SELECT emp_code,emp_name from stxemphr  where department='" + Dept + "' and emp_age < '" + Level + "' and emp_status = 'N' order by emp_code asc ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beanListItem itemList = new beanListItem();
                itemList.setText(rs.getString("emp_name").trim());
                itemList.setValue(rs.getString("emp_code").trim());
                arrList.add(itemList);

            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public List getemailtoplevel(String Level, String Dept) {
        try {

            String sql = "";
            sql = " SELECT email from stxemphr  where department='" + Dept + "' and emp_age >= '" + Level + "' and emp_status = 'N' and email is not null order by email asc ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beanListItem itemList = new beanListItem();
                itemList.setText(rs.getString("email").trim());
                arrList.add(itemList);

            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String checkdupdate(String detaild, String detailh) throws SQLException {
        String ret = "";
        try {
            String ahead[] = detailh.split("\\^");
            String hempid = ahead[1].trim();

            String adetail[] = detaild.split("\\|");

            for (Integer i_row = 0; i_row < adetail.length; i_row++) {
                String acell[] = adetail[i_row].split("\\^");
                String sdate = acell[1].trim();
                String resp = checkdateline(hempid, sdate, "");
                if (resp.equals("OK")) {
                } else {
                    ret += "ERROR";
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            ret = "ERROR";
        }
        return ret;

    }

    public String Addtravel(String createby, String detailh, String detaild) throws SQLException {
        String ret = "";
        try {

            int max = 0;
            max = getMaxdocno();

            if (max < 0) {

                return "";

            } else {

                max = max + 1;

                String docno = String.format("%05d", max);

                DateFormat dfyy = new SimpleDateFormat("yy");
                int yy = Integer.parseInt(dfyy.format(new Date()));

                docno = "T" + yy + docno;

                String ahead[] = detailh.split("\\^");
                clsConnect cls_connect = new clsConnect();
                Connection conn = cls_connect.getConnection();
                conn.setAutoCommit(false);

                String hdocdate = ahead[0].trim();
                String hempid = ahead[1].trim();
                String hnameth = ahead[2].trim();
                String hnameeng = ahead[3].trim();
                String hdept = ahead[4].trim();
                String hcc = ahead[5].trim();
                String hsection = ahead[6].trim();
                String hcountry = ahead[7].trim();
                String htrip = ahead[8].trim();
                String houtfit = ahead[9].trim();
                String hexrate = ahead[10].trim();
                String hvisafee = ahead[11].trim();
                String hphone = ahead[12].trim();
                String hgift = ahead[13].trim();
                String hddate = ahead[14].trim();
                String hdprovince = ahead[15].trim();
                String hdtravelby = ahead[16].trim();
                String hdfltime = ahead[17].trim();
                String hdflno = ahead[18].trim();
                String hdremark = ahead[19].trim();
                String hrdate = ahead[20].trim();
                String hrprovince = ahead[21].trim();
                String hrtravelby = ahead[22].trim();
                String hrfltime = ahead[23].trim();
                String hrflno = ahead[24].trim();
                String hrremark = ahead[25].trim();
                String hdprovname = ahead[26].trim();
                String hrprovname = ahead[27].trim();
                String hjccode = ahead[28].trim();
                String hdmile = ahead[29].trim();
                String hdmilefee = ahead[30].trim();
                String hrmile = ahead[31].trim();
                String hrmilefee = ahead[32].trim();

                String hxtype = ahead[33].trim();

                String htripdesc = ahead[34].trim();

                String hoiltype = ahead[35].trim();
                String hoilrate = ahead[36].trim();
                String hadvance = ahead[37].trim();
                String grandtotal = ahead[38].trim();

                String scurrcode = ahead[39].trim();
                String scurrrate = ahead[40].trim();

                String slocaltran = ahead[41].trim();

                String sadvanceall = ahead[42].trim();

                String docref = ahead[43].trim();

                String invite = ahead[44].trim();

//             DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//             Date docdate = null;
//             docdate = df.parse(hdocdate);
//
//             String tempdate = hdocdate.replace("/", "-");
//             tempdate = clsManage.manageYear4DMY(tempdate, -1);
//             hdocdate = tempdate.replace("-", "/");
//            float totalhead = 0;
//            totalhead = Float.valueOf(houtfit).floatValue() + Float.valueOf(hvisafee).floatValue() + Float.valueOf(hphone).floatValue() + Float.valueOf(hgift).floatValue() + Float.valueOf(hrmilefee).floatValue() ;
//            //totalhead = totalhead * Float.valueOf(hexrate).floatValue();
                try {

                    Statement st = conn.createStatement();

                    String sql1 = "INSERT INTO stytrave (doc_no,doc_status,doc_refno,doc_date,emp_code,emp_ename,emp_tname, ";
                    sql1 += " dept_code,cc_code,sec_code,jc_code,country_code,trip_code,trip_desc, ";
                    sql1 += " exch_rate,currcode,currrate,localtran,visa_fee,phone_fee,gift_amt,outfit_amt,de_date,de_provcode,de_provname,de_by, ";
                    sql1 += " de_flighttime,de_flightno,de_remark,re_date,re_provcode,re_provname,re_by,re_flighttime,re_flightno, ";
                    sql1 += " re_remark,oilrate,oiltype,advance_status,advance_allow,total_amt,demiles_total,demiles_fee,remiless_total,remiles_fee,exch_code,approve1_by,approve1_date,invite_letter) values ( ";
                    sql1 += " '" + docno + "', ";
                    sql1 += " 'NEW', "; //empcode
                    sql1 += " '" + docref + "', "; //doc_refno
                    sql1 += " '" + hdocdate + "', "; //doc_date
                    sql1 += " '" + hempid + "', "; //empcode
                    sql1 += " '" + hnameeng + "', "; //ename
                    sql1 += " '" + hnameth + "', "; //tname
                    sql1 += " '" + hdept + "', "; //deptcode
                    sql1 += " '" + hcc + "', "; //cc code
                    sql1 += " '" + hsection + "', "; // sec code
                    sql1 += " '" + hjccode + "', "; // jc code
                    sql1 += " '" + hcountry + "', "; //  countrycode
                    sql1 += " '" + htrip + "', "; // tripcode
                    sql1 += " '" + htripdesc + "', "; // tripdesc
                    sql1 += " '" + hexrate + "', "; // exch rate
                    sql1 += " '" + scurrcode + "', "; // currcode
                    sql1 += " '" + scurrrate + "', "; // currrate
                    sql1 += " '" + slocaltran + "', "; // localtran
                    sql1 += " '" + hvisafee + "', "; // visafee
                    sql1 += " '" + hphone + "', "; // phonefee
                    sql1 += " '" + hgift + "', "; // gift amt
                    sql1 += " '" + houtfit + "', "; // outfit amt
                    sql1 += " '" + hddate + "', "; // dedate
                    sql1 += " '" + hdprovince + "', "; // de provcode
                    sql1 += " '" + hdprovname + "', "; // de provname
                    sql1 += " '" + hdtravelby + "', "; // de by
                    sql1 += " '" + hdfltime + "', "; // hdfltime
                    sql1 += " '" + hdflno + "', "; // de flightno
                    sql1 += " '" + hdremark + "', "; // de remark
                    sql1 += " '" + hrdate + "', "; // dedate
                    sql1 += " '" + hrprovince + "', "; // re provcode
                    sql1 += " '" + hrprovname + "', "; // re provname
                    sql1 += " '" + hrtravelby + "', "; // re by
                    sql1 += " '" + hrfltime + "', "; // re flighttime
                    sql1 += " '" + hrflno + "', "; // re flightno
                    sql1 += " '" + hrremark + "', "; // re remark
                    sql1 += " '" + hoilrate + "', ";
                    sql1 += " '" + hoiltype + "', ";
                    sql1 += " '" + hadvance + "', ";
                    sql1 += " '" + sadvanceall + "', ";
                    sql1 += " '" + grandtotal + "', ";
                    sql1 += " '" + hdmile + "', "; // demile
                    sql1 += " '" + hdmilefee + "', "; // demilefee
                    sql1 += " '" + hrmile + "', "; // re mile
                    sql1 += " '" + hrmilefee + "',"; // re mile fee
                    sql1 += " '" + hxtype + "', "; // exch_code
                    sql1 += " '" + createby + "', ";
                    sql1 += "  current , ";
                    sql1 += " '" + invite + "' )";
                    //   sql1 += " '" + totalhead + "' ) "; // total amt

                    st.executeUpdate(sql1);

                    String adetail[] = detaild.split("\\|");

                    for (Integer i_row = 0; i_row < adetail.length; i_row++) {

                        String acell[] = adetail[i_row].split("\\^");
                        //sdatadetail += sline +"^"+ sdate +"^"+ sdesc +"^"+ sproject +"^"+ sprovince +"^"+ shotel +"^"+ salcom +"^"+ sallow1 +"^"+ sallow2 +"^"+ smeal +"^"+stran+"^"+ senter + sp

                        String sline = acell[0].trim();
                        String sdate = acell[1].trim();
                        String sdesc = acell[2].trim();
                        String sproject = acell[3].trim();
                        String sprovince = acell[4].trim();
                        String shotel = acell[5].trim();
                        String sprovincename = acell[6].trim();
                        String shotelname = acell[7].trim();
                        String salcom = acell[8].trim();
                        String sallow1 = acell[9].trim();
                        String sallow2 = acell[10].trim();
                        String smeal = acell[11].trim();
                        String stran = acell[12].trim();
                        String senter = acell[13].trim();
                        String subtotal = acell[14].trim();
                        String smile = acell[15].trim();
                        String smilefee = acell[16].trim();

                        String sprojname = acell[17].trim();

//                    float subtotal = 0;
//                    subtotal = Float.valueOf(salcom).floatValue() + Float.valueOf(sallow1).floatValue() + Float.valueOf(sallow2).floatValue() + Float.valueOf(smeal).floatValue() + Float.valueOf(stran).floatValue() + Float.valueOf(senter).floatValue() + Float.valueOf(smilefee).floatValue()  ;
//
//                    totalhead = totalhead + subtotal;
                        String sql2 = "INSERT INTO stytravd ( doc_no,line_no,line_date,line_desc,Proj_code, ";
                        sql2 += " prov_code,hotel_code,prov_name,hotel_name,proj_name,accom_amt,allow1_amt,allow2_amt,";
                        sql2 += " meal_amt,trans_amt,enter_amt,subtotal_amt,miles_total,miles_fee) values ( ";
                        sql2 += " '" + docno + "', ";
                        sql2 += " '" + sline + "', "; // line_no
                        sql2 += " '" + sdate + "', "; //line_date
                        sql2 += " '" + sdesc + "', "; //line_desc
                        sql2 += " '" + sproject + "', "; //Proj_code
                        sql2 += " '" + sprovince + "', "; //prov_code
                        sql2 += " '" + shotel + "', "; // hotel_code
                        sql2 += " '" + sprovincename + "', "; //prov_name
                        sql2 += " '" + shotelname + "', "; // hotel_name
                        sql2 += " '" + sprojname + "', ";
                        sql2 += " '" + salcom + "', "; //accom_amt
                        sql2 += " '" + sallow1 + "', "; //allow1_amt
                        sql2 += " '" + sallow2 + "', "; //allow2_amt
                        sql2 += " '" + smeal + "', "; //meal_amt
                        sql2 += " '" + stran + "', "; //trans_amt
                        sql2 += " '" + senter + "', "; //enter_amt
                        sql2 += " '" + subtotal + "', "; //subtotal_amt
                        sql2 += " '" + smile + "', "; //miles_total
                        sql2 += " '" + smilefee + "') "; //miles_fee

                        st.executeUpdate(sql2);

                    }

//                 String sql2 = " update stytrave set total_amt = '" + totalhead + "' where doc_no = '"+ docno +"' ";
//                 st.executeUpdate(sql2);
                    conn.commit();

                    ret = docno;

                } catch (Exception ex) {
                    ret = "";
                    conn.rollback();
                }

                conn.close();

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            ret = "";
        }
        return ret;
    }

    public int getMaxdocno() {
        int max = 0;
        String docno = "";
        //String sSql = " SELECT max(doc_no) as docmax FROM  stytrave ";
        String sSql = " SELECT   SKIP 0  FIRST 1  doc_no  FROM  stytrave order by doc_no desc  ";

        try {
            clsConnect cls_connect = new clsConnect();

            if (cls_connect.getRecord(sSql)) {
                while (cls_connect.getNextRecord()) {
                    try {

                        docno = cls_connect.getString("doc_no").trim();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            cls_connect.Close();

            int syy = Integer.parseInt(docno.substring(1, 3));

            DateFormat dfyy = new SimpleDateFormat("yy");
            int yynow = Integer.parseInt(dfyy.format(new Date()));

            if (syy < yynow) {
                max = 0;
            } else {
                String sdocx = docno.substring(3, docno.length());
                max = Integer.parseInt(sdocx);
            }
            // max = max + 1;

        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }

        return max;
    }

    public String getEditdata(String docno) {
        try {

            String sql = "";
            sql = " SELECT * from stytrave  where doc_no='" + docno + "'   ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            String arrList = "";
            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

            while (rs.next()) {

                String hdocno = rs.getString("doc_no");
                String hdocdate = "";
                Date dhdocdate = rs.getDate("doc_date");
                if (dhdocdate != null) {
                    hdocdate = fmt.format(dhdocdate);
                }
                String hempid = rs.getString("emp_code");
                String hnameth = rs.getString("emp_tname");
                String hnameeng = rs.getString("emp_ename");
                String hdept = rs.getString("dept_code");
                String hcc = rs.getString("cc_code");
                String hsection = rs.getString("sec_code");
                String hcountry = rs.getString("country_code");
                String htrip = rs.getString("trip_code");
                String tripdesc = rs.getString("trip_desc");
                String hexrate = rs.getString("exch_rate");
                String hvisafee = rs.getString("visa_fee");
                String hphone = rs.getString("phone_fee");
                String hgift = rs.getString("gift_amt");
                String houtfit = rs.getString("outfit_amt");
                String hddate = "";

                Date dhddate = rs.getDate("de_date");
                if (dhddate != null) {
                    hddate = fmt.format(dhddate);
                }

                String hdprovince = rs.getString("de_provcode");
                String hdtravelby = rs.getString("de_by");
                String hdfltime = rs.getString("de_flighttime");
                String hdflno = rs.getString("de_flightno");
                String hdremark = rs.getString("de_remark");
                String hrdate = "";
                Date dhrdate = rs.getDate("re_date");
                if (dhrdate != null) {
                    hrdate = fmt.format(dhrdate);
                }
                String hrprovince = rs.getString("re_provcode");
                String hrtravelby = rs.getString("re_by");
                String hrfltime = rs.getString("re_flighttime");
                String hrflno = rs.getString("re_flightno");
                String hrremark = rs.getString("re_remark");

                float totalhead = 0;
                totalhead = Float.valueOf(houtfit).floatValue() + Float.valueOf(hvisafee).floatValue() + Float.valueOf(hphone).floatValue() + Float.valueOf(hgift).floatValue();
                totalhead = totalhead * Float.valueOf(hexrate).floatValue();
                String tothead = String.valueOf(totalhead);
                String totamt = rs.getString("total_amt");
                String jccode = rs.getString("jc_code");

                String demile = rs.getString("demiles_total");
                String demilefee = rs.getString("demiles_fee");
                String remile = rs.getString("remiless_total");
                String remilefee = rs.getString("remiles_fee");

                String soilrate = rs.getString("oilrate");

                String soiltype = rs.getString("oiltype");
                String sadvance = rs.getString("advance_status");

                String scurrate = rs.getString("currrate");

                String localtran = rs.getString("localtran");

                String advanceall = rs.getString("advance_allow");

                String exchcode = rs.getString("exch_code");
                String currcode = rs.getString("currcode");
                String invite = rs.getString("invite_letter");

//            if (demile.equals("")){
//                demile = "0";
//            }
//            if (demilefee.equals("")){
//                demilefee = "0";
//            }
//            if (remile.equals("")){
//                remile = "0";
//            }
//            if (remilefee.equals("")){
//                remilefee = "0";
//            }
                String tmp = hdocno + "^" + hdocdate + "^" + hempid + "^" + hnameth + "^" + hnameeng + "^" + hdept;
                tmp += "^" + hcc + "^" + hsection + "^" + hcountry + "^" + htrip + "^" + hexrate + "^" + hvisafee;
                tmp += "^" + hphone + "^" + hgift + "^" + houtfit + "^" + hddate + "^" + hdprovince + "^" + hdtravelby;
                tmp += "^" + hdfltime + "^" + hdflno + "^" + hdremark + "^" + hrdate + "^" + hrprovince + "^" + hrtravelby;
                tmp += "^" + hrfltime + "^" + hrflno + "^" + hrremark + "^" + tothead + "^" + jccode + "^" + totamt;
                tmp += "^" + demile + "^" + demilefee + "^" + remile + "^" + remilefee + "^" + tripdesc + "^" + soilrate + "^" + soiltype + "^" + sadvance + "^" + scurrate + "^" + localtran + "^" + advanceall + "^" + exchcode + "^" + currcode + "^" + invite;
                arrList = tmp;

            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getCountline(String docno) {

        try {

            String sql = "SELECT COUNT(*) AS scount "
                    + " FROM stytravd where doc_no='" + docno + "' ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("scount")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getEditdetail(String sortField, String sortDescAsc, String startRow, String limit, String docno) {
        try {

            String sql = "";

            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

            sql = "SELECT * from stytravd  where doc_no='" + docno + "'  order by line_no,line_date asc   ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beantravd btrav = new beantravd();
                btrav.setdocNo(rs.getString("doc_no"));
                btrav.setlineno(rs.getString("line_no"));
                Date line_date = rs.getDate("line_date");
                if (line_date != null) {
                    btrav.setlinedate(fmt.format(line_date));
                } else {
                    btrav.setlinedate("");
                }

                btrav.setlinedesc(rs.getString("line_desc"));
                btrav.setprojcode(rs.getString("proj_code"));
                btrav.setprovcode(rs.getString("prov_code"));
                btrav.sethotel(rs.getString("hotel_code"));
                btrav.setprovename(rs.getString("prov_name"));
                btrav.sethotelname(rs.getString("hotel_name"));
                btrav.setaccom(rs.getString("accom_amt"));
                btrav.setall1(rs.getString("allow1_amt"));
                btrav.setall2(rs.getString("allow2_amt"));
                btrav.setmeal(rs.getString("meal_amt"));
                btrav.settran(rs.getString("trans_amt"));
                btrav.setenter(rs.getString("enter_amt"));
                btrav.settotal(rs.getString("subtotal_amt"));

                String xtmp = rs.getString("miles_total");

                String sprojname = rs.getString("proj_name");

                if (xtmp != null) {
                    btrav.setmiles(xtmp);
                } else {
                    btrav.setmiles("0");
                }

                String xtmpfee = rs.getString("miles_fee");
                if (xtmp != null) {
                    btrav.setmilesfee(xtmpfee);
                } else {
                    btrav.setmilesfee("0");
                }

                btrav.setprojname(rs.getString("proj_name"));

                // btrav.setmiles(rs.getString("miles_total"));
                arrList.add(btrav);
                btrav = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String updatetravel(String createby, String docno, String detailh, String detaild) throws SQLException {
        String ret = "error";
        try {

//            int max = 0;
//            max = getMaxdocno();
//            max = max + 1;
            String ahead[] = detailh.split("\\^");
            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();
            conn.setAutoCommit(false);

            String hdocdate = ahead[0].trim();
            String hempid = ahead[1].trim();
            String hnameth = ahead[2].trim();
            String hnameeng = ahead[3].trim();
            String hdept = ahead[4].trim();
            String hcc = ahead[5].trim();
            String hsection = ahead[6].trim();
            String hcountry = ahead[7].trim();
            String htrip = ahead[8].trim();
            String houtfit = ahead[9].trim();
            String hexrate = ahead[10].trim();
            String hvisafee = ahead[11].trim();
            String hphone = ahead[12].trim();
            String hgift = ahead[13].trim();
            String hddate = ahead[14].trim();
            String hdprovince = ahead[15].trim();
            String hdtravelby = ahead[16].trim();
            String hdfltime = ahead[17].trim();
            String hdflno = ahead[18].trim();
            String hdremark = ahead[19].trim();
            String hrdate = ahead[20].trim();
            String hrprovince = ahead[21].trim();
            String hrtravelby = ahead[22].trim();
            String hrfltime = ahead[23].trim();
            String hrflno = ahead[24].trim();
            String hrremark = ahead[25].trim();
            String hdprovname = ahead[26].trim();
            String hrprovname = ahead[27].trim();
            String hjccode = ahead[28].trim();

            String hdmile = ahead[29].trim();
            String hdmilefee = ahead[30].trim();
            String hrmile = ahead[31].trim();
            String hrmilefee = ahead[32].trim();

            String hexcode = ahead[33].trim();

            String htripdesc = ahead[34].trim();

            String oiltype = ahead[35].trim();
            String oilrate = ahead[36].trim();
            String advance = ahead[37].trim();
            String grandtotal = ahead[38].trim();

            String scurrcode = ahead[39].trim();
            String scurrate = ahead[40].trim();
            String slocaltran = ahead[41].trim();
            String advanceall = ahead[42].trim();

            String invite = ahead[43].trim();

//             DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//             Date docdate = null;
//             docdate = df.parse(hdocdate);
//
//             String tempdate = hdocdate.replace("/", "-");
//             tempdate = clsManage.manageYear4DMY(tempdate, -1);
//             hdocdate = tempdate.replace("-", "/");
            //   float totalhead = 0;
            //    totalhead = Float.valueOf(houtfit).floatValue() + Float.valueOf(hvisafee).floatValue() + Float.valueOf(hphone).floatValue() + Float.valueOf(hgift).floatValue()  + Float.valueOf(hrmilefee).floatValue()      ;
            //  totalhead = totalhead * Float.valueOf(hexrate).floatValue();
            try {

                Statement st = conn.createStatement();
                String sql1 = "";

                sql1 = " update stytrave set  ";
                sql1 += " doc_date='" + hdocdate + "', ";
                sql1 += " emp_code='" + hempid + "', ";
                sql1 += " emp_ename='" + hnameeng + "', ";
                sql1 += " emp_tname='" + hnameth + "', ";
                sql1 += " dept_code='" + hdept + "', ";
                sql1 += " cc_code='" + hcc + "', ";
                sql1 += " sec_code='" + hsection + "', ";
                sql1 += " jc_code='" + hjccode + "', ";
                sql1 += " country_code='" + hcountry + "', ";
                sql1 += " trip_code='" + htrip + "', ";
                sql1 += " trip_desc='" + htripdesc + "', ";
                sql1 += " exch_rate='" + hexrate + "', ";
                sql1 += " currcode='" + scurrcode + "', ";
                sql1 += " currrate='" + scurrate + "', ";
                sql1 += " localtran='" + slocaltran + "', ";
                sql1 += " visa_fee='" + hvisafee + "', ";
                sql1 += " phone_fee='" + hphone + "', ";
                sql1 += " gift_amt='" + hgift + "', ";
                sql1 += " outfit_amt='" + houtfit + "', ";
                sql1 += " de_date='" + hddate + "', ";
                sql1 += " de_provcode='" + hdprovince + "', ";
                sql1 += " de_provname='" + hdprovname + "', ";
                sql1 += " de_by='" + hdtravelby + "', ";
                sql1 += " de_flighttime='" + hdfltime + "', ";
                sql1 += " de_flightno='" + hdflno + "', ";
                sql1 += " de_remark='" + hdremark + "', ";
                sql1 += " re_date='" + hrdate + "', ";
                sql1 += " re_provcode='" + hrprovince + "', ";
                sql1 += " re_provname='" + hrprovname + "', ";
                sql1 += " re_by='" + hrtravelby + "', ";
                sql1 += " re_flighttime='" + hrfltime + "', ";
                sql1 += " re_flightno='" + hrflno + "', ";
                sql1 += " re_remark='" + hrremark + "', ";
                sql1 += " oilrate='" + oilrate + "', ";
                sql1 += " oiltype='" + oiltype + "', ";
                sql1 += " advance_status='" + advance + "', ";
                sql1 += " advance_allow='" + advanceall + "', ";
                sql1 += " total_amt='" + grandtotal + "', ";
                sql1 += " demiles_total='" + hdmile + "', ";
                sql1 += " demiles_fee='" + hdmilefee + "', ";
                sql1 += " remiless_total='" + hrmile + "', ";
                sql1 += " remiles_fee='" + hrmilefee + "' ,";
                sql1 += " exch_code='" + hexcode + "', ";
                sql1 += " approve1_by='" + createby + "', ";
                sql1 += " approve1_date= current , ";
                sql1 += " invite_letter ='" + invite + "' ";
                sql1 += " where doc_no='" + docno + "' ";

                st.executeUpdate(sql1);

//                String sql1 = "INSERT INTO stytrave (doc_no,doc_type,doc_date,emp_code,emp_ename,emp_tname, ";
//                sql1 += " dept_code,cc_code,sec_code,country_code,trip_code, ";
//                sql1 += " exch_rate,visa_fee,phone_fee,gift_amt,outfit_amt,de_date,de_province,de_by, ";
//                sql1 += " de_flighttime,de_flightno,de_remark,re_date,re_province,re_by,re_flighttime,re_flightno, ";
//                sql1 += " re_remark,total_amt) values ( ";
//                sql1 += " '" + String.valueOf(max) + "', ";
//                sql1 += " 'NEW', "; //empcode
//                sql1 += " '" + hdocdate + "', "; //doc_date
//                sql1 += " '" + hempid + "', "; //empcode
//                sql1 += " '" + hnameeng + "', "; //ename
//                sql1 += " '" + hnameth + "', "; //tname
//                sql1 += " '" + hdept + "', "; //deptcode
//                sql1 += " '" + hcc + "', "; //cc code
//                sql1 += " '" + hsection + "', "; // sec code
//                sql1 += " '" + hcountry + "', "; //  countrycode
//                sql1 += " '" + htrip + "', "; // tripcode
//                sql1 += " '" + hexrate + "', "; // exch rate
//                sql1 += " '" + hvisafee + "', "; // visafee
//                sql1 += " '" + hphone + "', "; // phonefee
//                sql1 += " '" + hgift + "', "; // gift amt
//                sql1 += " '" + houtfit + "', "; // outfit amt
//                sql1 += " '" + hddate + "', "; // dedate
//                sql1 += " '" + hdprovince + "', "; // de province
//                sql1 += " '" + hdtravelby + "', "; // de by
//                sql1 += " '" + hdfltime + "', "; // hdfltime
//                sql1 += " '" + hdflno + "', "; // de flightno
//                sql1 += " '" + hdremark + "', "; // de remark
//                sql1 += " '" + hrdate + "', "; // dedate
//                sql1 += " '" + hrprovince + "', "; // re province
//                sql1 += " '" + hrtravelby + "', "; // re by
//                sql1 += " '" + hrfltime + "', "; // re flighttime
//                sql1 += " '" + hrflno + "', "; // re flightno
//                sql1 += " '" + hrremark + "', "; // re remark
//                sql1 += " '" + totalhead + "' ) "; // total amt
                String sqld = " delete from stytravd where doc_no='" + docno + "' ";
                st.executeUpdate(sqld);

                String adetail[] = detaild.split("\\|");

                for (Integer i_row = 0; i_row < adetail.length; i_row++) {

                    String acell[] = adetail[i_row].split("\\^");
                    //sdatadetail += sline +"^"+ sdate +"^"+ sdesc +"^"+ sproject +"^"+ sprovince +"^"+ shotel +"^"+ salcom +"^"+ sallow1 +"^"+ sallow2 +"^"+ smeal +"^"+stran+"^"+ senter + sp

                    String sline = acell[0].trim();
                    String sdate = acell[1].trim();
                    String sdesc = acell[2].trim();
                    String sproject = acell[3].trim();
                    String sprovince = acell[4].trim();
                    String shotel = acell[5].trim();
                    String sprovincename = acell[6].trim();
                    String shotelname = acell[7].trim();
                    String salcom = acell[8].trim();
                    String sallow1 = acell[9].trim();
                    String sallow2 = acell[10].trim();
                    String smeal = acell[11].trim();
                    String stran = acell[12].trim();
                    String senter = acell[13].trim();
                    String subtotal = acell[14].trim();
                    String smile = acell[15].trim();
                    String smilefee = acell[16].trim();
                    String sprojname = acell[17].trim();

//                    float subtotal = 0;
//                    subtotal = Float.valueOf(salcom).floatValue() + Float.valueOf(sallow1).floatValue() + Float.valueOf(sallow2).floatValue() + Float.valueOf(smeal).floatValue() + Float.valueOf(stran).floatValue() + Float.valueOf(senter).floatValue() + Float.valueOf(smilefee).floatValue() ;
//                    totalhead = totalhead + subtotal;
                    String sql2 = "INSERT INTO stytravd ( doc_no,line_no,line_date,line_desc,Proj_code, ";
                    sql2 += " prov_code,hotel_code,prov_name,hotel_name,proj_name,accom_amt,allow1_amt,allow2_amt,";
                    sql2 += " meal_amt,trans_amt,enter_amt,subtotal_amt,miles_total,miles_fee) values ( ";
                    sql2 += " '" + docno + "', ";
                    sql2 += " '" + sline + "', "; // line_no
                    sql2 += " '" + sdate + "', "; //line_date
                    sql2 += " '" + sdesc + "', "; //line_desc
                    sql2 += " '" + sproject + "', "; //Proj_code
                    sql2 += " '" + sprovince + "', "; //prov_code
                    sql2 += " '" + shotel + "', "; // hotel_code
                    sql2 += " '" + sprovincename + "', "; //prov_code
                    sql2 += " '" + shotelname + "', "; // hotel_code
                    sql2 += " '" + sprojname + "', ";
                    sql2 += " '" + salcom + "', "; //accom_amt
                    sql2 += " '" + sallow1 + "', "; //allow1_amt
                    sql2 += " '" + sallow2 + "', "; //allow2_amt
                    sql2 += " '" + smeal + "', "; //meal_amt
                    sql2 += " '" + stran + "', "; //trans_amt
                    sql2 += " '" + senter + "', "; //enter_amt
                    sql2 += " '" + subtotal + "', "; //subtotal_amt
                    sql2 += " '" + smile + "', "; //miles_total
                    sql2 += " '" + smilefee + "') "; //miles_fee

                    st.executeUpdate(sql2);

                }

//                sql1 = " update stytrave set  ";
//                sql1 += " total_amt='"+ totalhead +"' ";
//                sql1 += " where doc_no='"+ docno +"' ";
//                st.executeUpdate(sql1);
                conn.commit();

                ret = "OK";
                return ret;
            } catch (Exception ex) {
                ret = "error";
                conn.rollback();
                return ret;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            ret = "error";
            return ret;
        }
    }

    public String Confirmtravel(String docno, String empcode, String apprtmp, String remark) {
        try {

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();
            conn.setAutoCommit(false);

            try {

                String requstid = "";
                String advanceflag = "";
                String sprovince = "";

                String sql = " select e.emp_code,e.advance_status,e.re_provname,d.prov_ename from stytrave e  ";
                sql = sql + " inner join stxprovd d on e.re_provcode = d.prov_code  and e.country_code = d.country_code  where  e.doc_no='" + docno + "'  ";
                clsConnect cConnectd = new clsConnect();
                ResultSet rs = cConnectd.getResultSet(sql);

                while (rs.next()) {
                    requstid = rs.getString("emp_code");  //รหัสพนักงานที่เดินทาง
                    advanceflag = rs.getString("advance_status");
                    sprovince = rs.getString("prov_ename");
                }

                String smailfrom = "";
                String sfrmname = "";
                String smailto = "";
                String stoname = "";

                String refrm = getmailuser(empcode);  // รหัสพนักงานที่ทำเรื่องขออนุมัติ
                String ref[] = refrm.split("\\^");
                smailfrom = ref[0].trim();
                smailfrom += "@mitsubishielevator.co.th";
                // sfrmname = ref[1].trim();

                String reem = getmailuser(requstid);  // รหัสพนักงานที่ไป
                String refm[] = reem.split("\\^");
                sfrmname = refm[1].trim();

                String firststep = getnextcode(requstid, "1");

                String reto = getmailuser(firststep);
                String ret[] = reto.split("\\^");
                smailto = ret[0].trim();
                smailto += "@mitsubishielevator.co.th";
                stoname = ret[1].trim();

                if (ret[0].trim().equals("")) { // no email
                    smailto = "no";
                }

                if (ret[0].trim().equals("GA")) { // no email
                    smailto = "GA";
                }

                //  String nextcode = getnextcode(empcode,"2");  //fix after first step default 2
                Statement st = conn.createStatement();

                String sqld = " update stytrave set doc_status='CONFIRM',approve1_desc='" + remark + "'  where doc_no='" + docno + "' ";
                st.executeUpdate(sqld);

                String arr[] = apprtmp.split("\\^");

                String Now = "Y";

                for (Integer i_row = 0; i_row < arr.length; i_row++) {

                    String acell[] = arr[i_row].split("\\|");
//<td>step</td><td>empcode</td><td>name</td><td>email</td>
                    String next = "";
                    if (i_row < arr.length - 1) {
                        String acellnext[] = arr[i_row + 1].split("\\|");
                        next = acellnext[1].trim();
                    }
                    String sstep = acell[0].trim();
                    String scode = acell[1].trim();
                    String sname = acell[2].trim();
                    String smail = acell[3].trim();

                    String sqlc = " insert into  apvlog (doc_type,doc_no,create_date,emp_code,stepid,app_code,next_code,stepnow) values  ";
                    sqlc += "('TRAV','" + docno + "',CURRENT,'" + requstid + "','" + sstep + "','" + scode + "','" + next + "','" + Now + "')";
                    st.executeUpdate(sqlc);

                    Now = "";

                }

//                
//                String from = sfrom;
//                String to = sto;
//                String from = "suttipong.t@mitsubishielevator.co.th";
//                String to = "suttipong.t@mitsubishielevator.co.th";
                if (!smailto.equals("GA")) { // no email
                    sendmailnext(requstid, smailfrom, smailto, docno, sfrmname, sprovince);
                }
//              

                conn.commit();

                conn.close();
                return "OK";

            } catch (Exception ex) {
                ex.printStackTrace();
                conn.rollback();
                return null;
            }

        } catch (Exception ex) {
            return null;
        }
    }

    public String getnextcode(String empcode, String step) {

        try {
            String sql = "";
            String scode = "";

            sql = "SELECT  *  "
                    + " FROM apvstep  where stepid='" + step + "' and emp_code='" + empcode + "' ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                scode = rs.getString("app_code");
            }
            cConnect.Close();
            return scode;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String approvetravel(String docno, String type, String userid, String total, String sfrom, String sto, String scc) {

        try {

            String requestname = "";
            String traveldate = "";
            String dfrom = "";
            String rfrom = "";
            String dp = "";
            String rp = "";
            String dby = "";
            String rby = "";
            String ddate = "";
            String rdate = "";
            String Empcode = "";
            String sprovince = "";
            String strip = "";

            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

            String sqlcmd = "select *  FROM stytrave"
                    + " WHERE doc_no = '" + docno + "'  and doc_status='CONFIRM' ";
            clsConnect cls_connect1 = new clsConnect();

            if (cls_connect1.getRecord(sqlcmd)) {
                while (cls_connect1.getNextRecord()) {

                    requestname = cls_connect1.getString("emp_ename");
                    Date doc_date = cls_connect1.getDate("doc_date");
                    if (doc_date != null) {
                        traveldate = fmt.format(doc_date);
                    } else {
                        traveldate = "";
                    }

                    sprovince = cls_connect1.getString("country_code");
                    strip = cls_connect1.getString("trip_code");
                    dfrom = cls_connect1.getString("de_date");
                    rfrom = cls_connect1.getString("re_date");
                    dp = cls_connect1.getString("de_provname");
                    rp = cls_connect1.getString("re_provname");
                    dby = cls_connect1.getString("de_by");
                    rby = cls_connect1.getString("re_by");
                    ddate = "";
                    rdate = "";
                    Empcode = "";

                }
            }

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();

            conn.setAutoCommit(false);

            try {

                Statement st = conn.createStatement();
                String sqld = "";
                Date nowdate = new Date();

//                if (type.equals("APPROVE")){
//                sqld = " update stytrave set doc_type='APPROVE'   ";
//                sqld = sqld + " , doc_no='"+ docno +"',approve_amt='"+ total +"' ";
//                sqld = sqld + " , approve_by='"+ userid +"',approve_date='"+ fmt.format(nowdate.getTime()) +"' ";
//                sqld = sqld + " , approve_bal='0' ";
//                sqld = sqld + " where doc_no='"+ docno +"' ";
//                }else{
//                sqld = " update stytrave set doc_type='REJECT'    ";
//                sqld = sqld + " , doc_no='"+ docno +"' ";
//                sqld = sqld + " , approve_by='"+ userid +"',approve_date='"+ fmt.format(nowdate.getTime()) +"' ";
//                sqld = sqld + " where doc_no='"+ docno +"' ";
//                }
//
//                st.executeUpdate(sqld);
//                String from = sfrom;
//                String to = sto;
//                String cc = scc;
                String from = "suttipong.t@mitsubishielevator.co.th";
                String to = "suttipong.t@mitsubishielevator.co.th";
                String cc = "suttipong.t@mitsubishielevator.co.th";

                SmtpClient client = new SmtpClient("mail.mitsubishielevator.co.th");
                client.from(from);
                client.to(to);

                PrintStream message = client.startMessage();

                message.println("To: " + to);
                message.println("Cc: " + cc);
                message.println("Subject: Confrim Travelling  Doc no: " + docno);
                message.println("");
                message.println("");
                message.println(" Travelling Doc no: " + docno);
                message.println(" travel date: " + traveldate);
                message.println(" By  K." + requestname);
                message.println("================================================");
                message.println(" Travelling detail url: ");
                message.println("================================================");
                message.println("");
                message.println(" Approve Status : " + type);
                message.println(" date:" + fmt.format(nowdate.getTime()));

                client.closeServer();

                // conn.commit();
                conn.close();
                return "OK";

            } catch (Exception ex) {
                ex.printStackTrace();
                conn.rollback();
                return null;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public String Canceltravel(String docno, String empid) {

        try {

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();

            Statement st = conn.createStatement();

            String sqld = " update stytrave set doc_status='CANCEL' where doc_no='" + docno + "' ";
            st.executeUpdate(sqld);

            //  String sqld2 = " update advance_log set app_status='C',remark='cancel by "+ empid +"',app_date = current ,update_by='"+ empid +"' where doc_no='"+ docno +"' ";
            // st.executeUpdate(sqld2);
            conn.close();

            return "OK";
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String deletetravel(String docno) {

        try {

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();

            Statement st = conn.createStatement();

            if (!docno.equals("")) {

                String sqld = " delete from stytrave  where doc_no='" + docno + "' ";
                st.executeUpdate(sqld);

                conn.close();

                return "OK";

            } else {

                return null;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }//

    public String updateprofile(String param) {

        try {

            String[] arrdata = param.split("\\|");

            String empid = arrdata[0];
            String snameth = arrdata[1];
            String snameen = arrdata[2];
            //  String sdept = arrdata[3];
            String scc = arrdata[3];
            String ssec = arrdata[4];
            String smail = arrdata[5];
            String sboss = arrdata[6];
            String sjc = arrdata[7];
            String spos = arrdata[8];
            String slevname = arrdata[9];

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();

            Statement st = conn.createStatement();

            String sqld = " update stxemphr set emp_name='" + snameth + "',spouse_name='" + snameen + "',";
            sqld += " department='" + scc + "',section='" + ssec + "', ";
            sqld += " email='" + smail + "' , boss_code='" + sboss + "', ";
            sqld += " position_name='" + spos + "' , level='" + slevname + "' ";
            sqld += " where emp_code='" + empid + "' ";
            st.executeUpdate(sqld);

            conn.close();

            return "OK";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String getstepapprove(String empcode) {

        try {
            String sql = "";
            String result = "";

            sql = "SELECT  *  "
                    + " FROM apvstep  where emp_code='" + empcode + "' order by stepid asc ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                result += rs.getString("stepid") + "^" + rs.getString("app_code") + "^" + rs.getString("app_name") + "^" + rs.getString("app_email") + "|";
            }
            cConnect.Close();
            return result;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String updatestepapv(String param, String semp) {

        try {
            String sql = "";
            String result = "";

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();
            conn.setAutoCommit(false);

            try {

                if (!param.equals("")) {
                    Statement st = conn.createStatement();

                    String sqle = " delete from apvstep  where emp_code='" + semp + "' ";
                    st.executeUpdate(sqle);

                    String sparam[] = param.split("\\|");

                    for (Integer i_row = 0; i_row < sparam.length; i_row++) {

                        String acell[] = sparam[i_row].split("\\^");

                        String sstep = acell[0].trim();
                        String scode = acell[1].trim();
                        String sname = acell[2].trim();
                        String smail = acell[3].trim();

                        String sqld = " insert into  apvstep";
                        sqld += "( stepid,emp_code,app_code,app_name,app_email ) values";
                        sqld += "('" + sstep + "','" + semp + "','" + scode + "','" + sname + "','" + smail + "' ) ";
                        st.executeUpdate(sqld);

                    }

                }

            } catch (Exception ex) {
                ex.printStackTrace();
                conn.rollback();
                return "";
            }

            conn.commit();
            return "OK";

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
    //updatestepapv

    public String getmailuser(String userid) {

        try {

            String mailuser = "";

            String sql = " select * from stxemphr where emp_code='" + userid + "'  ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                mailuser = rs.getString("email") + "^" + rs.getString("spouse_name");
            }

            return mailuser;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String getemailga() {

        try {

            String mailuser = "";

            String sql = " select  * from stxinfor where src_type='GAAPP' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                mailuser = rs.getString("src_desc");
            }
            mailuser = mailuser.trim();

            return mailuser;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String checkmanager(String empcode) {

        try {

            String level = "";

            String sql = " select  * from stxemphr where emp_code = '" + empcode + "' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                level = rs.getString("level");
            }
            level = level.trim();

            return level;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String trvreject(String appid, String docno, String status, String remark) {

        try {

            String sql = " select * from apvlog where doc_type='TRAV' and doc_no='" + docno + "' and app_code='" + appid + "' and app_status is null and stepnow = 'Y' ";

            String requserid = "";
            String Step = "";
            String Nextcode = "";
            String requestmail = "";
            String mailapp = "";
            String nameapp = "";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {

                requserid = rs.getString("emp_code");
                Step = rs.getString("stepid");
                Nextcode = rs.getString("next_code");

            }

            String createby = "";
            String sql2 = " select approve1_by from stytrave where  doc_no='" + docno + "'  ";

            ResultSet rs2 = cConnect.getResultSet(sql2);

            while (rs2.next()) {

                createby = rs2.getString("approve1_by");

            }

            if (createby == null || createby.equals("")) {
                createby = requserid;
            }

            Connection conn = cConnect.getConnection();
            conn.setAutoCommit(false);

            if (!requserid.trim().equals("")) {
                String reqd = getmailuser(createby);
                String req[] = reqd.split("\\^");
                requestmail = req[0].trim();
                requestmail += "@mitsubishielevator.co.th";

                String appd = getmailuser(appid);
                String app[] = appd.split("\\^");
                mailapp = app[0].trim();
                mailapp += "@mitsubishielevator.co.th";
                nameapp = app[1].trim();

                if (app[0].trim().equals("GA")) {
                    mailapp = getemailga();
                    mailapp += "@mitsubishielevator.co.th";
                }

                try {

                    Statement st = conn.createStatement();

                    String sqld = " update apvlog set app_status='N',app_date=CURRENT,remark='" + remark + "' ,stepnow=''  ";
                    sqld += " where doc_type='TRAV' and doc_no='" + docno + "' and emp_code='" + requserid + "' and stepid='" + Step + "' and app_code='" + appid + "' ";
                    st.executeUpdate(sqld);

                    String sqls = " update stytrave set doc_status='REJECT' ";
                    sqls += " where  doc_no='" + docno + "'  ";
                    st.executeUpdate(sqls);

//                String from = "suttipong.t@mitsubishielevator.co.th";
//                String to = "suttipong.t@mitsubishielevator.co.th";
                    String from = mailapp;
                    String to = requestmail;

                    conn.commit();

                    sendmailback(from, to, docno, nameapp, status);

                } catch (Exception ex) {
                    conn.rollback();
                    ex.printStackTrace();
                    return "";
                }

            }

            conn.close();
            return "OK";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    //trvapprove
    public String trvapprove(String appid, String docno, String status, String remark) {

        try {

            String Mdcode = getAPP("MDAPP");//  "03479";
            String Director10 = "02802";
            String Director12 = "90049";

            String ga = getAPP("GAAPP");
            String reqga = getmailuser(ga);
            String reqgan[] = reqga.split("\\^");
            String gamail = reqgan[0].trim();
            gamail += "@mitsubishielevator.co.th";

            String sql = " select * from apvlog where doc_type='TRAV' and doc_no='" + docno + "' and app_code='" + appid + "' and app_status is null and stepnow = 'Y' ";

            String requserid = "";
            String Step = "";
            String Nextcode = "";
            String requestmail = "";
            String mailapp = "";
            String nameapp = "";
            String ccmail = "";
            String Advanceflag = "";
            String allowflag = "";

            clsConnect cConnect = new clsConnect();

            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {

                requserid = rs.getString("emp_code");  // request user
                Step = rs.getString("stepid");
                Nextcode = rs.getString("next_code");

            }

            String createby = "";
            String sprovince = "";
            String sql2 = " select e.advance_status,e.approve1_by,e.re_provname,p.prov_ename,e.advance_allow,e.de_by,e.country_code, "
                    + "    to_char(e.de_date,'%d/%m/%Y') as dedate , to_char(e.re_date,'%d/%m/%Y') as redate,c.src_desc as country   "
                    + " from stytrave e ";
            sql2 = sql2 + " inner join stxprovd p on e.re_provcode = p.prov_code   and e.country_code = p.country_code "
                    + " inner join stxinfor c on c.src_key = e.country_code and  c.src_type='COUNTRY' "
                    + " where  e.doc_no='" + docno + "'  ";
            ResultSet rs2 = cConnect.getResultSet(sql2);

            String tripby = "";
            String scountry = "";
            String fromdate = "";
            String todate = "";
            String country = "";

            while (rs2.next()) {

                createby = rs2.getString("approve1_by");  // create by
                Advanceflag = rs2.getString("advance_status");
                sprovince = rs2.getString("prov_ename");
                allowflag = rs2.getString("advance_allow");
                tripby = rs2.getString("de_by");
                scountry = rs2.getString("country_code");
                fromdate = rs2.getString("dedate");
                todate = rs2.getString("redate");
                country = rs2.getString("country");

            }

            if (createby == null || createby.equals("")) {
                createby = requserid;
            }

//            float isumallow = 0;
//            if (Nextcode.trim().equals("") || Nextcode == null){
//                
//                String sqlx = " select sum(allow1_amt+allow2_amt+meal_amt) as total from stytravd where doc_no='"+ docno +"' ";
//                ResultSet rs3 = cConnect.getResultSet(sqlx);
//
//                while (rs3.next()) {
//
//                    isumallow = rs3.getFloat("total");  // create by 
//
//                }
//                
//            }
            Connection conn = cConnect.getConnection();
            conn.setAutoCommit(false);

            if (Nextcode.trim().equals("") || Nextcode == null) { // last step

                try {

                    String reqd = getmailuser(createby); // เป็นเมลคนที่ทำให้
                    String req[] = reqd.split("\\^");
                    requestmail = req[0].trim();
                    requestmail += "@mitsubishielevator.co.th";

                    String reqm = getmailuser(requserid); // ชื่อพนักงานที่ขอ
                    String reqt[] = reqm.split("\\^");
                    ccmail = reqt[0].trim();
                    nameapp = reqt[1].trim();

                    String appd = getmailuser(appid);
                    String app[] = appd.split("\\^");
                    mailapp = app[0].trim();
                    mailapp += "@mitsubishielevator.co.th";
                    nameapp = app[1].trim();

                    if (app[0].trim().equals("GA")) {

                        mailapp = getemailga();
                        mailapp += "@mitsubishielevator.co.th";

                    }

                    Statement st = conn.createStatement();

                    String sqld = " update apvlog set app_status='Y',app_date=CURRENT,remark='" + remark + "',stepnow='' ";
                    sqld += " where doc_type='TRAV' and doc_no='" + docno + "' and emp_code='" + requserid + "' and stepid='" + Step + "' and app_code='" + appid + "' ";
                    st.executeUpdate(sqld);

//
                    String sqls = " update stytrave set doc_status='APPROVE' ";
                    sqls += " where  doc_no='" + docno + "'  ";
                    st.executeUpdate(sqls);

                    if (Advanceflag.trim().equals("Y")) {  // send advance to finance

                        String sqlx = " insert into  advance_log (doc_no,create_date,create_by,emp_code,app_status) values  ";
                        sqlx += "('" + docno + "',CURRENT,'" + createby + "','" + requserid + "','')";
                        st.executeUpdate(sqlx);

                    }

                   String sdirectcode = getdirector(requserid);
                    String cMdcode = getAPP("MDAPP");
                    String appbys = "";

                    if (cMdcode.trim().equals(requserid.trim())) {
                        appbys = sdirectcode;
                    } else {
                        appbys = cMdcode;
                    }

                    //==========  create  Report Travelling travel by AIR to Oversea
                    if (tripby.trim().equals("AIR") && !scountry.trim().equals("TH")) {

                        String level = checkmanager(requserid);   // Only Manager level

                        if (level.trim().equals("DDM") || level.trim().equals("DM") || level.trim().equals("DMD") || level.trim().equals("GM") || level.trim().equals("M1") || level.trim().equals("MD")|| level.trim().equals("DIRECTOR") || level.trim().equals("SM")) {

                            String sqlx2 = " insert into travelreport (doc_no,doc_status,from_date,to_date,province,create_date,create_by,approve_by) values  ";
                            sqlx2 += "('" + docno + "','NEW','" + fromdate + "','" + todate + "','" + country + "',current,'" + requserid + "','"+ appbys +"')";
                            st.executeUpdate(sqlx2);

                        }

                    }

//                    
//                    if (Advanceflag.trim().equals("Y")) {  // send advance to finance
//
//                        String sqlx = " insert into  advance_log (doc_no,create_date,create_by,emp_code,app_status) values  ";
//                        sqlx += "('" + docno + "',CURRENT,'" + createby + "','" + requserid + "','')";
//                        st.executeUpdate(sqlx);
//
//                    }
//                              if (allowflag.trim().equals("N")){  // send allowance to payroll
//                                  
//                                  if(isumallow > 0){
//                                        String sqlx = " insert into  allowance_log (doc_no,create_date,emp_code,status) values  ";
//                                        sqlx += "('"+ docno +"',CURRENT,'"+ requserid +"','N')";
//                                        st.executeUpdate(sqlx);
//                                  } 
//               
//                              }
//                              
//                           String from = "suttipong.t@mitsubishielevator.co.th";
//                           String to = "suttipong.t@mitsubishielevator.co.th";
                    String from = mailapp;    // เป็นเมลคนที่อนุมัติ
                    String to = requestmail;  // เป็นเมลคนที่ทำให้

                    conn.commit();

                    conn.close();

                    sendmailback(from, to, docno, nameapp, status);

                    return "OK";

                } catch (Exception ex) {
                    conn.rollback();
                    ex.printStackTrace();
                    return "";
                }

            } else {

                Integer inext = Integer.parseInt(Step) + 1;
                String reqd = getmailuser(createby);
                String req[] = reqd.split("\\^");
                requestmail = req[0].trim();
                requestmail += "@mitsubishielevator.co.th";

                String reqm = getmailuser(requserid); // ชื่อพนักงานที่ขอ
                String reqt[] = reqm.split("\\^");
                nameapp = reqt[1].trim();

                String appd = getmailuser(Nextcode);
                String app[] = appd.split("\\^");
                mailapp = app[0].trim();
                mailapp += "@mitsubishielevator.co.th";

                if (app[0].trim().equals("GA")) {
                    mailapp = "GA";
                }

                try {

                    Statement st = conn.createStatement();

                    String sqld = " update apvlog set app_status='Y',app_date=CURRENT,remark='" + remark + "' ,stepnow='' ";
                    sqld += " where doc_type='TRAV' and doc_no='" + docno + "' and emp_code='" + requserid + "' and stepid='" + Step + "' and next_code='" + Nextcode + "' ";
                    st.executeUpdate(sqld);

                    String sqlv = " update apvlog set stepnow='Y' ";
                    sqlv += " where doc_type='TRAV' and doc_no='" + docno + "' and emp_code='" + requserid + "' and stepid='" + String.valueOf(inext) + "' and app_code='" + Nextcode + "' ";
                    st.executeUpdate(sqlv);

                    String from = requestmail;  // เป็นเมลคนที่ทำให้
                    String to = mailapp; // ชื่อพนักงานอนุมัติ คนต่อไป

                    if (!mailapp.trim().equals("GA")) {
                        sendmailnext(requserid, from, to, docno, nameapp, sprovince);
                    }

                    conn.commit();

                } catch (Exception ex) {
                    conn.rollback();
                    ex.printStackTrace();
                    return "";
                }

            }

            conn.close();
            return "OK";

            //  String Director1012 = getdirector(requserid);
//            if (Nextcode.equals("") || Nextcode == null){   // Last Step GA 
//                
//                try {
//                    
//                    
//                        String reqd = getmailuser(requserid);
//                        String req[] = reqd.split("\\^");
//                        requestmail = req[0].trim();
//                        requestmail += "@mitsubishielevator.co.th";
//
//
//                        String appd = getmailuser(appid);
//                        String app[] = appd.split("\\^");
//                        mailapp = app[0].trim();
//                        mailapp += "@mitsubishielevator.co.th";
//                        nameapp = app[1].trim();
//                        
//
//                            Statement st = conn.createStatement();
//
//                            String sqld = " update apvlog set app_status='Y',app_date=CURRENT,remark='" + remark + "' ";
//                            sqld += " where doc_type='TRAV' and doc_no='" + docno + "' and emp_code='" + requserid + "' and stepid='" + Step + "' and app_code='" + appid + "' ";
//                            st.executeUpdate(sqld);
//                            
////
//                            String sqls = " update stytrave set doc_status='APPROVE' ";
//                            sqls += " where  doc_no='" + docno + "'  ";
//                            st.executeUpdate(sqls);
//
//
////                            String from = "suttipong.t@mitsubishielevator.co.th";
////                            String to = "suttipong.t@mitsubishielevator.co.th";
//
//                             String from = mailapp;
//                             String to = requestmail;
//
//                            sendmailback(from,to,docno,nameapp,status);
//
//                            conn.commit();
//                            
//                            conn.close();
//                            return "OK";
//
//
//                        } catch (Exception ex) {
//                            conn.rollback();
//                            ex.printStackTrace();
//                            return "";
//                        }
//            
//            }
//            if (Nextcode.equals("") || Nextcode == null){  // last step
//                
//                String co = getcountryto(docno);
//                if (!co.equals("TH") && !co.equals("LA") && !co.equals("KH") && !co.equals("MM")){ // case ต่างประเทศ
//                    
//                    String trby = gettravelby(docno);
//                    if (trby.equals("AIR")){// เดินทางเครื่องบิน
//                    
//                    
//                    if ((requserid.equals(Mdcode)) || (requserid.equals(Director1012))){
//                    
//                        // special for md,director
//                        String reqd = getmailuser(requserid);
//                        String req[] = reqd.split("\\^");
//                        requestmail = req[0].trim();
//                        requestmail += "@mitsubishielevator.co.th";
//
//
//                        String appd = getmailuser(appid);
//                        String app[] = appd.split("\\^");
//                        mailapp = app[0].trim();
//                        mailapp += "@mitsubishielevator.co.th";
//                        nameapp = app[1].trim();
//
//                        try {
//
//                            Statement st = conn.createStatement();
//
//                            String sqld = " update apvlog set app_status='Y',app_date=CURRENT,remark='" + remark + "' ";
//                            sqld += " where doc_type='TRAV' and doc_no='" + docno + "' and emp_code='" + requserid + "' and stepid='" + Step + "' and app_code='" + appid + "' ";
//                            st.executeUpdate(sqld);
//                            
//
//                            String sqls = " update stytrave set doc_status='APPROVE' ";
//                            sqls += " where  doc_no='" + docno + "'  ";
//                            st.executeUpdate(sqls);
//
//
////                            String from = "suttipong.t@mitsubishielevator.co.th";
////                            String to = "suttipong.t@mitsubishielevator.co.th";
//
//                             
//                           String from = mailapp;
//                           String to = requestmail;
//
//                            sendmailback(from,to,docno,nameapp,status);
//
//                            conn.commit();
//
//
//                        } catch (Exception ex) {
//                            conn.rollback();
//                            ex.printStackTrace();
//                            return "";
//                        }
//                        
//                        
//                    
//                    }else{  // normal employee 
//                        
//                        
//                        if (appid.equals(Mdcode)){  // last step 
//                            
//                            
//                            // special for md,director
//                            String reqd = getmailuser(requserid);
//                            String req[] = reqd.split("\\^");
//                            requestmail = req[0].trim();
//                            requestmail += "@mitsubishielevator.co.th";
//
//
//                            String appd = getmailuser(appid);
//                            String app[] = appd.split("\\^");
//                            mailapp = app[0].trim();
//                            mailapp += "@mitsubishielevator.co.th";
//                            nameapp = app[1].trim();
//
//                            try {
//
//                                Statement st = conn.createStatement();
//
//                                String sqld = " update apvlog set app_status='Y',app_date=CURRENT,remark='" + remark + "' ";
//                                sqld += " where doc_type='TRAV' and doc_no='" + docno + "' and emp_code='" + requserid + "' and stepid='" + Step + "' and app_code='" + appid + "' ";
//                                st.executeUpdate(sqld);
//                               
//                                // LAST Step to GA
////                            Integer inext = Integer.parseInt(Step) + 1;
////                            String sqlc = " insert into  apvlog (doc_type,doc_no,create_date,emp_code,stepid,app_code,next_code) values  ";
////                                    sqlc += "('TRAV','" + docno + "',CURRENT,'" + requserid + "','" + String.valueOf(inext) + "','" + ga + "','GA')";
////                                    st.executeUpdate(sqlc);
////                                    
////                            sendmailtoGA(requestmail, gamail, docno, nameapp);       
//
//                                String sqls = " update stytrave set doc_status='APPROVE' ";
//                                sqls += " where  doc_no='" + docno + "'  ";
//                                st.executeUpdate(sqls);
//
////
////                                String from = "suttipong.t@mitsubishielevator.co.th";
////                                String to = "suttipong.t@mitsubishielevator.co.th";
//
//                                 String from = mailapp;
//                                 String to = requestmail;
////
//                                sendmailback(from,to,docno,nameapp,status);
//
//                                conn.commit();
//
//
//                            } catch (Exception ex) {
//                                conn.rollback();
//                                ex.printStackTrace();
//                                return "";
//                            }
//                            
//                            
//                        
//                        }else{   
//                            
//                            String sdirectcode = Director1012;
//                            String sAppstep = "";
//                            String sNextstep = "";
//                            
//                            
//                            
//                            if (appid.equals(sdirectcode)) {  // กรณี step นี้เป็น director แล้ว ไม่ต้องส่ง อีกให้ไป MD เลย
//
//                                String reqd = getmailuser(requserid);
//                                String req[] = reqd.split("\\^");
//                                requestmail = req[0].trim();
//                                requestmail += "@mitsubishielevator.co.th";
//                                nameapp = req[1].trim();
//
//                                String appd = getmailuser(Mdcode);
//                                String app[] = appd.split("\\^");
//                                mailapp = app[0].trim();
//                                mailapp += "@mitsubishielevator.co.th";
//
//                                try {
//
//                                    Statement st = conn.createStatement();
//
//                                    String sqld = " update apvlog set app_status='Y',app_date=CURRENT,remark='" + remark + "' ,next_code='" + Mdcode + "' ";
//                                    sqld += " where doc_type='TRAV' and doc_no='" + docno + "' and emp_code='" + requserid + "' and stepid='" + Step + "' and app_code='" + appid + "' ";
//                                    st.executeUpdate(sqld);
//
//                                    Integer inext = Integer.parseInt(Step) + 1;
//
//                                    String sqlc = " insert into  apvlog (doc_type,doc_no,create_date,emp_code,stepid,app_code,next_code) values  ";
//                                    sqlc += "('TRAV','" + docno + "',CURRENT,'" + requserid + "','" + String.valueOf(inext) + "','" + Mdcode + "','')";
//                                    st.executeUpdate(sqlc);
//
////                                String from = "suttipong.t@mitsubishielevator.co.th";
////                                String to = "suttipong.t@mitsubishielevator.co.th";
//
//                                    String from = requestmail;
//                                    String to = mailapp;
//
//                                    sendmailnext(from, to, docno, nameapp);
//
//                                    conn.commit();
//
//
//                                } catch (Exception ex) {
//                                    conn.rollback();
//                                    ex.printStackTrace();
//                                    return "";
//                                }
//
//
//
//                            } else {  
//                                
//                                // ส่งหา GA , director กรณีที่เป็น ต่างประเทศ
//                                
//                                String reqd = getmailuser(requserid);
//                                String req[] = reqd.split("\\^");
//                                requestmail = req[0].trim();
//                                requestmail += "@mitsubishielevator.co.th";
//                                nameapp = req[1].trim();
//
//                                String appd = getmailuser(sdirectcode);
//                                String app[] = appd.split("\\^");
//                                mailapp = app[0].trim();
//                                mailapp += "@mitsubishielevator.co.th";
//
//                                try {
//
//                                    Statement st = conn.createStatement();
//
//                                    String sqld = " update apvlog set app_status='Y',app_date=CURRENT,remark='" + remark + "' ,next_code='" + ga + "' ";
//                                    sqld += " where doc_type='TRAV' and doc_no='" + docno + "' and emp_code='" + requserid + "' and stepid='" + Step + "' and app_code='" + appid + "' ";
//                                    st.executeUpdate(sqld);
//
//                                    Integer inext = Integer.parseInt(Step) + 1;
//
//
//                                    String sqlc = " insert into  apvlog (doc_type,doc_no,create_date,emp_code,stepid,app_code,next_code) values  ";
//                                    sqlc += "('TRAV','" + docno + "',CURRENT,'" + requserid + "','" + String.valueOf(inext) + "','" + ga + "','" + sdirectcode + "')";
//                                    st.executeUpdate(sqlc);
//
////                                String from = "suttipong.t@mitsubishielevator.co.th";
////                                String to = "suttipong.t@mitsubishielevator.co.th";
//
//                                    String from = requestmail;
//                                    String to = mailapp;
//
//                                    sendmailnext(from, to, docno, nameapp);
//
//                                    conn.commit();
//
//
//                                } catch (Exception ex) {
//                                    conn.rollback();
//                                    ex.printStackTrace();
//                                    return "";
//                                }
//
//                            }
//                        
//                        }  // !=  (appid.equals(Mdcode))
//                    
//                    }
//                    
//                    }  // ไปเครื่องบิน
//                    else{
//                        
//                        // ไปต่างประเทศแต่เดินทางปกติ
//                        String reqd = getmailuser(requserid);
//                        String req[] = reqd.split("\\^");
//                        requestmail = req[0].trim();
//                        requestmail += "@mitsubishielevator.co.th";
//
//
//                        String appd = getmailuser(appid);
//                        String app[] = appd.split("\\^");
//                        mailapp = app[0].trim();
//                        mailapp += "@mitsubishielevator.co.th";
//                        nameapp = app[1].trim();
//
//                        try {
//
//                            Statement st = conn.createStatement();
//
//                            String sqld = " update apvlog set app_status='Y',app_date=CURRENT,remark='" + remark + "',next_code='" + ga + "' ";
//                            sqld += " where doc_type='TRAV' and doc_no='" + docno + "' and emp_code='" + requserid + "' and stepid='" + Step + "' and app_code='" + appid + "' ";
//                            st.executeUpdate(sqld);
//                            
//                            
//                                // LAST Step to GA
//                            Integer inext = Integer.parseInt(Step) + 1;
//                            String sqlc = " insert into  apvlog (doc_type,doc_no,create_date,emp_code,stepid,app_code,next_code) values  ";
//                                    sqlc += "('TRAV','" + docno + "',CURRENT,'" + requserid + "','" + String.valueOf(inext) + "','" + ga + "','')";
//                                    st.executeUpdate(sqlc);
//                                    
//                            sendmailtoGA(requestmail, gamail, docno, nameapp); 
//
////                            String sqls = " update stytrave set doc_status='APPROVE' ";
////                            sqls += " where  doc_no='" + docno + "'  ";
////                            st.executeUpdate(sqls);
//
//
////                            String from = "suttipong.t@mitsubishielevator.co.th";
////                            String to = "suttipong.t@mitsubishielevator.co.th";
//
////                            String from = mailapp;
////                            String to = requestmail;
//
//                           // sendmailback(from,to,docno,nameapp,status);
//                            
//                          //  sendmailback(from,to,docno,nameapp,status);
//
//                            conn.commit();
//
//
//                        } catch (Exception ex) {
//                            conn.rollback();
//                            ex.printStackTrace();
//                            return "";
//                        }
//                        
//                       
//                    }
//                    
//                    
//                }else{ // co == "TH  co == MM  co == KH  co == LA  ในประเทศ 
//                    
//                    String trby = gettravelby(docno);
//                    if (!trby.equals("AIR")){// เดินทางปกติ
//                        
//                        String reqd = getmailuser(requserid);
//                        String req[] = reqd.split("\\^");
//                        requestmail = req[0].trim();
//                        requestmail += "@mitsubishielevator.co.th";
//
//
//                        String appd = getmailuser(appid);
//                        String app[] = appd.split("\\^");
//                        mailapp = app[0].trim();
//                        mailapp += "@mitsubishielevator.co.th";
//                        nameapp = app[1].trim();
//
//                        try {
//
//                            Statement st = conn.createStatement();
//
////                            String sqld = " update apvlog set app_status='Y',app_date=CURRENT,remark='" + remark + "'";
////                            sqld += " where doc_type='TRAV' and doc_no='" + docno + "' and emp_code='" + requserid + "' and stepid='" + Step + "' and app_code='" + appid + "' ";
////                            st.executeUpdate(sqld);
////                            
////                            
//                            
//                            if (!appd.equals(ga)){  // ไม่เป็น GA last step
//                                
//                                String sqld = " update apvlog set app_status='Y',app_date=CURRENT,remark='" + remark + "' ,next_code='" + ga + "'  ";
//                                sqld += " where doc_type='TRAV' and doc_no='" + docno + "' and emp_code='" + requserid + "' and stepid='" + Step + "' and app_code='" + appid + "' ";
//                                st.executeUpdate(sqld);
//                            
//                                Integer inext = Integer.parseInt(Step) + 1;
//                                String sqlc = " insert into  apvlog (doc_type,doc_no,create_date,emp_code,stepid,app_code,next_code) values  ";
//                                    sqlc += "('TRAV','" + docno + "',CURRENT,'" + requserid + "','" + String.valueOf(inext) + "','" + ga + "','GA')";
//                                    st.executeUpdate(sqlc);
//                                    
//                                sendmailtoGA(requestmail, gamail, docno, nameapp);  
//                            
//                            }else{
//                                
//                                String sqld = " update apvlog set app_status='Y',app_date=CURRENT,remark='" + remark + "'";
//                                sqld += " where doc_type='TRAV' and doc_no='" + docno + "' and emp_code='" + requserid + "' and stepid='" + Step + "' and app_code='" + appid + "' ";
//                                st.executeUpdate(sqld);
//                                
//                                String sqls = " update stytrave set doc_status='APPROVE' ";
//                                sqls += " where  doc_no='" + docno + "'  ";
//                                st.executeUpdate(sqls);
//
//
//    //                            String from = "suttipong.t@mitsubishielevator.co.th";
//    //                            String to = "suttipong.t@mitsubishielevator.co.th";
//
//                                String from = mailapp;
//                                String to = requestmail;
//
//                                sendmailback(from,to,docno,nameapp,status);
//                            
//                            }
//                            
//                                // LAST Step to GA
//                            
//
//                            
//                         //   sendmailback(from,to,docno,nameapp,status);
//
//                            conn.commit();
//
//
//                        } catch (Exception ex) {
//                            conn.rollback();
//                            ex.printStackTrace();
//                            return "";
//                        }
//                        
//                    
//                    }else{  //  == AIR in thailand, LAO, MM,KH // เดินทางเครื่องบินในประเทศ
//                        
//                       
//                        
//                        String sdirectcode = Director1012;
//                        if (appid.equals(sdirectcode)) {   // last step
//                            
//                            // director last step
//                            
//                            String reqd = getmailuser(requserid);
//                            String req[] = reqd.split("\\^");
//                            requestmail = req[0].trim();
//                            requestmail += "@mitsubishielevator.co.th";
//
//
//                            String appd = getmailuser(appid);
//                            String app[] = appd.split("\\^");
//                            mailapp = app[0].trim();
//                            mailapp += "@mitsubishielevator.co.th";
//                            nameapp = app[1].trim();
//
//                            try {
//
//                                Statement st = conn.createStatement();
//
//                                String sqld = " update apvlog set app_status='Y',app_date=CURRENT,remark='" + remark + "' ,next_code='" + ga + "'  ";
//                                sqld += " where doc_type='TRAV' and doc_no='" + docno + "' and emp_code='" + requserid + "' and stepid='" + Step + "' and app_code='" + appid + "' ";
//                                st.executeUpdate(sqld);
//                                
//                                
//                                    // LAST Step to GA
//                            Integer inext = Integer.parseInt(Step) + 1;
//                            String sqlc = " insert into  apvlog (doc_type,doc_no,create_date,emp_code,stepid,app_code,next_code) values  ";
//                                    sqlc += "('TRAV','" + docno + "',CURRENT,'" + requserid + "','" + String.valueOf(inext) + "','" + ga + "','GA')";
//                                    st.executeUpdate(sqlc);
//                                    
//                            sendmailtoGA(requestmail, gamail, docno, nameapp); 
//
////                                String sqls = " update stytrave set doc_status='APPROVE' ";
////                                sqls += " where  doc_no='" + docno + "'  ";
////                                st.executeUpdate(sqls);
//
//
////                                String from = "suttipong.t@mitsubishielevator.co.th";
////                                String to = "suttipong.t@mitsubishielevator.co.th";
//
////                                String from = mailapp;
////                                String to = requestmail;
////
////                                sendmailback(from,to,docno,nameapp,status);
//
//                                conn.commit();
//
//
//                            } catch (Exception ex) {
//                                conn.rollback();
//                                ex.printStackTrace();
//                                return "";
//                            }
//                            
//                            
//                        } else {
//
//                            String reqd = getmailuser(requserid);
//                            String req[] = reqd.split("\\^");
//                            requestmail = req[0].trim();
//                            requestmail += "@mitsubishielevator.co.th";
//                            nameapp = req[1].trim();
//
//                            String appd = getmailuser(sdirectcode);
//                            String app[] = appd.split("\\^");
//                            mailapp = app[0].trim();
//                            mailapp += "@mitsubishielevator.co.th";
//
//                            try {
//
//                                Statement st = conn.createStatement();
//
//                                String sqld = " update apvlog set app_status='Y',app_date=CURRENT,remark='" + remark + "' ,next_code='" + sdirectcode + "' ";
//                                sqld += " where doc_type='TRAV' and doc_no='" + docno + "' and emp_code='" + requserid + "' and stepid='" + Step + "' and app_code='" + appid + "' ";
//                                st.executeUpdate(sqld);
//
//                                Integer inext = Integer.parseInt(Step) + 1;
//
//                                String sqlc = " insert into  apvlog (doc_type,doc_no,create_date,emp_code,stepid,app_code,next_code) values  ";
//                                sqlc += "('TRAV','" + docno + "',CURRENT,'" + requserid + "','" + String.valueOf(inext) + "','" + sdirectcode + "','')";
//                                st.executeUpdate(sqlc);
//
////                                String from = "suttipong.t@mitsubishielevator.co.th";
////                                String to = "suttipong.t@mitsubishielevator.co.th";
//
//                                String from = requestmail;
//                                String to = mailapp;
//
//                                sendmailnext(from, to, docno, nameapp);
//
//                                conn.commit();
//
//
//                            } catch (Exception ex) {
//                                conn.rollback();
//                                ex.printStackTrace();
//                                return "";
//                            }
//
//                        
//                        } 
//                    
//                    }
//                    
//                    
//                   
//                } // co == "TH"
//                
//                
//                
//                
//                //===================  auto step
//                
//            
//            } else{  // goto next step nextstep is not null
//                
//                //getnextstep
//                 String nextstepcode = "";
//                 Integer sstr = Integer.parseInt(Step);
//                 sstr = sstr + 1;
//                 
//                 String snextp = String.valueOf(sstr);
//          
//                 nextstepcode = getnextstep(requserid,String.valueOf(sstr+2));
//                
//                    String reqd =  getmailuser(requserid);
//                    String req[] = reqd.split("\\^");
//                    requestmail = req[0].trim();
//                    requestmail += "@mitsubishielevator.co.th";
//                    nameapp = req[1].trim();
//                    
//                    
//                    String appd = getmailuser(Nextcode);
//                            String app[] = appd.split("\\^");
//                            mailapp = app[0].trim();
//                            mailapp += "@mitsubishielevator.co.th";
//                            
//                
//                try {
//                  
//                        Statement st = conn.createStatement();
//
//                        String sqld = " update apvlog set app_status='Y',app_date=CURRENT,remark='"+ remark +"'";
//                                sqld += " where doc_type='TRAV' and doc_no='"+ docno +"' and emp_code='"+ requserid +"' and stepid='"+ Step +"' and app_code='"+ appid +"' ";
//                        st.executeUpdate(sqld);
//                        
//                        String sqlc = " insert into  apvlog (doc_type,doc_no,create_date,emp_code,stepid,app_code,next_code) values  ";
//                        sqlc += "('TRAV','"+ docno +"',CURRENT,'"+ requserid +"','"+ snextp +"','"+ Nextcode +"','"+ nextstepcode +"')";
//                        st.executeUpdate(sqlc);
//                        
////                        String from = "suttipong.t@mitsubishielevator.co.th";
////                        String to = "suttipong.t@mitsubishielevator.co.th";
//
//                        String from = requestmail;
//                        String to = mailapp;
//
//                        sendmailnext(from,to,docno,nameapp);
//
//                        conn.commit();
//
//
//                    }catch (Exception ex) {
//                        conn.rollback(); 
//                        ex.printStackTrace();
//                        return "";
//                    }
//                
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String getcountryto(String docno) {

        try {

            Boolean flag = true;
            String co = "";

            String sql = " select * from stytrave where doc_no='" + docno + "'  ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                co = rs.getString("country_code");
            }

            return co.trim();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String gettravelby(String docno) {

        try {

            Boolean flag = true;
            String co = "";
            String co2 = "";

            String sql = " select * from stytrave where doc_no='" + docno + "'  ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                co = rs.getString("de_by");
                co2 = rs.getString("re_by");
            }

            if (co.trim().equals("AIR")) {
                return co.trim();
            } else {
                return co2.trim();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String gettravelcountry(String docno) {

        try {

            Boolean flag = true;
            String co = "";

            String sql = " select country_code from stytrave where doc_no='" + docno + "'  ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                co = rs.getString("country_code");
            }

            return co.trim();

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String getnextstep(String empcode, String step) {

        try {

            String co = "";

            String sql = " select * from apvstep where stepid='" + step + "' and emp_code='" + empcode + "'  ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                co = rs.getString("app_code");
            }

            return co.trim();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String getdirector(String empcode) {

        try {

            String Director10 = "02802";
            String Director12 = "90049";

            String dep10 = "";

            String sql = "  select d2.src_char from stxemphr e ";
            sql += " inner join stxinfor d1 on e.department = d1.src_key and d1.src_type='DEPARTMENT' ";
            sql += " inner join stxinfor d2 on d1.src_char = d2.src_key and d2.src_type='DIVISION' ";
            sql += " where e.emp_code='" + empcode + "'";

//          String sql = " select department from stxemphr ";
//          sql += "where emp_code='"+ empcode +"' and department in (select src_key from stxinfor where src_type='DEPFLOOR10')";
            // String sql = " select * from apvstep where stepid='"+ step +"' and emp_code='"+ empcode +"'  ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                dep10 = rs.getString("src_char");
            }

            if (dep10.trim().equals("") || dep10 == null) {

                return "";

            } else {
                return dep10;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String getAPP(String type) {

        try {

            String ga = "";

            String sql = "  select  src_key from stxinfor where src_type='" + type + "' ";

//          String sql = " select department from stxemphr ";
//          sql += "where emp_code='"+ empcode +"' and department in (select src_key from stxinfor where src_type='DEPFLOOR10')";
            // String sql = " select * from apvstep where stepid='"+ step +"' and emp_code='"+ empcode +"'  ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                ga = rs.getString("src_key");
            }

            if (ga.trim().equals("") || ga == null) {

                return "";

            } else {
                return ga.trim();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String getccmail() {

        try {

            String ga = "";
            String prex = "";

            String sql = "  select  src_key from stxinfor where src_type='CCAIR' ";

//          String sql = " select department from stxemphr ";
//          sql += "where emp_code='"+ empcode +"' and department in (select src_key from stxinfor where src_type='DEPFLOOR10')";
            // String sql = " select * from apvstep where stepid='"+ step +"' and emp_code='"+ empcode +"'  ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                ga += prex + rs.getString("src_key");
                prex = "|";
            }

            if (ga.trim().equals("") || ga == null) {

                return "";

            } else {
                return ga.trim();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String sendmailnext(String empcode, String from, String to, String docno, String namefrom, String sprovince) {

//          try {         //  String from = "suttipong.t@mitsubishielevator.co.th";
//             //     String to = "suttipong.t@mitsubishielevator.co.th";
//
//             //                String from = mailapp;
//             //                String to = requestmail;
//
//             SmtpClient client = new SmtpClient("mail.mitsubishielevator.co.th");
//             client.from(from);
//             client.to(to);
//
//             PrintStream message = client.startMessage();
//             message.println("To: " + to);
//             // message.println("Cc: " + cc);
//
//             // message.println("Cc: " + cc);
//
//             message.println("Subject:Travelling Requst Doc no: " + docno + " from:" + namefrom);
//             message.println("");
//             message.println("");
//             message.println("K. " + namefrom + "  request Travelling ");
//             message.println("");
//             message.println("Please Approve url >> http://150.152.182.216:8088/travelling/approve.jsp?id=" + docno);
//            // message.println("Please Approve url >> http://150.152.190.202:8088/travelling/approve.jsp?id=" + docno);
//            // message.println("Please Approve url >> http://150.152.152.20:8084/travelling/approve.jsp?id=" + docno);
//            
//             
//             message.println("");
//             message.println("================================================");
//
//             client.closeServer();
//
//             return "";
//         } catch (Exception ex) {
//             ex.printStackTrace();
//             return "";
//         }
//          
        String tbr = "";
        tbr += " <table  style='background-color:lightsteelblue;color:#006699;' width='600px'> ";
        tbr += " <tr> ";
        tbr += "  <td colspan='2' style='background-color:white;' > <b> Traveling Online : " + docno + " </b></td>  ";
        tbr += "  </tr> ";
        tbr += "  <tr> ";
        tbr += " <td width='90px'> ";
        tbr += " <img  src='http://150.152.182.224/documentation/photo/" + empcode.trim() + ".jpg' width='90px' height='100px' /> ";
        tbr += " </td> ";
        tbr += " <td valign='top'> ";
        tbr += " <table width='100%' style='background-color:white;color:black;'> ";
        tbr += "              <tr> ";
        tbr += "                  <td width='100px' ><b>Empcode:</b> </td><td aligh='left' style=' font-size: 14px;color: red'><b>" + empcode + "</b></td> ";
        tbr += "              </tr> ";
        tbr += "              <tr> ";
        tbr += "                  <td width='100px' ><b>Name:</b> </td><td> " + namefrom + "</td> ";
        tbr += "              </tr> ";
        tbr += "              <tr> ";
        tbr += "                  <td width='100px' ><b>Province:</b> </td><td> " + sprovince + "</td> ";
        tbr += "              </tr> ";
        tbr += "              <tr> ";
        tbr += "                  <td colspan='2' align='center'> ";
        tbr += "                  <hr /> ";
        tbr += "                  Plaese Approve  <a href='http://150.152.182.206:8088/travelling/approve.jsp?id=" + docno + "' > Click Here </a>  ";
        tbr += "                   <hr /> ";
        tbr += "                  </td> ";
        tbr += "              </tr> ";
        tbr += " </table> ";
        tbr += " </td> ";
        tbr += " </tr> ";
        tbr += "  </table> ";

        String SMTP_HOST_NAME = "mail.mitsubishielevator.co.th"; //ขึ้นอยู่กับผู้ให้บริการ
        String SMTP_PORT = "25";//ขึ้นอยู่กับผู้ให้บริการ
        String FROM = from;
        String TO = to;

        String SUBJECT = "Subject:Travelling Requst Doc no: " + docno + " (" + sprovince + ") from:" + namefrom;
        String MESSAGE = "<br><br>K. " + namefrom + "  request Travelling ";
        MESSAGE += "<br>================================================ <br>" + tbr;
        //  MESSAGE += "Please Approve url >>> <a href='http://150.152.182.216:8088/travelling/approve.jsp?id=" + docno+ "'> http://150.152.182.216:8088/travelling/approve.jsp?id=" + docno + " </a> ";

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);

        Session session = Session.getInstance(props, null);
        session.setDebug(false);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            InternetAddress[] to_address = InternetAddress.parse(TO);
            message.setRecipients(Message.RecipientType.TO, to_address);

            String xccmail = "traveling-system@mitsubishielevator.co.th";
            InternetAddress[] cc_address = InternetAddress.parse(xccmail);
            message.setRecipients(Message.RecipientType.CC, cc_address);

            message.setSubject(SUBJECT);
            message.addHeader("X-Priority", "1");
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setText(MESSAGE, "UTF-8");
            mbp.setHeader("Content-Type", "text/html; charset=\"utf-8\"");

            Multipart content = new MimeMultipart();
            content.addBodyPart(mbp);

            message.setContent(content);
            Transport.send(message);

            return "";

        } catch (MessagingException mex) {
            return "";

        } catch (Exception e) {
            return "";
        } finally {
            return "";
        }

    }

    public String sendmailtoGA(String from, String to, String docno, String namefrom) {

        String SMTP_HOST_NAME = "mail.mitsubishielevator.co.th"; //ขึ้นอยู่กับผู้ให้บริการ
        String SMTP_PORT = "25";//ขึ้นอยู่กับผู้ให้บริการ
        String FROM = from;

        String TO = to;

        String SUBJECT = "Subject:Travelling Requst Doc no: " + docno + " from:" + namefrom;
        String MESSAGE = "<br><br>K. " + namefrom + "  request Travelling ";
        MESSAGE += "<br>================================================ <br>";
        MESSAGE += "Please Approve url >> http://150.152.182.206:8088/travelling/approve.jsp?id=" + docno;

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);

        Session session = Session.getInstance(props, null);
        session.setDebug(false);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            InternetAddress[] to_address = InternetAddress.parse(TO);
            message.setRecipients(Message.RecipientType.TO, to_address);

            message.setSubject(SUBJECT);
            message.addHeader("X-Priority", "1");
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setText(MESSAGE, "UTF-8");
            mbp.setHeader("Content-Type", "text/html; charset=\"utf-8\"");

            Multipart content = new MimeMultipart();
            content.addBodyPart(mbp);

            message.setContent(content);
            //   Transport.send(message);

            return "";

        } catch (MessagingException mex) {
            return "";

        } catch (Exception e) {
            return "";
        } finally {
            return "";
        }

    }

    public String sendmailback3(String from, String to, String docno, String nameapprove, String status) {

        try {         //  String from = "suttipong.t@mitsubishielevator.co.th";
            //     String to = "suttipong.t@mitsubishielevator.co.th";

            //                String from = mailapp;
            //                String to = requestmail;
            // String GA = "00389";
            String GA = "93955";
            String reqga = getmailuser(GA);
            String reqgan[] = reqga.split("\\^");
            String gamail = reqgan[0].trim();
            gamail += "@mitsubishielevator.co.th";

            SmtpClient client = new SmtpClient("mail.mitsubishielevator.co.th");
            client.from(from);
            client.to(to);

            PrintStream message = client.startMessage();
            message.println("To: " + to);
            if (status.equals("APPROVE")) {

                message.println("CC: " + gamail);

            }

            message.println("Subject:" + status + " Travelling Requst Doc no: " + docno + " from:" + nameapprove);
            message.println("");
            message.println("");
            message.println("K. " + nameapprove + "  " + status + "  Travelling Doc no: " + docno);
            message.println("");
            message.println("");
            message.println("================================================");
            message.println("");
            message.println("Please See detail >> http://150.152.182.206:8088/travelling");
            //  message.println("Please See detail >> http://150.152.190.202:8088/travelling/");
            //  message.println("Please See detail >> http://150.152.152.20:8084/travelling/");

            client.closeServer();

            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String getapprovestatus(String docno, String empcode) {

        try {

            String co = "";

            String sql = " select * from apvlog where doc_no='" + docno + "' and app_code='" + empcode + "' and  app_status is null ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                co = rs.getString("app_code").trim();
            }

            if (co.equals("") || co == null) {
                return "";
            }
            return "OK";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String getapvlog(String docno) {

        try {

            String co = "";

            String sql = " select a.*,b.emp_name from apvlog a "
                    + " inner join stytrave e on e.doc_no = a.doc_no "
                    + " inner join stxemphr b on a.app_code = b.emp_code where a.doc_no='" + docno + "' and a.emp_code=e.emp_code  order by a.stepid asc ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {

                String step = rs.getString("stepid");
                String sname = rs.getString("emp_name");
                String status = rs.getString("app_status");
                String sdate = rs.getString("app_date");
                String remark = rs.getString("remark");

                if (step == null) {
                    step = "";
                }
                if (sname == null) {
                    sname = "";
                }
                if (status == null) {
                    status = "";
                }
                if (sdate == null) {
                    sdate = "";
                }
                if (remark == null) {
                    remark = "";
                }

                co += step + "^" + sname + "^" + status + "^" + sdate + "^" + remark + "|";
            }

            if (co.equals("") || co == null) {
                return "";
            }
            return co;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String sendmailback(String from, String to, String docno, String nameapprove, String status) {

        // String CC = getAPP("AIRUSR"); 
        String trby = gettravelby(docno);

        String scountry = gettravelcountry(docno);

        String SMTP_HOST_NAME = "mail.mitsubishielevator.co.th"; //ขึ้นอยู่กับผู้ให้บริการ
        String SMTP_PORT = "25";//ขึ้นอยู่กับผู้ให้บริการ
        String FROM = from;
        String TO = to;

        String SUBJECT = status + " Travelling Requst Doc no: " + docno + " from:" + nameapprove;
        String MESSAGE = "<br><br>K. " + nameapprove + "  " + status + "  Travelling Doc no: " + docno;
        MESSAGE += "<br><br>================================================ ";
        MESSAGE += "<br>Please See detail >>> <a href='http://150.152.182.206:8088/travelling'> http://150.152.182.206:8088/travelling </a> ";

//
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);

        Session session = Session.getInstance(props, null);
        session.setDebug(false);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            InternetAddress[] to_address = InternetAddress.parse(TO);
            if (status.equals("APPROVE")) {

                message.setRecipients(Message.RecipientType.TO, to_address);

                if (trby.equals("AIR")) {// เดินทางเครื่องบิน 

                    if ((!scountry.equals("TH")) && (!scountry.equals(""))) { //ไปต่างประเทศ

                        String ccmail = getccmail();
                        String reqgan[] = ccmail.split("\\|");

                        if (reqgan.length > 0) {
                            String xccmail = "";
                            String xprx = "";
                            for (int i = 0; i < reqgan.length; i++) {

                                String ccempcode = reqgan[i].trim();

                                String suser = getmailuser(ccempcode);
                                String arruser[] = suser.split("\\^");
                                xccmail += xprx + arruser[0].trim();
                                xccmail += "@mitsubishielevator.co.th";
                                xprx = ",";

                            }
                            InternetAddress[] cc_address = InternetAddress.parse(xccmail);
                            message.setRecipients(Message.RecipientType.CC, cc_address);

                        }
//                        String gamail = reqgan[0].trim();
//                        gamail += "@mitsubishielevator.co.th";
//                        
//                        InternetAddress[] cc_address = InternetAddress.parse(gamail); 
//                        message.setRecipients(Message.RecipientType.CC, cc_address);

                        MESSAGE += "<br>  GA Link >>> <a href='http://150.152.182.206:8088/travelling/travrep2.jsp?traveno=" + docno.trim() + "'>   Click Here   </a> ";

                    }

                }

            } else {

                //case REJECT
                message.setRecipients(Message.RecipientType.TO, to_address);
            }

            message.setSubject(SUBJECT);
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setText(MESSAGE, "UTF-8");
            mbp.setHeader("X-Priority", "High");
            mbp.setHeader("Content-Type", "text/html; charset=\"utf-8\"");

            Multipart content = new MimeMultipart();
            content.addBodyPart(mbp);

            message.setContent(content);
            Transport.send(message);

            return "";

        } catch (MessagingException mex) {
            return "";

        } catch (Exception e) {
            return "";
        } finally {
            return "";
        }

        //  return "";
    }

    public String checkoldpass(String empcode, String oldpass) {

        try {

            String co = "";

            String sql = " select emp_code from stxemphr where emp_code='" + empcode + "' and user_pwd='" + oldpass + "MMG' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                co = rs.getString("emp_code");
            }

            if (co.trim().equals("") || co == null) {
                return "";
            }
            return "OK";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String updatepassw(String empcode, String oldpass, String newpass) {

        try {

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();

            Statement st = conn.createStatement();

            String sqld = " update stxemphr set user_pwd='" + newpass + "MMG' where emp_code='" + empcode + "' and  user_pwd='" + oldpass + "MMG'";
            st.executeUpdate(sqld);

            conn.close();

            return "OK";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String checkdeptallow(String dept) {

        try {

            String co = "";

            String sql = " select * from stxinfor where src_type='ALLOWANCE' and src_key='" + dept + "' and src_char='Y' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                co = rs.getString("src_key");
            }

            if (co.trim().equals("") || co == null) {
                return "YES";   // เบิก advance
            }
            return "NO";  //ถ้ามี ข้อมูล คือ ไม่เบิก advance
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String getlastapvlog(String travno) {
        try {
            //  String sql = " select * from apvlog where doc_no='"+ travno +"' order by stepid asc";

            String sql = " select l.*,e.emp_name from apvlog l "
                    + " inner join stxemphr e on e.emp_code = l.app_code "
                    + " where l.doc_no='" + travno + "' order by l.stepid asc ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            String apv = "";
            while (rs.next()) {

                String Step = rs.getString("stepid");
                String Empcode = rs.getString("app_code");
                String Empname = rs.getString("emp_name");
                String appdate = rs.getString("app_date");
                String remark = rs.getString("remark");

                apv += Step.trim() + "|" + Empcode + "|" + Empname + "|" + appdate + "|" + remark + "^";
            }

            return apv;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String getconfirmapr(String travno) {

        try {

            String Empcode = "";
            String traveby = "";
            String travereby = "";
            String stp1 = "";
            String apv = "";
            String lastapp = "";
            Integer i = 0;
            String Country = "";
            String sql = " select * from stytrave where  doc_no='" + travno + "'  ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                Empcode = rs.getString("emp_code");
                traveby = rs.getString("de_by");
                travereby = rs.getString("re_by");
                Country = rs.getString("country_code");
            }

            String sdirectcode = getdirector(Empcode);
            String Mdcode = getAPP("MDAPP");

            if (sdirectcode.trim().equals(Empcode.trim())) {

                // ถ้าเป็นระดับ director ให้ไปที่พี่หนูก่อน
                // GA confirm 
                String ga = getAPP("GAAPP");
                String reqd = getmailuser(ga);
                String req[] = reqd.split("\\^");
                String requestmail = req[0].trim();
                // requestmail += "@mitsubishielevator.co.th";
                String xname = req[1].trim();
                i = i + 1;
                apv += "1|" + ga + "|" + xname + "|" + requestmail + "^";

                String appd = getmailuser(Mdcode);
                String app[] = appd.split("\\^");
                String mailapp = app[0].trim();
                //  mailapp += "@mitsubishielevator.co.th";
                String mdname = app[1].trim();
                i = i + 1;
                apv += "2|" + Mdcode + "|" + mdname + "|" + mailapp + "^";

                return apv;

            }

            if (Empcode.trim().equals(Mdcode.trim())) {

                // ถ้าเป็นระดับ MD ให้ไปที่พี่หนูก่อน 
                String ga = getAPP("GAAPP");
                if (!lastapp.trim().equals(ga)) {

                    String reqd = getmailuser(ga);
                    String req[] = reqd.split("\\^");
                    String requestmail = req[0].trim();
                    // requestmail += "@mitsubishielevator.co.th";
                    String xname = req[1].trim();
                    i = i + 1;
                    apv += "1|" + ga + "|" + xname + "|" + requestmail + "^";
                }

                String xdirect = getdirector(ga);

                String appd = getmailuser(xdirect);
                String app[] = appd.split("\\^");
                String mailapp = app[0].trim();
                //  mailapp += "@mitsubishielevator.co.th";
                String mdname = app[1].trim();
                i = i + 1;
                apv += "2|" + xdirect + "|" + mdname + "|" + mailapp + "^";

                return apv;

            }

            String sql2 = " select * from apvstep where emp_code='" + Empcode + "' ";

            ResultSet rs2 = cConnect.getResultSet(sql2);

            while (rs2.next()) {
                i = rs2.getInt("stepid");
                apv += String.valueOf(i) + "|" + rs2.getString("app_Code") + "|" + rs2.getString("app_name") + "|" + rs2.getString("app_email") + "^";
                if (i == 1) {
                    stp1 = rs2.getString("app_Code");
                }
                lastapp = rs2.getString("app_Code");;
            }
            if (apv.equals("")) {
                return "";
            }

            if (traveby.trim().equals("AIR") || travereby.trim().equals("AIR")) {

                //get director
                if (!sdirectcode.trim().equals(Empcode.trim())) {

                    if (!sdirectcode.trim().equals(lastapp.trim())) {
                        if (!sdirectcode.trim().equals("")) {   // last step

                            String reqd = getmailuser(sdirectcode);
                            String req[] = reqd.split("\\^");
                            String requestmail = req[0].trim();
                            // requestmail += "@mitsubishielevator.co.th";
                            String xname = req[1].trim();
                            i = i + 1;
                            apv += String.valueOf(i) + "|" + sdirectcode + "|" + xname + "|" + requestmail + "^";
                        }
                    }

                }

                if (!Country.trim().equals("TH") && !Country.trim().equals("LA") && !Country.trim().equals("KH") && !Country.trim().equals("MM")) {
                    // ไปเครื่องบินต่างประเทศ
                    // String Mdcode =  getAPP("MDAPP");  //"03479";

                    if (!stp1.trim().equals(Mdcode.trim())) {
                        String appd = getmailuser(Mdcode);
                        String app[] = appd.split("\\^");
                        String mailapp = app[0].trim();
                        //  mailapp += "@mitsubishielevator.co.th";
                        String mdname = app[1].trim();
                        i = i + 1;
                        apv += String.valueOf(i) + "|" + Mdcode + "|" + mdname + "|" + mailapp + "^";
                    }
                } else if (Country.trim().equals("LA") || Country.trim().equals("KH") || Country.trim().equals("MM")) {

                    String sql3 = " select level from stxemphr where emp_code='" + Empcode + "' ";

                    ResultSet rs3 = cConnect.getResultSet(sql3);

                    String slevel = "";

                    while (rs3.next()) {
                        slevel = rs3.getString("level");
                    }

                    if (!slevel.trim().equals("S0") && !slevel.trim().equals("S1") && !slevel.trim().equals("S2") && !slevel.trim().equals("S3") && !slevel.trim().equals("SUP") && !slevel.trim().equals("")) {

                        if (!stp1.trim().equals(Mdcode.trim())) {
                            String appd = getmailuser(Mdcode);
                            String app[] = appd.split("\\^");
                            String mailapp = app[0].trim();
                            //  mailapp += "@mitsubishielevator.co.th";
                            String mdname = app[1].trim();
                            i = i + 1;
                            apv += String.valueOf(i) + "|" + Mdcode + "|" + mdname + "|" + mailapp + "^";
                        }

                    }

                }

            }

            // GA confirm 
            String ga = getAPP("GAAPP");
            if (!lastapp.equals(ga)) {

                String reqd = getmailuser(ga);
                String req[] = reqd.split("\\^");
                String requestmail = req[0].trim();
                // requestmail += "@mitsubishielevator.co.th";
                String xname = req[1].trim();
                i = i + 1;
                apv += String.valueOf(i) + "|" + ga + "|" + xname + "|" + requestmail + "^";
            }

            return apv;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String cheeckuserprofile(String empcode) {

        try {

            String email = "";
            String empname = "";
            String positionname = "";
            String dept = "";
            String level = "";
            String jobclass = "";
            String bosscode = "";
            String engname = "";

            String sql = " select * from stxemphr where emp_code='" + empcode + "' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                email = rs.getString("email");
                empname = rs.getString("emp_name");
                positionname = rs.getString("position_name");
                dept = rs.getString("department");
                level = rs.getString("level");
                jobclass = rs.getString("job_class");
                bosscode = rs.getString("boss_code");
                engname = rs.getString("spouse_name");
            }

            if (email.trim().equals("") || email == null) {
                return "";   // เบิก advance
            }
            if (empname.trim().equals("") || empname == null) {
                return "";   // เบิก advance
            }
            if (positionname.trim().equals("") || positionname == null) {
                return "";   // เบิก advance
            }
            if (dept.trim().equals("") || dept == null) {
                return "";   // เบิก advance
            }
            if (level.trim().equals("") || level == null) {
                return "";   // เบิก advance
            }
            if (jobclass.trim().equals("") || jobclass == null) {
                return "";   // เบิก advance
            }
            if (bosscode.trim().equals("") || bosscode == null) {
                return "";   // เบิก advance
            }
            if (engname.trim().equals("") || engname == null) {
                return "";   // เบิก advance
            }

            Integer sco = 0;
            String sql2 = " select count(*) as sco from apvstep where emp_code='" + empcode + "' ";
            ResultSet rs2 = cConnect.getResultSet(sql2);

            while (rs2.next()) {
                sco = rs2.getInt("sco");
            }

            if (sco <= 0) {
                return "";
            }

            return "OK";

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public List getCounthotel(String str) {

        try {

            String sql = "SELECT COUNT(*) AS scount "
                    + " FROM stxhotel   ";
            if (!str.equals("")) {
                sql = sql + str;
            }

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("scount")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List gethotelmaster(String sortField, String sortDescAsc, String startRow, String limit, String str) {
        try {

            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

            String sql = "";

            if (startRow.equals("99999") || limit.equals("99999")) {
                sql = "SELECT *  "
                        + " from  stxhotel   ";
                sql = sql + str + " ORDER BY " + sortField + " " + sortDescAsc;
            } else {
                sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
                        + " * "
                        + " from stxhotel   ";
                sql = sql + str + " ORDER BY " + sortField + " " + sortDescAsc;
            }
//            sql = "SELECT   "
//                    + " from stytrave  "
//                    + " ORDER BY " + sortField + " " + sortDescAsc;  

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beanhotel bhotel = new beanhotel();
                bhotel.sethotelcode(rs.getString("hotel_code"));
                bhotel.sethcountry(rs.getString("country_code"));
                bhotel.setprovename(rs.getString("prov_name"));
                bhotel.setampname(rs.getString("amp_name"));
                bhotel.sethotelname(rs.getString("hotel_name"));
                bhotel.sethoteltel(rs.getString("hotel_tel"));
                bhotel.setpricehi(rs.getString("price_hi"));

                arrList.add(bhotel);
                bhotel = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //addhotel  
    public String addhotel(String country, String province, String amphor, String hotel, String addr, String tel, String fax, String plo, String phi, String zone) {

        try {

            String oldhotel_code = "";
            String Provid = "";
            String Hotelcode = "";

            String sql = " select skip 0 first 1  hotel_code from stxhotel  where country_code like'%" + country + "%' and prov_name like '%" + province + "%'  order by hotel_code desc";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                oldhotel_code = rs.getString("hotel_code");
            }

            if (oldhotel_code.trim().equals("") || oldhotel_code == null) {
                String sql2 = " select prov_id from stxprovd where country_code='" + country + "' and  prov_tname like'%" + province + "%' ";
                ResultSet rs2 = cConnect.getResultSet(sql2);

                while (rs2.next()) {
                    Provid = rs2.getString("prov_id");
                }

                Hotelcode = Provid.trim() + "100";

            } else {

                String xtmp1 = oldhotel_code.substring(0, 5);
                String xtmp2 = oldhotel_code.substring(5, 8);

                int itmp2 = Integer.parseInt(xtmp2) + 1;
                Hotelcode = xtmp1 + String.valueOf(itmp2);
            }

            // Hotelcode = "TEST";
//          String sqlc = "INSERT INTO stxhotel (country_code,hotel_code,prov_name,amp_name,hotel_name,hotel_addr,hotel_telmhotel_fax,price_low,price_hi,zone) "
//                    + "VALUES ('" + country + "','" + Hotelcode + "','" + province + "','"+ amphor +"','"+ hotel +"','"+ addr +"','"+ tel +"','"+ fax +"','" + plo + "','" + phi + "','" + zone + "')";
//          //cConnect.ExecuteQuery(sqlc); 
            try {

                clsConnect cls_connect = new clsConnect();
                Connection conn = cls_connect.getConnection();

                Statement st = conn.createStatement();

                String sqlc = "INSERT INTO stxhotel (country_code,hotel_code,prov_name,amp_name,hotel_name,hotel_addr,hotel_tel,hotel_fax,price_low,price_hi,zone) "
                        + "VALUES ('" + country + "','" + Hotelcode + "','" + province + "','" + amphor + "','" + hotel + "','" + addr + "','" + tel + "','" + fax + "','" + plo + "','" + phi + "','" + zone + "')";

                st.executeUpdate(sqlc);

                conn.close();

                return "OK";
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String getedithotel(String id) {

        String result = "";
        try {

            String sql = "SELECT * from stxhotel where hotel_code='" + id + "' ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {

                result += rs.getString("hotel_code") + "^";
                result += rs.getString("country_code") + "^";
                result += rs.getString("prov_name") + "^";
                result += rs.getString("amp_name") + "^";
                result += rs.getString("hotel_name") + "^";
                result += rs.getString("hotel_addr") + "^";
                result += rs.getString("hotel_tel") + "^";
                result += rs.getString("hotel_fax") + "^";
                result += rs.getString("price_low") + "^";
                result += rs.getString("price_hi") + "^";
                result += rs.getString("zone");
            }

            cConnect.Close();

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    //updatehotel
    public String updatehotel(String htcode, String country, String province, String amphor, String hotel, String addr, String tel, String fax, String plo, String phi, String zone) {

        try {

            try {

                clsConnect cls_connect = new clsConnect();
                Connection conn = cls_connect.getConnection();

                Statement st = conn.createStatement();

//            String sqlc = "INSERT INTO stxhotel (country_code,hotel_code,prov_name,amp_name,hotel_name,hotel_addr,hotel_tel,hotel_fax,price_low,price_hi,zone) "
//                  + "VALUES ('" + country + "','" + Hotelcode + "','" + province + "','"+ amphor +"','"+ hotel +"','"+ addr +"','"+ tel +"','"+ fax +"','" + plo + "','" + phi + "','" + zone + "')";
                String sqlc = " update stxhotel set "
                        + " amp_name='" + amphor + "',hotel_name='" + hotel + "',hotel_addr='" + addr + "',hotel_tel='" + tel + "',hotel_fax='" + fax + "',price_low='" + plo + "',price_hi='" + phi + "',zone='" + zone + "' "
                        + " where hotel_code='" + htcode + "' ";
                st.executeUpdate(sqlc);

                conn.close();

                return "OK";
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    //deletehotel
    public String deletehotel(String htcode) {

        try {

            try {

                clsConnect cls_connect = new clsConnect();
                Connection conn = cls_connect.getConnection();

                Statement st = conn.createStatement();

                String sqlc = " delete from stxhotel  "
                        + " where hotel_code='" + htcode + "' ";
                st.executeUpdate(sqlc);

                conn.close();

                return "OK";
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String getallowreport(String emp_code, String xfrom, String xto, String xcc, String proj, String prov) {

        String result = "";
        try {

            String swhere = "";

            if (!emp_code.equals("")) {
                swhere = " where  emp_code='" + emp_code + "' ";
            }

            if (!xfrom.equals("") && !xto.equals("")) {

                if (!swhere.equals("")) {
                    swhere += " and ";
                } else {
                    swhere += " where ";
                }
                swhere += "  ( line_date between '" + xfrom + "' and '" + xto + "' ) ";

            }

            if (!xcc.equals("")) {
                if (!swhere.equals("")) {
                    swhere += " and ";
                } else {
                    swhere += " where ";
                }
                swhere += "  cc_code='" + xcc + "' ";
            }

            if (!proj.equals("")) {
                if (!swhere.equals("")) {
                    swhere += " and ";
                } else {
                    swhere += " where ";
                }
                swhere += "  proj_code='" + proj + "' ";
            }

            if (!prov.equals("")) {
                if (!swhere.equals("")) {
                    swhere += " and ";
                } else {
                    swhere += " where ";
                }
                swhere += "  prov_name like '%" + prov + "%'";
            }

//            String sql = " select e.*, "
//                   + " ( select sum(allow1_amt)+sum(allow2_amt)+sum(meal_amt) as total from stytravd where doc_no= e.doc_no group by doc_no ) as tot "
//                   + " from stytrave e " + swhere
//                   + " order by de_date asc ";
            String sql = " select d.*,e.* from stytravd d "
                    + " inner join stytrave e on d.doc_no = e.doc_no and e.doc_status = 'APPROVE'  " + swhere
                    + "  order by e.doc_no,line_date asc ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {

                result += rs.getString("doc_no") + "^";
                result += rs.getString("emp_code") + "^";
                result += rs.getString("emp_tname") + "^";
                result += rs.getString("cc_code") + "^";
                result += rs.getString("country_code") + "^";
                result += rs.getString("line_date") + "^";
                result += rs.getString("line_desc") + "^";
                result += rs.getString("allow1_amt") + "^";
                result += rs.getString("allow2_amt") + "^";
                result += rs.getString("meal_amt") + "^";
                result += rs.getString("exch_rate") + "^";

                int allow1 = 0;
                int allow2 = 0;
                float exrate = 1;
                float allow = 0;
                int imeal = 0;

                allow1 = rs.getInt("allow1_amt");
                allow2 = rs.getInt("allow2_amt");
                imeal = rs.getInt("meal_amt");
                exrate = rs.getFloat("exch_rate");

                allow = (allow1 + allow2 + imeal) * exrate;

                result += String.valueOf(allow) + "^";
                result += rs.getString("proj_code") + "^";
                result += rs.getString("prov_name") + "^";
                result += "|";
            }

            cConnect.Close();

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String getadvancereport(String emp_code, String xfrom, String xto, String xcc) {

        String result = "";
        try {

            String swhere = "";

            if (!emp_code.equals("")) {
                swhere = " where  emp_code='" + emp_code + "' ";
            }

            if (!xfrom.equals("") && !xto.equals("")) {

                if (!swhere.equals("")) {
                    swhere += " and ";
                } else {
                    swhere += " where ";
                }
                swhere += "  ( de_date between '" + xfrom + "' and '" + xto + "' ) ";

            }

            if (!xcc.equals("")) {
                if (!swhere.equals("")) {
                    swhere += " and ";
                } else {
                    swhere += " where ";
                }
                swhere += "  cc_code='" + xcc + "' ";
            }

//            String sql = " select e.*, "
//                   + " ( select sum(allow1_amt)+sum(allow2_amt)+sum(meal_amt) as total from stytravd where doc_no= e.doc_no group by doc_no ) as tot "
//                   + " from stytrave e " + swhere
//                   + " order by de_date asc ";
            String sql = " select * from stytrave  " + swhere
                    + "  and doc_status = 'APPROVE' "
                    + "  order by doc_no asc ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {

                String doc_no = rs.getString("doc_no");
                result += rs.getString("doc_no") + "^";
                result += rs.getString("emp_code") + "^";
                result += rs.getString("emp_tname") + "^";
                result += rs.getString("cc_code") + "^";
                result += rs.getString("country_code") + "^";
                result += rs.getString("de_date") + "^";
                result += rs.getString("re_date") + "^";
                int advance = 0;
                float exrate = 1;
                String advanceall = "";
                float advancetot = 0;
                String remark = "";

                advanceall = rs.getString("advance_allow");

                exrate = rs.getFloat("exch_rate"); // สกุลเงินที่ใช้

                if (advanceall == null) {
                    // หาจำนวนเบี้ยเลี้ยงที่ได้
                    String sqlto = "select sum(allow1_amt)+sum(allow2_amt)+sum(meal_amt) as total from stytravd where doc_no='" + doc_no + "' group by doc_no";
                    float totallow = 0;
                    ResultSet rs2 = cConnect.getResultSet(sqlto);
                    while (rs2.next()) {
                        totallow = Float.parseFloat(rs2.getString("total"));
                    }

                    float xtotal = 0;
                    xtotal = totallow * exrate;  // จำนวนเงินเบี้ยเลี้ยงที่ได้
                    advancetot = Float.parseFloat(rs.getString("total_amt")) - xtotal;
                    remark = "ไม่รวมเบี้ยเลี้ยง";

                } else if (advanceall.equals("Y")) {

                    advancetot = Float.parseFloat(rs.getString("total_amt"));
                    remark = "รวมเบี้ยเลี้ยง";
                } else {
                    // หาจำนวนเบี้ยเลี้ยงที่ได้
                    String sqlto = "select sum(allow1_amt)+sum(allow2_amt)+sum(meal_amt) as total from stytravd where doc_no='" + doc_no + "' group by doc_no";
                    float totallow = 0;
                    ResultSet rs2 = cConnect.getResultSet(sqlto);
                    while (rs2.next()) {
                        totallow = Float.parseFloat(rs2.getString("total"));
                    }

                    float xtotal = 0;
                    xtotal = totallow * exrate;  // จำนวนเงินเบี้ยเลี้ยงที่ได้
                    advancetot = Float.parseFloat(rs.getString("total_amt")) - xtotal;
                    remark = "ไม่รวมเบี้ยเลี้ยง";
                }

                result += String.valueOf(advancetot) + "^";
                result += remark;
                result += "|";

            }

            cConnect.Close();

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String checkallowancehour(String sstart, String send, String sstarttime, String sendtime, String totday, String sco) {

        try {

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String result = "";

            String sSartdate = sstart + " " + sstarttime;
            String sEnddate = send + " " + sendtime;

            int totdays = 0;
            int difhr = 0;

            totdays = Integer.parseInt(totday);

            if (totdays > 1) {

                String endday = sstart + " 23:59";  //นับถึงเที่ยงคืน
                Date d1 = null;
                Date d2 = null;
                d1 = format.parse(sSartdate);
                d2 = format.parse(endday);

                long diff = d2.getTime() - d1.getTime();
                double diffInHours = diff / ((double) 1000 * 60 * 60);

                difhr = (int) diffInHours;
                difhr = difhr + 1;

            } else {

                //เดินทางวันเดียว
                Date d1 = null;
                Date d2 = null;
                d1 = format.parse(sSartdate);
                d2 = format.parse(sEnddate);

                long diff = d2.getTime() - d1.getTime();
                double diffInHours = diff / ((double) 1000 * 60 * 60);

                difhr = (int) diffInHours;

            }

            String sql = "";
            if (sco.equals("TH")) {
                sql = " select * from stxinfor where src_type='ALLOWHR' and src_key='TH' ";
            } else {
                sql = " select * from stxinfor where src_type='ALLOWHR' and src_key='OTHER' ";
            }

            int allowhr = 0;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                allowhr = rs.getInt("src_num");
            }

            if (difhr >= allowhr) {
                result = "Y";
            } else {
                result = "N"; //ได้ครึ่งวัน
            }

            cConnect.Close();

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String checkallowancehourback(String sstart, String send, String sstarttime, String sendtime, String totday, String sco) {

        try {

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String result = "";

            String sSartdate = sstart + " " + sstarttime;
            String sEnddate = send + " " + sendtime;

            int totdays = 0;
            int difhr = 0;

            String endday = send + " 00:00";  //นับจากเที่ยงคืน
            Date d1 = null;
            Date d2 = null;
            d1 = format.parse(endday);
            d2 = format.parse(sEnddate);

            long diff = d2.getTime() - d1.getTime();
            double diffInHours = diff / ((double) 1000 * 60 * 60);

            difhr = (int) diffInHours;
            difhr = difhr;

            String sql = "";
            if (sco.equals("TH")) {
                sql = " select * from stxinfor where src_type='ALLOWHR' and src_key='TH' ";
            } else {
                sql = " select * from stxinfor where src_type='ALLOWHR' and src_key='OTHER' ";
            }

            int allowhr = 0;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                allowhr = rs.getInt("src_num");
            }

            if (difhr >= allowhr) {
                result = "Y";
            } else {
                result = "N"; //ได้ครึ่งวัน
            }

            cConnect.Close();

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String checklastoil() {

        String result = "";
        try {
            Calendar originalDate = Calendar.getInstance();

            Calendar previousMonthDay = (Calendar) originalDate.clone();
            previousMonthDay.add(Calendar.MONTH, -1);

            DateFormat dfyy = new SimpleDateFormat("15/MM/yyyy");
            String mmnow = dfyy.format(previousMonthDay.getTime());

            String sql = "SELECT * from stxoilrate where oildate='" + mmnow + "' ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {

                result += rs.getString("oiltype") + "^";
                result += rs.getString("oilrate") + "|";

            }

            cConnect.Close();

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String insertoiltodb(String data) {
        try {

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();
            conn.setAutoCommit(false);

            Calendar originalDate = Calendar.getInstance();

            Calendar previousMonthDay = (Calendar) originalDate.clone();
            previousMonthDay.add(Calendar.MONTH, -1);

            DateFormat dfyy = new SimpleDateFormat("15/MM/yyyy");
            String mmnow = dfyy.format(previousMonthDay.getTime());

            try {

                Statement st = conn.createStatement();

                String arr[] = data.split("\\|");

                for (Integer i_row = 0; i_row < arr.length; i_row++) {

                    String acell[] = arr[i_row].split("\\^");

                    String slast = acell[0].trim();   //2015-05-30T05:00:00+07:00

                    String dlast[] = slast.split("T");

                    String newdarr[] = dlast[0].split("\\-");

                    String lastdate = newdarr[2] + "/" + newdarr[1] + "/" + newdarr[0];

                    String stype = acell[1].trim();
                    String srate = acell[2].trim();

                    String sqlc = " insert into  stxoilrate (oiltype,oilrate,lastchange,oildate) values  ";
                    sqlc += "('" + stype.trim() + "','" + srate.trim() + "','" + lastdate.trim() + "','" + mmnow + "')";
                    st.executeUpdate(sqlc);
                }

                conn.commit();

                conn.close();
                return "OK";

            } catch (Exception ex) {
                ex.printStackTrace();
                conn.rollback();
                return "";
            }

        } catch (Exception ex) {
            return "";
        }
    }

    public String updateadvancestatus(String docno, String remark, String scode, String stype) {
        try {

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();
            conn.setAutoCommit(false);

            try {

                Statement st = conn.createStatement();

                if (stype.equals("Y")) {

                    String sqlc = " update  advance_log set app_status='" + stype + "',remark='" + remark.trim() + "',app_date=current,update_by='" + scode + "' ";
                    sqlc += " where doc_no='" + docno.trim() + "' ";
                    st.executeUpdate(sqlc);
                    // sendmailadvance(docno,scode,remark.trim(),"Y");
                } else if (stype.equals("C")) {

                    String sqlc = " update  advance_log set app_status='" + stype + "',remark='" + remark.trim() + "',app_date=current,update_by='" + scode + "' ";
                    sqlc += " where doc_no='" + docno.trim() + "' ";
                    st.executeUpdate(sqlc);
                    // sendmailadvance(docno,scode,remark.trim(),"C");
                } else {
                    String sqlc = " update  advance_log set app_status='" + stype + "',receive_date=current,receive_by='" + scode + "' ";
                    sqlc += " where doc_no='" + docno.trim() + "' ";
                    st.executeUpdate(sqlc);
                }

                conn.commit();

                conn.close();

                if (stype.equals("Y")) {
                    sendmailadvance(docno, scode, remark.trim(), "Y");
                } else if (stype.equals("C")) {
                    sendmailadvance(docno, scode, remark.trim(), "C");
                }

                return "OK";

            } catch (Exception ex) {
                ex.printStackTrace();
                conn.rollback();
                return "";
            }

        } catch (Exception ex) {
            return "";
        }
    }

    public String sendmailadvance(String docno, String scode, String sremark, String stype) {

        // String CC = getAPP("AIRUSR"); 
        String requestby = "";
        String Createby = "";

        try {
            String sql = "SELECT * from stytrave where doc_no='" + docno + "' ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {

                requestby = rs.getString("emp_code");
                Createby += rs.getString("approve1_by");

            }
            cConnect.Close();
        } catch (Exception ex) {
            return "";
        }

        String tmpdata = "";
        String mailto = "";
        String mailcc = "";
        String mailfrom = "";
        String xtmp = "";
        tmpdata = getmailuser(Createby);
        String req[] = tmpdata.split("\\^");
        mailto = req[0].trim();
        mailto += "@mitsubishielevator.co.th";

        if (!requestby.trim().equals(Createby.trim())) {

            tmpdata = getmailuser(requestby);
            String req2[] = tmpdata.split("\\^");
            mailcc = req2[0].trim();
            xtmp = req2[0].trim();
            mailcc += "@mitsubishielevator.co.th";

        }

        tmpdata = getmailuser(scode);  // confirm by
        String req3[] = tmpdata.split("\\^");
        mailfrom = req3[0].trim();
        mailfrom += "@mitsubishielevator.co.th";

        String SMTP_HOST_NAME = "mail.mitsubishielevator.co.th"; //ขึ้นอยู่กับผู้ให้บริการ
        String SMTP_PORT = "25";//ขึ้นอยู่กับผู้ให้บริการ
        String FROM = mailfrom;
        String TO = mailto;
        String CC = "";
        if (!xtmp.trim().equals("")) {
            CC = mailcc;
        }

        String SUBJECT = "";
        String MESSAGE = "";

        if (stype.equals("Y")) {
            SUBJECT = "Advance Travelling  Doc no: " + docno + "   Complete ";
            MESSAGE = "Advance Travelling  Doc no: " + docno + " &nbsp;&nbsp;&nbsp; >>&nbsp;&nbsp;  <b>COMPLETE </b> <br><br>";

        } else {
            SUBJECT = "Cancel Advance Travelling  Doc no: " + docno + " by Finance ";
            MESSAGE = "Advance Travelling  Doc no: " + docno + " &nbsp;&nbsp;&nbsp; >>&nbsp;&nbsp;  <b> CANCEL </b> <br><br>";

        }

        MESSAGE += "   <b> Remark: </b>  " + sremark;
        MESSAGE += "<br><br>================================================ ";
        MESSAGE += "<br>Please See detail >>> <a href='http://150.152.182.206:8088/travelling'> http://150.152.182.206:8088/travelling </a> ";

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);

        Session session = Session.getInstance(props, null);
        session.setDebug(false);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            InternetAddress[] to_address = InternetAddress.parse(TO);
            message.setRecipients(Message.RecipientType.TO, to_address);

            if (!CC.trim().equals("")) {

                InternetAddress[] cc_address = InternetAddress.parse(CC);
                message.setRecipients(Message.RecipientType.CC, cc_address);

            }

            message.setSubject(SUBJECT);
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setText(MESSAGE, "UTF-8");
            mbp.setHeader("X-Priority", "High");
            mbp.setHeader("Content-Type", "text/html; charset=\"utf-8\"");

            Multipart content = new MimeMultipart();
            content.addBodyPart(mbp);

            message.setContent(content);
            Transport.send(message);

            return "";

        } catch (MessagingException mex) {
            return "";

        } catch (Exception e) {
            return "";
        } finally {
            return "";
        }

    }

    public String checkdateline(String empid, String sdate, String trip) {
        String result = "";
        String sql = "";
        try {
            if (trip.trim().equals("")) {

                sql = " select d.doc_no as doc_no from stytravd d ";
                sql += " inner join stytrave e on e.doc_no = d.doc_no ";
                sql += "  where d.line_date = '" + sdate.trim() + "' and e.doc_status in ('APPROVE','NEW','CONFIRM') ";
                sql += "  and e.emp_code='" + empid.trim() + "'  ";
            } else {
                sql = " select d.doc_no as doc_no from stytravd d ";
                sql += " inner join stytrave e on e.doc_no = d.doc_no ";
                sql += "  where d.line_date = '" + sdate.trim() + "' and e.doc_status in ('APPROVE','NEW','CONFIRM') ";
                sql += "  and e.emp_code='" + empid.trim() + "' and d.doc_no not in ('" + trip.trim() + "')  ";
            }

            //String sql = "SELECT * from stxoilrate where oildate='"+ mmnow +"' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                result += rs.getString("doc_no");
            }
            cConnect.Close();

            if (result.equals("")) {
                return "OK";
            } else {
                return "";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String checkadvstatus(String sdoc) {
        String result = "";
        try {

            String sql = " select * from advance_log where doc_no='" + sdoc.trim() + "' and app_status in ('Y','P')  ";

            //String sql = "SELECT * from stxoilrate where oildate='"+ mmnow +"' ";
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                result += rs.getString("doc_no");
            }
            cConnect.Close();

            if (result.equals("")) {
                return "OK";
            } else {
                return "";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public List getproj(String Userid) {
        try {

            String sql = "";
            //  AND LAELST < 80
            sql = " SELECT trim(LAPROJ)  as projcode,trim(LATX40) as projname "
                    + " FROM BPROJS "
                    + " WHERE LAELNO = '1'  and LAPROJ like '%" + Userid + "%' "
                    + " Order By LAPROJ DESC ";

            clsConnectDB2 cConnect = new clsConnectDB2();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beanListItem itemList = new beanListItem();
                itemList.setText(rs.getString("projname"));
                itemList.setValue(rs.getString("projcode"));
                arrList.add(itemList);

            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getpermissionhotel(String empcode) {
        String result = "";
        try {

            String sql = " select * from stxmenua where menu_group='TRAVEL' and menu_id='TV002' and user_id='" + empcode + "'  ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                result += rs.getString("user_id");
            }
            cConnect.Close();

            if (result.trim().equals("")) {
                return "N";
            } else {
                return "Y";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "N";
        }
    }

    public List getCountallowlist(String str) {

        try {

            String sql = " select count(d.doc_no) AS scount  "
                    + "from stytrave e  "
                    + "inner join stytravd d on d.doc_no = e.doc_no  "
                    + "left outer join allowance_log l on l.doc_no = d.doc_no and l.line_no = d.line_no  "
                    + "where e.doc_status = 'APPROVE' and e.advance_allow='N' and ((d.allow1_amt + d.allow2_amt + d.meal_amt) > 0)  "
                    + "and l.status is null  ";
            if (!str.equals("")) {
                sql = sql + str;
            }

//            String sql = "SELECT COUNT(*) AS scount "
//                    + " FROM advance_log  E  ";
//            if (!str.equals("")){
//                   sql =  sql + str;
//            }
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("scount")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getallowlist(String sortField, String sortDescAsc, String startRow, String limit, String str) {
        try {
            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

            String sql = "";

//                sql = " select SKIP " + startRow + " FIRST " + limit + " d.doc_no,e.emp_code,e.emp_tname,e.cc_code,d.line_date,d.prov_name,d.proj_code,d.allow1_amt,d.allow2_amt,d.meal_amt "
//                + " , (d.allow1_amt + d.allow2_amt + d.meal_amt) as total, l.status "
//                + " from stytrave e "
//                + " inner join stytravd d on d.doc_no = e.doc_no "
//                + " left outer join allowance_log l on l.doc_no = d.doc_no and l.line_no = d.line_no "
//                + " where e.doc_status = 'APPROVE' and e.advance_allow='N' and ((d.allow1_amt + d.allow2_amt + d.meal_amt) > 0) "
//                + " and l.status is null " + str;
//                sql = sql + str +  " ORDER BY " + sortField + " " + sortDescAsc;
            sql = " select  d.doc_no,e.emp_code,e.emp_tname,e.cc_code,d.line_no,d.line_date,d.prov_name,d.proj_code,d.allow1_amt,d.allow2_amt,d.meal_amt,e.exch_rate "
                    + " , ((d.allow1_amt*e.exch_rate) + (d.allow2_amt*e.exch_rate) + d.meal_amt) as total, l.status "
                    + " from stytrave e "
                    + " inner join stytravd d on d.doc_no = e.doc_no "
                    + " left outer join allowance_log l on l.doc_no = d.doc_no and l.line_no = d.line_no "
                    + " where e.doc_status = 'APPROVE' and e.advance_allow='N' and ((d.allow1_amt + d.allow2_amt + d.meal_amt) > 0) "
                    + " and l.status is null " + str;
            sql = sql + " ORDER BY " + sortField + " " + sortDescAsc;

//            if (startRow.equals("99999") || limit.equals("99999")) {
//               
//                
//                
//            } else {
//                
//               sql = " select SKIP " + startRow + " FIRST " + limit + " d.doc_no,e.emp_code,e.emp_tname,e.cc_code,d.line_no,d.line_date,d.prov_name,d.proj_code,d.allow1_amt,d.allow2_amt,d.meal_amt,e.exch_rate "
//                + " , ((d.allow1_amt*e.exch_rate) + (d.allow2_amt*e.exch_rate) + (d.meal_amt*e.exch_rate)) as total, l.status "
//                + " from stytrave e "
//                + " inner join stytravd d on d.doc_no = e.doc_no "
//                + " left outer join allowance_log l on l.doc_no = d.doc_no and l.line_no = d.line_no "
//                + " where e.doc_status = 'APPROVE' and e.advance_allow='N' and ((d.allow1_amt + d.allow2_amt + d.meal_amt) > 0) "
//                + " and l.status is null " + str;
//                sql = sql + str +  " ORDER BY " + sortField + " " + sortDescAsc;
//            }
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beantravd btrav = new beantravd();
                btrav.setdocNo(rs.getString("doc_no"));
                btrav.setempcode(rs.getString("emp_code"));
                btrav.setemptname(rs.getString("emp_tname"));
                btrav.setcccode(rs.getString("cc_code"));
                btrav.setlineno(rs.getString("line_no"));

                Date line_date = rs.getDate("line_date");
                if (line_date != null) {
                    btrav.setlinedate(fmt.format(line_date));
                } else {
                    btrav.setlinedate("");
                }
                btrav.setprovename(rs.getString("prov_name"));
                btrav.setprojcode(rs.getString("proj_code"));
                btrav.setall1(rs.getString("allow1_amt"));
                btrav.setall2(rs.getString("allow2_amt"));
                btrav.setmeal(rs.getString("meal_amt"));
                btrav.setexch(rs.getString("exch_rate"));
                btrav.settotal(rs.getString("total"));

                arrList.add(btrav);
                btrav = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {

            ex.printStackTrace();
            return null;

        }
    }

    public String insertpendingallow(String data, String userid) {
        try {

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();
            conn.setAutoCommit(false);

            try {

                Statement st = conn.createStatement();

                String arr[] = data.split("\\|");

                for (Integer i_row = 0; i_row < arr.length; i_row++) {

                    String acell[] = arr[i_row].split("\\^");

                    String sdocno = acell[0].trim();
                    String lineno = acell[1].trim();
                    String linedate = acell[2].trim();
                    String empcode = acell[3].trim();

                    String sqlc = " insert into allowance_log (doc_no,line_no,line_date,emp_code,status,confirm_date,confirm_by) values  ";
                    sqlc += "('" + sdocno.trim() + "','" + lineno.trim() + "','" + linedate.trim() + "','" + empcode.trim() + "','N',CURRENT,'" + userid.trim() + "')";
                    st.executeUpdate(sqlc);
                }

                conn.commit();

                conn.close();
                return "OK";

            } catch (Exception ex) {
                ex.printStackTrace();
                conn.rollback();
                return "";
            }

        } catch (Exception ex) {
            return "";
        }
    }

    public List getCountallowexp(String str) {

        try {

            String sql = " select count(c.doc_no) AS scount  "
                    + " from allowance_log c  "
                    + " inner join stytrave e on e.doc_no = c.doc_no  "
                    + " inner join stytravd d on d.doc_no = c.doc_no and d.line_no = c.line_no  "
                    + " where c.status = 'N'  ";
            if (!str.equals("")) {
                sql = sql + str;
            }

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("scount")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getallowexportadm(String sortField, String sortDescAsc, String startRow, String limit, String str) {
        try {
            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat fmt2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            String sql = "";

            sql = " select d.doc_no,e.emp_code,e.emp_tname,e.cc_code,d.line_no,d.line_date,d.prov_name,d.proj_code,d.allow1_amt,d.allow2_amt,d.meal_amt,e.exch_rate "
                    + " , ((d.allow1_amt*e.exch_rate) + (d.allow2_amt*e.exch_rate) + d.meal_amt) as total "
                    + " ,c.confirm_date,c.confirm_by,c.admexp_date,c.admexp_by "
                    + " from allowance_log c "
                    + " inner join stytrave e on e.doc_no = c.doc_no "
                    + " inner join stytravd d on d.doc_no = c.doc_no and d.line_no = c.line_no "
                    + " where c.status = 'N' " + str;
            sql = sql + str + " ORDER BY " + sortField + " " + sortDescAsc;

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beantravd btrav = new beantravd();
                btrav.setdocNo(rs.getString("doc_no"));
                btrav.setempcode(rs.getString("emp_code"));
                btrav.setemptname(rs.getString("emp_tname"));
                btrav.setcccode(rs.getString("cc_code"));
                btrav.setlineno(rs.getString("line_no"));

                Date line_date = rs.getDate("line_date");
                if (line_date != null) {
                    btrav.setlinedate(fmt.format(line_date));
                } else {
                    btrav.setlinedate("");
                }
                btrav.setprovename(rs.getString("prov_name"));
                btrav.setprojcode(rs.getString("proj_code"));
                btrav.setall1(rs.getString("allow1_amt"));
                btrav.setall2(rs.getString("allow2_amt"));
                btrav.setmeal(rs.getString("meal_amt"));
                btrav.setexch(rs.getString("exch_rate"));
                btrav.settotal(rs.getString("total"));

                Date confdate = rs.getDate("confirm_date");
                if (confdate != null) {
                    btrav.setconfdate(fmt2.format(confdate));
                } else {
                    btrav.setconfdate("");
                }
                btrav.setconfby(rs.getString("confirm_by"));
                Date expdate = rs.getDate("admexp_date");
                if (expdate != null) {
                    btrav.setadmxepdate(fmt2.format(expdate));
                } else {
                    btrav.setadmxepdate("");
                }
                btrav.setadmexpby(rs.getString("admexp_by"));
                arrList.add(btrav);
                btrav = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {

            ex.printStackTrace();
            return null;

        }
    }

    public String delallowancelog(String docno, String line, String empid) {

        try {

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();

            Statement st = conn.createStatement();

            if (!docno.equals("")) {

                String sqld = " delete from allowance_log  where doc_no='" + docno + "' and line_no='" + line + "' and confirm_by='" + empid + "' and status='N' ";
                st.executeUpdate(sqld);

                conn.close();

                return "OK";

            } else {

                return "";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }//

    public String updateallowlog(String semp, String sdocno, String sline, String sstatus, String sremark, String user) {

        try {

            int result = -1;

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();

            Statement st = conn.createStatement();

            String xsql = " update allowance_log set status='" + sstatus.trim() + "',approve_date=CURRENT,approve_by='" + user + "', remark='" + sremark.trim() + "' where doc_no='" + sdocno.trim() + "' and line_no=" + sline.trim() + " and emp_code='" + semp.trim() + "' and status = 'N'  ";

            result = st.executeUpdate(xsql);

            conn.close();

            if (result > 0) {
                return "Success";
            } else {
                return "Error";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error";
        }
    }

    public List getCountallowexppayroll(String str) {

        try {

            String sql = " select count(c.doc_no) AS scount  "
                    + " from allowance_log c  "
                    + " inner join stytrave e on e.doc_no = c.doc_no  "
                    + " inner join stytravd d on d.doc_no = c.doc_no and d.line_no = c.line_no  "
                    + " where c.status = 'P'  ";
            if (!str.equals("")) {
                sql = sql + str;
            }

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("scount")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getallowexportpayroll(String sortField, String sortDescAsc, String startRow, String limit, String str) {
        try {
            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat fmt2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            String sql = "";

            sql = " select d.doc_no,e.emp_code,e.emp_tname,e.cc_code,d.line_no,d.line_date,d.prov_name,d.proj_code,d.allow1_amt,d.allow2_amt,d.meal_amt,e.exch_rate "
                    + " , ((d.allow1_amt*e.exch_rate) + (d.allow2_amt*e.exch_rate) + d.meal_amt) as total "
                    + " ,c.confirm_date,c.confirm_by,c.approve_date,c.approve_by "
                    + " from allowance_log c "
                    + " inner join stytrave e on e.doc_no = c.doc_no "
                    + " inner join stytravd d on d.doc_no = c.doc_no and d.line_no = c.line_no "
                    + " where c.status = 'P' " + str;
            sql = sql + str + " ORDER BY " + sortField + " " + sortDescAsc;

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beantravd btrav = new beantravd();
                btrav.setdocNo(rs.getString("doc_no"));
                btrav.setempcode(rs.getString("emp_code"));
                btrav.setemptname(rs.getString("emp_tname"));
                btrav.setcccode(rs.getString("cc_code"));
                btrav.setlineno(rs.getString("line_no"));

                Date line_date = rs.getDate("line_date");
                if (line_date != null) {
                    btrav.setlinedate(fmt.format(line_date));
                } else {
                    btrav.setlinedate("");
                }
                btrav.setprovename(rs.getString("prov_name"));
                btrav.setprojcode(rs.getString("proj_code"));
                btrav.setall1(rs.getString("allow1_amt"));
                btrav.setall2(rs.getString("allow2_amt"));
                btrav.setmeal(rs.getString("meal_amt"));
                btrav.setexch(rs.getString("exch_rate"));
                btrav.settotal(rs.getString("total"));

                Date confdate = rs.getDate("confirm_date");
                if (confdate != null) {
                    btrav.setconfdate(fmt2.format(confdate));
                } else {
                    btrav.setconfdate("");
                }
                btrav.setconfby(rs.getString("confirm_by"));
                Date expdate = rs.getDate("approve_date");
                if (expdate != null) {
                    btrav.setadmxepdate(fmt2.format(expdate));
                } else {
                    btrav.setadmxepdate("");
                }
                btrav.setadmexpby(rs.getString("approve_by"));
                arrList.add(btrav);
                btrav = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {

            ex.printStackTrace();
            return null;

        }
    }

    public List getCountallowlog(String str) {

        try {

            String sql = " select count(c.doc_no) AS scount  "
                    + " from allowance_log c  "
                    + " inner join stytrave e on e.doc_no = c.doc_no  "
                    + " inner join stytravd d on d.doc_no = c.doc_no and d.line_no = c.line_no  ";

            if (!str.equals("")) {
                sql = sql + str;
            }

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("scount")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getallowexportlog(String sortField, String sortDescAsc, String startRow, String limit, String str) {
        try {
            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat fmt2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            String sql = "";

            sql = " select d.doc_no,e.emp_code,e.emp_tname,e.cc_code,d.line_no,d.line_date,d.prov_name,d.proj_code,d.allow1_amt,d.allow2_amt,d.meal_amt,e.exch_rate "
                    + " , ((d.allow1_amt*e.exch_rate) + (d.allow2_amt*e.exch_rate) + d.meal_amt) as total "
                    + " ,c.confirm_date,c.confirm_by,c.admexp_date,c.admexp_by, c.status "
                    + " from allowance_log c "
                    + " inner join stytrave e on e.doc_no = c.doc_no "
                    + " inner join stytravd d on d.doc_no = c.doc_no and d.line_no = c.line_no ";
            sql = sql + str + " ORDER BY " + sortField + " " + sortDescAsc;

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beantravd btrav = new beantravd();
                btrav.setdocNo(rs.getString("doc_no"));
                btrav.setempcode(rs.getString("emp_code"));
                btrav.setemptname(rs.getString("emp_tname"));
                btrav.setcccode(rs.getString("cc_code"));
                btrav.setlineno(rs.getString("line_no"));

                Date line_date = rs.getDate("line_date");
                if (line_date != null) {
                    btrav.setlinedate(fmt.format(line_date));
                } else {
                    btrav.setlinedate("");
                }
                btrav.setprovename(rs.getString("prov_name"));
                btrav.setprojcode(rs.getString("proj_code"));
                btrav.setall1(rs.getString("allow1_amt"));
                btrav.setall2(rs.getString("allow2_amt"));
                btrav.setmeal(rs.getString("meal_amt"));
                btrav.setexch(rs.getString("exch_rate"));
                btrav.settotal(rs.getString("total"));

                Date confdate = rs.getDate("confirm_date");
                if (confdate != null) {
                    btrav.setconfdate(fmt2.format(confdate));
                } else {
                    btrav.setconfdate("");
                }
                btrav.setconfby(rs.getString("confirm_by"));
                Date expdate = rs.getDate("admexp_date");
                if (expdate != null) {
                    btrav.setadmxepdate(fmt2.format(expdate));
                } else {
                    btrav.setadmxepdate("");
                }
                btrav.setadmexpby(rs.getString("admexp_by"));
                btrav.setstatus(rs.getString("status"));
                arrList.add(btrav);
                btrav = null; //clear dat
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {

            ex.printStackTrace();
            return null;

        }
    }

    public String getapprovelog(String app_Code, String emp_code, String xfrom, String xto) {

        String result = "";
        try {

            String swhere = "";

            swhere = " where   l.app_code='" + app_Code + "' and l.app_status is not null ";

            if (!emp_code.equals("")) {
                swhere += "  and    e.emp_code='" + emp_code + "'   ";
            }

            if (!xfrom.equals("") && !xto.equals("")) {

                if (!swhere.equals("")) {
                    swhere += " and ";
                } else {
                    swhere += " where ";
                }
                swhere += "  ( l.app_date >='" + xfrom + "' and  l.app_date <= '" + xto + "'  ) ";

            }

            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

            String sql = "  select l.doc_no,l.app_date,l.app_status,l.remark,e.emp_code,e.emp_tname,e.cc_code,e.country_code,e.de_date "
                    + " ,e.de_provname,e.de_by,e.re_date,e.re_provname,e.re_by,e.total_amt "
                    + " from apvlog l "
                    + " inner join stytrave e on e.doc_no = l.doc_no  " + swhere;
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {

                result += rs.getString("doc_no") + "^";

                Date app_date = rs.getDate("app_date");
                if (app_date != null) {
                    result += fmt.format(app_date) + "^";
                } else {
                    result += "^";
                }
                result += rs.getString("app_status") + "^";
                result += rs.getString("remark") + "^";
                result += rs.getString("emp_code") + "^";
                result += rs.getString("emp_tname") + "^";
                result += rs.getString("cc_code") + "^";
                result += rs.getString("country_code") + "^";

                Date de_date = rs.getDate("de_date");
                if (de_date != null) {
                    result += fmt.format(de_date) + "^";
                } else {
                    result += "^";
                }

                result += rs.getString("de_provname") + "^";
                result += rs.getString("de_by") + "^";

                Date re_date = rs.getDate("re_date");
                if (re_date != null) {
                    result += fmt.format(re_date) + "^";
                } else {
                    result += "^";
                }

                result += rs.getString("re_provname") + "^";
                result += rs.getString("re_by") + "^";
                result += rs.getString("total_amt") + "^";
                result += "|";
            }

            cConnect.Close();

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public String getcountrycount(String country, String xfrom, String xto) {

        String result = "";
        try {

            String sql = "    select distinct(d.prov_code) as prov_code ,d.prov_name, "
                    + " ( "
                    + " select  count(distinct x.doc_no)  from stytravd x "
                    + " inner join stytrave r on r.doc_no = x.doc_no and  r.country_code='" + country + "' "
                    + " where x.line_date >='" + xfrom + "' and x.line_date <='" + xto + "' and r.doc_status='APPROVE' "
                    + " and x.prov_code = d.prov_code "
                    + " group by x.prov_code ,x.prov_name "
                    + " ) as scount "
                    + " from stytravd d "
                    + " inner join stytrave e on e.doc_no = d.doc_no and  e.country_code='" + country + "' "
                    + " where d.line_date >='" + xfrom + "' and d.line_date <='" + xto + "' and e.doc_status='APPROVE' "
                    + " and d.prov_code <> '' and d.prov_name <> '' "
                    + " order by scount desc ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {

                result += rs.getString("prov_code") + "^";
                result += rs.getString("prov_name") + "^";
                result += rs.getString("scount");
                result += "|";
            }

            cConnect.Close();

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public List getCountclearlist(String str) {

        try {

//            String sql = "SELECT COUNT(*) AS scount "
//                    + " FROM stytrave  E  ";
//            if (!str.equals("")) {
//                sql = sql + str;
//            }
            String sql = " SELECT COUNT(e.doc_no) AS scount "
                    + " FROM stytrave e "
                    + " left outer join clearadv_log c on c.doc_no = e.doc_no "
                    + " where   e.doc_status='APPROVE' ";
            if (!str.equals("")) {
                sql = sql + str;
            }

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("scount")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List getclearlist(String sortField, String sortDescAsc, String startRow, String limit, String str) {
        try {
            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

            String sql = "";

//            if (startRow.equals("99999") || limit.equals("99999")) {
//                sql = "SELECT E.*,T.emp_tname,T.doc_date  "
//                        + " from advance_log E   "
//                        + " left outer join stytrave T on E.doc_no = T.doc_no and T.doc_status='APPROVE' ";
//                sql = sql + str + " ORDER BY " + sortField + " " + sortDescAsc;
//            } else {
//                sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
//                        + " E.*,T.emp_tname,T.doc_date "
//                        + " from advance_log E   "
//                        + " inner join stytrave T on E.doc_no = T.doc_no and T.doc_status='APPROVE' ";
//                sql = sql + str + " ORDER BY " + sortField + " " + sortDescAsc;
//            }
            if (startRow.equals("99999") || limit.equals("99999")) {
                sql = " select e.*,nvl(c.app_status,'') as app_status,c.remark,c.app_date from stytrave e "
                        + " left outer join clearadv_log c on c.doc_no = e.doc_no "
                        + " where  e.doc_status='APPROVE' ";
                sql = sql + str + " ORDER BY " + sortField + " " + sortDescAsc;
            } else {
                sql = "SELECT SKIP " + startRow + " FIRST " + limit + " "
                        + "  e.*,nvl(c.app_status,'') as app_status,c.remark,c.app_date from stytrave e"
                        + " left outer join clearadv_log c on c.doc_no = e.doc_no "
                        + " where  e.doc_status='APPROVE' ";
                sql = sql + str + " ORDER BY " + sortField + " " + sortDescAsc;
            }

//            sql = "SELECT   "
//                    + " from stytrave  "
//                    + " ORDER BY " + sortField + " " + sortDescAsc;  
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            while (rs.next()) {

                beantravel btrav = new beantravel();
                btrav.setdocNo(rs.getString("doc_no"));
                Date doc_date = rs.getDate("doc_date");
                if (doc_date != null) {
                    btrav.setdocdate(fmt.format(doc_date));
                } else {
                    btrav.setdocdate("");
                }
                btrav.setempcode(rs.getString("emp_code"));
                btrav.setempename(rs.getString("emp_tname"));

                btrav.setadvance(rs.getString("app_status"));

                btrav.setrermk(rs.getString("remark"));

                Date app_date = rs.getDate("app_date");
                if (app_date != null) {
                    btrav.setappdate(fmt.format(app_date));
                } else {
                    btrav.setappdate("");
                }

                //  check if MANAGER AIR  REPORT  Acknowledge
                String statusack = getacknowstatus(rs.getString("doc_no"));
                if (statusack.equals("ERROR")) {
                    btrav.setdoctype("");
                } else {
                    btrav.setdoctype(statusack);
                }

                arrList.add(btrav);
                btrav = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {

            ex.printStackTrace();
            return null;

        }
    }

    public String updateclearstatus(String docno, String remark, String scode, String stype) {
        try {

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();
            conn.setAutoCommit(false);

            try {

                Statement st = conn.createStatement();

                String sqlc = " insert  into clearadv_log (doc_no,create_date,create_by,emp_code,app_status,remark,app_date,update_by) "
                        + " values ('" + docno.trim() + "',current,'" + scode + "','" + scode + "','Y','" + remark.trim() + "',current,'" + scode + "' )";
                st.executeUpdate(sqlc);

                conn.commit();

                conn.close();

//                if (stype.equals("Y")) {
//                    sendmailadvance(docno, scode, remark.trim(), "Y");
//                } else if (stype.equals("C")) {
//                    sendmailadvance(docno, scode, remark.trim(), "C");
//                }
                return "OK";

            } catch (Exception ex) {
                ex.printStackTrace();
                conn.rollback();
                return "";
            }

        } catch (Exception ex) {
            return "";
        }
    }

    public String cancelclearstatus(String docno, String remark, String scode, String stype) {
        try {

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();
            conn.setAutoCommit(false);

            try {

                Statement st = conn.createStatement();

                String sqlc = " delete from  clearadv_log ";
                sqlc += " where doc_no='" + docno.trim() + "' ";
                st.executeUpdate(sqlc);

                conn.commit();

                conn.close();

//                if (stype.equals("Y")) {
//                    sendmailadvance(docno, scode, remark.trim(), "Y");
//                } else if (stype.equals("C")) {
//                    sendmailadvance(docno, scode, remark.trim(), "C");
//                }
                return "OK";

            } catch (Exception ex) {
                ex.printStackTrace();
                conn.rollback();
                return "";
            }

        } catch (Exception ex) {
            return "";
        }
    }

    public String getclearreport(String emp_code, String xfrom, String xto, String xcc) {

        String result = "";
        try {

            String swhere = "";

            if (!emp_code.equals("")) {
                swhere = " where  e.emp_code='" + emp_code + "' ";
            }

            if (!xfrom.equals("") && !xto.equals("")) {

                if (!swhere.equals("")) {
                    swhere += " and ";
                } else {
                    swhere += " where ";
                }
                swhere += "  ( e.de_date between '" + xfrom + "' and '" + xto + "' ) ";

            }

            if (!xcc.equals("")) {
                if (!swhere.equals("")) {
                    swhere += " and ";
                } else {
                    swhere += " where ";
                }
                swhere += "  e.cc_code='" + xcc + "' ";
            }

//            String sql = " select e.*, "
//                   + " ( select sum(allow1_amt)+sum(allow2_amt)+sum(meal_amt) as total from stytravd where doc_no= e.doc_no group by doc_no ) as tot "
//                   + " from stytrave e " + swhere
//                   + " order by de_date asc ";
//            String sql = " select * from stytrave  " + swhere
//                    + "  and doc_status = 'APPROVE' "
//                    + "  order by doc_no asc ";
            String sql = " select d.app_status,d.remark,d.app_date,d.update_by,e.doc_no,e.emp_code ,e.emp_tname ,e.cc_code,e.country_code ,e.de_date ,e.re_date from clearadv_log d "
                    + "  inner join stytrave e on e.doc_no = d.doc_no " + swhere
                    + " order by e.doc_no asc ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {

                String doc_no = rs.getString("doc_no");
                result += rs.getString("doc_no") + "^";
                result += rs.getString("emp_code") + "^";
                result += rs.getString("emp_tname") + "^";
                result += rs.getString("cc_code") + "^";
                result += rs.getString("country_code") + "^";
                result += rs.getString("de_date") + "^";
                result += rs.getString("re_date") + "^";
                result += rs.getString("app_status") + "^";
                result += rs.getString("remark") + "^";
                result += rs.getString("app_date") + "^";
                result += rs.getString("update_by") + "^";

                result += "|";

            }

            cConnect.Close();

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public List getCountreplist(String str) {

        try {

            String sql = "SELECT COUNT(R.doc_no) AS scount "
                    + " FROM travelreport R  "
                    + " inner join stytrave E on E.doc_no = R.doc_no  ";
            if (!str.equals("")) {
                sql = sql + str;
            }

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            List arrList = new ArrayList();
            while (rs.next()) {
                beanCountRows bCountRows = new beanCountRows();
                bCountRows.setCountRows(Integer.parseInt(rs.getString("scount")));
                arrList.add(bCountRows);
                bCountRows = null; //clear data
            }
            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List gettravelreport(String sortField, String sortDescAsc, String startRow, String limit, String str) {
        try {

            DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

            String sql = "";

            if (startRow.equals("99999") || limit.equals("99999")) {

                sql = " select R.doc_no,E.emp_code,R.doc_status,to_char(R.from_date,'%d/%m/%Y') as  from_date,E.de_provname,to_char(R.to_date,'%d/%m/%Y') as  to_date,E.re_provname,R.subject,R.approve_by,to_char(R.approve_date,'%d/%m/%Y') as approve_date from travelreport R "
                        + " inner join stytrave E on E.doc_no = R.doc_no " + str
                        + " order by R.create_date desc ";

            } else {

                sql = "SELECT SKIP " + startRow + " FIRST " + limit + "  R.doc_no,E.emp_code,R.doc_status,to_char(R.from_date,'%d/%m/%Y') as  from_date,E.de_provname,to_char(R.to_date,'%d/%m/%Y') as  to_date,E.re_provname,R.subject,R.approve_by,to_char(R.approve_date,'%d/%m/%Y') as approve_date from travelreport R "
                        + " inner join stytrave E on E.doc_no = R.doc_no " + str
                        + " order by R.create_date desc ";

            }
            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);
            List arrList = new ArrayList();

            clsManage cMn = new clsManage();

            while (rs.next()) {

                beantravel btrav = new beantravel();
                btrav.setdocNo(rs.getString("doc_no"));
                btrav.setempcode(rs.getString("emp_code"));
                btrav.setdocstaus(rs.getString("doc_status"));
                btrav.setdedate(rs.getString("from_date"));
                btrav.setdeprovince(rs.getString("de_provname"));
                btrav.setredate(rs.getString("to_date"));
                btrav.setreprovince(rs.getString("re_provname"));
                btrav.settotamt(cMn.chkNull(rs.getString("subject")));
                btrav.setappby(cMn.chkNull(rs.getString("approve_by")));
                btrav.setappdate(cMn.chkNull(rs.getString("approve_date")));

                arrList.add(btrav);
                btrav = null; //clear data
            }

            cConnect.Close();

            return arrList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String sendresulttomd(String docno, String subject, String detail, String scode) {
        try {

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();
            conn.setAutoCommit(false);

            try {

                Statement st = conn.createStatement();

//                String sqlc = " update travelreport set doc_status = 'CONFIRM' ,subject = '" + subject + "' ,detail = '" + detail + "'  "
//                        + " ,update_date = current ,update_by = '" + scode + "' where doc_no = '" + docno + "' ";
                String sqlc = " update travelreport set doc_status = 'CONFIRM' ,subject = ? ,detail = ?  "
                        + " ,update_date = current ,update_by = ? where doc_no = ? ";

                PreparedStatement psupdall = conn.prepareStatement(sqlc);
                psupdall.setString(1, subject);
                psupdall.setString(2, detail);
                psupdall.setString(3, scode);
                psupdall.setString(4, docno);
                psupdall.executeUpdate();

                conn.commit();

                conn.close();

//                if (stype.equals("Y")) {
//                    sendmailadvance(docno, scode, remark.trim(), "Y");
//                } else if (stype.equals("C")) {
//                    sendmailadvance(docno, scode, remark.trim(), "C");
//                }
                return "OK";

            } catch (Exception ex) {
                ex.printStackTrace();
                conn.rollback();
                return "";
            }

        } catch (Exception ex) {
            return "";
        }
    }

    public String sendmailresponse(String empcode, String namefrom, String from, String to, String subject, String docno, String country) {
        String result = "";
        String tbr = "";
        tbr += " <table  style='background-color:lightsteelblue;color:#006699;' width='600px'> ";
        tbr += " <tr> ";
        tbr += "  <td colspan='2' style='background-color:white;' > <b> Traveling Online: " + docno + " </b></td>  ";
        tbr += "  </tr> ";
        tbr += "  <tr> ";
        tbr += " <td width='90px'> ";
        tbr += " <img  src='http://150.152.182.224/documentation/photo/" + empcode.trim() + ".jpg' width='90px' height='100px' /> ";
        tbr += " </td> ";
        tbr += " <td valign='top'> ";
        tbr += " <table width='100%' style='background-color:white;color:black;'> ";
        tbr += "              <tr> ";
        tbr += "                  <td width='100px' ><b>Empcode:</b> </td><td aligh='left' style=' font-size: 14px;color: red'><b>" + empcode + "</b></td> ";
        tbr += "              </tr> ";
        tbr += "              <tr> ";
        tbr += "                  <td width='100px' ><b>Name:</b> </td><td> " + namefrom + "</td> ";
        tbr += "              </tr> ";
        tbr += "              <tr> ";
        tbr += "                  <td width='100px' ><b>Country:</b> </td><td> " + country + "</td> ";
        tbr += "              </tr> ";
        tbr += "              <tr> ";
        tbr += "                  <td width='100px' ><b>Subject:</b> </td><td> " + subject + "</td> ";
        tbr += "              </tr> ";
        tbr += "              <tr> ";
        tbr += "                  <td colspan='2' align='center'> ";
        tbr += "                  <hr /> ";
        tbr += "                  Please Acknowledge  <a href='http://150.152.182.206:8088/travelling/acknowledge.jsp?id=" + docno + "' > Click Here </a>  ";
        tbr += "                   <hr /> ";
        tbr += "                  </td> ";
        tbr += "              </tr> ";
        tbr += " </table> ";
        tbr += " </td> ";
        tbr += " </tr> ";
        tbr += "  </table> ";

        String SMTP_HOST_NAME = "mail.mitsubishielevator.co.th"; //ขึ้นอยู่กับผู้ให้บริการ
        String SMTP_PORT = "25";//ขึ้นอยู่กับผู้ให้บริการ
        String FROM = from;
        String TO = to;

        String SUBJECT = "Please Acknowledge  Travelling  Report Doc no: " + docno + " (" + country + ") from:" + namefrom;
        String MESSAGE = "<br><br>K. " + namefrom + "  Report Travelling ";
        MESSAGE += "<br>================================================ <br>" + tbr;
        //  MESSAGE += "Please Approve url >>> <a href='http://150.152.182.216:8088/travelling/approve.jsp?id=" + docno+ "'> http://150.152.182.216:8088/travelling/approve.jsp?id=" + docno + " </a> ";

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);

        Session session = Session.getInstance(props, null);
        session.setDebug(false);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            InternetAddress[] to_address = InternetAddress.parse(TO);
            message.setRecipients(Message.RecipientType.TO, to_address);

            String xccmail = "traveling-system@mitsubishielevator.co.th";
            InternetAddress[] cc_address = InternetAddress.parse(xccmail);
            message.setRecipients(Message.RecipientType.CC, cc_address);

            message.setSubject(SUBJECT);
            message.addHeader("X-Priority", "1");
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setText(MESSAGE, "UTF-8");
            mbp.setHeader("Content-Type", "text/html; charset=\"utf-8\"");

            Multipart content = new MimeMultipart();
            content.addBodyPart(mbp);

            message.setContent(content);
            Transport.send(message);

            result = "OK";

        } catch (MessagingException mex) {
            result = "";

        } catch (Exception e) {
            result = "";
        } finally {
            return result;
        }

    }

    public String approveack(String docno, String appcode, String requestor) {
        try {

            clsConnect cls_connect = new clsConnect();
            Connection conn = cls_connect.getConnection();
            conn.setAutoCommit(false);

            try {

                Statement st = conn.createStatement();

                String sqlc = " update travelreport set doc_status = 'APPROVE'   "
                        + " ,approve_date = current ,approve_by = '" + appcode + "' where doc_no = '" + docno + "' ";
                st.executeUpdate(sqlc);

                conn.commit();

                conn.close();

//                if (stype.equals("Y")) {
//                    sendmailadvance(docno, scode, remark.trim(), "Y");
//                } else if (stype.equals("C")) {
//                    sendmailadvance(docno, scode, remark.trim(), "C");
//                }
                return "OK";

            } catch (Exception ex) {
                ex.printStackTrace();
                conn.rollback();
                return "";
            }

        } catch (Exception ex) {
            return "";
        }
    }

    public String sendmailapproveack(String empcode, String appname, String from, String to, String subject, String docno, String country, String CC, String reportname) {

        String SMTP_HOST_NAME = "mail.mitsubishielevator.co.th"; //ขึ้นอยู่กับผู้ให้บริการ
        String SMTP_PORT = "25";//ขึ้นอยู่กับผู้ให้บริการ
        String FROM = from;
        String TO = to;
        String result = "";

        String SUBJECT = "Acknowledge Travelling Report Doc no: " + docno + " By:" + appname;
        String MESSAGE = "<br><br>K. " + appname + "  Acknowledge  Travelling Report Doc no: " + docno;
        MESSAGE += "<br><br>Report From :" + reportname;
        MESSAGE += "<br><br>================================================ ";
        MESSAGE += "<br>Please See detail >>> <a href='http://150.152.182.206:8088/travelling/reportackview.jsp?id=" + docno + "'> View </a> ";
        //  MESSAGE += "<br>Please See detail >>> <a href='http://150.152.182.206:8088/travelling'> http://150.152.182.206:8088/travelling </a> ";

        //   http://localhost:8084/travellingonline/reportackview.jsp?id=T1605685
//
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);

        Session session = Session.getInstance(props, null);
        session.setDebug(false);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            InternetAddress[] to_address = InternetAddress.parse(TO);
            message.setRecipients(Message.RecipientType.TO, to_address);
//            String ccmail = getccmail();
//            String reqgan[] = ccmail.split("\\|");
//
//            if (reqgan.length > 0) {
//                String xccmail = "";
//                String xprx = "";
//                for (int i = 0; i < reqgan.length; i++) {
//
//                    String ccempcode = reqgan[i].trim();
//
//                    String suser = getmailuser(ccempcode);
//                    String arruser[] = suser.split("\\^");
//                    xccmail += xprx + arruser[0].trim();
//                    xccmail += "@mitsubishielevator.co.th";
//                    xprx = ",";
//
//                }
            InternetAddress[] cc_address = InternetAddress.parse(CC);
            message.setRecipients(Message.RecipientType.CC, cc_address);
//
//            }  

            message.setSubject(SUBJECT);
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setText(MESSAGE, "UTF-8");
            mbp.setHeader("X-Priority", "High");
            mbp.setHeader("Content-Type", "text/html; charset=\"utf-8\"");

            Multipart content = new MimeMultipart();
            content.addBodyPart(mbp);

            message.setContent(content);
            Transport.send(message);

            result = "OK";

        } catch (MessagingException mex) {
            result = "";

        } catch (Exception e) {
            result = "";
        } finally {
            return result;
        }

    }

    public String getacknowstatus(String docno) {
        String stataus = "";
        try {

            String sql = " select * from travelreport where doc_no='" + docno + "'  ";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            while (rs.next()) {
                stataus = rs.getString("doc_status");
            }
            cConnect.Close();

            return stataus;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "ERROR";
        }
    }

    public String getmailusercc(String docno, String mdcode) {
        String mailcc = "";
        try {

            clsManage cMn = new clsManage();

            String sql = "   select a.*,e.emp_name,e.email from apvlog a "
                    + "inner join stxemphr e on e.emp_code = a.app_code  "
                    + "  where a.doc_no = '" + docno + "' and a.app_code not in ('" + mdcode + "')";

            clsConnect cConnect = new clsConnect();
            ResultSet rs = cConnect.getResultSet(sql);

            String prx = "";

            while (rs.next()) {
                String smail = cMn.chkNull(rs.getString("email")).trim() + "@mitsubishielevator.co.th";
                mailcc = mailcc + prx + smail;
                prx = ",";
            }
            cConnect.Close();

            return mailcc;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public String sendmailapproveackreject(String empcode, String appname, String from, String to, String subject, String docno, String country, String reportname) {

        String SMTP_HOST_NAME = "mail.mitsubishielevator.co.th"; //ขึ้นอยู่กับผู้ให้บริการ
        String SMTP_PORT = "25";//ขึ้นอยู่กับผู้ให้บริการ
        String FROM = from;
        String TO = to;
        String result = "";

        String SUBJECT = "Reject Travelling Report Doc no: " + docno + " By:" + appname;
        String MESSAGE = "<br><br>K. " + appname + "  Reject  Travelling Report Doc no: " + docno;
        MESSAGE += "<br><br>Report From :" + reportname;
        MESSAGE += "<br><br>================================================ ";
        MESSAGE += "<br>Please contact MD/Director ";
        //  MESSAGE += "<br>Please See detail >>> <a href='http://150.152.182.206:8088/travelling'> http://150.152.182.206:8088/travelling </a> ";

        //   http://localhost:8084/travellingonline/reportackview.jsp?id=T1605685
//
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);

        Session session = Session.getInstance(props, null);
        session.setDebug(false);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            InternetAddress[] to_address = InternetAddress.parse(TO);
            message.setRecipients(Message.RecipientType.TO, to_address);
//            String ccmail = getccmail();
//            String reqgan[] = ccmail.split("\\|");
//
//            if (reqgan.length > 0) {
//                String xccmail = "";
//                String xprx = "";
//                for (int i = 0; i < reqgan.length; i++) {
//
//                    String ccempcode = reqgan[i].trim();
//
//                    String suser = getmailuser(ccempcode);
//                    String arruser[] = suser.split("\\^");
//                    xccmail += xprx + arruser[0].trim();
//                    xccmail += "@mitsubishielevator.co.th";
//                    xprx = ",";
//
//                }
//            InternetAddress[] cc_address = InternetAddress.parse(CC);
//            message.setRecipients(Message.RecipientType.CC, cc_address);
//
//            }  

            message.setSubject(SUBJECT);
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setText(MESSAGE, "UTF-8");
            mbp.setHeader("X-Priority", "High");
            mbp.setHeader("Content-Type", "text/html; charset=\"utf-8\"");

            Multipart content = new MimeMultipart();
            content.addBodyPart(mbp);

            message.setContent(content);
            Transport.send(message);

            result = "OK";

        } catch (MessagingException mex) {
            result = "";

        } catch (Exception e) {
            result = "";
        } finally {
            return result;
        }

    }

//</editor-fold>
}
