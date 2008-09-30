<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="process.label.searchProcesses" bundle="EXPENDITURE_RESOURCES"/></h2>

<fr:form action="/announcementProcess.do?method=searchAnnouncementProcess" >
	<fr:edit id="searchAnnouncementProcess"
			name="searchAnnouncementProcess"
			type="pt.ist.expenditureTrackingSystem.domain.announcements.SearchAnnouncementProcess"
			schema="searchAnnouncementProcess">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
		</fr:layout>
	</fr:edit>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/> </html:submit>
</fr:form>


<bean:define id="announcementProcesses" name="searchAnnouncementProcess" property="result"/>

<logic:notEmpty name="announcementProcesses">
	<fr:view name="announcementProcesses"
			schema="viewAnnouncementProcessInList">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 mtop2"/>
	
			<fr:property name="link(view)" value="/announcementProcess.do?method=viewAnnouncementProcess"/>
			<fr:property name="bundle(view)" value="EXPENDITURE_RESOURCES"/>
			<fr:property name="key(view)" value="link.view"/>
			<fr:property name="param(view)" value="OID/announcementProcessOid"/>
			<fr:property name="order(view)" value="1"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="announcementProcesses">
	<p><em><bean:message key="process.label.searchResultEmpty" bundle="EXPENDITURE_RESOURCES"/></em></p>
</logic:empty>