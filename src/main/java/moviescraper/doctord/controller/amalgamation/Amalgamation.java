/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviescraper.doctord.controller.amalgamation;

import com.google.gson.Gson;
import com.google.gson.stream.JsonToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author deusp
 */
public class Amalgamation {

	private String name;
	private Class type;
	private final Map<String, List<String>> categories;

	public Amalgamation(AmalgamationDefinition definition) throws ClassNotFoundException {
		this.name = definition.getName();
		this.type = Class.forName("moviescraper.doctord.model." + definition.getType());
		categories = new HashMap<>();
		Map<String, List<String>> categoriesMap = definition.getCategories();
		for(String categoryKeys: categoriesMap.keySet()) {
			// Load scraper
			categories.put(categoryKeys, categoriesMap.get(categoryKeys));
		}
	}

	public AmalgamationDefinition getDefinition() {
			return new AmalgamationDefinition(this.name, this.type);
	}

	public String getName() {
		return name;
	}

	public static List<Amalgamation> load(String fileName) {
		List<Amalgamation> groups = new ArrayList<>();

		// Read objects
		File jsonFile = new File(fileName);
		try(FileReader reader = new FileReader(jsonFile)) {
			try(com.google.gson.stream.JsonReader jsonReader = new com.google.gson.stream.JsonReader(reader)) {
				jsonReader.setLenient(true);
				Gson gson = new Gson();
				while (jsonReader.peek() != JsonToken.END_DOCUMENT) {
					AmalgamationDefinition definition = gson.fromJson(jsonReader, AmalgamationDefinition.class);
					groups.add(new Amalgamation(definition));
					for(Amalgamation ama: groups) {
						System.out.println(ama.name + "--");
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return groups;
	}

	public static void save(List<Amalgamation> groups, String fileName) {
		// Read objects
		File outputFile = new File(fileName);
		try(FileWriter output = new FileWriter(outputFile)) {
			Gson gson = new Gson();
			for(Amalgamation amalgamation: groups) {
				String json = gson.toJson(amalgamation.getDefinition());
				System.out.println(json);
				output.write(json);
			}
		}
		catch (IOException ex) {
			ex.printStackTrace();

		}
	}

}
