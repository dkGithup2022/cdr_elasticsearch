package com.dk0124.cdr.test.customCondition;

import java.io.FileReader;


import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

public class TestConditions {

	public static boolean dontTestOnParent() {
		try {
			MavenXpp3Reader reader = new MavenXpp3Reader();
			Model model = reader.read(new FileReader("pom.xml"));
			String propertyValue = model.getProperties().getProperty("DontTestOnParent");
			if (String.valueOf(propertyValue).equals("true"))
				return true;
			return false;
		} catch (Exception e) {
			return false;
		}
	}

}
