/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviescraper.doctord.controller.amalgamation;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import moviescraper.doctord.model.AmalgationField;


/**
 *
 * @author deusp
 */
public class AmalgamationGroup<ElementType> {

	private String name;
	private String group_type;
	private transient Class<?> group_type_class;
	private Map<String, List<Object>> categories;


	public AmalgamationGroup(String name, ElementType... dummy) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.name = name;
		this.group_type_class = dummy.getClass().getComponentType();
		this.group_type = this.group_type_class.getName();

		this.categories = new HashMap<>();

		for(Field field: this.group_type_class.getDeclaredFields()) {
			if(field.getAnnotation(AmalgationField.class) != null) {
				this.categories.put(field.getName(), new ArrayList<>());
			}
		}
	}
	
	public Map<String, List<Object>> getCategories() {
		return this.categories;
	}

	public ElementType order(List<ElementType> elements) {
		return elements.get(0);
	}
}
