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
import ourplugin.generator.fmmodel.FMProperty;
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

public class ControllerAbstractGenerator extends BasicGenerator {

	public ControllerAbstractGenerator(GeneratorOptions generatorOptions) {
		super(generatorOptions);
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
				String controllerPackage = replacePackageFragment(modelPackage, "model", "controller");
				String servicePackage = replacePackageFragment(modelPackage, "model", "service");
				String dtoPackage = replacePackageFragment(modelPackage, "model", "dto");
				
				out = getWriter(cl.getName(), controllerPackage);
				if (out != null) {
					
					context.clear();
					
					List<String> javaTypes = new ArrayList<String>();
					List<TypeMapping> typeMappings = ProjectOptions.getProjectOptions().getTypeMappings();
					for(TypeMapping type: typeMappings) {
						javaTypes.add(type.getDestType());
					}
					
					List<String> enumerationTypes = new ArrayList<String>();
					List<FMEnumeration> enumerations = FMModel.getInstance().getEnumerations();
					for(FMEnumeration enumVal: enumerations) {
						enumerationTypes.add(enumVal.getName());
					}
					
					// find entity relations in properties
					Map<String, FMProperty> entity_relations = new HashMap<String, FMProperty>();
					for(FMProperty p: cl.getProperties()) {
						if(!javaTypes.contains(p.getType().getName()) && !enumerationTypes.contains(p.getType().getName())) {
							entity_relations.put(p.getType().getName(), p);
						}
					}
					
					context.put("class", cl);
					context.put("class_package", controllerPackage);
					context.put("service_package", servicePackage);
					context.put("dto_package", dtoPackage);
					
					context.put("properties", cl.getProperties());
					context.put("entity_properties", entity_relations);
					context.put("importedPackages", cl.getImportedPackages());
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
