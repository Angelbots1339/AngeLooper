package angelooper.wizards.angelooper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.osgi.framework.Bundle;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class AngeLooperWizard extends Wizard implements INewWizard {
	private AngeLooperWizardPage page;

	public AngeLooperWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		page = new AngeLooperWizardPage();
		addPage(page);
	}
	
	public boolean performFinish() {
		File dir = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString() + "/" + page.getProjectName());
		if(dir.mkdir()){
			File dest = new File(dir.getAbsolutePath() + "/project.zip");
			
			copyProject(dest);
			extractAll(dest.getAbsolutePath(), dir.getAbsolutePath());
			dest.delete();
			
			importProject(new Path(dir.getAbsolutePath() + "/.project"));
		}
		else{
			MessageDialog.openError(null, "Error", "Error. A project of that name already exists in the workspace.");
		}
		return true;
	}
	
	private void copyProject(File file){
		Bundle b = Platform.getBundle("AngeLooper");
		InputStream in;
		OutputStream out;
		try {
			in = b.getResource("templates/project.zip").openStream();
			out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(buf)) > 0) {
				out.write(buf, 0, bytesRead);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void extractAll(String src, String dest){
		try {
	    	ZipFile zipFile = new ZipFile(src);
	    	zipFile.extractAll(dest);
		} catch (ZipException e) {
			e.printStackTrace();
	    }
	}
	
	private void importProject(Path descrip){
		IProjectDescription description;
		try {
			description = ResourcesPlugin.getWorkspace().loadProjectDescription(descrip);
			description.setName(page.getProjectName());
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
			project.create(description, null);
			project.open(null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {}
}