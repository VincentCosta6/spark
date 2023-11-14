package com.github.spark.lib.commands.annotations;

import com.github.spark.lib.commands.annotations.CommandHandler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes("CommandHandler")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class CommandProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        StringBuilder yaml = new StringBuilder("commands:\n");

        for (Element typeElement : roundEnv.getElementsAnnotatedWith(CommandHandler.class)) {
            if (typeElement instanceof TypeElement) {
                CommandHandler command = typeElement.getAnnotation(CommandHandler.class);
                yaml.append("  - name: ").append(command.name()).append("\n");
                yaml.append("    description: ").append(command.description()).append("\n");
                yaml.append("    subcommands:\n");

                // Fetch methods annotated inside the class
                for (Element enclosedElement : typeElement.getEnclosedElements()) {
                    CommandHandler subCommand = enclosedElement.getAnnotation(CommandHandler.class);
                    if (subCommand != null) {
                        yaml.append("      - name: ").append(subCommand.name()).append("\n");
                        yaml.append("        description: ").append(subCommand.description()).append("\n");
                    }
                }
            }
        }

        try {
            FileObject file = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT,
                    "", "commands.yml");
            try (Writer writer = file.openWriter()) {
                writer.write(yaml.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
