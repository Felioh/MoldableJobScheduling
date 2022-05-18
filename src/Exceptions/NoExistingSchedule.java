package Exceptions;

public class NoExistingSchedule extends Exception {

    public NoExistingSchedule(String errorMessage) {
        super(errorMessage);
    }
    
}
