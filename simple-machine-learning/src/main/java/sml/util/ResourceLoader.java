package sml.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ResourceLoader {
	public void loadAndParseResource(String resourcePath, ILineParser parser) {
		try (BufferedReader br = new BufferedReader(new FileReader(loadResourceAsFile(resourcePath)))) {
			String line;
			while ((line = br.readLine()) != null) {
				parser.parseLine(line);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public File loadResourceAsFile(String resourcePath) {
		ClassLoader classLoader = getClass().getClassLoader();
		return new File(classLoader.getResource(resourcePath).getFile());
	}

	public String loadResourceAsString(String resourcePath) {
		StringBuilder result = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new FileReader(loadResourceAsFile(resourcePath)))) {
			String line;
			while ((line = br.readLine()) != null) {
				result.append(line);
				result.append("\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result.toString();
	}
}
