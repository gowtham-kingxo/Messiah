package greenearth.united.com.messiah;

import java.util.Date;

/**
 * Created by gowtham g on 19-03-2018.
 */

public class VolunteerPost extends postID{

    public String user_id, image_url, desc, image_thumb, latitude, longitude, date, address, phone;
    public Date timestamp;





    //Empty constructor
    public VolunteerPost(){}

    //constructor
    public VolunteerPost(String user_id, String image_url, String desc, String image_thumb, Date timestamp,
                         String latitude, String longitude,String date, String address, String phone) {
        this.user_id = user_id;
        this.image_url = image_url;
        this.desc = desc;
        this.image_thumb = image_thumb;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.address = address;
        this.phone = phone;

    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }



    public String getImage_url() {

        return image_url;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setImage_url(String image_url) {

        this.image_url = image_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }






}
