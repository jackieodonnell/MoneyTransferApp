package com.techelevator.tenmo.model;

import javax.validation.constraints.NotEmpty;

public class RequestDTO {

    @NotEmpty
    private boolean isApproved;

    public boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }
}
