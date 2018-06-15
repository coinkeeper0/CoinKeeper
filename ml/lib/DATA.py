import pymysql
import datetime

class Data:
    def __init__(self, dbName, tableName):
        self.dbName = dbName
        self.tableName = tableName

        self.rawData = []
        
        self.features = []
        self.featureNames = []

    def __del__(self):
        pass

    def __add_new_feature__(self, newFeature):
        """
        Add a new feature to the feature set.

        :param newFeature: feature to be added to feature set
        :type newFeature: list of shape [n_samples, feature_dimension]
        :return: returns nothing
        """
        
        if len(self.features) is 0:
            self.features = newFeature.copy()
        else:
            if len(self.features) != len(newFeature):
                print(len(self.features), len(newFeature)) #
                raise ValueError
            
            for i in range(len(newFeature)):
                self.features[i] += newFeature[i]

        return

    def get_data(self, initPoint):
        """
        Get data from DB.

        :param initPoint: start date of the data, default value is '20150101'
        :type initPoint: str of format 'yyyymmdd'
        :return: returns nothing
        """

        #convert initPoint into datetime.date format
        initPoint = datetime.datetime.strptime(initPoint, '%Y%m%d').date()


        #connect to mySQL
        conn = pymysql.connect(
                   host='coinkeeper.cyafa3gjnbdg.ap-northeast-2.rds.amazonaws.com',
                   user='coinkeeper', 
                   password='coinkeeper',
                   db=self.dbName, 
                   charset='utf8'
               )
        
        #create cursor from connection
        curs = conn.cursor()

        #execute SQL query
        sql = 'select date, time, diff from {} where date >= "{}"'.format(self.tableName,
                                                                          str(initPoint))
        curs.execute(sql)

        #fetch data
        rows = list(curs.fetchall())
        self.rawData = rows.copy()

        #close connection
        conn.close()

        #initialization
        self.features= []
        self.featureNames = []

        return

    def set_feature(self, featSetting):
        """
        Set features as specified.

        :param featSetting: indicate how feature is set
        :type featSetting: dict
        :return: returns nothing
        """

        if featSetting['ndiff'] < 1:
            print("ERROR: INVALID ndiff value")
            raise ValueError
        
        self.feature_ndiff(featSetting['ndiff'])
        if featSetting['isWeekend']:    self.feature_isWeekend()
        if featSetting['dayOfTheWeek']: self.feature_dayOfTheWeek()
        if featSetting['hour']:         self.feature_hour()

        return

    def feature_ndiff(self, n):
        """
        Add a contiguous sequence of n diffs to the feature set.

        :param n: size of diff sequence to be considered
        :type n: int
        :return: returns nothing
        """

        newFeature = []

        for idx in range(len(self.rawData)):
            nDiffs = []
            
            for i in range(n):
                if idx-i < 0: nDiffs.append(0)
                else:         nDiffs.append(self.rawData[idx-i][2])
                
            newFeature.append(nDiffs)
        
            
        self.__add_new_feature__(newFeature=newFeature)
        for i in range(n):
            self.featureNames.append('ndiff_'+str(i+1))

        return

    def feature_isWeekend(self):
        """
        Add 1 as a feature if weekend; 0 otherwise.

        :return: returns nothing
        """
        
        newFeature = []

        for idx in range(len(self.rawData)):
            if self.rawData[idx][0].weekday() < 5: newFeature.append([0])
            else:                                  newFeature.append([1])

        self.__add_new_feature__(newFeature=newFeature)
        self.featureNames.append('isWeekend')

        return

    def feature_dayOfTheWeek(self):
        """
        Add the day of the week as a feature, where Monday is 0 and Sunday is 6.

        :return: returns nothing
        """

        newFeature = []

        for idx in range(len(self.rawData)):
            newFeature.append([self.rawData[idx][0].weekday()])

        self.__add_new_feature__(newFeature=newFeature)
        self.featureNames.append('dayOfTheWeek')

        return

    def feature_hour(self):
        """
        Add hour as a feature.

        :return: returns nothing
        """

        newFeature = []

        for idx in range(len(self.rawData)):
            newFeature.append([self.rawData[idx][1].seconds//3600])

        self.__add_new_feature__(newFeature=newFeature)
        self.featureNames.append('hour')

        return
