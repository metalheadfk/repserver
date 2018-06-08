package cls;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory; 
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List; 

public class clsManage {

//<editor-fold defaultstate="collapsed" desc="Date">
    public static String Get_Now(String format) { //dd/MM/yyyy
        SimpleDateFormat date_format = new SimpleDateFormat(format);
        String now = date_format.format(new Date());
        return now;
    }

    public static String Get_Day() { //dd/MM/yyyy
        Calendar cal = Calendar.getInstance();
        int data = cal.get(Calendar.DATE);
        return String.valueOf(data);
    }

    public static String Get_Month() { //dd/MM/yyyy
        Calendar cal = Calendar.getInstance();
        int data = cal.get(Calendar.MONTH) + 1;
        return String.valueOf(data);
    }

    public static String Get_Year() { //dd/MM/yyyy
        Calendar cal = Calendar.getInstance();
        int data = cal.get(Calendar.YEAR);
        return String.valueOf(data);
    }

    public static String Get_MonthName(int data) { //dd/MM/yyyy
        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return months[data - 1];
    }

    public static String convDMY2YMD(String dateDMY) {
        String subStringDate[] = dateDMY.split("-");
        String dates = subStringDate[0];
        String months = subStringDate[1];
        String years = subStringDate[2];
        return years + "-" + months + "-" + dates;
    }

    public static String convYMD2DMY(String dateYMD) {
        String subStringDate[] = dateYMD.split("-");
        String years = subStringDate[0];
        String months = subStringDate[1];
        String dates = subStringDate[2];
        return dates + "-" + months + "-" + years;
    }

    public static String convDMY2YYYYMMDD(String dateDMY) {
        String subStringDate[] = dateDMY.split("-");
        String dates = chkFormatDate(Integer.parseInt(subStringDate[0]));
        String months = chkFormatDate(Integer.parseInt(subStringDate[1]));
        String years = subStringDate[2];
        return years + "" + months + "" + dates;
    }

    public static String manageYear4DMY(String dateDMY, int chk) { //chk 1=Add //-1=Del
        String subStringDate[] = dateDMY.split("-");
        String dates = subStringDate[0];
        String months = subStringDate[1];
        String years = subStringDate[2];
        int newYear;
        if (chk >= 1) {
            newYear = (Integer.parseInt(years) + 543);
        } else {
            newYear = (Integer.parseInt(years) - 543);
        }
        return dates + "-" + months + "-" + newYear;
    }

    public static String AddDays(Date oDate, int n, String format) {
        SimpleDateFormat date_format = new SimpleDateFormat(format);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(oDate);
        calendar.add(Calendar.DATE, n);

        return date_format.format(calendar.getTime());
    }

    public static String ConvertDateToString(Date oDate, String format) {
        if (oDate == null) {
            return "-";
        } else {
            Locale locale = Locale.ENGLISH;
            SimpleDateFormat date_format = new SimpleDateFormat(format, locale);

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(oDate);

            String result = date_format.format(calendar.getTime());
            return result;
        }
    }

    public static Date ConvertStringToDate(String oDate, String format) throws ParseException {
        if (oDate == null) {
            return null;
        } else {
            Locale locale = Locale.ENGLISH;
            SimpleDateFormat sdf = new SimpleDateFormat(format, locale);

            Date tmp_date = sdf.parse(oDate);

            Calendar tmp_calendar = new GregorianCalendar();
            tmp_calendar.setTime(tmp_date);

            if (tmp_calendar.get(Calendar.YEAR) < 2000) {
                tmp_calendar.add(Calendar.YEAR, 543);
            }

            return tmp_calendar.getTime();
        }
    }

