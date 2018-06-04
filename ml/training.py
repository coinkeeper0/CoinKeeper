from lib.TRAINDATA import TrainData
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score
from sklearn.externals import joblib

def preprocessing(data, initPoint, featSetting, thetaList):
    #fetch data
    data.get_data(initPoint)

    #set feature
    data.set_feature(featSetting)

    #attach label
    data.attach_label(thetaList)

    return data.features, data.featureNames, data.labels

def split(features, featureNames, labels):
    df = pd.DataFrame(features, columns=featureNames)
    X_train, X_test, y_train, y_test = train_test_split(
                                           df[featureNames],
                                           labels,
                                           test_size=0.25,
                                           #random_state=123456
                                        )

    return X_train, X_test, y_train, y_test

def print_performance(y_test, predicted):
    accuracy = accuracy_score(y_test, predicted)

    print('Out-of-bag score estimate: %.3f' % rf.oob_score_)
    print('Mean accuracy score: %.3f' % accuracy)

    return
    
def save(model, featSetting):
    joblib.dump(model, 'model.sav')
    joblib.dump(featSetting, 'feature_setting.sav')

    return

if __name__ == '__main__':
    trainData = TrainData('price_db', 'pastdata')

    #set values
    #--------------------------------------------------------------
    initPoint = '20150101'
    featSetting = {
                    "ndiff":1,
                    "isWeekend":False,
                    "dayOfTheWeek":False,
                    "hour":False
               }
    thetaList = [0]
    #--------------------------------------------------------------
    features, featureNames, labels = preprocessing(trainData, initPoint, featSetting, thetaList)

    #split data set into training and test data sets
    X_train, X_test, y_train, y_test = split(features, featureNames, labels)

    #construct model
    #--------------------------------------------------------------
    rf = RandomForestClassifier(
             n_estimators=100,
             oob_score=True,
             random_state=123456
         )
    #--------------------------------------------------------------

    #training
    rf.fit(X_train, y_train)

    #test
    predicted = rf.predict(X_test)

    #print result
    print_performance(y_test, predicted)
    
    #save model & feature setting to disk
    save(rf, featSetting)
