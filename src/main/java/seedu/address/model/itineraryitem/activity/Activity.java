package seedu.address.model.itineraryitem.activity;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

import seedu.address.model.contact.Contact;
import seedu.address.model.field.Address;
import seedu.address.model.field.Name;
import seedu.address.model.itineraryitem.ItineraryItem;
import seedu.address.model.tag.Tag;

/**
 * Represents an Activity in the trip planner.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Activity extends ItineraryItem {

    private final Duration duration;
    private final Priority priority;

    /**
     * Every field must be present and not null.
     */
    public Activity(Name name, Address address, Contact contact, Set<Tag> tags, Duration duration, Priority priority) {
        super(name, address, contact, tags);
        this.duration = duration;
        this.priority = priority;
    }

    public Integer getDuration() {
        return duration.toMinutesPart();
    }

    public Optional<Priority> getPriority() {
        return Optional.ofNullable(priority);
    }

    /**
     * Returns true if both persons of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSameActivity(Activity otherActivity) {
        if (otherActivity == this) {
            return true;
        }
        return otherActivity != null
                && otherActivity.getName().equals(getName())
                && (otherActivity.getAddress().equals(getAddress()));
    }

    /**
     * Returns true if both activities have the same identity and data fields.
     * This defines a stronger notion of equality between two activities.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Activity)) {
            return false;
        }

        Activity otherActivity = (Activity) other;
        return otherActivity.getName().equals(getName())
                && otherActivity.getAddress().equals(getAddress())
                && otherActivity.getTags().equals(getTags())
                && otherActivity.getContact().equals(getContact())
                && otherActivity.getDuration().equals(getDuration())
                && otherActivity.getPriority().equals(getPriority());
    }
}
