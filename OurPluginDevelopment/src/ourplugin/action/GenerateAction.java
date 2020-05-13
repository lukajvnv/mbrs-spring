package ourplugin.action;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import ourplugin.analyzer.AnalyzeException;
import ourplugin.analyzer.ModelAnalyzer;
import ourplugin.generator.EJBGenerator;
import ourplugin.generator.fmmodel.FMModel;
import ourplugin.generator.options.GeneratorOptions;
import ourplugin.generator.options.ProjectOptions;
import ourplugin.generator.util.GeneratorsRunner;

/** Action that activate code generation */
@SuppressWarnings("serial")
public class GenerateAction extends MDAction{
	
	private String filePackage = "generated_app";
	
	public GenerateAction(String name) {			
		super("", name, null, null);		
	}

	public void actionPerformed(ActionEvent evt) {
		
		if (Application.getInstance().getProject() == null) return;
		Package root = Application.getInstance().getProject().getModel();
		
		if (root == null) return;
	
		ModelAnalyzer analyzer = new ModelAnalyzer(root, filePackage);	
		
		try {
			analyzer.prepareModel();
			
			// running particular generator 
//			GeneratorOptions go = ProjectOptions.getProjectOptions().getGeneratorOptions().get("EJBGenerator");			
//			EJBGenerator generator = new EJBGenerator(go);
//			generator.generate();
			
			GeneratorsRunner runner = new GeneratorsRunner();
			runner.generate();
			
			JOptionPane.showMessageDialog(null, "Code is successfully generated!");
			
		} catch (AnalyzeException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}			
	}
}