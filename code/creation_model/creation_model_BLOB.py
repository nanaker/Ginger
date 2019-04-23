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

##BLOB

df = pd.read_csv('dataset/BLOB/taken/BLOB_del.csv')
#df = pd.read_csv('dataset/BLOB/taken/BLOB_NearMiss.csv')
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

file_name='models/model_BLOB.pickle'
p.dump(clf, open(file_name, 'wb'))




























