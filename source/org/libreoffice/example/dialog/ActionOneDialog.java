package org.libreoffice.example.dialog;

import java.awt.Dialog;

import org.libreoffice.example.helper.DialogHelper;
import org.libreoffice.example.helper.FileHelper;

import com.sun.star.awt.XDialog;
import com.sun.star.awt.XDialogEventHandler;
import com.sun.star.awt.XFixedText;
import com.sun.star.awt.XTextComponent;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XText;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.XComponentContext;


public class ActionOneDialog implements XDialogEventHandler {
	
	private XDialog dialog;
	private static final String insertText = "insertText";
	private static final String close = "close";
	private static final String textModified = "textModified";
	private String[] supportedActions = new String[] { insertText, close };
	private XComponentContext xContext;
	
	public ActionOneDialog(XComponentContext xContext) {
		this.dialog = DialogHelper.createDialog("ActionOneDialog.xdl", xContext, this);
		this.xContext = xContext;
	}

	public void show() {
		dialog.execute();
	}
	
	private void onOkButtonPressed() {
		dialog.endExecute();
	}
	
	@Override
	public boolean callHandlerMethod(XDialog dialog, Object eventObject, String methodName) throws WrappedTargetException {
		if (methodName.equals(insertText)) {
			XTextComponent textComponentField = DialogHelper.getEditField(dialog,"TextField1");
			XTextDocument textDocument = FileHelper.getCurrentDocument(xContext);
			XText textFromDoc = textDocument.getText();
			textFromDoc.setString(textComponentField.getText());
			return true; // Event was handled
		}else if(methodName.equals(close)){
			onOkButtonPressed();
			return true;
		}else if(methodName.equals(textModified)){
			XTextComponent textComponentField = DialogHelper.getEditField(dialog,"TextField1");
			String[] words = textComponentField.getText().trim().replaceAll(" +", " ").split(" ");
			XFixedText dialogo = DialogHelper.getLabel(dialog, "Label3");
			if(textComponentField.getText().isEmpty()){
				dialogo.setText("0 words");
			}else{
				if(words.length==1){
					dialogo.setText("1 word");
				}else{
					dialogo.setText(words.length+" words");
				}

			}
			return true;
		}
		return false; // Event was not handled
	}

	@Override
	public String[] getSupportedMethodNames() {
		return supportedActions;
	}

}
