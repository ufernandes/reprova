package br.ufmg.engsoft.reprova.tests.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Field;


public class EnvironmentUtils {
	public static void setEnv(Map<String, String> newenv) throws Exception {
	  try {
	    Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
	    Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
	    theEnvironmentField.setAccessible(true);
	    Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
	    env.putAll(newenv);
	    Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
	    theCaseInsensitiveEnvironmentField.setAccessible(true);
	    Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
	    cienv.putAll(newenv);
	  } catch (NoSuchFieldException e) {
	    Class[] classes = Collections.class.getDeclaredClasses();
	    Map<String, String> env = System.getenv();
	    for(Class cl : classes) {
	      if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
	        Field field = cl.getDeclaredField("m");
	        field.setAccessible(true);
	        Object obj = field.get(env);
	        Map<String, String> map = (Map<String, String>) obj;
	        map.clear();
	        map.putAll(newenv);
	      }
	    }
	  }
	}
	
	public static void clearEnv() throws Exception {
var envVar = new HashMap<String, String>();
  	
  	envVar.put("PORT", "8888");
		envVar.put("ENABLE_STATISTICS", "false");
		envVar.put("ENABLE_ESTIMATED_TIME", "false");
		envVar.put("DIFFICULTY_GROUP", "0");
		envVar.put("ENABLE_MULTIPLE_CHOICE", "false");
  	
  	setEnv(envVar);
	}
	
	public static void setEnvVariables(boolean setOptionalVariables, int difficultyGroup) throws Exception {
		var envVar = new HashMap<String, String>();
  	
  	envVar.put("PORT", "8888");
  	
  	if (setOptionalVariables) {
  		envVar.put("ENABLE_STATISTICS", "true");
  		envVar.put("ENABLE_ESTIMATED_TIME", "true");
  		envVar.put("DIFFICULTY_GROUP", "" + difficultyGroup);
  		envVar.put("ENABLE_MULTIPLE_CHOICE", "true");
  	}
  	
  	setEnv(envVar);
	}
}
