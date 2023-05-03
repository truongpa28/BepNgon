package com.phamtruong.bepngon.ui.main.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.phamtruong.bepngon.R
import com.phamtruong.bepngon.databinding.ActivitySearchBinding
import com.phamtruong.bepngon.model.PostModel
import com.phamtruong.bepngon.sever.FBConstant
import com.phamtruong.bepngon.ui.adapter.EventClickPostsAdapterListener
import com.phamtruong.bepngon.ui.adapter.PostsAdapter
import com.phamtruong.bepngon.util.DataUtil
import com.phamtruong.bepngon.util.showToast
import com.phamtruong.bepngon.view.show

class SearchActivity : AppCompatActivity() , EventClickPostsAdapterListener {

    private lateinit var binding : ActivitySearchBinding
    lateinit var adapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PostsAdapter(this, ArrayList<PostModel>(), this)
        binding.rcyKetQua.adapter = adapter

        initView()

        initSearch()
    }

    private fun initSearch() {
        binding.edtSearch.doOnTextChanged { text, start, before, count ->
            if (text.toString().length > 2) {
                searchPost(text.toString())
            } else {
                adapter.setListData(ArrayList())
            }
        }
    }

    private val mDatabase = FirebaseDatabase.getInstance().getReference(FBConstant.ROOT)
    private fun searchPost(data: String) {
        mDatabase.child(FBConstant.POST_F).get().addOnSuccessListener { dataSnapshot->
            val listData = ArrayList<PostModel>()
            for (postSnapshot in dataSnapshot.children) {
                postSnapshot.getValue<PostModel>()?.let {
                    if (DataUtil.checkSearch(it.tag, data))
                        listData.add(
                            it
                        )
                }
            }
            adapter.setListData(listData)
        }.addOnFailureListener {
            //showToast("Lỗi kết nối!")
        }
    }

    private fun initView() {
        binding.toolBar.txtTitle.text = "Tìm kiếm"
        binding.toolBar.imgBack.show()
        binding.toolBar.imgBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun clickPost(post: PostModel) {

    }
}