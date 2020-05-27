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
					
					//creating list of basic properties (java datatypes or enums)
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
					
					List<FMProperty> basic_properties = new ArrayList<FMProperty>();
					for(FMProperty p: cl.getProperties()) {
						if(javaTypes.contains(p.getType().getName()) || enumerationTypes.contains(p.getType().getName())) {
							basic_properties.add(p);
						}
					}
						
					context.put("class", cl);
					context.put("model_package", modelPackage);
					context.put("class_package", converterPackage);
					context.put("repository_package", repositoryPackage);
					context.put("service_package", servicePackage);
					context.put("dto_package", dtoPackage);
					context.put("converter_package", converterPackage);	
					context.put("properties", cl.getProperties());
					context.put("basic_properties", basic_properties);
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
