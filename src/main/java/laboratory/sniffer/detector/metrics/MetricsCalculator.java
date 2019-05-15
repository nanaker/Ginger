package laboratory.sniffer.detector.metrics;
import laboratory.sniffer.detector.entities.DetectorClass;
import laboratory.sniffer.detector.entities.Entity;
import laboratory.sniffer.detector.entities.DetectorApp;
import laboratory.sniffer.detector.entities.DetectorMethod;
import laboratory.sniffer.detector.entities.DetectorVariable;
import spoon.reflect.declaration.CtClass;

import java.util.HashMap;


public class MetricsCalculator {



        public static void calculateAppMetrics(DetectorApp app)
        {
            NumberOfClasses.createNumberOfClasses(app, app.getDetectorClasses().size());
            int numberOfInterfaces=0;
            int numberOfContentProviders =0 ;
            int numberOfAsyncTasks =0;
            int numberOfInnerClasses =0;
            int numberOfBroadcastReceivers =0;
            int numberOfMethods =0 ;
            int numberOfServices =0;
            int numberOfViews =0;
            int numberOfActivities =0;
            int numberOfVariables = 0;


            for(DetectorClass c: app.getDetectorClasses()){
                if(c.isInterface()){
                    numberOfInterfaces++;
                }
                if(c.isInnerClass()){
                    numberOfInnerClasses++;
                }
                if (c.isActivity()){
                    numberOfActivities++;
                }else if (c.isBroadcastReceiver()){
                    numberOfBroadcastReceivers++;
                }else if (c.isContentProvider()){
                    numberOfContentProviders++;
                }else if (c.isService()){
                    numberOfServices++;
                }else if (c.isView()){
                    numberOfViews++;
                }else if (c.isAsyncTask()){
                    numberOfAsyncTasks++;
                }
                numberOfVariables += c.getDetectorVariables().size();
                numberOfMethods += c.getDetectorMethods().size();
            }
            NumberOfInterfaces.createNumberOfInterfaces(app,numberOfInterfaces);
            NumberOfActivities.createNumberOfActivities(app,numberOfActivities);
            NumberOfMethods.createNumberOfMethods(app,numberOfMethods);
            NumberOfViews.createNumberOfViews(app,numberOfViews);
            NumberOfServices.createNumberOfServices(app,numberOfServices);
            NumberOfBroadcastReceivers.createNumberOfBroadcastReceivers(app,numberOfBroadcastReceivers);
            NumberOfInnerClasses.createNumberOfInnerClasses(app,numberOfInnerClasses);
            NumberOfAsyncTasks.createNumberOfAsyncTasks(app,numberOfAsyncTasks);
            NumberOfContentProviders.createNumberOfContentProviders(app,numberOfContentProviders);
            NumberOfVariables.createNumberOfVariables(app, numberOfVariables);
            for(DetectorClass detectorClass : app.getDetectorClasses()){
                calculateClassMetrics(detectorClass);
            }
            calculateGraphMetrics(app);
        }


