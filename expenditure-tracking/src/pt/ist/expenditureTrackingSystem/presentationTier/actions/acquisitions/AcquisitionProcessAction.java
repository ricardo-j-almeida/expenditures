package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProposalDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Invoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.SearchAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.expenditureTrackingSystem.presentationTier.actions.BaseAction;
import pt.ist.expenditureTrackingSystem.presentationTier.util.FileUploadBean;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/acquisitionProcess")
@Forwards( { @Forward(name = "edit.request.acquisition", path = "/acquisitions/editAcquisitionRequest.jsp"),
	@Forward(name = "view.acquisition.process", path = "/acquisitions/viewAcquisitionProcess.jsp"),
	@Forward(name = "search.acquisition.process", path = "/acquisitions/searchAcquisitionProcess.jsp"),
	@Forward(name = "add.acquisition.proposal.document", path = "/acquisitions/addAcquisitionProposalDocument.jsp"),
	@Forward(name = "view.acquisition.request.item", path = "/acquisitions/viewAcquisitionRequestItem.jsp"),
	@Forward(name = "edit.acquisition.request.item", path = "/acquisitions/editAcquisitionRequestItem.jsp"),
	@Forward(name = "allocate.funds", path = "/acquisitions/allocateFunds.jsp"),
	@Forward(name = "allocate.funds.to.service.provider", path = "/acquisitions/allocateFundsToServiceProvider.jsp"),
	@Forward(name = "prepare.create.acquisition.request", path = "/acquisitions/createAcquisitionRequest.jsp"),
	@Forward(name = "receive.invoice", path = "/acquisitions/receiveInvoice.jsp"),
	@Forward(name = "view.active.processes", path = "/acquisitions/viewActiveProcesses.jsp") })
public class AcquisitionProcessAction extends BaseAction {

    private static final Context CONTEXT = new Context("acquisitions");

    @Override
    protected Context getContextModule() {
	return CONTEXT;
    }

    public static class AcquisitionProposalDocumentForm extends FileUploadBean {
    }

    public static class ReceiveInvoiceForm extends FileUploadBean {
	private String invoiceNumber;
	private DateTime invoiceDate;

	public String getInvoiceNumber() {
	    return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
	    this.invoiceNumber = invoiceNumber;
	}

	public DateTime getInvoiceDate() {
	    return invoiceDate;
	}

	public void setInvoiceDate(DateTime invoiceDate) {
	    this.invoiceDate = invoiceDate;
	}
    }

