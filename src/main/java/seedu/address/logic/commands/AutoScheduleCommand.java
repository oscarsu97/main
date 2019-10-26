package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;

public class AutoScheduleCommand {

    public static final String COMMAND_WORD = "autoSchedule";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Generates a list of activities for a specified day based on tag order given by user."
            + "Parameters: "
            + "[" + PREFIX_TAG + "TAG START_TIME]..."
            + "{" + PREFIX_NAME + "ACTIVITY_NAME START_TIME]..."
            + PREFIX_ADDRESS + "ADDRESS"
            + PREFIX_DAY + "DAY\n"
            + "Example 1: autoSchedule " + COMMAND_WORD + " "
            + PREFIX_TAG + "Dining 1000 "
            + PREFIX_TAG + "Attraction 1200 "
            + PREFIX_TAG + "Dining "
            + PREFIX_ADDRESS + "Tokyo "
            + PREFIX_DAY + "{DAY}...\n"
            + "Example 2: autoSchedule " + COMMAND_WORD + " "
            + PREFIX_TAG + "Dining 1000 "
            + PREFIX_NAME + "Disneyland 1200 "
            + PREFIX_TAG + "Dining 1800 "
            + PREFIX_ADDRESS + "Tokyo "
            + PREFIX_DAY + "{DAY}...\n";

    public static final String MESSAGE_SUCCESS = "Schedule for the day generated!";

}
