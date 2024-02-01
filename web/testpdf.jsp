<%@page import="java.awt.GraphicsEnvironment"%>
<%@page import="com.crystaldecisions.reports.sdk.*" %> 
<%@page import="com.crystaldecisions.sdk.occa.report.reportsource.*" %> 
<%@page import="com.crystaldecisions.sdk.occa.report.lib.*" %> 
<%@page import="com.crystaldecisions.sdk.occa.report.exportoptions.*" %> 
<%@page import="com.crystaldecisions.report.web.viewer.*"%>

<%

GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
ReportClientDocument reportClientDoc = new ReportClientDocument(); 
String report = "repform5.rpt"; 
reportClientDoc.open(report, 0); 
Object reportSource = reportClientDoc.getReportSource();         
ReportExportControl exportControl = new ReportExportControl(); 
ExportOptions exportOptions = new ExportOptions(); 
exportOptions.setExportFormatType(ReportExportFormat.PDF);   
exportControl.setReportSource(reportSource); 
exportControl.setExportOptions(exportOptions); 
exportControl.processHttpRequest(request, response, getServletConfig().getServletContext(), out);

%>