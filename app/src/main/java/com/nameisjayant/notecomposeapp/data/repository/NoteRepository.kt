package com.nameisjayant.notecomposeapp.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.nameisjayant.BaseApplication
import com.nameisjayant.notecomposeapp.data.model.Auth
import com.nameisjayant.notecomposeapp.data.model.AuthResponse
import com.nameisjayant.notecomposeapp.data.model.Note
import com.nameisjayant.notecomposeapp.data.model.NoteResponse
import com.nameisjayant.notecomposeapp.utils.NOTE
import com.nameisjayant.notecomposeapp.utils.PreferenceStore
import com.nameisjayant.notecomposeapp.utils.ResultState
import com.nameisjayant.notecomposeapp.utils.USER
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val authDb: FirebaseAuth,
    private val db: DatabaseReference,
    private val preferenceStore: PreferenceStore
) {

    suspend fun createUser(data: Auth?): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        authDb.createUserWithEmailAndPassword(
            data?.email ?: "",
            data?.password ?: ""
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                BaseApplication.scope.launch {
                    preferenceStore.setPref(PreferenceStore.userId, authDb.currentUser?.uid ?: "")
                    preferenceStore.setPref(PreferenceStore.email, data?.email ?: "")
                }
                trySend(ResultState.Success("User created successfully"))
            }
        }.addOnFailureListener {
            trySend(ResultState.Failure(it))
        }

        awaitClose {
            close()
        }
    }

    suspend fun loginUser(auth: Auth?): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        authDb.signInWithEmailAndPassword(
            auth?.email ?: "",
            auth?.password ?: ""
        ).addOnSuccessListener {
            BaseApplication.scope.launch {
                preferenceStore.setPref(PreferenceStore.userId, authDb.currentUser?.uid ?: "")
                preferenceStore.setPref(PreferenceStore.email, auth?.email ?: "")
            }
            trySend(ResultState.Success("login Successfully"))
        }.addOnFailureListener {
            trySend(ResultState.Failure(it))
        }
        awaitClose {
            close()
        }
    }

    suspend fun addUserDetails(data: Auth, id: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.child(USER).child(id).push()
            .setValue(
                data
            ).addOnCompleteListener {
                if (it.isSuccessful)
                    trySend(ResultState.Success("Data inserted Successfully.."))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    suspend fun getUserDetails(): Flow<ResultState<List<AuthResponse>>> = callbackFlow {
        trySend(ResultState.Loading)
        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val auth = snapshot.children.map {
                    AuthResponse(
                        auth = it.getValue(Auth::class.java),
                        key = it.key
                    )
                }
                trySend(ResultState.Success(auth))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }
        }

        db.child(USER).child(preferenceStore.getPref(PreferenceStore.userId).first())
            .addValueEventListener(valueEvent)
        awaitClose {
            db.child(NOTE).removeEventListener(valueEvent)
            close()
        }
    }

    suspend fun addNote(item: Note): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.child(NOTE).child(preferenceStore.getPref(PreferenceStore.userId).first()).push()
            .setValue(
                item
            ).addOnCompleteListener {
                if (it.isSuccessful)
                    trySend(ResultState.Success("Data inserted Successfully.."))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    suspend fun getNotes(): Flow<ResultState<List<NoteResponse>>> = callbackFlow {
        trySend(ResultState.Loading)

        val valueEvent = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.map {
                    Log.d("main", "onDataChange:${it.key} ")
                    NoteResponse(
                        id = it.key,
                        note = it.getValue(Note::class.java),
                    )
                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }
        }

        db.child(NOTE).child(preferenceStore.getPref(PreferenceStore.userId).first())
            .addValueEventListener(valueEvent)
        awaitClose {
            db.child(NOTE).removeEventListener(valueEvent)
            close()
        }
    }

    suspend fun deleteNote(key: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        Log.d(
            "main",
            "userId: ${preferenceStore.getPref(PreferenceStore.userId).first()} , key : $key"
        )
        val reference =
            db.child(NOTE).child(preferenceStore.getPref(PreferenceStore.userId).first()).child(key)

        val valueListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                    reference.removeValue()
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }

        }
        reference.addListenerForSingleValueEvent(valueListener)
        awaitClose {
            reference.removeEventListener(valueListener)
        }
    }

    suspend fun updateNote(res: NoteResponse): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val map = HashMap<String, Any>()
        map["title"] = res.note?.title ?: ""
        map["description"] = res.note?.description ?: ""

        db.child(NOTE).child(preferenceStore.getPref(PreferenceStore.userId).first())
            .child(res.id ?: "").updateChildren(
                map
            ).addOnCompleteListener {
                trySend(ResultState.Success("Update successfully..."))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    suspend fun updateUserDetail(auth: AuthResponse): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val map = HashMap<String, Any>()
        map["username"] = auth.auth?.username ?: ""
        map["mobileNumber"] = auth.auth?.mobileNumber ?: ""
        map["dob"] = auth.auth?.dob ?: ""
        map["gender"] = auth.auth?.gender ?: ""
        db.child(USER).child(preferenceStore.getPref(PreferenceStore.userId).first())
            .child(auth.key ?: "").updateChildren(
                map
            ).addOnCompleteListener {
                trySend(ResultState.Success("Update successfully..."))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    fun signOut() = authDb.signOut()

    fun resetPassword(
        email:String
    ): Flow<ResultState<String>> = callbackFlow {
        authDb.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    trySend(ResultState.Success("Password reset email send to your email address"))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }
}
