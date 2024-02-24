package com.example.travels.data.repositoriesImpl

import android.util.Log
import com.example.travels.data.models.UserModel
import com.example.travels.domain.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository : UserRepository {

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore
    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): UserModel? {

        Log.d("CREATE_USER", "isAuth: ${auth.currentUser?.email}")

        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.run {
                Log.d("CREATE_USER", "success")
                return UserModel(
                    id = this.uid,
                    email = email
                )
            }
        } catch (e: Exception) {
            Log.d("CREATE_USER", e.message.toString())
        }
        return null
    }

    override suspend fun saveUserToStore(user: UserModel): Boolean {
        return try {
            val result = db.collection("users").document(user.id).set(user)
            result.isSuccessful
        } catch (e: Exception) {
            Log.d("SAVE_USER", e.message.toString())
            false
        }
    }

}