package ourplugin.analyzer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.nomagic.ci.server.ac.core.model.NamedElement;
import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Enumeration;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Type;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

import ourplugin.generator.fmmodel.FMAttributeNameValue;
import ourplugin.generator.fmmodel.FMClass;
import ourplugin.generator.fmmodel.FMEnumeration;
import ourplugin.generator.fmmodel.FMModel;
import ourplugin.generator.fmmodel.FMProperty;
import ourplugin.generator.fmmodel.FMType;
import ourplugin.generator.options.ProjectOptions;
import ourplugin.generator.options.TypeMapping;
import ourplugin.util.LogHelperClass;


/** Model Analyzer takes necessary metadata from the MagicDraw model and puts it in 
 * the intermediate data structure (@see myplugin.generator.fmmodel.FMModel) optimized
 * for code generation using freemarker. Model Analyzer now takes metadata only for ejb code 
 * generation

 * @ToDo: Enhance (or completely rewrite) myplugin.generator.fmmodel classes and  
 * Model Analyzer methods in order to support GUI generation. */ 


public class ModelAnalyzer {	
	//root model package
	private Package root;
	
	//java root package for generated code
	private String filePackage;
	
	private List<String> logMessages = new ArrayList<String>();
	private LogHelperClass logHelper = LogHelperClass.getLogHelperClass();
	private String messagePattern = "******** Inside element %s, start processing element: %s";
	
	public ModelAnalyzer(Package root, String filePackage) {
		super();
		this.root = root;
		this.filePackage = filePackage;
	}

	public Package getRoot() {
		return root;
	}
	
