import pandas as pd


from sklearn import tree
import pickle as p


##HAS


#df = pd.read_csv('dataset/HAS/taken/HAS_del.csv')
data = pd.read_csv('dataset/HAS/prediction/HAS_prediction.csv')

# load model
file_name='models/model_HAS.pickle'
model = p.load(open(file_name, 'rb'))

# prediction
predictions =model.predict(data)

print(data)




























