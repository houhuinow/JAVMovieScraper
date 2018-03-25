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
import java.util.Set;
import moviescraper.doctord.controller.siteparsingprofile.SiteParsingProfile;
import moviescraper.doctord.controller.siteparsingprofile.specific.SpecificProfile;

/**
 * An amalgamation is the association of a name and a type to a sorted list of scraper for each field of this type
 * e.g. An amalgamation named "asian movies" for type "Movie" will hold a list of scrapers name for plot, one for year and so on
 * 
 */
public class Amalgamation {

	private final String name;
	private final Class type;
	private final String icon;
	private final Map<String, List<SiteParsingProfile>> fields;

	public Amalgamation(AmalgamationDefinition definition) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.name = definition.getName();
		this.type = Class.forName("moviescraper.doctord.model." + definition.getType());
		this.icon = definition.getIcon();
		fields = new HashMap<>();
		Map<String, List<String>> fieldMap = definition.getFields();

		for(Map.Entry<String, List<String>> field: fieldMap.entrySet()) {
			// Load scraper
			List<SiteParsingProfile> scrapers = new ArrayList<>();

			for(String scraperName: field.getValue()) {
				SiteParsingProfile scraper = (SiteParsingProfile) Class.forName("moviescraper.doctord.controller.siteparsingprofile.specific." + scraperName).newInstance();
				scrapers.add(scraper);
			}

			fields.put(field.getKey(), scrapers);
		}
	}

	public AmalgamationDefinition getDefinition() {
			return new AmalgamationDefinition(this.name, this.type, this.icon);
	}

	public String getName() {
		return name;
	}

	public String getIcon() {
		return icon;
	}

	public String toString() {
		StringBuilder outputString = new StringBuilder();
		outputString.append(this.getName());
		outputString.append(" --> ");
		for(Map.Entry<String,List<SiteParsingProfile>> field: fields.entrySet()) {
			outputString.append(field.getKey());
			outputString.append(field.getValue());
			outputString.append(", ");
		}

		return outputString.toString();
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
				}
				System.out.println("--");
				for(Amalgamation ama: groups) {
					System.out.println(" >" + ama.toString());
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
