package utils;

import org.apache.log4j.Level;
import spoon.reflect.code.CtNewClass;
import spoon.reflect.declaration.CtClass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.support.reflect.code.CtNewClassImpl;

public class SaverOfTheFile {


    public void reWriteFile(AbstractProcessor processor, CtClass element){

        try {

            ArrayList<String> classFile = new ArrayList<>();


            BufferedReader readFile = new BufferedReader(new FileReader(element.getPosition().getFile()));


            String line = "";
            while ((line = readFile.readLine()) != null) {

                classFile.add(line);
            }

            processor.getEnvironment().setAutoImports(true);
            String contenu=element.toString();
            BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(element.getPosition().getFile()));
            for (String s : classFile) {
                if(!s.matches(".*"+element.getSimpleName()+".*")) {
                    writer.write(s);
                    writer.newLine();
                }else {

                    writer.write(element+"");
                    writer.newLine();
                    writer.close();
                    processor.getEnvironment().report(processor, Level.WARN, element, "INFO :" + element.getReference());
                    return;

                }
            }




        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){

            System.out.println("Spoon can't refactor this "+processor.getClass()+"---"+element.getQualifiedName());

        }

    }


    public void reWriteFileInvocationImpl(AbstractProcessor processor, CtClass element){

        try {

            ArrayList<String> classFile = new ArrayList<>();


            BufferedReader readFile = new BufferedReader(new FileReader(element.getPosition().getFile()));


            String line = "";
            while ((line = readFile.readLine()) != null) {

                classFile.add(line);
            }

            processor.getEnvironment().setAutoImports(true);
            //String contenu=element.toString();

            BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(element.getPosition().getFile()));

            for (String s : classFile) {
                if(!s.matches(".*"+element.getSimpleName()+".*")) {
                    writer.write(s);
                    writer.newLine();
                }else {

                    writer.write(element+"");
                    writer.newLine();
                    writer.close();
                    processor.getEnvironment().report(processor, Level.WARN, element, "INFO :" + element.getReference());
                    return;

                }
            }




        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            System.out.println("Spoon can't refactor this "+processor.getClass()+"---"+element.getQualifiedName());

        }

    }


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

            CtClass theParentClass = getClassMere(element);

            if (theParentClass != null) {

                for (String s : classFile) {
                    if(!s.matches(".*"+theParentClass.getSimpleName()+".*")) {
                        writer.write(s);
                        writer.newLine();
                    }else {
                        writer.write(theParentClass+"");

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


    public CtClass getClassMere(CtMethod element){

        String theClassName=element.getParent(CtClass.class).getQualifiedName();
        String[] splitName = theClassName.split("\\$");

        theClassName= splitName[0];
        //System.out.println("hi here "+theClassName);
        CtClass classeMere=null;
        CtClass saveElement=element.getParent(CtClass.class);
        boolean sortir=true;
        if(saveElement!=null){
            while (sortir){
                if(saveElement!=null){
                    //System.out.println("hi hi here "+saveElement.getQualifiedName());
                    if(saveElement.getQualifiedName().equals(theClassName)){
                        sortir=false;
                        classeMere=saveElement;
                    }else{
                        saveElement=saveElement.getParent(CtClass.class);
                        //System.out.println("the element= "+saveElement);
                    }


                }
            }

        }

        return classeMere;
    }



}
