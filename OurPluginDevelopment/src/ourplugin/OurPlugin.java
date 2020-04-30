package ourplugin;

import java.io.File;
import java.util.Map;

import javax.swing.JOptionPane;

import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.actions.ActionsConfiguratorsManager;

import ourplugin.action.ExportToXmlAction;
import ourplugin.action.GenerateAction;
import ourplugin.action.ViewLogAction;
import ourplugin.generator.options.GeneratorOptions;
import ourplugin.generator.options.ProjectOptions;
import ourplugin.generator.options.xml.GeneratorOptionsRoot;
import ourplugin.generator.options.xml.XmlConfigurationHelper;
import ourplugin.util.LogHelperClass;

/** MagicDraw plugin that performes code generation */
public class OurPlugin extends com.nomagic.magicdraw.plugins.Plugin {
	
	String pluginDir = null; 
    	
	public void init() {
		pluginDir = getDescriptor().getPluginDirectory().getPath();
		String projectOptionsXmlPath = pluginDir + File.separator + "ProjectOptions.xml";
		String templatesDir = pluginDir + File.separator + "templates";
		String logFilePath = pluginDir + File.separator + "logs/log.txt";
		String environment = "PLUGIN";

		// Creating submenu in the MagicDraw main menu 	
		ActionsConfiguratorsManager manager = ActionsConfiguratorsManager.getInstance();		
		manager.addMainMenuConfigurator(new MainMenuConfigurator(getSubmenuActions()));
		
		LogHelperClass.getLogHelperClass().setLogFilePath(logFilePath);
		
		// loading generation options for all generators
		try {
			XmlConfigurationHelper conf = new XmlConfigurationHelper(projectOptionsXmlPath, environment);
			conf.setTemplatesDir(templatesDir);
			GeneratorOptionsRoot root;
			root = conf.loadFromXml();
			conf.setProjectOptions(root);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
				
		// settign options for particular generator
//		GeneratorOptions entityOptions = new GeneratorOptions("EntityGenerator", "c:/temp", "ejbclass", "templates", "{0}.java", true, "generated_app"); 				
//		ProjectOptions.getProjectOptions().getGeneratorOptions().put("EntityGenerator", entityOptions);		
//		entityOptions.setTemplateDir(templatesDir); 
		      
		new SuccessfulPluginInitBeep(2);
	}

	private NMAction[] getSubmenuActions()
	{
	   return new NMAction[]{
			new GenerateAction("Generate"), new ExportToXmlAction("Export To Xml"), new ViewLogAction("View log action")
	   };
	}
	
	public boolean close() {
		return true;
	}
	
	public boolean isSupported() {				
		return true;
	}

}


