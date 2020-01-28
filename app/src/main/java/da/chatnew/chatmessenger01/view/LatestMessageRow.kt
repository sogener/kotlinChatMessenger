package da.chatnew.chatmessenger01.view

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import da.chatnew.chatmessenger01.R
import da.chatnew.chatmessenger01.models.ChatMessage
import da.chatnew.chatmessenger01.models.User
import kotlinx.android.synthetic.main.latest_message_row.view.*


class LatestMessageRow (val chatMessage: ChatMessage): Item<GroupieViewHolder>(){
    var chatPartnerUser: User? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_textiview_latest_message.text = chatMessage.text

        // Проверка + загрузка имени пользователя  (latest_message_row)
        val chatPartnerId: String
        if (chatMessage.fromId == FirebaseAuth.getInstance().uid){
            chatPartnerId = chatMessage.toId
        } else {
            chatPartnerId = chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser = p0.getValue(User::class.java) // Пользователь
                viewHolder.itemView.username_textview_latest_message.text = chatPartnerUser?.username // Загрузка имени

                val targetImageView = viewHolder.itemView.imageview_latest_message // Загрузка фотографии пользователя
                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImageView) // Загрузка фотографии пользователя
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }
}