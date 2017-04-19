package ie.cm.models;

/**
 * Created by ftahir on 05/04/17.
 */

public class NewsItem {
    private String newsHeading;
    private String newsDesc;
    private String newsDescSmall;
    private String time;
    private String date;
    private String url;
    private String imageID;
    private boolean favourite;
    private int newsId;

    public NewsItem(String newsHeading, String newsDesc, String date, String time, String url, String imageID,int id) {
        this.newsHeading = newsHeading;
        this.imageID = imageID;
        this.url = url;
        this.time = time;
        this.date = date;
        this.newsDesc = newsDesc;
        this.newsId=id;

    }
    public NewsItem(String newsHeading, String newsDesc, String date, String time, String url, String imageID) {
        this.newsHeading = newsHeading;
        this.imageID = imageID;
        this.url = url;
        this.time = time;
        this.date = date;
        this.newsDesc = newsDesc;

    }
    public NewsItem(){}

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }
    public int getNewsId() {
        return newsId;
    }
    public void setImageID(String imageID) {
        this.imageID = imageID;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setNewsDescSmall(String newsDescSmall) {
        this.newsDescSmall = newsDescSmall;
    }
    public void setNewsDesc(String newsDesc) {
        this.newsDesc = newsDesc;
    }
    public void setNewsHeading(String newsHeading) {
        this.newsHeading = newsHeading;
    }
    public String getNewsDesc() {
        return newsDesc;
    }
    public String getNewsHeading() {
        return newsHeading;
    }
    public String getNewsDescSmall() {
        int length=(int)(this.newsDesc.length()/3);
        this.newsDescSmall=this.newsDesc.substring(0,length)+"....";
        return newsDescSmall;
    }
    public String getTime() {
        return time;
    }
    public String getDate() {
        return date;
    }
    public String getUrl() {
        return url;
    }
    public String getImageID() {
        return imageID;
    }
    public void setFavourite(boolean fv){
        this.favourite=fv;
    }
    public boolean getFavourite(){
        return this.favourite;
    }

}
