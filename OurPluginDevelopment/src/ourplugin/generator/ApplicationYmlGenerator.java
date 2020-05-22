package ourplugin.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import freemarker.template.TemplateException;
import ourplugin.generator.fmmodel.FMClass;
import ourplugin.generator.fmmodel.FMModel;
import ourplugin.generator.options.GeneratorOptions;

public class ApplicationYmlGenerator extends BasicGenerator{

	public ApplicationYmlGenerator(GeneratorOptions generatorOptions) {
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
			String port = "8081";
			String contextPath = "/myapp";
			String applicationName = "Springen application";
			String viewType = "jsp";
			String dataSourceUrl = "";
			String username = "";
			String password = "";
			String h2Path = "";
			String driverClassName = "";
			String hibernateDialect = "";
			if(dataSource.equals("h2")) {
				dataSourceUrl = "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
				username = "sa";
				password = "sa";
				h2Path = "/h2-console";
				driverClassName = "org.h2.Driver";
				hibernateDialect = "org.hibernate.dialect.H2Dialect";
			} else if(dataSource.equals("mysql")) {
				dataSourceUrl = "jdbc:mysql://localhost:3306/jsp?useSSL=false&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true";
				username = "root";
				password = "1234";
				driverClassName = "com.mysql.cj.jdbc.Driver";
				hibernateDialect = "org.hibernate.dialect.MySQL5Dialect";
			} else { //if postgres
				dataSourceUrl = "jdbc:postgresql://localhost:5432/postgres?useSSL=false";
				username = "postgres";
				password = "postgres";
				hibernateDialect = "org.hibernate.dialect.PostgreSQL95Dialect";
			}
			
			
			out = getWriter("", "resources");
			if (out != null) {
				
				context.clear();
				context.put("port", port);
				context.put("contextPath", contextPath);
				context.put("applicationName", applicationName);
				context.put("viewType", viewType);
				context.put("dataSource", dataSource);
				context.put("dataSourceUrl", dataSourceUrl);
				context.put("username", username);
				context.put("password", password);
				context.put("h2Path", h2Path);
				context.put("driverClassName", driverClassName);
				context.put("hibernateDialect", hibernateDialect);			
				
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
