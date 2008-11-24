package pt.ist.expenditureTrackingSystem.domain;

import javax.activation.MimetypesFileTypeMap;

import pt.ist.expenditureTrackingSystem.domain.util.ByteArray;

public class File extends File_Base {

    public static final String EXTENSION_PDF = "pdf";

    public File() {
	super();
	setOjbConcreteClass(getClass().getName());
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    protected String guessContentType(final String filename) {
	return new MimetypesFileTypeMap().getContentType(filename);
    }

    @Override
    public void setFilename(final String filename) {
	super.setFilename(filename);
	setContentType(guessContentType(filename));
    }

    public void setContent(final byte[] bytes) {
	final ByteArray byteArray = new ByteArray(bytes);
	setContent(byteArray);
    }

}
