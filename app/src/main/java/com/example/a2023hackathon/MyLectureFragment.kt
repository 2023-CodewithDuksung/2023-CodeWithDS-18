package com.example.a2023hackathon

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2023hackathon.MyApplication.Companion.auth
import com.example.a2023hackathon.MyApplication.Companion.db
import com.example.a2023hackathon.MyApplication.Companion.storage
import com.example.a2023hackathon.databinding.FragmentMyLectureBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
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

    lateinit var toolbar : ActionBarDrawerToggle

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
        binding = FragmentMyLectureBinding.inflate(inflater, container, false)

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        binding.btnBs.setOnClickListener {
            bottomSheetDialog.show()
        }
        setupBottomSheet()

        db.collection("users").document(auth.uid.toString()).collection("mylectures")
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Get the number of documents in the collection
                querySnapshot.size()
                // Now you can use the collectionSize as needed
                if(querySnapshot.size() == 0){
                    binding.textView.visibility = View.VISIBLE
                }
                Log.d("ToyProject", "${querySnapshot.size()}")
            }.addOnFailureListener { exception ->
                // Handle any errors
                Toast.makeText(requireContext(), "데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }

        binding.chatListToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.deleteAll.setOnClickListener {
            AlertDialog.Builder(context).run{
                setTitle("정말 삭제하시겠습니까?")
                setMessage("한 번 삭제하면 되돌릴 수 없습니다.")
                setNegativeButton("Cancle", alertHandler)
                setPositiveButton("Yes", alertHandler)
                show()
            }
        }

        // 어댑터를 설정하고 리사이클러뷰에 연결
        val itemList = mutableListOf<ItemLectureModel>()
        val adapter = MyLectureAdapter(requireContext(), itemList)
        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.feedRecyclerView.adapter = adapter

        return binding.root
    }

    private fun setupBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet, null)

//        bottomSheetDialog.window?.setBackgroundDrawableResource(R.drawable.custom_bs)

        // 바텀 시트를 표시할 버튼에 클릭 이벤트 설정
        binding.btnBs.setOnClickListener {
            bottomSheetDialog.show()
        }

        // 각 버튼의 클릭 이벤트 처리
        val termAll = view.findViewById<Button>(R.id.termAll)
        val term202301Button = view.findViewById<Button>(R.id.term202301)
        val term202302Button = view.findViewById<Button>(R.id.term202302)
        val term202401Button = view.findViewById<Button>(R.id.term202401)
        val term202402Button = view.findViewById<Button>(R.id.term202402)

        termAll.setOnClickListener {
            Toast.makeText(requireContext(), "2023년 1학기 선택", Toast.LENGTH_SHORT).show()
            binding.semester.setText("강의 목록")
            onStart()
            bottomSheetDialog.dismiss()
        }
        term202301Button.setOnClickListener {
            Toast.makeText(requireContext(), "2023년 1학기 선택", Toast.LENGTH_SHORT).show()
            binding.semester.setText("2023년 1학기")
            getLectures("2023-01")
            bottomSheetDialog.dismiss()
        }

        term202302Button.setOnClickListener {
            Toast.makeText(requireContext(), "2023년 2학기 선택", Toast.LENGTH_SHORT).show()
            binding.semester.setText("2023년 2학기")
            getLectures("2023-02")
            bottomSheetDialog.dismiss()
        }

        term202401Button.setOnClickListener {
            Toast.makeText(requireContext(), "2024년 1학기 선택", Toast.LENGTH_SHORT).show()
            binding.semester.setText("2024년 1학기")
            getLectures("2024-01")
            bottomSheetDialog.dismiss()
        }

        term202402Button.setOnClickListener {
            Toast.makeText(requireContext(), "2024년 2학기 선택", Toast.LENGTH_SHORT).show()
            binding.semester.setText("2024년 2학기")
            getLectures("2024-02")
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(view)
    }


    override fun onStart() {
        super.onStart()

        MyApplication.db.collection("users").document(auth.uid.toString()).collection("mylectures")
            .orderBy("term", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<ItemLectureModel>()
                for(document in result){
                    val item = document.toObject(ItemLectureModel::class.java)
                    item.docId = document.id
                    itemList.add(item)
                }
                binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.feedRecyclerView.adapter = MyLectureAdapter(requireContext(), itemList)
//                Toast.makeText(context, "내강의리스트 가져오기 성공", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(), "데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }
    }

    val alertHandler = object: DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            when(which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    MyApplication.db.collection("users").document(auth.uid.toString()).collection("mylectures")
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                document.reference.delete()
                                Log.d("ToyProject", "삭제 성공")
                            }
                            refreshRecyclerView()
                        }
                        .addOnFailureListener{
                            Log.d("ToyProject", "삭제 실패")
                        }
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    Log.d("ToyProject", "DialogInterface.BUTTON_NEGATIVE")
                }
            }
        }
    }

    private fun getLectures(semester: String) {
        db.collection("users").document(auth.uid.toString()).collection("mylectures")
            .whereEqualTo("term",semester)
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<ItemLectureModel>()
                for(document in result){
                    val item = document.toObject(ItemLectureModel::class.java)
                    item.docId = document.id
                    itemList.add(item)
                }
                binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.feedRecyclerView.adapter = MyLectureAdapter(requireContext(), itemList)
//                Toast.makeText(context, "내강의리스트 가져오기 성공", Toast.LENGTH_SHORT).show()
                if(result.size() == 0) binding.textView.visibility = View.VISIBLE
                else binding.textView.visibility = View.INVISIBLE
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(), "데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }
    }

    private fun refreshRecyclerView() {
        // 리사이클러뷰를 새로고침하고 데이터를 다시 로드
        val currentUser = auth.currentUser

        currentUser?.let {
            db.collection("users").document(auth.uid.toString()).collection("mylectures")
                .get()
                .addOnSuccessListener { result ->
                    val itemList = mutableListOf<ItemLectureModel>()
                    for(document in result){
                        val item = document.toObject(ItemLectureModel::class.java)
                        item.docId = document.id
                        itemList.add(item)
                    }
                    // 기존 리사이클러뷰 어댑터에 새 데이터 설정
                    binding.feedRecyclerView.adapter = MyLectureAdapter(requireContext(), itemList)
                    binding.textView.visibility = View.VISIBLE
                }
                .addOnFailureListener{
                    Toast.makeText(requireContext(), "데이터 획득 실패", Toast.LENGTH_SHORT).show()
                }
        }
    }
}


