package seedu.planner.commons.core;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_ACCOMMODATION_DISPLAYED_INDEX =
            "The accommodation index provided is invalid";
    public static final String MESSAGE_INVALID_ACTIVITY_DISPLAYED_INDEX = "The activity index provided is invalid";
    public static final String MESSAGE_INVALID_CONTACT_DISPLAYED_INDEX = "The contacts index provided is invalid";
    public static final String MESSAGE_INVALID_DAY_DISPLAYED_INDEX = "The day index provided is invalid";

    public static final String MESSAGE_ACTIVITIES_LISTED_OVERVIEW = "%1$d all activities listed!";
    public static final String MESSAGE_CONTACTS_LISTED_OVERVIEW = "%1$d all contacts listed!";
    public static final String MESSAGE_DAYS_LISTED_OVERVIEW = "%1$d all days listed!";

    public static final String MESSAGE_ACTIVITY_NOT_PRESENT_IN_DAY = "Activity not found in day!";
    public static final String MESSAGE_NO_DAYS_AVAILABLE = "No day(s) are present, please adds a Day first.";
    public static final String MESSAGE_SCHEDULE_ACTIVITY_SUCCESS = "Activities successfully scheduled.";
    public static final String MESSAGE_ACTIVITY_TAG_NOT_FOUND = "Activity with this tag %s not found";
    public static final String MESSAGE_ACTIVITY_NAME_NOT_FOUND = "Activity with this name %s not found";
    public static final String MESSAGE_ADDRESS_NOT_FOUND = "Activities of this address %s not found";


}
