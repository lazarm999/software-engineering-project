package com.parovi.zadruga.data;

public class UserRating {
    private UserInfoResume user;
    private short rating;

    public UserRating(UserInfoResume user, short rating) {
        this.user = user;
        this.rating = rating;
    }

    public UserInfoResume getUser() {
        return user;
    }

    public void setUser(UserInfoResume user) {
        this.user = user;
    }

    public short getRating() {
        return rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
    }
}
