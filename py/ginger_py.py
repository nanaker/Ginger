import os
import pandas as pd
from sklearn import tree
import pickle as p
import sys, getopt

def create(argv):


   base_path = ''

   try:
       opts, args = getopt.getopt(argv, "hb:", ["bpath="])
   except getopt.GetoptError:
       print('test.py predict -b <base_path> ')
       sys.exit(2)
   for opt, arg in opts:
       if opt == '-h':
           print('test.py -b <base_path> ')
           sys.exit()
       elif opt in ("-b", "--bpath"):
           base_path = arg
   dataset_path = os.path.join(base_path, 'dataset')
   model_path = os.path.join(base_path, 'models')

   

   HBR_dataset_path=os.path.join(dataset_path, 'HBR_dataset.csv')
   
   HAS_dataset_path= os.path.join(dataset_path, 'HAS_dataset.csv')
   
   LIC_dataset_path= os.path.join(dataset_path, 'LIC_dataset.csv')
   
   MIM_dataset_path= os.path.join(dataset_path, 'MIM_dataset.csv')
   
   NLMR_dataset_path= os.path.join(dataset_path, 'NLMR_dataset.csv')
   
   #if the file containing dataset is not empty then create the model
   #HBR
   try:
       size = os.stat(HBR_dataset_path).st_size
   except FileNotFoundError:
       print('  File or file path not found ', HBR_dataset_path)
       sys.exit(2)
   if (size != 0):
    try:
        df = pd.read_csv(HBR_dataset_path)
    except FileNotFoundError:
        print('  File or file path not found ',HBR_dataset_path)
        sys.exit(2)

    print(df['is_code_smell'].describe())
    # Construction du dataset
    Y = df.is_code_smell.values
    df.drop('is_code_smell', axis=1, inplace=True)
    X = df.values
    # creer le model d'entrainement en utilisant les arbres de decisions
    clf = tree.DecisionTreeClassifier()
    lr_model = clf.fit(X, Y)
    file_name = os.path.join(model_path,"model_HBR.pickle")

    p.dump(lr_model, open(file_name, 'wb'))
    # if the file containing dataset is not empty then create the model
   try:
       size = os.stat(HAS_dataset_path).st_size
   except FileNotFoundError:
       print('  File or file path not found ', HAS_dataset_path)
       sys.exit(2)
   if (size != 0):
     try:
           df = pd.read_csv(HAS_dataset_path)
     except FileNotFoundError:
           print('  File or file path not found ', HAS_dataset_path)
           sys.exit(2)

     print(df['is_code_smell'].describe())
     # Construction du dataset
     Y = df.is_code_smell.values
     df.drop('is_code_smell', axis=1, inplace=True)
     X = df.values
     # creer le model d'entrainement en utilisant les arbres de decisions
     clf = tree.DecisionTreeClassifier()
     lr_model = clf.fit(X, Y)
     file_name = os.path.join(model_path, "model_HAS.pickle")

     p.dump(lr_model, open(file_name, 'wb'))
   try:
       size = os.stat(LIC_dataset_path).st_size
   except FileNotFoundError:
       print('  File or file path not found ', LIC_dataset_path)
       sys.exit(2)
   if (size != 0):
     try:
           df = pd.read_csv(LIC_dataset_path)
     except FileNotFoundError:
           print('  File or file path not found ', LIC_dataset_path)
           sys.exit(2)


     print(df['is_code_smell'].describe())
     # Construction du dataset
     Y = df.is_code_smell.values
     df.drop('is_code_smell', axis=1, inplace=True)
     X = df.values
     # creer le model d'entrainement en utilisant les arbres de decisions
     clf = tree.DecisionTreeClassifier()
     lr_model = clf.fit(X, Y)
     file_name = os.path.join(model_path, "model_LIC.pickle")

     p.dump(lr_model, open(file_name, 'wb'))
   try:
       size = os.stat(MIM_dataset_path).st_size
   except FileNotFoundError:
       print('  File or file path not found ', MIM_dataset_path)
       sys.exit(2)
   if (size != 0):
     try:
           df = pd.read_csv(MIM_dataset_path)
     except FileNotFoundError:
           print('  File or file path not found ', MIM_dataset_path)
           sys.exit(2)


     print(df['is_code_smell'].describe())
     # Construction du dataset
     Y = df.is_code_smell.values
     df.drop('is_code_smell', axis=1, inplace=True)
     X = df.values
     # creer le model d'entrainement en utilisant les arbres de decisions
     clf = tree.DecisionTreeClassifier()
     lr_model = clf.fit(X, Y)
     file_name = os.path.join(model_path, "model_MIM.pickle")

     p.dump(lr_model, open(file_name, 'wb'))
   try:
       size = os.stat(NLMR_dataset_path).st_size
   except FileNotFoundError:
       print('  File or file path not found ', NLMR_dataset_path)
       sys.exit(2)
   if (size != 0):
     try:
           df = pd.read_csv(NLMR_dataset_path)
     except FileNotFoundError:
           print('  File or file path not found ', NLMR_dataset_path)
           sys.exit(2)


     print(df['is_code_smell'].describe())
     # Construction du dataset
     Y = df.is_code_smell.values
     df.drop('is_code_smell', axis=1, inplace=True)
     X = df.values
     # creer le model d'entrainement en utilisant les arbres de decisions
     clf = tree.DecisionTreeClassifier()
     lr_model = clf.fit(X, Y)
     file_name = os.path.join(model_path, "model_NLMR.pickle")

     p.dump(lr_model, open(file_name, 'wb'))




