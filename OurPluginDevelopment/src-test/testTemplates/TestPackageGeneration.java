package testTemplates;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import ourplugin.generator.fmmodel.FMAttributeNameValue;
import ourplugin.generator.fmmodel.FMClass;
import ourplugin.generator.fmmodel.FMModel;
import ourplugin.generator.fmmodel.FMProperty;
import ourplugin.generator.fmmodel.FMType;
import ourplugin.generator.options.GeneratorOptions;
import ourplugin.generator.options.TypeMapping;
import ourplugin.generator.options.xml.GeneratorOptionsEnvironment;
import ourplugin.generator.options.xml.GeneratorOptionsRoot;
import ourplugin.generator.options.xml.XmlConfigurationHelper;
import ourplugin.generator.util.GeneratorsRunner;

/** TestPackageGeneration: Class for package generation testing
 * @ToDo: Create another test class that loads metadata saved by MagicDraw plugin 
 * ( @see myplugin.GenerateAction#exportToXml() ) and activate code generation. 
 * This is the way to perform code generation testing without
 *  need to restart MagicDraw 
 *  */

public class TestPackageGeneration {
	
	public enum TestCase {BASIC_TEST, TEST_FROM_XML}
	
	private static final String projectXmlDirectory = "./resources/ProjectOptions.xml"; 
	
	public TestPackageGeneration(){
		
	}
	
	private void initTestModel() {		
		
		List<FMClass> classes = FMModel.getInstance().getClasses();
		
		classes.clear();
		
		FMClass setType = new FMClass("Set", "generated_app.test.model.util", "public");
		FMClass stringType = new FMClass("String", "java.lang", "public");
		FMClass dateType = new FMClass("Date", "java.util", "public");
		FMType intType = new FMType("Integer", "");
		FMType booleanType = new FMType("boolean", "");
		
		FMClass cl = new FMClass ("Preduzece", "generated_app.test.model.orgsema", "public");
		
		FMProperty p = new FMProperty("sifraPreduzeca", intType, "private", 1, 1);
		p.getIdentityProperties().add(new FMAttributeNameValue("", "@Id", new ArrayList<String>()));
		p.getIdentityProperties().add(new FMAttributeNameValue("strategy", "@GeneratedValue", Arrays.asList(new String[] {"\"GeneratedValue.Identity\""})));
		
		cl.addProperty(p);
		
		p = new FMProperty("nazivPreduzeca", stringType, "private", 1, 1);
		p.setPersistentAnnotationName("@Column");
		p.getIdentityProperties().add(new FMAttributeNameValue("name", "", Arrays.asList(new String[] {"\"imeFirme\""})));
		
		cl.addProperty(p);
		
		cl.getPersistentProperties().add(new FMAttributeNameValue("name", Arrays.asList(new String[] {"\"firma\""})));
		classes.add(cl);
		
		cl = new FMClass ("Materijal", "generated_app.test.model.magacin", "public");
		cl.addProperty(new FMProperty("sifraMaterijala", stringType, "private", 1, 1));
		cl.addProperty(new FMProperty("nazivMaterijala", stringType, "private", 1, 1));
		cl.addProperty(new FMProperty("slozen", booleanType, "private", 1, 1));
		
		classes.add(cl);
		
		cl = new FMClass ("Odeljenje", "generated_app.test.model.orgsema", "public");
		cl.addProperty(new FMProperty("sifra", stringType, "private", 1, 1));
		cl.addProperty(new FMProperty("naziv", stringType, "private", 1, 1));
		
		classes.add(cl);
		
		cl = new FMClass ("Osoba", "generated_app.test.model", "public");
		cl.addProperty(new FMProperty("prezime", stringType, "private", 1, 1));		
		cl.addProperty(new FMProperty("ime", stringType, "private", 1, 1));
		cl.addProperty(new FMProperty("datumRodjenja", dateType, "private", 0, 1));
		cl.addProperty(new FMProperty("clanoviPorodice", cl, "private", 0, -1));	
		cl.addProperty(new FMProperty("vestina", stringType, "private", 1, 3));
		cl.getPersistentProperties().add(new FMAttributeNameValue("name", Arrays.asList(new String[] {"\"licnost\""})));

		classes.add(cl);
		
		cl = new FMClass ("Kartica", "generated_app.test.model.magacin.kartica", "public");
		cl.addProperty(new FMProperty("sifraKartice", stringType, "private", 1, 1));
		cl.addProperty(new FMProperty("nazivKartice", stringType, "private", 1, 1));
		
		classes.add(cl);		
	}
	
	private void initXmlTestModel() throws UnsupportedEncodingException, FileNotFoundException {
		String path = "..\\models\\our_example.xml";
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"));
		
		XStream xstream = new XStream(new DomDriver());    
		 
		FMModel loadedModel = (FMModel) xstream.fromXML(r);
		
		
		FMModel.getInstance().setClasses(loadedModel.getClasses());
		FMModel.getInstance().setEnumerations(loadedModel.getEnumerations());
	}
	
	private void generate() {
		GeneratorsRunner runner = new GeneratorsRunner();
		try {
			runner.generate();
			System.out.println("Generisanje uspesno zavrseno!!!");
		} catch (IOException e) {
			System.out.println("Greska pri generisanju sablona!!!");
		}
	}
	
	public void testGenerator(TestCase testCase) throws UnsupportedEncodingException, FileNotFoundException {
		if (testCase.equals(TestCase.TEST_FROM_XML)) {
			initXmlTestModel();
		} else {
			initTestModel();
		}
				
		generate();
	}
	
	public static void main(String[] args) throws IOException {
		TestPackageGeneration tg = new TestPackageGeneration();
		
		// setting options for particular generator
//		GeneratorOptions entityOptions = new GeneratorOptions("EntityGenerator", "c:/temp", "ejbclass", "templates", "{0}.java", true, "generated_app"); 				
//		ProjectOptions.getProjectOptions().getGeneratorOptions().put("EntityGenerator", entityOptions);		
		
		// loading generation options for all generators from xml
		XmlConfigurationHelper conf = new XmlConfigurationHelper(projectXmlDirectory, "TESTING");
		GeneratorOptionsRoot root = conf.loadFromXml();
		conf.setProjectOptions(root);
		
		TestCase testCase = args.length > 0 ? TestCase.BASIC_TEST : TestCase.TEST_FROM_XML;
		tg.testGenerator(testCase);
	}
	
	
	
	
}
