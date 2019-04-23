import pandas as pd


# ##Défaut de code NLMR
# df = pd.read_csv('dataset/NLMR/NLMR_smelly.csv')
#
# df['has_onLowMemory'] = 'false'
# df['extend_class'] = 'false'
#
# df.rename(columns={'name': 'full_name'},inplace=True)
# print(df.head(3))
# columnsTitles = ['has_onLowMemory', 'extend_class','full_name']
#
# df = df.reindex(columns=columnsTitles)
# print(df.head(3))
# df.to_csv('dataset/NLMR/NLMR_smelly_new.csv',index=False)
# #
# ##Défaut de code LIC
# df = pd.read_csv('dataset/LIC/LIC_smelly.csv')
#
# df['is_static'] = 'false'
# df.rename(columns={'name': 'full_name'},inplace=True)
# print(df.head(3))
# columnsTitles = ['is_static','full_name']
#
# df = df.reindex(columns=columnsTitles)
# print(df.head(3))
# df.to_csv('dataset/LIC/LIC_smelly_new.csv',index=False)



# ##Défaut de code HAS
# df = pd.read_csv('dataset/HAS/HAS_smelly.csv')
#
#
#
# df['has_method'] = 'true'
# print(df.head(3))
# columnsTitles = ['cyclomatic_complexity', 'number_of_instructions','has_method', 'full_name']
#
# df = df.reindex(columns=columnsTitles)
# print(df.head(3))
# df.to_csv('dataset/HAS/HAS_smelly_new.csv',index=False)

##Défaut de code HBR
df = pd.read_csv('dataset/HBR/HBR_smelly.csv')


df['is_broadcast_receiver'] = 'true'
print(df.head(3))
df['has_methode_onReceive'] = 'true'
print(df.head(3))
columnsTitles = ['cyclomatic_complexity', 'number_of_instructions','has_methode_onReceive', 'full_name']

df = df.reindex(columns=columnsTitles)
print(df.head(3))
df.to_csv('dataset/HBR/HBR_smelly_new.csv',index=False)
#
#
# ##Défaut de code MIM
# df = pd.read_csv('dataset/MIM/MIM_smelly.csv')
#
# df['number_of_callers_not_null'] = 'true'
# df['is_init'] = 'false'
# df['is_static'] = 'false'
# df['is_override'] = 'false'
# df['uses_variables'] = 'false'
# df['call_external_methode'] = 'false'
#
# print(df.head(3))
# columnsTitles = ['number_of_callers_not_null', 'is_init','is_static','is_override','uses_variables','call_external_methode', 'full_name']
#
# df = df.reindex(columns=columnsTitles)
# print(df.head(3))
# df.to_csv('dataset/MIM/MIM_smelly_new.csv',index=False)
#
# ##Défaut de code BLOB
# df = pd.read_csv('dataset/BLOB/BLOB_smelly.csv')
#
# df.rename(columns={'name': 'full_name'},inplace=True)
# columnsTitles = ['lack_of_cohesion_in_methods','number_of_methods','number_of_attributes','full_name']
#
# df = df.reindex(columns=columnsTitles)
# print(df.head(3))
# df.to_csv('dataset/BLOB/BLOB_smelly_.csv',index=False)