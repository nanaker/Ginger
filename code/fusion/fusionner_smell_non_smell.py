import pandas as pd
import numpy as np

# ##BLOB
# df1 = pd.read_csv('dataset/BLOB/BLOB_smelly_.csv')
# df2 = pd.read_csv('dataset/BLOB/BLOB_new.csv')
# df1['is_code_smell'] = 'true'
# df2['is_code_smell'] = 'false'
# df = pd.concat([df1,df2], ignore_index=True)
# print(df.describe())
# idx = np.random.permutation(df.index)
# df.reindex(idx)
# df.to_csv('dataset/BLOB/BLOB.csv',index=False)
# ##Supprimer la colonne Full name
# df.drop('full_name', axis=1, inplace=True)
# df.to_csv('dataset/BLOB/BLOB_.csv', index=False)

# ##HAS
# df1 = pd.read_csv('dataset/HAS/HAS_smelly_new.csv')
# df2 = pd.read_csv('dataset/HAS/HAS_non_smelly_new.csv')
# df1['is_code_smell'] = 'true'
# df2['is_code_smell'] = 'false'
# df = pd.concat([df1,df2], ignore_index=True)
# print(df.describe())
# idx = np.random.permutation(df.index)
# df.reindex(idx)
# df.to_csv('dataset/HAS/HAS.csv',index=False)
# ##Supprimer la colonne Full name
# df.drop('full_name', axis=1, inplace=True)
# df.to_csv('dataset/HAS/HAS_.csv', index=False)


# ##NLMR
# df1 = pd.read_csv('dataset/NLMR/NLMR_smelly_new.csv')
# df2 = pd.read_csv('dataset/NLMR/NLMR_non_smelly_new.csv')
# df1['is_code_smell'] = 'true'
# df2['is_code_smell'] = 'false'
# df = pd.concat([df1,df2], ignore_index=True)
# print(df.describe())
# idx = np.random.permutation(df.index)
# df.reindex(idx)
# df.to_csv('dataset/NLMR/NLMR.csv',index=False)
# ##Supprimer la colonne Full name
# df.drop('full_name', axis=1, inplace=True)
# df.to_csv('dataset/NLMR/NLMR_.csv', index=False)
# #
# #
# ##LIC
# df1 = pd.read_csv('dataset/LIC/LIC_smelly_new.csv')
# df2 = pd.read_csv('dataset/LIC/LIC_non_smelly_new.csv')
# df1['is_code_smell'] = 'true'
# df2['is_code_smell'] = 'false'
# df = pd.concat([df1,df2], ignore_index=True)
# print(df.describe())
# idx = np.random.permutation(df.index)
# df.reindex(idx)
# df.to_csv('dataset/LIC/LIC.csv',index=False)
# ##Supprimer la colonne Full name
# df.drop('full_name', axis=1, inplace=True)
# df.to_csv('dataset/LIC/LIC_.csv', index=False)
#

##HBR
df1 = pd.read_csv('dataset/HBR/HBR_smelly_new.csv')
df2 = pd.read_csv('dataset/HBR/HBR_non_smelly_new.csv')
df1['is_code_smell'] = 'true'
df2['is_code_smell'] = 'false'
df = pd.concat([df1,df2], ignore_index=True)
print(df.describe())
idx = np.random.permutation(df.index)
df.reindex(idx)
df.to_csv('dataset/HBR/HBR.csv',index=False)
##Supprimer la colonne Full name
df.drop('full_name', axis=1, inplace=True)
df.to_csv('dataset/HBR/HBR_.csv', index=False)
#
#
#
#
# ##MIM
# df1 = pd.read_csv('dataset/MIM/MIM_smelly_new.csv')
# df2 = pd.read_csv('dataset/MIM/MIM_non_smelly_new.csv')
# df1['is_code_smell'] = 'true'
# df2['is_code_smell'] = 'false'
# df = pd.concat([df1,df2], ignore_index=True)
# print(df.describe())
# idx = np.random.permutation(df.index)
# df.reindex(idx)
# df.to_csv('dataset/MIM/MIM.csv',index=False)
# ##Supprimer la colonne Full name
# df.drop('full_name', axis=1, inplace=True)
# df.to_csv('dataset/MIM/MIM_.csv', index=False)
