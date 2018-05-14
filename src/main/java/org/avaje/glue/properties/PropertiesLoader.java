package org.avaje.glue.properties;

import java.util.Enumeration;
import java.util.Properties;

/**
 * Loads and evaluates properties and yml configuration.
 */
public class PropertiesLoader {

  private static Properties properties;

  /**
   * Provides properties by reading known locations.
   * <p>
   * <h3>Main configuration</h3>
   * <p>
   * <p>Firstly loads from main resources</p>
   * <pre>
   *   - application.properties
   *   - application.yml
   * </pre>
   * <p>
   * <p>Then loads from local files</p>
   * <pre>
   *   - application.properties
   *   - application.yml
   * </pre>
   * <p>
   * <p>Then loads from environment variable <em>PROPS_FILE</em></p>
   * <p>Then loads from system property <em>props.file</em></p>
   * <p>Then loads from <em>load.properties</em></p>
   * <p>
   * <h3>Test configuration</h3>
   * <p>
   * Once the main configuration is read it will try to read common test configuration.
   * This will only be successful if the test resources are available (i.e. running tests).
   * </p>
   * <p>Loads from test resources</p>
   * <pre>
   *   - application-test.properties
   *   - application-test.yml
   * </pre>
   */
  public static synchronized Properties load(String[] args) {

    if (properties == null) {
      Loader loader = new Loader();
      loader.load(args);
      properties = loader.eval();
    }

    return properties;
  }

  /**
   * Load the properties (if not already loaded) without using any command line args to pass properties file(s).
   */
  public static synchronized Properties load() {
    return load(null);
  }

  /**
   * Return a copy of the properties with 'eval' run on all the values.
   * This resolves expressions like ${HOME} etc.
   */
  public static Properties eval(Properties properties) {

    Properties evalCopy = new Properties();

    Enumeration<?> names = properties.propertyNames();
    while (names.hasMoreElements()) {
      String name = (String) names.nextElement();
      String value = PropertyEval.eval(properties.getProperty(name));
      evalCopy.setProperty(name, value);
    }
    return evalCopy;
  }
}
