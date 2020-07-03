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
    final String REPORT_NAME = "test.rpt";
    String tmpid = request.getParameter("tmpid");

%>

<%    try {
        //Open report.

        String sql = "";
        //String sdoc = request.getParameter("doc");	

        // sql = " select * from employee where emp_code = '03955' ";
        sql = " select h.tmph_id,h.tmph_desc,h.tmph_iso ,g.section_id,s.grp_name as secname ,g.grp_id,g.grp_name,d.tmpd_line,d.tmpd_desc,d.tmpd_type,d.tmpd_uom,d.tmpd_sort,c.choice_id,c.choice_seq,c.choice_desc,c.choice_val "
                + " from safety_tmph h "
                + " inner join safety_tmpgrp g on g.tmph_id = h.tmph_id  and  g.grp_type = 'GRP'   "
                + " left outer join safety_tmpgrp s on s.grp_id = g.section_id  and  s.grp_type = 'SEC'  "
                + " inner join safety_tmpd d on d.tmph_id = h.tmph_id and d.tmpd_grp = g.grp_id "
                + " left outer join  safety_tmpchoice c on c.tmpd_grp = g.grp_id and c.tmpd_line = d.tmpd_line "
                + " where h.tmph_id = '" + tmpid + "' "
                + " order by d.tmpd_sort,c.choice_seq asc ";

//    if (!sdoc.equals("")){
//    	
//    	sql = sql + " where doc_no='" + sdoc.trim() + "' ";
//    }
        clsConnect cConnect = new clsConnect();
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
        response.sendRedirect("viewrep.jsp?repname=" + "test.rpt"+ "&id=example");
    } catch (ReportSDKException ex) {
        out.println(ex);
    } catch (Exception ex) {
        out.println(ex);
    }
%>