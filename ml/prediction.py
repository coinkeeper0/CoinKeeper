from lib.PREDDATA import PredData
import datetime
import threading
from sklearn.externals import joblib

#load the model & features from disk
model = joblib.load('model.sav')
featSetting = joblib.load('feature_setting.sav')

currData = PredData('price_db', 'recentdata')

def predict(second):
    global model
    global currData

    initPoint = (datetime.datetime.now()-datetime.timedelta(minutes=30)).strftime('%Y%m%d')

    #fetch data
    currData.get_data(initPoint)

    #set feature
    currData.set_feature(featSetting)

    #predict label
    currData.set_predicted(model.predict([currData.features[-1]]))

    #insert data into DB
    currData.write_data('prediction_db', 'prediction')

    #call itself after 5 minutes
    threading.Timer(second, predict, [second]).start()

if __name__=='__main__':
    #when to start prediction
    run_at = '2018-06-04 13:46:00'
    
    #cycle between each prediction
    second = 300

    #calculate delay
    now = datetime.datetime.now()
    run_at = datetime.datetime.strptime(run_at, '%Y-%m-%d %H:%M:%S')
    delay = (run_at - now).total_seconds()

    #first call
    threading.Timer(delay, predict, [second]).start()