    public static int chkHoliday(String startDate, String endDate, String saturday, String sunday) { //dd/MM/yyyy
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

            //1 = Sunday, 2 = Monday, 3 = Tuesday, 4 = Wednesday, 4 = Thursday, 6 = Friday, 7 = Saturday
            String strPointDay = "";
            String mergeDate = "";
            Date nextDateD;
            String nextDateS;
            int countHoliday = 0;
            int chkLastRecord = 0; //ถ้าลงท้ายด้วยวันเสาร์ต้อง + อีก 1
            while (Integer.parseInt(strStart) <= Integer.parseInt(strEnd)) {
                strPointDay = strStart;
                String splitY = strPointDay.substring(0, 4);
                String splitM = strPointDay.substring(4, 6);
                String splitD = strPointDay.substring(6, 8);

                mergeDate = splitD + "/" + splitM + "/" + splitY; //dd/MM/YYYY

                Calendar cal = new GregorianCalendar(Integer.parseInt(splitY), Integer.parseInt(splitM) - 1, Integer.parseInt(splitD));
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

                if (saturday.equals("Y")) { //1=sunday   7=saturday
                    if (dayOfWeek == 7) {
                        countHoliday = countHoliday + 1;
                    }
                }
                if (sunday.equals("Y")) { //1=sunday   7=saturday
                    if (dayOfWeek == 1) {
                        countHoliday = countHoliday + 1;
                    }
                }

                chkLastRecord = dayOfWeek;
                nextDateD = clsManage.ConvertStringToDate(mergeDate, "dd/MM/yyyy");
                nextDateS = clsManage.AddDays(nextDateD, 1, "dd/MM/yyyy");
                String subStringNextDateS[] = nextDateS.split("/");
                dateS = subStringNextDateS[0];
                monthS = subStringNextDateS[1];
                yearS = subStringNextDateS[2];
                strStart = yearS + "" + monthS + "" + dateS;

            }
//            if (sunday.equals("Y")) {
//                if (chkLastRecord == 7) {
//                    countHoliday = countHoliday + 1;
//                }
//            }

            return countHoliday;
        } catch (Exception ex) {
            return 0;
        }
    }

    public static int chkDay(String oDate) { //dd/MM/yyyy
        try {
            String subStringStartDate[] = oDate.split("/");
            String splitD = subStringStartDate[0];
            String splitM = subStringStartDate[1];
            String splitY = subStringStartDate[2];

            //1 = Sunday, 2 = Monday, 3 = Tuesday, 4 = Wednesday, 4 = Thursday, 6 = Friday, 7 = Saturday
            Calendar cal = new GregorianCalendar(Integer.parseInt(splitY), Integer.parseInt(splitM) - 1, Integer.parseInt(splitD));
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

            return dayOfWeek;
        } catch (Exception ex) {
            return 0;
        }
    }

