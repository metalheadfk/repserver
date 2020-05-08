<%@page import="cls.clsManage"%>
<%@page import="com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat"%>
<%@page import="com.crystaldecisions.sdk.occa.report.exportoptions.ExportOptions"%>
<%@page import="com.crystaldecisions.report.web.viewer.ReportExportControl"%>
<%
    /* 
 * Applies to versions:	Crystal Reports for Eclipse Version 2
 * Date Created: November 2008
 * Description: This sample demonstrates how to use RCAPI to add a ResultSet Datasource to a report
 * Author: SP.
     */
%>
<%@page import="cls.clsConnect"%>
<%@page import="java.sql.*"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%//Crystal Reports for Eclipse Version 2 imports.%>
<%@ page import="com.crystaldecisions.sdk.occa.report.application.*"%>
<%@ page import="com.crystaldecisions.sdk.occa.report.data.*"%>
<%@ page import="com.crystaldecisions.sdk.occa.report.document.*"%>
<%@ page import="com.crystaldecisions.sdk.occa.report.definition.*"%>
<%@ page import="com.crystaldecisions.sdk.occa.report.lib.*" %>


<%
//Report can be opened from the relative location specified in the CRConfig.xml, or the report location
//tag can be removed to open the reports as Java resources or using an absolute path (absolute path not recommended
//for Web applications).
    final String REPORT_NAME = "safetyjob.rpt";
    String docno = request.getParameter("docno");
    String type = (request.getParameter("type") == null) ? "" : request.getParameter("type");

    session.removeAttribute("reportSource");

%>

