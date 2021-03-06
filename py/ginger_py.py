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

    print("Create HBR Model...")
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


   #HAS
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

     print("Create HAS Model...")
     # Construction du dataset
     Y = df.is_code_smell.values
     df.drop('is_code_smell', axis=1, inplace=True)
     X = df.values
     # creer le model d'entrainement en utilisant les arbres de decisions
     clf = tree.DecisionTreeClassifier()
     lr_model = clf.fit(X, Y)
     file_name = os.path.join(model_path, "model_HAS.pickle")

     p.dump(lr_model, open(file_name, 'wb'))

   #LIC
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

     print("Create LIC Model...")
     # Construction du dataset
     Y = df.is_code_smell.values
     df.drop('is_code_smell', axis=1, inplace=True)
     X = df.values
     # creer le model d'entrainement en utilisant les arbres de decisions
     clf = tree.DecisionTreeClassifier()
     lr_model = clf.fit(X, Y)
     file_name = os.path.join(model_path, "model_LIC.pickle")

     p.dump(lr_model, open(file_name, 'wb'))
    #MIM
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

     print("Create MIM Model...")
     # Construction du dataset
     Y = df.is_code_smell.values
     df.drop('is_code_smell', axis=1, inplace=True)
     X = df.values
     # creer le model d'entrainement en utilisant les arbres de decisions
     clf = tree.DecisionTreeClassifier()
     lr_model = clf.fit(X, Y)
     file_name = os.path.join(model_path, "model_MIM.pickle")

     p.dump(lr_model, open(file_name, 'wb'))

    #NLMR
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

     print("Create NLMR Model...")
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
        opts, args = getopt.getopt(argv, "hb:c:", ["bcpath=","codeSmell="])
    except getopt.GetoptError:
        print('test.py predict -b <base_path> -c <code_smell> ')
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print('test.py -p <prediction_path> -c <code_smell>')
            sys.exit()
        elif opt in ("-b", "--bpath"):
            base_path = arg
        elif opt in ("-c", "--codeSmell"):
            codeSmell = arg


    prediction_path=os.path.join(base_path, 'prediction')
    model_path = os.path.join(base_path, 'models')
    result_path = os.path.join(base_path, 'result')

    HBR_prediction_path = os.path.join(prediction_path, 'HBR_prediction.csv')

    HAS_prediction_path = os.path.join(prediction_path, 'HAS_prediction.csv')

    LIC_prediction_path = os.path.join(prediction_path, 'LIC_prediction.csv')

    MIM_prediction_path = os.path.join(prediction_path, 'MIM_prediction.csv')

    NLMR_prediction_path = os.path.join(prediction_path, 'NLMR_prediction.csv')
    if (codeSmell=="MIM") :
        mim(MIM_prediction_path, model_path, result_path)
    elif (codeSmell=="LIC") :
        lic(LIC_prediction_path, model_path, result_path)
    elif (codeSmell=="NLMR") :
        nlmr(NLMR_prediction_path, model_path, result_path)
    elif (codeSmell=="HAS") :
        has(HAS_prediction_path, model_path, result_path)
    elif (codeSmell=="HBR") :
        hbr(HBR_prediction_path, model_path, result_path)
    elif (codeSmell=="ALL") :
        all(MIM_prediction_path, LIC_prediction_path, NLMR_prediction_path, HAS_prediction_path,
            HBR_prediction_path, model_path, result_path)














def mim(MIM_prediction_path,model_path,result_path):
    # MIM
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
            print("Detecting MIM code smells...")
            columnsTitles = ['number_of_callers_not_null', 'is_init', 'is_static', 'is_override', 'uses_variables',
                             'call_methode',
                             'call_external_methode', 'cyclomatic_complexity', 'full_name']
            data_full_name = pd.read_csv(MIM_prediction_path)
            data_full_name = data_full_name.reindex(columns=columnsTitles)
            data_full_name.to_csv(MIM_prediction_path, index=False)

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
        print("Done")
        MIM_result_path = os.path.join(result_path, 'classification_result_MIM.csv')

        data2.to_csv(MIM_result_path, index=False)
    else:
        MIM_result_path = os.path.join(result_path, 'classification_result_MIM.csv')
        with open(MIM_result_path, 'w'):
            pass

