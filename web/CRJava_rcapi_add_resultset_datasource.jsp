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
final String REPORT_NAME = "repform5.rpt";

%>


<%

try {
	//Open report.
	
	String sql = "";
	String sdoc = request.getParameter("doc");	
		
    sql = "SELECT doc_no,doc_type,doc_date,emp_code,emp_tname,country_code,total_amt from stytrave  ";
    
    if (!sdoc.equals("")){
    	
    	sql = sql + " where doc_no='" + sdoc.trim() + "' ";
    }
    
    clsConnect cConnect = new clsConnect();
    ResultSet javaResultSet = cConnect.getResultSet(sql);
		
	ReportClientDocument boReportClientDocument = new ReportClientDocument();
	boReportClientDocument.open(REPORT_NAME, 0);
	
	
	boReportClientDocument.getDatabaseController().setDataSource(javaResultSet);

	
	// Add a couple ResultFields to the report to show it has worked.
	///int INSERT_AT_END = -1;
	//Field boField = (Field)boReportClientDocument.getDatabaseController().getDatabase().getTables().getTable(0).getDataFields().get(0);
	//boReportClientDocument.getDataDefController().getResultFieldController().add( INSERT_AT_END, boField ); 
	//boField = (Field)boReportClientDocument.getDatabaseController().getDatabase().getTables().getTable(0).getDataFields().get(1);
	//boReportClientDocument.getDataDefController().getResultFieldController().add( INSERT_AT_END, boField );		
	
	//Store the report source in session, will be used by the CrystalReportViewer.
	session.setAttribute("reportSource", boReportClientDocument.getReportSource());
		
	//Launch CrystalReportViewer page that contains the report viewer.
	response.sendRedirect("CrystalReport1-viewer.jsp");
		
}
catch(ReportSDKException ex) {	
	out.println(ex);
}
catch(Exception ex) {
	out.println(ex);			
}
%>