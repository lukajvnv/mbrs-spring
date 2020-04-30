package ourplugin.generator.options.xml;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import ourplugin.generator.options.GeneratorOptions;
import ourplugin.generator.options.TypeMapping;

@XStreamAlias("generator_options")
public class GeneratorOptionsRoot {

	@XStreamImplicit(itemFieldName = "environment")
    private List<GeneratorOptionsEnvironment> environments = new ArrayList<GeneratorOptionsEnvironment>();

	public GeneratorOptionsRoot() {
		
	}

	public List<GeneratorOptionsEnvironment> getEnvironments() {
		return environments;
	}

	public void setEnvironments(List<GeneratorOptionsEnvironment> environments) {
		this.environments = environments;
	}
	
	

	
	
}
