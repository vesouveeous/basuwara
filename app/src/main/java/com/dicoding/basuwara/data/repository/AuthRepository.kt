package com.dicoding.basuwara.data.repository

import android.util.Log
import com.dicoding.basuwara.data.model.UserModel
import com.dicoding.basuwara.data.preferences.UserPreference
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.dicoding.basuwara.util.Result
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val preferences: UserPreference
) {
    suspend fun loginUser(email: String, password: String): Flow<Result<AuthResult>> {
        return flow {
            emit(Result.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Result.Success(result))

        }.catch {
            emit(Result.Error(it.message.toString()))
        }
    }

    suspend fun registerUser(user: UserModel): Flow<Result<AuthResult>> {
        return flow {
            emit(Result.Loading())

            // Step 1: Register user using Firebase Authentication
            val authResult = firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
            emit(Result.Success(authResult))

            // Step 2: Save additional user data to Realtime Database
            saveUserDataToDatabase(authResult.user?.uid, user)

        }.catch {
            emit(Result.Error(it.message.toString()))
        }
    }

    private fun saveUserDataToDatabase(userId: String?, user: UserModel) {
        userId?.let {
            val databaseReference = firebaseDatabase.reference.child("users").child(userId)

            // Create a HashMap to store user data
            val userData = HashMap<String, Any>()
            userData["name"] = user.name
            userData["phone"] = user.phone
            userData["email"] = user.email

            // Save user data to Realtime Database
            databaseReference.setValue(userData)
                .addOnSuccessListener {
                    Log.i("AuthRepository", "Data saved on RTDB")
                }
                .addOnFailureListener {
                    Log.e("AuthRepository", "Failed to add data", it)
                }
        }
    }

    suspend fun getUserDataFromDatabase(userId: String): Flow<Result<UserModel>> {
        return callbackFlow {
            val databaseReference = firebaseDatabase.reference.child("users").child(userId)
            var user: UserModel? = null

            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").getValue(String::class.java)
                        val phone = snapshot.child("phone").getValue(String::class.java)
                        val email = snapshot.child("email").getValue(String::class.java)

                        user = UserModel(
                            name = name ?: "",
                            phone = phone ?: "",
                            email = email ?: "",
                            password = ""
                        )

                        // Emit hasil jika user tidak null
                        if (user != null) {
                            trySend(Result.Success(user!!))
                        } else {
                            trySend(Result.Error("User data is null"))
                        }

                        // Tutup aliran setelah emit
                        close()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled if needed
                }
            }

            databaseReference.addListenerForSingleValueEvent(valueEventListener)

            // Mendaftarkan pembatalan pada aliran
            awaitClose {
                // Membatalkan listener jika aliran ditutup sebelum pemanggilan selesai
                databaseReference.removeEventListener(valueEventListener)
            }
        }
    }

    suspend fun saveSession(id: String) {
        preferences.saveSession(id)
    }

    suspend fun getSession(): Flow<String>{
        return preferences.getSession()
    }

    suspend fun logout() {
        preferences.logout()
    }
}