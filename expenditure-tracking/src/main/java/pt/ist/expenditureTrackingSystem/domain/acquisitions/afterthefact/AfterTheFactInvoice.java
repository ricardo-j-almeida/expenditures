/*
 * @(#)AfterTheFactInvoice.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.fileBeans.AfterTheFactInvoiceBean;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
@ClassNameBundle(bundle = "AcquisitionResources")
public class AfterTheFactInvoice extends AfterTheFactInvoice_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(AfterTheFactInvoice.class, AfterTheFactInvoiceBean.class);
    }

    public AfterTheFactInvoice(String displayName, String filename, byte[] content) {
        super();
        init(displayName, filename, content);
    }

    public AfterTheFactInvoice(final AfterTheFactAcquisitionProcess process) {
        super();
        process.addFiles(this);
    }

    public AfterTheFactInvoice() {
        super();
    }

    @Override
    public void fillInNonDefaultFields(WorkflowFileUploadBean bean) {
        AfterTheFactInvoiceBean invoiceBean = (AfterTheFactInvoiceBean) bean;
        setInvoiceDate(invoiceBean.getInvoiceDate());
        setInvoiceNumber(invoiceBean.getInvoiceNumber());
    }

    @Override
    public void validateUpload(WorkflowProcess workflowProcess) {
        AfterTheFactAcquisitionProcess process = (AfterTheFactAcquisitionProcess) workflowProcess;
        if (process.getInvoice() != null) {
            throw new ProcessFileValidationException("resources/AcquisitionResources",
                    "error.message.cannotHaveMoreThanOneInvoice");
        }
    }

    @Override
    @Atomic
    public void delete() {
        setProcess(null);
        super.delete();
    }

    public void store(String filename, byte[] content) {
        init(getDisplayName(), filename, content);
    }
}
