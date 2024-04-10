package com.example.travels.data.repository

import com.example.travels.data.mapper.UserMapper
import com.example.travels.domain.auth.model.UserModel
import com.example.travels.domain.auth.repositoty.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val mapper: UserMapper
) : UserRepository {

    override suspend fun signUp(
        email: String,
        firstname: String,
        lastname: String,
        password: String
    ): UserModel {
        val user = createUserWithEmailAndPassword(email, password).copy(
            firstname = firstname,
            lastname = lastname
        )
        saveUserToStore(user)
        return user
    }

    override suspend fun signIn(email: String, password: String): UserModel {
        val userFB = auth.signInWithEmailAndPassword(email, password).await()
        return mapper.firebaseUserToUserModel(userFB.user!!)
    }

    private suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): UserModel {
        val userFB = auth.createUserWithEmailAndPassword(email, password).await()
        return mapper.firebaseUserToUserModel(userFB.user!!)
    }

    private suspend fun saveUserToStore(user: UserModel) {
        db.collection("users").document(user.id).set(user).await()
    }

    override suspend fun getUserById(uId: String): UserModel {
        return mapper.firebaseDocToUserModel(
            db.collection("users").document(uId).get().await()
        )
    }

}