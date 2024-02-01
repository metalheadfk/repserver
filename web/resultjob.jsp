<%@page import="cls.clsConnectafs"%>
<%@page import="cls.clsConnectf"%>
<%@page import="com.crystaldecisions.report.web.viewer.CrystalReportViewer"%>
<%@page import="sun.misc.BASE64Decoder"%>
<%@page import="cls.clsManage"%>
<%@page import="com.crystaldecisions.sdk.occa.report.exportoptions.ReportExportFormat"%>
<%@page import="com.crystaldecisions.sdk.occa.report.exportoptions.ExportOptions"%>
<%@page import="com.crystaldecisions.report.web.viewer.ReportExportControl"%>
<%@page import="java.sql.Connection"%> 
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
//    final String REPORT_NAME = "safetyjob.rpt";
//    String docno = request.getParameter("docno");
//    String type = (request.getParameter("type") == null) ? "" : request.getParameter("type");
//
//    session.removeAttribute("reportSource");

%>

<%    try {
        //Open report.

        //get sign 1
        clsConnectafs cConnect = new clsConnectafs();

        String sign1 = "";
        String sign2 = "";
        String app1 = "";
        String app2 = "";
        clsManage cl = new clsManage();
        String sqlx = " select p.id,p.name,p.display_name,p.function,p.email,p.phone ,p.parent_id ,h.name as parentname from res_partner p   "
                + " inner join res_partner h on h.id = p.parent_id  "
                + "  where p.active = true ";
        ResultSet rsx = cConnect.getResultSet(sqlx);

        ReportClientDocument boReportClientDocument = new ReportClientDocument();
        boReportClientDocument.open("report_example.rpt", 0);

        boReportClientDocument.getDatabaseController().setDataSource(rsx);

        ReportExportControl exportControl = new ReportExportControl();
        ExportOptions exportOptions = new ExportOptions();
        exportOptions.setExportFormatType(ReportExportFormat.PDF);
        exportControl.setReportSource(boReportClientDocument.getReportSource());
        exportControl.setExportOptions(exportOptions); 
        exportControl.processHttpRequest(request, response, getServletConfig().getServletContext(), out);
//    crystal viewver
       // ReportClientDocument reportClientDoc = new ReportClientDocument();
       //  reportClientDoc.open("report_example.rpt", 0);
        CrystalReportViewer viewer = new CrystalReportViewer();
        viewer.setDisplayGroupTree(false);
        viewer.setReportSource(boReportClientDocument.getReportSource());
        viewer.setOwnPage(true);
        viewer.setSeparatePages(false);
        viewer.processHttpRequest(request, response, getServletConfig().getServletContext(), null);
        viewer.refresh();
        viewer.dispose();

//    
        cConnect.Close();

    } catch (ReportSDKException ex) {
        out.println(ex.getMessage().toString());
    } catch (Exception ex) {
        out.println(ex.getMessage().toString());
    }
%>