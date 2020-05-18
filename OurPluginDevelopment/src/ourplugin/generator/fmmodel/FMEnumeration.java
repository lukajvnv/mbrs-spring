package ourplugin.generator.fmmodel;


import java.util.ArrayList;
import java.util.Iterator;



public class FMEnumeration extends FMType {
	public FMEnumeration() {
		super();
		// TODO Auto-generated constructor stub
	}

	private ArrayList <String> values = new ArrayList<String>();
	
	public FMEnumeration(String name, String typePackage) {
		super(name, typePackage);
	}
	
	public Iterator<String> getValueIterator(){
		return values.iterator();
	}
	
	public void addValue(String value){
		values.add(value);		
	}
	
	public int getValuesCount(){
		return values.size();
	}

	public String getValueAt(int i){
		return values.get(i);
	}

	public ArrayList<String> getValues() {
		return values;
	}
	
}
