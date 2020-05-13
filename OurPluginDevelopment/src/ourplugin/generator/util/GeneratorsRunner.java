package ourplugin.generator.util;

import java.io.IOException;
import java.util.Map;

import ourplugin.generator.BasicGenerator;
import ourplugin.generator.options.GeneratorOptions;
import ourplugin.generator.options.ProjectOptions;

public class GeneratorsRunner {

	public void generate() throws IOException {
		Map<String, GeneratorOptions> map = ProjectOptions.getProjectOptions().getGeneratorOptions();
	
		for(GeneratorOptions option : map.values()) {
			BasicGenerator generator = GeneratorFactory.getGenerator(option);
			generator.generate();
		}
	}
}
