package com.mirim.refrigerator.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mirim.refrigerator.R
import com.mirim.refrigerator.adapter.MyPageFamilyAdapter
import com.mirim.refrigerator.databinding.FragmentMyPageBinding
import com.mirim.refrigerator.dialog.ShowCodeDialog
import com.mirim.refrigerator.model.FamilyMember
import com.mirim.refrigerator.model.User
import com.mirim.refrigerator.network.RetrofitService
import com.mirim.refrigerator.server.responses.UserInfoResponse
import com.mirim.refrigerator.view.mypage.PolicyActivity
import com.mirim.refrigerator.view.mypage.ProfileModifyActivity
import com.mirim.refrigerator.viewmodel.UserViewModel
import com.mirim.refrigerator.viewmodel.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MyPageFragment: Fragment() {
    var _binding: FragmentMyPageBinding? = null
    private val userViewModel : UserViewModel by viewModels()
    private val binding get() = _binding!!
    lateinit var familyAdapter : MyPageFamilyAdapter

    companion object {
        val TAG = "태그"
        val currentVersion = "1.0.0"
    }

    override fun onResume() {
        super.onResume()
        userViewModel.loadUsers(App.user)
        userViewModel.getUser().observe(viewLifecycleOwner, Observer<User>{
            binding.userNickname.text = it.nickname
            binding.userName.text = it.name
            binding.userEmail.text = it.email
            if(App.imageUri != null) binding.userImage.setImageURI(App.imageUri)
            else {
                Glide.with(requireContext())
                    .load(RetrofitService.IMAGE_BASE_URL+userViewModel.getImage())
                    .error(R.drawable.icon_profile)
                    .fallback(R.drawable.icon_profile)
                    .into(binding.userImage)
            }
        })
        setBaseUserInfo()
        setFamilyMember()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.userImage.clipToOutline = true




        binding.btnGroupCode.setOnClickListener {
            val dialog = ShowCodeDialog(App.groupInviteCode)
            dialog.show(requireActivity().supportFragmentManager, "")
        }

        binding.btnProfileSetting.setOnClickListener {
            val intent = Intent(context, ProfileModifyActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
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

    private fun setBaseUserInfo() {
        val date : String = getDate()
        RetrofitService.userAPI.getUserData(App.user.userId!!,date).enqueue(object :Callback<UserInfoResponse> {
            override fun onResponse(
                call: Call<UserInfoResponse>,
                response: Response<UserInfoResponse>
            ) {
                val body = response.body()
                Log.e(TAG,body?.userImagePath.toString())
            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                Log.e(TAG,"실패")
            }

        })
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
                App.family = response.body()!!
                // 다른 멤버 존재 여부 확인
                if(body?.size==0) {
                    binding.mypageFamilyMembers.isVisible = false
                    binding.lineSecond.isVisible = false
                } else {
                    userViewModel.setFamilyList(response.body()!!)
                    familyAdapter = MyPageFamilyAdapter(context,App.family)
                    binding.recyclerFamilyMember.adapter = familyAdapter
                    binding.recyclerFamilyMember.layoutManager = object : LinearLayoutManager(context){ override fun canScrollVertically(): Boolean { return false } }
                }
            }
            override fun onFailure(call: Call<List<FamilyMember>>, t: Throwable) {

            }
        })
    }

    private fun getDate() : String {
        val currentTime = System.currentTimeMillis()
        return formatDate(currentTime)
    }
    private fun formatDate(time : Long) : String {
        val sdf = SimpleDateFormat("yyyy-MM", Locale.KOREA)
        return sdf.format(time)
    }
}