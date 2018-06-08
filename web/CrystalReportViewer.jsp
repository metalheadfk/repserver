<%@ taglib uri="/crystal-tags-reportviewer.tld" prefix="crviewer" %>
<%@page pageEncoding="UTF-8"%>
<crviewer:viewer viewerName="CrystalViewer" reportSourceType="reportingComponent">

<!-- Create the required report tag to specify the report to display 
and the session variable to cache the report source. -->
<crviewer:report reportName="repform5.rpt"/>
</crviewer:viewer>
