package ourplugin.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import freemarker.template.TemplateException;
import ourplugin.generator.options.GeneratorOptions;

public class PomXmlGenerator extends BasicGenerator{

	public PomXmlGenerator(GeneratorOptions generatorOptions) {
		super(generatorOptions);
		// TODO Auto-generated constructor stub
	}
	
	public void generate() {
		
		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
			
		Writer out;
		Map<String, Object> context = new HashMap<String, Object>();
	
		try {
			String dataSource = "h2";
			String groupId = "com.example";
			String artifactId = "jsp";
			String version = "0.0.1-SNAPSHOT";
			String applicationName = "Springen application";
			String viewType = "jsp";
			String packaging = "war";
			String description = "Demo project for Spring Boot";
			String javaVersion = "1.8";

			out = getWriter("", "");
			if (out != null) {
				
				context.clear();
				context.put("dataSource", dataSource);
				context.put("artifactId", artifactId);
				context.put("version", version);
				context.put("applicationName", applicationName);
				context.put("viewType", viewType);
				context.put("groupId", groupId);
				context.put("packaging", packaging);
				context.put("description", description);
				context.put("javaVersion", javaVersion);			
				
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