<%    try {
        //Open report.
        
        //get sign 1
        clsConnect cConnect = new clsConnect();
        
        String sign1 = "";
        String sign2 = "";
        String app1 = "";
        String app2 = "";
        clsManage cl = new clsManage();
        String sqlx = " select * from safety_apvlog where doc_no = '" + docno + "' order by step_id asc ";
        ResultSet rsx= cConnect.getResultSet(sqlx);
        while (rsx.next()) {
            
            if(sign1.equals("") && rsx.getString("step_id").equals("1")){
                sign1 = cl.chkNull(rsx.getString("app_email"));
                app1 = cl.chkNull(rsx.getString("app_status"));
            }else{
                sign2 = cl.chkNull(rsx.getString("app_email"));
                app2 = cl.chkNull(rsx.getString("app_status"));
            } 
            
        }
        
        if(sign2.equals("") && app1.equals("Y")){
                sign2 = sign1;
                app2 = app1;
        }

        String sql = "";

        sql = "  select h.*,d.sec_id,d.sec_name,d.group_id,d.group_name,d.doc_line,d.line_desc,d.line_type,d.line_min,d.line_max,d.line_std,d.line_txt,d.line_val,d.line_amt,d.line_uom,c.choice_id,c.choice_desc,c.choice_val "
                + " ,i.image_id,i.image_data,   "
                + " (select sum(line_val) from safety_jobd where doc_no = h.doc_no and line_val > 0) as total, "
                + " (select x.line_std from safety_jobd x where x.doc_no = d.doc_no  and  x.doc_line = d.doc_line and x.line_std = c.choice_val and x.line_val > 0) as stdval, "
                + " (select x.line_val from safety_jobd x where x.doc_no = d.doc_no  and  x.doc_line = d.doc_line and x.line_val = c.choice_val and x.line_val >= 0) as val, "
                + " (select x.line_val from safety_jobd x where x.doc_no = d.doc_no  and  x.doc_line = d.doc_line and x.line_val = c.choice_val ) as valna "
                + " ,e.emp_name_th,e2.emp_name_th as refname,'"+ sign1 +"' as sign1, '"+ sign2 +"' as sign2,'"+ app1 +"' as app1, '"+ app2 +"' as app2 "
                + " from safety_jobh h "
                + " inner join  safety_jobd d on d.doc_no = h.doc_no "
                + " left outer join  safety_jobc c on c.doc_no = d.doc_no and c.doc_line = d.doc_line  "
                + " left outer join   safety_jobimage i on i.doc_no = d.doc_no and i.doc_line = d.doc_line  and i.image_id is not null "
                + " left outer join  employee e on e.emp_code = h.create_by "
                + " left outer join  employee e2 on e2.emp_code = h.ref_code "
                + " where h.doc_no = '" + docno + "'  "
                + "  and (  "
                + " (select count(*) from safety_jobimage where doc_no = d.doc_no and doc_line = d.doc_line and d.line_type = 'IMG' )  > 0  "
                + " or  "
                + " (select '1' from safety_jobd where doc_no = d.doc_no and doc_line = d.doc_line and d.line_type <> 'IMG' )  = 1 "
                + " ) "
                + " order by d.sec_id,d.doc_line,d.group_id,c.choice_id,i.image_id  asc ";
//        
//        sql = " select h.*,d.sec_id,d.sec_name,d.group_id,d.group_name,d.doc_line,d.line_type,d.line_desc,d.line_txt,d.line_val,d.line_amt,d.line_uom,c.choice_id,c.choice_desc,c.choice_val "
//                + " ,i.image_id,i.image_data , (select sum(line_val) from safety_jobd where doc_no = h.doc_no) as total,e.emp_name_th,e2.emp_name_th as refname,"
//                + " (select x.line_val from safety_jobd x where x.doc_no = d.doc_no  and  x.doc_line = d.doc_line and x.line_val = c.choice_val) as val "
//                + " from safety_jobh h "
//                + " inner join  safety_jobd d on d.doc_no = h.doc_no "
//                + " left outer join  safety_jobc c on c.doc_no = d.doc_no and c.doc_line = d.doc_line "
//                + " left outer join   safety_jobimage i on i.doc_no = d.doc_no and i.doc_line = d.doc_line and i.image_id is not null "
//                + " left outer join  employee e on e.emp_code = h.create_by"
//                + " left outer join  employee e2 on e2.emp_code = h.ref_code"
//                + " where h.doc_no = '" + docno + "' "
//                + "and ( "
//                + "(select count(*) from safety_jobimage where doc_no = d.doc_no and doc_line = d.doc_line and d.line_type = 'IMG' )  > 0 "
//                + "or "
//                + "(select '1' from safety_jobd where doc_no = d.doc_no and doc_line = d.doc_line and d.line_type <> 'IMG' )  = 1"
//                + ")"
//                + " order by d.doc_line,c.choice_id,i.image_id asc  ";
       // clsConnect cConnect = new clsConnect();
        ResultSet javaResultSet = cConnect.getResultSet(sql);

        ReportClientDocument boReportClientDocument = new ReportClientDocument();
        boReportClientDocument.open(REPORT_NAME, 0);

        boReportClientDocument.getDatabaseController().setDataSource(javaResultSet);
        
        
 
	
        
        
        
        

//        Object reportSource = boReportClientDocument.getReportSource();
//        ReportExportControl exportControl = new ReportExportControl();
//        ExportOptions exportOptions = new ExportOptions();
//        exportOptions.setExportFormatType(ReportExportFormat.PDF);
//        exportControl.setReportSource(reportSource);
//        exportControl.setExportOptions(exportOptions);
//        exportControl.processHttpRequest(request, response, getServletConfig().getServletContext(), out);
        // Add a couple ResultFields to the report to show it has worked.
        ///int INSERT_AT_END = -1;
        //Field boField = (Field)boReportClientDocument.getDatabaseController().getDatabase().getTables().getTable(0).getDataFields().get(0);
        //boReportClientDocument.getDataDefController().getResultFieldController().add( INSERT_AT_END, boField ); 
        //boField = (Field)boReportClientDocument.getDatabaseController().getDatabase().getTables().getTable(0).getDataFields().get(1);
        //boReportClientDocument.getDataDefController().getResultFieldController().add( INSERT_AT_END, boField );		
        //Store the report source in session, will be used by the CrystalReportViewer.
        session.setAttribute("reportSource", boReportClientDocument.getReportSource());
        //Launch CrystalReportViewer page that contains the report viewer.
        if (type.equals("view")) {
            response.sendRedirect("viewer.jsp?repname=" + "safetyjob.rpt" + "&id=" + docno);
        } else {
            response.sendRedirect("viewrep.jsp?repname=" + "safetyjob.rpt" + "&id=" + docno);
        }

    } catch (ReportSDKException ex) {
        out.println(ex.getMessage().toString());
    } catch (Exception ex) {
        out.println(ex.getMessage().toString());
    }
%>