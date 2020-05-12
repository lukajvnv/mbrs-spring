package ourplugin.generator.fmmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class primarly for tag values
 * @author Luka
 *
 */
public class FMAttributeNameValue extends FMElement {
	
	public FMAttributeNameValue() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String annotationName;
	private List<String> values = new ArrayList<String>();
	
	public FMAttributeNameValue(String name, List<String> values) {
		super(name);
		this.values = values;
	}
	
	public FMAttributeNameValue(String name, String annotationName, List<String> values) {
		super(name);
		this.annotationName = annotationName;
		this.values = values;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public String getAnnotationName() {
		return annotationName;
	}

	public void setAnnotationName(String annotationName) {
		this.annotationName = annotationName;
	}
	
}
