import pymysql
import datetime

class Data:
    def __init__(self):
        self.rawData = []
        
        self.features= []
        self.featureNames = []
        self.labels = []

    def __del__(self):
        pass

    def __add_new_feature__(self, newFeature):
        """
        Add a new feature to the feature set.

        :param newFeature: feature to be added to the feature set
        :type newFeature: list of shape [n_samples, n_featureDimension]
        :return: returns nothing
        """
        
        if len(self.features) is 0:
            self.features = newFeature.copy()
        else:
            if len(self.features) != len(newFeature):
                print(len(self.features), len(newFeature))
                raise ValueError
            
            for i in range(len(newFeature)):
                self.features[i] += newFeature[i]

        return

    def get_data(self, tableName, initPoint='20150101'):
        """
        Get data from DB.

        :param tableName: 'pastdata' or 'recentdata'
        :param initPoint: start date of the data, default value is '20150101'
        :type tableName: str
        :type initPoint: str of format 'yyyymmdd'
        :return: returns nothing
        """

        if tableName != 'pastdata' and tableName != 'recentdata':
            print("ERROR: INVALID table name")
            raise ValueError
        
        #convert initPoint into datetime.date format
        initPoint = datetime.datetime.strptime(initPoint, '%Y%m%d').date()
        
        #yesterday's date
        yDay = datetime.date.today() - datetime.timedelta(1)
        
        if initPoint < datetime.date(2015, 1, 1) or initPoint > yDay:
            print("ERROR: INVALID Initial Point")
            raise ValueError

        #connect to mySQL
        conn = pymysql.connect(
                   host='coinkeeper.cyafa3gjnbdg.ap-northeast-2.rds.amazonaws.com',
                   user='coinkeeper', 
                   password='coinkeeper',
                   db='price_db', 
                   charset='utf8'
               )
        
        #create cursor from connection
        curs = conn.cursor()

        #execute SQL query
        sql = 'select date, time, diff from '+tableName+' where date >= "'+str(initPoint)+'"'
        curs.execute(sql)

        #fetch data
        rows = list(curs.fetchall())
        self.rawData = rows.copy()

        #close connection
        conn.close()

        return

    def set_feature(self,
                    ndiff=1,
                    isWeekend=False,
                    dayOfTheWeek=False,
                    hour=False):
        """
        Set features as specified.

        :param ndiff: number of diffs to be used as feature, default value is 1
        :param isWeekend: use weekend feature if true
        :param dayOfTheWeek: use day of the week feature if true
        :param hour: use hour as a feature if true
        :type ndiff: positive integer
        :type isWeekend: boolean
        :type dayOfTheWeek: boolean
        :hour: boolean
        :return: returns nothing
        """

        if ndiff < 1:
            print("ERROR: INVALID ndiff value")
            raise ValueError
        
        self.feature_ndiff(ndiff)
        if isWeekend: self.feature_isWeekend()
        if dayOfTheWeek: self.feature_dayOfTheWeek()
        if hour: self.feature_hour()

        return

    def feature_ndiff(self, n):
        """
        Add a contiguous sequence of n diffs to the feature set.

        :param n: size of diff sequence to be considered
        :type n: int
        :return: returns nothing
        """

        newFeature = []

        for idx in range(len(self.rawData)-1):
            nDiffs = []
            
            for i in range(n):
                if idx-i < 0: nDiffs.append(0)
                else:         nDiffs.append(self.rawData[idx-i][2])
                
            newFeature.append(nDiffs)
        
            
        self.__add_new_feature__(newFeature=newFeature)
        self.featureNames.append('ndiff')

        return

    def feature_isWeekend(self):
        """
        Add 1 as a feature if weekend; 0 otherwise.

        :return: returns nothing
        """
        
        newFeature = []

        for idx in range(len(self.rawData)-1):
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

        for idx in range(len(self.rawData)-1):
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

        for idx in range(len(self.rawData)-1):
            newFeature.append([self.rawData[idx][1].seconds//3600])

        self.__add_new_feature__(newFeature=newFeature)
        self.featureNames.append('hour')

        return

    def attach_label(self, thetaList):
        """
        Attach label to data.

        **** Labels ****
        -2: sharp drop
        -1: drop
         0: no change
         1: rise
         2: sharp rise
        ****************

        :param thetaList: boundaries between each labels
        :type thetaList: list
        :return: returns nothing
        """ 

        if len(thetaList) < 1 or len(thetaList) > 2:
            print("ERROR: Length of theta list must be 1 or 2")
            raise ValueError

        level = 2 * len(thetaList) + 1
        
        if level is 3:
            theta = abs(thetaList[0])
            
            for data in self.rawData:
                diff = data[2]
                
                if   diff >  theta: self.labels.append(1)
                elif diff < -theta: self.labels.append(-1)
                else:               self.labels.append(0)

            self.labels.pop(0)

        elif level is 5:
            thetaList[0] = abs(thetaList[0])
            thetaList[1] = abs(thetaList[1])
            thetaList.sort()

            theta0 = thetaList[0]
            theta1 = thetaList[1]

            for data in self.rawData:
                diff = data[2]

                if   diff >  theta1: self.labels.append(2)
                elif diff >  theta0: self.labels.append(1)
                elif diff > -theta0: self.labels.append(0)
                elif diff > -theta1: self.labels.append(-1)
                else:                self.labels.append(-2)

            self.labels.pop(0)
            
        return
