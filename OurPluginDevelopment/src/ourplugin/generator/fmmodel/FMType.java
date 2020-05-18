package ourplugin.generator.fmmodel;

public class FMType extends FMElement {	

	public FMType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTypePackage() {
		return typePackage;
	}

	public FMType(String name, String typePackage) {
		super(name);
		this.typePackage = typePackage;
	}

	public FMType(String name, String typePackage, boolean classType) {
		this(name, typePackage);
		this.classType = classType;
	}

	public void setTypePackage(String typePackage) {
		this.typePackage = typePackage;
	}
	
	//Qualified package name, used for import declaration 
	//Empty string for standard library types
	private String typePackage;
	
	private boolean classType;

	public boolean isClassType() {
		return classType;
	}

	public void setClassType(boolean classType) {
		this.classType = classType;
	}

}
