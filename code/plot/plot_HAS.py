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
import matplotlib

cmap = matplotlib.cm.get_cmap('plasma')



df = pd.read_csv('../../dataset/HAS/HAS_train_result.csv')
df1=df[df['validation_methode']=='cross Validation']
df1=df1.reset_index(drop=True)
print(df1.head(3))
df_decision_tree=df1[df1['classification_methode']=='Decision Tree']
df_random_forest=df_decision_tree.reset_index(drop=True)
print(df_decision_tree.head(3))

df_random_forest=df1[df1['classification_methode']=='Random Forest']
df_random_forest=df_random_forest.reset_index(drop=True)
print(df_random_forest.head(3))

df_naive_bayes=df1[df1['classification_methode']=='Naive Bayes']
df_naive_bayes=df_naive_bayes.reset_index(drop=True)
print(df_naive_bayes.head(3))

df_svm=df1[df1['classification_methode']=='SVM']
df_svm=df_svm.reset_index(drop=True)
print(df_svm.head(3))


initial=(df_decision_tree['F_mesure'].iloc[0],df_random_forest['F_mesure'].iloc[0],df_naive_bayes['F_mesure'].iloc[0],df_svm['F_mesure'].iloc[0])
print(initial)
RandomUnderSampler=(df_decision_tree['F_mesure'].iloc[1],df_random_forest['F_mesure'].iloc[1],df_naive_bayes['F_mesure'].iloc[1],df_svm['F_mesure'].iloc[1])
print(RandomUnderSampler)
AllKNN=(df_decision_tree['F_mesure'].iloc[2],df_random_forest['F_mesure'].iloc[2],df_naive_bayes['F_mesure'].iloc[2],df_svm['F_mesure'].iloc[2])
print(AllKNN)
InstanceHardnessThreshold=(df_decision_tree['F_mesure'].iloc[3],df_random_forest['F_mesure'].iloc[3],df_naive_bayes['F_mesure'].iloc[3],df_svm['F_mesure'].iloc[3])
print(InstanceHardnessThreshold)
NearMiss=(df_decision_tree['F_mesure'].iloc[4],df_random_forest['F_mesure'].iloc[4],df_naive_bayes['F_mesure'].iloc[4],df_svm['F_mesure'].iloc[4])
print(NearMiss)
OneSidedSelection=(df_decision_tree['F_mesure'].iloc[5],df_random_forest['F_mesure'].iloc[5],df_naive_bayes['F_mesure'].iloc[5],df_svm['F_mesure'].iloc[5])
print(OneSidedSelection)
RandomUnderSampler_default=(df_decision_tree['F_mesure'].iloc[6],df_random_forest['F_mesure'].iloc[6],df_naive_bayes['F_mesure'].iloc[6],df_svm['F_mesure'].iloc[6])
print(RandomUnderSampler_default)
TomekLinks=(df_decision_tree['F_mesure'].iloc[7],df_random_forest['F_mesure'].iloc[7],df_naive_bayes['F_mesure'].iloc[7],df_svm['F_mesure'].iloc[7])
print(TomekLinks)
CondensedNearestNeighbour=(df_decision_tree['F_mesure'].iloc[8],df_random_forest['F_mesure'].iloc[8],df_naive_bayes['F_mesure'].iloc[8],df_svm['F_mesure'].iloc[8])
print(CondensedNearestNeighbour)

import numpy as np
import matplotlib.pyplot as plt

men_means, men_std = (20, 35, 30, 35, 27), (2, 3, 4, 1, 2)
women_means, women_std = (25, 32, 34, 20, 25), (3, 5, 2, 3, 3)

ind = np.arange(len(RandomUnderSampler))  # the x locations for the groups
width = 0.1  # the width of the bars

#fig, ax = plt.subplots()
#fig = plt.figure()
fig=plt.figure(figsize=(10, 5))
ax = fig.add_subplot(111)
rects0 = ax.bar(ind , initial, width,
                color=cmap(0.1), label='Initial')
rects1 = ax.bar(ind + width, RandomUnderSampler, width,
                color=cmap(0.25), label='RandomUnderSampler')
rects2 = ax.bar(ind + width*2, AllKNN, width,
                color=cmap(0.4), label='AllKNN')
rects3 = ax.bar(ind + width*3, InstanceHardnessThreshold, width,
                color=cmap(0.55), label='InstanceHardnessThreshold')
rects4 = ax.bar(ind + width*4, NearMiss, width,
                color=cmap(0.7), label='NearMiss')
rects5 = ax.bar(ind + width*5, OneSidedSelection, width,
                color=cmap(0.8), label='OneSidedSelection')
rects6 = ax.bar(ind + width*6, TomekLinks, width,
                color=cmap(0.9), label='TomekLinks')
rects7 = ax.bar(ind + width*7, CondensedNearestNeighbour, width,
                color=cmap(1.0), label='CondensedNearestNeighbour')

# Add some text for labels, title and custom x-axis tick labels, etc.
ax.set_ylabel('F_mesure')
#ax.set_title('Résultat de classification  par dataset et par Algorithme du défaut de code HAS')
ax.set_xticks(ind+width*3.5)
ax.set_xticklabels(('Arbre de decision', 'Foret aléatoire', 'Naive bayes', 'SVM'))
ax.legend(loc='upper center', bbox_to_anchor=(0.5, -0.05), shadow=True, ncol=2)



plt.show()
































