/**
 * 
 */
package com.pinpad.ejb.exceptions;

import java.util.Locale;

import javax.ejb.ApplicationException;
import javax.validation.constraints.NotNull;

import com.pinpad.ejb.util.MensajesUtil;

/**
 * @author H P
 *
 */
@ApplicationException(rollback = false)
public class BOException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final Locale localeDefault = new Locale("es", "EC");
	private String codeMessage;
	private Object[] messageParametersValues;
	private Object data;

	public BOException() {
		super();
	}

	public BOException(String codeMessage, Throwable cause) {
		super(MensajesUtil.getMensaje(codeMessage, localeDefault), cause);
		this.codeMessage = codeMessage;
	}

	public BOException(String codeMessage) {
		super(MensajesUtil.getMensaje(codeMessage, localeDefault));
		this.codeMessage = codeMessage;
	}

	public BOException(Throwable cause) {
		super(cause);
	}

	public BOException(String codeMessage, @NotNull Object data) {
		super(MensajesUtil.getMensaje(codeMessage, localeDefault));
		this.codeMessage = codeMessage;
		this.data = data;
	}

	public BOException(String codeMessage, @NotNull Object[] messageParametersValues, Throwable cause) {
		super(MensajesUtil.getMensaje(codeMessage, messageParametersValues, localeDefault), cause);
		this.codeMessage = codeMessage;
		this.messageParametersValues = messageParametersValues;
	}

	public BOException(String codeMessage, @NotNull Object[] messageParametersValues) {
		super(MensajesUtil.getMensaje(codeMessage, messageParametersValues, localeDefault));
		this.codeMessage = codeMessage;
		this.messageParametersValues = messageParametersValues;
	}

	public BOException(String codeMessage, @NotNull Object[] messageParametersValues, @NotNull Object data) {
		super(MensajesUtil.getMensaje(codeMessage, messageParametersValues, localeDefault));
		this.codeMessage = codeMessage;
		this.messageParametersValues = messageParametersValues;
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getTranslatedMessage(String strLanguage) {
		Locale locale = MensajesUtil.validateSupportedLocale(strLanguage);
		if (localeDefault.equals(locale)) {
			return super.getMessage();
		} else {
			if (messageParametersValues != null && messageParametersValues.length > 0)
				return MensajesUtil.getMensaje(codeMessage, messageParametersValues, locale);
			else
				return MensajesUtil.getMensaje(codeMessage, locale);
		}
	}

}