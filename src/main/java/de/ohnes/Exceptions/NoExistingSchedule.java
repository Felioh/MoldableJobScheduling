package de.ohnes.Exceptions;

/**
 * a custom Exception to throw if there exists no schedule.
 */
public class NoExistingSchedule extends Exception {

    public NoExistingSchedule(String errorMessage) {
        super(errorMessage);
    }
    
}
