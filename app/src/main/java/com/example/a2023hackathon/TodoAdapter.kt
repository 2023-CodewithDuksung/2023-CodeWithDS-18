import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a2023hackathon.ItemTaskModel
import com.example.a2023hackathon.LectureDetailActivity
import com.example.a2023hackathon.MyApplication.Companion.storage
import com.example.a2023hackathon.R
import com.example.a2023hackathon.TodoTaskAdapter
import com.example.a2023hackathon.databinding.ItemTodoBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class TodoAdapter(
    private val data: ArrayList<String>,
    private val items: MutableList<ItemTaskModel>
) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private lateinit var adapter: TodoTaskAdapter
    private lateinit var context: Context
    private val selectedItems = SparseBooleanArray()
    private var prePosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemTodoBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < items.size) {
            holder.recyclerView.layoutManager = LinearLayoutManager(context)
            adapter = TodoTaskAdapter(context, items)
            holder.recyclerView.adapter = adapter
            holder.onBind(position)

            val data = items[position]

            holder.binding.run {
                todoDate.text = data.date
//                if (todoTaskRecyclerview.isNotEmpty()){ todoTaskRecyclerview.visibility =
//                    View.VISIBLE}else{todoTaskRecyclerview.visibility =
//                    View.INVISIBLE}

                // =============================
                todoDay.setOnClickListener {
                    val bundle: Bundle = Bundle()
                    bundle.putString("title", data.title)
                    bundle.putString("content", data.content)
                    bundle.putString("date", data.date)
                    bundle.putString("major", data.major)
                    bundle.putString("professor", data.professor)
                    bundle.putString("s_date", data.s_date)
                    bundle.putString("d_date", data.d_date)
//
//                    val clickedPosition = holder.adapterPosition
//                    if (selectedItems[clickedPosition]) {
//                        selectedItems.delete(clickedPosition)
//                    } else {
//                        selectedItems.delete(prePosition)
//                        selectedItems.put(clickedPosition, true)
//                    }
//                    if (prePosition != -1) notifyItemChanged(prePosition)
//                    notifyItemChanged(clickedPosition)
//                    prePosition = clickedPosition

                    Intent(context, LectureDetailActivity::class.java).apply {
                        putExtras(bundle)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }.run { context.startActivity(this) }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.todo_task_recyclerview)
        private var position = 0

        fun onBind(position: Int) {
            this.position = position
            changeVisibility(selectedItems[position])
        }

        private fun changeVisibility(isExpanded: Boolean) {
            val dpValue = 250
            val d = context.resources.displayMetrics.density
            val height = (dpValue * d).toInt()

            val va = if (isExpanded) ValueAnimator.ofInt(0, height) else ValueAnimator.ofInt(height, 0)
            va.duration = 600
            va.addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                recyclerView.layoutParams.height = value
                recyclerView.requestLayout()
                recyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE
            }
            va.start()
        }
    }
}
