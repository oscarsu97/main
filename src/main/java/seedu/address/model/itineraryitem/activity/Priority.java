package seedu.address.model.itineraryitem.activity;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.commons.util.StringUtil;

/**
 * Represents the priority of an Activity in the application.
 * Guarantees: immutable;
 */
public class Priority {

    public static final String MESSAGE_CONSTRAINTS =
            "Value of priority should be a non-zero positive integer";

    public final int priorityValue;

    /**
     * Constructs a {@code Priority}.
     *
     * @param value A valid priority value.
     */
    public Priority(int value) {
        requireNonNull(value);
        checkArgument(isValidPriority(value), MESSAGE_CONSTRAINTS);
        this.priorityValue = value;
    }

    /**
     * Returns true if a given integer is a valid priority value.
     */
    public static boolean isValidPriority(Integer test) {
        return StringUtil.isNonZeroUnsignedInteger(test.toString()) && (test >= 0);
    }

    @Override
    public String toString() {
        return Integer.toString(priorityValue);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Priority // instanceof handles nulls
                && priorityValue == ((Priority) other).priorityValue); // state check
    }

    @Override
    public int hashCode() {
        return ((Integer) priorityValue).hashCode();
    }
}

