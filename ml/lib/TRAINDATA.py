from lib.DATA import Data
import datetime

class TrainData(Data):
    def __init__(self, dbName, tableName):
        Data.__init__(self, dbName, tableName)
        self.labels = []

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

        :param thetaList: boundary values between each labels
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

            #delete the last element, whose label cannot be obtained
            self.labels.pop(0)
            self.features.pop()

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

            #delete the last element, whose label cannot be obtained
            self.labels.pop(0)
            self.features.pop()
            
        return
