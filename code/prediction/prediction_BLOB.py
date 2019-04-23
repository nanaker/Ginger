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

# Read the model

file_name='models/model_BLOB.pickle'
lr_model=p.load(open(file_name, 'rb'))

data_full_name = pd.read_csv('dataset/BLOB/prediction/BLOB_prediction.csv')

data=pd.read_csv('dataset/BLOB/prediction/BLOB_prediction.csv')
data.drop('full_name', axis=1, inplace=True)

X = data.values
# prediction
predictions =lr_model.predict(X)




data_full_name["is_code_smell"]=predictions

#data2=data[(data["is_code_smell"] =="True")]
data2 = data_full_name.loc[data_full_name['is_code_smell'] == 1]
print(data2["is_code_smell"].describe())
data2.to_csv('dataset/BLOB/prediction_result2.csv', index=False)


























