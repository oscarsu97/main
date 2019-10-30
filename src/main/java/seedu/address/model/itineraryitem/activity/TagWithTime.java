package seedu.address.model.itineraryitem.activity;

import static java.util.Objects.requireNonNull;

import java.time.LocalTime;

import seedu.address.model.tag.Tag;

public class TagWithTime {
    public static final String MESSAGE_CONSTRAINTS =
            "Value of priority should be a non-zero positive integer";

    public final Tag tag;
    public final Integer time;

    public TagWithTime(Tag tag, Integer time) {
        requireNonNull(tag);
        this.tag = tag;
        this.time = time;
    }

    public Tag getTag() {
        return tag;
    }

    public Integer getTime() {
        return time;
    }

    @Override
    public String toString() {
        return tag + time.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameWithTime // instanceof handles nulls
                && tag.equals(((NameWithTime) other).name)
                && time.equals(((NameWithTime) other).time)); // state check
    }

    @Override
    public int hashCode() {
        return time != null
                ? tag.hashCode() + time.hashCode()
                : tag.hashCode();
    }
}
