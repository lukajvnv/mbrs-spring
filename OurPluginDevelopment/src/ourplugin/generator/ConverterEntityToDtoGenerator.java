package ourplugin.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import freemarker.template.TemplateException;
import ourplugin.generator.fmmodel.FMClass;
import ourplugin.generator.fmmodel.FMModel;
import ourplugin.generator.options.GeneratorOptions;

public class ConverterEntityToDtoGenerator extends BasicGenerator {

	public ConverterEntityToDtoGenerator(GeneratorOptions generatorOptions) {
		super(generatorOptions);
		// TODO Auto-generated constructor stub
	}
	
	public void generate() {
		
		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		List<FMClass> classes = FMModel.getInstance().getClasses();
		
		for (int i = 0; i < classes.size(); i++) { 
			FMClass cl = classes.get(i);
			Writer out;
			Map<String, Object> context = new HashMap<String, Object>();
		
			
			try {
				String modelPackage = cl.getTypePackage();
				String servicePackage = replacePackageFragment(modelPackage, "model", "service");
				String dtoPackage = replacePackageFragment(modelPackage, "model", "dto");
				String repositoryPackage = replacePackageFragment(modelPackage, "model", "repository");
				String converterPackage = replacePackageFragment(modelPackage, "model", "converter");
				
				out = getWriter(cl.getName(), converterPackage);
				if (out != null) {
					
					context.clear();
					context.put("class", cl);
					context.put("model_package", modelPackage);
					context.put("class_package", converterPackage);
					context.put("repository_package", repositoryPackage);
					context.put("service_package", servicePackage);
					context.put("dto_package", dtoPackage);
					context.put("converter_package", converterPackage);	
					context.put("properties", cl.getProperties());
					context.put("importedPackages", cl.getImportedPackages());
					context.put("entities", FMModel.getInstance().getClasses());
					getTemplate().process(context, out);
					out.flush();
				}
					
			} catch (TemplateException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		
		}
	}

}
