<%@ page import="com.crystaldecisions.report.web.viewer.CrystalReportViewer" %>
<%@ page import="com.crystaldecisions.reports.sdk.ReportClientDocument" %>
<%@page import="cls.clsConnect"%>
<%@page import="java.sql.*"%>
<%   

    Object reportSource = session.getAttribute("reportSource");
    
    if (reportSource == null)
    {
       String report = "repform5.rpt"; 
           //      String report = "Crystaltest.rpt"; 
        ReportClientDocument reportClientDoc = new ReportClientDocument();
        reportClientDoc.open(report, 0);
        reportSource = reportClientDoc.getReportSource();
        session.setAttribute("reportSource", reportSource);
    }
    CrystalReportViewer viewer = new CrystalReportViewer();
    viewer.setDisplayGroupTree(false);
    viewer.setReportSource(reportSource);
    viewer.setOwnPage(true);
    viewer.processHttpRequest(request, response, getServletConfig().getServletContext(), null);

    
%>