def lic(LIC_prediction_path,model_path,result_path):
    # LIC
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
            print("Detecting LIC code smells...")
            columnsTitles = ['is_static', 'is_enum', 'uses_variables', 'call_method', 'is_interface', 'is_local_class',
                             'call_external_method', 'class_complexity', 'full_name']
            data_full_name = pd.read_csv(LIC_prediction_path)
            data_full_name = data_full_name.reindex(columns=columnsTitles)
            data_full_name.to_csv(LIC_prediction_path, index=False)

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
        print("Done")
        LIC_result_path = os.path.join(result_path, 'classification_result_LIC.csv')

        data2.to_csv(LIC_result_path, index=False)
    else:
        LIC_result_path = os.path.join(result_path, 'classification_result_LIC.csv')
        with open(LIC_result_path, 'w'):
            pass

def nlmr(NLMR_prediction_path,model_path,result_path):
    # NLMR
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
            print("Detecting NLMR code smells...")
            columnsTitles = ['has_onLowMemory', 'extend_class', 'class_complexity', 'full_name']
            data_full_name = pd.read_csv(NLMR_prediction_path)
            data_full_name = data_full_name.reindex(columns=columnsTitles)
            data_full_name.to_csv(NLMR_prediction_path, index=False)
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
        print("Done")
        NLMR_result_path = os.path.join(result_path, 'classification_result_NLMR.csv')

        data2.to_csv(NLMR_result_path, index=False)
    else:
        NLMR_result_path = os.path.join(result_path, 'classification_result_NLMR.csv')
        with open(NLMR_result_path, 'w'):
            pass


def has(HAS_prediction_path,model_path,result_path):
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
            print("Detecting HAS code smells...")
            columnsTitles = ['cyclomatic_complexity', 'number_of_instructions', 'has_method', 'full_name']
            data_full_name = pd.read_csv(HAS_prediction_path)
            data_full_name = data_full_name.reindex(columns=columnsTitles)
            data_full_name.to_csv(HAS_prediction_path, index=False)

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
        print("Done")
        HAS_result_path = os.path.join(result_path, 'classification_result_HAS.csv')

        data2.to_csv(HAS_result_path, index=False)
    else:
        HAS_result_path = os.path.join(result_path, 'classification_result_HAS.csv')
        with open(HAS_result_path, 'w'):
            pass

def hbr(HBR_prediction_path,model_path,result_path):
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
            print("Detecting HBR code smells...")
            columnsTitles = ['cyclomatic_complexity', 'number_of_instructions', 'has_methode_onReceive', 'full_name']
            data_full_name = pd.read_csv(HBR_prediction_path)
            data_full_name = data_full_name.reindex(columns=columnsTitles)
            data_full_name.to_csv(HBR_prediction_path, index=False)

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
        print("Done")
        HBR_result_path = os.path.join(result_path, 'classification_result_HBR.csv')

        data2.to_csv(HBR_result_path, index=False)
    else:
        HBR_result_path = os.path.join(result_path, 'classification_result_HBR.csv')
        with open(HBR_result_path, 'w'):
            pass


def all(MIM_prediction_path,LIC_prediction_path,NLMR_prediction_path,HAS_prediction_path,HBR_prediction_path,model_path,result_path):
   mim(MIM_prediction_path,model_path,result_path)
   lic(LIC_prediction_path,model_path,result_path)
   nlmr(NLMR_prediction_path,model_path,result_path)
   has(HAS_prediction_path,model_path,result_path)
   hbr(HBR_prediction_path,model_path,result_path)


if __name__ == "__main__":
   commande=sys.argv[1]
   if(commande=="create") :
       create(sys.argv[2:])
   else :
      if(commande=="predict") :
          predict(sys.argv[2:])
      else : print("commande deosn't correspond to create neither predict ERROR : commnde not found ")

