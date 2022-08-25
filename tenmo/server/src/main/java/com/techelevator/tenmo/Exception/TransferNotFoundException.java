package com.techelevator.tenmo.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Transfer not found!")
public class TransferNotFoundException extends Exception{

    public TransferNotFoundException() {
        super("Transfer not found!");
    }
}
