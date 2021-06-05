package com.parovi.zadruga.data;

public class ApplicantResume extends UserInfoResume {
    private boolean selected;

    public ApplicantResume(long id, String name, String username, boolean selected) {
        super(id, name, username);
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
