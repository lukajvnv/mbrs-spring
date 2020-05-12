package ourplugin.generator.fmmodel;

import java.util.ArrayList;
import java.util.List;

public class FMProperty extends FMElement  {
	public FMProperty() {
		super();
		// TODO Auto-generated constructor stub
	}

	//Property type
	private FMType type;
	// Property visibility (public, private, protected, package)
	private String visibility;
	//Multiplicity (lower value)
	private Integer lower;
	//Multiplicity (upper value) 
	private Integer upper;
	
	// @Column, @OneToMany, @ManyToMany....
	private String persistentAnnotationName;
	private List<FMAttributeNameValue> persistentProperties = new ArrayList<FMAttributeNameValue>();
	
	//if property is @Id
	private List<FMAttributeNameValue> identityProperties = new ArrayList<FMAttributeNameValue>();

	/** @ToDo: Add length, precision, unique... whatever is needed for ejb class generation
	 * Also, provide these meta-attributes or tags in the modeling languange metaclass or 
	 * stereotype */

	
	public FMProperty(String name, FMType type, String visibility, int lower, int upper) {
		super(name);
		this.type = type;
		this.visibility = visibility;
		
		this.lower = lower;
		this.upper = upper;		
	}
	
	public FMType getType() {
		return type;
	}
	public void setType(FMType type) {
		this.type = type;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	
	public Integer getLower() {
		return lower;
	}

	public void setLower(Integer lower) {
		this.lower = lower;
	}

	public Integer getUpper() {
		return upper;
	}

	public void setUpper(Integer upper) {
		this.upper = upper;
	}

	public List<FMAttributeNameValue> getPersistentProperties() {
		return persistentProperties;
	}

	public void setPersistentProperties(List<FMAttributeNameValue> persistentProperties) {
		this.persistentProperties = persistentProperties;
	}

	public String getPersistentAnnotationName() {
		return persistentAnnotationName;
	}

	public void setPersistentAnnotationName(String persistentAnnotationName) {
		this.persistentAnnotationName = persistentAnnotationName;
	}

	public List<FMAttributeNameValue> getIdentityProperties() {
		return identityProperties;
	}

	public void setIdentityProperties(List<FMAttributeNameValue> identityProperties) {
		this.identityProperties = identityProperties;
	}
}
