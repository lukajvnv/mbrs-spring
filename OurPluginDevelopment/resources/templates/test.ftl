<#import "/commons/utils.ftl" as u>
<#assign class_name_cap = class.name?cap_first>
<#assign class_name_uncap = class.name?uncap_first>
<#assign entity_list_var = u.plural(class_name_uncap) >
<#assign repository_var = "${class_name_uncap}Repository">
<#assign service_var = "${class_name_uncap}Service">
<#assign dto_to_entity_conv_var = "${class_name_uncap}DtoTo${class_name_cap}Converter">
<#assign entity_to_dto_conv_var = "${class_name_uncap}To${class_name_cap}DtoConverter">
<#assign entity_mock_1_var = "${class_name_uncap}Mock1">
<#assign entity_mock_2_var = "${class_name_uncap}Mock2">
<#assign dto_mock_1_var = "${class_name_uncap}DtoMock1">
<#assign dto_mock_2_var = "${class_name_uncap}DtoMock2">
<#macro set_value prop index>
<#compress>
	<#if prop.type.name == 'String'>
    "${prop.name}${index}"
	<#elseif prop.type.name == 'Date'>
	date
	<#elseif prop.type.name == 'Integer'>
	${index}
	<#elseif prop.type.name == 'boolean' || prop.type.name == 'Boolean'>
	true
	<#else>
	"${prop.name}${index}"
	</#if>
</#compress>
</#macro>
<#macro set_properties props ind>
	<#list props as property>
            .${property.name}(<@set_value prop=property index=ind />)
    </#list>
</#macro>
<@u.gen_template_metadata template_name=(.current_template_name) author=user class_name=class_name_cap/>
package ${service_package};

// static test imports
<#list static_imports as type>
import static ${type.typePackage}.${type.name};
</#list>

// other imports
<#list other_imports as type>
import ${type.typePackage}.${type.name};
</#list>

// entity specific imports
<#list importedPackages as type>
import ${type.typePackage}.${type.name};
</#list>

import ${model_package}.${class_name_cap};
import ${dto_package}.${class_name_cap}Dto;
import ${converter_package}.${class_name_cap}DtoTo${class_name_cap}Converter;
import ${converter_package}.${class_name_cap}To${class_name_cap}DtoConverter;
import ${repository_package}.${class_name_cap}Repository;

// dependent entities imports
<#list importedPackages?filter(p -> p.classType == true) as type>
import ${type.typePackage?replace("model", "dto")}.${type.name}Dto;
import ${type.typePackage?replace("model", "converter")}.${type.name}To${type.name}DtoConverter;
import ${type.typePackage?replace("model", "converter")}.${type.name}DtoTo${type.name}Converter;
import ${type.typePackage?replace("model", "repository")}.${type.name}Repository;

</#list>

@RunWith(SpringRunner.class)
@SpringBootTest
public class ${class_name_cap}ServiceTest {

    private List<${class_name_cap}> ${entity_list_var} = new ArrayList<${class_name_cap}>();
    
    private Date date;
	
    @Mock
    private ${class_name_cap}Repository ${repository_var};
	
    @Mock
    private ${class_name_cap}To${class_name_cap}DtoConverter ${entity_to_dto_conv_var};
	
    @Mock
    private ${class_name_cap}DtoTo${class_name_cap}Converter ${dto_to_entity_conv_var};
	
    @Mock
    private ${class_name_cap} ${entity_mock_1_var};
    
    @Mock
    private ${class_name_cap} ${entity_mock_2_var};

    @Mock
    private ${class_name_cap}Dto ${dto_mock_1_var};
	
    @Mock
    private ${class_name_cap}Dto ${dto_mock_2_var};
	
    @InjectMocks
    private ${class_name_cap}ImplService ${service_var};

