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

    public static CtClass createVariable(AbstractProcessor processor, CtClass element){
        String variableName=element.getQualifiedName();
        String[] splitName = variableName.split("\\$");
        variableName = splitName[0] + "$" +
                ((CtNewClass) element.getParent()).getType().getQualifiedName() + splitName[1];
        variableName= variableName.substring(variableName.lastIndexOf("$")+1);
        variableName=Character.toLowerCase(variableName.charAt(0)) + variableName.substring(1);//la declaration d'une variable commence toujours par une miniscule

        CtTypeReference ref = processor.getFactory().Core().createTypeReference();//C'est le type de la nouvelle variable
        ref.setSimpleName(getType(element.getParent().toString()));

        ModifierKind[] k=new ModifierKind[3];
        k[0]=ModifierKind.PRIVATE;
        k[1]=ModifierKind.STATIC;
        k[2]=ModifierKind.FINAL;
        CtField variable=processor.getFactory().createCtField(variableName,ref,element.getParent().toString(),k);


        CtClass classeMere=element.getParent().getParent().getParent(CtClass.class);
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


}
