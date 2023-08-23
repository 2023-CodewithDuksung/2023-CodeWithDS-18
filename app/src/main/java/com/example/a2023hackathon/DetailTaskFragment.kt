package com.example.a2023hackathon

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2023hackathon.databinding.FragmentDetailTaskBinding
import com.google.firebase.firestore.Query

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailTaskFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentDetailTaskBinding
    lateinit var major: String
    lateinit var professor: String
    lateinit var subCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailTaskBinding.inflate(inflater, container, false)

        major = arguments?.getString("major").toString()
        professor = arguments?.getString("professor").toString()
        subCode = arguments?.getString("sub_code").toString()

        binding.addTask.setOnClickListener {
            if(MyApplication.checkAuth()){
                val intent = Intent(requireContext(), AddTaskActivity::class.java)
                intent.putExtra(AddTaskActivity.EXTRA_MAJOR, major)
                intent.putExtra(AddTaskActivity.EXTRA_PROFESSOR, professor)
                intent.putExtra(AddTaskActivity.EXTRA_SUB_CODE, subCode)
                startActivity(intent)
            }
        }

        // 어댑터를 설정하고 리사이클러뷰에 연결
        val itemList = mutableListOf<ItemTaskModel>()
        val adapter = MyTaskAdapter(requireContext(), itemList)
        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.feedRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onStart() {
        super.onStart()



        MyApplication.db.collection("tasks")
            .orderBy("d_date", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<ItemTaskModel>()
                for(document in result){
                    val item = document.toObject(ItemTaskModel::class.java)
                    if(subCode.equals(item.sub_code)){
                        item.docId = document.id
                        itemList.add(item)
                    }
                }
                binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.feedRecyclerView.adapter = MyTaskAdapter(requireContext(), itemList)
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(), "데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailTaskFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailTaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}