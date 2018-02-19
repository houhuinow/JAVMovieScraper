/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviescraper.doctord.controller.amalgamation;

import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import moviescraper.doctord.model.AmalgationField;


/**
 *
 * @author deusp
 */
public class AmalgamationDefinition {

	private final String name;
	private String type;
	private final Map<String, List<String>> categories;


	public AmalgamationDefinition(String name, Class type){
		this.name = name;
		this.type = type.getSimpleName();

		this.categories = new HashMap<>();

		for(Field field: type.getDeclaredFields()) {
			if(field.getAnnotation(AmalgationField.class) != null) {
				this.categories.put(field.getName(), new ArrayList<>());
			}
		}
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}
	
	public Map<String, List<String>> getCategories() {
		return this.categories;
	}
}
