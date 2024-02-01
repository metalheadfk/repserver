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
    final String REPORT_NAME = "qrasset.rpt";
    String empcode = (request.getParameter("empcode") == null) ? "" : request.getParameter("empcode");   
    String type = (request.getParameter("type") == null) ? "" : request.getParameter("type");

    session.removeAttribute("reportSource");

%>

<%    try {
        //Open report.

        //get sign 1
        clsConnect cConnect = new clsConnect();  
 

//        String sql = "  select   asset_id , asset_ref as asset_no, '' as printdate ,(select model_name from it_models where model_id = a.asset_model) as asset_name "
//                + " from it_asset a "; 
//               sql += " where print_by = '"+ empcode +"' "; 
        
        
        String sql = "  select   asset_id , asset_ref as asset_no, '' as printdate ,asset_desc as asset_name,asset_location "
                + " from it_asset_print a ";
        sql += " where print_by = '"+ empcode +"' "; 
         
        ResultSet javaResultSet = cConnect.getResultSet(sql);
        
        

        ReportClientDocument boReportClientDocument = new ReportClientDocument();
        if(type.equals("L")){
            boReportClientDocument.open("qrasset.rpt", 0);
        }else{
            boReportClientDocument.open("qrassetsm.rpt", 0);
        }
        

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
        
        
        ReportExportControl exportControl = new ReportExportControl();
        ExportOptions exportOptions = new ExportOptions();
        exportOptions.setExportFormatType(ReportExportFormat.PDF);
        exportControl.setReportSource(boReportClientDocument.getReportSource());
        exportControl.setExportOptions(exportOptions);
        //exportControl.setName(id);
        exportControl.processHttpRequest(request, response, getServletConfig().getServletContext(), out);
//        
        

//
//    ReportClientDocument reportClientDoc = new ReportClientDocument();
//    reportClientDoc.open("safetyjob.rpt", 0);
//    CrystalReportViewer viewer = new CrystalReportViewer();
//    viewer.setDisplayGroupTree(false);
//    viewer.setReportSource(boReportClientDocument.getReportSource());
//    viewer.setOwnPage(true);
//    viewer.setSeparatePages(false);
//    viewer.processHttpRequest(request, response, getServletConfig().getServletContext(), null); 
//    viewer.refresh();
//    viewer.dispose();
//    
    cConnect.Close();

 
    } catch (ReportSDKException ex) {
        out.println(ex.getMessage().toString());
    } catch (Exception ex) {
        out.println(ex.getMessage().toString());
    }
%>