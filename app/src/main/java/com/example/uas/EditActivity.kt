package com.example.uas

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uas.data.ApiClient
import com.example.uas.data.Body
import com.example.uas.data.BodyPatch
import com.example.uas.data.ResponseApi
import com.example.uas.databinding.ActivityEditBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class EditActivity : AppCompatActivity() {
    private var myData: ResponseApi.ResponseApiItem? = null
    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myData = intent.getParcelableExtra("data")
        if (intent.hasExtra("data")) {
            myData?.apply {
                binding.title.setText(title)
                binding.des.setText(body)
            }

            binding.btnSave.setOnClickListener {
                val title = binding.title.text.toString()
                val des = binding.des.text.toString()
                if (title != myData?.title && des != myData?.body) {
                    val body = BodyPatch(des, title, myData?.userId ?: 0, myData?.id ?: 0)
                    ApiClient.service.putPost(body, myData?.id ?: 0)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Toast.makeText(
                                this@EditActivity,
                                "berhasil edit post",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }, {
                            Log.e("main", it.message.toString())
                        })
                } else {
                    ApiClient.service.patchPost(hashMapOf("title" to title, "body" to des))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Toast.makeText(
                                this@EditActivity,
                                "berhasil edit post",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }, {
                            Log.e("main", it.message.toString())
                        })
                }
            }
        } else {
            supportActionBar?.title = "Add Post"
            binding.btnSave.setOnClickListener {
                val title = binding.title.text.toString()
                val des = binding.des.text.toString()

                val body = Body(des, title, myData?.userId ?: 0)
                ApiClient.service.addPost(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Toast.makeText(
                            this@EditActivity,
                            "berhasil Add post",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }, {
                        Log.e("main", it.message.toString())
                    })
            }
        }
    }
}