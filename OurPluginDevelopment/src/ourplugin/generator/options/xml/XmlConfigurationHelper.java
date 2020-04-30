/**
 * 
 */
package ourplugin.generator.options.xml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import ourplugin.generator.options.GeneratorOptions;
import ourplugin.generator.options.ProjectOptions;
import ourplugin.generator.options.TypeMapping;

/**
 * Util class for loading and saving configuration from xml file and 
 * setting to ProjectOptions @see ourplugin.generator.options.ProjectOptions 
 */
public class XmlConfigurationHelper {
	
	public enum Environment {
		PLUGIN, TESTING
	}

	private String projectOptionsFilePath;
	private String templatesDir;
	
	private String environmentName;
	
	public XmlConfigurationHelper() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public XmlConfigurationHelper(String filePath, String environment) {
		super();
		this.projectOptionsFilePath = filePath;
		try {
			Environment.valueOf(environment);
			this.environmentName = environment;
		} catch (IllegalArgumentException e) {
			throw new NoEnvironmentException(String.format("Environment under the name %s does not exist", environment));
		}
	}

	public GeneratorOptionsRoot loadFromXml() throws FileNotFoundException {
		FileReader fileReader = new FileReader(projectOptionsFilePath);   
		XStream xstream = new XStream(new DomDriver());    
		xstream.processAnnotations(GeneratorOptions.class);    
		xstream.processAnnotations(GeneratorOptionsRoot.class); 
		xstream.processAnnotations(TypeMapping.class);
		xstream.processAnnotations(GeneratorOptionsEnvironment.class);
		 
		GeneratorOptionsRoot loadedConfig = (GeneratorOptionsRoot) xstream.fromXML(fileReader);
		return loadedConfig;
	}
	
	public void saveToXml() {
		//String xml = xstream.toXML(loadedConfig);
	}
	
	public void setProjectOptions(GeneratorOptionsRoot loadedConfig) {
		GeneratorOptionsEnvironment environment = getEnvironment(loadedConfig);
		if(environment == null) {
			
		}
		
		List<TypeMapping> typeMappings = environment.getTypeMappings();
		List<GeneratorOptions> generatorOptionsList = environment.getOptions();
		
		Map<String, GeneratorOptions> generatorOptionsMap = fromListToMap(environment.getEnvName(), generatorOptionsList);
		
		System.out.println();
		ProjectOptions.getProjectOptions().setTypeMappings(typeMappings);
		ProjectOptions.getProjectOptions().setGeneratorOptions(generatorOptionsMap);
	}
	
	private GeneratorOptionsEnvironment getEnvironment(GeneratorOptionsRoot loadedConfig) {		
		List<GeneratorOptionsEnvironment> environments = loadedConfig.getEnvironments();
		for(GeneratorOptionsEnvironment env: environments) {
			if(env.getEnvName().equals(environmentName)) {
				return env;
			}
		}
		
		return null;
	}
	
	private Map<String, GeneratorOptions> fromListToMap(String envName, List<GeneratorOptions> generatorOptionsList){
		Map<String, GeneratorOptions> generatorOptionsMap = new HashMap<String, GeneratorOptions>();
		for(GeneratorOptions option : generatorOptionsList) {
			if(envName.equals(Environment.PLUGIN.toString())) {
				option.setTemplateDir(templatesDir);
			}
			generatorOptionsMap.put(option.getGeneratorName(), option);
		}
		return generatorOptionsMap;
	}

	public String getProjectOptionsFilePath() {
		return projectOptionsFilePath;
	}

	public void setFilePath(String filePath) {
		this.projectOptionsFilePath = filePath;
	}

	public String getEnvironment() {
		return environmentName;
	}

	public void setEnvironment(String environment) {
		this.environmentName = environment;
	}

	public String getTemplatesDir() {
		return templatesDir;
	}

	public void setTemplatesDir(String templateDirs) {
		this.templatesDir = templateDirs;
	}
}
