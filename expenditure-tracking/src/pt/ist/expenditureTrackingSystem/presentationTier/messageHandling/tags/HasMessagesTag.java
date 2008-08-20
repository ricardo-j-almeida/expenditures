package pt.ist.expenditureTrackingSystem.presentationTier.messageHandling.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import pt.ist.expenditureTrackingSystem.presentationTier.messageHandling.MessageHandler;
import pt.ist.expenditureTrackingSystem.presentationTier.messageHandling.MessageHandler.MessageType;

public class HasMessagesTag extends TagSupport {

    private String type = null;

    @Override
    public int doStartTag() throws JspException {
	MessageHandler handler = (MessageHandler) pageContext.getRequest().getAttribute((MessageHandler.MESSAGE_HANDLER_NAME));

	if (handler == null || !handler.hasMessages(getMessageType())) {
	    return SKIP_BODY;
	}

	return EVAL_BODY_INCLUDE;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getType() {
	return type;
    }

    private MessageType getMessageType() {
	return getType() != null ? MessageType.valueOf(getType()) : null;
    }

}
