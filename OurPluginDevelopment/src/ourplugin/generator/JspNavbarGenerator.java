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

public class JspNavbarGenerator extends BasicGenerator{

	public JspNavbarGenerator(GeneratorOptions generatorOptions) {
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
		
		Writer out;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			String bootstrap_css = "https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css";
			String bootstrap_js = "https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js";
			String jquery = "jquery-3.5.1.min.js";
			String datatables_css = "https://cdn.datatables.net/1.10.21/css/jquery.dataTables.css";
			String datatables_js = "https://cdn.datatables.net/1.10.21/js/jquery.dataTables.js";
			
			String appName = "OurPluginApp";

			out = getWriter("", "webapp.WEB-INF.jsp");
			if (out != null) {
				context.clear();
				
				context.put("bootstrap_css", bootstrap_css);
				context.put("bootstrap_js", bootstrap_js);
				context.put("jquery", jquery);
				context.put("datatables_css", datatables_css);
				context.put("datatables_js", datatables_js);
				
				context.put("app_name", appName);
				context.put("entities", classes);
				
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
