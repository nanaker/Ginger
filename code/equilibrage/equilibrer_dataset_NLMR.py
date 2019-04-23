import pandas as pd
from imblearn.under_sampling import RandomUnderSampler
from imblearn.under_sampling import TomekLinks # did not worked cause it require a float not a string for the column full_name
from imblearn.under_sampling import CondensedNearestNeighbour # did not worked cause it require a float not a string for the column full_name
from imblearn.under_sampling import AllKNN
from imblearn.under_sampling import InstanceHardnessThreshold
from imblearn.under_sampling import NearMiss
from imblearn.under_sampling import OneSidedSelection
from imblearn.under_sampling import ClusterCentroids
from imblearn.over_sampling import RandomOverSampler
from imblearn.over_sampling import ADASYN
from imblearn.combine import SMOTEENN


##NLMR

df = pd.read_csv('../../dataset/NLMR/NLMR_.csv')
print(df['is_code_smell'].describe())

Y = df.is_code_smell.values
df.drop('is_code_smell', axis=1, inplace=True)
X = df.values






#RandomUnderSampler by default
rus = RandomUnderSampler(return_indices=True,random_state=0)
X_resampled, y_resampled, idx_resampled = rus.fit_sample(X, Y)
X_resampled = pd.DataFrame(X_resampled)
X_resampled.columns = ['has_onLowMemory','extend_class']
y_resampled = pd.DataFrame(y_resampled)
y_resampled.columns = ['is_code_smell']
undersampled_data = pd.concat([X_resampled, y_resampled], axis=1)
print("RandomUnderSampler by default")
print(undersampled_data.describe())
undersampled_data.to_csv('../../dataset/NLMR/NLMR_RandomUnderSampler.csv', index=False)


#
#RandomUnderSampler  with parameter random_state=0 ( a random number it garantee that we get the same result each time we run this code )
rus = RandomUnderSampler(return_indices=True,random_state=0)
X_resampled, y_resampled, idx_resampled = rus.fit_sample(X, Y)
X_resampled = pd.DataFrame(X_resampled)
X_resampled.columns = ['is_activity','has_onLowMemory']
y_resampled = pd.DataFrame(y_resampled)
y_resampled.columns = ['is_code_smell']
undersampled_data = pd.concat([X_resampled, y_resampled], axis=1)
print("RandomUnderSampler")
print(undersampled_data.describe())
undersampled_data.to_csv('../../dataset/NLMR/NLMR_RandomUnderSampler.csv', index=False)




#AllKNN --non efficace il a garder les memes instances
rus = AllKNN(return_indices=True)
X_resampled, y_resampled, idx_resampled = rus.fit_sample(X, Y)
X_resampled = pd.DataFrame(X_resampled)
X_resampled.columns = ['is_activity','has_onLowMemory']

y_resampled = pd.DataFrame(y_resampled)
y_resampled.columns = ['is_code_smell']
undersampled_data = pd.concat([X_resampled, y_resampled], axis=1)
print("AllKNN")
print(undersampled_data.describe())
undersampled_data.to_csv('../../dataset/NLMR/NLMR_AllKNN.csv', index=False)

#CondensedNearestNeighbour non efficace il a garder une seule instance de la classe non defaut de code
rus = CondensedNearestNeighbour(return_indices=True)
X_resampled, y_resampled, idx_resampled = rus.fit_sample(X, Y)
X_resampled = pd.DataFrame(X_resampled)
X_resampled.columns = ['is_activity','has_onLowMemory']
y_resampled = pd.DataFrame(y_resampled)
y_resampled.columns = ['is_code_smell']
undersampled_data = pd.concat([X_resampled, y_resampled], axis=1)
print("CondensedNearestNeighbour")
print(undersampled_data.describe())
undersampled_data.to_csv('../../dataset/NLMR/NLMR_CondensedNearestNeighbour.csv', index=False)
#
#
#
#TomekLinks non efficace il a garder les memes instances
rus = TomekLinks(return_indices=True)
X_resampled, y_resampled, idx_resampled = rus.fit_sample(X, Y)
X_resampled = pd.DataFrame(X_resampled)
X_resampled.columns = ['is_activity','has_onLowMemory']
y_resampled = pd.DataFrame(y_resampled)
y_resampled.columns = ['is_code_smell']
undersampled_data = pd.concat([X_resampled, y_resampled], axis=1)
print("TomekLinks")
print(undersampled_data.describe())
undersampled_data.to_csv('../../dataset/NLMR/NLMR_TomekLinks.csv', index=False)



#InstanceHardnessThreshold non efficace il a garder les memes instances
rus = InstanceHardnessThreshold(return_indices=True)
X_resampled, y_resampled, idx_resampled = rus.fit_sample(X, Y)
X_resampled = pd.DataFrame(X_resampled)
X_resampled.columns = ['is_activity','has_onLowMemory']
y_resampled = pd.DataFrame(y_resampled)
y_resampled.columns = ['is_code_smell']
undersampled_data = pd.concat([X_resampled, y_resampled], axis=1)
print("InstanceHardnessThreshold")
print(undersampled_data.describe())
undersampled_data.to_csv('../../dataset/NLMR/NLMR_InstanceHardnessThreshold.csv', index=False)


NearMiss
rus = NearMiss(return_indices=True)
X_resampled, y_resampled, idx_resampled = rus.fit_sample(X, Y)
X_resampled = pd.DataFrame(X_resampled)
X_resampled.columns = ['is_activity','has_onLowMemory']
y_resampled = pd.DataFrame(y_resampled)
y_resampled.columns = ['is_code_smell']
undersampled_data = pd.concat([X_resampled, y_resampled], axis=1)
print("NearMiss")
print(undersampled_data.describe())
undersampled_data.to_csv('../../dataset/NLMR/NLMR_NearMiss.csv', index=False)



#OneSidedSelection
rus = OneSidedSelection(return_indices=True)
X_resampled, y_resampled, idx_resampled = rus.fit_sample(X, Y)
X_resampled = pd.DataFrame(X_resampled)
X_resampled.columns = ['is_activity','has_onLowMemory']
y_resampled = pd.DataFrame(y_resampled)
y_resampled.columns = ['is_code_smell']
undersampled_data = pd.concat([X_resampled, y_resampled], axis=1)
print("OneSidedSelection")
print(undersampled_data.describe())
undersampled_data.to_csv('../../dataset/NLMR/NLMR_OneSidedSelection.csv', index=False)








