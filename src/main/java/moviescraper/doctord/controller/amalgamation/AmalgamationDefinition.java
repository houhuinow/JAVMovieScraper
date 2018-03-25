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
	private final String type;
	private final String icon;
	private final List<String> availableFields;
	private final Map<String, List<String>> fields;


	public AmalgamationDefinition(String name, Class type, String icon){
		this.name = name;
		this.type = type.getSimpleName();
		this.icon = icon;
		this.fields = new HashMap<>();

		availableFields = new ArrayList<>();
		for(Field field: type.getDeclaredFields()) {
			if(field.getAnnotation(AmalgationField.class) != null) {
				availableFields.add(field.getName());
			}
		}
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public String getIcon() {
		return this.icon;
	}

	public void addScraperToField(String fieldName, String scraper) throws IllegalArgumentException {
		List<String> scrapers = this.fields.get(fieldName);
		if(scrapers == null) {
			if(availableFields.contains(fieldName)) {
				this.fields.put(fieldName, new ArrayList<>());
			}
		}

		if(scrapers == null) {
			throw new IllegalArgumentException("Unknown parameter " + fieldName + " in type "+type);
		}
		scrapers.add(scraper);
	}
	
	public Map<String, List<String>> getFields() {
		return this.fields;
	}
}
