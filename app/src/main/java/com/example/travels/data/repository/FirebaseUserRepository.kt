package com.example.travels.data.repository

import android.util.Log
import com.example.travels.data.mapper.UserMapper
import com.example.travels.domain.user.UserModel
import com.example.travels.domain.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore

) : UserRepository {

    private var currentUser: UserModel? = null

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): UserModel {
        val userFB = auth.createUserWithEmailAndPassword(email, password).await()
        return UserMapper.firebaseUserToUserModel(userFB.user!!)
    }

    override suspend fun saveUserToStore(user: UserModel) {
        db.collection("users").document(user.id).set(user).await()
        currentUser = user
    }

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

    override suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun getCurrentUser(): UserModel {

        if (currentUser == null) {
            initCurrentUser()
        }
        return currentUser ?: throw IllegalStateException("User is not authorized")
    }

    override suspend fun initCurrentUser() {
        val uId = auth.currentUser?.uid
        if (uId != null) {
            db.collection("users").document(uId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        Log.d("SIGN_IN", "DocumentSnapshot data: ${document.data}")
                        currentUser = UserMapper.firebaseDocToUserModel(document)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("SIGN_IN", "get failed with $exception")
                }.await()
        }
    }

}