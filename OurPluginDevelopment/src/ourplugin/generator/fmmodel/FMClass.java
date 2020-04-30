package ourplugin.generator.fmmodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FMClass extends FMType {	
	
	private String visibility;
	
	//Class properties
	private List<FMProperty> FMProperties = new ArrayList<FMProperty>();
	
	//list of packages (for import declarations) 
	private List<FMType> importedPackages = new ArrayList<FMType>();
	
	private List<FMAttributeNameValue> persistentProperties = new ArrayList<FMAttributeNameValue>();
	
	/** @ToDo: add list of methods */
	private String comment;
	
	public FMClass(String name, String classPackage, String visibility) {
		super(name, classPackage);		
		this.visibility = visibility;
	}	
	
	public List<FMProperty> getProperties(){
		return FMProperties;
	}
	
	public Iterator<FMProperty> getPropertyIterator(){
		return FMProperties.iterator();
	}
	
	public void addProperty(FMProperty property){
		FMProperties.add(property);		
	}
	
	public int getPropertyCount(){
		return FMProperties.size();
	}
	
	public List<FMType> getImportedPackages(){
		return importedPackages;
	}

	public Iterator<FMType> getImportedIterator(){
		return importedPackages.iterator();
	}
	
//	public void addImportedPackage(String importedPackage){
//		importedPackages.add(importedPackage);		
//	}
	
	public void addImportedPackage(FMType importedPackage){
		importedPackages.add(importedPackage);		
	}
	
	public int getImportedCount(){
		return FMProperties.size();
	}
	
	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<FMAttributeNameValue> getPersistentProperties() {
		return persistentProperties;
	}

	public void setPersistentProperties(List<FMAttributeNameValue> persistentProperties) {
		this.persistentProperties = persistentProperties;
	}	

	
	
}
