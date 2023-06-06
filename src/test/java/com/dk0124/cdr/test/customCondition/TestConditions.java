package com.dk0124.cdr.test.customCondition;

import java.io.FileReader;


import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

public class TestConditions {

	/**
	 *  app 에서 테스트 시, 여기서 에러 난다면  pom.xml 의  BulkOperationTestOnModule false 로 변경.
	 * @return
	 */

	public static boolean doBulkTest() {
		try {
			MavenXpp3Reader reader = new MavenXpp3Reader();
			Model model = reader.read(new FileReader("pom.xml"));
			String testRequiredOnModule = model.getProperties().getProperty("BulkOperationTestOnModule");

			if(testRequiredOnModule != null && String.valueOf(testRequiredOnModule).equals("true"))
				return true;

			return false;
		} catch (Exception e) {
			return false;
		}
	}

}
