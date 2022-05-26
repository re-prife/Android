package com.mirim.refrigerator.view.ingredient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mirim.refrigerator.R
import com.mirim.refrigerator.databinding.ActivitySelectIngredientBinding
import com.mirim.refrigerator.dialog.IngredientDeleteDialog
import com.mirim.refrigerator.dialog.IngredientModifyDialog
import com.mirim.refrigerator.dialog.ShowCodeDialog

class SelectIngredientActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectIngredientBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectIngredientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.btnBack.setOnClickListener {
            finish()
        }

        binding.toolbar.toolbarTitle.text = "식재료 관리"

        binding.btnModify.setOnClickListener {
            Toast.makeText(applicationContext, "수정", Toast.LENGTH_SHORT).show()
            val dialog = IngredientModifyDialog(emptyList(), emptyList())
            dialog.show(supportFragmentManager, "")

        }

        binding.btnDelete.setOnClickListener {
            Toast.makeText(applicationContext, "삭제", Toast.LENGTH_SHORT).show()
            val dialog = IngredientDeleteDialog("팽이버섯")
            dialog.show(supportFragmentManager, "")
        }
    }
}