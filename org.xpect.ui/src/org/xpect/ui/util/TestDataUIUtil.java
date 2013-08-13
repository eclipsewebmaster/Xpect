package org.xpect.ui.util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestSuiteElement;

/**
 * 
 * @author Moritz Eysholdt
 */
public class TestDataUIUtil {
	public static class TestElementInfo {
		private String clazz;
		private String title;
		private IFile file;
		private URI uri;
		private IJavaProject javaProject;

		public IJavaProject getJavaProject() {
			return javaProject;
		}

		public String getTitle() {
			return title;
		}

		public String getClazz() {
			return clazz;
		}

		public IFile getFile() {
			return file;
		}

		public String getMethod() {
			if (title == null)
				return null;
			int i = 0;
			while (i < title.length() && Character.isJavaIdentifierPart(title.charAt(i)))
				i++;
			return title.substring(0, i);
		}

		public URI getURI() {
			return uri;
		}
	}

	protected static IFile findFile(ITestElement ele, String filename) {
		IProject project = ele.getTestRunSession().getLaunchedProject().getProject();
		IResource resource = project.findMember(filename);
		if (resource == null || !resource.exists())
			throw new IllegalStateException("File " + resource + " does not exist.");
		if (!(resource instanceof IFile))
			throw new IllegalStateException(resource + " is not a file, but a " + resource.getClass().getSimpleName());
		return (IFile) resource;
	}

	public static TestElementInfo parse(ITestElement element) {
		TestElementInfo result = new TestElementInfo();
		result.javaProject = element.getTestRunSession().getLaunchedProject();
		String project = result.javaProject.getProject().getName();
		if (element instanceof ITestCaseElement) {
			ITestCaseElement tce = (ITestCaseElement) element;
			String name = tce.getTestClassName();
			if (name.contains(":")) {
				int colon = name.indexOf(':');
				result.clazz = name.substring(0, colon).trim();
				URI uri = URI.createURI(name.substring(colon + 1).trim());
				URI base = URI.createPlatformResourceURI(project + "/", true);
				result.uri = uri.resolve(base);
				result.file = findFile(element, uri.trimFragment().toString());
			} else {
				result.clazz = name;
			}
			result.title = tce.getTestMethodName();
		} else if (element instanceof ITestSuiteElement) {
			ITestSuiteElement tse = (ITestSuiteElement) element;
			String name = tse.getSuiteTypeName();
			if (name.contains(":")) {
				int colon = name.indexOf(':');
				String filename = name.substring(0, colon).trim();
				String path = name.substring(colon + 1).trim();
				result.uri = URI.createPlatformResourceURI(project + "/" + path + "/" + filename, true);
				result.file = findFile(element, path + "/" + filename);
			} else {
				result.clazz = name;
			}
		}
		return result;
	}
}
