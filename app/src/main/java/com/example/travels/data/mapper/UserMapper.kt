package com.example.travels.data.mapper

import com.example.travels.domain.auth.model.UserModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserMapper @Inject constructor() {

    fun firebaseUserToUserModel(fireUser: FirebaseUser): UserModel {
        return UserModel(id = fireUser.uid, email = fireUser.email!!)
    }

    fun firebaseDocToUserModel(document: DocumentSnapshot): UserModel {
        return document.toObject(UserModel::class.java)!!
    }

}