package ourplugin.generator.fmmodel;


import java.util.ArrayList;
import java.util.Iterator;



public class FMEnumeration extends FMType {
	public FMEnumeration() {
		super();
		// TODO Auto-generated constructor stub
	}

	private ArrayList <String> Values = new ArrayList<String>();
	
	public FMEnumeration(String name, String typePackage) {
		super(name, typePackage);
	}
	
	public Iterator<String> getValueIterator(){
		return Values.iterator();
	}
	
	public void addValue(String value){
		Values.add(value);		
	}
	
	public int getValuesCount(){
		return Values.size();
	}

	public String getValueAt(int i){
		return Values.get(i);
	}
	
}