    public void setUp(){
        List<${class_name_cap}> ${entity_list_var}Test = new ArrayList<${class_name_cap}>();
        
        ${entity_mock_1_var} = ${class_name_cap}.builder()
        	<@set_properties props = properties ind = 1 />
            .build();
        ${dto_mock_1_var} = ${class_name_cap}Dto.builder()
             <@set_properties props = properties ind = 2 />
            .build();
        ${entity_list_var}Test.add(${entity_mock_1_var});
		
        ${entity_mock_2_var} = ${class_name_cap}.builder()
             <@set_properties props = properties ind = 1 />
            .build();
        ${dto_mock_2_var} = ${class_name_cap}Dto.builder()
             <@set_properties props = properties ind = 2 />
            .build();
        ${entity_list_var}Test.add(${entity_mock_2_var});
		
        ${entity_list_var} = ${entity_list_var}Test;
        
        Calendar cal = Calendar.getInstance();
        cal.set(2020, 04, 04, 0, 0);
		
        date = cal.getTime();
    }
    
    @Test
    public void testFindAll${entity_list_var?cap_first}() {
        setUp();
			
        when(${repository_var}.findAll()).thenReturn(${entity_list_var});
        when(${entity_to_dto_conv_var}.convert(${entity_mock_1_var})).thenReturn(${dto_mock_1_var});
        when(${entity_to_dto_conv_var}.convert(${entity_mock_2_var})).thenReturn(${dto_mock_2_var});

        List<${class_name_cap}Dto> ${entity_list_var}Test = ${service_var}.getAll();
        assertThat(${entity_list_var}Test).hasSize(2);
    }
	
    @Test
    public void testGet${class_name_cap}() {
        setUp();
   
        Optional<${class_name_cap}> optional${class_name_cap} = Optional.of(${entity_mock_1_var});
        when(${repository_var}.findById(1l)).thenReturn(optional${class_name_cap});
        
        when(${entity_to_dto_conv_var}.convert(${entity_mock_1_var})).thenReturn(${dto_mock_1_var});
		
        ${class_name_cap}Dto ${class_name_uncap} = ${service_var}.getOne(1);
        
        <#list properties as property>
        assertThat(${class_name_uncap}.get${property.name?cap_first}()).isEqualTo(${dto_mock_1_var}.get${property.name?cap_first}());
        </#list>

        verify(${repository_var}, times(1)).findById(1);
        verify(${entity_to_dto_conv_var}, times(1)).convert(${entity_mock_1_var});
        verifyNoMoreInteractions(${repository_var}, ${entity_to_dto_conv_var});
    }
	
    @Test
    @Transactional
    public void testCreate${class_name_cap}() {
        setUp();

        when(${repository_var}.findAll()).thenReturn(Arrays.asList(${entity_mock_1_var}));
        when(${entity_to_dto_conv_var}.convert(${entity_mock_1_var})).thenReturn(${dto_mock_1_var});
		
        int ${entity_list_var}Before = ${service_var}.getAll().size();

        ${class_name_cap}Dto ${class_name_uncap}Dto = ${class_name_cap}Dto.builder()
             <@set_properties props = properties ind = 3 />
            .build();
        
        ${class_name_cap} ${class_name_uncap} = ${class_name_cap}.builder()
              <@set_properties props = properties ind = 3 />
            .build();
        	 
        when(${repository_var}.save(${class_name_uncap})).thenReturn(${class_name_uncap});
        when(${dto_to_entity_conv_var}.convert(${class_name_uncap}Dto)).thenReturn(${class_name_uncap});
        when(${entity_to_dto_conv_var}.convert(${class_name_uncap})).thenReturn(${class_name_uncap}Dto);

        ${class_name_cap}Dto saved = ${service_var}.save(${class_name_uncap}Dto);
        assertThat(saved).isNotNull();
        <#list properties as property>
        assertThat(saved.get${property.name?cap_first}()).isEqualTo(${class_name_uncap}Dto.get${property.name?cap_first}());
        </#list>
		
        when(${repository_var}.findAll()).thenReturn(Arrays.asList(${entity_mock_1_var}, ${class_name_uncap}));
        when(${entity_to_dto_conv_var}.convert(${entity_mock_1_var})).thenReturn(${dto_mock_1_var});
        when(${entity_to_dto_conv_var}.convert(${class_name_uncap})).thenReturn(${class_name_uncap}Dto);
		
        int ${entity_list_var}After = ${service_var}.getAll().size();

        assertThat(${entity_list_var}Before + 1).isEqualTo(${entity_list_var}After);

        verify(${repository_var}, times(2)).findAll();
        verify(${repository_var}, times(1)).save(${class_name_uncap});
        verifyNoMoreInteractions(${repository_var});
    }
	
