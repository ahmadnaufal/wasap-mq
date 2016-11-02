import DatabaseConnection as db
import json
from Constants import *

class Handler(object):
    """docstring for Handler"""
    def __init__(self):
        self.dbConn = db.DatabaseConnection("wasap_mq.db")

    def login(self, username, password):
        response = {}
        retval, user = self.dbConn.getUser(username)
        if retval > 0:
            if user[3] == password:
                response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_SUCCESS
            else:
                response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_WRONG_LOGIN

        elif retval == 0:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_UNKNOWN_LOGIN
        else:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_ERROR

        return response

    def register(self, username, password):
        response = {}
        retval, user = self.dbConn.getUser(username)
        if retval > 0:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_DUPLICATE_USERNAME
        elif retval == 0:
            user_id = self.dbConn.insertNewUser(username, password)
            if user_id > 0:
                response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_SUCCESS
                response[KEY_RESPONSE_USERID] = user_id
            else:
                response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_ERROR
        else:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_ERROR

        return response

    def addFriend(self, my_username, friend_username):
        response = {}
        retval = self.dbConn.addFriend(my_username, friend_username)
        if retval > 0:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_SUCCESS
        elif retval == 0:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_FRIEND_EXISTING
        else:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_ERROR

        return response

    def createGroup(self, username, groupname):
        response = {}
        retval = self.dbConn.addGroup(groupname)
        if retval > 0:
            retval = self.dbConn.addUserToGroup(username, groupname, role=ROLE_ADMIN)
            if retval > 0:
                response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_SUCCESS
            else:
                response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_ERROR
        elif retval == 0:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_DUPLICATE
        else:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_ERROR

        return response

    def addUserToGroup(self, username, groupname):
        response = {}
        retval = self.dbConn.addUserToGroup(username, groupname, role=ROLE_USER)
        if retval > 0:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_SUCCESS
        elif retval == 0:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_DUPLICATE
        else:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_ERROR

        return response

    def removeUserFromGroup(self, username, groupname):
        response = {}
        retval = self.dbConn.removeUserFromGroup(username, groupname)
        if retval > 0:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_SUCCESS
        else:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_ERROR

        return response

    def getGroups(self, username):
        response = {}
        retval, groups = self.dbConn.getUserGroups(username)
        if retval > 0:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_SUCCESS
            response[KEY_RESPONSE_LIST_GROUP] = groups
        else:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_ERROR

        return response

    def getFriends(self, username):
        response = {}
        retval, friends = self.dbConn.getUserFriends(username)
        if retval > 0:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_SUCCESS
            response[KEY_RESPONSE_LIST_GROUP] = friends
        else:
            response[KEY_RESPONSE_STATUS] = RESPONSE_STATUS_ERROR

        return response
