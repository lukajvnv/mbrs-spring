package ourplugin.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import freemarker.template.TemplateException;
import ourplugin.generator.fmmodel.FMClass;
import ourplugin.generator.fmmodel.FMModel;
import ourplugin.generator.options.GeneratorOptions;

/**
 * EJB generator that now generates incomplete ejb classes based on MagicDraw
 * class model
 * 
 * @ToDo: enhance resources/templates/ejbclass.ftl template and intermediate
 *        data structure (@see myplugin.generator.fmmodel) in order to generate
 *        complete ejb classes
 */

public class MainGenerator extends BasicGenerator {

	public MainGenerator(GeneratorOptions generatorOptions) {
		super(generatorOptions);
	}

	public void generate() {

		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

		Writer out;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			FMClass cl = FMModel.getInstance().getClasses().get(0);

			String modelPackage = cl.getTypePackage();
			String repositoryPackage = modelPackage.replace("model", "repository");
			String mainPackage = modelPackage.replace(".model", "");

			String appName = "Our";
			
			out = getWriter(appName, mainPackage);
			if (out != null) {					
				context.clear();
				
				String repositoryClass = "BaseRepositoryImpl";
				context.put("app_name", appName);
				context.put("repository_class", repositoryClass);
				context.put("base_package", mainPackage);
				context.put("repository_package", repositoryPackage);
				getTemplate().process(context, out);
				out.flush();
			}
		} catch (TemplateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
	}

}
