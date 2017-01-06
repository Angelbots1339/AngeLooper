package angelooper.wizards.angelooper;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class AngeLooperWizardPage extends WizardPage {

	private Text nameText;
	
	public AngeLooperWizardPage() {
		super("wizardPage");
		setTitle("AngeLooper");
		setDescription("This wizard creates a new project with the AngeLooper format.");
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		
		Label label = new Label(container, SWT.NULL);
		label.setText("&Name:");

		nameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		nameText.setLayoutData(gd);
		nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		dialogChanged();
		setControl(container);
	}
	
	private void dialogChanged() {
		String name = getProjectName();
		if (name.length() == 0) {
			updateStatus("File name must be specified");
			return;
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getProjectName() {
		return nameText.getText();
	}
}