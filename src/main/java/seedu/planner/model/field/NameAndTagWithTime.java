package seedu.planner.model.field;

import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;

import seedu.planner.logic.commands.util.CommandUtil;
import seedu.planner.model.tag.Tag;

/**
 * Represents a Name or Tag with a timing tagged to it.
 */
public class NameAndTagWithTime {
    private final Name name;
    private final Tag tag;
    private final LocalTime time;

    public NameAndTagWithTime(Name name, Tag tag, LocalTime time) {
        CommandUtil.onlyOneNonNull(name, tag);
        this.name = name;
        this.tag = tag;
        this.time = time;
    }

    public Optional<Name> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<Tag> getTag() {
        return Optional.ofNullable(tag);
    }

    public LocalTime getTime() {
        return time;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameAndTagWithTime // instanceof handles nulls
                && tag.equals(((NameAndTagWithTime) other).tag)
                && name.equals(((NameAndTagWithTime) other).name)
                && time.equals(((NameAndTagWithTime) other).time)); // state check
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tag, time);
    }
}