    public final ActionForward createNewAcquisitionProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = AcquisitionProcess.createNewAcquisitionProcess();
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	return editAcquisitionRequest(mapping, request, acquisitionProcess);
    }

    public final ActionForward editAcquisitionRequest(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	return editAcquisitionRequest(mapping, request, acquisitionProcess);
    }

    protected final ActionForward editAcquisitionRequest(final ActionMapping mapping, final HttpServletRequest request,
	    final AcquisitionProcess acquisitionProcess) {
	final AcquisitionRequest acquisitionRequest = acquisitionProcess.getAcquisitionRequest();
	request.setAttribute("acquisitionRequest", acquisitionRequest);
	return mapping.findForward("edit.request.acquisition");
    }

    public final ActionForward viewAcquisitionProcess(final ActionMapping mapping, final HttpServletRequest request,
	    final AcquisitionProcess acquisitionProcess) {
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	return mapping.findForward("view.acquisition.process");
    }

    public final ActionForward viewAcquisitionProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	return viewAcquisitionProcess(mapping, request, acquisitionProcess);
    }

    public final ActionForward deleteAcquisitionProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	acquisitionProcess.delete();
	return searchAcquisitionProcess(mapping, form, request, response);
    }

    public final ActionForward searchAcquisitionProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	SearchAcquisitionProcess searchAcquisitionProcess = getRenderedObject();
	if (searchAcquisitionProcess == null) {
	    searchAcquisitionProcess = new SearchAcquisitionProcess();
	    final User user = UserView.getUser();
	    if (user != null) {
		searchAcquisitionProcess.setRequester(user.getUsername());
	    }
	    searchAcquisitionProcess.setAcquisitionProcessState(AcquisitionProcessStateType.IN_GENESIS);
	}
	request.setAttribute("searchAcquisitionProcess", searchAcquisitionProcess);
	return mapping.findForward("search.acquisition.process");
    }

    public final ActionForward prepareAddAcquisitionProposalDocument(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	final AcquisitionProposalDocumentForm acquisitionProposalDocumentForm = new AcquisitionProposalDocumentForm();
	request.setAttribute("acquisitionProposalDocumentForm", acquisitionProposalDocumentForm);
	return mapping.findForward("add.acquisition.proposal.document");
    }

    public final ActionForward addAcquisitionProposalDocument(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	final AcquisitionProposalDocumentForm acquisitionProposalDocumentForm = getRenderedObject();
	final String filename = acquisitionProposalDocumentForm.getFilename();
	final byte[] bytes = consumeInputStream(acquisitionProposalDocumentForm);
	acquisitionProcess.addAcquisitionProposalDocument(filename, bytes);
	return viewAcquisitionProcess(mapping, request, acquisitionProcess);
    }

    public final ActionForward downloadAcquisitionProposalDocument(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws IOException {
	final AcquisitionProposalDocument acquisitionProposalDocument = getDomainObject(request, "acquisitionProposalDocumentOid");
	return download(response, acquisitionProposalDocument);
    }

    public final ActionForward downloadAcquisitionRequestDocument(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws IOException {
	final AcquisitionRequestDocument acquisitionRequestDocument = getDomainObject(request, "acquisitionRequestDocumentOid");
	return download(response, acquisitionRequestDocument);
    }

    public final ActionForward createNewAcquisitionRequestItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	final AcquisitionRequestItem acquisitionRequestItem = acquisitionProcess.createAcquisitionRequestItem();
	return editAcquisitionRequestItem(mapping, request, acquisitionRequestItem);
    }

    protected final ActionForward editAcquisitionRequestItem(final ActionMapping mapping, final HttpServletRequest request,
	    final AcquisitionRequestItem acquisitionRequestItem) {
	final AcquisitionProcess acquisitionProcess = acquisitionRequestItem.getAcquisitionRequest().getAcquisitionProcess();
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	request.setAttribute("acquisitionRequestItem", acquisitionRequestItem);
	return mapping.findForward("edit.acquisition.request.item");
    }

    public final ActionForward editAcquisitionRequestItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionRequestItem acquisitionRequestItem = getDomainObject(request, "acquisitionRequestItemOid");
	return editAcquisitionRequestItem(mapping, request, acquisitionRequestItem);
    }

    public final ActionForward viewAcquisitionRequestItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionRequestItem acquisitionRequestItem = getDomainObject(request, "acquisitionRequestItemOid");
	request.setAttribute("acquisitionRequestItem", acquisitionRequestItem);
	return mapping.findForward("view.acquisition.request.item");
    }

    public final ActionForward deleteAcquisitionRequestItem(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionRequestItem acquisitionRequestItem = getDomainObject(request, "acquisitionRequestItemOid");
	final AcquisitionProcess acquisitionProcess = acquisitionRequestItem.getAcquisitionRequest().getAcquisitionProcess();
	acquisitionRequestItem.delete();
	return viewAcquisitionProcess(mapping, request, acquisitionProcess);
    }

    public final ActionForward submitForApproval(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	acquisitionProcess.submitForApproval();
	return viewAcquisitionProcess(mapping, request, acquisitionProcess);
    }

    public final ActionForward approve(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	acquisitionProcess.approve();
	return viewAcquisitionProcess(mapping, request, acquisitionProcess);
    }

    public final ActionForward allocateFunds(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	return mapping.findForward("allocate.funds");
    }

    public final ActionForward allocateFundsToServiceProvider(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	return mapping.findForward("allocate.funds.to.service.provider");
    }

    public final ActionForward prepareCreateAcquisitionRequest(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	return mapping.findForward("prepare.create.acquisition.request");
    }

    public final ActionForward showPendingProcesses(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	List<AcquisitionProcess> processes = new ArrayList<AcquisitionProcess>();

	for (AcquisitionProcess process : ExpenditureTrackingSystem.getInstance().getAcquisitionProcesses()) {
	    if (process.isPersonAbleToExecuteActivities()) {
		processes.add(process);
	    }
	}
	request.setAttribute("activeProcesses", processes);

	return mapping.findForward("view.active.processes");
    }

    public ActionForward createAcquisitionRequestDocument(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");

	AcquisitionRequestDocument acquisitionRequestDocument = acquisitionProcess.createAcquisitionRequest();
	byte[] data = acquisitionRequestDocument.getContent().getBytes();

	response.setContentLength(data.length);
	response.setContentType("application/pdf");
	response.addHeader("Content-Disposition", "attachment; filename=" + acquisitionRequestDocument.getFilename());

	final ServletOutputStream writer = response.getOutputStream();
	writer.write(data);
	writer.flush();
	writer.close();

	response.flushBuffer();

	ActionForward findForward = mapping.findForward("prepare.create.acquisition.request");
	findForward.setRedirect(true);
	return findForward;
    }

    public final ActionForward receiveInvoice(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	ReceiveInvoiceForm receiveInvoiceForm = getRenderedObject();
	if (receiveInvoiceForm == null) {
	    receiveInvoiceForm = new ReceiveInvoiceForm();
	    final AcquisitionRequest acquisitionRequest = acquisitionProcess.getAcquisitionRequest();
	    if (acquisitionRequest.hasInvoice()) {
		final Invoice invoice = acquisitionRequest.getInvoice();
		receiveInvoiceForm.setInvoiceNumber(invoice.getInvoiceNumber());
		receiveInvoiceForm.setInvoiceDate(invoice.getInvoiceDate());
	    }
	}
	request.setAttribute("receiveInvoiceForm", receiveInvoiceForm);
	return mapping.findForward("receive.invoice");
    }

    public final ActionForward saveInvoice(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	request.setAttribute("acquisitionProcess", acquisitionProcess);
	final ReceiveInvoiceForm receiveInvoiceForm = getRenderedObject();
	final byte[] bytes = consumeInputStream(receiveInvoiceForm);
	acquisitionProcess.receiveInvoice(receiveInvoiceForm.getFilename(), bytes, receiveInvoiceForm.getInvoiceNumber(),
		receiveInvoiceForm.getInvoiceDate());
	return viewAcquisitionProcess(mapping, request, acquisitionProcess);
    }

    public final ActionForward downloadInvoice(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws IOException {
	final Invoice invoice = getDomainObject(request, "invoiceOid");
	return download(response, invoice);
    }

    public final ActionForward confirmInvoice(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	acquisitionProcess.confirmInvoice();
	return viewAcquisitionProcess(mapping, request, acquisitionProcess);
    }
    
    
    public final ActionForward payAcquisition(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	acquisitionProcess.payAcquisition();
	return viewAcquisitionProcess(mapping, request, acquisitionProcess);
    }
    
    public final ActionForward alocateFundsPermanently(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	
	final AcquisitionProcess acquisitionProcess = getDomainObject(request, "acquisitionProcessOid");
	acquisitionProcess.alocateFundsPermanently();
	return viewAcquisitionProcess(mapping, request, acquisitionProcess);
    }

}
