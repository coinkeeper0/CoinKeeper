from lib.DATA import Data
import pymysql
import datetime

class PredData(Data):
    def __init__(self, dbName, tableName):
        Data.__init__(self, dbName, tableName)
        self.predicted = []
        
    def write_data(self, dbName, tableName):
        """
        Insert data to DB.

        :param dbName: name of DB
        :param tableName: name of table
        :type dbName: str
        :type tableName: str
        :return: returns nothing
        """
        
        #connect to mySQL
        conn = pymysql.connect(
                   host='coinkeeper.cyafa3gjnbdg.ap-northeast-2.rds.amazonaws.com',
                   user='coinkeeper', 
                   password='coinkeeper',
                   db=dbName, 
                   charset='utf8'
               )
        
        #create cursor from connection
        curs = conn.cursor()

        #insert new prediction result
        sql = "insert into {} values ('{}', '{}', {}, 0)".format(tableName,
                                                                 self.rawData[-1][0],
                                                                 self.rawData[-1][1],
                                                                 self.predicted[0])
        curs.execute(sql)
        print("EXECUTE: ", sql)

        #delete previous data
        currTime = datetime.datetime.combine(self.rawData[-1][0],
                                             (datetime.datetime.min+self.rawData[-1][1]).time())
        delTime = currTime - datetime.timedelta(minutes=10)
        sql = "delete from {} where date='{}' and time='{}'".format(tableName,
                                                                    delTime.date(),
                                                                    delTime.time())
        curs.execute(sql)
        print("EXECUTE: ", sql)

        #save change
        conn.commit()
        
        #close connection
        conn.close()

        return

    def set_predicted(self, predicted):
        """
        Store predicted value

        :param predicted: predicted result
        :type predicted: list of size 1
        :return: returns nothing
        """
        
        self.predicted = predicted.copy()

        return
