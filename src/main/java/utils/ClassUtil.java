package utils;


import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtNewClass;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.reflect.code.CtConstructorCallImpl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//this is a technical class, in order to not repeat codes
public class ClassUtil {

    public static CtMethod addComment(AbstractProcessor processor, CtMethod element, String commentContent){
        CtComment comment=processor.getFactory().Core().createComment().setContent(commentContent).setCommentType(CtComment.CommentType.BLOCK);
        //Remove old comments
        List<CtComment> listOfComments=element.getElements(new TypeFilter(CtComment.class));
        boolean var=false;
        for(CtComment c:listOfComments){

            if(c.getContent().equals(comment.getContent())){
                var=true;
                break;

            }
        }
        if(!var){
            //add the new comment to the method
            element.getBody().insertEnd(comment);
        }

        return element;
    }

    public static CtClass createVariable(AbstractProcessor processor, CtClass element, String variableName){

        CtTypeReference ref = processor.getFactory().Core().createTypeReference();//C'est le type de la nouvelle variable
        ref.setSimpleName(getType(element.getParent().toString()));

        ModifierKind[] k=new ModifierKind[3];
        k[0]=ModifierKind.PRIVATE;
        k[1]=ModifierKind.STATIC;
        k[2]=ModifierKind.FINAL;
        CtField variable=processor.getFactory().createCtField(variableName,ref,element.getParent().toString(),k);


        //CtClass classeMere=element.getParent().getParent().getParent(CtClass.class);
        CtClass classeMere=getClassMere(element);
        classeMere.addFieldAtTop(variable);//on declare la nouvelle variable en haut de la classe



        return  classeMere;
    }

    public static String getType(String text){
        String pattern1 = "new ";
        String pattern2 = "()";


        Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
        Matcher m = p.matcher(text);
        if (m.find()) {

            return m.group(1);
        }

        return null;
    }



    public static  CtClass getClassMere(CtClass element){
        String theClassName=element.getQualifiedName();
        String[] splitName = theClassName.split("\\$");

        theClassName= splitName[0];

        CtClass classeMere=null;
        CtClass saveElement=element;
        boolean sortir=true;
        if(saveElement!=null){
            while (sortir){
                if(saveElement!=null){

                    if(saveElement.getQualifiedName().equals(theClassName)){
                        sortir=false;
                        classeMere=saveElement;
                    }else{
                        saveElement=saveElement.getParent(CtClass.class);

                    }


                }
            }

        }

        return classeMere;
    }

}