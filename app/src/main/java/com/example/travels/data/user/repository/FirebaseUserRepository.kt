package com.example.travels.data.user.repository

import android.content.SharedPreferences
import com.example.travels.data.user.mapper.UserMapper
import com.example.travels.domain.auth.model.UserModel
import com.example.travels.domain.auth.repositoty.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val sharedPreferences: SharedPreferences,
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
        saveAuthUserToSP(user)
        return user
    }

    override suspend fun signIn(email: String, password: String): UserModel {
        auth.signInWithEmailAndPassword(email, password).await()
        val user = getCurrentUserFromRemote()
        saveAuthUserToSP(user)
        return user
    }

    private suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): UserModel {
        return mapper.firebaseUserToUserModel(
            auth.createUserWithEmailAndPassword(email, password).await().user!!
        )
    }

    private fun saveAuthUserToSP(user: UserModel) {
        sharedPreferences.edit()
            .putString(SP_USER_ID_KEY, user.id)
            .putString(SP_USER_FIRSTNAME_KEY, user.firstname)
            .putString(SP_USER_LASTNAME_KEY, user.lastname)
            .putString(SP_USER_EMAIL_KEY, user.email)
            .apply()
    }

    private suspend fun saveUserToStore(user: UserModel) {
        db.collection(USERS_COLLECTION_PATH).document(user.id).set(user).await()
    }

    override suspend fun getUserById(uId: String): UserModel {
        val user = mapper.firebaseDocToUserModel(
            db.collection(USERS_COLLECTION_PATH).document(uId).get().await()
        )
        return user
    }

    override suspend fun getCurrentUserFromRemote(): UserModel {
        return mapper.firebaseDocToUserModel(
            db.collection(USERS_COLLECTION_PATH).document(auth.currentUser!!.uid).get().await()
        )
    }

    override suspend fun getCurrentUserFromLocal(): UserModel? {
        with(sharedPreferences) {
            val id = getString(SP_USER_ID_KEY, "") ?: return null
            val firstname = getString(SP_USER_FIRSTNAME_KEY, "") ?: return null
            val lastname = getString(SP_USER_LASTNAME_KEY, "") ?: return null
            val email = getString(SP_USER_EMAIL_KEY, "") ?: return null
            return UserModel(
                id = id,
                firstname = firstname,
                lastname = lastname,
                email = email
            )
        }
    }

    override suspend fun signOut() {
        deleteUserFromSP()
        auth.signOut()
    }

    private fun deleteUserFromSP() {
        sharedPreferences.edit()
            .remove(SP_USER_ID_KEY)
            .remove(SP_USER_FIRSTNAME_KEY)
            .remove(SP_USER_LASTNAME_KEY)
            .remove(SP_USER_EMAIL_KEY)
            .apply()
    }

    override suspend fun isAuth(): Boolean {
        return !sharedPreferences.getString(SP_USER_ID_KEY, "").isNullOrEmpty()
    }

    companion object {
        private const val USERS_COLLECTION_PATH = "users"
        private const val SP_USER_ID_KEY = "USER_ID"
        private const val SP_USER_FIRSTNAME_KEY = "USER_FIRSTNAME"
        private const val SP_USER_LASTNAME_KEY = "USER_LASTNAME"
        private const val SP_USER_EMAIL_KEY = "USER_EMAIL"
    }

}