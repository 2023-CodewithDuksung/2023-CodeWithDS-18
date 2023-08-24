package com.example.a2023hackathon

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class MyApplication : MultiDexApplication() {
    companion object{
        lateinit var db : FirebaseFirestore
        lateinit var storage : FirebaseStorage
        lateinit var auth : FirebaseAuth
        var email:String? = null

        fun checkAuth(): Boolean{
            var currentuser = auth.currentUser
            return currentuser?.let{
                email = currentuser.email
                if(currentuser.isEmailVerified) true
                else false
            } ?: false
        }

    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth

        db = FirebaseFirestore.getInstance()
        storage = Firebase.storage


        var userInfo = UserModel()

        userInfo.uid = auth.uid
        userInfo.email = auth.currentUser?.email

        db.collection("users").document(auth.uid.toString())
            .get()
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    val document = task.result
                    if (document.exists()){ Log.d("ToyProject", "이미 존재하는 계정입니다.") }
                    else {
                        db.collection("users").document(auth.uid.toString()).set(userInfo)
                        Log.d("ToyProject", "계정을 user collection에 추가했습니다.")
                    }
                }
            }

    }

}