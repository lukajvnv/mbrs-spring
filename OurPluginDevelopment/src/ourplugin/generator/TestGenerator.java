package ourplugin.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import freemarker.template.TemplateException;
import ourplugin.generator.fmmodel.FMClass;
import ourplugin.generator.fmmodel.FMEnumeration;
import ourplugin.generator.fmmodel.FMModel;
import ourplugin.generator.fmmodel.FMProperty;
import ourplugin.generator.fmmodel.FMType;
import ourplugin.generator.options.GeneratorOptions;
import ourplugin.generator.options.ProjectOptions;
import ourplugin.generator.options.TypeMapping;

public class TestGenerator extends BasicGenerator {

	public TestGenerator(GeneratorOptions generatorOptions) {
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
				
				out = getWriter(cl.getName(), servicePackage);
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
					
					//context.put("javaTypes", javaTypes);
					//context.put("enumTypes", enumerationTypes);
					//context.put("enumValues", enumerationValues);
					
					context.put("class", cl);
					context.put("model_package", modelPackage);
					context.put("service_package", servicePackage);
					context.put("dto_package", dtoPackage);
					context.put("repository_package", repositoryPackage);
					context.put("converter_package", converterPackage);
										
					context.put("static_imports", initStaticImports());
					context.put("other_imports", initImports());
					
					// it be used only simple data type properties
					List<FMProperty> testingProperties = new ArrayList<FMProperty>();
					for(FMProperty property: cl.getProperties()) {
						if(javaTypes.contains(property.getType().getName())) {
							testingProperties.add(property);
						}						
					}

					context.put("properties", testingProperties);
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
	
	private static List<FMType> initStaticImports() {
		List<FMType> imports = new ArrayList<FMType>();
		
		String mockitoPackage = "org.mockito.Mockito";
		
		imports.add(new FMType("assertThat", "org.assertj.core.api.Assertions"));
		imports.add(new FMType("doNothing", mockitoPackage));
		imports.add(new FMType("times", mockitoPackage));
		imports.add(new FMType("verify", mockitoPackage));
		imports.add(new FMType("verifyNoMoreInteractions", mockitoPackage));
		imports.add(new FMType("when", mockitoPackage));
		
		return imports;
	}
	
	
	private static List<FMType> initImports() {
		List<FMType> imports = new ArrayList<FMType>();
		
		String mockitoPackage = "org.mockito";
		String utilPackage = "java.util";
		
		imports.add(new FMType("List", utilPackage));
		imports.add(new FMType("ArrayList", utilPackage));
		imports.add(new FMType("Arrays", utilPackage));
		imports.add(new FMType("Calendar", utilPackage));
		imports.add(new FMType("Date", utilPackage));
		imports.add(new FMType("Optional", utilPackage));

		imports.add(new FMType("Mock", mockitoPackage));
		imports.add(new FMType("InjectMocks", mockitoPackage));
		imports.add(new FMType("Test", "org.junit.jupiter.api"));
		imports.add(new FMType("RunWith", "org.junit.runner"));
		imports.add(new FMType("SpringBootTest", "org.springframework.boot.test.context"));
		imports.add(new FMType("SpringRunner", "org.springframework.test.context.junit4"));
		imports.add(new FMType("Transactional", "org.springframework.transaction.annotation"));
		
		return imports;
	}

}
