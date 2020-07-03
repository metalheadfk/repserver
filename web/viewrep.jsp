<%-- 
    Document   : viewrep
    Created on : Dec 11, 2019, 1:41:12 PM
    Author     : R6100041-LE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="cls.clsConnect"%>
<%@page import="java.sql.*"%>
<%@page contentType="text/html"%> 

<%//Crystal Reports for Eclipse Version 2 imports.%>

<%@ page import="com.crystaldecisions.sdk.occa.report.application.*"%>
<%@ page import="com.crystaldecisions.sdk.occa.report.data.*"%>
<%@ page import="com.crystaldecisions.sdk.occa.report.document.*"%>
<%@ page import="com.crystaldecisions.sdk.occa.report.definition.*"%>
<%@ page import="com.crystaldecisions.sdk.occa.report.lib.*" %>
<%@ page import="com.crystaldecisions.report.web.viewer.CrystalReportViewer" %>
<%@ page import="com.crystaldecisions.sdk.occa.report.exportoptions.*" %> 
<%@ page import="com.crystaldecisions.report.web.viewer.*"%>
<!DOCTYPE html>
<%
    String repname = request.getParameter("repname");
    Object reportSource = session.getAttribute("reportSource");
    String id = request.getParameter("id");

    if (reportSource == null) {
        //String report = "test.rpt";
        //      String report = "Crystaltest.rpt"; 
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        reportClientDoc.open(repname, 0);
        reportSource = reportClientDoc.getReportSource();  
        
        session.setAttribute("reportSource", reportSource);
        

    }
    
//    CrystalReportViewer viewer = new CrystalReportViewer();
//    viewer.setDisplayGroupTree(false);
//    viewer.setReportSource(reportSource);
//    viewer.setOwnPage(true);
//    viewer.processHttpRequest(request, response, getServletConfig().getServletContext(), null); 
//    viewer.refresh();
//    viewer.dispose();
//
    ReportExportControl exportControl = new ReportExportControl();
    ExportOptions exportOptions = new ExportOptions();
    exportOptions.setExportFormatType(ReportExportFormat.PDF);
    exportControl.setReportSource(reportSource);
    exportControl.setExportOptions(exportOptions);
    exportControl.setName(id);
    exportControl.processHttpRequest(request, response, getServletConfig().getServletContext(), out);


%>
