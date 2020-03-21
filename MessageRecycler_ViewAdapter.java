package riaz.chatrk.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.recyclerview.widget.RecyclerView;
import riaz.chatrk.Objects.AllChatCommunicationMessage_Obj;
import riaz.chatrk.R;

public class MessageRecycler_ViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_MSG = 1;
    private static final int TYPE_SELF = 2;
    Calendar c = Calendar.getInstance();

    ArrayList<AllChatCommunicationMessage_Obj> messages;
    Context context;
    LayoutInflater inflater;
    String self_id;

    class MessageHolder extends RecyclerView.ViewHolder {
        TextView message_text,message_time,userNm;

        public MessageHolder(View itemView) {
            super(itemView);
            userNm=itemView.findViewById(R.id.userNm);
            message_text=itemView.findViewById(R.id.msg);
            message_time=itemView.findViewById(R.id.date_time);
        }
    }

    class SelfHolder extends RecyclerView.ViewHolder {
        TextView msg_text,msg_time,userName;

        public SelfHolder(View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.userNm);
            msg_text=itemView.findViewById(R.id.msg);
            msg_time=itemView.findViewById(R.id.date_time);
        }
    }

    public MessageRecycler_ViewAdapter(ArrayList<AllChatCommunicationMessage_Obj> messages, Context context,
                                       String self_id) {
        this.messages = messages;
        this.context = context;
        this.self_id = self_id;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        int type = -1;
        AllChatCommunicationMessage_Obj message = messages.get(position);
//        if (message.getSender_id()==self_id ||
//                message.getEmail().equals(self_id)) {
        if (message.getEmail().equals(self_id)) {


                type = TYPE_SELF;
        }
        else {
            type = TYPE_MSG;
        }
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_MSG:
            {
                View itemView = inflater.inflate(R.layout.left_message_layout, parent, false);
                holder = new MessageHolder(itemView);
                break;
            }

            case TYPE_SELF:
            {
                View itemView = inflater.inflate(R.layout.right_message_layout, parent, false);
                holder = new SelfHolder(itemView);
                break;
            }
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // reuse the builder specs to create multiple drawables
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String current_format_date="";

        switch (holder.getItemViewType()) {

            case TYPE_MSG:
            {
                MessageHolder messageHolder = (MessageHolder) holder;
                messageHolder.userNm.setText(messages.get(position).getSender_name());
                messageHolder.message_text.setText(messages.get(position).getMessage());

//                current_format_date= df.format(messages.get(position).getDate_time().toString());

                messageHolder.message_time.setText(messages.get(position).getDate_time()
                .substring(11,16));


                break;
            }
            case TYPE_SELF:
            {
                SelfHolder selfHolder = (SelfHolder) holder;
                selfHolder.userName.setText(messages.get(position).getSender_name());
                selfHolder.msg_text.setText(messages.get(position).getMessage());

                selfHolder.msg_time.setText(messages.get(position).getDate_time()
                        .substring(11,16));

                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
