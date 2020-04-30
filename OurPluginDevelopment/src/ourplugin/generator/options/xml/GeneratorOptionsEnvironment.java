package ourplugin.generator.options.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import ourplugin.generator.options.GeneratorOptions;
import ourplugin.generator.options.TypeMapping;

@XStreamAlias("environment")
public class GeneratorOptionsEnvironment {
	
	@XStreamAsAttribute
	private String envName;

	@XStreamImplicit(itemFieldName = "generator_option")
    private List<GeneratorOptions> options = new ArrayList<GeneratorOptions>();

	@XStreamImplicit(itemFieldName = "type_mapping")
	private List<TypeMapping> typeMappings = new ArrayList<TypeMapping>();

	public GeneratorOptionsEnvironment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<GeneratorOptions> getOptions() {
		return options;
	}

	public void setOptions(List<GeneratorOptions> options) {
		this.options = options;
	}

	public List<TypeMapping> getTypeMappings() {
		return typeMappings;
	}

	public void setTypeMappings(List<TypeMapping> typeMappings) {
		this.typeMappings = typeMappings;
	}

	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	
}
