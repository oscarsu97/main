package seedu.address.model.itineraryitem.activity;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the priority of an Activity in the application.
 * Guarantees: immutable; is valid as declared in {@link #isValidPriority(String)}
 */
public class Priority {

    public static final String MESSAGE_CONSTRAINTS =
            "Value of priority should be of an integer value";

    public final String priorityValue;

    /**
     * Constructs a {@code Name}.
     *
     * @param value A valid priority value.
     */
    public Priority(String value) {
        requireNonNull(value);
        checkArgument(isValidPriority(value), MESSAGE_CONSTRAINTS);
        this.priorityValue = value;
    }

    /**
     * Returns true if a given string is a valid integer value.
     */
    public static boolean isValidPriority(String test) {
        try {
            Integer.parseInt(test);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return priorityValue;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Priority // instanceof handles nulls
                && priorityValue.equals(((Priority) other).priorityValue)); // state check
    }

    @Override
    public int hashCode() {
        return priorityValue.hashCode();
    }
}

