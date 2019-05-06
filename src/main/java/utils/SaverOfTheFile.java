package utils;

import org.apache.log4j.Level;
import spoon.reflect.declaration.CtClass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtMethod;

public class SaverOfTheFile {


        public void reWriteFile(AbstractProcessor processor, CtMethod element){
            try {
                ArrayList<String> classFile = new ArrayList<>();


                BufferedReader readFile = new BufferedReader(new FileReader(element.getPosition().getFile()));
                String line = "";
                while ((line = readFile.readLine()) != null) {

                    classFile.add(line);
                }
                processor.getEnvironment().setAutoImports(true);

                BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(element.getPosition().getFile()));

                CtClass theParentClass = null;
                if (element.getParent() instanceof CtClass) {
                    theParentClass = (CtClass) element.getParent();

                }
                if (theParentClass != null) {

                    for (String s : classFile) {
                        if(!s.matches(".*"+theParentClass.getSimpleName()+".*")) {
                            writer.write(s);
                            writer.newLine();
                        }else {
                            writer.write(element.getParent()+"");

                            writer.newLine();
                            writer.close();
                            processor.getEnvironment().report(processor, Level.WARN, element, "INFO :" + element.getReference());
                            return;

                        }
                    }


                }

            }catch (IOException e) {
                e.printStackTrace();
            }

        }

}