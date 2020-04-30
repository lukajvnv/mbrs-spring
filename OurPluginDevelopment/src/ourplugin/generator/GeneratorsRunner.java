package ourplugin.generator;

import java.io.IOException;
import java.util.Map;

import ourplugin.generator.options.GeneratorOptions;
import ourplugin.generator.options.ProjectOptions;
import ourplugin.generator.util.GeneratorFactory;

public class GeneratorsRunner {

	public void generate() throws IOException {
		Map<String, GeneratorOptions> map = ProjectOptions.getProjectOptions().getGeneratorOptions();
	
		for(GeneratorOptions option : map.values()) {
			BasicGenerator generator = GeneratorFactory.getGenerator(option);
			generator.generate();
		}
	}
}
