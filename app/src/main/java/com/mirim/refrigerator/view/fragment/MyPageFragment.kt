package com.mirim.refrigerator.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mirim.refrigerator.databinding.FragmentMyPageBinding
import com.mirim.refrigerator.dialog.ShowCodeDialog
import com.mirim.refrigerator.model.FamilyMember
import com.mirim.refrigerator.model.User
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.view.mypage.PolicyActivity
import com.mirim.refrigerator.view.mypage.ProfileModifyActivity
import com.mirim.refrigerator.viewmodel.UserViewModel
import com.mirim.refrigerator.viewmodel.app
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageFragment: Fragment() {
    var _binding: FragmentMyPageBinding? = null
    private val userViewModel : UserViewModel by viewModels()
    private val binding get() = _binding!!

    companion object {
        val TAG = "태그"
        val currentVersion = "1.0.0"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.userImage.clipToOutline = true

        userViewModel.loadUsers(User(app.user.nickname,app.user.name,app.user.email,app.user.userId,app.user.groupId,app.user.userImagePath))
        userViewModel.getUser().observe(viewLifecycleOwner, Observer<User>{
            binding.userNickname.text = it.nickname
            binding.userName.text = it.name
            binding.userEmail.text = it.email
        })


        setFamilyMember()


        binding.btnGroupCode.setOnClickListener {
            val dialog = ShowCodeDialog("hajinhajin")
            dialog.show(requireActivity().supportFragmentManager, "")
        }

        binding.btnProfileSetting.setOnClickListener {
            startActivity(Intent(context, ProfileModifyActivity::class.java))
        }

        binding.btnVersion.setOnClickListener {
            Toast.makeText(context, "현재 버전 : ${currentVersion}", Toast.LENGTH_SHORT).show()
        }

        binding.btnPolicy.setOnClickListener {
            startActivity(Intent(activity, PolicyActivity::class.java))
        }

        binding.btnContact.setOnClickListener {
            var intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("s2009@e-mirim.hs.kr"))
            startActivity(intent)
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setFamilyMember() {
        RetrofitService.familyAPI.getFamilyList(userViewModel.getGroupId(),userViewModel.getUserId()).enqueue(object : Callback<List<FamilyMember>> {
            override fun onResponse(
                call: Call<List<FamilyMember>>,
                response: Response<List<FamilyMember>>
            ) {
                val body = response.body()
                Log.d(TAG,response.body().toString())
                // 다른 멤버 존재 여부 확인
                if(body == null) {
                    // 존재하지 않으면..
                } else {
                    userViewModel.setFamilyList(response.body()!!)
                    // 가족 리스트
                    for(i in 0 until userViewModel.getFamily().size)
                        Log.d(TAG,userViewModel.getFamily().get(i).name+" : "+userViewModel.getFamily().get(i).nickname)
                    // 존재하면 recyclerview
                }



            }

            override fun onFailure(call: Call<List<FamilyMember>>, t: Throwable) {

            }
        })
    }
}