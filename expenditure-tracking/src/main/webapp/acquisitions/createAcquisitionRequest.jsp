<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="acquisitionProcess.title.createPurchaseOrderDocument" bundle="ACQUISITION_RESOURCES"/></h2>

<%--
<p class="mtop15"><strong><bean:message key="label.requester" bundle="EXPENDITURE_RESOURCES"/></strong></p>
--%>

<html:messages id="message" message="true" bundle="MISSION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<bean:define id="processRequest" name="acquisitionProcess" property="request" toScope="request"/>
<jsp:include page="commons/viewAcquisitionRequest.jsp" flush="true"/>


<p class="mtop15"><strong><bean:message key="label.supplier" bundle="EXPENDITURE_RESOURCES"/></strong></p>

<div class="infobox">

	<fr:view name="acquisitionProcess" property="acquisitionRequest.supplier"
			schema="viewAcquisitionSupplier">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
	<bean:define id="acquisitionProcessOID" name="acquisitionProcess" property="externalId" type="java.lang.String"/>
	<bean:define id="acquisitionProcessClass" name="acquisitionProcess" property="class.simpleName"/>
	<bean:define id="actionMapping" value="<%= "/acquisition" + acquisitionProcessClass %>"/>
	
</div>

<logic:present name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet">
	<bean:size id="totalItems" name="acquisitionProcess" property="acquisitionRequest.acquisitionRequestItemsSet"/>
	<logic:iterate id="acquisitionRequestItem" name="acquisitionProcess" property="acquisitionRequest.orderedRequestItemsSet" indexId="index">
		<bean:define id="currentIndex" value="<%= String.valueOf(index + 1) %>"/>
		<p class="mtop15"><strong><bean:message key="acquisitionRequestItem.label.item" bundle="ACQUISITION_RESOURCES"/></strong> (<fr:view name="currentIndex"/>/<fr:view name="totalItems"/>)</p>
		<bean:define id="item" name="acquisitionRequestItem" toScope="request"/>
		<jsp:include page="commons/viewAcquisitionRequestItem.jsp" flush="false"/>
	</logic:iterate>
</logic:present>

<div class="documents">
	<p>
		<bean:message key="acquisitionProcess.label.requestDocument" bundle="ACQUISITION_RESOURCES"/>:
		<logic:present name="acquisitionProcess" property="purchaseOrderDocument">
			
			<bean:define id="acquisitionRequestDocumentOID" name="acquisitionProcess" property="purchaseOrderDocument.externalId" type="java.lang.String"/>
			
			<a id="file" href="<%= request.getContextPath() +  actionMapping + ".do?method=downloadAcquisitionPurchaseOrderDocument&purchaseOrderDocumentOid=" + acquisitionRequestDocumentOID%>">
				<span id="fileName">
					<bean:write name="acquisitionProcess" property="purchaseOrderDocument.filename"/>
				</span>
			</a>
		</logic:present>
		<logic:notPresent name="acquisitionProcess" property="purchaseOrderDocument">
			<a id="file" href="#">
				<span id="fileName">
					<em><bean:message key="document.message.info.notAvailable" bundle="EXPENDITURE_RESOURCES"/></em>
				</span>
			</a>
		</logic:notPresent>
	</p>
</div>

<bean:define id="url"><%= actionMapping %>.do?method=createAcquisitionPurchaseOrderDocument&amp;acquisitionProcessOid=<%= acquisitionProcessOID %></bean:define>

<logic:present name="acquisitionProcess" property="purchaseOrderDocument">
	<div class="infobox_warning">
			<bean:message key="label.warning.purchaseOrderDocumentAlreadyExists" bundle="ACQUISITION_RESOURCES"/>
	</div>
</logic:present>

<div class="switchInline">
	<form id="createFile" action="<%= request.getContextPath()+ url %>" method="post" target="iframe">
		<p>
			<a href="#"><bean:message key="acquisitionProcess.link.createPurchaseOrderDocument" bundle="ACQUISITION_RESOURCES"/></a>
		</p>
		<iframe id="iframe" name="iframe" src="" style="display: none;"></iframe>
	</form>
	
	<script type="text/javascript">
		$("#createFile > p > a").click( function() {
			$("#createFile").submit();
		});
		
		$("#iframe").load(function() { 
				var iframeContents =  $("#iframe").contents();
				var link = iframeContents.find("#file").attr('href');
				var name = iframeContents.find("#fileName").html();
				if (link != null && name != null) {
					$("#file").attr('href',link);
					$("#fileName").replaceWith(name);
					$("#iframe").attr('src',link);
				}
		 });
	</script>
</div>

<div class="switchNone">
	<form id="createFile" action="<%= request.getContextPath()+ url %>" method="post">
		<html:submit styleClass="inputbutton"><bean:message key="acquisitionProcess.link.createPurchaseOrderDocument" bundle="ACQUISITION_RESOURCES"/></html:submit>
	</form>
</div>

<bean:define id="urlView"><%= actionMapping %>.do?method=viewAcquisitionProcess&amp;acquisitionProcessOid=<%= acquisitionProcessOID %></bean:define>
<fr:form action="<%= urlView %>">
	<p class="mtop15">
	<html:submit styleClass="inputbutton">
		<bean:message key="button.back" bundle="EXPENDITURE_RESOURCES"/>
	</html:submit>
	</p>
</fr:form>

<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/switchScript.js" %>"></script>
<script type="text/javascript">
	switchGlobal();
</script>
