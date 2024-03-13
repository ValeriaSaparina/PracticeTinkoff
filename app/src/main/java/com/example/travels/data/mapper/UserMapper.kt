package com.example.travels.data.mapper

import com.example.travels.domain.user.UserModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Singleton

@Singleton
object UserMapper {

    fun firebaseUserToUserModel(fireUser: FirebaseUser): UserModel {
        return UserModel(id = fireUser.uid, email = fireUser.email!!)
    }

    fun firebaseDocToUserModel(document: DocumentSnapshot): UserModel {
        return document.toObject(UserModel::class.java)!!
    }

}