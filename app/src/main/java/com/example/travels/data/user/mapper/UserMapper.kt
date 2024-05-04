package com.example.travels.data.user.mapper

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
        return with(document) {
            UserModel(
                id = id,
                email = getString("email") ?: "",
                firstname = getString("firstname"),
                lastname = getString("lastname"),
            )
        }
    }

}