//    public static String chkDay(String oDate) {
//        if (oDate == null) {
//            return null;
//        } else {
//            //0 = Sunday, 1 = Monday, 2 = Tuesday, 3 = Wednesday, 4 = Thursday, 5 = Friday, 6 = Saturday
//            //Calendar cal = new GregorianCalendar(2003, Calendar.JANUARY, 1);
//            String subStringDate[] = oDate.split("/");
//            int dates = Integer.parseInt(subStringDate[0]);
//            int months = Integer.parseInt(subStringDate[1]);
//            int years = Integer.parseInt(subStringDate[2]);
//            Calendar cal = new GregorianCalendar(years, months, dates);
//            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
//            return String.valueOf(dayOfWeek);
//        }
//    }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Grant">
    public static String getAuthenName(String authen) {
        if (authen.equals("SA")) {
            return "System Admin";
        } else if (authen.equals("AD")) {
            return "Admin";
        } else if (authen.equals("CO")) {
            return "Coordinator";
        } else if (authen.equals("US")) {
            return "User";
        } else {
            return "Unknow : " + authen;
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="ConvertText">
    public static String convPriorityCode2Name(String PriorityCode) {
        if (PriorityCode.equals("H")) {
            return "High";
        } else if (PriorityCode.equals("M")) {
            return "Medium";
        } else if (PriorityCode.equals("L")) {
            return "Low";
        } else {
            return "";
        }
    }

    public static String convStatusCode2Name(String StatusCode) {
        if (StatusCode.equals("N")) {
            return "New";
        } else if (StatusCode.equals("O")) {
            return "On Process";
        } else if (StatusCode.equals("S")) {
            return "Success";
        } else if (StatusCode.equals("C")) {
            return "Cancel";
        } else {
            return "";
        }
    }

    public static String convChkPointCode2Name(String chkPointCode) {
        if (chkPointCode.equals("SP")) {
            return "Start Project";
        } else if (chkPointCode.equals("PD")) {
            return "Production";
        } else if (chkPointCode.equals("SH")) {
            return "Ship To";
        } else if (chkPointCode.equals("TS")) {
            return "To Site";
        } else if (chkPointCode.equals("FP")) {
            return "Finish Project";
        } else {
            return "";
        }
    }

    public static String convProductCode2Name(String PriorityCode) {
        if (PriorityCode.equals("ESC")) {
            return "Escalator";
        } else if (PriorityCode.equals("ELE")) {
            return "Elevator";
        } else {
            return "";
        }
    }

    public static String convDocType(String data) {
        if (data.equals("D")) {
            return "Doc";
        } else if (data.equals("L")) {
            return "URL";
        } else {
            return "";
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Other">
    public static String chkNull(String Data) {

        return (Data == null) ? "" : Data;
        
    }
    
    public static String chkNullview(String Data) {

         String x = Data.trim();
        if (Data.equals("")){
           x = "&nbsp;";
        }
        if (Data == null){
           x = "&nbsp;";
        }
       return x;
        
    }
    
    public static String checkblank(String Data) {
        String x = Data.trim();
        if (Data.equals("")){
           x = "-";
        }
        if (Data == null){
           x = "-";
        }
       return x;
    }

    public static String strReplace(String data, String oldSign, String newSign) {
        String sign = data.replace(oldSign, newSign);
        return sign;
    }

    public static String chkFormatDate(int Data) {
        if (Data < 10) {
            return "0" + Data;
        } else {
            return String.valueOf(Data);
        }
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="NumberFormat">
    public static String numberFormat(Double Data) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("#,###.00");
        return df.format(Data);
    }

    public static String percentage(Double Data) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("#.00");
        return df.format(Data);
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Blank">
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="P'AR">
//    public String getDisplayTime(Date oDate) {
//        Date today = new Date();
//
//        if (today.compareTo(oDate) > 0) {
//            return DateFormat(oDate, "HH:mm:ss");
//        } else {
//            return DateFormat(oDate, "dd-MM-yyyy HH:mm:ss");
//        }
//    }
    public String AddMonth(Date oDate, int n, String format) {
        SimpleDateFormat date_format = new SimpleDateFormat(format);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(oDate);
        calendar.add(Calendar.MONTH, n);

        return date_format.format(calendar.getTime());
    }

    public String AddYear(Date oDate, int n, String format) {
        SimpleDateFormat date_format = new SimpleDateFormat(format);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(oDate);
        calendar.add(Calendar.YEAR, n);

        return date_format.format(calendar.getTime());
    }

    public static int ConvertBoolToBit(boolean value) {
        if (value) {
            return 1;
        } else {
            return 0;
        }
    }

    public static byte[] ConvertImageToBytes(String image_path) {
        byte[] tmp_byte = null;

        try {
            InputStream is = new FileInputStream(image_path);
            tmp_byte = ConvertInputStreamToBytes(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return tmp_byte;
    }

    public static byte[] ConvertInputStreamToBytes(InputStream is) {
        byte[] tmp_byte = null;

        try {
            tmp_byte = new byte[is.available()];
            is.read(tmp_byte);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return tmp_byte;
    }

//    public static Date ConvertStringToDate(String oDate, String format) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat(format);
//        Date tmp_date = sdf.parse(oDate);
//
//        Calendar tmp_calendar = new GregorianCalendar();
//        tmp_calendar.setTime(tmp_date);
//
//        if (tmp_calendar.YEAR < 2500) {
//            tmp_calendar.add(Calendar.YEAR, 543);
//        }
//
//        return tmp_calendar.getTime();
//    }
//    public static String DateFormat(Date oDate, String format) {
//        Locale locale = Locale.ENGLISH;
//        SimpleDateFormat date_format = new SimpleDateFormat(format, locale);
//
//        Calendar calendar = new GregorianCalendar();
//        calendar.setTime(oDate);
//
//        String result = date_format.format(calendar.getTime());
//        return result;
//    }
    public static Date getDate(int oyear, int omonth, int oday) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(oyear, omonth, oday);

        return calendar.getTime();
    }

    public static String HexToString(String oValue) {
        String result = "";

        for (int i = 0; i < oValue.length(); i += 2) {
            int tmp = Integer.parseInt(oValue.substring(i, i + 2), 16);

            if (tmp == 0) {
                result += "";
            } else {
                result += (char) tmp;
            }
        }

        return result;
    }

    public static String StringToHex(String oValue) {
        String result = "";
        char tmp[] = oValue.toCharArray();

        for (int i = 0; i < oValue.length(); i++) {
            result += Integer.toHexString((int) tmp[i]);
        }

        return result;
    }

    public static String StringToMD5(String oValue) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(oValue.getBytes("UTF8"));
            byte tmp[] = md.digest();

            String result = "";

            for (int i = 0; i < tmp.length; i++) {
                result += Integer.toHexString((0x000000ff & tmp[i]) | 0xffffff00).substring(6).toUpperCase();
            }

            return result;
        } catch (Exception ex) {
            return null;
        }
    }
//</editor-fold>

// <editor-fold defaultstate="collapsed" desc="Function of P'AR">
    private String getClassName() {
        String thisClassName;

        //Build a string with executing class's name
        thisClassName = this.getClass().getName();
        thisClassName = thisClassName.substring(thisClassName.lastIndexOf(".") + 1, thisClassName.length());
        thisClassName += ".class";  //this is the name of the bytecode file that is executing

        return thisClassName;
    }

    public String getLocalDirName() {
        String localDirName;
        //Use that name to get a URL to the directory we are executing in
        //Open a URL to the our .class file
        java.net.URL myURL = this.getClass().getResource(getClassName());
        //Clean up the URL and make a String with absolute path name
        localDirName = myURL.getPath();  //Strip path to URL object out
        localDirName = myURL.getPath().replaceAll("%20", " ");  //change %20 chars to spaces
        //Get the current execution directory
        localDirName = localDirName.substring(0, localDirName.lastIndexOf("/"));  //clean off the file name

        return localDirName;
    }
// </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Folder">
    public String getAppSetting(String config_name) {
        try {
            ResourceBundle rb = new PropertyResourceBundle(new FileInputStream(getLocalDirName() + "/appconfig.properties"));
            String config = rb.getString(config_name);
            return config;
        } catch (IOException ex) {
            return null;
        }
    }

    public void ConvertByteToFile(String filepath, byte[] obyte) throws IOException {
        try {
            this.DeleteFile(filepath);

            FileOutputStream fos = new FileOutputStream(filepath);
            fos.write(obyte);
            fos.close();
        } catch (FileNotFoundException ex) {
        }
    }

    public boolean DeleteFile(String filepath) {
        File oFile = new File(filepath);

        //??????????????????????????????
        if (this.CheckExitsFile(filepath)) {
            return oFile.delete();
        } else {
            return true;
        }
    }

    public boolean CheckExitsFile(String filepath) {
        File oFile = new File(filepath);
        return oFile.exists();
    }
    
     

    
//</editor-fold>
}
