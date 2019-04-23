import pandas as pd
import numpy as np

from sklearn.model_selection import train_test_split
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.metrics import mean_squared_error,r2_score
from sklearn.model_selection import KFold
#Models
from sklearn import tree
from sklearn.ensemble import RandomForestClassifier
from sklearn.naive_bayes import GaussianNB
from sklearn import svm

from sklearn.metrics import confusion_matrix,recall_score,precision_recall_curve,auc,roc_curve,roc_auc_score,classification_report
import graphviz
from sklearn.externals.six import StringIO
import pydot

##LIC
datasetpath=["dataset/LIC/taken/LIC_.csv","dataset/LIC/taken/LIC_RandomUnderSampler.csv","dataset/LIC/taken/LIC_AllKNN.csv","dataset/LIC/taken/LIC_InstanceHardnessThreshold.csv","dataset/LIC/taken/LIC_NearMiss.csv","dataset/LIC/taken/LIC_OneSidedSelection.csv","dataset/LIC/taken/LIC_TomekLinks.csv","dataset/LIC/taken/LIC_CondensedNearestNeighbour.csv"]

#df = pd.read_csv('dataset/LIC/taken/LIC_del.csv')
#df = pd.read_csv('dataset/LIC/taken/LIC_RandomUnderSampler.csv')
#df = pd.read_csv('dataset/LIC/taken/LIC_AllKNN.csv')
#df = pd.read_csv('dataset/LIC/taken/LIC_CondensedNearestNeighbour.csv')
#df = pd.read_csv('dataset/LIC/taken/LIC_InstanceHardnessThreshold.csv')
#df = pd.read_csv('dataset/LIC/taken/LIC_NearMiss.csv')
#df = pd.read_csv('dataset/LIC/taken/LIC_OneSidedSelection.csv')
#df = pd.read_csv('dataset/LIC/taken/LIC_RandomUnderSampler_default.csv')
#df = pd.read_csv('dataset/LIC/taken/LIC_TomekLinks.csv')



result = pd.DataFrame(columns=['classification_methode', 'path', 'validation_methode','F_mesure'])
print(result)

for j in range(4):


 for path in datasetpath:

    #if((j==3)&(path=="dataset/LIC/taken/LIC_CondensedNearestNeighbour.csv")):
     #   continue

    print("\n\n")
    if (j == 0):
        print("--------Decision Tree------------")
        classification="Decision Tree"
    if (j == 1):
        print("--------Random Forest------------")
        classification = "Random Forest"
    if (j == 2):
        print("--------Naive Bayes------------")
        classification = "Naive Bayes"
    if (j == 3):
        print("--------SVM------------")
        classification = "SVM"

    print("\n\n")
    print(path)
    print("\n\n")
    df = pd.read_csv(path)
    test_methode="Validation_70/30"
    print(df['is_code_smell'].describe())

    #Construction du dataset
    Y = df.is_code_smell.values
    df.drop('is_code_smell', axis=1, inplace=True)
    X = df.values

    # creer les datastes d'entrainement et de test
    X_train, X_test, y_train, y_test = train_test_split(X, Y ,test_size=0.3)


    #creer le model d'entrainement en utilisant les arbres de decisions, foret aléatoire , Naive Bayes , SVM
    if(j==0): clf = tree.DecisionTreeClassifier()
    if (j == 1):   clf = RandomForestClassifier(n_estimators=100, max_depth=2, random_state=0)

    if (j == 2): clf=GaussianNB()
    if (j == 3): clf = svm.SVC(gamma='scale')

    #clf = RandomForestClassifier(n_estimators=100, max_depth=2, random_state=0)
    #clf=GaussianNB()
    #clf = svm.SVC(gamma='scale')


    lr_model = clf.fit(X_train, y_train)

    #Visualisation
   # tree.export_graphviz(lr_model, out_file="dataset/LIC/LIC_model_tree",
  #                   feature_names=list(df.columns),
   #                    class_names=['non_smelly','smelly'],
   #                    filled=True, rounded=True,
  #                     special_characters=True)


    # Test the model using the test set
    predictions = lr_model.predict(X_test)


   # Let's see the confusion matrix and evaluate the model
    cnf_matrix=confusion_matrix(y_test,predictions)


    print(cnf_matrix)
    if (cnf_matrix.size != 4):
        continue
    print("the recall for this model is :",cnf_matrix[1,1]/(cnf_matrix[1,1]+cnf_matrix[1,0]))

    fig= plt.figure(figsize=(6,3))# to plot the graph
    print("TP",cnf_matrix[1,1,]) # no of fraud transaction which are predicted fraud
    print("TN",cnf_matrix[0,0]) # no. of normal transaction which are predited normal
    print("FP",cnf_matrix[0,1]) # no of normal transaction which are predicted fraud
    print("FN",cnf_matrix[1,0]) # no of fraud Transaction which are predicted normal
    sns.heatmap(cnf_matrix,cmap="coolwarm_r",annot=True,linewidths=0.5)
    plt.title("Confusion_matrix")
    plt.xlabel("Predicted_class")
    plt.ylabel("Real class")
   # plt.show()
    print("\n----------Classification Report------------------------------------")
    print(classification_report(y_test,predictions))
    TP=cnf_matrix[1,1]
    TN=cnf_matrix[0,0]
    FP=cnf_matrix[0,1]
    FN=cnf_matrix[1,0]
    Precision=TP/(TP+FP)
    Rappel=TP/(TP+FN)
    F_mesure=2*Rappel*Precision/(Precision+Rappel)
    print("Precision = ",Precision)
    print("Rappel= ",Rappel)
    print("F_Mesure=",F_mesure)

    print(classification,path, test_methode, F_mesure)

    result=result.append(pd.Series([classification, path, test_methode, F_mesure], index=result.columns), ignore_index=True)




    print("\n----------Using cross Validation------------------------------------")
    test_methode = "cross Validation"
    kf = KFold(n_splits=5, shuffle=True)
    if (j == 0): clf = tree.DecisionTreeClassifier()
    if (j == 1):   clf = RandomForestClassifier(n_estimators=100, max_depth=2, random_state=0)

    if (j == 2): clf = GaussianNB()
    if (j == 3): clf = svm.SVC(gamma='scale')
    F_mesures=[]
    kfold = KFold(5, True, 1)
    df = pd.read_csv(path)
    k=1


    for train, test in kfold.split(X):
      try :
       print("\n----------k=", k, "------------------------------------\n")
       k=k+1
       x_train, x_test, y_train, y_test = X[train], X[test], Y[train], Y[test]

       model = clf.fit(x_train, y_train)
       predictions = model.predict(x_test)
       cnf_matrix = confusion_matrix(y_test, predictions)
       TP = cnf_matrix[1, 1]
       TN = cnf_matrix[0, 0]
       FP = cnf_matrix[0, 1]
       FN = cnf_matrix[1, 0]
       Precision = TP / (TP + FP)
       Rappel = TP / (TP + FN)
       F_mesure = 2 * Rappel * Precision / (Precision + Rappel)
       print("F_Mesure=", F_mesure)
       F_mesures.append(F_mesure)
      except:
          pass

    print("F_Mesures moyenne =", np.mean(F_mesures))

    result=result.append(pd.Series([classification, path, test_methode, np.mean(F_mesures)], index=result.columns), ignore_index=True)


result.to_csv('dataset/LIC/LIC_train_result2.csv', index=False)





























