import sqlite3
from Constants import *

class DatabaseConnection(object):
    """docstring for DatabaseConnection"""
    def __init__(self, dbname):
        self.dbname = dbname

    def insertNewUser(user):
        retval = 0
        try:
            con = sqlite3.connect(self.dbname)
            cur = con.cursor()

            query = '''
                INSERT INTO {0}
                ({1}, {2})
                VALUES ({3}, {4})
            '''.format(TABLE_USERS, USERS_COLUMN_USERNAME, USERS_COLUMN_PASSWORD, user.username, user.password)

            cur.execute(query)
            con.commit()

            retval = cur.lastrowid
        except sqlite3.Error as e:
            if con:
                con.rollback()

            print "Error %s:" % e.args[1]
            retval = -1
        finally:
            if con:
                con.close()

            return retval
        
    def getUserFromId(id):
        retval = 0
        user = None
        try:
            con = sqlite3.connect(self.dbname)
            cur = con.cursor()

            query = """
                SELECT * FROM ?
                WHERE (? = ?)
            """

            cur.execute(query, TABLE_USERS, USERS_COLUMN_ID, id)

            row = cur.fetchone()
            if row:
                user = User(row[1])
                retval = 1

        except sqlite3.Error as e:
            if con:
                con.rollback()

            print "Error %s:" % e.args[1]
            retval = -1
        finally:
            if con:
                con.close()

            return user, retval

    def getUser(username):
        retval = 0
        user = None
        try:
            con = sqlite3.connect(self.dbname)
            cur = con.cursor()

            query = """
                SELECT * FROM ?
                WHERE (? = ?)
            """

            cur.execute(query, TABLE_USERS, USERS_COLUMN_USERNAME, username)

            row = cur.fetchone()
            if row:
                user = User(row[1])
                retval = 1

        except sqlite3.Error as e:
            if con:
                con.rollback()

            print "Error %s:" % e.args[1]
            retval = -1
        finally:
            if con:
                con.close()

            return user, retval

    def checkLogin(username, password)
        retval = 0
        try:
            con = sqlite3.connect(self.dbname)
            cur = con.cursor()

            query = """
                SELECT * FROM ?
                WHERE (? = ?)
                AND (? = ?)
            """

            cur.execute(query, TABLE_USERS, USERS_COLUMN_USERNAME, username, USERS_COLUMN_PASSWORD, password)

            row = cur.fetchone()
            if row:
                retval = 1

        except sqlite3.Error as e:
            if con:
                con.rollback()

            print "Error %s:" % e.args[1]
            retval = -1
        finally:
            if con:
                con.close()

            return retval
