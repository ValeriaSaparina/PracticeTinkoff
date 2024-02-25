package com.example.travels.data.repositoriesImpl

import android.util.Log
import com.example.travels.data.mapper.UserMapper
import com.example.travels.domain.user.UserModel
import com.example.travels.domain.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository : UserRepository {

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore
    private var currentUser: UserModel? = null

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): UserModel {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result.user?.run {
            Log.d("CREATE_USER", "success")
            return UserMapper.firebaseUserToUserModel(this)
        }
        throw Throwable("Something went wrong")
    }

    override suspend fun saveUserToStore(user: UserModel): Boolean {
        return try {
            val result = db.collection("users").document(user.id).set(user)
            currentUser = user
            result.isSuccessful
        } catch (e: Exception) {
            Log.d("SAVE_USER", e.message.toString())
            false
        }
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

    override suspend fun signIn(email: String, password: String): Boolean {
        var isSuccess = false
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                isSuccess = true
                Log.d("SIGN_IN", "success")
            }
            .addOnFailureListener {
                Log.d("SIGN_IN", "${it.message}")
            }.await()
        return isSuccess
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