def predict(argv):
    base_path = ''

    try:
        opts, args = getopt.getopt(argv, "hb:", ["bpath="])
    except getopt.GetoptError:
        print('test.py predict -b <base_path> ')
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print('test.py -p <prediction_path> -m <model_path>')
            sys.exit()
        elif opt in ("-b", "--bpath"):
            base_path = arg

    prediction_path=os.path.join(base_path, 'prediction')
    model_path = os.path.join(base_path, 'models')
    result_path = os.path.join(base_path, 'result')

    HBR_prediction_path = os.path.join(prediction_path, 'HBR_prediction.csv')

    HAS_prediction_path = os.path.join(prediction_path, 'HAS_prediction.csv')

    LIC_prediction_path = os.path.join(prediction_path, 'LIC_prediction.csv')

    MIM_prediction_path = os.path.join(prediction_path, 'MIM_prediction.csv')

    NLMR_prediction_path = os.path.join(prediction_path, 'NLMR_prediction.csv')


    # if the file containing dataset is not empty then create the model
    # HBR
    try:
        size = os.stat(HBR_prediction_path).st_size
    except FileNotFoundError:
        print('  File or file path not found ', HBR_prediction_path)
        sys.exit(2)
    if (size != 0):
        file_name = os.path.join(model_path, "model_HBR.pickle")
        try:
            lr_model = p.load(open(file_name, 'rb'))
        except FileNotFoundError:
            print('  File or file path not found ', file_name)
            sys.exit(2)
        try:
            data_full_name = pd.read_csv(HBR_prediction_path)
            data = pd.read_csv(HBR_prediction_path)
        except FileNotFoundError:
            print('  File or file path not found ', HBR_prediction_path)
            sys.exit(2)



        data.drop('full_name', axis=1, inplace=True)
        X = data.values
        # prediction
        predictions = lr_model.predict(X)
        data_full_name["is_code_smell"] = predictions
        data2 = data_full_name.loc[data_full_name['is_code_smell'] == 1]
        print(data2["is_code_smell"].describe())
        HBR_result_path=os.path.join(result_path, 'classification_result_HBR.csv')

        data2.to_csv(HBR_result_path, index=False)

        # if the file containing dataset is not empty then create the model
    #HAS
    try:
        size = os.stat(HAS_prediction_path).st_size
    except FileNotFoundError:
        print('  File or file path not found ', HAS_prediction_path)
        sys.exit(2)
    if (size != 0):


        file_name = os.path.join(model_path, "model_HAS.pickle")
        try:
            lr_model = p.load(open(file_name, 'rb'))
        except FileNotFoundError:
            print('  File or file path not found ', file_name)
            sys.exit(2)
        try:
            data_full_name = pd.read_csv(HAS_prediction_path)
            data = pd.read_csv(HAS_prediction_path)
        except FileNotFoundError:
            print('  File or file path not found ', HAS_prediction_path)
            sys.exit(2)

        data.drop('full_name', axis=1, inplace=True)
        X = data.values
        # prediction
        predictions = lr_model.predict(X)
        data_full_name["is_code_smell"] = predictions
        data2 = data_full_name.loc[data_full_name['is_code_smell'] == 1]
        print(data2["is_code_smell"].describe())
        HAS_result_path = os.path.join(result_path, 'classification_result_HAS.csv')

        data2.to_csv(HAS_result_path, index=False)
    #LIC
    try:
        size = os.stat(LIC_prediction_path).st_size
    except FileNotFoundError:
        print('  File or file path not found ', LIC_prediction_path)
        sys.exit(2)
    if (size != 0):
        file_name = os.path.join(model_path, "model_LIC.pickle")
        try:
            lr_model = p.load(open(file_name, 'rb'))
        except FileNotFoundError:
            print('  File or file path not found ', file_name)
            sys.exit(2)
        try:
            data_full_name = pd.read_csv(LIC_prediction_path)
            data = pd.read_csv(LIC_prediction_path)
        except FileNotFoundError:
            print('  File or file path not found ', LIC_prediction_path)
            sys.exit(2)

        data.drop('full_name', axis=1, inplace=True)
        data.drop('class_complexity', axis=1, inplace=True)
        X = data.values
        # prediction
        predictions = lr_model.predict(X)
        data_full_name["is_code_smell"] = predictions
        data2 = data_full_name.loc[data_full_name['is_code_smell'] == 1]
        print(data2["is_code_smell"].describe())
        LIC_result_path = os.path.join(result_path, 'classification_result_LIC.csv')

        data2.to_csv(LIC_result_path, index=False)
    #MIM
    try:
        size = os.stat(MIM_prediction_path).st_size
    except FileNotFoundError:
        print('  File or file path not found ', MIM_prediction_path)
        sys.exit(2)
    if (size != 0):
        file_name = os.path.join(model_path, "model_MIM.pickle")
        try:
            lr_model = p.load(open(file_name, 'rb'))
        except FileNotFoundError:
            print('  File or file path not found ', file_name)
            sys.exit(2)
        try:
            data_full_name = pd.read_csv(MIM_prediction_path)
            data = pd.read_csv(MIM_prediction_path)
        except FileNotFoundError:
            print('  File or file path not found ', MIM_prediction_path)
            sys.exit(2)

        data.drop('full_name', axis=1, inplace=True)
        data.drop('cyclomatic_complexity', axis=1, inplace=True)
        X = data.values
        # prediction
        predictions = lr_model.predict(X)
        data_full_name["is_code_smell"] = predictions
        data2 = data_full_name.loc[data_full_name['is_code_smell'] == 1]
        print(data2["is_code_smell"].describe())
        MIM_result_path = os.path.join(result_path, 'classification_result_MIM.csv')

        data2.to_csv(MIM_result_path, index=False)
    #NLMR
    try:
        size = os.stat(NLMR_prediction_path).st_size
    except FileNotFoundError:
        print('  File or file path not found ', NLMR_prediction_path)
        sys.exit(2)
    if (size != 0):

        file_name = os.path.join(model_path, "model_NLMR.pickle")
        try:
            lr_model = p.load(open(file_name, 'rb'))
        except FileNotFoundError:
            print('  File or file path not found ', file_name)
            sys.exit(2)
        try:
            data_full_name = pd.read_csv(NLMR_prediction_path)
            data = pd.read_csv(NLMR_prediction_path)
        except FileNotFoundError:
            print('  File or file path not found ', NLMR_prediction_path)
            sys.exit(2)

        data.drop('full_name', axis=1, inplace=True)
        data.drop('class_complexity', axis=1, inplace=True)
        X = data.values
        # prediction
        predictions = lr_model.predict(X)
        data_full_name["is_code_smell"] = predictions
        data2 = data_full_name.loc[data_full_name['is_code_smell'] == 1]
        print(data2["is_code_smell"].describe())
        NLMR_result_path = os.path.join(result_path, 'classification_result_NLMR.csv')

        data2.to_csv(NLMR_result_path, index=False)


if __name__ == "__main__":
   commande=sys.argv[1]
   if(commande=="create") :
       create(sys.argv[2:])
   else :
      if(commande=="predict") :
          predict(sys.argv[2:])
      else : print("commande deosn't correspond to create neither predict ERROR : commnde not found ")

