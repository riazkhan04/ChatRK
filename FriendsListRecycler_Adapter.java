package riaz.chatrk.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import riaz.chatrk.Objects.User_ModelObj;
import riaz.chatrk.R;

public class FriendsListRecycler_Adapter extends RecyclerView.Adapter<FriendsListRecycler_Adapter.ViewHolder> {

    Context context;
    ArrayList<User_ModelObj> friends_arr;
    ItemSelectListener selectListener;

    public FriendsListRecycler_Adapter(Context context, ArrayList<User_ModelObj> friends_arr,
    ItemSelectListener listener) {
        this.context = context;
        this.friends_arr = friends_arr;
        this.selectListener=listener;
    }

    @NonNull
    @Override
    public FriendsListRecycler_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.items_friends,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsListRecycler_Adapter.ViewHolder holder, final int position) {


        holder.friends_name.setText(friends_arr.get(position).getName());

         Glide.with(context)
                    .load(Uri.parse(friends_arr.get(position).getImage()))
                    .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                    .centerCrop()
                    .into(holder.profile_img);

         holder.user_LL.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 selectListener.onFriends_ItemClickListener(position,
                         friends_arr.get(position).getUid(),
                         friends_arr.get(position).getName(),
                         friends_arr.get(position).getImage()
                 );
             }
         });


    }

    @Override
    public int getItemCount() {
        return friends_arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView friends_name;
        CircleImageView profile_img;
        LinearLayout user_LL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friends_name=itemView.findViewById(R.id.friends_usernm);
            profile_img=itemView.findViewById(R.id.userimage);
            user_LL=itemView.findViewById(R.id.user_LL);
        }
    }
    public interface ItemSelectListener{
        void onFriends_ItemClickListener(int pos,String UID,String name,String image);
    }
}
