package ourplugin.generator.util;

import ourplugin.generator.BasicGenerator;
import ourplugin.generator.EJBGenerator;
import ourplugin.generator.EntityGenerator;
import ourplugin.generator.RepositoryGenerator;
import ourplugin.generator.options.GeneratorOptions;

public class GeneratorFactory {
	
	public enum GeneratorType {
		EJBGenerator,
		EntityGenerator, 
		RepositoryGenerator
	}

	public static BasicGenerator getGenerator(GeneratorOptions generatorOptions) {
		GeneratorType generatorType = null;
		try {
			generatorType = GeneratorType.valueOf(generatorOptions.getGeneratorName());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			throw new NoGeneratorException("Could not find requested generator");
		}
		
		switch (generatorType) {
			case EJBGenerator:
				return new EJBGenerator(generatorOptions);
			case EntityGenerator:
				return new EntityGenerator(generatorOptions);
			case RepositoryGenerator:
				return new RepositoryGenerator(generatorOptions);
			default: 
				return new EJBGenerator(generatorOptions);
		}
	}
}
