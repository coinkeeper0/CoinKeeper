from Data import Data
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score

def preprocessing():
    pastData = Data()

    #fetch data
    pastData.get_data('pastdata', '20150101')

    #select feature
    pastData.set_feature(
                 ndiff=1,
                 isWeekend=False,
                 dayOfTheWeek=False,
                 hour=False
             )

    #attach label
    pastData.attach_label(thetaList=[0])

    return pastData.features, pastData.featureNames, pastData.labels

if __name__=='__main__':
    features, featureNames, labels = preprocessing()

    df = pd.DataFrame(features, columns=featureNames)
    X_train, X_test, y_train, y_test = train_test_split(
                                           df[featureNames],
                                           labels,
                                           test_size=0.25,
                                           random_state=123456
                                        )

    #construct model
    rf = RandomForestClassifier(
             n_estimators=100,
             oob_score=True,
             random_state=123456
         )

    #training
    rf.fit(X_train, y_train)

    #test
    predicted = rf.predict(X_test)
    accuracy = accuracy_score(y_test, predicted)

    print('Out-of-bag score estimate: %.3f' % rf.oob_score_)
    print('Mean accuracy score: %.3f' % accuracy)
