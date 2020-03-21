package riaz.chatrk.Objects;

public class AllChatCommunicationMessage_Obj {

    String message,date_time,sender_name,sender_id,email;

    public AllChatCommunicationMessage_Obj() {
    }

    public AllChatCommunicationMessage_Obj(String message, String date_time, String sender_name, String sender_id, String email) {
        this.message = message;
        this.date_time = date_time;
        this.sender_name = sender_name;
        this.sender_id = sender_id;
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
