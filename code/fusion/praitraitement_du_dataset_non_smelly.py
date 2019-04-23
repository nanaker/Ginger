import pandas as pd


# #Defaut de code NMLR
#
# df1 = pd.read_csv('dataset_smelly/NLMR_c.csv')
# df = pd.read_csv('dataset_non_smelly/NLMR_.csv')
#
#
# print(df.describe())
# print(df.head(3))
# for index,row in df.iterrows():
#
#
#  if(df1['full_name'].str.contains(row.full_name).any()):
#    print('Found in '+row.full_name)
#    print('Found in index ' +str(index))
#    df.drop(index, inplace=True)
#    #print(df.describe())
#
# df.to_csv('dataset_non_smelly/NLMR_new.csv',index=False)
#
# ##Defaut de code LIC
#
# df1 = pd.read_csv('dataset_smelly/LIC_c.csv')
# df = pd.read_csv('dataset_non_smelly/LIC_.csv')
#
# # print(df.describe())
# # print(df.head(3))
# for index, row in df.iterrows():
#
#     if (df1['full_name'].str.contains(row.full_name).any()):
#         print('Found in ' + row.full_name)
#         print('Found in index ' + str(index))
#         df.drop(index, inplace=True)
#         # print(df.describe())
#
# df.to_csv('dataset_non_smelly/LIC_new.csv', index=False)


# ##Defaut de code HAS
#
# df1 = pd.read_csv('dataset/HAS/HAS_smelly_new.csv')
# df = pd.read_csv('dataset/HAS/HAS_non_smelly.csv')
#
# # print(df.describe())
# # print(df.head(3))
# for index, row in df.iterrows():
#
#     if (df1['full_name'].str.contains(row.full_name).any()):
#         print('Found in ' + row.full_name)
#         print('Found in index ' + str(index))
#         df.drop(index, inplace=True)
#         # print(df.describe())
#
# df.to_csv('dataset/HAS/HAS_non_smelly_new.csv', index=False)


# ##Defaut de code HBR
#
# df1 = pd.read_csv('dataset_smelly/HBR_c.csv')
# df = pd.read_csv('dataset_non_smelly/HBR_.csv')
#
# # print(df.describe())
# # print(df.head(3))
# for index, row in df.iterrows():
#
#     if (df1['full_name'].str.contains(row.full_name).any()):
#         print('Found in ' + row.full_name)
#         print('Found in index ' + str(index))
#         df.drop(index, inplace=True)
#         # print(df.describe())
#
# df.to_csv('dataset_non_smelly/HBR_new.csv', index=False)
#
#
##Defaut de code MIM

df1 = pd.read_csv('dataset/MIM/MIM_smelly_new.csv')
df = pd.read_csv('dataset/MIM/MIM_non_smelly.csv')

# print(df.describe())
# print(df.head(3))
for index, row in df.iterrows():

    if (df1['full_name'].str.contains(row.full_name).any()):
        print('Found in ' + row.full_name)
        print('Found in index ' + str(index))
        df.drop(index, inplace=True)
        # print(df.describe())

df.to_csv('dataset/MIM/MIM_non_smelly_new.csv', index=False)

#
#
# ##Defaut de code BLOB
#
# df1 = pd.read_csv('dataset/BLOB/BLOB_smelly_.csv')
# df = pd.read_csv('dataset/BLOB/BLOB_non_smelly.csv')
#
#
# for index, row in df.iterrows():
#
#     if (df1['full_name'].str.contains(row.full_name).any()):
#         print('Found in ' + row.full_name)
#         print('Found in index ' + str(index))
#         df.drop(index, inplace=True)
#
#
# df.to_csv('dataset/BLOB/BLOB_new.csv', index=False)