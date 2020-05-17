package ourplugin.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import freemarker.template.TemplateException;
import ourplugin.generator.fmmodel.FMClass;
import ourplugin.generator.fmmodel.FMEnumeration;
import ourplugin.generator.fmmodel.FMModel;
import ourplugin.generator.options.GeneratorOptions;
import ourplugin.generator.options.ProjectOptions;
import ourplugin.generator.options.TypeMapping;

/**
 * EJB generator that now generates incomplete ejb classes based on MagicDraw
 * class model
 * 
 * @ToDo: enhance resources/templates/ejbclass.ftl template and intermediate
 *        data structure (@see myplugin.generator.fmmodel) in order to generate
 *        complete ejb classes
 */

public class SqlDataGenerator extends BasicGenerator {

	public SqlDataGenerator(GeneratorOptions generatorOptions) {
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

			out = getWriter("", "resources");
			if (out != null) {					
				context.clear();
				
				List<String> javaTypes = new ArrayList<String>();
				List<TypeMapping> typeMappings = ProjectOptions.getProjectOptions().getTypeMappings();
				for(TypeMapping type: typeMappings) {
					javaTypes.add(type.getDestType());
				}
				
				List<String> enumerationTypes = new ArrayList<String>();
				Map<String, String> enumerationValues = new HashMap<String, String>();
				List<FMEnumeration> enumerations = FMModel.getInstance().getEnumerations();
				for(FMEnumeration enumVal: enumerations) {
					enumerationTypes.add(enumVal.getName());
					enumerationValues.put(enumVal.getName(), enumVal.getValueAt(0));
				}
				
				context.put("javaTypes", javaTypes);
				context.put("enumTypes", enumerationTypes);
				context.put("enumValues", enumerationValues);
				context.put("entities", FMModel.getInstance().getClasses());
				
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
