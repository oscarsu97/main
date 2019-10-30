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
public class Activity extends ItineraryItem implements Comparable<Activity> {

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

    public Duration getDuration() {
        return duration;
    }

    public Priority getPriority() {
        return priority;
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

    @Override
    public int compareTo(Activity o) {
        int thisPriority = priority.priorityValue;
        int thatPriority = o.priority.priorityValue;
        if (thisPriority > 0) {
            if (thatPriority <= 0) {
                return -1;
            } else {
                if (thisPriority - thatPriority > 0) {
                    return 1;
                }
                if (thatPriority - thatPriority < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        } else {
            return 1;
        }
    }
}
