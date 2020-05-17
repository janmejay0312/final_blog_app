package com.example.janmejay.myblogapp;

public class Blog {
    private String title;
    private String image;
    private String description;
    private String id;

    private String time;
    private long upvote;
    private long downvote;
    public Blog(){
        super();
    }
    public Blog(String title,String image,String description,String id,String time,long upvote,long downvote)
    {
        this.description=description;
        this.image=image;
        this.title=title;
        this.id=id;
        this.time=time;
        this.upvote=upvote;
        this.downvote=downvote;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
   public String getTime(){
        return time;
   }
   public void setTime(String time){
        this.time=time;
   }
   public long getUpvote(){
        return upvote;
   }
   public void setUpvote(long upvote){
        this.upvote=upvote;
   }

    public long getDownvote() {
        return downvote;
    }

    public void setDownvote(long downvote) {
        this.downvote = downvote;
    }
}