    @Test
    @Transactional
    public void testUpdate${class_name_cap}() {
        setUp();
        
        when(${repository_var}.findAll()).thenReturn(Arrays.asList(${entity_mock_1_var}));
        when(${entity_to_dto_conv_var}.convert(${entity_mock_1_var})).thenReturn(${dto_mock_1_var});
		
        int ${entity_list_var}Before = ${service_var}.getAll().size();
		
        ${class_name_cap}Dto ${class_name_uncap}Dto = ${class_name_cap}Dto.builder()
             <@set_properties props = properties ind = 3 />
            .build();
        
        ${class_name_cap} ${class_name_uncap} = ${class_name_cap}.builder()
             <@set_properties props = properties ind = 3 />
            .build();
		
        when(${repository_var}.save(${class_name_uncap})).thenReturn(${class_name_uncap});
        when(${dto_to_entity_conv_var}.convert(${class_name_uncap}Dto)).thenReturn(${class_name_uncap});
        when(${entity_to_dto_conv_var}.convert(${class_name_uncap})).thenReturn(${class_name_uncap}Dto);

        ${class_name_cap}Dto saved = ${service_var}.save(${class_name_uncap}Dto);
        assertThat(saved).isNotNull();
        <#list properties as property>
        assertThat(saved.get${property.name?cap_first}()).isEqualTo(${class_name_uncap}Dto.get${property.name?cap_first}());
        </#list>
		
        when(${repository_var}.findAll()).thenReturn(Arrays.asList(${class_name_uncap}));
        when(${entity_to_dto_conv_var}.convert(${class_name_uncap})).thenReturn(${class_name_uncap}Dto);
		
        int ${entity_list_var}After = ${service_var}.getAll().size();

        assertThat(${entity_list_var}Before).isEqualTo(${entity_list_var}After);

        verify(${repository_var}, times(2)).findAll();
        verify(${repository_var}, times(1)).save(${class_name_uncap});
        verifyNoMoreInteractions(${repository_var});		
    }
	
    @Test
    @Transactional
    public void testRemove${class_name_cap}() {
        setUp();
       
        when(${repository_var}.findAll()).thenReturn(${entity_list_var});
        when(${entity_to_dto_conv_var}.convert(${entity_mock_1_var})).thenReturn(${dto_mock_1_var});
        when(${entity_to_dto_conv_var}.convert(${entity_mock_2_var})).thenReturn(${dto_mock_2_var});
		
        int dbSizeBeforeRemove = ${service_var}.getAll().size();
        doNothing().when(${repository_var}).deleteById(2);
        ${service_var}.deleteById(2);
		
        when(${repository_var}.findAll()).thenReturn(Arrays.asList(${entity_mock_1_var}));
        when(${entity_to_dto_conv_var}.convert(${entity_mock_1_var})).thenReturn(${dto_mock_1_var});

        List<${class_name_cap}Dto> ${entity_list_var}Test = ${service_var}.getAll();
        assertThat(${entity_list_var}Test).hasSize(dbSizeBeforeRemove - 1);
		
        Optional<${class_name_cap}> optional${class_name_cap} = Optional.ofNullable(null);
        when(${repository_var}.findById(2)).thenReturn(optional${class_name_cap});
        ${class_name_cap}Dto ${class_name_uncap}Test = ${service_var}.getOne(2);
        assertThat(${class_name_uncap}Test).isNull();
        
        verify(${repository_var}, times(1)).deleteById(2);
        verify(${repository_var}, times(2)).findAll();
        verify(${repository_var}, times(1)).findById(2);
        verifyNoMoreInteractions(${repository_var});
    }
}

<@u.block_end />