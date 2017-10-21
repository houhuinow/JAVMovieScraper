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

/**
 *
 * @author deusp
 */
public class Amalgamation {

	public static AmalgamationGroup[] load(String fileName) {
		AmalgamationGroup[] groups = null;
		// Read objects
		File jsonFile = new File(fileName);
		try(FileReader reader = new FileReader(jsonFile)) {
			try(com.google.gson.stream.JsonReader jsonReader = new com.google.gson.stream.JsonReader(reader)) {
				Gson gson = new Gson();
				groups = gson.fromJson(jsonReader, AmalgamationGroup[].class);
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

	public static void save(AmalgamationGroup[] groups, String fileName) {
		// Read objects
		File outputFile = new File(fileName);
		try(FileWriter output = new FileWriter(outputFile)) {
			Gson gson = new Gson();
			String json = gson.toJson(groups);
			output.write(json);
		}
		catch (IOException ex) {
			ex.printStackTrace();

		}

	}
}