        public static void calculateClassMetrics(DetectorClass detectorClass){
            if(detectorClass.isInterface()){
                IsInterface.createIsInterface(detectorClass,true);
            }
            if(detectorClass.isInnerClass()){
                IsInnerClass.createIsInnerClass(detectorClass,true);
            }
            if (detectorClass.isActivity()){
                IsActivity.createIsActivity(detectorClass,true);
            }else if (detectorClass.isBroadcastReceiver()){
                IsBroadcastReceiver.createIsBroadcastReceiver(detectorClass,true);
            }else if (detectorClass.isContentProvider()){
                IsContentProvider.createIsContentProvider(detectorClass,true);
            }else if (detectorClass.isService()){
                IsService.createIsService(detectorClass,true);
            }else if (detectorClass.isView()){
                IsView.createIsView(detectorClass,true);
            }else if (detectorClass.isAsyncTask()){
                IsAsyncTask.createIsAsyncTask(detectorClass,true);
            }else if (detectorClass.isApplication()){
                IsApplication.createIsApplication(detectorClass,true);
            }
            NumberOfAttributes.createNumberOfAttributes(detectorClass, detectorClass.getDetectorVariables().size());
            NumberOfMethods.createNumberOfMethods(detectorClass, detectorClass.getDetectorMethods().size());
            NumberOfImplementedInterfaces.createNumberOfImplementedInterfaces(detectorClass,
                    detectorClass.getInterfacesNames().size());
            CouplingBetweenObjects.createCouplingBetweenObjects(detectorClass);
            DepthOfInheritance.createDepthOfInheritance(detectorClass, detectorClass.getDepthOfInheritance());
            LackofCohesionInMethods.createLackofCohesionInMethods(detectorClass);
            ClassComplexity.createClassComplexity(detectorClass);
            NPathComplexity.createNPathComplexity(detectorClass);
            if(detectorClass.isStatic())
            {
                IsStatic.createIsStatic(detectorClass,true);
            }
            if(detectorClass.getClasse().isEnum()){
                IsEnum.createIsEnum(detectorClass,true);
            }
            else IsEnum.createIsEnum(detectorClass,false);

            if(!(detectorClass.getClasse().isAnonymous())&&(detectorClass.isInnerClass())){


                if(!(detectorClass.getClasse().getParent() instanceof CtClass)){
                    IsLocalClass.createisLocalClass(detectorClass,true);

                }
                else IsLocalClass.createisLocalClass(detectorClass,false);
            }
            else IsLocalClass.createisLocalClass(detectorClass,false);
            NumberOfChildren.createNumberOfChildren(detectorClass);
            for(DetectorMethod detectorMethod : detectorClass.getDetectorMethods()){
                calculateMethodMetrics(detectorMethod);
            }

            for (DetectorVariable detectorVariable : detectorClass.getDetectorVariables()){
                if(detectorVariable.isStatic()){
                    IsStatic.createIsStatic(detectorVariable,true);
                }
            }

        }

        public static void calculateMethodMetrics(DetectorMethod detectorMethod){
            NumberOfParameters.createNumberOfParameters(detectorMethod, detectorMethod.getArguments().size());
            NumberOfDirectCalls.createNumberOfDirectCalls(detectorMethod, detectorMethod.getCalledMethods().size());
            if(detectorMethod.isConstructor()){
                IsInit.createIsInit(detectorMethod,true);
            }else if(detectorMethod.isGetter()){
                IsGetter.createIsGetter(detectorMethod,true);
            }else if(detectorMethod.isSetter()){
                IsSetter.createIsSetter(detectorMethod,true);
            }
            if(detectorMethod.isStatic()){
                IsStatic.createIsStatic(detectorMethod,true);
            }
            if(detectorMethod.isOverride()){
                IsOverride.createIsOverride(detectorMethod, true);
            }
            NumberOfLines.createNumberOfLines(detectorMethod, detectorMethod.getNumberOfLines());
            CyclomaticComplexity.createCyclomaticComplexity(detectorMethod, detectorMethod.getComplexity());
        }

        private static void calculateGraphMetrics(DetectorApp app){
            HashMap<DetectorMethod, Integer> numberOfCallers = new HashMap<>();
            Integer nb;
            for(DetectorClass detectorClass : app.getDetectorClasses()){
                for (DetectorMethod detectorMethod : detectorClass.getDetectorMethods()){
                    if(!numberOfCallers.containsKey(detectorMethod)){
                        numberOfCallers.put(detectorMethod,0);
                    }
                    for(Entity entity: detectorMethod.getCalledMethods()){
                        if(entity instanceof DetectorMethod){
                            nb=numberOfCallers.get((DetectorMethod)entity);
                            if(nb==null){
                                numberOfCallers.put((DetectorMethod)entity,1);
                            }else{
                                numberOfCallers.put((DetectorMethod)entity,nb+1);
                            }
                        }
                    }
                }
            }
            for(DetectorClass detectorClass :app.getDetectorClasses()){
                //compute the number of callers
                for(DetectorMethod detectorMethod : detectorClass.getDetectorMethods()){
                    NumberOfCallers.createNumberOfCallers(detectorMethod,numberOfCallers.get(detectorMethod));
                }
            }
        }

}
