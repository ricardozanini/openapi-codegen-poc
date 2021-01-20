package com.github.zanini.openapi.codegen;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.google.common.collect.Maps;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.config.CodegenConfigurator;
import org.openapitools.codegen.config.GlobalSettings;

import static java.util.Objects.requireNonNull;

public class Main {

    public static void main(String[] args) throws IOException {
        // load our OpenAPI Spec File
        final File inputSpecFile = new File(requireNonNull(Main.class.getClassLoader().getResource("petstore.yaml")).getFile());
        // silly check
        if (!inputSpecFile.canRead()) {
            throw new IllegalArgumentException("Can't read file petstore.yaml from the classpath");
        }

        // load generator global properties
        final Properties globalProperties = new Properties();
        globalProperties.load(Main.class.getClassLoader().getResourceAsStream("generator-config.properties"));
        globalProperties.forEach((key, value) -> GlobalSettings.setProperty(key.toString(), value.toString()));

        // Getting the output dir
        final File output = new File(getTargetDir() + "/generated-sources/" + requireNonNull(globalProperties.getProperty("output")));

        // load our specific configurations
        final CodegenConfigurator configurator =
                CodegenConfigurator.fromFile(requireNonNull(Main.class.getClassLoader().getResource("java-generator-config.yaml")).getFile());
        configurator.setGlobalProperties(Maps.fromProperties(globalProperties));
        configurator.setGeneratorName(globalProperties.getProperty("generatorName"));
        configurator.setLibrary(globalProperties.getProperty("library"));
        configurator.setApiPackage(globalProperties.getProperty("apiPackage"));
        configurator.setModelPackage(globalProperties.getProperty("modelPackage"));
        configurator.setInvokerPackage(globalProperties.getProperty("invokerPackage"));
        configurator.setOutputDir(output.getPath());

        // set our input, which is our spec file!
        configurator.setInputSpec(inputSpecFile.getPath());
        // convert the configuration to a valid type, and convert it!
        // check your output :)
        new DefaultGenerator().opts(configurator.toClientOptInput()).generate();
    }

    private static File getTargetDir() {
        final File targetDir = new File(requireNonNull(Main.class.getClassLoader().getResource("")).getPath());

        return targetDir.getParentFile();
    }
}