	public void prepareModel() throws AnalyzeException {
		FMModel.getInstance().getClasses().clear();
		FMModel.getInstance().getEnumerations().clear();
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			logMessages.add("Processing started at: " + dateFormat.format(date));
			processPackage(root, filePackage);
			updateImportReferences();
		} catch (Exception e1) {
			throw new AnalyzeException(e1.getMessage());
		} finally {
			try {
				logHelper.saveToFile(logMessages);
			} catch (IOException e) {
				throw new AnalyzeException("Error while writing logs in file!");
			}
		}
	}
	
	private void processPackage(Package pack, String packageOwner) throws AnalyzeException {
		//Recursive procedure that extracts data from package elements and stores it in the 
		// intermediate data structure
		if (pack.getName() == null) throw  
			new AnalyzeException("Packages must have names!");
		
		String packageName = packageOwner;
		if (pack != root) {
			packageName += "." + pack.getName();
		}
		
		logMessages.add(String.format(messagePattern, packageName, packageName));
		
		if (pack.hasOwnedElement()) {
			
			for (Iterator<Element> it = pack.getOwnedElement().iterator(); it.hasNext();) {
				Element ownedElement = it.next();
				if (ownedElement instanceof Class) {
					Class cl = (Class)ownedElement;
					FMClass fmClass = getClassData(cl, packageName);
					FMModel.getInstance().getClasses().add(fmClass);
				}
				
				if (ownedElement instanceof Enumeration) {
					Enumeration en = (Enumeration)ownedElement;
					FMEnumeration fmEnumeration = getEnumerationData(en, packageName);
					FMModel.getInstance().getEnumerations().add(fmEnumeration);
				}								
			}
			
			for (Iterator<Element> it = pack.getOwnedElement().iterator(); it.hasNext();) {
				Element ownedElement = it.next();
				if (ownedElement instanceof Package) {					
					Package ownedPackage = (Package)ownedElement;
					if (StereotypesHelper.getAppliedStereotypeByString(ownedPackage, "Proba") != null)
						//only packages with stereotype Proba are candidates for metadata extraction and code generation:
						processPackage(ownedPackage, packageName);
				}
			}
			
			/** @ToDo:
			  * Process other package elements, as needed */ 
		}
	}
	
	private FMClass getClassData(Class cl, String packageName) throws AnalyzeException {
		if (cl.getName() == null) 
			throw new AnalyzeException("Classes must have names!");
		
		logMessages.add(String.format(messagePattern, packageName, cl.getName()));
		
		FMClass fmClass = new FMClass(cl.getName(), packageName, cl.getVisibility().toString());
		Iterator<Property> it = ModelHelper.attributes(cl);
		while (it.hasNext()) {
			Property p = it.next();
			FMProperty prop = getPropertyData(p, cl);			
			fmClass.addProperty(prop);	
		}	
		
		//if property apply entity stereotype
		Stereotype entityStereotype = StereotypesHelper.getAppliedStereotypeByString(cl, "Entity");
		List<FMAttributeNameValue> persistentProperties = createAttributes(cl, entityStereotype);
		fmClass.getPersistentProperties().addAll(persistentProperties);
		
		findClassImports(fmClass);
		
		/** @ToDo:
		 * UI and everything other. */	
		Stereotype s = StereotypesHelper.getAppliedStereotypeByString(cl, "StandardnaForma");
		if(s != null) {
			List<?> value = StereotypesHelper.getStereotypePropertyValue(
                    cl, s, "labela");
			fmClass.setComment((String)value.get(0));
		}
		
		return fmClass;
	}
	
	private FMProperty getPropertyData(Property p, Class cl) throws AnalyzeException {
		String attName = p.getName();
		String className = cl.getName();
		if (attName == null) 
			throw new AnalyzeException("Properties of the class: " + cl.getName() +
					" must have names!");
		Type attType = p.getType();
		if (attType == null)
			throw new AnalyzeException("Property " + cl.getName() + "." +
			p.getName() + " must have type!");
		
		String typeName = attType.getName();
		if (typeName == null)
			throw new AnalyzeException("Type ot the property " + cl.getName() + "." +
			p.getName() + " must have name!");	
		
		logMessages.add(String.format(messagePattern, cl.getName(), p.getName()));
			
		int lower = p.getLower();
		int upper = p.getUpper();
		
		/**
		 * TypeMapping between Magic Draw and intermidiate model
		 */
		TypeMapping typeMapping = isDataType(typeName);
		String typePackage = "";
		if(typeMapping != null) {
			typeName = typeMapping.getDestType();
			typePackage = typeMapping.getLibraryName();
		}
		FMType type = new FMType(typeName, typePackage);
		
		FMProperty prop = new FMProperty(attName, type, p.getVisibility().toString(), 
				lower, upper);
		
		/**
		 * Entity profile
		 */
		//if property is of id type
		Stereotype identityStereotype = StereotypesHelper.getAppliedStereotypeByString(p, "IdentityProperty");
		if (identityStereotype != null) {
			List<FMAttributeNameValue> identityStereotypeProperties = createAttributes(p, identityStereotype);
			
			FMAttributeNameValue strategyAttr = null;
			for(FMAttributeNameValue att : identityStereotypeProperties) {
				if(att.getName().equals("strategy")) {
					strategyAttr = att;
				}
			}
			if(strategyAttr != null) {
				strategyAttr.setAnnotationName("@GeneratedValue");
			} else {
				strategyAttr = new FMAttributeNameValue(
						"", "@GeneratedValue", new ArrayList<String>());
			}
			
			FMAttributeNameValue idAttr = new FMAttributeNameValue(
					"", "@Id", new ArrayList<String>());
			
			List<FMAttributeNameValue> identityAtrrs = new ArrayList<FMAttributeNameValue>();
			identityAtrrs.add(idAttr);
			identityAtrrs.add(strategyAttr);
			
			prop.getIdentityProperties().addAll(identityAtrrs);
		}
		
		// if property is of standardized uml/java datatype
		Stereotype peristentStereotype = StereotypesHelper.getAppliedStereotypeByString(p, "PersistentProperty");
		if (peristentStereotype != null) {
			List<FMAttributeNameValue> persistentProperties = createAttributes(p, peristentStereotype);
			prop.getPersistentProperties().addAll(persistentProperties);
			prop.setPersistentAnnotationName("@Column");
		}
		
		// if property is reference to other entity
		Stereotype linkedProperty = StereotypesHelper.getAppliedStereotypeByString(p, "LinkedProperty");
		if (linkedProperty != null) {
			List<FMAttributeNameValue> linkedProperties = createAttributes(p, linkedProperty);
			prop.getPersistentProperties().addAll(linkedProperties);

			// determing if @OneToOne, @OneToMany, @ManyToMany, @ManyToOne
			String annotationPattern = "@%sTo%s";
			String referencedClass;
			if(upper == 1) {
				referencedClass = "One";
			} else {
				referencedClass = "Many";
			}
			
			Property referencingProperty = p.getOpposite();
			int referencingPropUpper = referencingProperty.getUpper();
			String opN = referencingProperty.getName();
			String referencingClass;
			if(referencingPropUpper == 1) {
				referencingClass = "One";
			} else {
				referencingClass = "Many";
			}
			prop.setPersistentAnnotationName(String.format(annotationPattern, referencingClass, referencedClass));
			
		}
		
		return prop;		
	}	
	
	private FMEnumeration getEnumerationData(Enumeration enumeration, String packageName) throws AnalyzeException {
		logMessages.add(String.format(messagePattern, packageName, enumeration.getName()));
		FMEnumeration fmEnum = new FMEnumeration(enumeration.getName(), packageName);
		List<EnumerationLiteral> list = enumeration.getOwnedLiteral();
		for (int i = 0; i < list.size() - 1; i++) {
			EnumerationLiteral literal = list.get(i);
			if (literal.getName() == null)  
				throw new AnalyzeException("Items of the enumeration " + enumeration.getName() +
				" must have names!");
			fmEnum.addValue(literal.getName());
		}
		return fmEnum;
	}
	
	/**
	 * retrieve and add all tag values if present for model element
	 * @param el model element (Class or Property)
	 * @param stereotype applied stereotype
	 * @return
	 */
	private List<FMAttributeNameValue> createAttributes(Element el, Stereotype stereotype) {
		List<FMAttributeNameValue> attributeNamaValuess = new ArrayList<FMAttributeNameValue>();
		
		if (stereotype != null) {
			List<Property> tags = stereotype.getOwnedAttribute();
			for(Property tag : tags) {
	            String tagName = tag.getName();
	            // preuzimanje vrednosti tagova
	            List<?> values = StereotypesHelper.getStereotypePropertyValue(
                        el, stereotype, tag.getName());
	            
	            //if tag has tag values
	            if(values.size() > 0) {
		            List<String> tagValues = new ArrayList<String>();
	            	
	            	for(Object val: values) {
	            		String newValue;
	            		if(val instanceof String) {
	            			newValue = "\"" + val + "\"";
	            		} else if (val instanceof EnumerationLiteral) {
	            			EnumerationLiteral eL = (EnumerationLiteral)val;
	            			newValue = eL.getName();
	            		} else {
	            			newValue = val.toString();
	            		}
	            		tagValues.add(newValue);
	            	}
	            	attributeNamaValuess.add(new FMAttributeNameValue(tagName, tagValues));
	            }

			}
		}
		
		return attributeNamaValuess;
	}
	
	/**
	 * Check if property is standardized Uml primitive or data Type
	 * @param umlDataType - String type value from uml model 
	 * @return
	 */
	private TypeMapping isDataType(String umlDataType) {
		List<TypeMapping> typeMappings = ProjectOptions.getProjectOptions().getTypeMappings();
		for(TypeMapping typeMapping : typeMappings) {
			if(typeMapping.getuMLType().equals(umlDataType)) {
				return typeMapping;
			}
		}
		return null;
	}
	
	/**
	 * find import path if some class has property whose type is other Class or Enum in second pass 
	 * @throws AnalyzeException 
	 */
	private void updateImportReferences() throws AnalyzeException {
		List<FMClass> classes = FMModel.getInstance().getClasses();
		List<FMEnumeration> enumeration = FMModel.getInstance().getEnumerations();
	
		List<FMType> elements = new ArrayList<FMType>(classes);
		elements.addAll(enumeration);
		
		for(FMClass cl : classes) {
			List<FMType> importsToRemove = new ArrayList<FMType>();
			List<FMType> importsToAdd = new ArrayList<FMType>();
			for(FMType typeImport : cl.getImportedPackages()) {
				// it is not primitive datatype
				if(typeImport.getTypePackage().equals("")) {
					FMType type = findElementByTypeName(typeImport.getName(), elements);
					FMType updatedImport = new FMType(type.getName(), type.getTypePackage());
					importsToAdd.add(updatedImport);
					importsToRemove.add(typeImport);
				}
			}
			cl.getImportedPackages().removeAll(importsToRemove);
			cl.getImportedPackages().addAll(importsToAdd);
			
		}
	}
	
	/**
	 * Find FMType element from the all model elements ( classes and enums)
	 * @param typeName 
	 * @param elements all elements it the model
	 * @return
	 * @throws AnalyzeException
	 */
	private FMType findElementByTypeName(String typeName, List<FMType> elements) throws AnalyzeException {
		FMType element = null;
		for(FMType el: elements) {
			if(el.getName().equals(typeName)) {
				element = el;
				break;
			}
		}
		
		if(element != null) {
			return element;
		} else {
			throw new AnalyzeException(String.format("Cound not find type with name %s in model's elements registry", typeName));
		}
	}
	
	/**
	 * add import declarations based on data elements used in classe
	 * @param cl
	 */
	private void findClassImports(FMClass cl) {
		// property check
		for (FMProperty property : cl.getProperties()) {
			String typePackage = property.getType().getTypePackage().trim();
			if ( !typePackage.equals("java.lang")
					&& !typePackage.equals(cl.getTypePackage())) {
				cl.getImportedPackages().add(property.getType());
			}

			if (property.getUpper() == -1) {
				cl.getImportedPackages().add(new FMType("Set", "java.util"));
			}
		}
	}
	
}
