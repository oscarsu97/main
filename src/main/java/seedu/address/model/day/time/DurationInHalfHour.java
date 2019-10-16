package seedu.address.model.day.time;

/**
 * Represents the duration with each unit representing 30 minutes.
 */
public class DurationInHalfHour {
    private final int numberOfHalfHour;

    public DurationInHalfHour(int numberOfHalfHour) {
        this.numberOfHalfHour = numberOfHalfHour;
    }

    public int getNumberOfHalfHour() {
        return numberOfHalfHour;
    }
}