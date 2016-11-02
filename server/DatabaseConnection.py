import sqlite3
from Constants import *

class DatabaseConnection(object):
    """docstring for DatabaseConnection"""
    def __init__(self, dbname):
        self.dbname = dbname

    def insertNewUser(self, username, password):
        retval = 0
        try:
            con = sqlite3.connect(self.dbname)
            cur = con.cursor()

            query = '''
                INSERT INTO {0}
                ({1}, {2})
                VALUES ('{3}', '{4}')
            '''.format(TABLE_USERS, USERS_COLUMN_USERNAME, USERS_COLUMN_PASSWORD, username, password)

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

    def getUserFromId(self, id):
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
                user = row[1]
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

    def getUser(self, username):
        retval = 0
        user = None
        try:
            con = sqlite3.connect(self.dbname)
            cur = con.cursor()

            query = """
                SELECT * FROM {0}
                WHERE ({1} = '{2}')
            """.format(TABLE_USERS, USERS_COLUMN_USERNAME, username)

            cur.execute(query)

            row = cur.fetchone()
            if row:
                retval = 1
                user = row

        except sqlite3.Error as e:
            if con:
                con.rollback()

            print "Error %s:" % e.args[1]
            retval = -1
        finally:
            if con:
                con.close()

            return retval, user

    def addGroup(self, group_name):
        retval = 0
        try:
            con = sqlite3.connect(self.dbname)
            cur = con.cursor()

            query = '''
                INSERT INTO {0}
                ({1})
                VALUES ('{2}')
            '''.format(TABLE_GROUPS, GROUPS_COLUMN_NAME, group_name)

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

    def getGroup(self, group_id):
        retval = 0
        group = None
        try:
            con = sqlite3.connect(self.dbname)
            cur = con.cursor()

            query = """
                SELECT * FROM ?
                WHERE (? = ?)
            """

            cur.execute(query, TABLE_GROUPS, GROUPS_COLUMN_ID, group_id)

            row = cur.fetchone()
            if row:
                group = Group(row[1])
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

    def addUserToGroup(self, username, groupname, role=ROLE_USER):
        retval = 0
        try:
            con = sqlite3.connect(self.dbname)
            cur = con.cursor()

            query = '''
                INSERT INTO {0}
                ({1}, {2}, {3})
                VALUES ('{4}', '{5}', '{6}')
            '''.format(TABLE_GROUPS_USER, GROUPS_USER_COLUMN_GROUPNAME, GROUPS_USER_COLUMN_USERNAME, GROUPS_USER_COLUMN_ROLE, username, groupname, role)

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

    def removeUserFromGroup(self, username, groupname):
        retval = 0
        try:
            con = sqlite3.connect(self.dbname)
            cur = con.cursor()

            query = '''
                DELETE FROM {0}
                WHERE ({1} = '{2}') AND ({3} = '{4}')
            '''.format(TABLE_GROUPS_USER, GROUPS_USER_COLUMN_GROUPNAME, groupname, GROUPS_USER_COLUMN_USERNAME, username)

            cur.execute(query)
            con.commit()

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

    # def getGroupMember(self, group_id):
    #     retval = 0
    #     try:
    #         con = sqlite3.connect(self.dbname)
    #         cur = con.cursor()

    #         query = '''
    #             SELECT * FROM {0}
    #             WHERE ({1} = {2})
    #         '''.format(TABLE_GROUPS_USER, GROUPS_USER_COLUMN_GROUP_ID, group_id)

    #         cur.execute(query)
    #         rows = cur.fetchall()
    #     except sqlite3.Error as e:
    #         if con:
    #             con.rollback()

    #         print "Error %s:" % e.args[1]
    #         retval = -1
    #     finally:
    #         if con:
    #             con.close()

    #         return retval

    def getUserGroups(self, username):
        retval = 0
        groups = []
        try:
            con = sqlite3.connect(self.dbname)
            cur = con.cursor()

            query = '''
                SELECT * FROM {0}
                WHERE ({1} = '{2}')
            '''.format(TABLE_GROUPS_USER, GROUPS_USER_COLUMN_USERNAME, username)

            cur.execute(query)
            rows = cur.fetchall()

            retval = 1
            for row in rows:
                groups.append(row[1])

        except sqlite3.Error as e:
            if con:
                con.rollback()

            print "Error %s:" % e.args[1]
            retval = -1
        finally:
            if con:
                con.close()

            return retval, groups

    def addFriend(self, username_1, username_2):
        retval = 0
        try:
            con = sqlite3.connect(self.dbname)
            cur = con.cursor()

            query = '''
                INSERT INTO {0}
                ({1}, {2})
                VALUES ('{3}', '{4}')
            '''.format(TABLE_USERS_FRIEND, USERS_FRIEND_COLUMN_USERNAME_1, USERS_FRIEND_COLUMN_USERNAME_2, username_1, username_2)

            cur.execute(query)
            con.commit()

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

    # TODO: DISTINCT FRIENDS
    def getUserFriends(self, username):
        retval = 0
        friends = []
        try:
            con = sqlite3.connect(self.dbname)
            cur = con.cursor()

            query = '''
                SELECT * FROM {0}
                WHERE ({1} = '{2}') OR ({3} = '{4}')
            '''.format(TABLE_USERS_FRIEND, USERS_FRIEND_COLUMN_USERNAME_1, username, USERS_FRIEND_COLUMN_USERNAME_2, username)

            cur.execute(query)
            rows = cur.fetchall()

            retval = 1
            for row in rows:
                if row[0] == username:
                    friends.append(str(row[1]))
                else:
                    friends.append(str(row[0]))

        except sqlite3.Error as e:
            if con:
                con.rollback()

            print "Error %s:" % e.args[1]
            retval = -1
        finally:
            if con:
                con.close()

            return retval, friends
