import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
import matplotlib.pyplot as plt
import seaborn as sns
#Models
from sklearn import tree
import pickle as p
from sklearn.ensemble import RandomForestClassifier
from sklearn.naive_bayes import GaussianNB
from sklearn import svm

from sklearn.metrics import confusion_matrix,recall_score,precision_recall_curve,auc,roc_curve,roc_auc_score,classification_report
import graphviz
from sklearn.externals.six import StringIO
import pydot

##HAS

#df = pd.read_csv('dataset/HAS/taken/HAS_del.csv')

datasetpath=["dataset/HAS/taken/HAS_.csv","dataset/HAS/taken/HAS_RandomUnderSampler.csv"
    ,"dataset/HAS/taken/HAS_AllKNN.csv",
          #   "dataset/HAS/taken/HAS_InstanceHardnessThreshold.csv",
             "dataset/HAS/taken/HAS_NearMiss.csv",
             "dataset/HAS/taken/HAS_OneSidedSelection.csv",
             "dataset/HAS/taken/HAS_RandomUnderSampler_default.csv",
             "dataset/HAS/taken/HAS_TomekLinks.csv"
          #   ,"dataset/HAS/taken/HAS_CondensedNearestNeighbour.csv"
             ]

df = pd.read_csv(datasetpath[8])
idx = np.random.permutation(df.index)
df.reindex(idx)
print(df['is_code_smell'].describe())

#Construction du dataset
Y = df.is_code_smell.values
df.drop('is_code_smell', axis=1, inplace=True)
X = df.values



#creer le model d'entrainement en utilisant les arbres de decisions, foret aléatoire , Naive Bayes , SVM 

clf = tree.DecisionTreeClassifier()
#clf = RandomForestClassifier(n_estimators=100, max_depth=2, random_state=0)
#clf=GaussianNB()
#clf = svm.SVC(gamma='scale')


lr_model = clf.fit(X, Y)

file_name='models/model_HAS.pickle'
p.dump(clf, open(file_name, 'wb'))




























