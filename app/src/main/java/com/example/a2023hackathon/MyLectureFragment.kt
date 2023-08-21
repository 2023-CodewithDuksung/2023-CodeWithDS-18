package com.example.a2023hackathon

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2023hackathon.MyApplication.Companion.db
import com.example.a2023hackathon.databinding.FragmentMyLectureBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyLectureFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyLectureFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentMyLectureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
//        var toolbar = binding.toolbarBack
//        toolbar.inflateMenu(R.menu.menu_back)
//
//        toolbar.setOnMenuItemClickListener{
//            when(it.itemId){
//                R.id.back ->{
//                    startActivity(Intent(context, NotificationsFragment::class.java))
//                    true
//                }
//                else -> false
//            }
//        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyLectureBinding.inflate(inflater, container, false)

        binding.btnAddtask.setOnClickListener {
            val intent = Intent(requireContext(), AddTaskActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        MyApplication.db.collection("tasks")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<ItemTaskModel>()
                for(document in result){
                    val item = document.toObject(ItemTaskModel::class.java)
                    item.docId = document.id
                    itemList.add(item)
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
         * @return A new instance of fragment FragmentFive_ReviewList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyLectureFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }


    }

}